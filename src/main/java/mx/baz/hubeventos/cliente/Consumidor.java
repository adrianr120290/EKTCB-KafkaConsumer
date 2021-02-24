package mx.baz.hubeventos.cliente;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import mx.baz.hubeventos.avro.serializer.*;
import mx.baz.hubeventos.conf.ConsumidorProperties;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.errors.WakeupException;


	public class Consumidor {

		

		public static void main(String[] args) {
			Properties properties = new Properties();
	    	Properties prop = null;  
	    	
	    	try {
	    		prop = new ConsumidorProperties().getPropValues(); 
	    	} catch (Exception e) {
				System.out.println("Hubo un error al leer el archivo de propiedades " + e.getMessage());
				System.exit(-1);
	    	} 	
	    	
	    	properties.put("bootstrap.servers", prop.getProperty("SERVIDORES"));
			properties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
			properties.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, "ssl/" + prop.getProperty("USUARIO") +  ".truststore.jks");
			properties.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG,  prop.getProperty("PASSWORD_TRUSTSTORE"));       
			       
			 // configure the following three settings for SSL Authentication
			 properties.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, "ssl/" + prop.getProperty("USUARIO") +  ".keystore.jks");
			 properties.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, prop.getProperty("PASSWORD_KEYSTORE"));
			 properties.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, prop.getProperty("PASSWORD_KEY"));
			 	
		//	 properties.put(SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG, "");
		     properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
		     properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArrayDeserializer");
		    
		       
		     properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		     properties.put(ConsumerConfig.GROUP_ID_CONFIG, prop.getProperty("GRUPO"));   
		     properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		       
	         properties.put(ConsumerConfig.GROUP_INSTANCE_ID_CONFIG, prop.getProperty("INSTANCE_ID")); 
	         properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 120000);
		   
		   
		   
		   	KafkaConsumer<String, byte[]> consumer = new KafkaConsumer<String, byte[]>(properties);
		   	String lista_topicos = prop.getProperty("TOPICOS");
		   	List<String> lista = null;
		   	Map<String, Schema> esquemas = new HashMap<String, Schema>();
		   	
		   	if (lista_topicos != null && lista_topicos.length() > 0 ) {
		   		lista = Arrays.asList(lista_topicos.split(","));
		   		consumer.subscribe(lista);   
		   	} else {
		   		System.out.println("Sin lista de tópicos");
		   		System.exit(1);	
		   	}

		   	String posicion = prop.getProperty("POSICION");
		   	if (posicion != null && ( "INICIO".compareToIgnoreCase(posicion) == 0 || ("FIN".compareToIgnoreCase(posicion) == 0))) {
			   	consumer.poll(Duration.ofMillis(5000));
			
		   		Set<TopicPartition> s =  consumer.assignment();
		   		Map<TopicPartition,Long> fi = consumer.endOffsets(s);
		   		for (TopicPartition partition : fi.keySet() ) {
					System.out.println("Topico: " + partition.topic() +  " Particion: " + partition.partition() + " Totales " + fi.get( partition ));
		   		}
		   		
		   		Map<TopicPartition,Long> ii = consumer.beginningOffsets(s);
		   		for (TopicPartition partition : ii.keySet() ) {
					System.out.println("Topico: " + partition.topic() +  " Particion: " + partition.partition() + " Totales " + ii.get( partition ));
		   		}
		   		
		   		if ("INICIO".compareToIgnoreCase(posicion) == 0) {
		   			consumer.seekToBeginning(s);
		   		} else {
		   			consumer.seekToEnd(s);
		   		}
		   	} 
	   		
		   	
		   	
		   	String directorio = prop.getProperty("AVROS");
			if (directorio != null) {
				String[] esquemas_archivo = new File(directorio).list();
				
				for (int archivos = 0 ; archivos < esquemas_archivo.length; archivos++) {
					File archivo_esquema = new File(directorio +  File.separator  + esquemas_archivo[archivos]);
					// Solo el nombre sin .avsc
					String nombre_topico = archivo_esquema.getName().toUpperCase().substring(0,  archivo_esquema.getName().length() - 5);
					
					try {
						esquemas.put(nombre_topico, new Schema.Parser().parse(archivo_esquema));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}		   	
	   		
			String directorio_salida = prop.getProperty("SALIDA");
			if (directorio_salida == null || directorio_salida.length() == 0) {
				System.out.println("Sin directorio de salida");
		   		System.exit(1);	
			}
			
			
		    long i[] = new long[lista.size()];
			try {
				Date d1 = new Date();
				while (true) {
					ConsumerRecords<String, byte[]> records = consumer.poll(Duration.ofMillis(5000));
					
					for (ConsumerRecord<String, byte[]> record : records) {
						String topico = record.topic().toUpperCase();
						FileWriter fw = new FileWriter(directorio_salida + File.separator  + topico, true);
						BufferedWriter bw = new BufferedWriter(fw);
						 
						
						bw.write("Topico: " + topico + " Particion: " + record.partition() + " Offset: "  + record.offset() + " Recepcion: " + new Date(record.timestamp()));
						bw.newLine();
						
						
						Schema esquema = esquemas.get(topico);						
						if (esquema != null) {
							try {
	
								DatumReader<GenericRecord> datumReader = new SpecificDatumReader<GenericRecord>(esquema);
							    Decoder decoder = DecoderFactory.get().binaryDecoder(record.value(), null);
							    GenericRecord sp = datumReader.read(null, decoder);
							    bw.write(sp.toString());
							    bw.newLine();

							} catch (Exception e) {
								System.out.println("No se pudo procesar el mensaje " + e.getMessage());	
								e.printStackTrace();
							}
						} else {
							
							bw.write(new String(record.value()));		
							bw.newLine();
						}
						
						i[lista.indexOf(record.topic())]++;
						bw.close();
						consumer.commitSync(); //new ConsumerOffsetCommitCallback());	
						
					}
						
						
					Date d2 = new Date();
				    System.out.println("Mensajes leidos en " + (d2.getTime() - d1.getTime()) + "ms");    
					for  (int index =  0 ; index < lista.size() ; index++) {
						System.out.println("\t" + lista.get(index) + " : " + i[index]);
					}
						
						
						
					} 
			   } catch (Exception e){
				   e.printStackTrace();
		           System.out.println("Hubo un error ");
			   }  finally {
					consumer.close();
			   }  
		    
		}

		
}
