package com.github.simplesteph.udemy.kafka.streams

import java.lang
import java.util.Properties

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.kstream.{KStream, KStreamBuilder, KTable}
import org.apache.kafka.streams.{KafkaStreams, KeyValue, StreamsConfig}

object FavoriteColorApp {
  def main(args: Array[String]): Unit = {

    val config: Properties = new Properties
    config.put(
      StreamsConfig.APPLICATION_ID_CONFIG,
      "hello-kafka-streams-favorite-color"
    )
    config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
    config.put(
      StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG,
      Serdes.String.getClass
    )
    config.put(
      StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,
      Serdes.String.getClass
    )

    config.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, "0")

    val builder: KStreamBuilder = new KStreamBuilder

    val textLines: KStream[String, String] =
      builder.stream[String, String]("favorite-color-input")

    val usersAndColors: KStream[String, String] = textLines
      .filter((key: String, value: String) => value.contains(","))
      .selectKey[String]((key: String, value: String) =>
        value.split(",")(0).toLowerCase
      )
      .mapValues[String]((value: String) => value.split(",")(1).toLowerCase)
      .filter((user: String, color: String) =>
        List("green", "blue", "red").contains(color)
      )

    val intermediaryTopic = "user-keys-and-colors"
    usersAndColors.to(intermediaryTopic)

    val usersAndColorsTable: KTable[String, String] =
      builder.table(intermediaryTopic)

    val favoriteColors: KTable[String, lang.Long] = usersAndColorsTable
      .groupBy((user: String, color: String) =>
        new KeyValue[String, String](color, color)
      )
      .count("CountsByColors")

    favoriteColors.to(Serdes.String, Serdes.Long, "favorite-color-output")

    val streams: KafkaStreams = new KafkaStreams(builder, config)
    streams.cleanUp()
    streams.start()

    // print the topology
    System.out.println(streams.toString)

    // shutdown hook to correctly close the streams application
    Runtime.getRuntime.addShutdownHook(new Thread {
      override def run(): Unit = {
        streams.close()
      }
    })
  }
}
