import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import java.nio.charset.Charset

fun main() {
  val queueName = "durableQueue"
  val connectionFactory = ConnectionFactory().apply {
    host = "localhost"
    port = 5672
  }
  val connection = connectionFactory.newConnection()
  val channel = connection.createChannel()
  channel.queueDeclare(queueName, true, false, false, null)
  channel.basicQos(1)
  println("[x] Waiting for messages. To exit Press Ctrl+C")
  val deliverCallback = DeliverCallback { consumerTag, delivery ->
    val message = String(delivery.body, Charset.forName("UTF-8"))
    println("[x] Received message: '$message'")
    message.toCharArray().forEach { character ->
      if (character == '.') Thread.sleep(1000)
    }
    println("[x] Done")
    channel.basicAck(delivery.envelope.deliveryTag, false)
  }
  channel.basicConsume(queueName, false, deliverCallback) { _ -> }
}