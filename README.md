```
https://solr.apache.org/guide/8_4/kerberos-authentication-plugin.html
https://docs.cloudera.com/cdp-private-cloud-base/7.1.3/search-managing/topics/search-config-templates.html?
```

```
beeline -u "jdbc:hive2://ip-10-0-160-242.us-west-1.compute.internal:8443/;ssl=true;sslTrustStore=/etc/pki/java/cacerts;trustStorePassword=changeit;transportMode=http;httpPath=gateway/cdp-proxy-api/hive" -n knox_user -p password
```

```
# Ensure SOLR_ZK_ENSEMBLE is set
source /etc/solr/conf/solr-env.sh

# Check ZooKeeper
zookeeper-client
> get /solr-infra/security.json

kinit -kt /var/run/cloudera-scm-agent/process/235-solr-SOLR_SERVER/solr.keytab solr/ip-10-0-160-242.us-west-1.compute.internal@CLOUDERA.LOCAL

# Design
# A collection has a configuration and a schema. It is essentially a logical index.
# The logical index can be spread across multiple servers in a SolrCloud.
# Zookeeper manages cluster configuration and state for the SolrCloud. 
# An index can be split into one-to-many shards. 
# A replica is a physical copy of a shard running in a core on a server.
# One of these replicas will be designated the leader.
# A core is that part of a server catering to one collection. e.g it can be a JVM running one-to-many shards

# Create a configuration
sudo solrctl config --create logs_config managedTemplate -p immutable=false

# Create a collection
sudo solrctl collection --create logs -s 1 -c logs_config
```


