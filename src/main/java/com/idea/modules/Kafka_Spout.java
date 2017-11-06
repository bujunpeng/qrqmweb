package com.idea.modules;


import org.apache.storm.kafka.BrokerHosts;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.kafka.SpoutConfig;
import org.apache.storm.kafka.ZkHosts;

/**
 * Created by poul on 2017/10/30.
 */
public class Kafka_Spout {

    private Kafka_Spout(){}

    public static KafkaSpout getKafkaSpout(){

    /**
        此处为kafka的broker在zk中的配置
         *  brokerZkStr：zk的地址
         *  brokerZkPath：kafka的broker在zk中的路径
         *  如果broker位置是/kafka/brokers中，则可以使用：
         *  public ZkHosts("123.58.172.117:2181,123.58.172.98:2181,123.58.172.111:2181,123.58.172.114:2181,123.58.172.116:2181", "/kafka")
         * 或者直接：
         *  new ZkHosts("123.58.172.117:2181,123.58.172.98:2181,123.58.172.111:2181,123.58.172.114:2181,123.58.172.116:2181/kafka")
         *  默认情况下，每60秒去读取一次kafka的分区信息，可以通过修改host.refreshFreqSecs来设置。
         *
         *
         *  除了使用ZkHosts来指定kafka的broker，还可以使用静态指定：StaticHosts
         * Broker brokerForPartition0 = new Broker("localhost");//localhost:9092
         * Broker brokerForPartition1 = new Broker("localhost", 9092);//localhost:9092 but we specified the port explicitly
         * Broker brokerForPartition2 = new Broker("localhost:9092");//localhost:9092 specified as one string.
         * GlobalPartitionInformation partitionInfo = new GlobalPartitionInformation();
         * partitionInfo.addPartition(0, brokerForPartition0);//mapping form partition 0 to brokerForPartition0
         * partitionInfo.addPartition(1, brokerForPartition1);//mapping form partition 1 to brokerForPartition1
         * partitionInfo.addPartition(2, brokerForPartition2);//mapping form partition 2 to brokerForPartition2
         * BrokerHosts brokerHosts = new StaticHosts(partitionInfo);
         *
         */
        String brokerZkStr = "h1:2181,h2:2181,h3:2181";
        String brokerZkPath = "/brokers"; // e.g., /kafka/brokers
        BrokerHosts brokerHosts = new ZkHosts(brokerZkStr,brokerZkPath);

        String topic = null;

        /*以下2个目录确定offset在zk中存储的位置*/
        String zkRoot = "/storm";//此处是storm在zk中的位置   consumer在zk中的的目录？
        String id = null;  //consumer的唯一标示？

        SpoutConfig spoutConfig = new SpoutConfig(brokerHosts,topic,zkRoot,id);
        return new KafkaSpout(spoutConfig);
    }
}
