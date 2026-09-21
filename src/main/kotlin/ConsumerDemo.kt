import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.*

class ConsumerDemo {
    val log = LoggerFactory.getLogger(ConsumerDemo::class.java.getSimpleName())!!
    val properties = Properties()
    val groupId = "my-kotlin-application"
    val topic = "demo_kotlin"
    init {
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092")
        properties.setProperty("key.deserializer", StringDeserializer::class.java.name)
        properties.setProperty("value.deserializer", StringDeserializer::class.java.name)
        properties.setProperty("group.id", groupId)
        properties.setProperty("auto.offset.reset", "earliest")
    }

    fun run() {
        log.info("I am a Kafka consumer!")
        val consumer = KafkaConsumer<String, String>(properties)
        consumer.subscribe(listOf(topic))
        while (true) {
            log.info("Polling...")
            val records = consumer.poll(Duration.ofMillis(1000))
            for (record in records) {
                log.info(
                    "Key: ${record.key()}, Value: ${record.value()}, Partition: ${record.partition()}, Offset: ${record.offset()}"
                )
            }
        }
    }
}

fun main() {
    ConsumerDemo().run()
}