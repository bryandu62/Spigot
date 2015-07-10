package gnu.trove.impl.sync;

import gnu.trove.TIntCollection;
import gnu.trove.function.TIntFunction;
import gnu.trove.iterator.TFloatIntIterator;
import gnu.trove.map.TFloatIntMap;
import gnu.trove.set.TFloatSet;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;

public class TSynchronizedFloatIntMap
  implements TFloatIntMap, Serializable
{
  private static final long serialVersionUID = 1978198479659022715L;
  private final TFloatIntMap m;
  final Object mutex;
  
  public TSynchronizedFloatIntMap(TFloatIntMap m)
  {
    if (m == null) {
      throw new NullPointerException();
    }
    this.m = m;
    this.mutex = this;
  }
  
  public TSynchronizedFloatIntMap(TFloatIntMap m, Object mutex)
  {
    this.m = m;
    this.mutex = mutex;
  }
  
  /* Error */
  public int size()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 37	gnu/trove/impl/sync/TSynchronizedFloatIntMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 35	gnu/trove/impl/sync/TSynchronizedFloatIntMap:m	Lgnu/trove/map/TFloatIntMap;
    //   11: invokeinterface 44 1 0
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
    //   0	24	0	this	TSynchronizedFloatIntMap
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
    //   1: getfield 37	gnu/trove/impl/sync/TSynchronizedFloatIntMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 35	gnu/trove/impl/sync/TSynchronizedFloatIntMap:m	Lgnu/trove/map/TFloatIntMap;
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
    //   Java source line #74	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	24	0	this	TSynchronizedFloatIntMap
    //   5	16	1	Ljava/lang/Object;	Object
    //   19	4	2	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	18	19	finally
    //   19	22	19	finally
  }
  
  /* Error */
  public boolean containsKey(float key)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 37	gnu/trove/impl/sync/TSynchronizedFloatIntMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 35	gnu/trove/impl/sync/TSynchronizedFloatIntMap:m	Lgnu/trove/map/TFloatIntMap;
    //   11: fload_1
    //   12: invokeinterface 52 2 0
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
    //   0	25	0	this	TSynchronizedFloatIntMap
    //   0	25	1	key	float
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public boolean containsValue(int value)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 37	gnu/trove/impl/sync/TSynchronizedFloatIntMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 35	gnu/trove/impl/sync/TSynchronizedFloatIntMap:m	Lgnu/trove/map/TFloatIntMap;
    //   11: iload_1
    //   12: invokeinterface 58 2 0
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
    //   0	25	0	this	TSynchronizedFloatIntMap
    //   0	25	1	value	int
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public int get(float key)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 37	gnu/trove/impl/sync/TSynchronizedFloatIntMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 35	gnu/trove/impl/sync/TSynchronizedFloatIntMap:m	Lgnu/trove/map/TFloatIntMap;
    //   11: fload_1
    //   12: invokeinterface 64 2 0
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
    //   0	25	0	this	TSynchronizedFloatIntMap
    //   0	25	1	key	float
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public int put(float key, int value)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 37	gnu/trove/impl/sync/TSynchronizedFloatIntMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_3
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 35	gnu/trove/impl/sync/TSynchronizedFloatIntMap:m	Lgnu/trove/map/TFloatIntMap;
    //   11: fload_1
    //   12: iload_2
    //   13: invokeinterface 68 3 0
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
    //   0	28	0	this	TSynchronizedFloatIntMap
    //   0	28	1	key	float
    //   0	28	2	value	int
    //   5	19	3	Ljava/lang/Object;	Object
    //   21	5	4	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	20	21	finally
    //   21	25	21	finally
  }
  
  /* Error */
  public int remove(float key)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 37	gnu/trove/impl/sync/TSynchronizedFloatIntMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 35	gnu/trove/impl/sync/TSynchronizedFloatIntMap:m	Lgnu/trove/map/TFloatIntMap;
    //   11: fload_1
    //   12: invokeinterface 71 2 0
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
    //   0	25	0	this	TSynchronizedFloatIntMap
    //   0	25	1	key	float
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  public void putAll(Map<? extends Float, ? extends Integer> map)
  {
    synchronized (this.mutex)
    {
      this.m.putAll(map);
    }
  }
  
  public void putAll(TFloatIntMap map)
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
  
  private transient TFloatSet keySet = null;
  private transient TIntCollection values = null;
  
  public TFloatSet keySet()
  {
    synchronized (this.mutex)
    {
      if (this.keySet == null) {
        this.keySet = new TSynchronizedFloatSet(this.m.keySet(), this.mutex);
      }
      return this.keySet;
    }
  }
  
  /* Error */
  public float[] keys()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 37	gnu/trove/impl/sync/TSynchronizedFloatIntMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 35	gnu/trove/impl/sync/TSynchronizedFloatIntMap:m	Lgnu/trove/map/TFloatIntMap;
    //   11: invokeinterface 95 1 0
    //   16: aload_1
    //   17: monitorexit
    //   18: areturn
    //   19: astore_2
    //   20: aload_1
    //   21: monitorexit
    //   22: aload_2
    //   23: athrow
    // Line number table:
    //   Java source line #113	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	24	0	this	TSynchronizedFloatIntMap
    //   5	16	1	Ljava/lang/Object;	Object
    //   19	4	2	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	18	19	finally
    //   19	22	19	finally
  }
  
  /* Error */
  public float[] keys(float[] array)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 37	gnu/trove/impl/sync/TSynchronizedFloatIntMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 35	gnu/trove/impl/sync/TSynchronizedFloatIntMap:m	Lgnu/trove/map/TFloatIntMap;
    //   11: aload_1
    //   12: invokeinterface 98 2 0
    //   17: aload_2
    //   18: monitorexit
    //   19: areturn
    //   20: astore_3
    //   21: aload_2
    //   22: monitorexit
    //   23: aload_3
    //   24: athrow
    // Line number table:
    //   Java source line #116	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	TSynchronizedFloatIntMap
    //   0	25	1	array	float[]
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  public TIntCollection valueCollection()
  {
    synchronized (this.mutex)
    {
      if (this.values == null) {
        this.values = new TSynchronizedIntCollection(this.m.valueCollection(), this.mutex);
      }
      return this.values;
    }
  }
  
  /* Error */
  public int[] values()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 37	gnu/trove/impl/sync/TSynchronizedFloatIntMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 35	gnu/trove/impl/sync/TSynchronizedFloatIntMap:m	Lgnu/trove/map/TFloatIntMap;
    //   11: invokeinterface 112 1 0
    //   16: aload_1
    //   17: monitorexit
    //   18: areturn
    //   19: astore_2
    //   20: aload_1
    //   21: monitorexit
    //   22: aload_2
    //   23: athrow
    // Line number table:
    //   Java source line #127	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	24	0	this	TSynchronizedFloatIntMap
    //   5	16	1	Ljava/lang/Object;	Object
    //   19	4	2	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	18	19	finally
    //   19	22	19	finally
  }
  
  /* Error */
  public int[] values(int[] array)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 37	gnu/trove/impl/sync/TSynchronizedFloatIntMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 35	gnu/trove/impl/sync/TSynchronizedFloatIntMap:m	Lgnu/trove/map/TFloatIntMap;
    //   11: aload_1
    //   12: invokeinterface 115 2 0
    //   17: aload_2
    //   18: monitorexit
    //   19: areturn
    //   20: astore_3
    //   21: aload_2
    //   22: monitorexit
    //   23: aload_3
    //   24: athrow
    // Line number table:
    //   Java source line #130	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	TSynchronizedFloatIntMap
    //   0	25	1	array	int[]
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  public TFloatIntIterator iterator()
  {
    return this.m.iterator();
  }
  
  public float getNoEntryKey()
  {
    return this.m.getNoEntryKey();
  }
  
  public int getNoEntryValue()
  {
    return this.m.getNoEntryValue();
  }
  
  /* Error */
  public int putIfAbsent(float key, int value)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 37	gnu/trove/impl/sync/TSynchronizedFloatIntMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_3
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 35	gnu/trove/impl/sync/TSynchronizedFloatIntMap:m	Lgnu/trove/map/TFloatIntMap;
    //   11: fload_1
    //   12: iload_2
    //   13: invokeinterface 130 3 0
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
    //   0	28	0	this	TSynchronizedFloatIntMap
    //   0	28	1	key	float
    //   0	28	2	value	int
    //   5	19	3	Ljava/lang/Object;	Object
    //   21	5	4	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	20	21	finally
    //   21	25	21	finally
  }
  
  /* Error */
  public boolean forEachKey(gnu.trove.procedure.TFloatProcedure procedure)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 37	gnu/trove/impl/sync/TSynchronizedFloatIntMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 35	gnu/trove/impl/sync/TSynchronizedFloatIntMap:m	Lgnu/trove/map/TFloatIntMap;
    //   11: aload_1
    //   12: invokeinterface 134 2 0
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
    //   0	25	0	this	TSynchronizedFloatIntMap
    //   0	25	1	procedure	gnu.trove.procedure.TFloatProcedure
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public boolean forEachValue(gnu.trove.procedure.TIntProcedure procedure)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 37	gnu/trove/impl/sync/TSynchronizedFloatIntMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 35	gnu/trove/impl/sync/TSynchronizedFloatIntMap:m	Lgnu/trove/map/TFloatIntMap;
    //   11: aload_1
    //   12: invokeinterface 140 2 0
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
    //   0	25	0	this	TSynchronizedFloatIntMap
    //   0	25	1	procedure	gnu.trove.procedure.TIntProcedure
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public boolean forEachEntry(gnu.trove.procedure.TFloatIntProcedure procedure)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 37	gnu/trove/impl/sync/TSynchronizedFloatIntMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 35	gnu/trove/impl/sync/TSynchronizedFloatIntMap:m	Lgnu/trove/map/TFloatIntMap;
    //   11: aload_1
    //   12: invokeinterface 145 2 0
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
    //   0	25	0	this	TSynchronizedFloatIntMap
    //   0	25	1	procedure	gnu.trove.procedure.TFloatIntProcedure
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  public void transformValues(TIntFunction function)
  {
    synchronized (this.mutex)
    {
      this.m.transformValues(function);
    }
  }
  
  /* Error */
  public boolean retainEntries(gnu.trove.procedure.TFloatIntProcedure procedure)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 37	gnu/trove/impl/sync/TSynchronizedFloatIntMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 35	gnu/trove/impl/sync/TSynchronizedFloatIntMap:m	Lgnu/trove/map/TFloatIntMap;
    //   11: aload_1
    //   12: invokeinterface 155 2 0
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
    //   0	25	0	this	TSynchronizedFloatIntMap
    //   0	25	1	procedure	gnu.trove.procedure.TFloatIntProcedure
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public boolean increment(float key)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 37	gnu/trove/impl/sync/TSynchronizedFloatIntMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 35	gnu/trove/impl/sync/TSynchronizedFloatIntMap:m	Lgnu/trove/map/TFloatIntMap;
    //   11: fload_1
    //   12: invokeinterface 158 2 0
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
    //   0	25	0	this	TSynchronizedFloatIntMap
    //   0	25	1	key	float
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public boolean adjustValue(float key, int amount)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 37	gnu/trove/impl/sync/TSynchronizedFloatIntMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_3
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 35	gnu/trove/impl/sync/TSynchronizedFloatIntMap:m	Lgnu/trove/map/TFloatIntMap;
    //   11: fload_1
    //   12: iload_2
    //   13: invokeinterface 162 3 0
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
    //   0	28	0	this	TSynchronizedFloatIntMap
    //   0	28	1	key	float
    //   0	28	2	amount	int
    //   5	19	3	Ljava/lang/Object;	Object
    //   21	5	4	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	20	21	finally
    //   21	25	21	finally
  }
  
  /* Error */
  public int adjustOrPutValue(float key, int adjust_amount, int put_amount)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 37	gnu/trove/impl/sync/TSynchronizedFloatIntMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore 4
    //   7: monitorenter
    //   8: aload_0
    //   9: getfield 35	gnu/trove/impl/sync/TSynchronizedFloatIntMap:m	Lgnu/trove/map/TFloatIntMap;
    //   12: fload_1
    //   13: iload_2
    //   14: iload_3
    //   15: invokeinterface 167 4 0
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
    //   0	32	0	this	TSynchronizedFloatIntMap
    //   0	32	1	key	float
    //   0	32	2	adjust_amount	int
    //   0	32	3	put_amount	int
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
    //   1: getfield 37	gnu/trove/impl/sync/TSynchronizedFloatIntMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 35	gnu/trove/impl/sync/TSynchronizedFloatIntMap:m	Lgnu/trove/map/TFloatIntMap;
    //   11: aload_1
    //   12: invokevirtual 173	java/lang/Object:equals	(Ljava/lang/Object;)Z
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
    //   0	23	0	this	TSynchronizedFloatIntMap
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
    //   1: getfield 37	gnu/trove/impl/sync/TSynchronizedFloatIntMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 35	gnu/trove/impl/sync/TSynchronizedFloatIntMap:m	Lgnu/trove/map/TFloatIntMap;
    //   11: invokevirtual 177	java/lang/Object:hashCode	()I
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
    //   0	22	0	this	TSynchronizedFloatIntMap
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
    //   1: getfield 37	gnu/trove/impl/sync/TSynchronizedFloatIntMap:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 35	gnu/trove/impl/sync/TSynchronizedFloatIntMap:m	Lgnu/trove/map/TFloatIntMap;
    //   11: invokevirtual 181	java/lang/Object:toString	()Ljava/lang/String;
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
    //   0	22	0	this	TSynchronizedFloatIntMap
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
