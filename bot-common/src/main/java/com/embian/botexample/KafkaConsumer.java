package com.embian.botexample;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

public abstract class KafkaConsumer implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

	private final String zkHost;
	private final String groupId;
	private final String topic;
	private final int partitions;

	public KafkaConsumer(String zkHost, String topic, String groupId, int partitions) {
		this.zkHost = zkHost;
		this.topic = topic;
		this.groupId = groupId;
		this.partitions = partitions;
	}
	
	@Override
	public void run() {
    	Properties props = new Properties();
		props.put("zookeeper.connect", zkHost);
		props.put("group.id", groupId);
		props.put("zookeeper.connection.timeout.ms", "6000");
		props.put("auto.commit.enable", "true");
		props.put("auto.offset.reset", "largest");
		props.put("auto.commit.interval.ms", "1000");

		// Create the connection to the cluster
		ConsumerConfig consumerConfig = new ConsumerConfig(props);
		ConsumerConnector consumerConnector = Consumer
				.createJavaConsumerConnector(consumerConfig);

		Map<String, List<KafkaStream<byte[], byte[]>>> topicMessageStreams = consumerConnector
				.createMessageStreams(ImmutableMap.of(topic, partitions));
		List<KafkaStream<byte[], byte[]>> streams = topicMessageStreams.get(topic);

		ExecutorService executor = Executors.newFixedThreadPool(partitions);

		// consume the messages in the threads
		for (final KafkaStream<byte[], byte[]> stream : streams) {
			executor.submit(new Runnable() {
				public void run() {
					boolean alive = true;
					ConsumerIterator<byte[], byte[]> consumerIte = stream.iterator();
					while (consumerIte.hasNext()) {
						if (!alive)
							break;
						byte[] message = consumerIte.next().message();
						
						try {
							processEvent(message);

							Thread.sleep(100);
						} catch (InterruptedException ie) {
							alive = false;
						} catch (Exception e) {
							LOGGER.error("error occurred on consume message. {}", e);
						}
					}
				}
			});
		}
	}

	abstract protected void processEvent(byte[] message);
}
