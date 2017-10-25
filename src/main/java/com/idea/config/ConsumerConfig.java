package com.idea.config;

public final class ConsumerConfig {

    private ConsumerConfig(){}

    /**
     * kafka brokers 列表
     */
    public static final String BOOTSTRAP_SERVERS = "bootstrap.servers";

    /**
     * 消费组ID
     */
    public static final String GROUP_ID = "group.id";

    /**
     * 设置自动提交offset
     */
    public static final String ENABLE_AUTO_COMMIT = "enable.auto.commit";

    /**
     * 消费TOPIC列表
     */
    public static final String TOPICS = "topics";

}
