package com.avaje.ebean.enhance.agent;

import java.net.URL;
import java.net.URLClassLoader;

public class LocalClassLoader
  extends URLClassLoader
{
  public LocalClassLoader(URL[] urls, ClassLoader loader)
  {
    super(urls, loader);
  }
  
  /* Error */
  protected synchronized Class<?> loadClass(String name, boolean resolve)
    throws java.lang.ClassNotFoundException
  {
    // Byte code:
    //   0: aload_1
    //   1: ldc 25
    //   3: invokevirtual 31	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   6: ifeq +10 -> 16
    //   9: aload_0
    //   10: aload_1
    //   11: iload_2
    //   12: invokespecial 33	java/net/URLClassLoader:loadClass	(Ljava/lang/String;Z)Ljava/lang/Class;
    //   15: areturn
    //   16: aload_0
    //   17: aload_1
    //   18: invokespecial 37	java/net/URLClassLoader:findLoadedClass	(Ljava/lang/String;)Ljava/lang/Class;
    //   21: astore_3
    //   22: aload_3
    //   23: ifnull +5 -> 28
    //   26: aload_3
    //   27: areturn
    //   28: new 39	java/lang/StringBuilder
    //   31: dup
    //   32: invokespecial 42	java/lang/StringBuilder:<init>	()V
    //   35: aload_1
    //   36: bipush 46
    //   38: bipush 47
    //   40: invokevirtual 46	java/lang/String:replace	(CC)Ljava/lang/String;
    //   43: invokevirtual 50	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   46: ldc 52
    //   48: invokevirtual 50	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   51: invokevirtual 56	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   54: astore 4
    //   56: aload_0
    //   57: aload 4
    //   59: invokespecial 60	java/net/URLClassLoader:getResource	(Ljava/lang/String;)Ljava/net/URL;
    //   62: astore 5
    //   64: aload 5
    //   66: ifnonnull +12 -> 78
    //   69: new 19	java/lang/ClassNotFoundException
    //   72: dup
    //   73: aload_1
    //   74: invokespecial 63	java/lang/ClassNotFoundException:<init>	(Ljava/lang/String;)V
    //   77: athrow
    //   78: new 65	java/io/File
    //   81: dup
    //   82: new 39	java/lang/StringBuilder
    //   85: dup
    //   86: invokespecial 42	java/lang/StringBuilder:<init>	()V
    //   89: ldc 67
    //   91: invokevirtual 50	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   94: aload 4
    //   96: invokevirtual 50	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   99: invokevirtual 56	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   102: invokespecial 68	java/io/File:<init>	(Ljava/lang/String;)V
    //   105: astore 6
    //   107: getstatic 74	java/lang/System:out	Ljava/io/PrintStream;
    //   110: new 39	java/lang/StringBuilder
    //   113: dup
    //   114: invokespecial 42	java/lang/StringBuilder:<init>	()V
    //   117: ldc 76
    //   119: invokevirtual 50	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   122: aload 6
    //   124: invokevirtual 80	java/io/File:length	()J
    //   127: invokevirtual 83	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   130: ldc 85
    //   132: invokevirtual 50	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   135: aload 6
    //   137: invokevirtual 88	java/io/File:getName	()Ljava/lang/String;
    //   140: invokevirtual 50	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   143: invokevirtual 56	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   146: invokevirtual 93	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   149: aload 5
    //   151: invokevirtual 99	java/net/URL:openStream	()Ljava/io/InputStream;
    //   154: astore 7
    //   156: new 101	java/io/ByteArrayOutputStream
    //   159: dup
    //   160: invokespecial 102	java/io/ByteArrayOutputStream:<init>	()V
    //   163: astore 8
    //   165: sipush 2048
    //   168: newarray <illegal type>
    //   170: astore 9
    //   172: aload 7
    //   174: aload 9
    //   176: iconst_0
    //   177: sipush 2048
    //   180: invokevirtual 108	java/io/InputStream:read	([BII)I
    //   183: dup
    //   184: istore 10
    //   186: iconst_m1
    //   187: if_icmpeq +16 -> 203
    //   190: aload 8
    //   192: aload 9
    //   194: iconst_0
    //   195: iload 10
    //   197: invokevirtual 112	java/io/ByteArrayOutputStream:write	([BII)V
    //   200: goto -28 -> 172
    //   203: aload 8
    //   205: invokevirtual 116	java/io/ByteArrayOutputStream:toByteArray	()[B
    //   208: astore 11
    //   210: getstatic 119	java/lang/System:err	Ljava/io/PrintStream;
    //   213: new 39	java/lang/StringBuilder
    //   216: dup
    //   217: invokespecial 42	java/lang/StringBuilder:<init>	()V
    //   220: ldc 121
    //   222: invokevirtual 50	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   225: aload 11
    //   227: arraylength
    //   228: invokevirtual 124	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   231: ldc 126
    //   233: invokevirtual 50	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   236: aload 4
    //   238: invokevirtual 50	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   241: invokevirtual 56	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   244: invokevirtual 93	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   247: aload_0
    //   248: aload_1
    //   249: aload 11
    //   251: iconst_0
    //   252: aload 11
    //   254: arraylength
    //   255: invokevirtual 130	com/avaje/ebean/enhance/agent/LocalClassLoader:defineClass	(Ljava/lang/String;[BII)Ljava/lang/Class;
    //   258: astore 12
    //   260: aload 7
    //   262: ifnull +8 -> 270
    //   265: aload 7
    //   267: invokevirtual 133	java/io/InputStream:close	()V
    //   270: aload 12
    //   272: areturn
    //   273: astore 13
    //   275: aload 7
    //   277: ifnull +8 -> 285
    //   280: aload 7
    //   282: invokevirtual 133	java/io/InputStream:close	()V
    //   285: aload 13
    //   287: athrow
    //   288: astore 5
    //   290: aload_0
    //   291: aload_1
    //   292: iload_2
    //   293: invokespecial 33	java/net/URLClassLoader:loadClass	(Ljava/lang/String;Z)Ljava/lang/Class;
    //   296: areturn
    //   297: astore 5
    //   299: new 19	java/lang/ClassNotFoundException
    //   302: dup
    //   303: aload_1
    //   304: aload 5
    //   306: invokespecial 136	java/lang/ClassNotFoundException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   309: athrow
    // Line number table:
    //   Java source line #23	-> byte code offset #0
    //   Java source line #25	-> byte code offset #9
    //   Java source line #27	-> byte code offset #16
    //   Java source line #28	-> byte code offset #22
    //   Java source line #29	-> byte code offset #26
    //   Java source line #31	-> byte code offset #28
    //   Java source line #36	-> byte code offset #56
    //   Java source line #37	-> byte code offset #64
    //   Java source line #38	-> byte code offset #69
    //   Java source line #41	-> byte code offset #78
    //   Java source line #42	-> byte code offset #107
    //   Java source line #44	-> byte code offset #149
    //   Java source line #46	-> byte code offset #156
    //   Java source line #47	-> byte code offset #165
    //   Java source line #49	-> byte code offset #172
    //   Java source line #50	-> byte code offset #190
    //   Java source line #52	-> byte code offset #203
    //   Java source line #53	-> byte code offset #210
    //   Java source line #54	-> byte code offset #247
    //   Java source line #56	-> byte code offset #260
    //   Java source line #57	-> byte code offset #265
    //   Java source line #56	-> byte code offset #273
    //   Java source line #57	-> byte code offset #280
    //   Java source line #60	-> byte code offset #288
    //   Java source line #61	-> byte code offset #290
    //   Java source line #62	-> byte code offset #297
    //   Java source line #63	-> byte code offset #299
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	310	0	this	LocalClassLoader
    //   0	310	1	name	String
    //   0	310	2	resolve	boolean
    //   21	6	3	c	Class<?>
    //   54	183	4	resource	String
    //   62	88	5	url	URL
    //   288	3	5	e	SecurityException
    //   297	8	5	e	java.io.IOException
    //   105	31	6	f	java.io.File
    //   154	127	7	is	java.io.InputStream
    //   163	41	8	os	java.io.ByteArrayOutputStream
    //   170	23	9	b	byte[]
    //   184	12	10	count	int
    //   208	45	11	bytes	byte[]
    //   273	13	13	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   156	260	273	finally
    //   273	275	273	finally
    //   56	270	288	java/lang/SecurityException
    //   273	288	288	java/lang/SecurityException
    //   56	270	297	java/io/IOException
    //   273	288	297	java/io/IOException
  }
}
