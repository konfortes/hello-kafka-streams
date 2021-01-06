package favoritecolor

import java.util.Properties
import java.util.concurrent.TimeUnit

import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala._
import org.apache.kafka.streams.scala.kstream._
import org.apache.kafka.streams.scala.serialization.Serdes._
import org.apache.kafka.streams.{KafkaStreams, KeyValue, StreamsConfig}

object FavoriteColorApp extends App {
  val props: Properties = {
    val p = new Properties()
    p.put(StreamsConfig.APPLICATION_ID_CONFIG, "hello-kafka-streams-colors")
    p.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    // disable caching in order to view each step instantly
    p.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, "0")
//    p.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String.getClass)
//    p.put(
//      StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,
//      Serdes.String.getClass
//    )

    p
  }

  val builder: StreamsBuilder = new StreamsBuilder
  val favorites: KStream[String, String] =
    builder.stream[String, String]("favorite-color-input")

  favorites
    .filter((_, value) => value.contains(","))
    .selectKey((_, value) => value.split(",").head.toLowerCase)
    .mapValues(value => value.split(",").tail.head.toLowerCase)
    .filter((_, color) => List("green", "blue", "red").contains(color))

  favorites.to("user-keys-and-colors")

  val usersColorsTable: KTable[String, String] =
    builder.table("user-keys-and-colors")

  val favoriteColors: KTable[String, java.lang.Long] =
    usersColorsTable
      .groupBy((_: String, color: String) =>
        new KeyValue[String, String](color, color)
      )
      .count("count-by-color")

//  favoriteColors.to("favorite-color-output")

  val streams: KafkaStreams = new KafkaStreams(builder.build(), props)
  streams.start()

  sys.ShutdownHookThread {
    streams.close(10, TimeUnit.SECONDS)
  }
}
