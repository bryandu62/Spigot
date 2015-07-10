package com.avaje.ebeaninternal.server.core;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.bean.SerializeControl;
import com.avaje.ebeaninternal.api.ClassUtil;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.subclass.SubClassUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

public class ProxyBeanObjectInputStream
  extends ObjectInputStream
{
  private final SpiEbeanServer ebeanServer;
  
  public ProxyBeanObjectInputStream(InputStream in, EbeanServer ebeanServer)
    throws IOException
  {
    super(in);
    this.ebeanServer = ((SpiEbeanServer)ebeanServer);
    SerializeControl.setVanilla(false);
  }
  
  public void close()
    throws IOException
  {
    super.close();
    SerializeControl.resetToDefault();
  }
  
  protected Class<?> resolveGenerated(ObjectStreamClass desc)
    throws IOException, ClassNotFoundException
  {
    String className = desc.getName();
    
    String vanillaClassName = SubClassUtil.getSuperClassName(className);
    Class<?> vanillaClass = ClassUtil.forName(vanillaClassName, getClass());
    
    BeanDescriptor<?> d = this.ebeanServer.getBeanDescriptor(vanillaClass);
    if (d == null)
    {
      String msg = "Could not find BeanDescriptor for " + vanillaClassName;
      throw new IOException(msg);
    }
    return d.getFactoryType();
  }
  
  protected Class<?> resolveClass(ObjectStreamClass desc)
    throws IOException, ClassNotFoundException
  {
    String className = desc.getName();
    if (SubClassUtil.isSubClass(className)) {
      return resolveGenerated(desc);
    }
    return super.resolveClass(desc);
  }
}
