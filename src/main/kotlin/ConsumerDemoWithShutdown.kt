import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.errors.WakeupException
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.*

class ConsumerDemoWithShutdown {
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
        log.info("I am a Kafka consumer with a shutdown hook!")
        val consumer = KafkaConsumer<String, String>(properties)
        val mainThread = Thread.currentThread()
        Runtime.getRuntime().addShutdownHook(Thread {
            log.info("Detected a shutdown, let's exit by calling consumer.wakeup()...")
            consumer.wakeup()

            try {
                mainThread.join()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        })
        try {
            consumer.subscribe(listOf(topic))
            while (true) {
                val records = consumer.poll(Duration.ofMillis(1000))
                for (record in records) {
                    log.info(
                        "Key: ${record.key()}, Value: ${record.value()}, Partition: ${record.partition()}, Offset: ${record.offset()}"
                    )
                }
            }
        } catch (_: WakeupException) {
            log.info("Consumer is starting to shut down...")
        } catch (e: Exception) {
            log.error("Unexpected exception in the consumer", e)
        } finally {
            consumer.close()
            log.info("The consumer is now gracefully shut down.")
        }
    }
}

fun main() {
    ConsumerDemoWithShutdown().run()
}