package org.bukkit.command.defaults;

import com.google.common.collect.ImmutableList;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.util.StringUtil;

public class TimingsCommand
  extends BukkitCommand
{
  private static final List<String> TIMINGS_SUBCOMMANDS = ImmutableList.of("report", "reset", "on", "off", "paste");
  public static long timingStart = 0L;
  
  public TimingsCommand(String name)
  {
    super(name);
    this.description = "Manages Spigot Timings data to see performance of the server.";
    this.usageMessage = "/timings <reset|report|on|off|paste>";
    setPermission("bukkit.command.timings");
  }
  
  /* Error */
  public void executeSpigotTimings(CommandSender sender, String[] args)
  {
    // Byte code:
    //   0: ldc 26
    //   2: aload_2
    //   3: iconst_0
    //   4: aaload
    //   5: invokevirtual 73	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   8: ifeq +25 -> 33
    //   11: invokestatic 79	org/bukkit/Bukkit:getPluginManager	()Lorg/bukkit/plugin/PluginManager;
    //   14: checkcast 81	org/bukkit/plugin/SimplePluginManager
    //   17: iconst_1
    //   18: invokevirtual 85	org/bukkit/plugin/SimplePluginManager:useTimings	(Z)V
    //   21: invokestatic 90	org/spigotmc/CustomTimingsHandler:reload	()V
    //   24: aload_1
    //   25: ldc 92
    //   27: invokeinterface 97 2 0
    //   32: return
    //   33: ldc 28
    //   35: aload_2
    //   36: iconst_0
    //   37: aaload
    //   38: invokevirtual 73	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   41: ifeq +22 -> 63
    //   44: invokestatic 79	org/bukkit/Bukkit:getPluginManager	()Lorg/bukkit/plugin/PluginManager;
    //   47: checkcast 81	org/bukkit/plugin/SimplePluginManager
    //   50: iconst_0
    //   51: invokevirtual 85	org/bukkit/plugin/SimplePluginManager:useTimings	(Z)V
    //   54: aload_1
    //   55: ldc 99
    //   57: invokeinterface 97 2 0
    //   62: return
    //   63: invokestatic 79	org/bukkit/Bukkit:getPluginManager	()Lorg/bukkit/plugin/PluginManager;
    //   66: invokeinterface 104 1 0
    //   71: ifne +12 -> 83
    //   74: aload_1
    //   75: ldc 106
    //   77: invokeinterface 97 2 0
    //   82: return
    //   83: ldc 30
    //   85: aload_2
    //   86: iconst_0
    //   87: aaload
    //   88: invokevirtual 73	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   91: istore_3
    //   92: ldc 24
    //   94: aload_2
    //   95: iconst_0
    //   96: aaload
    //   97: invokevirtual 73	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   100: ifeq +17 -> 117
    //   103: invokestatic 90	org/spigotmc/CustomTimingsHandler:reload	()V
    //   106: aload_1
    //   107: ldc 108
    //   109: invokeinterface 97 2 0
    //   114: goto +344 -> 458
    //   117: ldc 110
    //   119: aload_2
    //   120: iconst_0
    //   121: aaload
    //   122: invokevirtual 73	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   125: ifne +18 -> 143
    //   128: ldc 22
    //   130: aload_2
    //   131: iconst_0
    //   132: aaload
    //   133: invokevirtual 73	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   136: ifne +7 -> 143
    //   139: iload_3
    //   140: ifeq +318 -> 458
    //   143: invokestatic 116	java/lang/System:nanoTime	()J
    //   146: getstatic 40	org/bukkit/command/defaults/TimingsCommand:timingStart	J
    //   149: lsub
    //   150: lstore 4
    //   152: iconst_0
    //   153: istore 6
    //   155: new 118	java/io/File
    //   158: dup
    //   159: ldc 120
    //   161: invokespecial 121	java/io/File:<init>	(Ljava/lang/String;)V
    //   164: astore 7
    //   166: aload 7
    //   168: invokevirtual 124	java/io/File:mkdirs	()Z
    //   171: pop
    //   172: new 118	java/io/File
    //   175: dup
    //   176: aload 7
    //   178: ldc 126
    //   180: invokespecial 129	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   183: astore 8
    //   185: iload_3
    //   186: ifeq +13 -> 199
    //   189: new 131	java/io/ByteArrayOutputStream
    //   192: dup
    //   193: invokespecial 133	java/io/ByteArrayOutputStream:<init>	()V
    //   196: goto +4 -> 200
    //   199: aconst_null
    //   200: astore 9
    //   202: goto +39 -> 241
    //   205: new 118	java/io/File
    //   208: dup
    //   209: aload 7
    //   211: new 137	java/lang/StringBuilder
    //   214: dup
    //   215: ldc 120
    //   217: invokespecial 138	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   220: iinc 6 1
    //   223: iload 6
    //   225: invokevirtual 142	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   228: ldc -112
    //   230: invokevirtual 147	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   233: invokevirtual 151	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   236: invokespecial 129	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   239: astore 8
    //   241: aload 8
    //   243: invokevirtual 154	java/io/File:exists	()Z
    //   246: ifne -41 -> 205
    //   249: aconst_null
    //   250: astore 10
    //   252: iload_3
    //   253: ifeq +15 -> 268
    //   256: new 156	java/io/PrintStream
    //   259: dup
    //   260: aload 9
    //   262: invokespecial 159	java/io/PrintStream:<init>	(Ljava/io/OutputStream;)V
    //   265: goto +12 -> 277
    //   268: new 156	java/io/PrintStream
    //   271: dup
    //   272: aload 8
    //   274: invokespecial 162	java/io/PrintStream:<init>	(Ljava/io/File;)V
    //   277: astore 10
    //   279: aload 10
    //   281: invokestatic 166	org/spigotmc/CustomTimingsHandler:printTimings	(Ljava/io/PrintStream;)V
    //   284: aload 10
    //   286: new 137	java/lang/StringBuilder
    //   289: dup
    //   290: ldc -88
    //   292: invokespecial 138	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   295: lload 4
    //   297: invokevirtual 171	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   300: ldc -83
    //   302: invokevirtual 147	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   305: lload 4
    //   307: l2d
    //   308: ldc2_w 174
    //   311: ddiv
    //   312: invokevirtual 178	java/lang/StringBuilder:append	(D)Ljava/lang/StringBuilder;
    //   315: ldc -76
    //   317: invokevirtual 147	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   320: invokevirtual 151	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   323: invokevirtual 183	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   326: aload 10
    //   328: ldc -71
    //   330: invokevirtual 183	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   333: aload 10
    //   335: invokestatic 189	org/bukkit/Bukkit:spigot	()Lorg/bukkit/Server$Spigot;
    //   338: invokevirtual 193	org/bukkit/Server$Spigot:getConfig	()Lorg/bukkit/configuration/file/YamlConfiguration;
    //   341: invokevirtual 198	org/bukkit/configuration/file/YamlConfiguration:saveToString	()Ljava/lang/String;
    //   344: invokevirtual 183	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   347: aload 10
    //   349: ldc -56
    //   351: invokevirtual 183	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   354: iload_3
    //   355: ifeq +27 -> 382
    //   358: new 12	org/bukkit/command/defaults/TimingsCommand$PasteThread
    //   361: dup
    //   362: aload_1
    //   363: aload 9
    //   365: invokespecial 203	org/bukkit/command/defaults/TimingsCommand$PasteThread:<init>	(Lorg/bukkit/command/CommandSender;Ljava/io/ByteArrayOutputStream;)V
    //   368: invokevirtual 206	org/bukkit/command/defaults/TimingsCommand$PasteThread:start	()V
    //   371: aload 10
    //   373: ifnull +8 -> 381
    //   376: aload 10
    //   378: invokevirtual 209	java/io/PrintStream:close	()V
    //   381: return
    //   382: aload_1
    //   383: new 137	java/lang/StringBuilder
    //   386: dup
    //   387: ldc -45
    //   389: invokespecial 138	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   392: aload 8
    //   394: invokevirtual 214	java/io/File:getPath	()Ljava/lang/String;
    //   397: invokevirtual 147	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   400: invokevirtual 151	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   403: invokeinterface 97 2 0
    //   408: aload_1
    //   409: ldc -40
    //   411: invokeinterface 97 2 0
    //   416: goto +32 -> 448
    //   419: pop
    //   420: aload 10
    //   422: ifnull +36 -> 458
    //   425: aload 10
    //   427: invokevirtual 209	java/io/PrintStream:close	()V
    //   430: goto +28 -> 458
    //   433: astore 11
    //   435: aload 10
    //   437: ifnull +8 -> 445
    //   440: aload 10
    //   442: invokevirtual 209	java/io/PrintStream:close	()V
    //   445: aload 11
    //   447: athrow
    //   448: aload 10
    //   450: ifnull +8 -> 458
    //   453: aload 10
    //   455: invokevirtual 209	java/io/PrintStream:close	()V
    //   458: return
    // Line number table:
    //   Java source line #48	-> byte code offset #0
    //   Java source line #50	-> byte code offset #11
    //   Java source line #51	-> byte code offset #21
    //   Java source line #52	-> byte code offset #24
    //   Java source line #53	-> byte code offset #32
    //   Java source line #54	-> byte code offset #33
    //   Java source line #56	-> byte code offset #44
    //   Java source line #57	-> byte code offset #54
    //   Java source line #58	-> byte code offset #62
    //   Java source line #61	-> byte code offset #63
    //   Java source line #63	-> byte code offset #74
    //   Java source line #64	-> byte code offset #82
    //   Java source line #67	-> byte code offset #83
    //   Java source line #68	-> byte code offset #92
    //   Java source line #69	-> byte code offset #103
    //   Java source line #70	-> byte code offset #106
    //   Java source line #71	-> byte code offset #114
    //   Java source line #72	-> byte code offset #143
    //   Java source line #73	-> byte code offset #152
    //   Java source line #74	-> byte code offset #155
    //   Java source line #75	-> byte code offset #166
    //   Java source line #76	-> byte code offset #172
    //   Java source line #77	-> byte code offset #185
    //   Java source line #78	-> byte code offset #202
    //   Java source line #79	-> byte code offset #249
    //   Java source line #81	-> byte code offset #252
    //   Java source line #83	-> byte code offset #279
    //   Java source line #84	-> byte code offset #284
    //   Java source line #86	-> byte code offset #326
    //   Java source line #87	-> byte code offset #333
    //   Java source line #88	-> byte code offset #347
    //   Java source line #90	-> byte code offset #354
    //   Java source line #92	-> byte code offset #358
    //   Java source line #101	-> byte code offset #371
    //   Java source line #102	-> byte code offset #376
    //   Java source line #93	-> byte code offset #381
    //   Java source line #96	-> byte code offset #382
    //   Java source line #97	-> byte code offset #408
    //   Java source line #99	-> byte code offset #416
    //   Java source line #101	-> byte code offset #420
    //   Java source line #102	-> byte code offset #425
    //   Java source line #100	-> byte code offset #433
    //   Java source line #101	-> byte code offset #435
    //   Java source line #102	-> byte code offset #440
    //   Java source line #104	-> byte code offset #445
    //   Java source line #101	-> byte code offset #448
    //   Java source line #102	-> byte code offset #453
    //   Java source line #106	-> byte code offset #458
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	459	0	this	TimingsCommand
    //   0	459	1	sender	CommandSender
    //   0	459	2	args	String[]
    //   91	264	3	paste	boolean
    //   150	156	4	sampleTime	long
    //   153	71	6	index	int
    //   164	46	7	timingFolder	java.io.File
    //   183	210	8	timings	java.io.File
    //   200	164	9	bout	ByteArrayOutputStream
    //   250	204	10	fileTimings	java.io.PrintStream
    //   419	1	11	localIOException	IOException
    //   433	13	11	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   252	371	419	java/io/IOException
    //   382	416	419	java/io/IOException
    //   252	371	433	finally
    //   382	420	433	finally
  }
  
  public boolean execute(CommandSender sender, String currentAlias, String[] args)
  {
    if (!testPermission(sender)) {
      return true;
    }
    if (args.length < 1)
    {
      sender.sendMessage(ChatColor.RED + "Usage: " + this.usageMessage);
      return false;
    }
    executeSpigotTimings(sender, args);return true;
  }
  
  public List<String> tabComplete(CommandSender sender, String alias, String[] args)
  {
    Validate.notNull(sender, "Sender cannot be null");
    Validate.notNull(args, "Arguments cannot be null");
    Validate.notNull(alias, "Alias cannot be null");
    if (args.length == 1) {
      return (List)StringUtil.copyPartialMatches(args[0], TIMINGS_SUBCOMMANDS, new ArrayList(TIMINGS_SUBCOMMANDS.size()));
    }
    return ImmutableList.of();
  }
  
  private static class PasteThread
    extends Thread
  {
    private final CommandSender sender;
    private final ByteArrayOutputStream bout;
    
    public PasteThread(CommandSender sender, ByteArrayOutputStream bout)
    {
      super();
      this.sender = sender;
      this.bout = bout;
    }
    
    public synchronized void start()
    {
      if ((this.sender instanceof RemoteConsoleCommandSender)) {
        run();
      } else {
        super.start();
      }
    }
    
    public void run()
    {
      try
      {
        HttpURLConnection con = (HttpURLConnection)new URL("http://paste.ubuntu.com/").openConnection();
        con.setDoOutput(true);
        con.setRequestMethod("POST");
        con.setInstanceFollowRedirects(false);
        
        OutputStream out = con.getOutputStream();
        out.write("poster=Spigot&syntax=text&content=".getBytes("UTF-8"));
        out.write(URLEncoder.encode(this.bout.toString("UTF-8"), "UTF-8").getBytes("UTF-8"));
        out.close();
        con.getInputStream().close();
        
        String location = con.getHeaderField("Location");
        String pasteID = location.substring("http://paste.ubuntu.com/".length(), location.length() - 1);
        this.sender.sendMessage(ChatColor.GREEN + "Timings results can be viewed at http://www.spigotmc.org/go/timings?url=" + pasteID);
      }
      catch (IOException ex)
      {
        this.sender.sendMessage(ChatColor.RED + "Error pasting timings, check your console for more information");
        Bukkit.getServer().getLogger().log(Level.WARNING, "Could not paste timings", ex);
      }
    }
  }
}
