package gnu.trove.impl.sync;

import gnu.trove.function.TObjectFunction;
import gnu.trove.iterator.TLongObjectIterator;
import gnu.trove.map.TLongObjectMap;
import gnu.trove.set.TLongSet;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

public class TSynchronizedLongObjectMap<V>
  implements TLongObjectMap<V>, Serializable
{
  private static final long serialVersionUID = 1978198479659022715L;
  private final TLongObjectMap<V> m;
  final Object mutex;
  
  public TSynchronizedLongObjectMap(TLongObjectMap<V> m)
  {
    if (m == null) {
      throw new NullPointerException();
    }
    this.m = m;
    this.mutex = this;
  }
  
  public TSynchronizedLongObjectMap(TLongObjectMap<V> m, Object mutex)
  {
    this.m = m;
    this.mutex = mutex;
  }
  
  /* Error */
  public int size()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedLongObjectMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedLongObjectMap:m	Lgnu/trove/map/TLongObjectMap;
    //   11: invokeinterface 48 1 0
    //   16: aload_1
    //   17: monitorexit
    //   18: ireturn
    //   19: astore_2
    //   20: aload_1
    //   21: monitorexit
    //   22: aload_2
    //   23: athrow
    // Line number table:
    //   Java source line #73	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	24	0	this	TSynchronizedLongObjectMap<V>
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
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedLongObjectMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedLongObjectMap:m	Lgnu/trove/map/TLongObjectMap;
    //   11: invokeinterface 52 1 0
    //   16: aload_1
    //   17: monitorexit
    //   18: ireturn
    //   19: astore_2
    //   20: aload_1
    //   21: monitorexit
    //   22: aload_2
    //   23: athrow
    // Line number table:
    //   Java source line #76	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	24	0	this	TSynchronizedLongObjectMap<V>
    //   5	16	1	Ljava/lang/Object;	Object
    //   19	4	2	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	18	19	finally
    //   19	22	19	finally
  }
  
  /* Error */
  public boolean containsKey(long key)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedLongObjectMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_3
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedLongObjectMap:m	Lgnu/trove/map/TLongObjectMap;
    //   11: lload_1
    //   12: invokeinterface 56 3 0
    //   17: aload_3
    //   18: monitorexit
    //   19: ireturn
    //   20: astore 4
    //   22: aload_3
    //   23: monitorexit
    //   24: aload 4
    //   26: athrow
    // Line number table:
    //   Java source line #79	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	27	0	this	TSynchronizedLongObjectMap<V>
    //   0	27	1	key	long
    //   5	18	3	Ljava/lang/Object;	Object
    //   20	5	4	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	24	20	finally
  }
  
  /* Error */
  public boolean containsValue(Object value)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedLongObjectMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedLongObjectMap:m	Lgnu/trove/map/TLongObjectMap;
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
    //   Java source line #82	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	TSynchronizedLongObjectMap<V>
    //   0	25	1	value	Object
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public V get(long key)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedLongObjectMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_3
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedLongObjectMap:m	Lgnu/trove/map/TLongObjectMap;
    //   11: lload_1
    //   12: invokeinterface 66 3 0
    //   17: aload_3
    //   18: monitorexit
    //   19: areturn
    //   20: astore 4
    //   22: aload_3
    //   23: monitorexit
    //   24: aload 4
    //   26: athrow
    // Line number table:
    //   Java source line #85	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	27	0	this	TSynchronizedLongObjectMap<V>
    //   0	27	1	key	long
    //   5	18	3	Ljava/lang/Object;	Object
    //   20	5	4	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	24	20	finally
  }
  
  /* Error */
  public V put(long key, V value)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedLongObjectMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore 4
    //   7: monitorenter
    //   8: aload_0
    //   9: getfield 38	gnu/trove/impl/sync/TSynchronizedLongObjectMap:m	Lgnu/trove/map/TLongObjectMap;
    //   12: lload_1
    //   13: aload_3
    //   14: invokeinterface 70 4 0
    //   19: aload 4
    //   21: monitorexit
    //   22: areturn
    //   23: astore 5
    //   25: aload 4
    //   27: monitorexit
    //   28: aload 5
    //   30: athrow
    // Line number table:
    //   Java source line #89	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	31	0	this	TSynchronizedLongObjectMap<V>
    //   0	31	1	key	long
    //   0	31	3	value	V
    //   5	21	4	Ljava/lang/Object;	Object
    //   23	6	5	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   8	22	23	finally
    //   23	28	23	finally
  }
  
  /* Error */
  public V remove(long key)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedLongObjectMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_3
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedLongObjectMap:m	Lgnu/trove/map/TLongObjectMap;
    //   11: lload_1
    //   12: invokeinterface 74 3 0
    //   17: aload_3
    //   18: monitorexit
    //   19: areturn
    //   20: astore 4
    //   22: aload_3
    //   23: monitorexit
    //   24: aload 4
    //   26: athrow
    // Line number table:
    //   Java source line #92	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	27	0	this	TSynchronizedLongObjectMap<V>
    //   0	27	1	key	long
    //   5	18	3	Ljava/lang/Object;	Object
    //   20	5	4	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	24	20	finally
  }
  
  public void putAll(Map<? extends Long, ? extends V> map)
  {
    synchronized (this.mutex)
    {
      this.m.putAll(map);
    }
  }
  
  public void putAll(TLongObjectMap<? extends V> map)
  {
    synchronized (this.mutex)
    {
      this.m.putAll(map);
    }
  }
  
  public void clear()
  {
    synchronized (this.mutex)
    {
      this.m.clear();
    }
  }
  
  private transient TLongSet keySet = null;
  private transient Collection<V> values = null;
  
  public TLongSet keySet()
  {
    synchronized (this.mutex)
    {
      if (this.keySet == null) {
        this.keySet = new TSynchronizedLongSet(this.m.keySet(), this.mutex);
      }
      return this.keySet;
    }
  }
  
  /* Error */
  public long[] keys()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedLongObjectMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedLongObjectMap:m	Lgnu/trove/map/TLongObjectMap;
    //   11: invokeinterface 99 1 0
    //   16: aload_1
    //   17: monitorexit
    //   18: areturn
    //   19: astore_2
    //   20: aload_1
    //   21: monitorexit
    //   22: aload_2
    //   23: athrow
    // Line number table:
    //   Java source line #115	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	24	0	this	TSynchronizedLongObjectMap<V>
    //   5	16	1	Ljava/lang/Object;	Object
    //   19	4	2	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	18	19	finally
    //   19	22	19	finally
  }
  
  /* Error */
  public long[] keys(long[] array)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedLongObjectMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedLongObjectMap:m	Lgnu/trove/map/TLongObjectMap;
    //   11: aload_1
    //   12: invokeinterface 102 2 0
    //   17: aload_2
    //   18: monitorexit
    //   19: areturn
    //   20: astore_3
    //   21: aload_2
    //   22: monitorexit
    //   23: aload_3
    //   24: athrow
    // Line number table:
    //   Java source line #118	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	TSynchronizedLongObjectMap<V>
    //   0	25	1	array	long[]
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  public Collection<V> valueCollection()
  {
    synchronized (this.mutex)
    {
      if (this.values == null) {
        this.values = new SynchronizedCollection(this.m.valueCollection(), this.mutex);
      }
      return this.values;
    }
  }
  
  /* Error */
  public Object[] values()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedLongObjectMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedLongObjectMap:m	Lgnu/trove/map/TLongObjectMap;
    //   11: invokeinterface 116 1 0
    //   16: aload_1
    //   17: monitorexit
    //   18: areturn
    //   19: astore_2
    //   20: aload_1
    //   21: monitorexit
    //   22: aload_2
    //   23: athrow
    // Line number table:
    //   Java source line #130	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	24	0	this	TSynchronizedLongObjectMap<V>
    //   5	16	1	Ljava/lang/Object;	Object
    //   19	4	2	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	18	19	finally
    //   19	22	19	finally
  }
  
  /* Error */
  public V[] values(V[] array)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedLongObjectMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedLongObjectMap:m	Lgnu/trove/map/TLongObjectMap;
    //   11: aload_1
    //   12: invokeinterface 119 2 0
    //   17: aload_2
    //   18: monitorexit
    //   19: areturn
    //   20: astore_3
    //   21: aload_2
    //   22: monitorexit
    //   23: aload_3
    //   24: athrow
    // Line number table:
    //   Java source line #133	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	TSynchronizedLongObjectMap<V>
    //   0	25	1	array	V[]
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  public TLongObjectIterator<V> iterator()
  {
    return this.m.iterator();
  }
  
  public long getNoEntryKey()
  {
    return this.m.getNoEntryKey();
  }
  
  /* Error */
  public V putIfAbsent(long key, V value)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedLongObjectMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore 4
    //   7: monitorenter
    //   8: aload_0
    //   9: getfield 38	gnu/trove/impl/sync/TSynchronizedLongObjectMap:m	Lgnu/trove/map/TLongObjectMap;
    //   12: lload_1
    //   13: aload_3
    //   14: invokeinterface 132 4 0
    //   19: aload 4
    //   21: monitorexit
    //   22: areturn
    //   23: astore 5
    //   25: aload 4
    //   27: monitorexit
    //   28: aload 5
    //   30: athrow
    // Line number table:
    //   Java source line #144	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	31	0	this	TSynchronizedLongObjectMap<V>
    //   0	31	1	key	long
    //   0	31	3	value	V
    //   5	21	4	Ljava/lang/Object;	Object
    //   23	6	5	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   8	22	23	finally
    //   23	28	23	finally
  }
  
  /* Error */
  public boolean forEachKey(gnu.trove.procedure.TLongProcedure procedure)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedLongObjectMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedLongObjectMap:m	Lgnu/trove/map/TLongObjectMap;
    //   11: aload_1
    //   12: invokeinterface 136 2 0
    //   17: aload_2
    //   18: monitorexit
    //   19: ireturn
    //   20: astore_3
    //   21: aload_2
    //   22: monitorexit
    //   23: aload_3
    //   24: athrow
    // Line number table:
    //   Java source line #147	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	TSynchronizedLongObjectMap<V>
    //   0	25	1	procedure	gnu.trove.procedure.TLongProcedure
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public boolean forEachValue(gnu.trove.procedure.TObjectProcedure<? super V> procedure)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedLongObjectMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedLongObjectMap:m	Lgnu/trove/map/TLongObjectMap;
    //   11: aload_1
    //   12: invokeinterface 142 2 0
    //   17: aload_2
    //   18: monitorexit
    //   19: ireturn
    //   20: astore_3
    //   21: aload_2
    //   22: monitorexit
    //   23: aload_3
    //   24: athrow
    // Line number table:
    //   Java source line #150	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	TSynchronizedLongObjectMap<V>
    //   0	25	1	procedure	gnu.trove.procedure.TObjectProcedure<? super V>
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public boolean forEachEntry(gnu.trove.procedure.TLongObjectProcedure<? super V> procedure)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedLongObjectMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedLongObjectMap:m	Lgnu/trove/map/TLongObjectMap;
    //   11: aload_1
    //   12: invokeinterface 148 2 0
    //   17: aload_2
    //   18: monitorexit
    //   19: ireturn
    //   20: astore_3
    //   21: aload_2
    //   22: monitorexit
    //   23: aload_3
    //   24: athrow
    // Line number table:
    //   Java source line #153	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	TSynchronizedLongObjectMap<V>
    //   0	25	1	procedure	gnu.trove.procedure.TLongObjectProcedure<? super V>
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  public void transformValues(TObjectFunction<V, V> function)
  {
    synchronized (this.mutex)
    {
      this.m.transformValues(function);
    }
  }
  
  /* Error */
  public boolean retainEntries(gnu.trove.procedure.TLongObjectProcedure<? super V> procedure)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedLongObjectMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedLongObjectMap:m	Lgnu/trove/map/TLongObjectMap;
    //   11: aload_1
    //   12: invokeinterface 160 2 0
    //   17: aload_2
    //   18: monitorexit
    //   19: ireturn
    //   20: astore_3
    //   21: aload_2
    //   22: monitorexit
    //   23: aload_3
    //   24: athrow
    // Line number table:
    //   Java source line #159	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	TSynchronizedLongObjectMap<V>
    //   0	25	1	procedure	gnu.trove.procedure.TLongObjectProcedure<? super V>
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public boolean equals(Object o)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedLongObjectMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedLongObjectMap:m	Lgnu/trove/map/TLongObjectMap;
    //   11: aload_1
    //   12: invokevirtual 163	java/lang/Object:equals	(Ljava/lang/Object;)Z
    //   15: aload_2
    //   16: monitorexit
    //   17: ireturn
    //   18: astore_3
    //   19: aload_2
    //   20: monitorexit
    //   21: aload_3
    //   22: athrow
    // Line number table:
    //   Java source line #163	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	23	0	this	TSynchronizedLongObjectMap<V>
    //   0	23	1	o	Object
    //   5	15	2	Ljava/lang/Object;	Object
    //   18	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	17	18	finally
    //   18	21	18	finally
  }
  
  /* Error */
  public int hashCode()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedLongObjectMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedLongObjectMap:m	Lgnu/trove/map/TLongObjectMap;
    //   11: invokevirtual 167	java/lang/Object:hashCode	()I
    //   14: aload_1
    //   15: monitorexit
    //   16: ireturn
    //   17: astore_2
    //   18: aload_1
    //   19: monitorexit
    //   20: aload_2
    //   21: athrow
    // Line number table:
    //   Java source line #166	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	22	0	this	TSynchronizedLongObjectMap<V>
    //   5	14	1	Ljava/lang/Object;	Object
    //   17	4	2	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	16	17	finally
    //   17	20	17	finally
  }
  
  /* Error */
  public String toString()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedLongObjectMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedLongObjectMap:m	Lgnu/trove/map/TLongObjectMap;
    //   11: invokevirtual 171	java/lang/Object:toString	()Ljava/lang/String;
    //   14: aload_1
    //   15: monitorexit
    //   16: areturn
    //   17: astore_2
    //   18: aload_1
    //   19: monitorexit
    //   20: aload_2
    //   21: athrow
    // Line number table:
    //   Java source line #169	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	22	0	this	TSynchronizedLongObjectMap<V>
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
