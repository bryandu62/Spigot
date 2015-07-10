package org.bukkit.craftbukkit.libs.jline.internal;

import java.io.Closeable;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TerminalLineSettings
{
  public static final String JLINE_STTY = "org.bukkit.craftbukkit.libs.jline.stty";
  public static final String DEFAULT_STTY = "stty";
  public static final String JLINE_SH = "org.bukkit.craftbukkit.libs.jline.sh";
  public static final String DEFAULT_SH = "sh";
  private String sttyCommand;
  private String shCommand;
  private String config;
  private String initialConfig;
  private long configLastFetched;
  
  public TerminalLineSettings()
    throws IOException, InterruptedException
  {
    this.sttyCommand = Configuration.getString("org.bukkit.craftbukkit.libs.jline.stty", "stty");
    this.shCommand = Configuration.getString("org.bukkit.craftbukkit.libs.jline.sh", "sh");
    this.initialConfig = get("-g").trim();
    this.config = get("-a");
    this.configLastFetched = System.currentTimeMillis();
    
    Log.debug(new Object[] { "Config: ", this.config });
    if (this.config.length() == 0) {
      throw new IOException(MessageFormat.format("Unrecognized stty code: {0}", new Object[] { this.config }));
    }
  }
  
  public String getConfig()
  {
    return this.config;
  }
  
  public void restore()
    throws IOException, InterruptedException
  {
    set(this.initialConfig);
  }
  
  public String get(String args)
    throws IOException, InterruptedException
  {
    return stty(args);
  }
  
  public void set(String args)
    throws IOException, InterruptedException
  {
    stty(args);
  }
  
  public int getProperty(String name)
  {
    Preconditions.checkNotNull(name);
    long currentTime = System.currentTimeMillis();
    try
    {
      if ((this.config == null) || (currentTime - this.configLastFetched > 1000L)) {
        this.config = get("-a");
      }
    }
    catch (Exception e)
    {
      if ((e instanceof InterruptedException)) {
        Thread.currentThread().interrupt();
      }
      Log.debug(new Object[] { "Failed to query stty ", name, "\n", e });
      if (this.config == null) {
        return -1;
      }
    }
    if (currentTime - this.configLastFetched > 1000L) {
      this.configLastFetched = currentTime;
    }
    return getProperty(name, this.config);
  }
  
  protected static int getProperty(String name, String stty)
  {
    Pattern pattern = Pattern.compile(name + "\\s+=\\s+(.*?)[;\\n\\r]");
    Matcher matcher = pattern.matcher(stty);
    if (!matcher.find())
    {
      pattern = Pattern.compile(name + "\\s+([^;]*)[;\\n\\r]");
      matcher = pattern.matcher(stty);
      if (!matcher.find())
      {
        pattern = Pattern.compile("(\\S*)\\s+" + name);
        matcher = pattern.matcher(stty);
        if (!matcher.find()) {
          return -1;
        }
      }
    }
    return parseControlChar(matcher.group(1));
  }
  
  private static int parseControlChar(String str)
  {
    if ("<undef>".equals(str)) {
      return -1;
    }
    if (str.charAt(0) == '0') {
      return Integer.parseInt(str, 8);
    }
    if ((str.charAt(0) >= '1') && (str.charAt(0) <= '9')) {
      return Integer.parseInt(str, 10);
    }
    if (str.charAt(0) == '^')
    {
      if (str.charAt(1) == '?') {
        return 127;
      }
      return str.charAt(1) - '@';
    }
    if ((str.charAt(0) == 'M') && (str.charAt(1) == '-'))
    {
      if (str.charAt(2) == '^')
      {
        if (str.charAt(3) == '?') {
          return 255;
        }
        return str.charAt(3) - '@' + 128;
      }
      return str.charAt(2) + '';
    }
    return str.charAt(0);
  }
  
  private String stty(String args)
    throws IOException, InterruptedException
  {
    Preconditions.checkNotNull(args);
    return exec(String.format("%s %s < /dev/tty", new Object[] { this.sttyCommand, args }));
  }
  
  private String exec(String cmd)
    throws IOException, InterruptedException
  {
    Preconditions.checkNotNull(cmd);
    return exec(new String[] { this.shCommand, "-c", cmd });
  }
  
  /* Error */
  private String exec(String... cmd)
    throws IOException, InterruptedException
  {
    // Byte code:
    //   0: aload_1
    //   1: invokestatic 111	org/bukkit/craftbukkit/libs/jline/internal/Preconditions:checkNotNull	(Ljava/lang/Object;)Ljava/lang/Object;
    //   4: pop
    //   5: new 206	java/io/ByteArrayOutputStream
    //   8: dup
    //   9: invokespecial 207	java/io/ByteArrayOutputStream:<init>	()V
    //   12: astore_2
    //   13: iconst_2
    //   14: anewarray 4	java/lang/Object
    //   17: dup
    //   18: iconst_0
    //   19: ldc -47
    //   21: aastore
    //   22: dup
    //   23: iconst_1
    //   24: aload_1
    //   25: aastore
    //   26: invokestatic 212	org/bukkit/craftbukkit/libs/jline/internal/Log:trace	([Ljava/lang/Object;)V
    //   29: invokestatic 218	java/lang/Runtime:getRuntime	()Ljava/lang/Runtime;
    //   32: aload_1
    //   33: invokevirtual 221	java/lang/Runtime:exec	([Ljava/lang/String;)Ljava/lang/Process;
    //   36: astore_3
    //   37: aconst_null
    //   38: astore 4
    //   40: aconst_null
    //   41: astore 5
    //   43: aconst_null
    //   44: astore 6
    //   46: aload_3
    //   47: invokevirtual 227	java/lang/Process:getInputStream	()Ljava/io/InputStream;
    //   50: astore 4
    //   52: aload 4
    //   54: invokevirtual 232	java/io/InputStream:read	()I
    //   57: dup
    //   58: istore 7
    //   60: iconst_m1
    //   61: if_icmpeq +12 -> 73
    //   64: aload_2
    //   65: iload 7
    //   67: invokevirtual 236	java/io/ByteArrayOutputStream:write	(I)V
    //   70: goto -18 -> 52
    //   73: aload_3
    //   74: invokevirtual 239	java/lang/Process:getErrorStream	()Ljava/io/InputStream;
    //   77: astore 5
    //   79: aload 5
    //   81: invokevirtual 232	java/io/InputStream:read	()I
    //   84: dup
    //   85: istore 7
    //   87: iconst_m1
    //   88: if_icmpeq +12 -> 100
    //   91: aload_2
    //   92: iload 7
    //   94: invokevirtual 236	java/io/ByteArrayOutputStream:write	(I)V
    //   97: goto -18 -> 79
    //   100: aload_3
    //   101: invokevirtual 243	java/lang/Process:getOutputStream	()Ljava/io/OutputStream;
    //   104: astore 6
    //   106: aload_3
    //   107: invokevirtual 246	java/lang/Process:waitFor	()I
    //   110: pop
    //   111: iconst_3
    //   112: anewarray 248	java/io/Closeable
    //   115: dup
    //   116: iconst_0
    //   117: aload 4
    //   119: aastore
    //   120: dup
    //   121: iconst_1
    //   122: aload 6
    //   124: aastore
    //   125: dup
    //   126: iconst_2
    //   127: aload 5
    //   129: aastore
    //   130: invokestatic 252	org/bukkit/craftbukkit/libs/jline/internal/TerminalLineSettings:close	([Ljava/io/Closeable;)V
    //   133: goto +30 -> 163
    //   136: astore 8
    //   138: iconst_3
    //   139: anewarray 248	java/io/Closeable
    //   142: dup
    //   143: iconst_0
    //   144: aload 4
    //   146: aastore
    //   147: dup
    //   148: iconst_1
    //   149: aload 6
    //   151: aastore
    //   152: dup
    //   153: iconst_2
    //   154: aload 5
    //   156: aastore
    //   157: invokestatic 252	org/bukkit/craftbukkit/libs/jline/internal/TerminalLineSettings:close	([Ljava/io/Closeable;)V
    //   160: aload 8
    //   162: athrow
    //   163: aload_2
    //   164: invokevirtual 253	java/io/ByteArrayOutputStream:toString	()Ljava/lang/String;
    //   167: astore 7
    //   169: iconst_2
    //   170: anewarray 4	java/lang/Object
    //   173: dup
    //   174: iconst_0
    //   175: ldc -1
    //   177: aastore
    //   178: dup
    //   179: iconst_1
    //   180: aload 7
    //   182: aastore
    //   183: invokestatic 212	org/bukkit/craftbukkit/libs/jline/internal/Log:trace	([Ljava/lang/Object;)V
    //   186: aload 7
    //   188: areturn
    // Line number table:
    //   Java source line #190	-> byte code offset #0
    //   Java source line #192	-> byte code offset #5
    //   Java source line #194	-> byte code offset #13
    //   Java source line #196	-> byte code offset #29
    //   Java source line #198	-> byte code offset #37
    //   Java source line #199	-> byte code offset #40
    //   Java source line #200	-> byte code offset #43
    //   Java source line #203	-> byte code offset #46
    //   Java source line #204	-> byte code offset #52
    //   Java source line #205	-> byte code offset #64
    //   Java source line #207	-> byte code offset #73
    //   Java source line #208	-> byte code offset #79
    //   Java source line #209	-> byte code offset #91
    //   Java source line #211	-> byte code offset #100
    //   Java source line #212	-> byte code offset #106
    //   Java source line #215	-> byte code offset #111
    //   Java source line #216	-> byte code offset #133
    //   Java source line #215	-> byte code offset #136
    //   Java source line #218	-> byte code offset #163
    //   Java source line #220	-> byte code offset #169
    //   Java source line #222	-> byte code offset #186
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	189	0	this	TerminalLineSettings
    //   0	189	1	cmd	String[]
    //   12	152	2	bout	java.io.ByteArrayOutputStream
    //   36	71	3	p	Process
    //   38	107	4	in	java.io.InputStream
    //   41	114	5	err	java.io.InputStream
    //   44	106	6	out	java.io.OutputStream
    //   58	35	7	c	int
    //   167	20	7	result	String
    //   136	25	8	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   46	111	136	finally
    //   136	138	136	finally
  }
  
  private static void close(Closeable... closeables)
  {
    for (Closeable c : closeables) {
      try
      {
        c.close();
      }
      catch (Exception e) {}
    }
  }
}
