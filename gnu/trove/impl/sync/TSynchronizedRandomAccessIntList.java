package gnu.trove.impl.sync;

import gnu.trove.list.TIntList;
import java.util.RandomAccess;

public class TSynchronizedRandomAccessIntList
  extends TSynchronizedIntList
  implements RandomAccess
{
  static final long serialVersionUID = 1530674583602358482L;
  
  public TSynchronizedRandomAccessIntList(TIntList list)
  {
    super(list);
  }
  
  public TSynchronizedRandomAccessIntList(TIntList list, Object mutex)
  {
    super(list, mutex);
  }
  
  /* Error */
  public TIntList subList(int fromIndex, int toIndex)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 28	gnu/trove/impl/sync/TSynchronizedRandomAccessIntList:mutex	Ljava/lang/Object;
    //   4: dup
    //   5: astore_3
    //   6: monitorenter
    //   7: new 2	gnu/trove/impl/sync/TSynchronizedRandomAccessIntList
    //   10: dup
    //   11: aload_0
    //   12: getfield 30	gnu/trove/impl/sync/TSynchronizedRandomAccessIntList:list	Lgnu/trove/list/TIntList;
    //   15: iload_1
    //   16: iload_2
    //   17: invokeinterface 34 3 0
    //   22: aload_0
    //   23: getfield 28	gnu/trove/impl/sync/TSynchronizedRandomAccessIntList:mutex	Ljava/lang/Object;
    //   26: invokespecial 35	gnu/trove/impl/sync/TSynchronizedRandomAccessIntList:<init>	(Lgnu/trove/list/TIntList;Ljava/lang/Object;)V
    //   29: aload_3
    //   30: monitorexit
    //   31: areturn
    //   32: astore 4
    //   34: aload_3
    //   35: monitorexit
    //   36: aload 4
    //   38: athrow
    // Line number table:
    //   Java source line #66	-> byte code offset #0
    //   Java source line #67	-> byte code offset #7
    //   Java source line #69	-> byte code offset #32
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	39	0	this	TSynchronizedRandomAccessIntList
    //   0	39	1	fromIndex	int
    //   0	39	2	toIndex	int
    //   5	30	3	Ljava/lang/Object;	Object
    //   32	5	4	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	31	32	finally
    //   32	36	32	finally
  }
  
  private Object writeReplace()
  {
    return new TSynchronizedIntList(this.list);
  }
}
