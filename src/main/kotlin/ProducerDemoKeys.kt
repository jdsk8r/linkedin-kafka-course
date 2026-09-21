import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.LoggerFactory
import java.util.Properties

class ProducerDemoKeys {
    val log = LoggerFactory.getLogger(ProducerDemoWithCallback::class.java.getSimpleName())!!
    val properties = Properties()
    init {
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092")
        properties.setProperty("key.serializer", StringSerializer::class.java.name)
        properties.setProperty("value.serializer", StringSerializer::class.java.name)
    }

    fun run() {
        log.info("I am a Kafka producer with keys!")
        val producer = KafkaProducer<String, String>(properties)
        (1..2).forEach { _ ->
            for (i in 1..10) {
                val topic = "demo_kotlin"
                val key = "id_$i"
                val value = "hello world $i"
                val producerRecord = ProducerRecord(topic, key, value)
                producer.send(producerRecord) callback@{ metadata, exception ->
                    if (exception == null) {
                        log.info(
                            "Received new metadata. \n" +
                                    "Topic: ${metadata.topic()}\n" +
                                    "Key: $key\n" +
                                    "Partition: ${metadata.partition()}\n" +
                                    "Offset: ${metadata.offset()}\n" +
                                    "Timestamp: ${metadata.timestamp()}"
                        )
                    } else {
                        log.error("Error while producing", exception)
                    }
                }
            }
            Thread.sleep(500)
        }
        producer.close()
    }
}

fun main() {
    ProducerDemoKeys().run()
}