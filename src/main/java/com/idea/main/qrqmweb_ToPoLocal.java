package com.idea.main;

import com.idea.modules.Mysql_Bolt;
import com.idea.modules.OutPut_Bolt;
import com.idea.modules.ParseJson_Bolt;
import com.idea.modules.qrqmweb_Spout;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;

/**
 * Created by poul on 2017/10/23.
 */
public class qrqmweb_ToPoLocal {
    public static void main(String[] args) {
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("qrqmweb_Spout",new qrqmweb_Spout(),3);
        builder.setBolt("paseJson_Bolt",new ParseJson_Bolt(),3).shuffleGrouping("qrqmweb_Spout","Line");
        builder.setBolt("outPut_Bolt",new OutPut_Bolt()).shuffleGrouping("paseJson_Bolt","Fileds");
        builder.setBolt("mysql_Bolt",new Mysql_Bolt()).shuffleGrouping("paseJson_Bolt","Fileds");

        Config conf = new Config();
        conf.setDebug(false);
        conf.setMaxTaskParallelism(3);

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("qrqmweb_ToPoLocal",conf,builder.createTopology());


    }
}
