```
beeline -u "jdbc:hive2://ip-10-0-160-242.us-west-1.compute.internal:8443/;ssl=true;sslTrustStore=/etc/pki/java/cacerts;trustStorePassword=changeit;transportMode=http;httpPath=gateway/cdp-proxy-api/hive" -n knox_user -p password
```
## Ensure SOLR_ZK_ENSEMBLE is set
```
source /etc/solr/conf/solr-env.sh

zookeeper-client
> get /solr-infra/security.json
```

## Design
```
A collection has a configuration and a schema. It is essentially a logical index.
The logical index can be spread across multiple servers in a SolrCloud.
Zookeeper manages cluster configuration and state for the SolrCloud. 
An index can be split into one-to-many shards. 
A replica is a physical copy of a shard running in a core on a server.
One of these replicas will be designated the leader.
A core is that part of a server catering to one collection. e.g it can be a JVM running one-to-many shards
```

## Get a Kerberos ticket for Solr
```
kinit -kt /var/run/cloudera-scm-agent/process/235-solr-SOLR_SERVER/solr.keytab solr/ip-10-0-160-242.us-west-1.compute.internal@CLOUDERA.LOCAL
```
## Create a configuration
```
sudo solrctl config --create logs_config managedTemplate -p immutable=false
```
## Create a collection
```
sudo solrctl collection --create logs -s 1 -c logs_config
```
## View the instance directory created
```
sudo solrctl instancedir --list
...
logs_config
...
```
## View the location in HDFS
```
sudo hdfs dfs -ls /solr-infra/logs/core_node2/data
drwxr-xr-x   - solr solr          0 2022-01-30 23:52 /solr-infra/logs/core_node2/data/index
drwxr-xr-x   - solr solr          0 2022-01-30 23:52 /solr-infra/logs/core_node2/data/snapshot_metadata
drwxr-xr-x   - solr solr          0 2022-01-30 23:52 /solr-infra/logs/core_node2/data/tlog
```
## Add a document to a collection
```
sudo curl -k --negotiate -u : 'https://ip-10-0-160-242.us-west-1.compute.internal:8995/api/collections/logs/update'\
'?split=/exams'\
'&f=first:/first'\
'&f=last:/last'\
'&f=grade:/grade'\
'&f=course:/exams/course'\
'&f=test:/exams/test'\
'&f=score:/exams/score'\
 -H 'Content-type:application/json' -d '
{
  "first": "John",
  "last": "Doe",
  "grade": 8,
  "exams": [
    {
      "course" : "Precalculus",
      "test"   : "term1",
      "score"  : 90},
    {
      "course" : "Biology",
      "test"   : "term1",
      "score"  : 86}
  ]
}'

  "responseHeader":{
    "rf":1,
    "status":0,
    "QTime":174}}
```
## Query the collection
```
sudo curl -k --negotiate -u : 'https://ip-10-0-160-242.us-west-1.compute.internal:8995/api/collections/logs/select'\
'?q=course:Biology' -H 'Content-type:application/json'

{
  "responseHeader":{
    "zkConnected":true,
    "status":0,
    "QTime":0,
    "params":{
      "q":"course:Biology",
      "json":""}},
  "response":{"numFound":1,"start":0,"docs":[
      {
        "first":["John"],
        "last":["Doe"],
        "grade":[8],
        "course":["Biology"],
        "test":["term1"],
        "score":[86],
        "id":"4bcecab6-61cb-40ae-9620-9f1ddd62a0f0",
        "_version_":1723506523821309952}]
  }}
```

## References
```
https://docs.cloudera.com/cdp-private-cloud-base/7.1.6/security-how-to-guides/topics/cm-security-browser-access-kerberos-protected-url.html
https://solr.apache.org/guide/8_4/kerberos-authentication-plugin.html
https://solr.apache.org/docs/8_4_0/solr-solrj/org/apache/solr/client/solrj/impl/Http2SolrClient.Builder.html
https://solr.apache.org/docs/8_4_0/solr-solrj/org/apache/solr/client/solrj/embedded/SSLConfig.html

```
