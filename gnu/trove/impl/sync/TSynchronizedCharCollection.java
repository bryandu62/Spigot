package gnu.trove.impl.sync;

import gnu.trove.TCharCollection;
import gnu.trove.iterator.TCharIterator;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class TSynchronizedCharCollection
  implements TCharCollection, Serializable
{
  private static final long serialVersionUID = 3053995032091335093L;
  final TCharCollection c;
  final Object mutex;
  
  public TSynchronizedCharCollection(TCharCollection c)
  {
    if (c == null) {
      throw new NullPointerException();
    }
    this.c = c;
    this.mutex = this;
  }
  
  public TSynchronizedCharCollection(TCharCollection c, Object mutex)
  {
    this.c = c;
    this.mutex = mutex;
  }
  
  /* Error */
  public int size()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 29	gnu/trove/impl/sync/TSynchronizedCharCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 27	gnu/trove/impl/sync/TSynchronizedCharCollection:c	Lgnu/trove/TCharCollection;
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
    //   0	24	0	this	TSynchronizedCharCollection
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
    //   1: getfield 29	gnu/trove/impl/sync/TSynchronizedCharCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 27	gnu/trove/impl/sync/TSynchronizedCharCollection:c	Lgnu/trove/TCharCollection;
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
    //   0	24	0	this	TSynchronizedCharCollection
    //   5	16	1	Ljava/lang/Object;	Object
    //   19	4	2	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	18	19	finally
    //   19	22	19	finally
  }
  
  /* Error */
  public boolean contains(char o)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 29	gnu/trove/impl/sync/TSynchronizedCharCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 27	gnu/trove/impl/sync/TSynchronizedCharCollection:c	Lgnu/trove/TCharCollection;
    //   11: iload_1
    //   12: invokeinterface 44 2 0
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
    //   0	25	0	this	TSynchronizedCharCollection
    //   0	25	1	o	char
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public char[] toArray()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 29	gnu/trove/impl/sync/TSynchronizedCharCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 27	gnu/trove/impl/sync/TSynchronizedCharCollection:c	Lgnu/trove/TCharCollection;
    //   11: invokeinterface 50 1 0
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
    //   0	24	0	this	TSynchronizedCharCollection
    //   5	16	1	Ljava/lang/Object;	Object
    //   19	4	2	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	18	19	finally
    //   19	22	19	finally
  }
  
  /* Error */
  public char[] toArray(char[] a)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 29	gnu/trove/impl/sync/TSynchronizedCharCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 27	gnu/trove/impl/sync/TSynchronizedCharCollection:c	Lgnu/trove/TCharCollection;
    //   11: aload_1
    //   12: invokeinterface 53 2 0
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
    //   0	25	0	this	TSynchronizedCharCollection
    //   0	25	1	a	char[]
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  public TCharIterator iterator()
  {
    return this.c.iterator();
  }
  
  /* Error */
  public boolean add(char e)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 29	gnu/trove/impl/sync/TSynchronizedCharCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 27	gnu/trove/impl/sync/TSynchronizedCharCollection:c	Lgnu/trove/TCharCollection;
    //   11: iload_1
    //   12: invokeinterface 62 2 0
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
    //   0	25	0	this	TSynchronizedCharCollection
    //   0	25	1	e	char
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public boolean remove(char o)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 29	gnu/trove/impl/sync/TSynchronizedCharCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 27	gnu/trove/impl/sync/TSynchronizedCharCollection:c	Lgnu/trove/TCharCollection;
    //   11: iload_1
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
    //   Java source line #93	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	TSynchronizedCharCollection
    //   0	25	1	o	char
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public boolean containsAll(java.util.Collection<?> coll)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 29	gnu/trove/impl/sync/TSynchronizedCharCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 27	gnu/trove/impl/sync/TSynchronizedCharCollection:c	Lgnu/trove/TCharCollection;
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
    //   Java source line #97	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	TSynchronizedCharCollection
    //   0	25	1	coll	java.util.Collection<?>
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public boolean containsAll(TCharCollection coll)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 29	gnu/trove/impl/sync/TSynchronizedCharCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 27	gnu/trove/impl/sync/TSynchronizedCharCollection:c	Lgnu/trove/TCharCollection;
    //   11: aload_1
    //   12: invokeinterface 76 2 0
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
    //   0	25	0	this	TSynchronizedCharCollection
    //   0	25	1	coll	TCharCollection
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public boolean containsAll(char[] array)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 29	gnu/trove/impl/sync/TSynchronizedCharCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 27	gnu/trove/impl/sync/TSynchronizedCharCollection:c	Lgnu/trove/TCharCollection;
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
    //   Java source line #103	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	TSynchronizedCharCollection
    //   0	25	1	array	char[]
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public boolean addAll(java.util.Collection<? extends Character> coll)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 29	gnu/trove/impl/sync/TSynchronizedCharCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 27	gnu/trove/impl/sync/TSynchronizedCharCollection:c	Lgnu/trove/TCharCollection;
    //   11: aload_1
    //   12: invokeinterface 83 2 0
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
    //   0	25	0	this	TSynchronizedCharCollection
    //   0	25	1	coll	java.util.Collection<? extends Character>
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public boolean addAll(TCharCollection coll)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 29	gnu/trove/impl/sync/TSynchronizedCharCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 27	gnu/trove/impl/sync/TSynchronizedCharCollection:c	Lgnu/trove/TCharCollection;
    //   11: aload_1
    //   12: invokeinterface 86 2 0
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
    //   0	25	0	this	TSynchronizedCharCollection
    //   0	25	1	coll	TCharCollection
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public boolean addAll(char[] array)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 29	gnu/trove/impl/sync/TSynchronizedCharCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 27	gnu/trove/impl/sync/TSynchronizedCharCollection:c	Lgnu/trove/TCharCollection;
    //   11: aload_1
    //   12: invokeinterface 88 2 0
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
    //   0	25	0	this	TSynchronizedCharCollection
    //   0	25	1	array	char[]
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
    //   1: getfield 29	gnu/trove/impl/sync/TSynchronizedCharCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 27	gnu/trove/impl/sync/TSynchronizedCharCollection:c	Lgnu/trove/TCharCollection;
    //   11: aload_1
    //   12: invokeinterface 91 2 0
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
    //   0	25	0	this	TSynchronizedCharCollection
    //   0	25	1	coll	java.util.Collection<?>
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public boolean removeAll(TCharCollection coll)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 29	gnu/trove/impl/sync/TSynchronizedCharCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 27	gnu/trove/impl/sync/TSynchronizedCharCollection:c	Lgnu/trove/TCharCollection;
    //   11: aload_1
    //   12: invokeinterface 93 2 0
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
    //   0	25	0	this	TSynchronizedCharCollection
    //   0	25	1	coll	TCharCollection
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public boolean removeAll(char[] array)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 29	gnu/trove/impl/sync/TSynchronizedCharCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 27	gnu/trove/impl/sync/TSynchronizedCharCollection:c	Lgnu/trove/TCharCollection;
    //   11: aload_1
    //   12: invokeinterface 95 2 0
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
    //   0	25	0	this	TSynchronizedCharCollection
    //   0	25	1	array	char[]
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
    //   1: getfield 29	gnu/trove/impl/sync/TSynchronizedCharCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 27	gnu/trove/impl/sync/TSynchronizedCharCollection:c	Lgnu/trove/TCharCollection;
    //   11: aload_1
    //   12: invokeinterface 98 2 0
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
    //   0	25	0	this	TSynchronizedCharCollection
    //   0	25	1	coll	java.util.Collection<?>
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public boolean retainAll(TCharCollection coll)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 29	gnu/trove/impl/sync/TSynchronizedCharCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 27	gnu/trove/impl/sync/TSynchronizedCharCollection:c	Lgnu/trove/TCharCollection;
    //   11: aload_1
    //   12: invokeinterface 100 2 0
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
    //   0	25	0	this	TSynchronizedCharCollection
    //   0	25	1	coll	TCharCollection
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  /* Error */
  public boolean retainAll(char[] array)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 29	gnu/trove/impl/sync/TSynchronizedCharCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 27	gnu/trove/impl/sync/TSynchronizedCharCollection:c	Lgnu/trove/TCharCollection;
    //   11: aload_1
    //   12: invokeinterface 102 2 0
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
    //   0	25	0	this	TSynchronizedCharCollection
    //   0	25	1	array	char[]
    //   5	17	2	Ljava/lang/Object;	Object
    //   20	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	23	20	finally
  }
  
  public char getNoEntryValue()
  {
    return this.c.getNoEntryValue();
  }
  
  /* Error */
  public boolean forEach(gnu.trove.procedure.TCharProcedure procedure)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 29	gnu/trove/impl/sync/TSynchronizedCharCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 27	gnu/trove/impl/sync/TSynchronizedCharCollection:c	Lgnu/trove/TCharCollection;
    //   11: aload_1
    //   12: invokeinterface 110 2 0
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
    //   0	25	0	this	TSynchronizedCharCollection
    //   0	25	1	procedure	gnu.trove.procedure.TCharProcedure
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
    //   1: getfield 29	gnu/trove/impl/sync/TSynchronizedCharCollection:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 27	gnu/trove/impl/sync/TSynchronizedCharCollection:c	Lgnu/trove/TCharCollection;
    //   11: invokevirtual 119	java/lang/Object:toString	()Ljava/lang/String;
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
    //   0	22	0	this	TSynchronizedCharCollection
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
