package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.craftbukkit.v1_8_R3.CraftCrashReport;

public class CrashReport
{
  private static final Logger a = ;
  private final String b;
  private final Throwable c;
  private final CrashReportSystemDetails d = new CrashReportSystemDetails(this, "System Details");
  private final List<CrashReportSystemDetails> e = Lists.newArrayList();
  private File f;
  private boolean g = true;
  private StackTraceElement[] h = new StackTraceElement[0];
  
  public CrashReport(String s, Throwable throwable)
  {
    this.b = s;
    this.c = throwable;
    h();
  }
  
  private void h()
  {
    this.d.a("Minecraft Version", new Callable()
    {
      public String a()
      {
        return "1.8.7";
      }
      
      public Object call()
        throws Exception
      {
        return a();
      }
    });
    this.d.a("Operating System", new Callable()
    {
      public String a()
      {
        return System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ") version " + System.getProperty("os.version");
      }
      
      public Object call()
        throws Exception
      {
        return a();
      }
    });
    this.d.a("CPU", new Callable()
    {
      public String a()
      {
        return SystemUtils.a();
      }
      
      public Object call()
        throws Exception
      {
        return a();
      }
    });
    this.d.a("Java Version", new Callable()
    {
      public String a()
      {
        return System.getProperty("java.version") + ", " + System.getProperty("java.vendor");
      }
      
      public Object call()
        throws Exception
      {
        return a();
      }
    });
    this.d.a("Java VM Version", new Callable()
    {
      public String a()
      {
        return System.getProperty("java.vm.name") + " (" + System.getProperty("java.vm.info") + "), " + System.getProperty("java.vm.vendor");
      }
      
      public Object call()
        throws Exception
      {
        return a();
      }
    });
    this.d.a("Memory", new Callable()
    {
      public String a()
      {
        Runtime runtime = Runtime.getRuntime();
        long i = runtime.maxMemory();
        long j = runtime.totalMemory();
        long k = runtime.freeMemory();
        long l = i / 1024L / 1024L;
        long i1 = j / 1024L / 1024L;
        long j1 = k / 1024L / 1024L;
        
        return k + " bytes (" + j1 + " MB) / " + j + " bytes (" + i1 + " MB) up to " + i + " bytes (" + l + " MB)";
      }
      
      public Object call()
        throws Exception
      {
        return a();
      }
    });
    this.d.a("JVM Flags", new Callable()
    {
      public String a()
      {
        RuntimeMXBean runtimemxbean = ManagementFactory.getRuntimeMXBean();
        List list = runtimemxbean.getInputArguments();
        int i = 0;
        StringBuilder stringbuilder = new StringBuilder();
        Iterator iterator = list.iterator();
        while (iterator.hasNext())
        {
          String s = (String)iterator.next();
          if (s.startsWith("-X"))
          {
            if (i++ > 0) {
              stringbuilder.append(" ");
            }
            stringbuilder.append(s);
          }
        }
        return String.format("%d total; %s", new Object[] { Integer.valueOf(i), stringbuilder.toString() });
      }
      
      public Object call()
        throws Exception
      {
        return a();
      }
    });
    this.d.a("IntCache", new Callable()
    {
      public String a()
        throws Exception
      {
        return IntCache.b();
      }
      
      public Object call()
        throws Exception
      {
        return a();
      }
    });
    this.d.a("CraftBukkit Information", new CraftCrashReport());
  }
  
  public String a()
  {
    return this.b;
  }
  
  public Throwable b()
  {
    return this.c;
  }
  
  public void a(StringBuilder stringbuilder)
  {
    if (((this.h == null) || (this.h.length <= 0)) && (this.e.size() > 0)) {
      this.h = ((StackTraceElement[])ArrayUtils.subarray(((CrashReportSystemDetails)this.e.get(0)).a(), 0, 1));
    }
    if ((this.h != null) && (this.h.length > 0))
    {
      stringbuilder.append("-- Head --\n");
      stringbuilder.append("Stacktrace:\n");
      StackTraceElement[] astacktraceelement = this.h;
      int i = astacktraceelement.length;
      for (int j = 0; j < i; j++)
      {
        StackTraceElement stacktraceelement = astacktraceelement[j];
        
        stringbuilder.append("\t").append("at ").append(stacktraceelement.toString());
        stringbuilder.append("\n");
      }
      stringbuilder.append("\n");
    }
    Iterator iterator = this.e.iterator();
    while (iterator.hasNext())
    {
      CrashReportSystemDetails crashreportsystemdetails = (CrashReportSystemDetails)iterator.next();
      
      crashreportsystemdetails.a(stringbuilder);
      stringbuilder.append("\n\n");
    }
    this.d.a(stringbuilder);
  }
  
  /* Error */
  public String d()
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_1
    //   2: aconst_null
    //   3: astore_2
    //   4: aload_0
    //   5: getfield 79	net/minecraft/server/v1_8_R3/CrashReport:c	Ljava/lang/Throwable;
    //   8: astore_3
    //   9: aload_3
    //   10: checkcast 191	java/lang/Throwable
    //   13: invokevirtual 194	java/lang/Throwable:getMessage	()Ljava/lang/String;
    //   16: ifnonnull +80 -> 96
    //   19: aload_3
    //   20: instanceof 196
    //   23: ifeq +18 -> 41
    //   26: new 196	java/lang/NullPointerException
    //   29: dup
    //   30: aload_0
    //   31: getfield 77	net/minecraft/server/v1_8_R3/CrashReport:b	Ljava/lang/String;
    //   34: invokespecial 199	java/lang/NullPointerException:<init>	(Ljava/lang/String;)V
    //   37: astore_3
    //   38: goto +44 -> 82
    //   41: aload_3
    //   42: instanceof 205
    //   45: ifeq +18 -> 63
    //   48: new 205	java/lang/StackOverflowError
    //   51: dup
    //   52: aload_0
    //   53: getfield 77	net/minecraft/server/v1_8_R3/CrashReport:b	Ljava/lang/String;
    //   56: invokespecial 206	java/lang/StackOverflowError:<init>	(Ljava/lang/String;)V
    //   59: astore_3
    //   60: goto +22 -> 82
    //   63: aload_3
    //   64: instanceof 208
    //   67: ifeq +15 -> 82
    //   70: new 208	java/lang/OutOfMemoryError
    //   73: dup
    //   74: aload_0
    //   75: getfield 77	net/minecraft/server/v1_8_R3/CrashReport:b	Ljava/lang/String;
    //   78: invokespecial 209	java/lang/OutOfMemoryError:<init>	(Ljava/lang/String;)V
    //   81: astore_3
    //   82: aload_3
    //   83: checkcast 191	java/lang/Throwable
    //   86: aload_0
    //   87: getfield 79	net/minecraft/server/v1_8_R3/CrashReport:c	Ljava/lang/Throwable;
    //   90: invokevirtual 212	java/lang/Throwable:getStackTrace	()[Ljava/lang/StackTraceElement;
    //   93: invokevirtual 216	java/lang/Throwable:setStackTrace	([Ljava/lang/StackTraceElement;)V
    //   96: aload_3
    //   97: checkcast 191	java/lang/Throwable
    //   100: invokevirtual 217	java/lang/Throwable:toString	()Ljava/lang/String;
    //   103: astore 4
    //   105: new 201	java/io/StringWriter
    //   108: dup
    //   109: invokespecial 218	java/io/StringWriter:<init>	()V
    //   112: astore_1
    //   113: new 203	java/io/PrintWriter
    //   116: dup
    //   117: aload_1
    //   118: invokespecial 221	java/io/PrintWriter:<init>	(Ljava/io/Writer;)V
    //   121: astore_2
    //   122: aload_3
    //   123: checkcast 191	java/lang/Throwable
    //   126: aload_2
    //   127: invokevirtual 225	java/lang/Throwable:printStackTrace	(Ljava/io/PrintWriter;)V
    //   130: aload_1
    //   131: invokevirtual 226	java/io/StringWriter:toString	()Ljava/lang/String;
    //   134: astore 4
    //   136: goto +16 -> 152
    //   139: astore 5
    //   141: aload_1
    //   142: invokestatic 233	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Writer;)V
    //   145: aload_2
    //   146: invokestatic 233	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Writer;)V
    //   149: aload 5
    //   151: athrow
    //   152: aload_1
    //   153: invokestatic 233	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Writer;)V
    //   156: aload_2
    //   157: invokestatic 233	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Writer;)V
    //   160: aload 4
    //   162: areturn
    // Line number table:
    //   Java source line #181	-> byte code offset #0
    //   Java source line #182	-> byte code offset #2
    //   Java source line #183	-> byte code offset #4
    //   Java source line #185	-> byte code offset #9
    //   Java source line #186	-> byte code offset #19
    //   Java source line #187	-> byte code offset #26
    //   Java source line #188	-> byte code offset #38
    //   Java source line #189	-> byte code offset #48
    //   Java source line #190	-> byte code offset #60
    //   Java source line #191	-> byte code offset #70
    //   Java source line #194	-> byte code offset #82
    //   Java source line #197	-> byte code offset #96
    //   Java source line #200	-> byte code offset #105
    //   Java source line #201	-> byte code offset #113
    //   Java source line #202	-> byte code offset #122
    //   Java source line #203	-> byte code offset #130
    //   Java source line #204	-> byte code offset #136
    //   Java source line #205	-> byte code offset #141
    //   Java source line #206	-> byte code offset #145
    //   Java source line #207	-> byte code offset #149
    //   Java source line #205	-> byte code offset #152
    //   Java source line #206	-> byte code offset #156
    //   Java source line #209	-> byte code offset #160
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	163	0	this	CrashReport
    //   1	152	1	stringwriter	java.io.StringWriter
    //   3	154	2	printwriter	java.io.PrintWriter
    //   8	115	3	object	Object
    //   103	58	4	s	String
    //   139	11	5	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   105	139	139	finally
  }
  
  public String e()
  {
    StringBuilder stringbuilder = new StringBuilder();
    
    stringbuilder.append("---- Minecraft Crash Report ----\n");
    stringbuilder.append("// ");
    stringbuilder.append(i());
    stringbuilder.append("\n\n");
    stringbuilder.append("Time: ");
    stringbuilder.append(new SimpleDateFormat().format(new Date()));
    stringbuilder.append("\n");
    stringbuilder.append("Description: ");
    stringbuilder.append(this.b);
    stringbuilder.append("\n\n");
    stringbuilder.append(d());
    stringbuilder.append("\n\nA detailed walkthrough of the error, its code path and all known details is as follows:\n");
    for (int i = 0; i < 87; i++) {
      stringbuilder.append("-");
    }
    stringbuilder.append("\n\n");
    a(stringbuilder);
    return stringbuilder.toString();
  }
  
  public boolean a(File file)
  {
    if (this.f != null) {
      return false;
    }
    if (file.getParentFile() != null) {
      file.getParentFile().mkdirs();
    }
    try
    {
      FileWriter filewriter = new FileWriter(file);
      
      filewriter.write(e());
      filewriter.close();
      this.f = file;
      return true;
    }
    catch (Throwable throwable)
    {
      a.error("Could not save crash report to " + file, throwable);
    }
    return false;
  }
  
  public CrashReportSystemDetails g()
  {
    return this.d;
  }
  
  public CrashReportSystemDetails a(String s)
  {
    return a(s, 1);
  }
  
  public CrashReportSystemDetails a(String s, int i)
  {
    CrashReportSystemDetails crashreportsystemdetails = new CrashReportSystemDetails(this, s);
    if (this.g)
    {
      int j = crashreportsystemdetails.a(i);
      StackTraceElement[] astacktraceelement = this.c.getStackTrace();
      StackTraceElement stacktraceelement = null;
      StackTraceElement stacktraceelement1 = null;
      int k = astacktraceelement.length - j;
      if (k < 0) {
        System.out.println("Negative index in crash report handler (" + astacktraceelement.length + "/" + j + ")");
      }
      if ((astacktraceelement != null) && (k >= 0) && (k < astacktraceelement.length))
      {
        stacktraceelement = astacktraceelement[k];
        if (astacktraceelement.length + 1 - j < astacktraceelement.length) {
          stacktraceelement1 = astacktraceelement[(astacktraceelement.length + 1 - j)];
        }
      }
      this.g = crashreportsystemdetails.a(stacktraceelement, stacktraceelement1);
      if ((j > 0) && (!this.e.isEmpty()))
      {
        CrashReportSystemDetails crashreportsystemdetails1 = (CrashReportSystemDetails)this.e.get(this.e.size() - 1);
        
        crashreportsystemdetails1.b(j);
      }
      else if ((astacktraceelement != null) && (astacktraceelement.length >= j) && (k >= 0) && (k < astacktraceelement.length))
      {
        this.h = new StackTraceElement[k];
        System.arraycopy(astacktraceelement, 0, this.h, 0, this.h.length);
      }
      else
      {
        this.g = false;
      }
    }
    this.e.add(crashreportsystemdetails);
    return crashreportsystemdetails;
  }
  
  private static String i()
  {
    String[] astring = { "Who set us up the TNT?", "Everything's going to plan. No, really, that was supposed to happen.", "Uh... Did I do that?", "Oops.", "Why did you do that?", "I feel sad now :(", "My bad.", "I'm sorry, Dave.", "I let you down. Sorry :(", "On the bright side, I bought you a teddy bear!", "Daisy, daisy...", "Oh - I know what I did wrong!", "Hey, that tickles! Hehehe!", "I blame Dinnerbone.", "You should try our sister game, Minceraft!", "Don't be sad. I'll do better next time, I promise!", "Don't be sad, have a hug! <3", "I just don't know what went wrong :(", "Shall we play a game?", "Quite honestly, I wouldn't worry myself about that.", "I bet Cylons wouldn't have this problem.", "Sorry :(", "Surprise! Haha. Well, this is awkward.", "Would you like a cupcake?", "Hi. I'm Minecraft, and I'm a crashaholic.", "Ooh. Shiny.", "This doesn't make any sense!", "Why is it breaking :(", "Don't do that.", "Ouch. That hurt :(", "You're mean.", "This is a token for 1 free hug. Redeem at your nearest Mojangsta: [~~HUG~~]", "There are four lights!", "But it works on my machine." };
    try
    {
      return astring[((int)(System.nanoTime() % astring.length))];
    }
    catch (Throwable localThrowable) {}
    return "Witty comment unavailable :(";
  }
  
  public static CrashReport a(Throwable throwable, String s)
  {
    CrashReport crashreport;
    CrashReport crashreport;
    if ((throwable instanceof ReportedException)) {
      crashreport = ((ReportedException)throwable).a();
    } else {
      crashreport = new CrashReport(s, throwable);
    }
    return crashreport;
  }
}
