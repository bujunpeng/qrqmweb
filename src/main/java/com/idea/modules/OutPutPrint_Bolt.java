package com.idea.modules;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

/**
 * Created by poul on 2017/10/25.
 *
 * 此bolt用作本地测试，打印所有流向本bolt的流中的所有字段
 */
public class OutPutPrint_Bolt extends BaseBasicBolt {
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        for (String filed : input.getFields()){
            System.out.print(input.getValueByField(filed)+"====");
        }
        System.out.println();
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
