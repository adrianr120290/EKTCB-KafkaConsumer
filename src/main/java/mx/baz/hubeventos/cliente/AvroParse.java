package mx.baz.hubeventos.cliente;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.generic.GenericRecord;



	public class AvroParse {

		
	  
		
		public static void main(String[] args) {


			final String json = "{\n" + 
					"   \"transactionData\":{\n" + 
					"      \"entidad\":\"0127\",\n" + 
					"      \"canal\":\"01\",\n" + 
					"      \"canalOrigen\":\"COR\",\n" + 
					"      \"sucursal\":\"4037\",\n" + 
					"      \"fechaEmision\":\"20200623 02:06:03\",\n" + 
					"      \"idMensaje\":1540,\n" + 
					"      \"terminal\":{\n" + 
					"         \"string\":\"PQ0E\"\n" + 
					"      },\n" + 
					"      \"usuario\":{\n" + 
					"         \"string\":\"BUPAYHUB\"\n" + 
					"      },\n" + 
					"      \"idAplicactivo\":1\n" + 
					"   },\n" + 
					"}";
			
			
			final String avro  = "{\n" + 
					"  \"type\" : \"record\",\n" + 
					"  \"name\" : \"TransactionSpeiEnvio\",\n" + 
					"  \"namespace\" : \"mx.baz.avro.model\",\n" + 
					"  \"doc\" : \"This Schema describes about...\",\n" + 
					"  \"fields\" : [ {\n" + 
					"    \"name\" : \"transactionData\",\n" + 
					"    \"type\" : {\n" + 
					"      \"type\" : \"record\",\n" + 
					"      \"name\" : \"TransactionHeader\",\n" + 
					"      \"fields\" : [ {\n" + 
					"        \"name\" : \"entidad\",\n" + 
					"        \"type\" : \"string\",\n" + 
					"        \"example\" : \"0127\",\n" + 
					"        \"minLength\" : 4,\n" + 
					"        \"maxLength\" : 4\n" + 
					"      }, {\n" + 
					"        \"name\" : \"canal\",\n" + 
					"        \"type\" : \"string\",\n" + 
					"        \"example\" : \"001\",\n" + 
					"        \"minLength\" : 1,\n" + 
					"        \"maxLength\" : 3\n" + 
					"      }, {\n" + 
					"        \"name\" : \"canalOrigen\",\n" + 
					"        \"type\" : \"string\",\n" + 
					"        \"example\" : \"BEA\",\n" + 
					"        \"minLength\" : 3,\n" + 
					"        \"maxLength\" : 3\n" + 
					"      }, {\n" + 
					"        \"name\" : \"sucursal\",\n" + 
					"        \"type\" : \"string\",\n" + 
					"        \"example\" : \"0100\",\n" + 
					"        \"minLength\" : 4,\n" + 
					"        \"maxLength\" : 4\n" + 
					"      }, {\n" + 
					"        \"name\" : \"fechaEmision\",\n" + 
					"        \"type\" : \"string\",\n" + 
					"        \"example\" : \"20200216 14:00:00\",\n" + 
					"        \"minLength\" : 17,\n" + 
					"        \"maxLength\" : 17\n" + 
					"      }, {\n" + 
					"        \"name\" : \"idMensaje\",\n" + 
					"        \"type\" : \"int\",\n" + 
					"        \"example\" : \"1234567890\"\n" + 
					"      }, {\n" + 
					"        \"name\" : \"terminal\",\n" + 
					"        \"type\" : [ \"string\", \"null\" ],\n" + 
					"        \"example\" : \"IKOSF900\",\n" + 
					"        \"minLength\" : 4,\n" + 
					"        \"maxLength\" : 8\n" + 
					"      }, {\n" + 
					"        \"name\" : \"usuario\",\n" + 
					"        \"type\" : [ \"string\", \"null\" ],\n" + 
					"        \"example\" : \"FE2C9000\",\n" + 
					"        \"minLength\" : 8,\n" + 
					"        \"maxLength\" : 8\n" + 
					"      }, {\n" + 
					"        \"name\" : \"idAplicactivo\",\n" + 
					"        \"type\" : \"int\",\n" + 
					"        \"example\" : \"1\"\n" + 
					"      }, {\n" + 
					"        \"name\" : \"referenciaUnica\",\n" + 
					"        \"type\" : [  \"null\",  \"string\"],\n" + 
					"        \"example\" : \"123e4567e89b12d3a456426655440000\",\n" + 
					"        \"minLength\" : 32,\n" + 
					"        \"maxLength\" : 32,\n" + 
					"		 \"default\" : null\n" + 
					"      } ]\n" + 
					"    }\n" + 
					"  }]\n" +
					"}";
			
			
		   		
		 
		   	try {
			   	InputStream input = new ByteArrayInputStream(json.getBytes());
			    DataInputStream din = new DataInputStream(input);
			    org.apache.avro.Schema schema = new org.apache.avro.Schema.Parser().setValidateDefaults(true).parse(avro);
			  
			    Decoder decoder = DecoderFactory.get().jsonDecoder(schema, din);
			    DatumReader<Object> reader = new GenericDatumReader<Object>(schema);
			    Object datum = reader.read(null, decoder);
	
			    GenericDatumWriter<Object>  w = new GenericDatumWriter<Object>(schema);
			    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	
			    Encoder enc = EncoderFactory.get().binaryEncoder(outputStream, null);
	
			    w.write(datum, enc);	
			    enc.flush();
	
			    byte[] msg= outputStream.toByteArray();
			  
			    DatumReader<GenericRecord> reader1 = new GenericDatumReader<GenericRecord>(schema);
			    Decoder decoder1 = DecoderFactory.get().binaryDecoder(msg, null);
			    GenericRecord result = reader1.read(null, decoder1);
			    
			    System.out.println(result);
		   	} catch (Exception e) {
		   		e.printStackTrace();
		   	}
					
			
		}

}
