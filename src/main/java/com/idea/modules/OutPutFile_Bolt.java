package com.idea.modules;

import com.google.common.base.Joiner;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by poul on 2017/10/23.
 */
public class OutPutFile_Bolt extends BaseBasicBolt {
    private static final Logger logger = LoggerFactory.getLogger(OutPutFile_Bolt.class);
    private static final Joiner joiner = Joiner.on("@#$").useForNull("");
    public void execute(Tuple input, BasicOutputCollector collector) {

        String result = joiner.join(input.getStringByField("code"),
                input.getStringByField("requestTime"),
                input.getStringByField("responseTime"),
                input.getStringByField("locationType"),
                input.getStringByField("phone"),
                input.getStringByField("prvcID"),
                input.getStringByField("channel"),
                input.getStringByField("vice_card_code"),
                input.getStringByField("vice_card_url"),
                input.getStringByField("g3Tg4_code"),
                input.getStringByField("g3Tg4_url"),
                input.getStringByField("big_business_code"),
                input.getStringByField("big_business_url"),
                input.getStringByField("roaming_code"),
                input.getStringByField("roaming_url"),
                input.getStringByField("stock_code"),
                input.getStringByField("music_code"),
                input.getStringByField("music_url"),
                input.getStringByField("home_code"),
                input.getStringByField("home_url"),
                input.getStringByField("video_card_code"),
                input.getStringByField("video_card_url")
        );
        logger.info(result);

    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
