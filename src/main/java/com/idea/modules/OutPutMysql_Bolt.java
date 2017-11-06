package com.idea.modules;

import com.idea.config.MysqlConfig;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

 /**
 * Created by poul on 2017/10/23.
 */
public class OutPutMysql_Bolt extends BaseRichBolt {

    private Properties prop;
    private OutputCollector collector;
    private Connection conn;
    private PreparedStatement pstm;
    private String sql = "INSERT INTO strom_qrqmweb VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    int i = 0;

    public OutPutMysql_Bolt() throws IOException {
        prop = new Properties();
        prop.load(new FileInputStream("/consumer.properties"));

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer arg0) {
    }


    @Override
    public void execute(Tuple tuple) {

        try {
            pstm.setString(1, tuple.getStringByField("code"));
            pstm.setString(2, tuple.getStringByField("requestTime"));
            pstm.setString(3, tuple.getStringByField("responseTime"));
            pstm.setString(4, tuple.getStringByField("locationType"));
            pstm.setString(5, tuple.getStringByField("phone"));
            pstm.setString(6, tuple.getStringByField("prvcID"));
            pstm.setString(7, tuple.getStringByField("channel"));
            pstm.setString(8, tuple.getStringByField("vice_card_code"));
            pstm.setString(9, tuple.getStringByField("vice_card_url"));
            pstm.setString(10, tuple.getStringByField("g3Tg4_code"));
            pstm.setString(11, tuple.getStringByField("g3Tg4_url"));
            pstm.setString(12, tuple.getStringByField("big_business_code"));
            pstm.setString(13, tuple.getStringByField("big_business_url"));
            pstm.setString(14, tuple.getStringByField("roaming_code"));
            pstm.setString(15, tuple.getStringByField("roaming_url"));
            pstm.setString(16, tuple.getStringByField("stock_code"));
            pstm.setString(17, tuple.getStringByField("stock_url"));
            pstm.setString(18, tuple.getStringByField("music_code"));
            pstm.setString(19, tuple.getStringByField("music_url"));
            pstm.setString(20, tuple.getStringByField("home_code"));
            pstm.setString(21, tuple.getStringByField("home_url"));
            pstm.setString(22, tuple.getStringByField("video_card_code"));
            pstm.setString(23, tuple.getStringByField("video_card_url"));
            pstm.executeUpdate();
            i++;
            if (i >= 5000) {
                conn.commit();
                i = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + prop.getProperty(MysqlConfig.IP_PORT) + "/" + prop.getProperty(MysqlConfig.DATABASE),
                    prop.getProperty(MysqlConfig.USERNAME), prop.getProperty(MysqlConfig.PASSWORD));
            pstm = conn.prepareStatement(sql);
            conn.setAutoCommit(false);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
