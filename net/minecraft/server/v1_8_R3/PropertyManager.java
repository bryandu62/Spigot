package net.minecraft.server.v1_8_R3;

import java.io.File;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.craftbukkit.libs.joptsimple.OptionSet;

public class PropertyManager
{
  private static final Logger a = ;
  public final Properties properties;
  private final File file;
  private OptionSet options;
  
  /* Error */
  public PropertyManager(File file)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial 31	java/lang/Object:<init>	()V
    //   4: aload_0
    //   5: new 33	java/util/Properties
    //   8: dup
    //   9: invokespecial 34	java/util/Properties:<init>	()V
    //   12: putfield 36	net/minecraft/server/v1_8_R3/PropertyManager:properties	Ljava/util/Properties;
    //   15: aload_0
    //   16: aconst_null
    //   17: putfield 38	net/minecraft/server/v1_8_R3/PropertyManager:options	Lorg/bukkit/craftbukkit/libs/joptsimple/OptionSet;
    //   20: aload_0
    //   21: aload_1
    //   22: putfield 40	net/minecraft/server/v1_8_R3/PropertyManager:file	Ljava/io/File;
    //   25: aload_1
    //   26: invokevirtual 46	java/io/File:exists	()Z
    //   29: ifeq +102 -> 131
    //   32: aconst_null
    //   33: astore_2
    //   34: new 48	java/io/FileInputStream
    //   37: dup
    //   38: aload_1
    //   39: invokespecial 50	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   42: astore_2
    //   43: aload_0
    //   44: getfield 36	net/minecraft/server/v1_8_R3/PropertyManager:properties	Ljava/util/Properties;
    //   47: aload_2
    //   48: invokevirtual 54	java/util/Properties:load	(Ljava/io/InputStream;)V
    //   51: goto +65 -> 116
    //   54: astore_3
    //   55: getstatic 23	net/minecraft/server/v1_8_R3/PropertyManager:a	Lorg/apache/logging/log4j/Logger;
    //   58: new 56	java/lang/StringBuilder
    //   61: dup
    //   62: ldc 58
    //   64: invokespecial 61	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   67: aload_1
    //   68: invokevirtual 65	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   71: invokevirtual 69	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   74: aload_3
    //   75: invokeinterface 75 3 0
    //   80: aload_0
    //   81: invokevirtual 77	net/minecraft/server/v1_8_R3/PropertyManager:a	()V
    //   84: aload_2
    //   85: ifnull +77 -> 162
    //   88: aload_2
    //   89: invokevirtual 80	java/io/FileInputStream:close	()V
    //   92: goto +70 -> 162
    //   95: pop
    //   96: goto +66 -> 162
    //   99: astore 4
    //   101: aload_2
    //   102: ifnull +11 -> 113
    //   105: aload_2
    //   106: invokevirtual 80	java/io/FileInputStream:close	()V
    //   109: goto +4 -> 113
    //   112: pop
    //   113: aload 4
    //   115: athrow
    //   116: aload_2
    //   117: ifnull +45 -> 162
    //   120: aload_2
    //   121: invokevirtual 80	java/io/FileInputStream:close	()V
    //   124: goto +38 -> 162
    //   127: pop
    //   128: goto +34 -> 162
    //   131: getstatic 23	net/minecraft/server/v1_8_R3/PropertyManager:a	Lorg/apache/logging/log4j/Logger;
    //   134: new 56	java/lang/StringBuilder
    //   137: dup
    //   138: invokespecial 83	java/lang/StringBuilder:<init>	()V
    //   141: aload_1
    //   142: invokevirtual 65	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   145: ldc 85
    //   147: invokevirtual 88	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   150: invokevirtual 69	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   153: invokeinterface 90 2 0
    //   158: aload_0
    //   159: invokevirtual 77	net/minecraft/server/v1_8_R3/PropertyManager:a	()V
    //   162: return
    // Line number table:
    //   Java source line #19	-> byte code offset #0
    //   Java source line #16	-> byte code offset #4
    //   Java source line #48	-> byte code offset #15
    //   Java source line #20	-> byte code offset #20
    //   Java source line #21	-> byte code offset #25
    //   Java source line #22	-> byte code offset #32
    //   Java source line #25	-> byte code offset #34
    //   Java source line #26	-> byte code offset #43
    //   Java source line #27	-> byte code offset #51
    //   Java source line #28	-> byte code offset #55
    //   Java source line #29	-> byte code offset #80
    //   Java source line #31	-> byte code offset #84
    //   Java source line #33	-> byte code offset #88
    //   Java source line #34	-> byte code offset #92
    //   Java source line #30	-> byte code offset #99
    //   Java source line #31	-> byte code offset #101
    //   Java source line #33	-> byte code offset #105
    //   Java source line #34	-> byte code offset #109
    //   Java source line #39	-> byte code offset #113
    //   Java source line #31	-> byte code offset #116
    //   Java source line #33	-> byte code offset #120
    //   Java source line #34	-> byte code offset #124
    //   Java source line #40	-> byte code offset #128
    //   Java source line #41	-> byte code offset #131
    //   Java source line #42	-> byte code offset #158
    //   Java source line #45	-> byte code offset #162
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	163	0	this	PropertyManager
    //   0	163	1	file	File
    //   33	88	2	fileinputstream	java.io.FileInputStream
    //   54	21	3	exception	Exception
    //   99	15	4	localObject	Object
    //   95	1	5	localIOException1	java.io.IOException
    //   112	1	6	localIOException2	java.io.IOException
    //   127	1	7	localIOException3	java.io.IOException
    // Exception table:
    //   from	to	target	type
    //   34	51	54	java/lang/Exception
    //   88	92	95	java/io/IOException
    //   34	84	99	finally
    //   105	109	112	java/io/IOException
    //   120	124	127	java/io/IOException
  }
  
  public PropertyManager(OptionSet options)
  {
    this((File)options.valueOf("config"));
    
    this.options = options;
  }
  
  private <T> T getOverride(String name, T value)
  {
    if ((this.options != null) && (this.options.has(name)) && (!name.equals("online-mode"))) {
      return (T)this.options.valueOf(name);
    }
    return value;
  }
  
  public void a()
  {
    a.info("Generating new properties file");
    savePropertiesFile();
  }
  
  /* Error */
  public void savePropertiesFile()
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_1
    //   2: aload_0
    //   3: getfield 40	net/minecraft/server/v1_8_R3/PropertyManager:file	Ljava/io/File;
    //   6: invokevirtual 46	java/io/File:exists	()Z
    //   9: ifeq +26 -> 35
    //   12: aload_0
    //   13: getfield 40	net/minecraft/server/v1_8_R3/PropertyManager:file	Ljava/io/File;
    //   16: invokevirtual 136	java/io/File:canWrite	()Z
    //   19: ifne +16 -> 35
    //   22: aload_1
    //   23: ifnull +11 -> 34
    //   26: aload_1
    //   27: invokevirtual 139	java/io/FileOutputStream:close	()V
    //   30: goto +4 -> 34
    //   33: pop
    //   34: return
    //   35: new 138	java/io/FileOutputStream
    //   38: dup
    //   39: aload_0
    //   40: getfield 40	net/minecraft/server/v1_8_R3/PropertyManager:file	Ljava/io/File;
    //   43: invokespecial 140	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
    //   46: astore_1
    //   47: aload_0
    //   48: getfield 36	net/minecraft/server/v1_8_R3/PropertyManager:properties	Ljava/util/Properties;
    //   51: aload_1
    //   52: ldc -114
    //   54: invokevirtual 146	java/util/Properties:store	(Ljava/io/OutputStream;Ljava/lang/String;)V
    //   57: goto +66 -> 123
    //   60: astore_2
    //   61: getstatic 23	net/minecraft/server/v1_8_R3/PropertyManager:a	Lorg/apache/logging/log4j/Logger;
    //   64: new 56	java/lang/StringBuilder
    //   67: dup
    //   68: ldc -108
    //   70: invokespecial 61	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   73: aload_0
    //   74: getfield 40	net/minecraft/server/v1_8_R3/PropertyManager:file	Ljava/io/File;
    //   77: invokevirtual 65	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   80: invokevirtual 69	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   83: aload_2
    //   84: invokeinterface 75 3 0
    //   89: aload_0
    //   90: invokevirtual 77	net/minecraft/server/v1_8_R3/PropertyManager:a	()V
    //   93: aload_1
    //   94: ifnull +41 -> 135
    //   97: aload_1
    //   98: invokevirtual 139	java/io/FileOutputStream:close	()V
    //   101: goto +34 -> 135
    //   104: pop
    //   105: goto +30 -> 135
    //   108: astore_3
    //   109: aload_1
    //   110: ifnull +11 -> 121
    //   113: aload_1
    //   114: invokevirtual 139	java/io/FileOutputStream:close	()V
    //   117: goto +4 -> 121
    //   120: pop
    //   121: aload_3
    //   122: athrow
    //   123: aload_1
    //   124: ifnull +11 -> 135
    //   127: aload_1
    //   128: invokevirtual 139	java/io/FileOutputStream:close	()V
    //   131: goto +4 -> 135
    //   134: pop
    //   135: return
    // Line number table:
    //   Java source line #71	-> byte code offset #0
    //   Java source line #75	-> byte code offset #2
    //   Java source line #86	-> byte code offset #22
    //   Java source line #88	-> byte code offset #26
    //   Java source line #89	-> byte code offset #30
    //   Java source line #76	-> byte code offset #34
    //   Java source line #80	-> byte code offset #35
    //   Java source line #81	-> byte code offset #47
    //   Java source line #82	-> byte code offset #57
    //   Java source line #83	-> byte code offset #61
    //   Java source line #84	-> byte code offset #89
    //   Java source line #86	-> byte code offset #93
    //   Java source line #88	-> byte code offset #97
    //   Java source line #89	-> byte code offset #101
    //   Java source line #85	-> byte code offset #108
    //   Java source line #86	-> byte code offset #109
    //   Java source line #88	-> byte code offset #113
    //   Java source line #89	-> byte code offset #117
    //   Java source line #94	-> byte code offset #121
    //   Java source line #86	-> byte code offset #123
    //   Java source line #88	-> byte code offset #127
    //   Java source line #89	-> byte code offset #131
    //   Java source line #96	-> byte code offset #135
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	136	0	this	PropertyManager
    //   1	127	1	fileoutputstream	java.io.FileOutputStream
    //   60	24	2	exception	Exception
    //   108	14	3	localObject	Object
    //   33	1	4	localIOException1	java.io.IOException
    //   104	1	5	localIOException2	java.io.IOException
    //   120	1	6	localIOException3	java.io.IOException
    //   134	1	7	localIOException4	java.io.IOException
    // Exception table:
    //   from	to	target	type
    //   26	30	33	java/io/IOException
    //   2	22	60	java/lang/Exception
    //   35	57	60	java/lang/Exception
    //   97	101	104	java/io/IOException
    //   2	22	108	finally
    //   35	93	108	finally
    //   113	117	120	java/io/IOException
    //   127	131	134	java/io/IOException
  }
  
  public File c()
  {
    return this.file;
  }
  
  public String getString(String s, String s1)
  {
    if (!this.properties.containsKey(s))
    {
      this.properties.setProperty(s, s1);
      savePropertiesFile();
      savePropertiesFile();
    }
    return (String)getOverride(s, this.properties.getProperty(s, s1));
  }
  
  public int getInt(String s, int i)
  {
    try
    {
      return ((Integer)getOverride(s, Integer.valueOf(Integer.parseInt(getString(s, i))))).intValue();
    }
    catch (Exception localException)
    {
      this.properties.setProperty(s, i);
      savePropertiesFile();
    }
    return ((Integer)getOverride(s, Integer.valueOf(i))).intValue();
  }
  
  public long getLong(String s, long i)
  {
    try
    {
      return ((Long)getOverride(s, Long.valueOf(Long.parseLong(getString(s, i))))).longValue();
    }
    catch (Exception localException)
    {
      this.properties.setProperty(s, i);
      savePropertiesFile();
    }
    return ((Long)getOverride(s, Long.valueOf(i))).longValue();
  }
  
  public boolean getBoolean(String s, boolean flag)
  {
    try
    {
      return ((Boolean)getOverride(s, Boolean.valueOf(Boolean.parseBoolean(getString(s, flag))))).booleanValue();
    }
    catch (Exception localException)
    {
      this.properties.setProperty(s, flag);
      savePropertiesFile();
    }
    return ((Boolean)getOverride(s, Boolean.valueOf(flag))).booleanValue();
  }
  
  public void setProperty(String s, Object object)
  {
    this.properties.setProperty(s, object);
  }
}
