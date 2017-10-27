package com.idea.main;

import com.idea.modules.Kafka_Spout;
import com.idea.modules.OutPutFile_Bolt;
import com.idea.modules.ParseJson_Bolt;
import org.apache.storm.Config;
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

import java.util.ArrayList;


/**
 * Created by poul on 2017/10/24.
 */
public class qrqmweb_ToPo {
    public static void main(String[] args) throws InvalidTopologyException, AuthorizationException, AlreadyAliveException {
       /* RecordFormat format = new DelimitedRecordFormat().withFieldDelimiter(" : ");
        SyncPolicy syncPolicy = new CountSyncPolicy(10);
        FileRotationPolicy rotationPolicy = new TimedRotationPolicy(1.0f, TimedRotationPolicy.TimeUnit.MINUTES);
        FileNameFormat fileNameFormat = new DefaultFileNameFormat().withPath("/user/dianqu/private/ods/test4/").withPrefix("app_").withExtension(".log");

        HdfsBolt hdfs_Bolt = new HdfsBolt()
                .withFsUrl("hdfs://ns1")
                .withFileNameFormat(fileNameFormat)
                .withRecordFormat(format)
                .withRotationPolicy(rotationPolicy)
                .withSyncPolicy(syncPolicy);*/

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("Kafka_Spout", new Kafka_Spout());
        builder.setBolt("PaseJson_Bolt", new ParseJson_Bolt(), 3).shuffleGrouping("Kafka_Spout");
        builder.setBolt("OutPutFile_Bolt",new OutPutFile_Bolt()).shuffleGrouping("PaseJson_Bolt");
//        builder.setBolt("hdfs_Bolt", hdfs_Bolt).shuffleGrouping("PaseJson_Bolt");

        Config conf = new Config();
//        ArrayList<String> list = new ArrayList<>();
//        list.add("10.0.180.33");
//        conf.put(Config.NIMBUS_SEEDS,list);
        conf.setNumWorkers(1);
        String name = qrqmweb_ToPo.class.getSimpleName();
        StormSubmitter.submitTopologyWithProgressBar(name, conf, builder.createTopology());

    }

}
