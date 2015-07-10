package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JsonList<K, V extends JsonListEntry<K>>
{
  protected static final Logger a = ;
  protected final Gson b;
  private final File c;
  private final Map<String, V> d = Maps.newHashMap();
  private boolean e = true;
  private static final ParameterizedType f = new ParameterizedType()
  {
    public Type[] getActualTypeArguments()
    {
      return new Type[] { JsonListEntry.class };
    }
    
    public Type getRawType()
    {
      return List.class;
    }
    
    public Type getOwnerType()
    {
      return null;
    }
  };
  
  public JsonList(File file)
  {
    this.c = file;
    GsonBuilder gsonbuilder = new GsonBuilder().setPrettyPrinting();
    
    gsonbuilder.registerTypeHierarchyAdapter(JsonListEntry.class, new JsonListEntrySerializer(null));
    this.b = gsonbuilder.create();
  }
  
  public boolean isEnabled()
  {
    return this.e;
  }
  
  public void a(boolean flag)
  {
    this.e = flag;
  }
  
  public File c()
  {
    return this.c;
  }
  
  public void add(V v0)
  {
    this.d.put(a(v0.getKey()), v0);
    try
    {
      save();
    }
    catch (IOException ioexception)
    {
      a.warn("Could not save the list after adding a user.", ioexception);
    }
  }
  
  public V get(K k0)
  {
    h();
    return (JsonListEntry)this.d.get(a(k0));
  }
  
  public void remove(K k0)
  {
    this.d.remove(a(k0));
    try
    {
      save();
    }
    catch (IOException ioexception)
    {
      a.warn("Could not save the list after removing a user.", ioexception);
    }
  }
  
  public String[] getEntries()
  {
    return (String[])this.d.keySet().toArray(new String[this.d.size()]);
  }
  
  public Collection<V> getValues()
  {
    return this.d.values();
  }
  
  public boolean isEmpty()
  {
    return this.d.size() < 1;
  }
  
  protected String a(K k0)
  {
    return k0.toString();
  }
  
  protected boolean d(K k0)
  {
    return this.d.containsKey(a(k0));
  }
  
  private void h()
  {
    ArrayList arraylist = Lists.newArrayList();
    Iterator iterator = this.d.values().iterator();
    while (iterator.hasNext())
    {
      JsonListEntry jsonlistentry = (JsonListEntry)iterator.next();
      if (jsonlistentry.hasExpired()) {
        arraylist.add(jsonlistentry.getKey());
      }
    }
    iterator = arraylist.iterator();
    while (iterator.hasNext())
    {
      Object object = iterator.next();
      
      this.d.remove(object);
    }
  }
  
  protected JsonListEntry<K> a(JsonObject jsonobject)
  {
    return new JsonListEntry(null, jsonobject);
  }
  
  protected Map<String, V> e()
  {
    return this.d;
  }
  
  /* Error */
  public void save()
    throws IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 49	net/minecraft/server/v1_8_R3/JsonList:d	Ljava/util/Map;
    //   4: invokeinterface 161 1 0
    //   9: astore_1
    //   10: aload_0
    //   11: getfield 75	net/minecraft/server/v1_8_R3/JsonList:b	Lcom/google/gson/Gson;
    //   14: aload_1
    //   15: invokevirtual 215	com/google/gson/Gson:toJson	(Ljava/lang/Object;)Ljava/lang/String;
    //   18: astore_2
    //   19: aconst_null
    //   20: astore_3
    //   21: aload_0
    //   22: getfield 53	net/minecraft/server/v1_8_R3/JsonList:c	Ljava/io/File;
    //   25: getstatic 221	com/google/common/base/Charsets:UTF_8	Ljava/nio/charset/Charset;
    //   28: invokestatic 227	com/google/common/io/Files:newWriter	(Ljava/io/File;Ljava/nio/charset/Charset;)Ljava/io/BufferedWriter;
    //   31: astore_3
    //   32: aload_3
    //   33: aload_2
    //   34: invokevirtual 233	java/io/BufferedWriter:write	(Ljava/lang/String;)V
    //   37: goto +12 -> 49
    //   40: astore 4
    //   42: aload_3
    //   43: invokestatic 241	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Writer;)V
    //   46: aload 4
    //   48: athrow
    //   49: aload_3
    //   50: invokestatic 241	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Writer;)V
    //   53: return
    // Line number table:
    //   Java source line #153	-> byte code offset #0
    //   Java source line #154	-> byte code offset #10
    //   Java source line #155	-> byte code offset #19
    //   Java source line #158	-> byte code offset #21
    //   Java source line #159	-> byte code offset #32
    //   Java source line #160	-> byte code offset #37
    //   Java source line #161	-> byte code offset #42
    //   Java source line #162	-> byte code offset #46
    //   Java source line #161	-> byte code offset #49
    //   Java source line #164	-> byte code offset #53
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	54	0	this	JsonList<K, V>
    //   9	6	1	collection	Collection
    //   18	16	2	s	String
    //   20	30	3	bufferedwriter	java.io.BufferedWriter
    //   40	7	4	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   21	40	40	finally
  }
  
  /* Error */
  public void load()
    throws java.io.FileNotFoundException
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_1
    //   2: aconst_null
    //   3: astore_2
    //   4: aload_0
    //   5: getfield 53	net/minecraft/server/v1_8_R3/JsonList:c	Ljava/io/File;
    //   8: getstatic 221	com/google/common/base/Charsets:UTF_8	Ljava/nio/charset/Charset;
    //   11: invokestatic 256	com/google/common/io/Files:newReader	(Ljava/io/File;Ljava/nio/charset/Charset;)Ljava/io/BufferedReader;
    //   14: astore_2
    //   15: aload_0
    //   16: getfield 75	net/minecraft/server/v1_8_R3/JsonList:b	Lcom/google/gson/Gson;
    //   19: aload_2
    //   20: getstatic 39	net/minecraft/server/v1_8_R3/JsonList:f	Ljava/lang/reflect/ParameterizedType;
    //   23: invokevirtual 260	com/google/gson/Gson:fromJson	(Ljava/io/Reader;Ljava/lang/reflect/Type;)Ljava/lang/Object;
    //   26: checkcast 178	java/util/Collection
    //   29: astore_1
    //   30: goto +108 -> 138
    //   33: pop
    //   34: invokestatic 267	org/bukkit/Bukkit:getLogger	()Ljava/util/logging/Logger;
    //   37: getstatic 273	java/util/logging/Level:INFO	Ljava/util/logging/Level;
    //   40: ldc_w 275
    //   43: aload_0
    //   44: getfield 53	net/minecraft/server/v1_8_R3/JsonList:c	Ljava/io/File;
    //   47: invokevirtual 281	java/util/logging/Logger:log	(Ljava/util/logging/Level;Ljava/lang/String;Ljava/lang/Object;)V
    //   50: aload_2
    //   51: invokestatic 284	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Reader;)V
    //   54: goto +88 -> 142
    //   57: pop
    //   58: invokestatic 267	org/bukkit/Bukkit:getLogger	()Ljava/util/logging/Logger;
    //   61: getstatic 287	java/util/logging/Level:WARNING	Ljava/util/logging/Level;
    //   64: ldc_w 289
    //   67: aload_0
    //   68: getfield 53	net/minecraft/server/v1_8_R3/JsonList:c	Ljava/io/File;
    //   71: invokevirtual 281	java/util/logging/Logger:log	(Ljava/util/logging/Level;Ljava/lang/String;Ljava/lang/Object;)V
    //   74: new 291	java/io/File
    //   77: dup
    //   78: new 293	java/lang/StringBuilder
    //   81: dup
    //   82: invokespecial 294	java/lang/StringBuilder:<init>	()V
    //   85: aload_0
    //   86: getfield 53	net/minecraft/server/v1_8_R3/JsonList:c	Ljava/io/File;
    //   89: invokevirtual 298	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   92: ldc_w 300
    //   95: invokevirtual 303	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   98: invokevirtual 304	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   101: invokespecial 306	java/io/File:<init>	(Ljava/lang/String;)V
    //   104: astore_3
    //   105: aload_0
    //   106: getfield 53	net/minecraft/server/v1_8_R3/JsonList:c	Ljava/io/File;
    //   109: aload_3
    //   110: invokevirtual 310	java/io/File:renameTo	(Ljava/io/File;)Z
    //   113: pop
    //   114: aload_0
    //   115: getfield 53	net/minecraft/server/v1_8_R3/JsonList:c	Ljava/io/File;
    //   118: invokevirtual 313	java/io/File:delete	()Z
    //   121: pop
    //   122: aload_2
    //   123: invokestatic 284	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Reader;)V
    //   126: goto +16 -> 142
    //   129: astore 4
    //   131: aload_2
    //   132: invokestatic 284	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Reader;)V
    //   135: aload 4
    //   137: athrow
    //   138: aload_2
    //   139: invokestatic 284	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Reader;)V
    //   142: aload_1
    //   143: ifnull +71 -> 214
    //   146: aload_0
    //   147: getfield 49	net/minecraft/server/v1_8_R3/JsonList:d	Ljava/util/Map;
    //   150: invokeinterface 316 1 0
    //   155: aload_1
    //   156: invokeinterface 182 1 0
    //   161: astore_3
    //   162: goto +43 -> 205
    //   165: aload_3
    //   166: invokeinterface 189 1 0
    //   171: checkcast 62	net/minecraft/server/v1_8_R3/JsonListEntry
    //   174: astore 4
    //   176: aload 4
    //   178: invokevirtual 94	net/minecraft/server/v1_8_R3/JsonListEntry:getKey	()Ljava/lang/Object;
    //   181: ifnull +24 -> 205
    //   184: aload_0
    //   185: getfield 49	net/minecraft/server/v1_8_R3/JsonList:d	Ljava/util/Map;
    //   188: aload_0
    //   189: aload 4
    //   191: invokevirtual 94	net/minecraft/server/v1_8_R3/JsonListEntry:getKey	()Ljava/lang/Object;
    //   194: invokevirtual 97	net/minecraft/server/v1_8_R3/JsonList:a	(Ljava/lang/Object;)Ljava/lang/String;
    //   197: aload 4
    //   199: invokeinterface 103 3 0
    //   204: pop
    //   205: aload_3
    //   206: invokeinterface 197 1 0
    //   211: ifne -46 -> 165
    //   214: return
    // Line number table:
    //   Java source line #167	-> byte code offset #0
    //   Java source line #168	-> byte code offset #2
    //   Java source line #171	-> byte code offset #4
    //   Java source line #172	-> byte code offset #15
    //   Java source line #174	-> byte code offset #30
    //   Java source line #176	-> byte code offset #34
    //   Java source line #185	-> byte code offset #50
    //   Java source line #177	-> byte code offset #57
    //   Java source line #179	-> byte code offset #58
    //   Java source line #180	-> byte code offset #74
    //   Java source line #181	-> byte code offset #105
    //   Java source line #182	-> byte code offset #114
    //   Java source line #185	-> byte code offset #122
    //   Java source line #184	-> byte code offset #129
    //   Java source line #185	-> byte code offset #131
    //   Java source line #186	-> byte code offset #135
    //   Java source line #185	-> byte code offset #138
    //   Java source line #188	-> byte code offset #142
    //   Java source line #189	-> byte code offset #146
    //   Java source line #190	-> byte code offset #155
    //   Java source line #192	-> byte code offset #162
    //   Java source line #193	-> byte code offset #165
    //   Java source line #195	-> byte code offset #176
    //   Java source line #196	-> byte code offset #184
    //   Java source line #192	-> byte code offset #205
    //   Java source line #201	-> byte code offset #214
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	215	0	this	JsonList<K, V>
    //   1	155	1	collection	Collection
    //   3	136	2	bufferedreader	java.io.BufferedReader
    //   104	6	3	backup	File
    //   161	45	3	iterator	Iterator
    //   129	7	4	localObject	Object
    //   174	24	4	jsonlistentry	JsonListEntry
    //   33	1	7	localFileNotFoundException	java.io.FileNotFoundException
    //   57	1	8	localJsonSyntaxException	com.google.gson.JsonSyntaxException
    // Exception table:
    //   from	to	target	type
    //   4	30	33	java/io/FileNotFoundException
    //   4	30	57	com/google/gson/JsonSyntaxException
    //   4	50	129	finally
    //   57	122	129	finally
  }
  
  class JsonListEntrySerializer
    implements JsonDeserializer<JsonListEntry<K>>, JsonSerializer<JsonListEntry<K>>
  {
    private JsonListEntrySerializer() {}
    
    public JsonElement a(JsonListEntry<K> jsonlistentry, Type type, JsonSerializationContext jsonserializationcontext)
    {
      JsonObject jsonobject = new JsonObject();
      
      jsonlistentry.a(jsonobject);
      return jsonobject;
    }
    
    public JsonListEntry<K> a(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext)
      throws JsonParseException
    {
      if (jsonelement.isJsonObject())
      {
        JsonObject jsonobject = jsonelement.getAsJsonObject();
        JsonListEntry jsonlistentry = JsonList.this.a(jsonobject);
        
        return jsonlistentry;
      }
      return null;
    }
    
    public JsonElement serialize(JsonListEntry<K> object, Type type, JsonSerializationContext jsonserializationcontext)
    {
      return a(object, type, jsonserializationcontext);
    }
    
    public JsonListEntry<K> deserialize(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext)
      throws JsonParseException
    {
      return a(jsonelement, type, jsondeserializationcontext);
    }
    
    JsonListEntrySerializer(Object object)
    {
      this();
    }
  }
}
