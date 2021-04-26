package com.example.products.pactutils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(value = Include.NON_NULL)
public class Interaction {
  public String description;
  public Request request = new Request();
  public Response response = new Response();
}
