import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.LoggerFactory
import java.util.Properties

class ProducerDemo {
    val log = LoggerFactory.getLogger(ProducerDemo::class.java.getSimpleName())!!
    val properties = Properties()
    init {
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092")
        properties.setProperty("key.serializer", StringSerializer::class.java.name)
        properties.setProperty("value.serializer", StringSerializer::class.java.name)
    }

    fun run() {
        log.info("I am a Kafka producer!")
        val producer = KafkaProducer<String, String>(properties)
        val producerRecord = ProducerRecord<String, String>("demo_kotlin", "hello world")
        producer.send(producerRecord)
        producer.close()
    }
}

fun main() {
    ProducerDemo().run()
}