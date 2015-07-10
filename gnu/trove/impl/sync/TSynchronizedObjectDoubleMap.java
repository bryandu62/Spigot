package gnu.trove.impl.sync;

import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.iterator.TObjectDoubleIterator;
import gnu.trove.map.TObjectDoubleMap;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public class TSynchronizedObjectDoubleMap<K>
  implements TObjectDoubleMap<K>, Serializable
{
  private static final long serialVersionUID = 1978198479659022715L;
  private final TObjectDoubleMap<K> m;
  final Object mutex;
  
  public TSynchronizedObjectDoubleMap(TObjectDoubleMap<K> m)
  {
    if (m == null) {
      throw new NullPointerException();
    }
    this.m = m;
    this.mutex = this;
  }
  
  public TSynchronizedObjectDoubleMap(TObjectDoubleMap<K> m, Object mutex)
  {
    this.m = m;
    this.mutex = mutex;
  }
  
  /* Error */
  public int size()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedObjectDoubleMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedObjectDoubleMap:m	Lgnu/trove/map/TObjectDoubleMap;
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
    //   Java source line #71	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	24	0	this	TSynchronizedObjectDoubleMap<K>
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
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedObjectDoubleMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedObjectDoubleMap:m	Lgnu/trove/map/TObjectDoubleMap;
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
    //   Java source line #74	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	24	0	this	TSynchronizedObjectDoubleMap<K>
    //   5	16	1	Ljava/lang/Object;	Object
    //   19	4	2	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	18	19	finally
    //   19	22	19	finally
  }
  
  /* Error */
  public boolean containsKey(Object key)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedObjectDoubleMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedObjectDoubleMap:m	Lgnu/trove/map/TObjectDoubleMap;
    //   11: aload_1
    //   12: invokeinterface 56 2 0
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
    //   0	25	0	this	TSynchronizedObjectDoubleMap<K>
    //   0	25	1	key	Object
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public boolean containsValue(double value)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedObjectDoubleMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_3
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedObjectDoubleMap:m	Lgnu/trove/map/TObjectDoubleMap;
    //   11: dload_1
    //   12: invokeinterface 61 3 0
    //   17: aload_3
    //   18: monitorexit
    //   19: ireturn
    //   20: astore 4
    //   22: aload_3
    //   23: monitorexit
    //   24: aload 4
    //   26: athrow
    // Line number table:
    //   Java source line #80	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	27	0	this	TSynchronizedObjectDoubleMap<K>
    //   0	27	1	value	double
    //   5	18	3	Ljava/lang/Object;	Object
    //   20	5	4	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	24	20	finally
  }
  
  /* Error */
  public double get(Object key)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedObjectDoubleMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedObjectDoubleMap:m	Lgnu/trove/map/TObjectDoubleMap;
    //   11: aload_1
    //   12: invokeinterface 67 2 0
    //   17: aload_2
    //   18: monitorexit
    //   19: dreturn
    //   20: astore_3
    //   21: aload_2
    //   22: monitorexit
    //   23: aload_3
    //   24: athrow
    // Line number table:
    //   Java source line #83	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	TSynchronizedObjectDoubleMap<K>
    //   0	25	1	key	Object
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public double put(K key, double value)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedObjectDoubleMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore 4
    //   7: monitorenter
    //   8: aload_0
    //   9: getfield 38	gnu/trove/impl/sync/TSynchronizedObjectDoubleMap:m	Lgnu/trove/map/TObjectDoubleMap;
    //   12: aload_1
    //   13: dload_2
    //   14: invokeinterface 71 4 0
    //   19: aload 4
    //   21: monitorexit
    //   22: dreturn
    //   23: astore 5
    //   25: aload 4
    //   27: monitorexit
    //   28: aload 5
    //   30: athrow
    // Line number table:
    //   Java source line #87	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	31	0	this	TSynchronizedObjectDoubleMap<K>
    //   0	31	1	key	K
    //   0	31	2	value	double
    //   5	21	4	Ljava/lang/Object;	Object
    //   23	6	5	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   8	22	23	finally
    //   23	28	23	finally
  }
  
  /* Error */
  public double remove(Object key)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedObjectDoubleMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedObjectDoubleMap:m	Lgnu/trove/map/TObjectDoubleMap;
    //   11: aload_1
    //   12: invokeinterface 75 2 0
    //   17: aload_2
    //   18: monitorexit
    //   19: dreturn
    //   20: astore_3
    //   21: aload_2
    //   22: monitorexit
    //   23: aload_3
    //   24: athrow
    // Line number table:
    //   Java source line #90	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	TSynchronizedObjectDoubleMap<K>
    //   0	25	1	key	Object
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  public void putAll(Map<? extends K, ? extends Double> map)
  {
    synchronized (this.mutex)
    {
      this.m.putAll(map);
    }
  }
  
  public void putAll(TObjectDoubleMap<? extends K> map)
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
  
  private transient Set<K> keySet = null;
  private transient TDoubleCollection values = null;
  
  public Set<K> keySet()
  {
    synchronized (this.mutex)
    {
      if (this.keySet == null) {
        this.keySet = new SynchronizedSet(this.m.keySet(), this.mutex);
      }
      return this.keySet;
    }
  }
  
  /* Error */
  public Object[] keys()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedObjectDoubleMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedObjectDoubleMap:m	Lgnu/trove/map/TObjectDoubleMap;
    //   11: invokeinterface 100 1 0
    //   16: aload_1
    //   17: monitorexit
    //   18: areturn
    //   19: astore_2
    //   20: aload_1
    //   21: monitorexit
    //   22: aload_2
    //   23: athrow
    // Line number table:
    //   Java source line #114	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	24	0	this	TSynchronizedObjectDoubleMap<K>
    //   5	16	1	Ljava/lang/Object;	Object
    //   19	4	2	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	18	19	finally
    //   19	22	19	finally
  }
  
  /* Error */
  public K[] keys(K[] array)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedObjectDoubleMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedObjectDoubleMap:m	Lgnu/trove/map/TObjectDoubleMap;
    //   11: aload_1
    //   12: invokeinterface 103 2 0
    //   17: aload_2
    //   18: monitorexit
    //   19: areturn
    //   20: astore_3
    //   21: aload_2
    //   22: monitorexit
    //   23: aload_3
    //   24: athrow
    // Line number table:
    //   Java source line #117	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	TSynchronizedObjectDoubleMap<K>
    //   0	25	1	array	K[]
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  public TDoubleCollection valueCollection()
  {
    synchronized (this.mutex)
    {
      if (this.values == null) {
        this.values = new TSynchronizedDoubleCollection(this.m.valueCollection(), this.mutex);
      }
      return this.values;
    }
  }
  
  /* Error */
  public double[] values()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedObjectDoubleMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedObjectDoubleMap:m	Lgnu/trove/map/TObjectDoubleMap;
    //   11: invokeinterface 118 1 0
    //   16: aload_1
    //   17: monitorexit
    //   18: areturn
    //   19: astore_2
    //   20: aload_1
    //   21: monitorexit
    //   22: aload_2
    //   23: athrow
    // Line number table:
    //   Java source line #128	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	24	0	this	TSynchronizedObjectDoubleMap<K>
    //   5	16	1	Ljava/lang/Object;	Object
    //   19	4	2	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	18	19	finally
    //   19	22	19	finally
  }
  
  /* Error */
  public double[] values(double[] array)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedObjectDoubleMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedObjectDoubleMap:m	Lgnu/trove/map/TObjectDoubleMap;
    //   11: aload_1
    //   12: invokeinterface 121 2 0
    //   17: aload_2
    //   18: monitorexit
    //   19: areturn
    //   20: astore_3
    //   21: aload_2
    //   22: monitorexit
    //   23: aload_3
    //   24: athrow
    // Line number table:
    //   Java source line #131	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	TSynchronizedObjectDoubleMap<K>
    //   0	25	1	array	double[]
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  public TObjectDoubleIterator<K> iterator()
  {
    return this.m.iterator();
  }
  
  public double getNoEntryValue()
  {
    return this.m.getNoEntryValue();
  }
  
  /* Error */
  public double putIfAbsent(K key, double value)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedObjectDoubleMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore 4
    //   7: monitorenter
    //   8: aload_0
    //   9: getfield 38	gnu/trove/impl/sync/TSynchronizedObjectDoubleMap:m	Lgnu/trove/map/TObjectDoubleMap;
    //   12: aload_1
    //   13: dload_2
    //   14: invokeinterface 133 4 0
    //   19: aload 4
    //   21: monitorexit
    //   22: dreturn
    //   23: astore 5
    //   25: aload 4
    //   27: monitorexit
    //   28: aload 5
    //   30: athrow
    // Line number table:
    //   Java source line #142	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	31	0	this	TSynchronizedObjectDoubleMap<K>
    //   0	31	1	key	K
    //   0	31	2	value	double
    //   5	21	4	Ljava/lang/Object;	Object
    //   23	6	5	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   8	22	23	finally
    //   23	28	23	finally
  }
  
  /* Error */
  public boolean forEachKey(gnu.trove.procedure.TObjectProcedure<? super K> procedure)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedObjectDoubleMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedObjectDoubleMap:m	Lgnu/trove/map/TObjectDoubleMap;
    //   11: aload_1
    //   12: invokeinterface 137 2 0
    //   17: aload_2
    //   18: monitorexit
    //   19: ireturn
    //   20: astore_3
    //   21: aload_2
    //   22: monitorexit
    //   23: aload_3
    //   24: athrow
    // Line number table:
    //   Java source line #145	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	TSynchronizedObjectDoubleMap<K>
    //   0	25	1	procedure	gnu.trove.procedure.TObjectProcedure<? super K>
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public boolean forEachValue(gnu.trove.procedure.TDoubleProcedure procedure)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedObjectDoubleMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedObjectDoubleMap:m	Lgnu/trove/map/TObjectDoubleMap;
    //   11: aload_1
    //   12: invokeinterface 144 2 0
    //   17: aload_2
    //   18: monitorexit
    //   19: ireturn
    //   20: astore_3
    //   21: aload_2
    //   22: monitorexit
    //   23: aload_3
    //   24: athrow
    // Line number table:
    //   Java source line #148	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	TSynchronizedObjectDoubleMap<K>
    //   0	25	1	procedure	gnu.trove.procedure.TDoubleProcedure
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public boolean forEachEntry(gnu.trove.procedure.TObjectDoubleProcedure<? super K> procedure)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedObjectDoubleMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedObjectDoubleMap:m	Lgnu/trove/map/TObjectDoubleMap;
    //   11: aload_1
    //   12: invokeinterface 149 2 0
    //   17: aload_2
    //   18: monitorexit
    //   19: ireturn
    //   20: astore_3
    //   21: aload_2
    //   22: monitorexit
    //   23: aload_3
    //   24: athrow
    // Line number table:
    //   Java source line #151	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	TSynchronizedObjectDoubleMap<K>
    //   0	25	1	procedure	gnu.trove.procedure.TObjectDoubleProcedure<? super K>
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  public void transformValues(TDoubleFunction function)
  {
    synchronized (this.mutex)
    {
      this.m.transformValues(function);
    }
  }
  
  /* Error */
  public boolean retainEntries(gnu.trove.procedure.TObjectDoubleProcedure<? super K> procedure)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedObjectDoubleMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedObjectDoubleMap:m	Lgnu/trove/map/TObjectDoubleMap;
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
    //   Java source line #157	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	TSynchronizedObjectDoubleMap<K>
    //   0	25	1	procedure	gnu.trove.procedure.TObjectDoubleProcedure<? super K>
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public boolean increment(K key)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedObjectDoubleMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedObjectDoubleMap:m	Lgnu/trove/map/TObjectDoubleMap;
    //   11: aload_1
    //   12: invokeinterface 163 2 0
    //   17: aload_2
    //   18: monitorexit
    //   19: ireturn
    //   20: astore_3
    //   21: aload_2
    //   22: monitorexit
    //   23: aload_3
    //   24: athrow
    // Line number table:
    //   Java source line #160	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	TSynchronizedObjectDoubleMap<K>
    //   0	25	1	key	K
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public boolean adjustValue(K key, double amount)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedObjectDoubleMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore 4
    //   7: monitorenter
    //   8: aload_0
    //   9: getfield 38	gnu/trove/impl/sync/TSynchronizedObjectDoubleMap:m	Lgnu/trove/map/TObjectDoubleMap;
    //   12: aload_1
    //   13: dload_2
    //   14: invokeinterface 167 4 0
    //   19: aload 4
    //   21: monitorexit
    //   22: ireturn
    //   23: astore 5
    //   25: aload 4
    //   27: monitorexit
    //   28: aload 5
    //   30: athrow
    // Line number table:
    //   Java source line #163	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	31	0	this	TSynchronizedObjectDoubleMap<K>
    //   0	31	1	key	K
    //   0	31	2	amount	double
    //   5	21	4	Ljava/lang/Object;	Object
    //   23	6	5	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   8	22	23	finally
    //   23	28	23	finally
  }
  
  /* Error */
  public double adjustOrPutValue(K key, double adjust_amount, double put_amount)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedObjectDoubleMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore 6
    //   7: monitorenter
    //   8: aload_0
    //   9: getfield 38	gnu/trove/impl/sync/TSynchronizedObjectDoubleMap:m	Lgnu/trove/map/TObjectDoubleMap;
    //   12: aload_1
    //   13: dload_2
    //   14: dload 4
    //   16: invokeinterface 172 6 0
    //   21: aload 6
    //   23: monitorexit
    //   24: dreturn
    //   25: astore 7
    //   27: aload 6
    //   29: monitorexit
    //   30: aload 7
    //   32: athrow
    // Line number table:
    //   Java source line #166	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	33	0	this	TSynchronizedObjectDoubleMap<K>
    //   0	33	1	key	K
    //   0	33	2	adjust_amount	double
    //   0	33	4	put_amount	double
    //   5	23	6	Ljava/lang/Object;	Object
    //   25	6	7	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   8	24	25	finally
    //   25	30	25	finally
  }
  
  /* Error */
  public boolean equals(Object o)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedObjectDoubleMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedObjectDoubleMap:m	Lgnu/trove/map/TObjectDoubleMap;
    //   11: aload_1
    //   12: invokevirtual 177	java/lang/Object:equals	(Ljava/lang/Object;)Z
    //   15: aload_2
    //   16: monitorexit
    //   17: ireturn
    //   18: astore_3
    //   19: aload_2
    //   20: monitorexit
    //   21: aload_3
    //   22: athrow
    // Line number table:
    //   Java source line #170	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	23	0	this	TSynchronizedObjectDoubleMap<K>
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
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedObjectDoubleMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedObjectDoubleMap:m	Lgnu/trove/map/TObjectDoubleMap;
    //   11: invokevirtual 181	java/lang/Object:hashCode	()I
    //   14: aload_1
    //   15: monitorexit
    //   16: ireturn
    //   17: astore_2
    //   18: aload_1
    //   19: monitorexit
    //   20: aload_2
    //   21: athrow
    // Line number table:
    //   Java source line #173	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	22	0	this	TSynchronizedObjectDoubleMap<K>
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
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedObjectDoubleMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedObjectDoubleMap:m	Lgnu/trove/map/TObjectDoubleMap;
    //   11: invokevirtual 185	java/lang/Object:toString	()Ljava/lang/String;
    //   14: aload_1
    //   15: monitorexit
    //   16: areturn
    //   17: astore_2
    //   18: aload_1
    //   19: monitorexit
    //   20: aload_2
    //   21: athrow
    // Line number table:
    //   Java source line #176	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	22	0	this	TSynchronizedObjectDoubleMap<K>
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
