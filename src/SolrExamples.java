package com.cloudera.pse.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient.Builder;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.params.MapSolrParams;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

public class SolrExamples
{
  private static CloudSolrClient getSolrClient(String url)
  {
    final List<String> zkServers = new ArrayList<>();
    zkServers.add(url);
    return new CloudSolrClient.Builder(zkServers, Optional.empty()).build();
  }

  private static void queryLogsCollection(CloudSolrClient client) throws Exception
  {
    final Map<String, String> qMap = new HashMap<String, String>();
    qMap.put("q", "course:Biology");
    MapSolrParams parameters = new MapSolrParams(qMap);

    final QueryResponse resp = client.query("logs", parameters);
    final SolrDocumentList docs = resp.getResults();

    System.out.println("Found " + docs.getNumFound() + " documents");
  }

  private static void postEntitiesCollection(CloudSolrClient client) throws Exception
  {
    final SolrInputDocument doc = new SolrInputDocument();
    doc.addField("id", UUID.randomUUID().toString());
    doc.addField("character", "Jon Snow");

    final UpdateResponse updateResponse = client.add("entities", doc);
    client.commit("entities");
  }

  private static void queryEntitiesCollection(CloudSolrClient client) throws Exception
  {
    SolrQuery q = new SolrQuery("*:*");
    q.setRows(0);
    System.out.print(client.query("entities", q).getResults().getNumFound());
  }

  public static void main(String[] args)
  {
    try
    {
      String url = "ip-10-0-160-242.us-west-1.compute.internal:2181/solr";
      final CloudSolrClient client = getSolrClient(url);
      postEntitiesCollection(client);
      queryEntitiesCollection(client);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }
}
