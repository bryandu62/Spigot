package com.avaje.ebeaninternal.server.transaction;

import com.avaje.ebean.bean.PersistenceContext;
import com.avaje.ebeaninternal.api.Monitor;
import com.avaje.ebeaninternal.server.subclass.SubClassUtil;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public final class DefaultPersistenceContext
  implements PersistenceContext
{
  private final HashMap<String, ClassContext> typeCache = new HashMap();
  private final Monitor monitor = new Monitor();
  
  public void put(Object id, Object bean)
  {
    synchronized (this.monitor)
    {
      getClassContext(bean.getClass()).put(id, bean);
    }
  }
  
  /* Error */
  public Object putIfAbsent(Object id, Object bean)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 36	com/avaje/ebeaninternal/server/transaction/DefaultPersistenceContext:monitor	Lcom/avaje/ebeaninternal/api/Monitor;
    //   4: dup
    //   5: astore_3
    //   6: monitorenter
    //   7: aload_0
    //   8: aload_2
    //   9: invokevirtual 44	java/lang/Object:getClass	()Ljava/lang/Class;
    //   12: invokespecial 48	com/avaje/ebeaninternal/server/transaction/DefaultPersistenceContext:getClassContext	(Ljava/lang/Class;)Lcom/avaje/ebeaninternal/server/transaction/DefaultPersistenceContext$ClassContext;
    //   15: aload_1
    //   16: aload_2
    //   17: invokestatic 61	com/avaje/ebeaninternal/server/transaction/DefaultPersistenceContext$ClassContext:access$100	(Lcom/avaje/ebeaninternal/server/transaction/DefaultPersistenceContext$ClassContext;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   20: aload_3
    //   21: monitorexit
    //   22: areturn
    //   23: astore 4
    //   25: aload_3
    //   26: monitorexit
    //   27: aload 4
    //   29: athrow
    // Line number table:
    //   Java source line #72	-> byte code offset #0
    //   Java source line #73	-> byte code offset #7
    //   Java source line #74	-> byte code offset #23
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	30	0	this	DefaultPersistenceContext
    //   0	30	1	id	Object
    //   0	30	2	bean	Object
    //   5	21	3	Ljava/lang/Object;	Object
    //   23	5	4	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	22	23	finally
    //   23	27	23	finally
  }
  
  /* Error */
  public Object get(Class<?> beanType, Object id)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 36	com/avaje/ebeaninternal/server/transaction/DefaultPersistenceContext:monitor	Lcom/avaje/ebeaninternal/api/Monitor;
    //   4: dup
    //   5: astore_3
    //   6: monitorenter
    //   7: aload_0
    //   8: aload_1
    //   9: invokespecial 48	com/avaje/ebeaninternal/server/transaction/DefaultPersistenceContext:getClassContext	(Ljava/lang/Class;)Lcom/avaje/ebeaninternal/server/transaction/DefaultPersistenceContext$ClassContext;
    //   12: aload_2
    //   13: invokestatic 67	com/avaje/ebeaninternal/server/transaction/DefaultPersistenceContext$ClassContext:access$200	(Lcom/avaje/ebeaninternal/server/transaction/DefaultPersistenceContext$ClassContext;Ljava/lang/Object;)Ljava/lang/Object;
    //   16: aload_3
    //   17: monitorexit
    //   18: areturn
    //   19: astore 4
    //   21: aload_3
    //   22: monitorexit
    //   23: aload 4
    //   25: athrow
    // Line number table:
    //   Java source line #83	-> byte code offset #0
    //   Java source line #84	-> byte code offset #7
    //   Java source line #85	-> byte code offset #19
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	26	0	this	DefaultPersistenceContext
    //   0	26	1	beanType	Class<?>
    //   0	26	2	id	Object
    //   5	17	3	Ljava/lang/Object;	Object
    //   19	5	4	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	18	19	finally
    //   19	23	19	finally
  }
  
  public int size(Class<?> beanType)
  {
    synchronized (this.monitor)
    {
      ClassContext classMap = (ClassContext)this.typeCache.get(beanType.getName());
      return classMap == null ? 0 : classMap.size();
    }
  }
  
  public void clear()
  {
    synchronized (this.monitor)
    {
      this.typeCache.clear();
    }
  }
  
  public void clear(Class<?> beanType)
  {
    synchronized (this.monitor)
    {
      ClassContext classMap = (ClassContext)this.typeCache.get(beanType.getName());
      if (classMap != null) {
        classMap.clear();
      }
    }
  }
  
  public void clear(Class<?> beanType, Object id)
  {
    synchronized (this.monitor)
    {
      ClassContext classMap = (ClassContext)this.typeCache.get(beanType.getName());
      if ((classMap != null) && (id != null)) {
        classMap.remove(id);
      }
    }
  }
  
  public String toString()
  {
    synchronized (this.monitor)
    {
      StringBuilder sb = new StringBuilder();
      Iterator<Map.Entry<String, ClassContext>> it = this.typeCache.entrySet().iterator();
      while (it.hasNext())
      {
        Map.Entry<String, ClassContext> entry = (Map.Entry)it.next();
        if (((ClassContext)entry.getValue()).size() > 0) {
          sb.append((String)entry.getKey() + ":" + ((ClassContext)entry.getValue()).size() + "; ");
        }
      }
      return sb.toString();
    }
  }
  
  private ClassContext getClassContext(Class<?> beanType)
  {
    String clsName = SubClassUtil.getSuperClassName(beanType.getName());
    
    ClassContext classMap = (ClassContext)this.typeCache.get(clsName);
    if (classMap == null)
    {
      classMap = new ClassContext(null);
      this.typeCache.put(clsName, classMap);
    }
    return classMap;
  }
  
  private static class ClassContext
  {
    private final WeakValueMap<Object, Object> map = new WeakValueMap();
    
    private Object get(Object id)
    {
      return this.map.get(id);
    }
    
    private Object putIfAbsent(Object id, Object bean)
    {
      return this.map.putIfAbsent(id, bean);
    }
    
    private void put(Object id, Object b)
    {
      this.map.put(id, b);
    }
    
    private int size()
    {
      return this.map.size();
    }
    
    private void clear()
    {
      this.map.clear();
    }
    
    private Object remove(Object id)
    {
      return this.map.remove(id);
    }
  }
}
