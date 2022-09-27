package com.aidokay.contract.kafka

import java.time.Duration
import java.util
import java.util.Properties

import org.apache.kafka.clients.consumer.KafkaConsumer

import scala.jdk.CollectionConverters.IterableHasAsScala

object SimpleConsumer {
  def main(args: Array[String]): Unit = {
    consumeFromKafka("contract.test")
  }

  def consumeFromKafka(topic: String): Unit = {
    val props = new Properties()
    //props.put("bootstrap.servers", "localhost:9092")
    props.put("bootstrap.servers", "192.168.1.70:9092")
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("auto.offset.reset", "earliest")
    props.put("group.id", "contract-test-group2")
    val consumer: KafkaConsumer[String, String] = new KafkaConsumer[String, String](props)
    consumer.subscribe(util.Arrays.asList(topic))
    while (true) {
      val record = consumer.poll(Duration.ofMillis(1000)).asScala
      for (data <- record.iterator)
        println(data.value())
    }
  }
}
