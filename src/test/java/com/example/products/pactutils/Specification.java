package com.example.products.pactutils;

import lombok.Data;

@Data
public class Specification {
  public String version;
  public Specification(String version) {
    this.version = version;
  }
}