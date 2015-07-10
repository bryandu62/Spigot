package gnu.trove.impl.sync;

import gnu.trove.set.TLongSet;

public class TSynchronizedLongSet
  extends TSynchronizedLongCollection
  implements TLongSet
{
  private static final long serialVersionUID = 487447009682186044L;
  
  public TSynchronizedLongSet(TLongSet s)
  {
    super(s);
  }
  
  public TSynchronizedLongSet(TLongSet s, Object mutex)
  {
    super(s, mutex);
  }
  
  /* Error */
  public boolean equals(Object o)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 30	gnu/trove/impl/sync/TSynchronizedLongSet:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 34	gnu/trove/impl/sync/TSynchronizedLongSet:c	Lgnu/trove/TLongCollection;
    //   11: aload_1
    //   12: invokevirtual 38	java/lang/Object:equals	(Ljava/lang/Object;)Z
    //   15: aload_2
    //   16: monitorexit
    //   17: ireturn
    //   18: astore_3
    //   19: aload_2
    //   20: monitorexit
    //   21: aload_3
    //   22: athrow
    // Line number table:
    //   Java source line #65	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	23	0	this	TSynchronizedLongSet
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
    //   1: getfield 30	gnu/trove/impl/sync/TSynchronizedLongSet:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 34	gnu/trove/impl/sync/TSynchronizedLongSet:c	Lgnu/trove/TLongCollection;
    //   11: invokevirtual 43	java/lang/Object:hashCode	()I
    //   14: aload_1
    //   15: monitorexit
    //   16: ireturn
    //   17: astore_2
    //   18: aload_1
    //   19: monitorexit
    //   20: aload_2
    //   21: athrow
    // Line number table:
    //   Java source line #68	-> byte code offset #0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	22	0	this	TSynchronizedLongSet
    //   5	14	1	Ljava/lang/Object;	Object
    //   17	4	2	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	16	17	finally
    //   17	20	17	finally
  }
}
