package com.idea.modules;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.Iterator;

/**
 * Created by poul on 2017/10/23.
 */
public class ParseJson_Bolt extends BaseBasicBolt{
    String code = "";
    String requestTime = "";
    String responseTime = "";
    String locationType = "";
    String phone = "";
    String prvcID = "";
    String channel = "";

    String vice_card_code = "0";
    String vice_card_url = "";
    String g3Tg4_code = "0";
    String g3Tg4_url = "";
    String big_business_code = "0";
    String big_business_url = "";
    String roaming_code = "0";
    String roaming_url = "";
    String stock_code = "0";
    String stock_url = "";
    String music_code = "0";
    String music_url = "";
    String home_code = "0";
    String home_url = "";
    String video_card_code = "0";
    String video_card_url = "";
    public void execute(Tuple input, BasicOutputCollector collector) {
        String line = input.getStringByField("line");
        parseJson(line);
        collector.emit("Fileds",new Values(code,requestTime,responseTime,locationType,phone,prvcID,channel,
                vice_card_code,vice_card_url,g3Tg4_code,g3Tg4_url,big_business_code,big_business_url,
                roaming_code,roaming_url,stock_code,stock_url,music_code,music_url,home_code,
                home_url,video_card_code,video_card_url));
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declareStream("Fileds",new Fields("code","requestTime","responseTime","locationType","phone","prvcID","channel",
                "vice_card_code","vice_card_url","g3Tg4_code","g3Tg4_url","big_business_code","big_business_url",
                "roaming_code","roaming_url","stock_code","stock_url","music_code","music_url","home_code",
                "home_url","video_card_code","video_card_url"));
    }
    public void parseJson(String jsondata){
        try{
            JsonObject jsondataobject =  new JsonParser().parse(jsondata).getAsJsonObject();
            requestTime = jsondataobject.get("requestTime").getAsString();
            responseTime = jsondataobject.get("responseTime").getAsString();
            JsonObject requestBody = jsondataobject.get("requestBody").getAsJsonObject();
            JsonObject responseBody = jsondataobject.get("responseBody").getAsJsonObject();

            locationType = requestBody.get("locationType").getAsString();
            phone = requestBody.get("phone").getAsString();
            prvcID = requestBody.get("prvcID").getAsString();
            channel = requestBody.get("channel").getAsString();

            code = responseBody.get("code").getAsString();
            if("0".equals(code)){
                JsonObject dataObject = responseBody.get("dataObject").getAsJsonObject();
                JsonArray webIndexData = dataObject.get("webIndexData").getAsJsonArray();
                Iterator iter = webIndexData.iterator();
                while(iter.hasNext()) {
                    JsonElement element = (JsonElement)iter.next();
                    JsonObject obj = element.getAsJsonObject();
                    String miniurl = obj.get("link").getAsString();
                    String minititle = obj.get("title").getAsString();
                    if ("3升4业务".equals(minititle)){
                        g3Tg4_url=miniurl;
                    }else if("乐享家副卡业务".equals(minititle)){
                        home_url=miniurl;
                    }else if("国际漫游包".equals(minititle)){
                        roaming_url=miniurl;
                    }else if("大三元业务".equals(minititle)){
                        big_business_url=miniurl;
                    }else if("炒股定向流量包".equals(minititle)){
                        stock_url=miniurl;
                    }else if("音乐定向流量包".equals(minititle)){
                        music_url=miniurl;
                    }else if("副卡业务".equals(minititle)){
                        vice_card_url=miniurl;
                    }else if("视频定向流量包".equals(minititle)){
                        video_card_url=miniurl;
                    }
                }

            }
        }catch(Exception e){}
    }
}
