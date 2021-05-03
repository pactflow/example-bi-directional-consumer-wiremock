package com.example.products;

import com.example.products.pactutils.PactAdapter;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.admin.model.ListStubMappingsResult;
import com.github.tomakehurst.wiremock.client.WireMock;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.io.IOException;
import java.util.List;

@SpringBootTest
@ContextConfiguration(initializers = { WireMockInitialiser.class })
@TestInstance(Lifecycle.PER_CLASS)
class ProductsPactTest {
  @Autowired
  private WireMockServer wireMockServer;

  @BeforeAll
  void clearMappings() {
    File index = new File("src/test/resources/mappings");
    String[]entries = index.list();
    for(String s: entries){
        File currentFile = new File(index.getPath(),s);
        currentFile.delete();
    }
  }

  @AfterAll
  void writeStubsToFile() {
    // Option 2: Use the Java representation to create a pact file
    PactAdapter adapter = new PactAdapter();
    adapter.writePact(wireMockServer.getAllServeEvents());

    // Option 2: Serialise the wiremock stubs to file, and convert to a Pact file
    //
    // I think writing to file this way is actually better.
    // We skip many of the internal representations and having to interpret behaviour
    //
    // The downsides to this are:
    // 1. You get the serialised mappings in JSON (so where there are multiple alternatives, they would need to be expanded)
    // 2. This also means of course that we could also perform the transformation on upload to Pactflow
    wireMockServer.saveMappings();
  }

  @Test
  void getProduct() throws IOException {

    wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/product/10")).willReturn(
        aResponse().withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE).withBody("{ \"name\": \"pizza\", \"id\": 10, \"type\": \"food\" }")));

    Product product = new ProductClient().setUrl(wireMockServer.baseUrl()).getProduct("10");
    assertThat(product.getId(), is("10"));
  }

  @Test
  void createProduct() throws IOException {

    String productJson = "{ \"id\": \"27\", \"name\": \"pizza\", \"type\": \"food\" }";
    wireMockServer.stubFor(WireMock.post(WireMock.urlEqualTo("/products")).withRequestBody(equalToJson(productJson, true, true)).willReturn(
        aResponse().withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE).withBody(productJson)));

    Product product = new ProductClient().setUrl(wireMockServer.baseUrl()).createProduct(new Product("27", "pizza", "food"));
    assertThat(product.getId(), is("27"));
  }

  @Test
  void getProducts() throws IOException {

    wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/products")).willReturn(
        aResponse().withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE).withBody("[{ \"name\": \"pizza\", \"id\": 10, \"type\": \"food\" }]")));

    List<Product> products = new ProductClient().setUrl(wireMockServer.baseUrl()).getProducts();
    assertThat(products.get(0).getId(), is("10"));
  }
}