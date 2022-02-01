package com.cloudera.pse.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient.Builder;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.MapSolrParams;
import org.apache.solr.common.SolrDocumentList;

public class SolrQuery
{
  private static CloudSolrClient getSolrClient()
  {
    final List<String> zkServers = new ArrayList<>();
    zkServers.add("ip-10-0-160-242.us-west-1.compute.internal:2181/solr-infra");
    return new CloudSolrClient.Builder(zkServers, Optional.empty()).build();
  }

  public static void main(String[] args)
  {
    try
    {
      final CloudSolrClient client = getSolrClient();

      final Map<String, String> qMap = new HashMap<String, String>();
      qMap.put("q", "course:Biology");
      MapSolrParams parameters = new MapSolrParams(qMap);

      final QueryResponse resp = client.query("logs", parameters);
      final SolrDocumentList docs = resp.getResults();

      System.out.println("Found " + docs.getNumFound() + " documents");
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }
}

