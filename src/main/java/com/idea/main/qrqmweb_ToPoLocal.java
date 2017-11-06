package com.idea.main;

import com.idea.modules.MyKafka_Spout;
import com.idea.modules.OutPutFile_Bolt;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.topology.TopologyBuilder;

 /**
 * Created by poul on 2017/10/23.
  */
public class qrqmweb_ToPoLocal {
    public static void main(String[] args) throws InvalidTopologyException, AuthorizationException, AlreadyAliveException {
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("MyKafka_Spout",new MyKafka_Spout());
//        builder.setBolt("PaseJson_Bolt",new ParseJson_Bolt(),3).shuffleGrouping("MyKafka_Spout");
        builder.setBolt("OutPutFile_Bolt",new OutPutFile_Bolt()).shuffleGrouping("PaseJson_Bolt");
//        builder.setBolt("OutPutMysql_Bolt",new OutPutMysql_Bolt()).shuffleGrouping("PaseJson_Bolt");

        Config conf = new Config();
        conf.setDebug(false);

//        conf.setNumWorkers(1);
//        StormSubmitter.submitTopology("qrqmweb_ToPoLocal", conf, builder.createTopology());

        conf.setMaxTaskParallelism(3);
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("qrqmweb_ToPoLocal",conf,builder.createTopology());


    }
}
