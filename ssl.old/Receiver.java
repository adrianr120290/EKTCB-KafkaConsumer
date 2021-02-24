package mx.baz.hubeventos.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class Receiver {

	private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);

//	private CountDownLatch latch = new CountDownLatch(1);
//
//	public CountDownLatch getLatch() {
//		return latch;
//	}
//
//	@KafkaListener(topics = "${tpd.topic-name}")
//	public void receive(Transaction user) {
//		LOGGER.info("Hola Oscar Este es el listener");
//		LOGGER.info("received user='{}'", user.toString());
//		latch.countDown();
//	}
	
	@Value("${spring.kafka.bootstrap-servers}")
	private static String bootstrapServers;

	@Value("${spring.kafka.user}")
	private String kafkaUser;

	@Value("${tpd.topic-name}")
	private String topicoName;
	
//	public static void main(String[] args) {
//		Properties properties = new Properties();
//		Properties prop;
//		   try {
//			   
//			   properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//			   properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//				props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, AvroSerializer.class);
//				
//		       properties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
//		       properties.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, "src/main/resources/ssl/" + prop.getProperty("USUARIO") + ".truststore.jks");
//		       properties.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG,  prop.getProperty("USUARIO"));       
//		       
//		    // configure the following three settings for SSL Authentication
//		       properties.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, "src/main/resources/ssl/" + prop.getProperty("USUARIO") + ".keystore.jks");
//		       properties.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, prop.getProperty("USUARIO") + "key");
//		       properties.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, prop.getProperty("USUARIO") + "sec");
//		    
//		       properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
//		       properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
//		       
//		       
////		   properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//		   properties.put(ConsumerConfig.GROUP_ID_CONFIG, prop.getProperty("GRUPO"));
//		       
//			KafkaConsumer<String, String> consumer = new KafkaConsumer<String,String>(properties);
//			
//			consumer.subscribe(Pattern.compile(prop.getProperty("TOPICO")));
//			
//			
//			
//			try {
//				long i = 0;
//				while (true) {
//					Date d1 = new Date();
//					
//					long j = 0;
//					ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(5000));
//					for (ConsumerRecord<String, String> record : records) {
//						if ("si".compareToIgnoreCase(prop.getProperty("IMPRIMIR")) == 0) {
//							System.out.println("topic = " + record.topic() + " , partition = " + record.partition() +  
//								" offset = " + record.offset() + ", key = " + record.key() + " , speiData = " + record.value());
//						}
//						j++;
//							
//					
//					}
//					
//					Date d2 = new Date();
//					i+=j;
//		            System.out.println("Mensajes leidos: " + j + " en " + (d2.getTime() - d1.getTime()) + "ms total: " + i);    
//					
//				}
//			} finally {
//				consumer.close();
//			}
//		   }catch (Exception e){
//			   System.out.println(e.getMessage());
//	           System.out.println("Existe un error con el archivo consumidor.properties");
//		   }
//	}
}
