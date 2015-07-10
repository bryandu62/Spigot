package org.apache.logging.log4j.core.net;

import java.io.Serializable;
import javax.jms.JMSException;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.NamingException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.ManagerFactory;

public class JMSTopicManager
  extends AbstractJMSManager
{
  private static final JMSTopicManagerFactory FACTORY = new JMSTopicManagerFactory(null);
  private TopicInfo info;
  private final String factoryBindingName;
  private final String topicBindingName;
  private final String userName;
  private final String password;
  private final Context context;
  
  protected JMSTopicManager(String name, Context context, String factoryBindingName, String topicBindingName, String userName, String password, TopicInfo info)
  {
    super(name);
    this.context = context;
    this.factoryBindingName = factoryBindingName;
    this.topicBindingName = topicBindingName;
    this.userName = userName;
    this.password = password;
    this.info = info;
  }
  
  public static JMSTopicManager getJMSTopicManager(String factoryName, String providerURL, String urlPkgPrefixes, String securityPrincipalName, String securityCredentials, String factoryBindingName, String topicBindingName, String userName, String password)
  {
    if (factoryBindingName == null)
    {
      LOGGER.error("No factory name provided for JMSTopicManager");
      return null;
    }
    if (topicBindingName == null)
    {
      LOGGER.error("No topic name provided for JMSTopicManager");
      return null;
    }
    String name = "JMSTopic:" + factoryBindingName + '.' + topicBindingName;
    return (JMSTopicManager)getManager(name, FACTORY, new FactoryData(factoryName, providerURL, urlPkgPrefixes, securityPrincipalName, securityCredentials, factoryBindingName, topicBindingName, userName, password));
  }
  
  public void send(Serializable object)
    throws Exception
  {
    if (this.info == null) {
      this.info = connect(this.context, this.factoryBindingName, this.topicBindingName, this.userName, this.password, false);
    }
    try
    {
      super.send(object, this.info.session, this.info.publisher);
    }
    catch (Exception ex)
    {
      cleanup(true);
      throw ex;
    }
  }
  
  public void releaseSub()
  {
    if (this.info != null) {
      cleanup(false);
    }
  }
  
  private void cleanup(boolean quiet)
  {
    try
    {
      this.info.session.close();
    }
    catch (Exception e)
    {
      if (!quiet) {
        LOGGER.error("Error closing session for " + getName(), e);
      }
    }
    try
    {
      this.info.conn.close();
    }
    catch (Exception e)
    {
      if (!quiet) {
        LOGGER.error("Error closing connection for " + getName(), e);
      }
    }
    this.info = null;
  }
  
  private static class FactoryData
  {
    private final String factoryName;
    private final String providerURL;
    private final String urlPkgPrefixes;
    private final String securityPrincipalName;
    private final String securityCredentials;
    private final String factoryBindingName;
    private final String topicBindingName;
    private final String userName;
    private final String password;
    
    public FactoryData(String factoryName, String providerURL, String urlPkgPrefixes, String securityPrincipalName, String securityCredentials, String factoryBindingName, String topicBindingName, String userName, String password)
    {
      this.factoryName = factoryName;
      this.providerURL = providerURL;
      this.urlPkgPrefixes = urlPkgPrefixes;
      this.securityPrincipalName = securityPrincipalName;
      this.securityCredentials = securityCredentials;
      this.factoryBindingName = factoryBindingName;
      this.topicBindingName = topicBindingName;
      this.userName = userName;
      this.password = password;
    }
  }
  
  private static TopicInfo connect(Context context, String factoryBindingName, String queueBindingName, String userName, String password, boolean suppress)
    throws Exception
  {
    try
    {
      TopicConnectionFactory factory = (TopicConnectionFactory)lookup(context, factoryBindingName);
      TopicConnection conn;
      TopicConnection conn;
      if (userName != null) {
        conn = factory.createTopicConnection(userName, password);
      } else {
        conn = factory.createTopicConnection();
      }
      TopicSession sess = conn.createTopicSession(false, 1);
      Topic topic = (Topic)lookup(context, queueBindingName);
      TopicPublisher publisher = sess.createPublisher(topic);
      conn.start();
      return new TopicInfo(conn, sess, publisher);
    }
    catch (NamingException ex)
    {
      LOGGER.warn("Unable to locate connection factory " + factoryBindingName, ex);
      if (!suppress) {
        throw ex;
      }
    }
    catch (JMSException ex)
    {
      LOGGER.warn("Unable to create connection to queue " + queueBindingName, ex);
      if (!suppress) {
        throw ex;
      }
    }
    return null;
  }
  
  private static class TopicInfo
  {
    private final TopicConnection conn;
    private final TopicSession session;
    private final TopicPublisher publisher;
    
    public TopicInfo(TopicConnection conn, TopicSession session, TopicPublisher publisher)
    {
      this.conn = conn;
      this.session = session;
      this.publisher = publisher;
    }
  }
  
  private static class JMSTopicManagerFactory
    implements ManagerFactory<JMSTopicManager, JMSTopicManager.FactoryData>
  {
    public JMSTopicManager createManager(String name, JMSTopicManager.FactoryData data)
    {
      try
      {
        Context ctx = AbstractJMSManager.createContext(JMSTopicManager.FactoryData.access$400(data), JMSTopicManager.FactoryData.access$500(data), JMSTopicManager.FactoryData.access$600(data), JMSTopicManager.FactoryData.access$700(data), JMSTopicManager.FactoryData.access$800(data));
        
        JMSTopicManager.TopicInfo info = JMSTopicManager.connect(ctx, JMSTopicManager.FactoryData.access$900(data), JMSTopicManager.FactoryData.access$1000(data), JMSTopicManager.FactoryData.access$1100(data), JMSTopicManager.FactoryData.access$1200(data), true);
        
        return new JMSTopicManager(name, ctx, JMSTopicManager.FactoryData.access$900(data), JMSTopicManager.FactoryData.access$1000(data), JMSTopicManager.FactoryData.access$1100(data), JMSTopicManager.FactoryData.access$1200(data), info);
      }
      catch (NamingException ex)
      {
        JMSTopicManager.LOGGER.error("Unable to locate resource", ex);
      }
      catch (Exception ex)
      {
        JMSTopicManager.LOGGER.error("Unable to connect", ex);
      }
      return null;
    }
  }
}
