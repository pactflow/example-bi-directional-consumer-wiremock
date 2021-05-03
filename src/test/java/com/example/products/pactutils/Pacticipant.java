package com.example.products.pactutils;

import lombok.Data;

@Data
public class Pacticipant {
  public Pacticipant(String name) {
    this.name = name;
  }

  public String name;
}
