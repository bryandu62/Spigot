package org.bukkit.configuration.file;

import com.google.common.base.Charsets;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemoryConfiguration;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public abstract class FileConfiguration
  extends MemoryConfiguration
{
  @Deprecated
  public static final boolean UTF8_OVERRIDE;
  @Deprecated
  public static final boolean UTF_BIG;
  @Deprecated
  public static final boolean SYSTEM_UTF;
  
  static
  {
    byte[] testBytes = Base64Coder.decode("ICEiIyQlJicoKSorLC0uLzAxMjM0NTY3ODk6Ozw9Pj9AQUJDREVGR0hJSktMTU5PUFFSU1RVVldYWVpbXF1eX2BhYmNkZWZnaGlqa2xtbm9wcXJzdHV2d3h5ent8fX4NCg==");
    
    Charset defaultCharset = Charset.defaultCharset();
    String resultString = new String(testBytes, defaultCharset);
    boolean trueUTF = defaultCharset.name().contains("UTF");
    UTF8_OVERRIDE = (!" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\r\n".equals(resultString)) || (defaultCharset.equals(Charset.forName("US-ASCII")));
    SYSTEM_UTF = (trueUTF) || (UTF8_OVERRIDE);
    UTF_BIG = (trueUTF) && (UTF8_OVERRIDE);
  }
  
  public FileConfiguration() {}
  
  public FileConfiguration(Configuration defaults)
  {
    super(defaults);
  }
  
  /* Error */
  public void save(File file)
    throws IOException
  {
    // Byte code:
    //   0: aload_1
    //   1: ldc 83
    //   3: invokestatic 89	org/apache/commons/lang/Validate:notNull	(Ljava/lang/Object;Ljava/lang/String;)V
    //   6: aload_1
    //   7: invokestatic 94	com/google/common/io/Files:createParentDirs	(Ljava/io/File;)V
    //   10: aload_0
    //   11: invokevirtual 97	org/bukkit/configuration/file/FileConfiguration:saveToString	()Ljava/lang/String;
    //   14: astore_2
    //   15: new 99	java/io/OutputStreamWriter
    //   18: dup
    //   19: new 101	java/io/FileOutputStream
    //   22: dup
    //   23: aload_1
    //   24: invokespecial 103	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
    //   27: getstatic 59	org/bukkit/configuration/file/FileConfiguration:UTF8_OVERRIDE	Z
    //   30: ifeq +15 -> 45
    //   33: getstatic 63	org/bukkit/configuration/file/FileConfiguration:UTF_BIG	Z
    //   36: ifne +9 -> 45
    //   39: getstatic 108	com/google/common/base/Charsets:UTF_8	Ljava/nio/charset/Charset;
    //   42: goto +6 -> 48
    //   45: invokestatic 26	java/nio/charset/Charset:defaultCharset	()Ljava/nio/charset/Charset;
    //   48: invokespecial 113	java/io/OutputStreamWriter:<init>	(Ljava/io/OutputStream;Ljava/nio/charset/Charset;)V
    //   51: astore_3
    //   52: aload_3
    //   53: aload_2
    //   54: invokevirtual 119	java/io/Writer:write	(Ljava/lang/String;)V
    //   57: goto +12 -> 69
    //   60: astore 4
    //   62: aload_3
    //   63: invokevirtual 124	java/io/Writer:close	()V
    //   66: aload 4
    //   68: athrow
    //   69: aload_3
    //   70: invokevirtual 124	java/io/Writer:close	()V
    //   73: return
    // Line number table:
    //   Java source line #99	-> byte code offset #0
    //   Java source line #101	-> byte code offset #6
    //   Java source line #103	-> byte code offset #10
    //   Java source line #105	-> byte code offset #15
    //   Java source line #108	-> byte code offset #52
    //   Java source line #109	-> byte code offset #57
    //   Java source line #110	-> byte code offset #62
    //   Java source line #111	-> byte code offset #66
    //   Java source line #110	-> byte code offset #69
    //   Java source line #112	-> byte code offset #73
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	74	0	this	FileConfiguration
    //   0	74	1	file	File
    //   14	40	2	data	String
    //   51	19	3	writer	java.io.Writer
    //   60	7	4	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   52	60	60	finally
  }
  
  public void save(String file)
    throws IOException
  {
    Validate.notNull(file, "File cannot be null");
    
    save(new File(file));
  }
  
  public abstract String saveToString();
  
  public void load(File file)
    throws FileNotFoundException, IOException, InvalidConfigurationException
  {
    Validate.notNull(file, "File cannot be null");
    
    FileInputStream stream = new FileInputStream(file);
    
    load(new InputStreamReader(stream, (UTF8_OVERRIDE) && (!UTF_BIG) ? Charsets.UTF_8 : Charset.defaultCharset()));
  }
  
  @Deprecated
  public void load(InputStream stream)
    throws IOException, InvalidConfigurationException
  {
    Validate.notNull(stream, "Stream cannot be null");
    
    load(new InputStreamReader(stream, UTF8_OVERRIDE ? Charsets.UTF_8 : Charset.defaultCharset()));
  }
  
  /* Error */
  public void load(java.io.Reader reader)
    throws IOException, InvalidConfigurationException
  {
    // Byte code:
    //   0: aload_1
    //   1: instanceof 159
    //   4: ifeq +10 -> 14
    //   7: aload_1
    //   8: checkcast 159	java/io/BufferedReader
    //   11: goto +11 -> 22
    //   14: new 159	java/io/BufferedReader
    //   17: dup
    //   18: aload_1
    //   19: invokespecial 161	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
    //   22: astore_2
    //   23: new 163	java/lang/StringBuilder
    //   26: dup
    //   27: invokespecial 164	java/lang/StringBuilder:<init>	()V
    //   30: astore_3
    //   31: goto +17 -> 48
    //   34: aload_3
    //   35: aload 4
    //   37: invokevirtual 168	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   40: pop
    //   41: aload_3
    //   42: bipush 10
    //   44: invokevirtual 171	java/lang/StringBuilder:append	(C)Ljava/lang/StringBuilder;
    //   47: pop
    //   48: aload_2
    //   49: invokevirtual 174	java/io/BufferedReader:readLine	()Ljava/lang/String;
    //   52: dup
    //   53: astore 4
    //   55: ifnonnull -21 -> 34
    //   58: goto +12 -> 70
    //   61: astore 5
    //   63: aload_2
    //   64: invokevirtual 175	java/io/BufferedReader:close	()V
    //   67: aload 5
    //   69: athrow
    //   70: aload_2
    //   71: invokevirtual 175	java/io/BufferedReader:close	()V
    //   74: aload_0
    //   75: aload_3
    //   76: invokevirtual 178	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   79: invokevirtual 181	org/bukkit/configuration/file/FileConfiguration:loadFromString	(Ljava/lang/String;)V
    //   82: return
    // Line number table:
    //   Java source line #211	-> byte code offset #0
    //   Java source line #213	-> byte code offset #23
    //   Java source line #218	-> byte code offset #31
    //   Java source line #219	-> byte code offset #34
    //   Java source line #220	-> byte code offset #41
    //   Java source line #218	-> byte code offset #48
    //   Java source line #222	-> byte code offset #58
    //   Java source line #223	-> byte code offset #63
    //   Java source line #224	-> byte code offset #67
    //   Java source line #223	-> byte code offset #70
    //   Java source line #226	-> byte code offset #74
    //   Java source line #227	-> byte code offset #82
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	83	0	this	FileConfiguration
    //   0	83	1	reader	java.io.Reader
    //   22	49	2	input	java.io.BufferedReader
    //   30	46	3	builder	StringBuilder
    //   34	2	4	line	String
    //   53	3	4	line	String
    //   61	7	5	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   31	61	61	finally
  }
  
  public void load(String file)
    throws FileNotFoundException, IOException, InvalidConfigurationException
  {
    Validate.notNull(file, "File cannot be null");
    
    load(new File(file));
  }
  
  public abstract void loadFromString(String paramString)
    throws InvalidConfigurationException;
  
  protected abstract String buildHeader();
  
  public FileConfigurationOptions options()
  {
    if (this.options == null) {
      this.options = new FileConfigurationOptions(this);
    }
    return (FileConfigurationOptions)this.options;
  }
}
