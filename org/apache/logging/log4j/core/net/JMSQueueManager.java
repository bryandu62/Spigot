package org.apache.logging.log4j.core.net;

import java.io.Serializable;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.naming.Context;
import javax.naming.NamingException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.ManagerFactory;

public class JMSQueueManager
  extends AbstractJMSManager
{
  private static final JMSQueueManagerFactory FACTORY = new JMSQueueManagerFactory(null);
  private QueueInfo info;
  private final String factoryBindingName;
  private final String queueBindingName;
  private final String userName;
  private final String password;
  private final Context context;
  
  protected JMSQueueManager(String name, Context context, String factoryBindingName, String queueBindingName, String userName, String password, QueueInfo info)
  {
    super(name);
    this.context = context;
    this.factoryBindingName = factoryBindingName;
    this.queueBindingName = queueBindingName;
    this.userName = userName;
    this.password = password;
    this.info = info;
  }
  
  public static JMSQueueManager getJMSQueueManager(String factoryName, String providerURL, String urlPkgPrefixes, String securityPrincipalName, String securityCredentials, String factoryBindingName, String queueBindingName, String userName, String password)
  {
    if (factoryBindingName == null)
    {
      LOGGER.error("No factory name provided for JMSQueueManager");
      return null;
    }
    if (queueBindingName == null)
    {
      LOGGER.error("No topic name provided for JMSQueueManager");
      return null;
    }
    String name = "JMSQueue:" + factoryBindingName + '.' + queueBindingName;
    return (JMSQueueManager)getManager(name, FACTORY, new FactoryData(factoryName, providerURL, urlPkgPrefixes, securityPrincipalName, securityCredentials, factoryBindingName, queueBindingName, userName, password));
  }
  
  public synchronized void send(Serializable object)
    throws Exception
  {
    if (this.info == null) {
      this.info = connect(this.context, this.factoryBindingName, this.queueBindingName, this.userName, this.password, false);
    }
    try
    {
      super.send(object, this.info.session, this.info.sender);
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
    private final String queueBindingName;
    private final String userName;
    private final String password;
    
    public FactoryData(String factoryName, String providerURL, String urlPkgPrefixes, String securityPrincipalName, String securityCredentials, String factoryBindingName, String queueBindingName, String userName, String password)
    {
      this.factoryName = factoryName;
      this.providerURL = providerURL;
      this.urlPkgPrefixes = urlPkgPrefixes;
      this.securityPrincipalName = securityPrincipalName;
      this.securityCredentials = securityCredentials;
      this.factoryBindingName = factoryBindingName;
      this.queueBindingName = queueBindingName;
      this.userName = userName;
      this.password = password;
    }
  }
  
  private static QueueInfo connect(Context context, String factoryBindingName, String queueBindingName, String userName, String password, boolean suppress)
    throws Exception
  {
    try
    {
      QueueConnectionFactory factory = (QueueConnectionFactory)lookup(context, factoryBindingName);
      QueueConnection conn;
      QueueConnection conn;
      if (userName != null) {
        conn = factory.createQueueConnection(userName, password);
      } else {
        conn = factory.createQueueConnection();
      }
      QueueSession sess = conn.createQueueSession(false, 1);
      Queue queue = (Queue)lookup(context, queueBindingName);
      QueueSender sender = sess.createSender(queue);
      conn.start();
      return new QueueInfo(conn, sess, sender);
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
  
  private static class QueueInfo
  {
    private final QueueConnection conn;
    private final QueueSession session;
    private final QueueSender sender;
    
    public QueueInfo(QueueConnection conn, QueueSession session, QueueSender sender)
    {
      this.conn = conn;
      this.session = session;
      this.sender = sender;
    }
  }
  
  private static class JMSQueueManagerFactory
    implements ManagerFactory<JMSQueueManager, JMSQueueManager.FactoryData>
  {
    public JMSQueueManager createManager(String name, JMSQueueManager.FactoryData data)
    {
      try
      {
        Context ctx = AbstractJMSManager.createContext(JMSQueueManager.FactoryData.access$400(data), JMSQueueManager.FactoryData.access$500(data), JMSQueueManager.FactoryData.access$600(data), JMSQueueManager.FactoryData.access$700(data), JMSQueueManager.FactoryData.access$800(data));
        
        JMSQueueManager.QueueInfo info = JMSQueueManager.connect(ctx, JMSQueueManager.FactoryData.access$900(data), JMSQueueManager.FactoryData.access$1000(data), JMSQueueManager.FactoryData.access$1100(data), JMSQueueManager.FactoryData.access$1200(data), true);
        
        return new JMSQueueManager(name, ctx, JMSQueueManager.FactoryData.access$900(data), JMSQueueManager.FactoryData.access$1000(data), JMSQueueManager.FactoryData.access$1100(data), JMSQueueManager.FactoryData.access$1200(data), info);
      }
      catch (NamingException ex)
      {
        JMSQueueManager.LOGGER.error("Unable to locate resource", ex);
      }
      catch (Exception ex)
      {
        JMSQueueManager.LOGGER.error("Unable to connect", ex);
      }
      return null;
    }
  }
}
