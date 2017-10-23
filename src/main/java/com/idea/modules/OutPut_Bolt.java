package com.idea.modules;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

/**
 * Created by poul on 2017/10/23.
 */
public class OutPut_Bolt extends BaseBasicBolt {
    public void execute(Tuple input, BasicOutputCollector collector) {
        for (String filed : input.getFields()){
            System.out.print(input.getValueByField(filed)+"====");
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
