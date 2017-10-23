package com.idea.modules;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

/**
 * Created by poul on 2017/10/23.
 */
public class qrqmweb_Spout extends BaseRichSpout {
    private KafkaConsumer kc ;
    private SpoutOutputCollector spoutOutputCollector ;
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declareStream("Line",new Fields("line"));
    }

    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        spoutOutputCollector = collector;
        getKc();
        kc.subscribe(Arrays.asList("qrqmweb"));
    }

    public void nextTuple() {
        ConsumerRecords<String,String> records = kc.poll(1000);
        for(ConsumerRecord record : records){
            spoutOutputCollector.emit("Line",new Values(record.value()+""),System.currentTimeMillis());
        }
    }

    public KafkaConsumer<String, String> getKc() {
        if(kc == null) {
            Properties props = new Properties();
            props.put("bootstrap.servers", "10.0.180.33:9092,10.0.180.34:9092,10.0.180.45:9092");
            props.put("group.id", "group-qrqmweb");
            props.put("enable.auto.commit", "true");
            props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            kc = new KafkaConsumer<String, String>(props);
        }
        return kc;
    }
}
