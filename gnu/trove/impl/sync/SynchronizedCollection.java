package gnu.trove.impl.sync;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

class SynchronizedCollection<E>
  implements Collection<E>, Serializable
{
  private static final long serialVersionUID = 3053995032091335093L;
  final Collection<E> c;
  final Object mutex;
  
  SynchronizedCollection(Collection<E> c, Object mutex)
  {
    this.c = c;
    this.mutex = mutex;
  }
  
  /* Error */
  public int size()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 28	gnu/trove/impl/sync/SynchronizedCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 26	gnu/trove/impl/sync/SynchronizedCollection:c	Ljava/util/Collection;
    //   11: invokeinterface 35 1 0
    //   16: aload_1
    //   17: monitorexit
    //   18: ireturn
    //   19: astore_2
    //   20: aload_1
    //   21: monitorexit
    //   22: aload_2
    //   23: athrow
    // Line number table:
    //   Java source line #43	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	24	0	this	SynchronizedCollection<E>
    //   5	16	1	Ljava/lang/Object;	Object
    //   19	4	2	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	18	19	finally
    //   19	22	19	finally
  }
  
  /* Error */
  public boolean isEmpty()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 28	gnu/trove/impl/sync/SynchronizedCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 26	gnu/trove/impl/sync/SynchronizedCollection:c	Ljava/util/Collection;
    //   11: invokeinterface 39 1 0
    //   16: aload_1
    //   17: monitorexit
    //   18: ireturn
    //   19: astore_2
    //   20: aload_1
    //   21: monitorexit
    //   22: aload_2
    //   23: athrow
    // Line number table:
    //   Java source line #46	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	24	0	this	SynchronizedCollection<E>
    //   5	16	1	Ljava/lang/Object;	Object
    //   19	4	2	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	18	19	finally
    //   19	22	19	finally
  }
  
  /* Error */
  public boolean contains(Object o)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 28	gnu/trove/impl/sync/SynchronizedCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 26	gnu/trove/impl/sync/SynchronizedCollection:c	Ljava/util/Collection;
    //   11: aload_1
    //   12: invokeinterface 43 2 0
    //   17: aload_2
    //   18: monitorexit
    //   19: ireturn
    //   20: astore_3
    //   21: aload_2
    //   22: monitorexit
    //   23: aload_3
    //   24: athrow
    // Line number table:
    //   Java source line #49	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	SynchronizedCollection<E>
    //   0	25	1	o	Object
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public Object[] toArray()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 28	gnu/trove/impl/sync/SynchronizedCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 26	gnu/trove/impl/sync/SynchronizedCollection:c	Ljava/util/Collection;
    //   11: invokeinterface 48 1 0
    //   16: aload_1
    //   17: monitorexit
    //   18: areturn
    //   19: astore_2
    //   20: aload_1
    //   21: monitorexit
    //   22: aload_2
    //   23: athrow
    // Line number table:
    //   Java source line #52	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	24	0	this	SynchronizedCollection<E>
    //   5	16	1	Ljava/lang/Object;	Object
    //   19	4	2	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	18	19	finally
    //   19	22	19	finally
  }
  
  /* Error */
  public <T> T[] toArray(T[] a)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 28	gnu/trove/impl/sync/SynchronizedCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 26	gnu/trove/impl/sync/SynchronizedCollection:c	Ljava/util/Collection;
    //   11: aload_1
    //   12: invokeinterface 51 2 0
    //   17: aload_2
    //   18: monitorexit
    //   19: areturn
    //   20: astore_3
    //   21: aload_2
    //   22: monitorexit
    //   23: aload_3
    //   24: athrow
    // Line number table:
    //   Java source line #56	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	SynchronizedCollection<E>
    //   0	25	1	a	T[]
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  public Iterator<E> iterator()
  {
    return this.c.iterator();
  }
  
  /* Error */
  public boolean add(E e)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 28	gnu/trove/impl/sync/SynchronizedCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 26	gnu/trove/impl/sync/SynchronizedCollection:c	Ljava/util/Collection;
    //   11: aload_1
    //   12: invokeinterface 61 2 0
    //   17: aload_2
    //   18: monitorexit
    //   19: ireturn
    //   20: astore_3
    //   21: aload_2
    //   22: monitorexit
    //   23: aload_3
    //   24: athrow
    // Line number table:
    //   Java source line #64	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	SynchronizedCollection<E>
    //   0	25	1	e	E
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public boolean remove(Object o)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 28	gnu/trove/impl/sync/SynchronizedCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 26	gnu/trove/impl/sync/SynchronizedCollection:c	Ljava/util/Collection;
    //   11: aload_1
    //   12: invokeinterface 66 2 0
    //   17: aload_2
    //   18: monitorexit
    //   19: ireturn
    //   20: astore_3
    //   21: aload_2
    //   22: monitorexit
    //   23: aload_3
    //   24: athrow
    // Line number table:
    //   Java source line #67	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	SynchronizedCollection<E>
    //   0	25	1	o	Object
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public boolean containsAll(Collection<?> coll)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 28	gnu/trove/impl/sync/SynchronizedCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 26	gnu/trove/impl/sync/SynchronizedCollection:c	Ljava/util/Collection;
    //   11: aload_1
    //   12: invokeinterface 70 2 0
    //   17: aload_2
    //   18: monitorexit
    //   19: ireturn
    //   20: astore_3
    //   21: aload_2
    //   22: monitorexit
    //   23: aload_3
    //   24: athrow
    // Line number table:
    //   Java source line #71	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	SynchronizedCollection<E>
    //   0	25	1	coll	Collection<?>
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public boolean addAll(Collection<? extends E> coll)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 28	gnu/trove/impl/sync/SynchronizedCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 26	gnu/trove/impl/sync/SynchronizedCollection:c	Ljava/util/Collection;
    //   11: aload_1
    //   12: invokeinterface 75 2 0
    //   17: aload_2
    //   18: monitorexit
    //   19: ireturn
    //   20: astore_3
    //   21: aload_2
    //   22: monitorexit
    //   23: aload_3
    //   24: athrow
    // Line number table:
    //   Java source line #74	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	SynchronizedCollection<E>
    //   0	25	1	coll	Collection<? extends E>
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public boolean removeAll(Collection<?> coll)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 28	gnu/trove/impl/sync/SynchronizedCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 26	gnu/trove/impl/sync/SynchronizedCollection:c	Ljava/util/Collection;
    //   11: aload_1
    //   12: invokeinterface 79 2 0
    //   17: aload_2
    //   18: monitorexit
    //   19: ireturn
    //   20: astore_3
    //   21: aload_2
    //   22: monitorexit
    //   23: aload_3
    //   24: athrow
    // Line number table:
    //   Java source line #77	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	SynchronizedCollection<E>
    //   0	25	1	coll	Collection<?>
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public boolean retainAll(Collection<?> coll)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 28	gnu/trove/impl/sync/SynchronizedCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 26	gnu/trove/impl/sync/SynchronizedCollection:c	Ljava/util/Collection;
    //   11: aload_1
    //   12: invokeinterface 82 2 0
    //   17: aload_2
    //   18: monitorexit
    //   19: ireturn
    //   20: astore_3
    //   21: aload_2
    //   22: monitorexit
    //   23: aload_3
    //   24: athrow
    // Line number table:
    //   Java source line #80	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	SynchronizedCollection<E>
    //   0	25	1	coll	Collection<?>
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  public void clear()
  {
    synchronized (this.mutex)
    {
      this.c.clear();
    }
  }
  
  /* Error */
  public String toString()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 28	gnu/trove/impl/sync/SynchronizedCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 26	gnu/trove/impl/sync/SynchronizedCollection:c	Ljava/util/Collection;
    //   11: invokevirtual 89	java/lang/Object:toString	()Ljava/lang/String;
    //   14: aload_1
    //   15: monitorexit
    //   16: areturn
    //   17: astore_2
    //   18: aload_1
    //   19: monitorexit
    //   20: aload_2
    //   21: athrow
    // Line number table:
    //   Java source line #86	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	22	0	this	SynchronizedCollection<E>
    //   5	14	1	Ljava/lang/Object;	Object
    //   17	4	2	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	16	17	finally
    //   17	20	17	finally
  }
  
  private void writeObject(ObjectOutputStream s)
    throws IOException
  {
    synchronized (this.mutex)
    {
      s.defaultWriteObject();
    }
  }
}
