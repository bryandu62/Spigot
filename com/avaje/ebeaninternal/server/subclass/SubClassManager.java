package com.avaje.ebeaninternal.server.subclass;

import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.enhance.agent.EnhanceConstants;
import com.avaje.ebeaninternal.api.ClassUtil;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import javax.persistence.PersistenceException;

public class SubClassManager
  implements EnhanceConstants
{
  private static final Logger logger = Logger.getLogger(SubClassManager.class.getName());
  private final ConcurrentHashMap<String, Class<?>> clzMap;
  private final SubClassFactory subclassFactory;
  private final String serverName;
  private final int logLevel;
  
  public SubClassManager(ServerConfig serverConfig)
  {
    String s = serverConfig.getProperty("subClassManager.preferContextClassloader", "true");
    final boolean preferContext = "true".equalsIgnoreCase(s);
    
    this.serverName = serverConfig.getName();
    this.logLevel = serverConfig.getEnhanceLogLevel();
    this.clzMap = new ConcurrentHashMap();
    try
    {
      this.subclassFactory = ((SubClassFactory)AccessController.doPrivileged(new PrivilegedExceptionAction()
      {
        public Object run()
        {
          ClassLoader cl = ClassUtil.getClassLoader(getClass(), preferContext);
          SubClassManager.logger.info("SubClassFactory parent ClassLoader [" + cl.getClass().getName() + "]");
          return new SubClassFactory(cl, SubClassManager.this.logLevel);
        }
      }));
    }
    catch (PrivilegedActionException e)
    {
      throw new PersistenceException(e);
    }
  }
  
  public Class<?> resolve(String name)
  {
    synchronized (this)
    {
      String superName = SubClassUtil.getSuperClassName(name);
      Class<?> clz = (Class)this.clzMap.get(superName);
      if (clz == null)
      {
        clz = createClass(superName);
        this.clzMap.put(superName, clz);
      }
      return clz;
    }
  }
  
  private Class<?> createClass(String name)
  {
    try
    {
      Class<?> superClass = Class.forName(name, true, this.subclassFactory.getParent());
      
      return this.subclassFactory.create(superClass, this.serverName);
    }
    catch (Exception ex)
    {
      String m = "Error creating subclass for [" + name + "]";
      throw new PersistenceException(m, ex);
    }
  }
}
