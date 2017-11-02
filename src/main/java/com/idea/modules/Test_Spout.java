/*package com.idea.modules;

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

public class Test_Spout extends BaseRichSpout {
    private SpoutOutputCollector spoutOutputCollector ;
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("line"));
    }

    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        spoutOutputCollector = collector;
    }

    public void nextTuple() {
        String line = "{\"requestTime\":\"2017-10-27 13:25:41.980\",\"responseTime\":\"2017-10-27 13:25:42.150\",\"requestBody\":{\"locationType\":2,\"phone\":\"18028001510\",\"prvcID\":\"01\",\"channel\":\"WEB\"},\"responseBody\":{\"code\":0,\"dataObject\":{\"myGoData\":[{\"iconUrl\":\"http://image1.chinatelecom-ec.com/images/2017/9/12/000000008407A181CBBA42A28FED2A3E2A40F6AF.png\",\"imageUrl\":\"http://image1.chinatelecom-ec.com/images/2017/9/12/000000008407A181CBBA42A28FED2A3E2A40F6AF.png\",\"link\":\"http://www.189.cn/products/19736193019.html\",\"title\":\"炒股定向流量包\",\"introduction\":\"\",\"order\":6},{\"iconUrl\":\"http://image1.chinatelecom-ec.com/images/2017/9/12/00000000D073F40A53AE40078926241929490C71.png\",\"imageUrl\":\"http://image1.chinatelecom-ec.com/images/2017/9/12/00000000D073F40A53AE40078926241929490C71.png\",\"link\":\"http://www.189.cn/products/19736152616.html\",\"title\":\"音乐定向流量包\",\"introduction\":\"\",\"order\":7}]}}}";
        spoutOutputCollector.emit(new Values(line));
//        try {
//            Thread.sleep(50);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

}
*/