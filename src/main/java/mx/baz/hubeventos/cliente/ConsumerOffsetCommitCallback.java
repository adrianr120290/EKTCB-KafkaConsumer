package mx.baz.hubeventos.cliente;

import java.util.Map;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;

public class ConsumerOffsetCommitCallback implements OffsetCommitCallback {

	@Override
	public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
		
		if (exception != null ) {
			for (Map.Entry<TopicPartition, OffsetAndMetadata> entry : offsets.entrySet()) {
				System.out.println( "Failed to commit: " + entry.getKey().topic() + " "  + entry.getValue().offset() + " " + exception.getMessage());
			}
		}
		
	}
}
