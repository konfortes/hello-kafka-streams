# hello-kafka-streams

## Word Count

### Producer

```bash
echo "dog dog cat cat cat" | kafkacat -b localhost:9092 -t TextLinesTopic
```

### Consumer

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

## FavoriteColor

### Producer

```bash
kafkacat -b localhost:9092 -t FavoriteColorInput -P
stephane,blue
badinput:yellow
john,green
stephane,red
alice,red
```

### Consumer

```bash
kafkacat -b localhost:9092 -t FavoriteColorOutput -C -f '%k: %s\n'
```
