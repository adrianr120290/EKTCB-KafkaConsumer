package mx.baz.hubeventos.producer;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.baz.avro.model.envio.TransactionSpeiEnvio;
import mx.baz.hubeventos.avro.serializer.AvroSerializer;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(path = "/api/v1/producer", produces = MediaType.APPLICATION_JSON_VALUE)
public class Producer {

	private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);
	
//	@Value("${spring.kafka.bootstrap-servers}")
//	private String bootstrapServers;
//
//	@Value("${spring.kafka.user}")
//	private String kafkaUser;

	@Value("${tpd.topic-name}")
	private String topicoName;

//	@Autowired
//	private KafkaTemplate<String, String> kafkaTemplate;

//	@Autowired
//	private KafkaTemplate<String, TransactionSpeiEnvio> kafkaTemplate;

	@Autowired
	private KafkaTemplate<String, TransactionSpeiEnvio> kafkaProducer;

//	@PostMapping
//	public ResponseEntity<Mensaje> enviaMensaje(@RequestBody String mensaje) {
//		log.info("Publica mensaje...");
//		Mensaje ms = new Mensaje();
//		ms.setContenidoMensaje(mensaje);
//		ms.setCodRespuesta("0");
//		ms.setDesRespuesta("Mensaje publicado");		
//		kafkaTemplate.send(topicoName, ms.getContenidoMensaje());
//
//		return new ResponseEntity<>(ms, HttpStatus.OK);
//	}

//	@PostMapping("/user")
//	public ResponseEntity<String> enviaMensajeAvro(@RequestBody User user) {
//		LOGGER.info("sending user='{}'", user.toString());
//		
//		kafkaTemplate.send(topicoName, user);
//
//		return new ResponseEntity<>("Mensaje publicado", HttpStatus.OK);
//	}

//	@PostMapping("/order")
//	public ResponseEntity<String> enviaMensajeAvro(@RequestBody Order user) {
//		LOGGER.info("sending user='{}'", user.toString());
//		
//		kafkaTemplate.send(topicoName, user);
//
//		return new ResponseEntity<>("Mensaje publicado", HttpStatus.OK);
//	}


	@PostMapping("/spei")
	public ResponseEntity<String> enviaMensajeAvro(@RequestBody TransactionSpeiEnvio user) {
		LOGGER.info("sending user='{}'", user.toString());

//		kafkaTemplate.send(topicoName, user);
		
		

//		Map<String, Object> props = new HashMap<>();
//
//		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, AvroSerializer.class);
//
//		/***/
//		props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
//		props.put(SslConfigs.DEFAULT_SSL_ENDPOINT_IDENTIFICATION_ALGORITHM, "");
//		props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, "src/main/resources/ssl/" + kafkaUser + ".truststore.jks");
//		props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, kafkaUser);
//
//		// configure the following three settings for SSL Authentication
//		props.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, "src/main/resources/ssl/" + kafkaUser + ".keystore.jks");
//		props.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, kafkaUser + "key");
//		props.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, kafkaUser + "sec");
//
//		props.put(ProducerConfig.ACKS_CONFIG, "all");
//		props.put(ProducerConfig.RETRIES_CONFIG, 3);
		
		

//		props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");

//		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
//				"org.apache.kafka.common.serialization.StringSerializer");
//		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

//		KafkaProducer<String, TransactionSpeiEnvio> kafkaProducer = new KafkaProducer<String, TransactionSpeiEnvio>(props);
		int i = 0;

		try {
			
			for (i = 0; i < 2; i++) {
				System.out.println("OARP" + i);
				ProducerRecord<String, TransactionSpeiEnvio> pr = new ProducerRecord<String, TransactionSpeiEnvio>(topicoName, "r" + i,
						user);
				
//				kafkaProducer.send(pr);
				kafkaProducer.send(topicoName, user);
			}			
		} catch (Exception e) {			
			System.out.println("Errror sending msg: " + i);
			e.printStackTrace();
			return new ResponseEntity<>("Error en enviar el mensaje", HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
//			kafkaProducer.close();
		}

		return new ResponseEntity<>("Mensaje publicado", HttpStatus.OK);
	}

}
