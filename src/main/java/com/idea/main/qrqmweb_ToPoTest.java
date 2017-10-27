package com.idea.main;

import com.idea.modules.*;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.topology.TopologyBuilder;

/**
 * Created by poul on 2017/10/23.
 */
public class qrqmweb_ToPoTest {
    public static void main(String[] args) throws InvalidTopologyException, AuthorizationException, AlreadyAliveException {
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("Test_Spout",new Test_Spout());
        builder.setBolt("PaseJson_Bolt",new ParseJson_Bolt()).shuffleGrouping("Test_Spout");
        builder.setBolt("OutPutFile_Bolt",new OutPutFile_Bolt()).shuffleGrouping("PaseJson_Bolt");
//        builder.setBolt("OutPutPrint_Bolt",new OutPutPrint_Bolt()).shuffleGrouping("PaseJson_Bolt");
//        builder.setBolt("OutPutMysql_Bolt",new OutPutMysql_Bolt()).shuffleGrouping("PaseJson_Bolt");

        Config conf = new Config();
        conf.setDebug(false);

        conf.setNumWorkers(1);
        StormSubmitter.submitTopology("qrqmweb_ToPoTest", conf, builder.createTopology());
//        conf.setMaxTaskParallelism(3);
//        LocalCluster cluster = new LocalCluster();
//        cluster.submitTopology("qrqmweb_ToPoTest",conf,builder.createTopology());

    }
}
