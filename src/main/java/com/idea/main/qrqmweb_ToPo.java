package com.idea.main;

import com.idea.modules.ParseJson_Bolt;
import com.idea.modules.Kafka_Spout;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.hdfs.bolt.HdfsBolt;
import org.apache.storm.hdfs.bolt.format.DefaultFileNameFormat;
import org.apache.storm.hdfs.bolt.format.DelimitedRecordFormat;
import org.apache.storm.hdfs.bolt.format.FileNameFormat;
import org.apache.storm.hdfs.bolt.format.RecordFormat;
import org.apache.storm.hdfs.bolt.rotation.FileRotationPolicy;
import org.apache.storm.hdfs.bolt.rotation.TimedRotationPolicy;
import org.apache.storm.hdfs.bolt.sync.CountSyncPolicy;
import org.apache.storm.hdfs.bolt.sync.SyncPolicy;
import org.apache.storm.topology.TopologyBuilder;


/**
 * Created by poul on 2017/10/24.
 */
public class qrqmweb_ToPo {
    public static void main(String[] args) throws InvalidTopologyException, AuthorizationException, AlreadyAliveException {
        RecordFormat format = new DelimitedRecordFormat().withFieldDelimiter(" : ");
        SyncPolicy syncPolicy = new CountSyncPolicy(10);
        FileRotationPolicy rotationPolicy = new TimedRotationPolicy(1.0f, TimedRotationPolicy.TimeUnit.MINUTES);
        FileNameFormat fileNameFormat = new DefaultFileNameFormat().withPath("/storm/").withPrefix("app_").withExtension(".log");

        HdfsBolt hdfs_Bolt = new HdfsBolt()
                .withFsUrl("hdfs://192.168.1.68:9000")
                .withFileNameFormat(fileNameFormat)
                .withRecordFormat(format)
                .withRotationPolicy(rotationPolicy)
                .withSyncPolicy(syncPolicy);


        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("Kafka_Spout", new Kafka_Spout());
        builder.setBolt("paseJson_Bolt",new ParseJson_Bolt(),3).shuffleGrouping("Kafka_Spout", "Line");
        builder.setBolt("hdfs_Bolt", hdfs_Bolt).shuffleGrouping("paseJson_Bolt", "Fileds");

        Config conf = new Config();

        String name = qrqmweb_ToPo.class.getSimpleName();
        if (args != null && args.length > 0) {
            conf.put(Config.NIMBUS_HOST, args[0]);
            conf.setNumWorkers(3);
            StormSubmitter.submitTopologyWithProgressBar(name, conf, builder.createTopology());
        } else {
            conf.setMaxTaskParallelism(3);
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology(name, conf, builder.createTopology());
//             Thread.sleep(60000);
//             cluster.shutdown();
        }
    }

}
