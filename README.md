# hello-kafka-streams

## Word Count

### Producer

```bash
echo "dog dog cat cat cat" | kafkacat -b localhost:9092 -t TextLinesTopic
```

### Consumer

```bash
# intermediary
kafka-console-consumer --bootstrap-server localhost:9092 \
--topic user-keys-and-colors \
--from-beginning \
--formatter kafka.tools.DefaultMessageFormatter \
--property print.key=true \
--property print.value=true \
--property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer \
--property value.deserializer=org.apache.kafka.common.serialization.StringDeserializer


    kafka-console-consumer --bootstrap-server localhost:9092 \
    --topic favorite-color-output \
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
kafkacat -b localhost:9092 -t favorite-color-input -P
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
