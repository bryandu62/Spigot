package gnu.trove.impl.sync;

import gnu.trove.TByteCollection;
import gnu.trove.function.TByteFunction;
import gnu.trove.iterator.TObjectByteIterator;
import gnu.trove.map.TObjectByteMap;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public class TSynchronizedObjectByteMap<K>
  implements TObjectByteMap<K>, Serializable
{
  private static final long serialVersionUID = 1978198479659022715L;
  private final TObjectByteMap<K> m;
  final Object mutex;
  
  public TSynchronizedObjectByteMap(TObjectByteMap<K> m)
  {
    if (m == null) {
      throw new NullPointerException();
    }
    this.m = m;
    this.mutex = this;
  }
  
  public TSynchronizedObjectByteMap(TObjectByteMap<K> m, Object mutex)
  {
    this.m = m;
    this.mutex = mutex;
  }
  
  /* Error */
  public int size()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedObjectByteMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedObjectByteMap:m	Lgnu/trove/map/TObjectByteMap;
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
    //   0	24	0	this	TSynchronizedObjectByteMap<K>
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
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedObjectByteMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedObjectByteMap:m	Lgnu/trove/map/TObjectByteMap;
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
    //   0	24	0	this	TSynchronizedObjectByteMap<K>
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
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedObjectByteMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedObjectByteMap:m	Lgnu/trove/map/TObjectByteMap;
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
    //   0	25	0	this	TSynchronizedObjectByteMap<K>
    //   0	25	1	key	Object
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public boolean containsValue(byte value)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedObjectByteMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedObjectByteMap:m	Lgnu/trove/map/TObjectByteMap;
    //   11: iload_1
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
    //   Java source line #80	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	TSynchronizedObjectByteMap<K>
    //   0	25	1	value	byte
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public byte get(Object key)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedObjectByteMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedObjectByteMap:m	Lgnu/trove/map/TObjectByteMap;
    //   11: aload_1
    //   12: invokeinterface 67 2 0
    //   17: aload_2
    //   18: monitorexit
    //   19: ireturn
    //   20: astore_3
    //   21: aload_2
    //   22: monitorexit
    //   23: aload_3
    //   24: athrow
    // Line number table:
    //   Java source line #83	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	TSynchronizedObjectByteMap<K>
    //   0	25	1	key	Object
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public byte put(K key, byte value)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedObjectByteMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_3
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedObjectByteMap:m	Lgnu/trove/map/TObjectByteMap;
    //   11: aload_1
    //   12: iload_2
    //   13: invokeinterface 71 3 0
    //   18: aload_3
    //   19: monitorexit
    //   20: ireturn
    //   21: astore 4
    //   23: aload_3
    //   24: monitorexit
    //   25: aload 4
    //   27: athrow
    // Line number table:
    //   Java source line #87	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	28	0	this	TSynchronizedObjectByteMap<K>
    //   0	28	1	key	K
    //   0	28	2	value	byte
    //   5	19	3	Ljava/lang/Object;	Object
    //   21	5	4	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	20	21	finally
    //   21	25	21	finally
  }
  
  /* Error */
  public byte remove(Object key)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedObjectByteMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedObjectByteMap:m	Lgnu/trove/map/TObjectByteMap;
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
    //   Java source line #90	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	TSynchronizedObjectByteMap<K>
    //   0	25	1	key	Object
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  public void putAll(Map<? extends K, ? extends Byte> map)
  {
    synchronized (this.mutex)
    {
      this.m.putAll(map);
    }
  }
  
  public void putAll(TObjectByteMap<? extends K> map)
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
  private transient TByteCollection values = null;
  
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
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedObjectByteMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedObjectByteMap:m	Lgnu/trove/map/TObjectByteMap;
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
    //   0	24	0	this	TSynchronizedObjectByteMap<K>
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
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedObjectByteMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedObjectByteMap:m	Lgnu/trove/map/TObjectByteMap;
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
    //   0	25	0	this	TSynchronizedObjectByteMap<K>
    //   0	25	1	array	K[]
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  public TByteCollection valueCollection()
  {
    synchronized (this.mutex)
    {
      if (this.values == null) {
        this.values = new TSynchronizedByteCollection(this.m.valueCollection(), this.mutex);
      }
      return this.values;
    }
  }
  
  /* Error */
  public byte[] values()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedObjectByteMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedObjectByteMap:m	Lgnu/trove/map/TObjectByteMap;
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
    //   0	24	0	this	TSynchronizedObjectByteMap<K>
    //   5	16	1	Ljava/lang/Object;	Object
    //   19	4	2	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	18	19	finally
    //   19	22	19	finally
  }
  
  /* Error */
  public byte[] values(byte[] array)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedObjectByteMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedObjectByteMap:m	Lgnu/trove/map/TObjectByteMap;
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
    //   0	25	0	this	TSynchronizedObjectByteMap<K>
    //   0	25	1	array	byte[]
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  public TObjectByteIterator<K> iterator()
  {
    return this.m.iterator();
  }
  
  public byte getNoEntryValue()
  {
    return this.m.getNoEntryValue();
  }
  
  /* Error */
  public byte putIfAbsent(K key, byte value)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedObjectByteMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_3
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedObjectByteMap:m	Lgnu/trove/map/TObjectByteMap;
    //   11: aload_1
    //   12: iload_2
    //   13: invokeinterface 133 3 0
    //   18: aload_3
    //   19: monitorexit
    //   20: ireturn
    //   21: astore 4
    //   23: aload_3
    //   24: monitorexit
    //   25: aload 4
    //   27: athrow
    // Line number table:
    //   Java source line #142	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	28	0	this	TSynchronizedObjectByteMap<K>
    //   0	28	1	key	K
    //   0	28	2	value	byte
    //   5	19	3	Ljava/lang/Object;	Object
    //   21	5	4	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	20	21	finally
    //   21	25	21	finally
  }
  
  /* Error */
  public boolean forEachKey(gnu.trove.procedure.TObjectProcedure<? super K> procedure)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedObjectByteMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedObjectByteMap:m	Lgnu/trove/map/TObjectByteMap;
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
    //   0	25	0	this	TSynchronizedObjectByteMap<K>
    //   0	25	1	procedure	gnu.trove.procedure.TObjectProcedure<? super K>
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public boolean forEachValue(gnu.trove.procedure.TByteProcedure procedure)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedObjectByteMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedObjectByteMap:m	Lgnu/trove/map/TObjectByteMap;
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
    //   0	25	0	this	TSynchronizedObjectByteMap<K>
    //   0	25	1	procedure	gnu.trove.procedure.TByteProcedure
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public boolean forEachEntry(gnu.trove.procedure.TObjectByteProcedure<? super K> procedure)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedObjectByteMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedObjectByteMap:m	Lgnu/trove/map/TObjectByteMap;
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
    //   0	25	0	this	TSynchronizedObjectByteMap<K>
    //   0	25	1	procedure	gnu.trove.procedure.TObjectByteProcedure<? super K>
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  public void transformValues(TByteFunction function)
  {
    synchronized (this.mutex)
    {
      this.m.transformValues(function);
    }
  }
  
  /* Error */
  public boolean retainEntries(gnu.trove.procedure.TObjectByteProcedure<? super K> procedure)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedObjectByteMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedObjectByteMap:m	Lgnu/trove/map/TObjectByteMap;
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
    //   0	25	0	this	TSynchronizedObjectByteMap<K>
    //   0	25	1	procedure	gnu.trove.procedure.TObjectByteProcedure<? super K>
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
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedObjectByteMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedObjectByteMap:m	Lgnu/trove/map/TObjectByteMap;
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
    //   0	25	0	this	TSynchronizedObjectByteMap<K>
    //   0	25	1	key	K
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public boolean adjustValue(K key, byte amount)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedObjectByteMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_3
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedObjectByteMap:m	Lgnu/trove/map/TObjectByteMap;
    //   11: aload_1
    //   12: iload_2
    //   13: invokeinterface 167 3 0
    //   18: aload_3
    //   19: monitorexit
    //   20: ireturn
    //   21: astore 4
    //   23: aload_3
    //   24: monitorexit
    //   25: aload 4
    //   27: athrow
    // Line number table:
    //   Java source line #163	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	28	0	this	TSynchronizedObjectByteMap<K>
    //   0	28	1	key	K
    //   0	28	2	amount	byte
    //   5	19	3	Ljava/lang/Object;	Object
    //   21	5	4	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	20	21	finally
    //   21	25	21	finally
  }
  
  /* Error */
  public byte adjustOrPutValue(K key, byte adjust_amount, byte put_amount)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedObjectByteMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore 4
    //   7: monitorenter
    //   8: aload_0
    //   9: getfield 38	gnu/trove/impl/sync/TSynchronizedObjectByteMap:m	Lgnu/trove/map/TObjectByteMap;
    //   12: aload_1
    //   13: iload_2
    //   14: iload_3
    //   15: invokeinterface 172 4 0
    //   20: aload 4
    //   22: monitorexit
    //   23: ireturn
    //   24: astore 5
    //   26: aload 4
    //   28: monitorexit
    //   29: aload 5
    //   31: athrow
    // Line number table:
    //   Java source line #166	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	32	0	this	TSynchronizedObjectByteMap<K>
    //   0	32	1	key	K
    //   0	32	2	adjust_amount	byte
    //   0	32	3	put_amount	byte
    //   5	22	4	Ljava/lang/Object;	Object
    //   24	6	5	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   8	23	24	finally
    //   24	29	24	finally
  }
  
  /* Error */
  public boolean equals(Object o)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedObjectByteMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedObjectByteMap:m	Lgnu/trove/map/TObjectByteMap;
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
    //   0	23	0	this	TSynchronizedObjectByteMap<K>
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
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedObjectByteMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedObjectByteMap:m	Lgnu/trove/map/TObjectByteMap;
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
    //   0	22	0	this	TSynchronizedObjectByteMap<K>
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
    //   1: getfield 40	gnu/trove/impl/sync/TSynchronizedObjectByteMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 38	gnu/trove/impl/sync/TSynchronizedObjectByteMap:m	Lgnu/trove/map/TObjectByteMap;
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
    //   0	22	0	this	TSynchronizedObjectByteMap<K>
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
