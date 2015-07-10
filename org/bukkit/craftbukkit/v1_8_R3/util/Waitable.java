package org.bukkit.craftbukkit.v1_8_R3.util;

import java.util.concurrent.ExecutionException;

public abstract class Waitable<T>
  implements Runnable
{
  /* Error */
  public final void run()
  {
    // Byte code:
    //   0: aload_0
    //   1: dup
    //   2: astore_1
    //   3: monitorenter
    //   4: aload_0
    //   5: getfield 31	org/bukkit/craftbukkit/v1_8_R3/util/Waitable:status	Lorg/bukkit/craftbukkit/v1_8_R3/util/Waitable$Status;
    //   8: getstatic 29	org/bukkit/craftbukkit/v1_8_R3/util/Waitable$Status:WAITING	Lorg/bukkit/craftbukkit/v1_8_R3/util/Waitable$Status;
    //   11: if_acmpeq +30 -> 41
    //   14: new 39	java/lang/IllegalStateException
    //   17: dup
    //   18: new 41	java/lang/StringBuilder
    //   21: dup
    //   22: ldc 43
    //   24: invokespecial 46	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   27: aload_0
    //   28: getfield 31	org/bukkit/craftbukkit/v1_8_R3/util/Waitable:status	Lorg/bukkit/craftbukkit/v1_8_R3/util/Waitable$Status;
    //   31: invokevirtual 50	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   34: invokevirtual 54	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   37: invokespecial 55	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   40: athrow
    //   41: aload_0
    //   42: getstatic 58	org/bukkit/craftbukkit/v1_8_R3/util/Waitable$Status:RUNNING	Lorg/bukkit/craftbukkit/v1_8_R3/util/Waitable$Status;
    //   45: putfield 31	org/bukkit/craftbukkit/v1_8_R3/util/Waitable:status	Lorg/bukkit/craftbukkit/v1_8_R3/util/Waitable$Status;
    //   48: aload_1
    //   49: monitorexit
    //   50: goto +6 -> 56
    //   53: aload_1
    //   54: monitorexit
    //   55: athrow
    //   56: aload_0
    //   57: aload_0
    //   58: invokevirtual 62	org/bukkit/craftbukkit/v1_8_R3/util/Waitable:evaluate	()Ljava/lang/Object;
    //   61: putfield 26	org/bukkit/craftbukkit/v1_8_R3/util/Waitable:value	Ljava/lang/Object;
    //   64: goto +58 -> 122
    //   67: astore_1
    //   68: aload_0
    //   69: aload_1
    //   70: putfield 24	org/bukkit/craftbukkit/v1_8_R3/util/Waitable:t	Ljava/lang/Throwable;
    //   73: aload_0
    //   74: dup
    //   75: astore_2
    //   76: monitorenter
    //   77: aload_0
    //   78: getstatic 65	org/bukkit/craftbukkit/v1_8_R3/util/Waitable$Status:FINISHED	Lorg/bukkit/craftbukkit/v1_8_R3/util/Waitable$Status;
    //   81: putfield 31	org/bukkit/craftbukkit/v1_8_R3/util/Waitable:status	Lorg/bukkit/craftbukkit/v1_8_R3/util/Waitable$Status;
    //   84: aload_0
    //   85: invokevirtual 68	java/lang/Object:notifyAll	()V
    //   88: aload_2
    //   89: monitorexit
    //   90: goto +55 -> 145
    //   93: aload_2
    //   94: monitorexit
    //   95: athrow
    //   96: astore_3
    //   97: aload_0
    //   98: dup
    //   99: astore_2
    //   100: monitorenter
    //   101: aload_0
    //   102: getstatic 65	org/bukkit/craftbukkit/v1_8_R3/util/Waitable$Status:FINISHED	Lorg/bukkit/craftbukkit/v1_8_R3/util/Waitable$Status;
    //   105: putfield 31	org/bukkit/craftbukkit/v1_8_R3/util/Waitable:status	Lorg/bukkit/craftbukkit/v1_8_R3/util/Waitable$Status;
    //   108: aload_0
    //   109: invokevirtual 68	java/lang/Object:notifyAll	()V
    //   112: aload_2
    //   113: monitorexit
    //   114: goto +6 -> 120
    //   117: aload_2
    //   118: monitorexit
    //   119: athrow
    //   120: aload_3
    //   121: athrow
    //   122: aload_0
    //   123: dup
    //   124: astore_2
    //   125: monitorenter
    //   126: aload_0
    //   127: getstatic 65	org/bukkit/craftbukkit/v1_8_R3/util/Waitable$Status:FINISHED	Lorg/bukkit/craftbukkit/v1_8_R3/util/Waitable$Status;
    //   130: putfield 31	org/bukkit/craftbukkit/v1_8_R3/util/Waitable:status	Lorg/bukkit/craftbukkit/v1_8_R3/util/Waitable$Status;
    //   133: aload_0
    //   134: invokevirtual 68	java/lang/Object:notifyAll	()V
    //   137: aload_2
    //   138: monitorexit
    //   139: goto +6 -> 145
    //   142: aload_2
    //   143: monitorexit
    //   144: athrow
    //   145: return
    // Line number table:
    //   Java source line #17	-> byte code offset #0
    //   Java source line #18	-> byte code offset #4
    //   Java source line #19	-> byte code offset #14
    //   Java source line #21	-> byte code offset #41
    //   Java source line #17	-> byte code offset #48
    //   Java source line #24	-> byte code offset #56
    //   Java source line #25	-> byte code offset #64
    //   Java source line #26	-> byte code offset #68
    //   Java source line #28	-> byte code offset #73
    //   Java source line #29	-> byte code offset #77
    //   Java source line #30	-> byte code offset #84
    //   Java source line #28	-> byte code offset #88
    //   Java source line #27	-> byte code offset #96
    //   Java source line #28	-> byte code offset #97
    //   Java source line #29	-> byte code offset #101
    //   Java source line #30	-> byte code offset #108
    //   Java source line #28	-> byte code offset #112
    //   Java source line #32	-> byte code offset #120
    //   Java source line #28	-> byte code offset #122
    //   Java source line #29	-> byte code offset #126
    //   Java source line #30	-> byte code offset #133
    //   Java source line #28	-> byte code offset #137
    //   Java source line #33	-> byte code offset #145
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	146	0	this	Waitable<T>
    //   2	52	1	Ljava/lang/Object;	Object
    //   67	3	1	t	Throwable
    //   75	19	2	Ljava/lang/Object;	Object
    //   99	19	2	Ljava/lang/Object;	Object
    //   124	19	2	Ljava/lang/Object;	Object
    //   96	25	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   4	50	53	finally
    //   53	55	53	finally
    //   56	64	67	java/lang/Throwable
    //   77	90	93	finally
    //   93	95	93	finally
    //   56	73	96	finally
    //   101	114	117	finally
    //   117	119	117	finally
    //   126	139	142	finally
    //   142	144	142	finally
  }
  
  protected abstract T evaluate();
  
  private static enum Status
  {
    WAITING,  RUNNING,  FINISHED;
  }
  
  Throwable t = null;
  T value = null;
  Status status = Status.WAITING;
  
  public synchronized T get()
    throws InterruptedException, ExecutionException
  {
    while (this.status != Status.FINISHED) {
      wait();
    }
    if (this.t != null) {
      throw new ExecutionException(this.t);
    }
    return (T)this.value;
  }
}
