package com.buland.graphql.netflixdgs.springboot.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.opensearch.action.get.GetRequest;
import org.opensearch.action.get.GetResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestClientBuilder;
import org.opensearch.client.RestHighLevelClient;

import com.buland.graphql.netflixdgs.springboot.datatypes.Metric;
import com.buland.graphql.netflixdgs.springboot.datatypes.SearchInput;

public class ESConfigurator implements Configurator {
  RestHighLevelClient client;

  public void load(String metricName) {
    CredentialsProvider credentialsProvider = loadCredentials();

    // Create a client.
    RestClientBuilder builder =
        RestClient.builder(new HttpHost("zgd2wi0ku6vcyp8ooo18.us-west-2.aoss.amazonaws.com", 8080, "https"))
            .setHttpClientConfigCallback(
                new RestClientBuilder.HttpClientConfigCallback() {
                  @Override
                  public HttpAsyncClientBuilder customizeHttpClient(
                      HttpAsyncClientBuilder httpClientBuilder) {
                    return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                  }
                });
    this.client = new RestHighLevelClient(builder);
  }

  public Metric fetchData(SearchInput input) {

    try {
      // Getting back the document
      GetRequest getRequest = new GetRequest("ecomm-index", "1");
      GetResponse response = this.client.get(getRequest, RequestOptions.DEFAULT);
      System.out.println(response.getSourceAsString());

    } catch (Exception e) {
      System.err.println("something went wrong " + e);
    }
    return null;
  }

  CredentialsProvider loadCredentials() {
    // Point to keystore with appropriate certificates for security.
    System.setProperty("javax.net.ssl.trustStore", "/full/path/to/keystore");
    System.setProperty("javax.net.ssl.trustStorePassword", "password-to-keystore");

    // Establish credentials to use basic authentication.
    // Only for demo purposes. Don't specify your credentials in code.
    final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

    credentialsProvider.setCredentials(
        AuthScope.ANY, new UsernamePasswordCredentials("admin", "admin"));
    return credentialsProvider;
  }
}
