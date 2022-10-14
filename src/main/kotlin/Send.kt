import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.MessageProperties
import java.util.*

fun main() {
  val queueName = "durableQueue"
  val connectionFactory = ConnectionFactory().apply {
    host = "localhost"
    port = 5672
  }
  connectionFactory.newConnection().use { connection ->
    connection.createChannel().use { channel ->
      channel.queueDeclare(queueName, true, false, false, null)
      Scanner(System.`in`).use { scanner ->
        while (scanner.hasNextLine()) {
          val message = scanner.nextLine()
          channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, message.toByteArray())
          println("[x] Sent: '$message'")
        }
      }
    }
  }
}