package gnu.trove.impl.sync;

import gnu.trove.TLongCollection;
import gnu.trove.iterator.TLongIterator;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class TSynchronizedLongCollection
  implements TLongCollection, Serializable
{
  private static final long serialVersionUID = 3053995032091335093L;
  final TLongCollection c;
  final Object mutex;
  
  public TSynchronizedLongCollection(TLongCollection c)
  {
    if (c == null) {
      throw new NullPointerException();
    }
    this.c = c;
    this.mutex = this;
  }
  
  public TSynchronizedLongCollection(TLongCollection c, Object mutex)
  {
    this.c = c;
    this.mutex = mutex;
  }
  
  /* Error */
  public int size()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 29	gnu/trove/impl/sync/TSynchronizedLongCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 27	gnu/trove/impl/sync/TSynchronizedLongCollection:c	Lgnu/trove/TLongCollection;
    //   11: invokeinterface 36 1 0
    //   16: aload_1
    //   17: monitorexit
    //   18: ireturn
    //   19: astore_2
    //   20: aload_1
    //   21: monitorexit
    //   22: aload_2
    //   23: athrow
    // Line number table:
    //   Java source line #70	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	24	0	this	TSynchronizedLongCollection
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
    //   1: getfield 29	gnu/trove/impl/sync/TSynchronizedLongCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 27	gnu/trove/impl/sync/TSynchronizedLongCollection:c	Lgnu/trove/TLongCollection;
    //   11: invokeinterface 40 1 0
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
    //   0	24	0	this	TSynchronizedLongCollection
    //   5	16	1	Ljava/lang/Object;	Object
    //   19	4	2	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	18	19	finally
    //   19	22	19	finally
  }
  
  /* Error */
  public boolean contains(long o)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 29	gnu/trove/impl/sync/TSynchronizedLongCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_3
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 27	gnu/trove/impl/sync/TSynchronizedLongCollection:c	Lgnu/trove/TLongCollection;
    //   11: lload_1
    //   12: invokeinterface 44 3 0
    //   17: aload_3
    //   18: monitorexit
    //   19: ireturn
    //   20: astore 4
    //   22: aload_3
    //   23: monitorexit
    //   24: aload 4
    //   26: athrow
    // Line number table:
    //   Java source line #76	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	27	0	this	TSynchronizedLongCollection
    //   0	27	1	o	long
    //   5	18	3	Ljava/lang/Object;	Object
    //   20	5	4	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	24	20	finally
  }
  
  /* Error */
  public long[] toArray()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 29	gnu/trove/impl/sync/TSynchronizedLongCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 27	gnu/trove/impl/sync/TSynchronizedLongCollection:c	Lgnu/trove/TLongCollection;
    //   11: invokeinterface 49 1 0
    //   16: aload_1
    //   17: monitorexit
    //   18: areturn
    //   19: astore_2
    //   20: aload_1
    //   21: monitorexit
    //   22: aload_2
    //   23: athrow
    // Line number table:
    //   Java source line #79	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	24	0	this	TSynchronizedLongCollection
    //   5	16	1	Ljava/lang/Object;	Object
    //   19	4	2	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	18	19	finally
    //   19	22	19	finally
  }
  
  /* Error */
  public long[] toArray(long[] a)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 29	gnu/trove/impl/sync/TSynchronizedLongCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 27	gnu/trove/impl/sync/TSynchronizedLongCollection:c	Lgnu/trove/TLongCollection;
    //   11: aload_1
    //   12: invokeinterface 52 2 0
    //   17: aload_2
    //   18: monitorexit
    //   19: areturn
    //   20: astore_3
    //   21: aload_2
    //   22: monitorexit
    //   23: aload_3
    //   24: athrow
    // Line number table:
    //   Java source line #82	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	TSynchronizedLongCollection
    //   0	25	1	a	long[]
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  public TLongIterator iterator()
  {
    return this.c.iterator();
  }
  
  /* Error */
  public boolean add(long e)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 29	gnu/trove/impl/sync/TSynchronizedLongCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_3
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 27	gnu/trove/impl/sync/TSynchronizedLongCollection:c	Lgnu/trove/TLongCollection;
    //   11: lload_1
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
    //   Java source line #90	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	27	0	this	TSynchronizedLongCollection
    //   0	27	1	e	long
    //   5	18	3	Ljava/lang/Object;	Object
    //   20	5	4	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	24	20	finally
  }
  
  /* Error */
  public boolean remove(long o)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 29	gnu/trove/impl/sync/TSynchronizedLongCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_3
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 27	gnu/trove/impl/sync/TSynchronizedLongCollection:c	Lgnu/trove/TLongCollection;
    //   11: lload_1
    //   12: invokeinterface 65 3 0
    //   17: aload_3
    //   18: monitorexit
    //   19: ireturn
    //   20: astore 4
    //   22: aload_3
    //   23: monitorexit
    //   24: aload 4
    //   26: athrow
    // Line number table:
    //   Java source line #93	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	27	0	this	TSynchronizedLongCollection
    //   0	27	1	o	long
    //   5	18	3	Ljava/lang/Object;	Object
    //   20	5	4	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	24	20	finally
  }
  
  /* Error */
  public boolean containsAll(java.util.Collection<?> coll)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 29	gnu/trove/impl/sync/TSynchronizedLongCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 27	gnu/trove/impl/sync/TSynchronizedLongCollection:c	Lgnu/trove/TLongCollection;
    //   11: aload_1
    //   12: invokeinterface 69 2 0
    //   17: aload_2
    //   18: monitorexit
    //   19: ireturn
    //   20: astore_3
    //   21: aload_2
    //   22: monitorexit
    //   23: aload_3
    //   24: athrow
    // Line number table:
    //   Java source line #97	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	TSynchronizedLongCollection
    //   0	25	1	coll	java.util.Collection<?>
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public boolean containsAll(TLongCollection coll)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 29	gnu/trove/impl/sync/TSynchronizedLongCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 27	gnu/trove/impl/sync/TSynchronizedLongCollection:c	Lgnu/trove/TLongCollection;
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
    //   Java source line #100	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	TSynchronizedLongCollection
    //   0	25	1	coll	TLongCollection
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public boolean containsAll(long[] array)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 29	gnu/trove/impl/sync/TSynchronizedLongCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 27	gnu/trove/impl/sync/TSynchronizedLongCollection:c	Lgnu/trove/TLongCollection;
    //   11: aload_1
    //   12: invokeinterface 78 2 0
    //   17: aload_2
    //   18: monitorexit
    //   19: ireturn
    //   20: astore_3
    //   21: aload_2
    //   22: monitorexit
    //   23: aload_3
    //   24: athrow
    // Line number table:
    //   Java source line #103	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	TSynchronizedLongCollection
    //   0	25	1	array	long[]
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public boolean addAll(java.util.Collection<? extends Long> coll)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 29	gnu/trove/impl/sync/TSynchronizedLongCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 27	gnu/trove/impl/sync/TSynchronizedLongCollection:c	Lgnu/trove/TLongCollection;
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
    //   Java source line #107	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	TSynchronizedLongCollection
    //   0	25	1	coll	java.util.Collection<? extends Long>
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public boolean addAll(TLongCollection coll)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 29	gnu/trove/impl/sync/TSynchronizedLongCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 27	gnu/trove/impl/sync/TSynchronizedLongCollection:c	Lgnu/trove/TLongCollection;
    //   11: aload_1
    //   12: invokeinterface 85 2 0
    //   17: aload_2
    //   18: monitorexit
    //   19: ireturn
    //   20: astore_3
    //   21: aload_2
    //   22: monitorexit
    //   23: aload_3
    //   24: athrow
    // Line number table:
    //   Java source line #110	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	TSynchronizedLongCollection
    //   0	25	1	coll	TLongCollection
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public boolean addAll(long[] array)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 29	gnu/trove/impl/sync/TSynchronizedLongCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 27	gnu/trove/impl/sync/TSynchronizedLongCollection:c	Lgnu/trove/TLongCollection;
    //   11: aload_1
    //   12: invokeinterface 87 2 0
    //   17: aload_2
    //   18: monitorexit
    //   19: ireturn
    //   20: astore_3
    //   21: aload_2
    //   22: monitorexit
    //   23: aload_3
    //   24: athrow
    // Line number table:
    //   Java source line #113	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	TSynchronizedLongCollection
    //   0	25	1	array	long[]
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public boolean removeAll(java.util.Collection<?> coll)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 29	gnu/trove/impl/sync/TSynchronizedLongCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 27	gnu/trove/impl/sync/TSynchronizedLongCollection:c	Lgnu/trove/TLongCollection;
    //   11: aload_1
    //   12: invokeinterface 90 2 0
    //   17: aload_2
    //   18: monitorexit
    //   19: ireturn
    //   20: astore_3
    //   21: aload_2
    //   22: monitorexit
    //   23: aload_3
    //   24: athrow
    // Line number table:
    //   Java source line #117	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	TSynchronizedLongCollection
    //   0	25	1	coll	java.util.Collection<?>
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public boolean removeAll(TLongCollection coll)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 29	gnu/trove/impl/sync/TSynchronizedLongCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 27	gnu/trove/impl/sync/TSynchronizedLongCollection:c	Lgnu/trove/TLongCollection;
    //   11: aload_1
    //   12: invokeinterface 92 2 0
    //   17: aload_2
    //   18: monitorexit
    //   19: ireturn
    //   20: astore_3
    //   21: aload_2
    //   22: monitorexit
    //   23: aload_3
    //   24: athrow
    // Line number table:
    //   Java source line #120	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	TSynchronizedLongCollection
    //   0	25	1	coll	TLongCollection
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public boolean removeAll(long[] array)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 29	gnu/trove/impl/sync/TSynchronizedLongCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 27	gnu/trove/impl/sync/TSynchronizedLongCollection:c	Lgnu/trove/TLongCollection;
    //   11: aload_1
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
    //   Java source line #123	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	TSynchronizedLongCollection
    //   0	25	1	array	long[]
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public boolean retainAll(java.util.Collection<?> coll)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 29	gnu/trove/impl/sync/TSynchronizedLongCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 27	gnu/trove/impl/sync/TSynchronizedLongCollection:c	Lgnu/trove/TLongCollection;
    //   11: aload_1
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
    //   Java source line #127	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	TSynchronizedLongCollection
    //   0	25	1	coll	java.util.Collection<?>
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public boolean retainAll(TLongCollection coll)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 29	gnu/trove/impl/sync/TSynchronizedLongCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 27	gnu/trove/impl/sync/TSynchronizedLongCollection:c	Lgnu/trove/TLongCollection;
    //   11: aload_1
    //   12: invokeinterface 99 2 0
    //   17: aload_2
    //   18: monitorexit
    //   19: ireturn
    //   20: astore_3
    //   21: aload_2
    //   22: monitorexit
    //   23: aload_3
    //   24: athrow
    // Line number table:
    //   Java source line #130	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	TSynchronizedLongCollection
    //   0	25	1	coll	TLongCollection
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public boolean retainAll(long[] array)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 29	gnu/trove/impl/sync/TSynchronizedLongCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 27	gnu/trove/impl/sync/TSynchronizedLongCollection:c	Lgnu/trove/TLongCollection;
    //   11: aload_1
    //   12: invokeinterface 101 2 0
    //   17: aload_2
    //   18: monitorexit
    //   19: ireturn
    //   20: astore_3
    //   21: aload_2
    //   22: monitorexit
    //   23: aload_3
    //   24: athrow
    // Line number table:
    //   Java source line #133	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	TSynchronizedLongCollection
    //   0	25	1	array	long[]
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  public long getNoEntryValue()
  {
    return this.c.getNoEntryValue();
  }
  
  /* Error */
  public boolean forEach(gnu.trove.procedure.TLongProcedure procedure)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 29	gnu/trove/impl/sync/TSynchronizedLongCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 27	gnu/trove/impl/sync/TSynchronizedLongCollection:c	Lgnu/trove/TLongCollection;
    //   11: aload_1
    //   12: invokeinterface 109 2 0
    //   17: aload_2
    //   18: monitorexit
    //   19: ireturn
    //   20: astore_3
    //   21: aload_2
    //   22: monitorexit
    //   23: aload_3
    //   24: athrow
    // Line number table:
    //   Java source line #138	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	TSynchronizedLongCollection
    //   0	25	1	procedure	gnu.trove.procedure.TLongProcedure
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
    //   1: getfield 29	gnu/trove/impl/sync/TSynchronizedLongCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 27	gnu/trove/impl/sync/TSynchronizedLongCollection:c	Lgnu/trove/TLongCollection;
    //   11: invokevirtual 118	java/lang/Object:toString	()Ljava/lang/String;
    //   14: aload_1
    //   15: monitorexit
    //   16: areturn
    //   17: astore_2
    //   18: aload_1
    //   19: monitorexit
    //   20: aload_2
    //   21: athrow
    // Line number table:
    //   Java source line #145	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	22	0	this	TSynchronizedLongCollection
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
