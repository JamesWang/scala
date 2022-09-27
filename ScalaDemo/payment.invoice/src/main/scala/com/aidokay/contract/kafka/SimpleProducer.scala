package com.aidokay.contract.kafka

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import scala.util.Using

object SimpleProducer {

  def main(args:Array[String]): Unit = {
    writeToKafka("contract.test", Seq("test1","test2"))
  }
  def writeToKafka(topic: String, messages: Seq[String]): Unit = {
    val props = new Properties()

    props.put("bootstrap.servers", "192.168.1.70:9092")
    //props.put("bootstrap.servers", "localhost:9092")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    //props.put("max.block.ms", 0)

    Using.resource(new KafkaProducer[String, String](props)) { producer ⇒
      messages.foreach { message ⇒
        val record = new ProducerRecord[String, String](topic, "invoice", message)
        producer.send(record)
      }
    }
  }

}
