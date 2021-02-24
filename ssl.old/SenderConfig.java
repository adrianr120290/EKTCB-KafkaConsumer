package mx.baz.hubeventos.producer;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import mx.baz.avro.model.envio.TransactionSpeiEnvio;
import mx.baz.hubeventos.avro.serializer.AvroSerializer;

@Configuration
public class SenderConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;

	@Value("${spring.kafka.user}")
	private String kafkaUser;

	@Bean
	public Map<String, Object> producerConfigs() {
		Map<String, Object> props = new HashMap<>();

		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, AvroSerializer.class);

		/***/
		props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
		props.put(SslConfigs.DEFAULT_SSL_ENDPOINT_IDENTIFICATION_ALGORITHM, "");
		props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, "src/main/resources/ssl/" + kafkaUser + ".truststore.jks");
		props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, kafkaUser);

		// configure the following three settings for SSL Authentication
		props.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, "src/main/resources/ssl/" + kafkaUser + ".keystore.jks");
		props.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, kafkaUser + "key");
		props.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, kafkaUser + "sec");

		props.put(ProducerConfig.ACKS_CONFIG, "all");
		props.put(ProducerConfig.RETRIES_CONFIG, 3);

//		props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");

//		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
//		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

		return props;
	}

	@Bean
	public ProducerFactory<String, TransactionSpeiEnvio> producerFactory() {
		return new DefaultKafkaProducerFactory<>(producerConfigs());
	}

	@Bean
	public KafkaTemplate<String, TransactionSpeiEnvio> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}

}
