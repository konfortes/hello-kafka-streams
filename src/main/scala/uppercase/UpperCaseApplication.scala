// package uppercase

// import java.util.Properties
// import java.util.concurrent.TimeUnit

// import org.apache.kafka.streams.kstream.Materialized
// import org.apache.kafka.streams.scala.ImplicitConversions._
// import org.apache.kafka.streams.scala._
// import org.apache.kafka.streams.scala.kstream._
// import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}

// object UpperCaseApplication extends App {
//   import Serdes._

//   val props: Properties = {
//     val p = new Properties()
//     p.put(StreamsConfig.APPLICATION_ID_CONFIG, "hello-kafka-streams-uppercase")
//     p.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
//     // disable caching in order to view each step instantly
//     p.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, "0")
//     p
//   }

//   val builder: StreamsBuilder = new StreamsBuilder
//   val textLines: KStream[String, String] =
//     builder.stream[String, String]("TextLinesTopic")

//   textLines
//     .mapValues(word => word.toUpperCase)
//     .to("UpperCaseWordsTopic")

//   val streams: KafkaStreams = new KafkaStreams(builder.build(), props)
//   streams.start()

//   sys.ShutdownHookThread {
//     streams.close(10, TimeUnit.SECONDS)
//   }
// }
