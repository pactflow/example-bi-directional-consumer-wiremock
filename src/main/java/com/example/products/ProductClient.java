package com.example.products;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
        try {
          final ObjectMapper mapper = new ObjectMapper();
          final Product product = mapper.readValue(httpResponse.getEntity().getContent(), Product.class);

          return product;
        } catch (final JsonMappingException e) {
          throw new IOException(e);
        }
      });
  }

  public Product getProduct(final String id) throws IOException {
    return Request.Get(this.url + "/product/" + id)
      .addHeader("Accept", "application/json")
      .execute().handleResponse(httpResponse -> {
        try {
          final ObjectMapper mapper = new ObjectMapper();
          final Product product = mapper.readValue(httpResponse.getEntity().getContent(), Product.class);

          return product;
        } catch (final JsonMappingException e) {
          throw new IOException(e);
        }
      });
  }

  public List<Product> getProducts() throws IOException {
    return Request.Get(this.url + "/products")
      .addHeader("Accept", "application/json")
      .execute().handleResponse(httpResponse -> {
        try {
          final ObjectMapper mapper = new ObjectMapper();
          final List<Product> products = mapper.readValue(httpResponse.getEntity().getContent(), new TypeReference<List<Product>>(){});

          return products;
        } catch (final JsonMappingException e) {
          throw new IOException(e);
        }
      });
    }

    private String toString(final Product p) throws IOException {
      try {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(p);
      } catch (final JsonMappingException e) {
        throw new IOException(e);
      }
  }
}
