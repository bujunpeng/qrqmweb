package com.idea.modules;

import com.idea.config.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

/**
 * Created by poul on 2017/10/23.
 */
public class qrqmweb_Spout extends BaseRichSpout {
    private Properties prop;
    private KafkaConsumer kc;
    private SpoutOutputCollector spoutOutputCollector;

    public qrqmweb_Spout() throws IOException {
        prop = new Properties();
        prop.load(new FileInputStream("/consumer.properties"));

    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declareStream("Line", new Fields("line"));
    }

    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        spoutOutputCollector = collector;
        getKc();
        kc.subscribe(Arrays.asList(prop.getProperty(ConsumerConfig.TOPICS)));
    }

    public void nextTuple() {
        ConsumerRecords<String, String> records = kc.poll(1000);
        for (ConsumerRecord record : records) {
            spoutOutputCollector.emit("Line", new Values(record.value() + ""), System.currentTimeMillis());
        }
    }

    public KafkaConsumer<String, String> getKc() {
        if (kc == null) {
            Properties props = new Properties();
            props.put(ConsumerConfig.BOOTSTRAP_SERVERS, prop.getProperty(ConsumerConfig.BOOTSTRAP_SERVERS));
            props.put(ConsumerConfig.GROUP_ID, prop.getProperty(ConsumerConfig.GROUP_ID));
            props.put(ConsumerConfig.ENABLE_AUTO_COMMIT, prop.getProperty(ConsumerConfig.ENABLE_AUTO_COMMIT));
            props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            kc = new KafkaConsumer<String, String>(props);
        }
        return kc;
    }
}
