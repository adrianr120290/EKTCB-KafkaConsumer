package mx.baz.hubeventos.conf;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
 

public class ConsumidorProperties {

		String result = "";
		InputStream inputStream;
	 
		public Properties getPropValues() throws IOException {
			Properties prop = null;
			
			try {
				prop = new Properties();
				String propFileName = "consumidor.properties";
	 
				inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
	 
				if (inputStream != null) {
					prop.load(inputStream);
				} else {
					throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
				}
	 
				
				
			} catch (Exception e) {
				System.out.println("Exception: " + e);
			} finally {
				inputStream.close();
			}
			return prop;
		}
	}
	
