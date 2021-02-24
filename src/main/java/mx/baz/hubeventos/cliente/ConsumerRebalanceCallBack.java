package mx.baz.hubeventos.cliente;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.OffsetAndTimestamp;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;

public class ConsumerRebalanceCallBack  implements ConsumerRebalanceListener {
	
	Consumer<?,?> consumer;
	String topico; 
	long time;;
	Map<TopicPartition,OffsetAndTimestamp> il;
	
	public ConsumerRebalanceCallBack (Consumer<?,?> consumer, String topico, long time) {
		this.topico = topico;
		this.consumer = consumer;
		this.time = time;
	
		Map<TopicPartition, Long> map = new HashMap<TopicPartition,java.lang.Long>();
		List<PartitionInfo> p = consumer.partitionsFor(topico);
		
		for (PartitionInfo partition : p ) {
			TopicPartition tp = new TopicPartition(topico, partition.partition());
			map.put(tp, time);
		}
		il = (consumer.offsetsForTimes(map)); 	
		
	}
	
	@Override
	public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
		// TODO Auto-generated method stub
		
		
				
		for (Entry<TopicPartition, OffsetAndTimestamp> tp : il.entrySet()) {
			if (partitions.contains(tp.getKey())) {
				consumer.seek(tp.getKey(), tp.getValue().offset());
			}
		}
	}

}
