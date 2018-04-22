package com.joker.spark.kafka;

/**
 * Created by hunter on 2018/4/10.
 */
public class KafkaClientApp {
    public static void main(String[] args) {
        new KafkaProducer(KafkaProperties.TOPIC).start();

        //new KafkaConsumer(KafkaProperties.TOPIC).start();

    }
}
