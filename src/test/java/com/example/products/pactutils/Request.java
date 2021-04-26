package com.example.products.pactutils;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(value = Include.NON_NULL)
public class Request {
  public String method;
  public String path;

  @JsonRawValue
  public String body;
  public String query;
  public Map<String,String> headers;
}
