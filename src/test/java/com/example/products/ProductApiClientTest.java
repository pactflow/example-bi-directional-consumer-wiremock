package com.example.products;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.matching.ContainsPattern;
import com.maciejwalkowiak.wiremock.spring.InjectWireMock;

@SpringBootTest
@AutoConfigureMockMvc
class ProductApiClientTest extends WireMockPactBaseTest {
    @InjectWireMock("wiremock-service-name")
    private WireMockServer wiremock;
	
	@Autowired
	private ProductClient productClient;

	@Test
	void getProduct() throws IOException {

		// Arrange
		this.wiremock.stubFor(WireMock.get(WireMock.urlEqualTo("/product/10"))
				.willReturn(aResponse().withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
						.withBody("{ \"name\": \"pizza\", \"id\": \"10\", \"type\": \"food\" }")));

		// Act
		final Product product = this.productClient.getProduct("10");

		// Assert
		assertThat(product.getId(), is("10"));
	}

	@Test
	void createProduct() throws IOException {

		final String productJson = "{ \"id\": \"27\", \"name\": \"pizza\", \"type\": \"food\" }";
		this.wiremock.stubFor(
				WireMock.post(WireMock.urlEqualTo("/products")).withRequestBody(equalToJson(productJson, true, true))
						.withHeader("Content-Type", new ContainsPattern("application/json")).willReturn(aResponse()
								.withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE).withBody(productJson)));

		final Product product = this.productClient.createProduct(new Product("27", "pizza", "food", 27.0));
		assertThat(product.getId(), is("27"));
	}

	@Test
	void getProducts() throws IOException {

		this.wiremock.stubFor(WireMock.get(WireMock.urlEqualTo("/products"))
				.willReturn(aResponse().withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
						.withBody("[{ \"name\": \"pizza\", \"id\": \"10\", \"type\": \"food\", \"price\":  100 }]")));

		final List<Product> products = this.productClient.getProducts();
		assertThat(products.get(0).getId(), is("10"));
	}
}