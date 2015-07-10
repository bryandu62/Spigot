package org.apache.logging.log4j.core.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.Charset;
import javax.jms.JMSException;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.logging.log4j.Logger;

public class JMSTopicReceiver
  extends AbstractJMSReceiver
{
  public JMSTopicReceiver(String tcfBindingName, String topicBindingName, String username, String password)
  {
    try
    {
      Context ctx = new InitialContext();
      
      TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory)lookup(ctx, tcfBindingName);
      TopicConnection topicConnection = topicConnectionFactory.createTopicConnection(username, password);
      topicConnection.start();
      TopicSession topicSession = topicConnection.createTopicSession(false, 1);
      Topic topic = (Topic)ctx.lookup(topicBindingName);
      TopicSubscriber topicSubscriber = topicSession.createSubscriber(topic);
      topicSubscriber.setMessageListener(this);
    }
    catch (JMSException e)
    {
      this.logger.error("Could not read JMS message.", e);
    }
    catch (NamingException e)
    {
      this.logger.error("Could not read JMS message.", e);
    }
    catch (RuntimeException e)
    {
      this.logger.error("Could not read JMS message.", e);
    }
  }
  
  public static void main(String[] args)
    throws Exception
  {
    if (args.length != 4) {
      usage("Wrong number of arguments.");
    }
    String tcfBindingName = args[0];
    String topicBindingName = args[1];
    String username = args[2];
    String password = args[3];
    
    new JMSTopicReceiver(tcfBindingName, topicBindingName, username, password);
    
    Charset enc = Charset.defaultCharset();
    BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in, enc));
    
    System.out.println("Type \"exit\" to quit JMSTopicReceiver.");
    for (;;)
    {
      String line = stdin.readLine();
      if ((line == null) || (line.equalsIgnoreCase("exit")))
      {
        System.out.println("Exiting. Kill the application if it does not exit due to daemon threads.");
        
        return;
      }
    }
  }
  
  private static void usage(String msg)
  {
    System.err.println(msg);
    System.err.println("Usage: java " + JMSTopicReceiver.class.getName() + " TopicConnectionFactoryBindingName TopicBindingName username password");
    
    System.exit(1);
  }
}
