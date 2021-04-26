package com.example.products.pactutils;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(value = Include.NON_NULL)
public class Pact {
  public String consumer;
  public String provider;
  public List<Interaction> interactions = new ArrayList<>();
  public Metadata metadata = new Metadata();
}