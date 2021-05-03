package com.example.products;

import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class ProductClient {
  private String url;

  public ProductClient setUrl(String url) {
    this.url = url;

    return this;
  }

  public Product createProduct(Product p) throws IOException {
    return (Product) Request.Post(this.url + "/products")
      .addHeader("Accept", "application/json")
      .bodyString(this.toString(p), ContentType.APPLICATION_JSON)
      .execute().handleResponse(httpResponse -> {
        try {
          ObjectMapper mapper = new ObjectMapper();
          Product product = mapper.readValue(httpResponse.getEntity().getContent(), Product.class);

          return product;
        } catch (JsonMappingException e) {
          throw new IOException(e);
        }
      });
  }

  public Product getProduct(String id) throws IOException {
    return (Product) Request.Get(this.url + "/product/" + id)
      .addHeader("Accept", "application/json")
      .execute().handleResponse(httpResponse -> {
        try {
          ObjectMapper mapper = new ObjectMapper();
          Product product = mapper.readValue(httpResponse.getEntity().getContent(), Product.class);

          return product;
        } catch (JsonMappingException e) {
          throw new IOException(e);
        }
      });
  }

  public List<Product> getProducts() throws IOException {
    return (List<Product>) Request.Get(this.url + "/products")
      .addHeader("Accept", "application/json")
      .execute().handleResponse(httpResponse -> {
        try {
          ObjectMapper mapper = new ObjectMapper();
          List<Product> products = mapper.readValue(httpResponse.getEntity().getContent(), new TypeReference<List<Product>>(){});

          return products;
        } catch (JsonMappingException e) {
          throw new IOException(e);
        }
      });
    }

    private String toString(Product p) throws IOException {
      try {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(p);
      } catch (JsonMappingException e) {
        throw new IOException(e);
      }
  }
}
