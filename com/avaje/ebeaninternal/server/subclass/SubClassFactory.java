package com.avaje.ebeaninternal.server.subclass;

import com.avaje.ebean.enhance.agent.ClassPathClassBytesReader;
import com.avaje.ebean.enhance.agent.EnhanceConstants;
import com.avaje.ebean.enhance.agent.EnhanceContext;
import com.avaje.ebean.enhance.asm.ClassReader;
import com.avaje.ebean.enhance.asm.ClassWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SubClassFactory
  extends ClassLoader
  implements EnhanceConstants, GenSuffix
{
  private static final Logger logger = Logger.getLogger(SubClassFactory.class.getName());
  private static final int CLASS_WRITER_FLAGS = 3;
  private final EnhanceContext enhanceContext;
  private final ClassLoader parentClassLoader;
  
  public SubClassFactory(ClassLoader parent, int logLevel)
  {
    super(parent);
    this.parentClassLoader = parent;
    
    ClassPathClassBytesReader reader = new ClassPathClassBytesReader(null);
    this.enhanceContext = new EnhanceContext(reader, true, "debug=" + logLevel);
  }
  
  public Class<?> create(Class<?> normalClass, String serverName)
    throws IOException
  {
    String subClassSuffix = "$$EntityBean";
    if (serverName != null) {
      subClassSuffix = subClassSuffix + "$" + serverName;
    }
    String clsName = normalClass.getName();
    String subClsName = clsName + subClassSuffix;
    try
    {
      byte[] newClsBytes = subclassBytes(clsName, subClassSuffix);
      
      return defineClass(subClsName, newClsBytes, 0, newClsBytes.length);
    }
    catch (IOException ex)
    {
      String m = "Error creating subclass for [" + clsName + "]";
      logger.log(Level.SEVERE, m, ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      String m = "Error creating subclass for [" + clsName + "]";
      logger.log(Level.SEVERE, m, ex);
      throw new RuntimeException(ex);
    }
  }
  
  private byte[] subclassBytes(String className, String subClassSuffix)
    throws IOException
  {
    String resName = className.replace('.', '/') + ".class";
    
    InputStream is = getResourceAsStream(resName);
    
    ClassReader cr = new ClassReader(is);
    ClassWriter cw = new ClassWriter(3);
    
    SubClassClassAdpater ca = new SubClassClassAdpater(subClassSuffix, cw, this.parentClassLoader, this.enhanceContext);
    if (ca.isLog(1)) {
      ca.log(" enhancing " + className + subClassSuffix);
    }
    cr.accept(ca, 0);
    
    return cw.toByteArray();
  }
}
