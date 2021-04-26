package com.example.products.pactutils;

import com.github.tomakehurst.wiremock.admin.model.ListStubMappingsResult;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PactAdapter {
  // public void writePact(ListStubMappingsResult stubs) {
  public void writePact(List<ServeEvent> events) {
    ObjectMapper mapper = new ObjectMapper();
    Pact pact = new Pact();

    try {
      for (ServeEvent e : events) {
        Interaction interaction = new Interaction();

        interaction.setDescription(e.getRequest().getMethod() + "_" + e.getRequest().getUrl() + "_" + e.getId());
        interaction.request.setMethod(e.getRequest().getMethod().toString());
        interaction.request.setPath(e.getRequest().getUrl());

        if (e.getStubMapping().getRequest().getHeaders() != null) {
          interaction.request.headers = new HashMap<>();
          e.getStubMapping().getRequest().getHeaders().forEach((h, v) -> {
            interaction.request.headers.put(h.toString(), v.getExpected());
          });
        }

        if (e.getRequest().getBodyAsString() != null && e.getRequest().getBody().length > 0) {
          System.out.println("Requestbody" + e.getRequest().getBodyAsString());
          interaction.request.setBody(e.getRequest().getBodyAsString());
        }

        if (e.getStubMapping().getResponse().getHeaders() != null) {
          Map<String, String> headers = new HashMap<>();
          e.getStubMapping().getResponse().getHeaders().all().forEach((h) -> {
            headers.put(h.caseInsensitiveKey().toString(), String.join(",", h.values()));
          });
          interaction.response.setHeaders(headers);
        }
        interaction.response.setStatus(e.getStubMapping().getResponse().getStatus());
        if (e.getStubMapping().getResponse().specifiesBodyContent()) {
          System.out.println(e.getStubMapping().getResponse().getBody());
          interaction.response.setBody(e.getStubMapping().getResponse().getBody());
        }

        pact.interactions.add(interaction);
      }

      pact.consumer = "cons";
      pact.provider = "prov";
      mapper.writeValue(new File("/tmp/test.json"), pact);

    } catch (IOException e) {
        e.printStackTrace();
    }
  }
}
