package com.idea.main;

import com.idea.utils.MyMoveFileAction;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.hdfs.bolt.HdfsBolt;
import org.apache.storm.hdfs.bolt.format.DefaultFileNameFormat;
import org.apache.storm.hdfs.bolt.format.DelimitedRecordFormat;
import org.apache.storm.hdfs.bolt.format.FileNameFormat;
import org.apache.storm.hdfs.bolt.format.RecordFormat;
import org.apache.storm.hdfs.bolt.rotation.FileRotationPolicy;
import org.apache.storm.hdfs.bolt.rotation.FileSizeRotationPolicy;
import org.apache.storm.hdfs.bolt.rotation.TimedRotationPolicy;
import org.apache.storm.hdfs.bolt.sync.CountSyncPolicy;
import org.apache.storm.hdfs.bolt.sync.SyncPolicy;
import org.apache.storm.hdfs.common.rotation.MoveFileAction;
import org.apache.storm.shade.org.yaml.snakeyaml.Yaml;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by poul on 2017/10/31.
 */
public class HDFSToPo {
    static final String SENTENCE_SPOUT_ID = "sentence-spout";
    static final String BOLT_ID = "my-bolt";
    static final String TOPOLOGY_NAME = "test-topology";

    public static void main(String[] args) throws Exception {
        Config config = new Config();

        SentenceSpout spout = new SentenceSpout();

        // sync the filesystem after every 1k tuples
        SyncPolicy syncPolicy = new CountSyncPolicy(1000);

        // rotate files when they reach 5MB
        FileRotationPolicy rotationPolicy = new TimedRotationPolicy(1.0f, TimedRotationPolicy.TimeUnit.MINUTES);
        FileRotationPolicy rotationPolicy1 = new FileSizeRotationPolicy(128.0f, FileSizeRotationPolicy.Units.MB);

        FileNameFormat fileNameFormat = new DefaultFileNameFormat()
                .withPath("/user/dianqu/private/test/stormhdfs1/")
                .withPrefix("stormhdfs-")
                .withExtension(".txt");

        // use "|" instead of "," for field delimiter
        RecordFormat format = new DelimitedRecordFormat()
                .withFieldDelimiter("\t");

        Yaml yaml = new Yaml();
        InputStream in = new FileInputStream(System.getProperty("qrqmweb_HOME")+"/conf/hdfs.yaml");
        Map<String, Object> yamlConf = (Map<String, Object>) yaml.load(in);
        in.close();
        config.put("hdfs.config", yamlConf);

        HdfsBolt bolt = new HdfsBolt()
                .withConfigKey("hdfs.config")
                .withFsUrl("hdfs://10.0.180.3:8020")//hdfs://10.0.180.2:8020
                .withFileNameFormat(fileNameFormat)
                .withRecordFormat(format)
                .withRotationPolicy(rotationPolicy1)
                .withSyncPolicy(syncPolicy)
                .addRotationAction(new MyMoveFileAction().toDestination("/user/dianqu/private/test/stormhdfs2/"));

        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout(SENTENCE_SPOUT_ID, spout, 1);
        // SentenceSpout --> MyBolt
        builder.setBolt(BOLT_ID, bolt, 1).shuffleGrouping(SENTENCE_SPOUT_ID);
        String topoName = TOPOLOGY_NAME;
        /*if (args.length == 3) {
            topoName = args[2];
        } else if (args.length > 3) {
            System.out.println("Usage: HdfsFileTopology [hdfs url] [hdfs yaml config file] <topology name>");
            return;
        }*/

//        config.setNumWorkers(1);
        config.setMaxTaskParallelism(3);
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology(topoName, config, builder.createTopology());
    }

    public static class SentenceSpout extends BaseRichSpout {
        private ConcurrentHashMap<UUID, Values> pending;
        private SpoutOutputCollector collector;
        private String[] sentences = {
                "my dog has fleas",
                "i like cold beverages",
                "the dog ate my homework",
                "don't have a cow man",
                "i don't think i like fleas"
        };
        private int index = 0;
        private int count = 0;
        private long total = 0L;

        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("sentence", "timestamp"));
        }

        public void open(Map config, TopologyContext context,
                         SpoutOutputCollector collector) {
            this.collector = collector;
            this.pending = new ConcurrentHashMap<UUID, Values>();
        }


        public void nextTuple() {
            Values values = new Values(sentences[index], System.currentTimeMillis());
            UUID msgId = UUID.randomUUID();
            this.pending.put(msgId, values);
            this.collector.emit(values, msgId);
            index++;
            if (index >= sentences.length) {
                index = 0;
            }
            count++;
            total++;
            if (count > 20000) {
                count = 0;
                System.out.println("Pending count: " + this.pending.size() + ", total: " + this.total);
            }
            Thread.yield();
        }

        public void ack(Object msgId) {
            this.pending.remove(msgId);
        }

        public void fail(Object msgId) {
            System.out.println("**** RESENDING FAILED TUPLE");
            this.collector.emit(this.pending.get(msgId), msgId);
        }
    }
    public static class MyBolt extends BaseRichBolt {

        private HashMap<String, Long> counts = null;
        private OutputCollector collector;

        public void prepare(Map config, TopologyContext context, OutputCollector collector) {
            this.counts = new HashMap<String, Long>();
            this.collector = collector;
        }

        public void execute(Tuple tuple) {
            collector.ack(tuple);
        }

        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            // this bolt does not emit anything
        }

        @Override
        public void cleanup() {
        }
    }
}
