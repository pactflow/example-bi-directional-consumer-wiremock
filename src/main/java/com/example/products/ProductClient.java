package com.example.products;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@Service
public class ProductClient {
  private final String url;

  public ProductClient(@Value("${basepath}") final String url) {
    this.url = url;
  }

  public Product createProduct(final Product p) throws IOException {
    return Request.Post(this.url + "/products")
      .addHeader("Accept", "application/json")
      .bodyString(this.toString(p), ContentType.APPLICATION_JSON)
      .execute().handleResponse(httpResponse -> {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(httpResponse.getEntity().getContent(), Product.class);
      });
  }

  public Product getProduct(final String id) throws IOException {
    return Request.Get(this.url + "/product/" + id)
      .addHeader("Accept", "application/json")
      .execute().handleResponse(httpResponse -> {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(httpResponse.getEntity().getContent(), Product.class);
      });
  }

  public List<Product> getProducts() throws IOException {
    return Request.Get(this.url + "/products")
      .addHeader("Accept", "application/json")
      .execute().handleResponse(httpResponse -> {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(httpResponse.getEntity().getContent(), new TypeReference<List<Product>>(){});
      });
  }

  private String toString(final Product p) throws IOException {
    final ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(p);
  }
}
