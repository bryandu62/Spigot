package gnu.trove.impl.sync;

import gnu.trove.function.TIntFunction;
import gnu.trove.list.TIntList;
import java.util.Random;
import java.util.RandomAccess;

public class TSynchronizedIntList
  extends TSynchronizedIntCollection
  implements TIntList
{
  static final long serialVersionUID = -7754090372962971524L;
  final TIntList list;
  
  public TSynchronizedIntList(TIntList list)
  {
    super(list);
    this.list = list;
  }
  
  public TSynchronizedIntList(TIntList list, Object mutex)
  {
    super(list, mutex);
    this.list = list;
  }
  
  /* Error */
  public boolean equals(Object o)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 32	gnu/trove/impl/sync/TSynchronizedIntList:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 20	gnu/trove/impl/sync/TSynchronizedIntList:list	Lgnu/trove/list/TIntList;
    //   11: aload_1
    //   12: invokevirtual 36	java/lang/Object:equals	(Ljava/lang/Object;)Z
    //   15: aload_2
    //   16: monitorexit
    //   17: ireturn
    //   18: astore_3
    //   19: aload_2
    //   20: monitorexit
    //   21: aload_3
    //   22: athrow
    // Line number table:
    //   Java source line #69	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	23	0	this	TSynchronizedIntList
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
    //   1: getfield 32	gnu/trove/impl/sync/TSynchronizedIntList:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 20	gnu/trove/impl/sync/TSynchronizedIntList:list	Lgnu/trove/list/TIntList;
    //   11: invokevirtual 41	java/lang/Object:hashCode	()I
    //   14: aload_1
    //   15: monitorexit
    //   16: ireturn
    //   17: astore_2
    //   18: aload_1
    //   19: monitorexit
    //   20: aload_2
    //   21: athrow
    // Line number table:
    //   Java source line #72	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	22	0	this	TSynchronizedIntList
    //   5	14	1	Ljava/lang/Object;	Object
    //   17	4	2	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	16	17	finally
    //   17	20	17	finally
  }
  
  /* Error */
  public int get(int index)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 32	gnu/trove/impl/sync/TSynchronizedIntList:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 20	gnu/trove/impl/sync/TSynchronizedIntList:list	Lgnu/trove/list/TIntList;
    //   11: iload_1
    //   12: invokeinterface 45 2 0
    //   17: aload_2
    //   18: monitorexit
    //   19: ireturn
    //   20: astore_3
    //   21: aload_2
    //   22: monitorexit
    //   23: aload_3
    //   24: athrow
    // Line number table:
    //   Java source line #76	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	TSynchronizedIntList
    //   0	25	1	index	int
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public int set(int index, int element)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 32	gnu/trove/impl/sync/TSynchronizedIntList:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_3
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 20	gnu/trove/impl/sync/TSynchronizedIntList:list	Lgnu/trove/list/TIntList;
    //   11: iload_1
    //   12: iload_2
    //   13: invokeinterface 51 3 0
    //   18: aload_3
    //   19: monitorexit
    //   20: ireturn
    //   21: astore 4
    //   23: aload_3
    //   24: monitorexit
    //   25: aload 4
    //   27: athrow
    // Line number table:
    //   Java source line #79	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	28	0	this	TSynchronizedIntList
    //   0	28	1	index	int
    //   0	28	2	element	int
    //   5	19	3	Ljava/lang/Object;	Object
    //   21	5	4	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	20	21	finally
    //   21	25	21	finally
  }
  
  public void set(int offset, int[] values)
  {
    synchronized (this.mutex)
    {
      this.list.set(offset, values);
    }
  }
  
  public void set(int offset, int[] values, int valOffset, int length)
  {
    synchronized (this.mutex)
    {
      this.list.set(offset, values, valOffset, length);
    }
  }
  
  /* Error */
  public int replace(int offset, int val)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 32	gnu/trove/impl/sync/TSynchronizedIntList:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_3
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 20	gnu/trove/impl/sync/TSynchronizedIntList:list	Lgnu/trove/list/TIntList;
    //   11: iload_1
    //   12: iload_2
    //   13: invokeinterface 66 3 0
    //   18: aload_3
    //   19: monitorexit
    //   20: ireturn
    //   21: astore 4
    //   23: aload_3
    //   24: monitorexit
    //   25: aload 4
    //   27: athrow
    // Line number table:
    //   Java source line #89	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	28	0	this	TSynchronizedIntList
    //   0	28	1	offset	int
    //   0	28	2	val	int
    //   5	19	3	Ljava/lang/Object;	Object
    //   21	5	4	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	20	21	finally
    //   21	25	21	finally
  }
  
  public void remove(int offset, int length)
  {
    synchronized (this.mutex)
    {
      this.list.remove(offset, length);
    }
  }
  
  /* Error */
  public int removeAt(int offset)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 32	gnu/trove/impl/sync/TSynchronizedIntList:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 20	gnu/trove/impl/sync/TSynchronizedIntList:list	Lgnu/trove/list/TIntList;
    //   11: iload_1
    //   12: invokeinterface 74 2 0
    //   17: aload_2
    //   18: monitorexit
    //   19: ireturn
    //   20: astore_3
    //   21: aload_2
    //   22: monitorexit
    //   23: aload_3
    //   24: athrow
    // Line number table:
    //   Java source line #95	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	TSynchronizedIntList
    //   0	25	1	offset	int
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  public void add(int[] vals)
  {
    synchronized (this.mutex)
    {
      this.list.add(vals);
    }
  }
  
  public void add(int[] vals, int offset, int length)
  {
    synchronized (this.mutex)
    {
      this.list.add(vals, offset, length);
    }
  }
  
  public void insert(int offset, int value)
  {
    synchronized (this.mutex)
    {
      this.list.insert(offset, value);
    }
  }
  
  public void insert(int offset, int[] values)
  {
    synchronized (this.mutex)
    {
      this.list.insert(offset, values);
    }
  }
  
  public void insert(int offset, int[] values, int valOffset, int len)
  {
    synchronized (this.mutex)
    {
      this.list.insert(offset, values, valOffset, len);
    }
  }
  
  /* Error */
  public int indexOf(int o)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 32	gnu/trove/impl/sync/TSynchronizedIntList:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 20	gnu/trove/impl/sync/TSynchronizedIntList:list	Lgnu/trove/list/TIntList;
    //   11: iload_1
    //   12: invokeinterface 94 2 0
    //   17: aload_2
    //   18: monitorexit
    //   19: ireturn
    //   20: astore_3
    //   21: aload_2
    //   22: monitorexit
    //   23: aload_3
    //   24: athrow
    // Line number table:
    //   Java source line #116	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	TSynchronizedIntList
    //   0	25	1	o	int
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public int lastIndexOf(int o)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 32	gnu/trove/impl/sync/TSynchronizedIntList:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 20	gnu/trove/impl/sync/TSynchronizedIntList:list	Lgnu/trove/list/TIntList;
    //   11: iload_1
    //   12: invokeinterface 97 2 0
    //   17: aload_2
    //   18: monitorexit
    //   19: ireturn
    //   20: astore_3
    //   21: aload_2
    //   22: monitorexit
    //   23: aload_3
    //   24: athrow
    // Line number table:
    //   Java source line #119	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	TSynchronizedIntList
    //   0	25	1	o	int
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public TIntList subList(int fromIndex, int toIndex)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 32	gnu/trove/impl/sync/TSynchronizedIntList:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_3
    //   6: monitorenter
    //   7: new 2	gnu/trove/impl/sync/TSynchronizedIntList
    //   10: dup
    //   11: aload_0
    //   12: getfield 20	gnu/trove/impl/sync/TSynchronizedIntList:list	Lgnu/trove/list/TIntList;
    //   15: iload_1
    //   16: iload_2
    //   17: invokeinterface 101 3 0
    //   22: aload_0
    //   23: getfield 32	gnu/trove/impl/sync/TSynchronizedIntList:mutex	Ljava/lang/Object;
    //   26: invokespecial 103	gnu/trove/impl/sync/TSynchronizedIntList:<init>	(Lgnu/trove/list/TIntList;Ljava/lang/Object;)V
    //   29: aload_3
    //   30: monitorexit
    //   31: areturn
    //   32: astore 4
    //   34: aload_3
    //   35: monitorexit
    //   36: aload 4
    //   38: athrow
    // Line number table:
    //   Java source line #131	-> byte code offset #0
    //   Java source line #132	-> byte code offset #7
    //   Java source line #134	-> byte code offset #32
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	39	0	this	TSynchronizedIntList
    //   0	39	1	fromIndex	int
    //   0	39	2	toIndex	int
    //   5	30	3	Ljava/lang/Object;	Object
    //   32	5	4	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	31	32	finally
    //   32	36	32	finally
  }
  
  /* Error */
  public int[] toArray(int offset, int len)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 32	gnu/trove/impl/sync/TSynchronizedIntList:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_3
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 20	gnu/trove/impl/sync/TSynchronizedIntList:list	Lgnu/trove/list/TIntList;
    //   11: iload_1
    //   12: iload_2
    //   13: invokeinterface 109 3 0
    //   18: aload_3
    //   19: monitorexit
    //   20: areturn
    //   21: astore 4
    //   23: aload_3
    //   24: monitorexit
    //   25: aload 4
    //   27: athrow
    // Line number table:
    //   Java source line #138	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	28	0	this	TSynchronizedIntList
    //   0	28	1	offset	int
    //   0	28	2	len	int
    //   5	19	3	Ljava/lang/Object;	Object
    //   21	5	4	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	20	21	finally
    //   21	25	21	finally
  }
  
  /* Error */
  public int[] toArray(int[] dest, int offset, int len)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 32	gnu/trove/impl/sync/TSynchronizedIntList:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore 4
    //   7: monitorenter
    //   8: aload_0
    //   9: getfield 20	gnu/trove/impl/sync/TSynchronizedIntList:list	Lgnu/trove/list/TIntList;
    //   12: aload_1
    //   13: iload_2
    //   14: iload_3
    //   15: invokeinterface 112 4 0
    //   20: aload 4
    //   22: monitorexit
    //   23: areturn
    //   24: astore 5
    //   26: aload 4
    //   28: monitorexit
    //   29: aload 5
    //   31: athrow
    // Line number table:
    //   Java source line #141	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	32	0	this	TSynchronizedIntList
    //   0	32	1	dest	int[]
    //   0	32	2	offset	int
    //   0	32	3	len	int
    //   5	22	4	Ljava/lang/Object;	Object
    //   24	6	5	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   8	23	24	finally
    //   24	29	24	finally
  }
  
  /* Error */
  public int[] toArray(int[] dest, int source_pos, int dest_pos, int len)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 32	gnu/trove/impl/sync/TSynchronizedIntList:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore 5
    //   7: monitorenter
    //   8: aload_0
    //   9: getfield 20	gnu/trove/impl/sync/TSynchronizedIntList:list	Lgnu/trove/list/TIntList;
    //   12: aload_1
    //   13: iload_2
    //   14: iload_3
    //   15: iload 4
    //   17: invokeinterface 116 5 0
    //   22: aload 5
    //   24: monitorexit
    //   25: areturn
    //   26: astore 6
    //   28: aload 5
    //   30: monitorexit
    //   31: aload 6
    //   33: athrow
    // Line number table:
    //   Java source line #144	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	34	0	this	TSynchronizedIntList
    //   0	34	1	dest	int[]
    //   0	34	2	source_pos	int
    //   0	34	3	dest_pos	int
    //   0	34	4	len	int
    //   5	24	5	Ljava/lang/Object;	Object
    //   26	6	6	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   8	25	26	finally
    //   26	31	26	finally
  }
  
  /* Error */
  public int indexOf(int offset, int value)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 32	gnu/trove/impl/sync/TSynchronizedIntList:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_3
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 20	gnu/trove/impl/sync/TSynchronizedIntList:list	Lgnu/trove/list/TIntList;
    //   11: iload_1
    //   12: iload_2
    //   13: invokeinterface 120 3 0
    //   18: aload_3
    //   19: monitorexit
    //   20: ireturn
    //   21: astore 4
    //   23: aload_3
    //   24: monitorexit
    //   25: aload 4
    //   27: athrow
    // Line number table:
    //   Java source line #148	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	28	0	this	TSynchronizedIntList
    //   0	28	1	offset	int
    //   0	28	2	value	int
    //   5	19	3	Ljava/lang/Object;	Object
    //   21	5	4	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	20	21	finally
    //   21	25	21	finally
  }
  
  /* Error */
  public int lastIndexOf(int offset, int value)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 32	gnu/trove/impl/sync/TSynchronizedIntList:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_3
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 20	gnu/trove/impl/sync/TSynchronizedIntList:list	Lgnu/trove/list/TIntList;
    //   11: iload_1
    //   12: iload_2
    //   13: invokeinterface 122 3 0
    //   18: aload_3
    //   19: monitorexit
    //   20: ireturn
    //   21: astore 4
    //   23: aload_3
    //   24: monitorexit
    //   25: aload 4
    //   27: athrow
    // Line number table:
    //   Java source line #151	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	28	0	this	TSynchronizedIntList
    //   0	28	1	offset	int
    //   0	28	2	value	int
    //   5	19	3	Ljava/lang/Object;	Object
    //   21	5	4	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	20	21	finally
    //   21	25	21	finally
  }
  
  public void fill(int val)
  {
    synchronized (this.mutex)
    {
      this.list.fill(val);
    }
  }
  
  public void fill(int fromIndex, int toIndex, int val)
  {
    synchronized (this.mutex)
    {
      this.list.fill(fromIndex, toIndex, val);
    }
  }
  
  public void reverse()
  {
    synchronized (this.mutex)
    {
      this.list.reverse();
    }
  }
  
  public void reverse(int from, int to)
  {
    synchronized (this.mutex)
    {
      this.list.reverse(from, to);
    }
  }
  
  public void shuffle(Random rand)
  {
    synchronized (this.mutex)
    {
      this.list.shuffle(rand);
    }
  }
  
  public void sort()
  {
    synchronized (this.mutex)
    {
      this.list.sort();
    }
  }
  
  public void sort(int fromIndex, int toIndex)
  {
    synchronized (this.mutex)
    {
      this.list.sort(fromIndex, toIndex);
    }
  }
  
  /* Error */
  public int binarySearch(int value)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 32	gnu/trove/impl/sync/TSynchronizedIntList:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 20	gnu/trove/impl/sync/TSynchronizedIntList:list	Lgnu/trove/list/TIntList;
    //   11: iload_1
    //   12: invokeinterface 151 2 0
    //   17: aload_2
    //   18: monitorexit
    //   19: ireturn
    //   20: astore_3
    //   21: aload_2
    //   22: monitorexit
    //   23: aload_3
    //   24: athrow
    // Line number table:
    //   Java source line #180	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	TSynchronizedIntList
    //   0	25	1	value	int
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public int binarySearch(int value, int fromIndex, int toIndex)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 32	gnu/trove/impl/sync/TSynchronizedIntList:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore 4
    //   7: monitorenter
    //   8: aload_0
    //   9: getfield 20	gnu/trove/impl/sync/TSynchronizedIntList:list	Lgnu/trove/list/TIntList;
    //   12: iload_1
    //   13: iload_2
    //   14: iload_3
    //   15: invokeinterface 154 4 0
    //   20: aload 4
    //   22: monitorexit
    //   23: ireturn
    //   24: astore 5
    //   26: aload 4
    //   28: monitorexit
    //   29: aload 5
    //   31: athrow
    // Line number table:
    //   Java source line #183	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	32	0	this	TSynchronizedIntList
    //   0	32	1	value	int
    //   0	32	2	fromIndex	int
    //   0	32	3	toIndex	int
    //   5	22	4	Ljava/lang/Object;	Object
    //   24	6	5	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   8	23	24	finally
    //   24	29	24	finally
  }
  
  /* Error */
  public TIntList grep(gnu.trove.procedure.TIntProcedure condition)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 32	gnu/trove/impl/sync/TSynchronizedIntList:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 20	gnu/trove/impl/sync/TSynchronizedIntList:list	Lgnu/trove/list/TIntList;
    //   11: aload_1
    //   12: invokeinterface 158 2 0
    //   17: aload_2
    //   18: monitorexit
    //   19: areturn
    //   20: astore_3
    //   21: aload_2
    //   22: monitorexit
    //   23: aload_3
    //   24: athrow
    // Line number table:
    //   Java source line #187	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	TSynchronizedIntList
    //   0	25	1	condition	gnu.trove.procedure.TIntProcedure
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public TIntList inverseGrep(gnu.trove.procedure.TIntProcedure condition)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 32	gnu/trove/impl/sync/TSynchronizedIntList:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 20	gnu/trove/impl/sync/TSynchronizedIntList:list	Lgnu/trove/list/TIntList;
    //   11: aload_1
    //   12: invokeinterface 163 2 0
    //   17: aload_2
    //   18: monitorexit
    //   19: areturn
    //   20: astore_3
    //   21: aload_2
    //   22: monitorexit
    //   23: aload_3
    //   24: athrow
    // Line number table:
    //   Java source line #190	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	TSynchronizedIntList
    //   0	25	1	condition	gnu.trove.procedure.TIntProcedure
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public int max()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 32	gnu/trove/impl/sync/TSynchronizedIntList:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 20	gnu/trove/impl/sync/TSynchronizedIntList:list	Lgnu/trove/list/TIntList;
    //   11: invokeinterface 166 1 0
    //   16: aload_1
    //   17: monitorexit
    //   18: ireturn
    //   19: astore_2
    //   20: aload_1
    //   21: monitorexit
    //   22: aload_2
    //   23: athrow
    // Line number table:
    //   Java source line #193	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	24	0	this	TSynchronizedIntList
    //   5	16	1	Ljava/lang/Object;	Object
    //   19	4	2	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	18	19	finally
    //   19	22	19	finally
  }
  
  /* Error */
  public int min()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 32	gnu/trove/impl/sync/TSynchronizedIntList:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 20	gnu/trove/impl/sync/TSynchronizedIntList:list	Lgnu/trove/list/TIntList;
    //   11: invokeinterface 169 1 0
    //   16: aload_1
    //   17: monitorexit
    //   18: ireturn
    //   19: astore_2
    //   20: aload_1
    //   21: monitorexit
    //   22: aload_2
    //   23: athrow
    // Line number table:
    //   Java source line #194	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	24	0	this	TSynchronizedIntList
    //   5	16	1	Ljava/lang/Object;	Object
    //   19	4	2	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	18	19	finally
    //   19	22	19	finally
  }
  
  /* Error */
  public int sum()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 32	gnu/trove/impl/sync/TSynchronizedIntList:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 20	gnu/trove/impl/sync/TSynchronizedIntList:list	Lgnu/trove/list/TIntList;
    //   11: invokeinterface 172 1 0
    //   16: aload_1
    //   17: monitorexit
    //   18: ireturn
    //   19: astore_2
    //   20: aload_1
    //   21: monitorexit
    //   22: aload_2
    //   23: athrow
    // Line number table:
    //   Java source line #195	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	24	0	this	TSynchronizedIntList
    //   5	16	1	Ljava/lang/Object;	Object
    //   19	4	2	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	18	19	finally
    //   19	22	19	finally
  }
  
  /* Error */
  public boolean forEachDescending(gnu.trove.procedure.TIntProcedure procedure)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 32	gnu/trove/impl/sync/TSynchronizedIntList:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 20	gnu/trove/impl/sync/TSynchronizedIntList:list	Lgnu/trove/list/TIntList;
    //   11: aload_1
    //   12: invokeinterface 176 2 0
    //   17: aload_2
    //   18: monitorexit
    //   19: ireturn
    //   20: astore_3
    //   21: aload_2
    //   22: monitorexit
    //   23: aload_3
    //   24: athrow
    // Line number table:
    //   Java source line #198	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	TSynchronizedIntList
    //   0	25	1	procedure	gnu.trove.procedure.TIntProcedure
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
      this.list.transformValues(function);
    }
  }
  
  private Object readResolve()
  {
    return (this.list instanceof RandomAccess) ? new TSynchronizedRandomAccessIntList(this.list) : this;
  }
}
