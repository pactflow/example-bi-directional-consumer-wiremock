package com.example.products;

import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.test.context.TestPropertySource;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.maciejwalkowiak.wiremock.spring.ConfigureWireMock;
import com.maciejwalkowiak.wiremock.spring.EnableWireMock;
import com.maciejwalkowiak.wiremock.spring.WireMockConfigurationCustomizer;

import se.bjurr.wiremockpact.wiremockpactextensionjunit5.WireMockPactExtension;
import se.bjurr.wiremockpact.wiremockpactlib.api.WireMockPactConfig;

@EnableWireMock({
	  @ConfigureWireMock(
	      name = "wiremock-service-name",
	      property = "wiremock.server.url",
	      stubLocation = "wiremock",
	      configurationCustomizers = {WireMockPactBaseTest.class})
	})
@TestPropertySource(locations = "classpath:autotest.properties")
public class WireMockPactBaseTest implements WireMockConfigurationCustomizer {
  @RegisterExtension
  static WireMockPactExtension WIREMOCK_PACT_EXTENSION =
      new WireMockPactExtension(
          WireMockPactConfig.builder() //
              .setConsumerDefaultValue("pactflow-example-bi-directional-consumer-wiremock") //
              .setProviderDefaultValue(System.getenv().getOrDefault("PACT_PROVIDER", "pactflow-example-bi-directional-provider-restassured")) //
              .setPactJsonFolder("build/pacts"));

  @Override
  public void customize(
      final WireMockConfiguration configuration, final ConfigureWireMock options) {
    configuration.extensions(WIREMOCK_PACT_EXTENSION);
  }
}

