package org.bukkit.util;

public class FileUtil
{
  /* Error */
  public static boolean copy(java.io.File inFile, java.io.File outFile)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 21	java/io/File:exists	()Z
    //   4: ifne +5 -> 9
    //   7: iconst_0
    //   8: ireturn
    //   9: aconst_null
    //   10: astore_2
    //   11: aconst_null
    //   12: astore_3
    //   13: new 23	java/io/FileInputStream
    //   16: dup
    //   17: aload_0
    //   18: invokespecial 26	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   21: invokevirtual 30	java/io/FileInputStream:getChannel	()Ljava/nio/channels/FileChannel;
    //   24: astore_2
    //   25: new 32	java/io/FileOutputStream
    //   28: dup
    //   29: aload_1
    //   30: invokespecial 33	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
    //   33: invokevirtual 34	java/io/FileOutputStream:getChannel	()Ljava/nio/channels/FileChannel;
    //   36: astore_3
    //   37: lconst_0
    //   38: lstore 4
    //   40: aload_2
    //   41: invokevirtual 40	java/nio/channels/FileChannel:size	()J
    //   44: lstore 6
    //   46: goto +18 -> 64
    //   49: lload 4
    //   51: aload_2
    //   52: lload 4
    //   54: ldc2_w 41
    //   57: aload_3
    //   58: invokevirtual 46	java/nio/channels/FileChannel:transferTo	(JJLjava/nio/channels/WritableByteChannel;)J
    //   61: ladd
    //   62: lstore 4
    //   64: lload 4
    //   66: lload 6
    //   68: lcmp
    //   69: iflt -20 -> 49
    //   72: goto +55 -> 127
    //   75: pop
    //   76: aload_2
    //   77: ifnull +7 -> 84
    //   80: aload_2
    //   81: invokevirtual 49	java/nio/channels/FileChannel:close	()V
    //   84: aload_3
    //   85: ifnull +13 -> 98
    //   88: aload_3
    //   89: invokevirtual 49	java/nio/channels/FileChannel:close	()V
    //   92: goto +6 -> 98
    //   95: pop
    //   96: iconst_0
    //   97: ireturn
    //   98: iconst_0
    //   99: ireturn
    //   100: astore 8
    //   102: aload_2
    //   103: ifnull +7 -> 110
    //   106: aload_2
    //   107: invokevirtual 49	java/nio/channels/FileChannel:close	()V
    //   110: aload_3
    //   111: ifnull +13 -> 124
    //   114: aload_3
    //   115: invokevirtual 49	java/nio/channels/FileChannel:close	()V
    //   118: goto +6 -> 124
    //   121: pop
    //   122: iconst_0
    //   123: ireturn
    //   124: aload 8
    //   126: athrow
    //   127: aload_2
    //   128: ifnull +7 -> 135
    //   131: aload_2
    //   132: invokevirtual 49	java/nio/channels/FileChannel:close	()V
    //   135: aload_3
    //   136: ifnull +13 -> 149
    //   139: aload_3
    //   140: invokevirtual 49	java/nio/channels/FileChannel:close	()V
    //   143: goto +6 -> 149
    //   146: pop
    //   147: iconst_0
    //   148: ireturn
    //   149: iconst_1
    //   150: ireturn
    // Line number table:
    //   Java source line #22	-> byte code offset #0
    //   Java source line #23	-> byte code offset #7
    //   Java source line #26	-> byte code offset #9
    //   Java source line #27	-> byte code offset #11
    //   Java source line #30	-> byte code offset #13
    //   Java source line #31	-> byte code offset #25
    //   Java source line #33	-> byte code offset #37
    //   Java source line #34	-> byte code offset #40
    //   Java source line #36	-> byte code offset #46
    //   Java source line #37	-> byte code offset #49
    //   Java source line #36	-> byte code offset #64
    //   Java source line #39	-> byte code offset #72
    //   Java source line #43	-> byte code offset #76
    //   Java source line #44	-> byte code offset #80
    //   Java source line #46	-> byte code offset #84
    //   Java source line #47	-> byte code offset #88
    //   Java source line #49	-> byte code offset #92
    //   Java source line #50	-> byte code offset #96
    //   Java source line #40	-> byte code offset #98
    //   Java source line #41	-> byte code offset #100
    //   Java source line #43	-> byte code offset #102
    //   Java source line #44	-> byte code offset #106
    //   Java source line #46	-> byte code offset #110
    //   Java source line #47	-> byte code offset #114
    //   Java source line #49	-> byte code offset #118
    //   Java source line #50	-> byte code offset #122
    //   Java source line #52	-> byte code offset #124
    //   Java source line #43	-> byte code offset #127
    //   Java source line #44	-> byte code offset #131
    //   Java source line #46	-> byte code offset #135
    //   Java source line #47	-> byte code offset #139
    //   Java source line #49	-> byte code offset #143
    //   Java source line #50	-> byte code offset #147
    //   Java source line #54	-> byte code offset #149
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	151	0	inFile	java.io.File
    //   0	151	1	outFile	java.io.File
    //   10	122	2	in	java.nio.channels.FileChannel
    //   12	128	3	out	java.nio.channels.FileChannel
    //   38	27	4	pos	long
    //   44	23	6	size	long
    //   75	1	7	localIOException1	java.io.IOException
    //   95	1	8	localIOException2	java.io.IOException
    //   100	25	8	localObject	Object
    //   121	1	9	localIOException3	java.io.IOException
    //   146	1	10	localIOException4	java.io.IOException
    // Exception table:
    //   from	to	target	type
    //   13	72	75	java/io/IOException
    //   76	92	95	java/io/IOException
    //   13	76	100	finally
    //   102	118	121	java/io/IOException
    //   127	143	146	java/io/IOException
  }
}
