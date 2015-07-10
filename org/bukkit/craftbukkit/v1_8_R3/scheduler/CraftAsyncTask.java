package org.bukkit.craftbukkit.v1_8_R3.scheduler;

import java.util.LinkedList;
import java.util.Map;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitWorker;

class CraftAsyncTask
  extends CraftTask
{
  private final LinkedList<BukkitWorker> workers = new LinkedList();
  private final Map<Integer, CraftTask> runners;
  
  CraftAsyncTask(Map<Integer, CraftTask> runners, Plugin plugin, Runnable task, int id, long delay)
  {
    super(plugin, task, id, delay);
    this.runners = runners;
  }
  
  public boolean isSync()
  {
    return false;
  }
  
  /* Error */
  public void run()
  {
    // Byte code:
    //   0: invokestatic 48	java/lang/Thread:currentThread	()Ljava/lang/Thread;
    //   3: astore_1
    //   4: aload_0
    //   5: getfield 25	org/bukkit/craftbukkit/v1_8_R3/scheduler/CraftAsyncTask:workers	Ljava/util/LinkedList;
    //   8: dup
    //   9: astore_2
    //   10: monitorenter
    //   11: aload_0
    //   12: invokevirtual 52	org/bukkit/craftbukkit/v1_8_R3/scheduler/CraftAsyncTask:getPeriod	()J
    //   15: ldc2_w 53
    //   18: lcmp
    //   19: ifne +6 -> 25
    //   22: aload_2
    //   23: monitorexit
    //   24: return
    //   25: aload_0
    //   26: getfield 25	org/bukkit/craftbukkit/v1_8_R3/scheduler/CraftAsyncTask:workers	Ljava/util/LinkedList;
    //   29: new 7	org/bukkit/craftbukkit/v1_8_R3/scheduler/CraftAsyncTask$1
    //   32: dup
    //   33: aload_0
    //   34: aload_1
    //   35: invokespecial 57	org/bukkit/craftbukkit/v1_8_R3/scheduler/CraftAsyncTask$1:<init>	(Lorg/bukkit/craftbukkit/v1_8_R3/scheduler/CraftAsyncTask;Ljava/lang/Thread;)V
    //   38: invokevirtual 61	java/util/LinkedList:add	(Ljava/lang/Object;)Z
    //   41: pop
    //   42: aload_2
    //   43: monitorexit
    //   44: goto +6 -> 50
    //   47: aload_2
    //   48: monitorexit
    //   49: athrow
    //   50: aconst_null
    //   51: astore_2
    //   52: aload_0
    //   53: invokespecial 63	org/bukkit/craftbukkit/v1_8_R3/scheduler/CraftTask:run	()V
    //   56: goto +261 -> 317
    //   59: astore_3
    //   60: aload_3
    //   61: astore_2
    //   62: new 65	org/apache/commons/lang/UnhandledException
    //   65: dup
    //   66: ldc 67
    //   68: iconst_2
    //   69: anewarray 69	java/lang/Object
    //   72: dup
    //   73: iconst_0
    //   74: aload_0
    //   75: invokevirtual 73	org/bukkit/craftbukkit/v1_8_R3/scheduler/CraftAsyncTask:getOwner	()Lorg/bukkit/plugin/Plugin;
    //   78: invokeinterface 79 1 0
    //   83: invokevirtual 85	org/bukkit/plugin/PluginDescriptionFile:getFullName	()Ljava/lang/String;
    //   86: aastore
    //   87: dup
    //   88: iconst_1
    //   89: aload_0
    //   90: invokevirtual 89	org/bukkit/craftbukkit/v1_8_R3/scheduler/CraftAsyncTask:getTaskId	()I
    //   93: invokestatic 95	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   96: aastore
    //   97: invokestatic 101	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   100: aload_2
    //   101: invokespecial 104	org/apache/commons/lang/UnhandledException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   104: athrow
    //   105: astore 4
    //   107: aload_0
    //   108: getfield 25	org/bukkit/craftbukkit/v1_8_R3/scheduler/CraftAsyncTask:workers	Ljava/util/LinkedList;
    //   111: dup
    //   112: astore 5
    //   114: monitorenter
    //   115: aload_0
    //   116: getfield 25	org/bukkit/craftbukkit/v1_8_R3/scheduler/CraftAsyncTask:workers	Ljava/util/LinkedList;
    //   119: invokevirtual 108	java/util/LinkedList:iterator	()Ljava/util/Iterator;
    //   122: astore 6
    //   124: iconst_0
    //   125: istore 7
    //   127: goto +35 -> 162
    //   130: aload 6
    //   132: invokeinterface 114 1 0
    //   137: checkcast 116	org/bukkit/scheduler/BukkitWorker
    //   140: invokeinterface 119 1 0
    //   145: aload_1
    //   146: if_acmpne +16 -> 162
    //   149: aload 6
    //   151: invokeinterface 122 1 0
    //   156: iconst_1
    //   157: istore 7
    //   159: goto +13 -> 172
    //   162: aload 6
    //   164: invokeinterface 125 1 0
    //   169: ifne -39 -> 130
    //   172: iload 7
    //   174: ifne +94 -> 268
    //   177: new 127	java/lang/IllegalStateException
    //   180: dup
    //   181: ldc -127
    //   183: iconst_3
    //   184: anewarray 69	java/lang/Object
    //   187: dup
    //   188: iconst_0
    //   189: aload_1
    //   190: invokevirtual 132	java/lang/Thread:getName	()Ljava/lang/String;
    //   193: aastore
    //   194: dup
    //   195: iconst_1
    //   196: aload_0
    //   197: invokevirtual 89	org/bukkit/craftbukkit/v1_8_R3/scheduler/CraftAsyncTask:getTaskId	()I
    //   200: invokestatic 95	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   203: aastore
    //   204: dup
    //   205: iconst_2
    //   206: aload_0
    //   207: invokevirtual 73	org/bukkit/craftbukkit/v1_8_R3/scheduler/CraftAsyncTask:getOwner	()Lorg/bukkit/plugin/Plugin;
    //   210: invokeinterface 79 1 0
    //   215: invokevirtual 85	org/bukkit/plugin/PluginDescriptionFile:getFullName	()Ljava/lang/String;
    //   218: aastore
    //   219: invokestatic 101	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   222: aload_2
    //   223: invokespecial 133	java/lang/IllegalStateException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   226: athrow
    //   227: astore 8
    //   229: aload_0
    //   230: invokevirtual 52	org/bukkit/craftbukkit/v1_8_R3/scheduler/CraftAsyncTask:getPeriod	()J
    //   233: lconst_0
    //   234: lcmp
    //   235: ifge +30 -> 265
    //   238: aload_0
    //   239: getfield 25	org/bukkit/craftbukkit/v1_8_R3/scheduler/CraftAsyncTask:workers	Ljava/util/LinkedList;
    //   242: invokevirtual 136	java/util/LinkedList:isEmpty	()Z
    //   245: ifeq +20 -> 265
    //   248: aload_0
    //   249: getfield 27	org/bukkit/craftbukkit/v1_8_R3/scheduler/CraftAsyncTask:runners	Ljava/util/Map;
    //   252: aload_0
    //   253: invokevirtual 89	org/bukkit/craftbukkit/v1_8_R3/scheduler/CraftAsyncTask:getTaskId	()I
    //   256: invokestatic 95	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   259: invokeinterface 141 2 0
    //   264: pop
    //   265: aload 8
    //   267: athrow
    //   268: aload_0
    //   269: invokevirtual 52	org/bukkit/craftbukkit/v1_8_R3/scheduler/CraftAsyncTask:getPeriod	()J
    //   272: lconst_0
    //   273: lcmp
    //   274: ifge +30 -> 304
    //   277: aload_0
    //   278: getfield 25	org/bukkit/craftbukkit/v1_8_R3/scheduler/CraftAsyncTask:workers	Ljava/util/LinkedList;
    //   281: invokevirtual 136	java/util/LinkedList:isEmpty	()Z
    //   284: ifeq +20 -> 304
    //   287: aload_0
    //   288: getfield 27	org/bukkit/craftbukkit/v1_8_R3/scheduler/CraftAsyncTask:runners	Ljava/util/Map;
    //   291: aload_0
    //   292: invokevirtual 89	org/bukkit/craftbukkit/v1_8_R3/scheduler/CraftAsyncTask:getTaskId	()I
    //   295: invokestatic 95	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   298: invokeinterface 141 2 0
    //   303: pop
    //   304: aload 5
    //   306: monitorexit
    //   307: goto +7 -> 314
    //   310: aload 5
    //   312: monitorexit
    //   313: athrow
    //   314: aload 4
    //   316: athrow
    //   317: aload_0
    //   318: getfield 25	org/bukkit/craftbukkit/v1_8_R3/scheduler/CraftAsyncTask:workers	Ljava/util/LinkedList;
    //   321: dup
    //   322: astore 5
    //   324: monitorenter
    //   325: aload_0
    //   326: getfield 25	org/bukkit/craftbukkit/v1_8_R3/scheduler/CraftAsyncTask:workers	Ljava/util/LinkedList;
    //   329: invokevirtual 108	java/util/LinkedList:iterator	()Ljava/util/Iterator;
    //   332: astore 6
    //   334: iconst_0
    //   335: istore 7
    //   337: goto +35 -> 372
    //   340: aload 6
    //   342: invokeinterface 114 1 0
    //   347: checkcast 116	org/bukkit/scheduler/BukkitWorker
    //   350: invokeinterface 119 1 0
    //   355: aload_1
    //   356: if_acmpne +16 -> 372
    //   359: aload 6
    //   361: invokeinterface 122 1 0
    //   366: iconst_1
    //   367: istore 7
    //   369: goto +13 -> 382
    //   372: aload 6
    //   374: invokeinterface 125 1 0
    //   379: ifne -39 -> 340
    //   382: iload 7
    //   384: ifne +94 -> 478
    //   387: new 127	java/lang/IllegalStateException
    //   390: dup
    //   391: ldc -127
    //   393: iconst_3
    //   394: anewarray 69	java/lang/Object
    //   397: dup
    //   398: iconst_0
    //   399: aload_1
    //   400: invokevirtual 132	java/lang/Thread:getName	()Ljava/lang/String;
    //   403: aastore
    //   404: dup
    //   405: iconst_1
    //   406: aload_0
    //   407: invokevirtual 89	org/bukkit/craftbukkit/v1_8_R3/scheduler/CraftAsyncTask:getTaskId	()I
    //   410: invokestatic 95	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   413: aastore
    //   414: dup
    //   415: iconst_2
    //   416: aload_0
    //   417: invokevirtual 73	org/bukkit/craftbukkit/v1_8_R3/scheduler/CraftAsyncTask:getOwner	()Lorg/bukkit/plugin/Plugin;
    //   420: invokeinterface 79 1 0
    //   425: invokevirtual 85	org/bukkit/plugin/PluginDescriptionFile:getFullName	()Ljava/lang/String;
    //   428: aastore
    //   429: invokestatic 101	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   432: aload_2
    //   433: invokespecial 133	java/lang/IllegalStateException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   436: athrow
    //   437: astore 8
    //   439: aload_0
    //   440: invokevirtual 52	org/bukkit/craftbukkit/v1_8_R3/scheduler/CraftAsyncTask:getPeriod	()J
    //   443: lconst_0
    //   444: lcmp
    //   445: ifge +30 -> 475
    //   448: aload_0
    //   449: getfield 25	org/bukkit/craftbukkit/v1_8_R3/scheduler/CraftAsyncTask:workers	Ljava/util/LinkedList;
    //   452: invokevirtual 136	java/util/LinkedList:isEmpty	()Z
    //   455: ifeq +20 -> 475
    //   458: aload_0
    //   459: getfield 27	org/bukkit/craftbukkit/v1_8_R3/scheduler/CraftAsyncTask:runners	Ljava/util/Map;
    //   462: aload_0
    //   463: invokevirtual 89	org/bukkit/craftbukkit/v1_8_R3/scheduler/CraftAsyncTask:getTaskId	()I
    //   466: invokestatic 95	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   469: invokeinterface 141 2 0
    //   474: pop
    //   475: aload 8
    //   477: athrow
    //   478: aload_0
    //   479: invokevirtual 52	org/bukkit/craftbukkit/v1_8_R3/scheduler/CraftAsyncTask:getPeriod	()J
    //   482: lconst_0
    //   483: lcmp
    //   484: ifge +30 -> 514
    //   487: aload_0
    //   488: getfield 25	org/bukkit/craftbukkit/v1_8_R3/scheduler/CraftAsyncTask:workers	Ljava/util/LinkedList;
    //   491: invokevirtual 136	java/util/LinkedList:isEmpty	()Z
    //   494: ifeq +20 -> 514
    //   497: aload_0
    //   498: getfield 27	org/bukkit/craftbukkit/v1_8_R3/scheduler/CraftAsyncTask:runners	Ljava/util/Map;
    //   501: aload_0
    //   502: invokevirtual 89	org/bukkit/craftbukkit/v1_8_R3/scheduler/CraftAsyncTask:getTaskId	()I
    //   505: invokestatic 95	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   508: invokeinterface 141 2 0
    //   513: pop
    //   514: aload 5
    //   516: monitorexit
    //   517: goto +7 -> 524
    //   520: aload 5
    //   522: monitorexit
    //   523: athrow
    //   524: return
    // Line number table:
    //   Java source line #29	-> byte code offset #0
    //   Java source line #30	-> byte code offset #4
    //   Java source line #31	-> byte code offset #11
    //   Java source line #34	-> byte code offset #22
    //   Java source line #36	-> byte code offset #25
    //   Java source line #37	-> byte code offset #29
    //   Java source line #36	-> byte code offset #38
    //   Java source line #30	-> byte code offset #42
    //   Java source line #51	-> byte code offset #50
    //   Java source line #53	-> byte code offset #52
    //   Java source line #54	-> byte code offset #56
    //   Java source line #55	-> byte code offset #60
    //   Java source line #56	-> byte code offset #62
    //   Java source line #58	-> byte code offset #66
    //   Java source line #59	-> byte code offset #74
    //   Java source line #60	-> byte code offset #89
    //   Java source line #57	-> byte code offset #97
    //   Java source line #61	-> byte code offset #100
    //   Java source line #56	-> byte code offset #101
    //   Java source line #62	-> byte code offset #105
    //   Java source line #64	-> byte code offset #107
    //   Java source line #66	-> byte code offset #115
    //   Java source line #67	-> byte code offset #124
    //   Java source line #68	-> byte code offset #127
    //   Java source line #69	-> byte code offset #130
    //   Java source line #70	-> byte code offset #149
    //   Java source line #71	-> byte code offset #156
    //   Java source line #72	-> byte code offset #159
    //   Java source line #68	-> byte code offset #162
    //   Java source line #75	-> byte code offset #172
    //   Java source line #76	-> byte code offset #177
    //   Java source line #78	-> byte code offset #181
    //   Java source line #79	-> byte code offset #189
    //   Java source line #80	-> byte code offset #196
    //   Java source line #81	-> byte code offset #206
    //   Java source line #77	-> byte code offset #219
    //   Java source line #82	-> byte code offset #222
    //   Java source line #76	-> byte code offset #223
    //   Java source line #84	-> byte code offset #227
    //   Java source line #85	-> byte code offset #229
    //   Java source line #88	-> byte code offset #248
    //   Java source line #90	-> byte code offset #265
    //   Java source line #85	-> byte code offset #268
    //   Java source line #88	-> byte code offset #287
    //   Java source line #64	-> byte code offset #304
    //   Java source line #92	-> byte code offset #314
    //   Java source line #64	-> byte code offset #317
    //   Java source line #66	-> byte code offset #325
    //   Java source line #67	-> byte code offset #334
    //   Java source line #68	-> byte code offset #337
    //   Java source line #69	-> byte code offset #340
    //   Java source line #70	-> byte code offset #359
    //   Java source line #71	-> byte code offset #366
    //   Java source line #72	-> byte code offset #369
    //   Java source line #68	-> byte code offset #372
    //   Java source line #75	-> byte code offset #382
    //   Java source line #76	-> byte code offset #387
    //   Java source line #78	-> byte code offset #391
    //   Java source line #79	-> byte code offset #399
    //   Java source line #80	-> byte code offset #406
    //   Java source line #81	-> byte code offset #416
    //   Java source line #77	-> byte code offset #429
    //   Java source line #82	-> byte code offset #432
    //   Java source line #76	-> byte code offset #433
    //   Java source line #84	-> byte code offset #437
    //   Java source line #85	-> byte code offset #439
    //   Java source line #88	-> byte code offset #458
    //   Java source line #90	-> byte code offset #475
    //   Java source line #85	-> byte code offset #478
    //   Java source line #88	-> byte code offset #497
    //   Java source line #64	-> byte code offset #514
    //   Java source line #93	-> byte code offset #524
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	525	0	this	CraftAsyncTask
    //   3	397	1	thread	Thread
    //   9	39	2	Ljava/lang/Object;	Object
    //   51	382	2	thrown	Throwable
    //   59	2	3	t	Throwable
    //   105	210	4	localObject1	Object
    //   112	199	5	Ljava/lang/Object;	Object
    //   322	199	5	Ljava/lang/Object;	Object
    //   122	41	6	workers	java.util.Iterator<BukkitWorker>
    //   332	41	6	workers	java.util.Iterator<BukkitWorker>
    //   125	48	7	removed	boolean
    //   335	48	7	removed	boolean
    //   227	39	8	localObject2	Object
    //   437	39	8	localObject3	Object
    // Exception table:
    //   from	to	target	type
    //   11	24	47	finally
    //   25	44	47	finally
    //   47	49	47	finally
    //   52	56	59	java/lang/Throwable
    //   52	105	105	finally
    //   115	227	227	finally
    //   115	307	310	finally
    //   310	313	310	finally
    //   325	437	437	finally
    //   325	517	520	finally
    //   520	523	520	finally
  }
  
  LinkedList<BukkitWorker> getWorkers()
  {
    return this.workers;
  }
  
  boolean cancel0()
  {
    synchronized (this.workers)
    {
      setPeriod(-2L);
      if (this.workers.isEmpty()) {
        this.runners.remove(Integer.valueOf(getTaskId()));
      }
    }
    return true;
  }
}
