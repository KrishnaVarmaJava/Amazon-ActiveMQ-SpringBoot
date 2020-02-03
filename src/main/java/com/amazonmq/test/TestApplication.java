package com.amazonmq.test;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestApplication.class, args);
		
		
		ActiveMQConnectionFactory connFactory = new ActiveMQConnectionFactory("admin","123456789101112","ssl://b-77010294-6f95-414a-b729-33a5f87ac4b3-1.mq.us-east-2.amazonaws.com:61617");
		 
		System.out.println("Conn factory Created");
	    PooledConnectionFactory pooledConnFactory = new PooledConnectionFactory(connFactory);
	    System.out.println("pool factory Created");
		
	    try{
	    	Connection producerConnection = pooledConnFactory.createConnection();
	    	System.out.println("producer factory Created");
	    	producerConnection.start();
	    	System.out.println("Conn factory started");
	    	
	    	
	    	 Session producerSession = producerConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);

	    	    Destination producerDest = producerSession.createQueue("TEST");

	    	    MessageProducer producer = producerSession.createProducer(producerDest);
	    	    producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

	    	    String text = "Message from Producer using Amazon MQ";
	    	    TextMessage producerMessage = producerSession.createTextMessage(text);
	    	    producer.send(producerMessage);
	    	    System.out.println("Message sent successfully");

	    	    
	   
	    	       Destination consumerDest = producerSession.createQueue("TEST");

	    	       MessageConsumer consumer = producerSession.createConsumer(consumerDest);

	    	       Message consumerMesg = consumer.receive(1000);
	    	       System.out.println(consumerMesg.toString());
	    	       if (consumerMesg instanceof ActiveMQTextMessage) {
	    	    	    ActiveMQTextMessage consumerTextMesg = (ActiveMQTextMessage) consumerMesg;
	    	    	    
	 	    	       System.out.println("Received Message is : " + consumerTextMesg.getText());

	    	    	} else {
	    	    		if (consumerMesg instanceof BytesMessage){
	    	            	BytesMessage byteMessage = (BytesMessage) consumerMesg;
	    	            	byte[] byteData = null;
	    	            	try {
	    	    				byteData = new byte[(int) byteMessage.getBodyLength()];
	    	    			} catch (JMSException e) {
	    	    				// TODO Auto-generated catch block
	    	    				e.printStackTrace();
	    	    			}
	    	            	try {
	    	    				byteMessage.readBytes(byteData);
	    	    			} catch (JMSException e) {
	    	    				// TODO Auto-generated catch block
	    	    				e.printStackTrace();
	    	    			}
	    	            	try {
	    	    				byteMessage.reset();
	    	    			} catch (JMSException e) {
	    	    				// TODO Auto-generated catch block
	    	    				e.printStackTrace();
	    	    			}
	    	            	String stringMessage =  new String(byteData);
	    	            	System.out.println(stringMessage);
	    	            	}
	    	    	}
	    	       

	    	   
	    }catch(JMSException e) {
	    	e.printStackTrace();
	    }
	}



}

