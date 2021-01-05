# hello-kafka-streams

### Word count consumer:

```bash
kafka-console-consumer --bootstrap-server localhost:9092 \
--topic WordsWithCountsTopic \
--from-beginning \
--formatter kafka.tools.DefaultMessageFormatter \
--property print.key=true \
--property print.value=true \
--property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer \
--property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer
```
