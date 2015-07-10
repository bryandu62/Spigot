package org.apache.logging.log4j.core.appender.db.nosql.couch;

import java.lang.reflect.Method;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.db.nosql.NoSQLProvider;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.NameUtil;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.status.StatusLogger;
import org.lightcouch.CouchDbClient;
import org.lightcouch.CouchDbProperties;

@Plugin(name="CouchDb", category="Core", printObject=true)
public final class CouchDBProvider
  implements NoSQLProvider<CouchDBConnection>
{
  private static final int HTTP = 80;
  private static final int HTTPS = 443;
  private static final Logger LOGGER = ;
  private final CouchDbClient client;
  private final String description;
  
  private CouchDBProvider(CouchDbClient client, String description)
  {
    this.client = client;
    this.description = ("couchDb{ " + description + " }");
  }
  
  public CouchDBConnection getConnection()
  {
    return new CouchDBConnection(this.client);
  }
  
  public String toString()
  {
    return this.description;
  }
  
  @PluginFactory
  public static CouchDBProvider createNoSQLProvider(@PluginAttribute("databaseName") String databaseName, @PluginAttribute("protocol") String protocol, @PluginAttribute("server") String server, @PluginAttribute("port") String port, @PluginAttribute("username") String username, @PluginAttribute("password") String password, @PluginAttribute("factoryClassName") String factoryClassName, @PluginAttribute("factoryMethodName") String factoryMethodName)
  {
    String description;
    CouchDbClient client;
    if ((factoryClassName != null) && (factoryClassName.length() > 0) && (factoryMethodName != null) && (factoryMethodName.length() > 0)) {
      try
      {
        Class<?> factoryClass = Class.forName(factoryClassName);
        Method method = factoryClass.getMethod(factoryMethodName, new Class[0]);
        Object object = method.invoke(null, new Object[0]);
        String description;
        if ((object instanceof CouchDbClient))
        {
          CouchDbClient client = (CouchDbClient)object;
          description = "uri=" + client.getDBUri();
        }
        else
        {
          String description;
          if ((object instanceof CouchDbProperties))
          {
            CouchDbProperties properties = (CouchDbProperties)object;
            CouchDbClient client = new CouchDbClient(properties);
            description = "uri=" + client.getDBUri() + ", username=" + properties.getUsername() + ", passwordHash=" + NameUtil.md5(new StringBuilder().append(password).append(CouchDBProvider.class.getName()).toString()) + ", maxConnections=" + properties.getMaxConnections() + ", connectionTimeout=" + properties.getConnectionTimeout() + ", socketTimeout=" + properties.getSocketTimeout();
          }
          else
          {
            if (object == null)
            {
              LOGGER.error("The factory method [{}.{}()] returned null.", new Object[] { factoryClassName, factoryMethodName });
              return null;
            }
            LOGGER.error("The factory method [{}.{}()] returned an unsupported type [{}].", new Object[] { factoryClassName, factoryMethodName, object.getClass().getName() });
            
            return null;
          }
        }
      }
      catch (ClassNotFoundException e)
      {
        LOGGER.error("The factory class [{}] could not be loaded.", new Object[] { factoryClassName, e });
        return null;
      }
      catch (NoSuchMethodException e)
      {
        LOGGER.error("The factory class [{}] does not have a no-arg method named [{}].", new Object[] { factoryClassName, factoryMethodName, e });
        
        return null;
      }
      catch (Exception e)
      {
        LOGGER.error("The factory method [{}.{}()] could not be invoked.", new Object[] { factoryClassName, factoryMethodName, e });
        
        return null;
      }
    }
    if ((databaseName != null) && (databaseName.length() > 0))
    {
      if ((protocol != null) && (protocol.length() > 0))
      {
        protocol = protocol.toLowerCase();
        if ((!protocol.equals("http")) && (!protocol.equals("https")))
        {
          LOGGER.error("Only protocols [http] and [https] are supported, [{}] specified.", new Object[] { protocol });
          return null;
        }
      }
      else
      {
        protocol = "http";
        LOGGER.warn("No protocol specified, using default port [http].");
      }
      int portInt = AbstractAppender.parseInt(port, protocol.equals("https") ? 443 : 80);
      if (Strings.isEmpty(server))
      {
        server = "localhost";
        LOGGER.warn("No server specified, using default server localhost.");
      }
      if ((Strings.isEmpty(username)) || (Strings.isEmpty(password)))
      {
        LOGGER.error("You must provide a username and password for the CouchDB provider.");
        return null;
      }
      client = new CouchDbClient(databaseName, false, protocol, server, portInt, username, password);
      description = "uri=" + client.getDBUri() + ", username=" + username + ", passwordHash=" + NameUtil.md5(new StringBuilder().append(password).append(CouchDBProvider.class.getName()).toString());
    }
    else
    {
      LOGGER.error("No factory method was provided so the database name is required.");
      return null;
    }
    String description;
    CouchDbClient client;
    return new CouchDBProvider(client, description);
  }
}
