package net.minecraft.server.v1_8_R3;

import com.google.common.base.Charsets;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.ProfileLookupCallback;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingDeque;
import org.apache.commons.io.IOUtils;
import org.spigotmc.SpigotConfig;

public class UserCache
{
  public static final SimpleDateFormat a = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
  private final Map<String, UserCacheEntry> c = Maps.newHashMap();
  private final Map<UUID, UserCacheEntry> d = Maps.newHashMap();
  private final Deque<GameProfile> e = new LinkedBlockingDeque();
  private final MinecraftServer f;
  protected final Gson b;
  private final File g;
  private static final ParameterizedType h = new ParameterizedType()
  {
    public Type[] getActualTypeArguments()
    {
      return new Type[] { UserCache.UserCacheEntry.class };
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
  
  public UserCache(MinecraftServer minecraftserver, File file)
  {
    this.f = minecraftserver;
    this.g = file;
    GsonBuilder gsonbuilder = new GsonBuilder();
    
    gsonbuilder.registerTypeHierarchyAdapter(UserCacheEntry.class, new BanEntrySerializer(null));
    this.b = gsonbuilder.create();
    b();
  }
  
  private static GameProfile a(MinecraftServer minecraftserver, String s)
  {
    GameProfile[] agameprofile = new GameProfile[1];
    ProfileLookupCallback profilelookupcallback = new ProfileLookupCallback()
    {
      public void onProfileLookupSucceeded(GameProfile gameprofile)
      {
        UserCache.this[0] = gameprofile;
      }
      
      public void onProfileLookupFailed(GameProfile gameprofile, Exception exception)
      {
        UserCache.this[0] = null;
      }
    };
    minecraftserver.getGameProfileRepository().findProfilesByNames(new String[] { s }, Agent.MINECRAFT, profilelookupcallback);
    if ((!minecraftserver.getOnlineMode()) && (agameprofile[0] == null))
    {
      UUID uuid = EntityHuman.a(new GameProfile(null, s));
      GameProfile gameprofile = new GameProfile(uuid, s);
      
      profilelookupcallback.onProfileLookupSucceeded(gameprofile);
    }
    return agameprofile[0];
  }
  
  public void a(GameProfile gameprofile)
  {
    a(gameprofile, null);
  }
  
  private void a(GameProfile gameprofile, Date date)
  {
    UUID uuid = gameprofile.getId();
    if (date == null)
    {
      Calendar calendar = Calendar.getInstance();
      
      calendar.setTime(new Date());
      calendar.add(2, 1);
      date = calendar.getTime();
    }
    gameprofile.getName().toLowerCase(Locale.ROOT);
    UserCacheEntry usercache_usercacheentry = new UserCacheEntry(gameprofile, date, null);
    if (this.d.containsKey(uuid))
    {
      UserCacheEntry usercache_usercacheentry1 = (UserCacheEntry)this.d.get(uuid);
      
      this.c.remove(usercache_usercacheentry1.a().getName().toLowerCase(Locale.ROOT));
      this.e.remove(gameprofile);
    }
    this.c.put(gameprofile.getName().toLowerCase(Locale.ROOT), usercache_usercacheentry);
    this.d.put(uuid, usercache_usercacheentry);
    this.e.addFirst(gameprofile);
    c();
  }
  
  public GameProfile getProfile(String s)
  {
    String s1 = s.toLowerCase(Locale.ROOT);
    UserCacheEntry usercache_usercacheentry = (UserCacheEntry)this.c.get(s1);
    if ((usercache_usercacheentry != null) && (new Date().getTime() >= usercache_usercacheentry.c.getTime()))
    {
      this.d.remove(usercache_usercacheentry.a().getId());
      this.c.remove(usercache_usercacheentry.a().getName().toLowerCase(Locale.ROOT));
      this.e.remove(usercache_usercacheentry.a());
      usercache_usercacheentry = null;
    }
    if (usercache_usercacheentry != null)
    {
      GameProfile gameprofile = usercache_usercacheentry.a();
      this.e.remove(gameprofile);
      this.e.addFirst(gameprofile);
    }
    else
    {
      GameProfile gameprofile = a(this.f, s);
      if (gameprofile != null)
      {
        a(gameprofile);
        usercache_usercacheentry = (UserCacheEntry)this.c.get(s1);
      }
    }
    if (!SpigotConfig.saveUserCacheOnStopOnly) {
      c();
    }
    return usercache_usercacheentry == null ? null : usercache_usercacheentry.a();
  }
  
  public String[] a()
  {
    ArrayList arraylist = Lists.newArrayList(this.c.keySet());
    
    return (String[])arraylist.toArray(new String[arraylist.size()]);
  }
  
  public GameProfile a(UUID uuid)
  {
    UserCacheEntry usercache_usercacheentry = (UserCacheEntry)this.d.get(uuid);
    
    return usercache_usercacheentry == null ? null : usercache_usercacheentry.a();
  }
  
  private UserCacheEntry b(UUID uuid)
  {
    UserCacheEntry usercache_usercacheentry = (UserCacheEntry)this.d.get(uuid);
    if (usercache_usercacheentry != null)
    {
      GameProfile gameprofile = usercache_usercacheentry.a();
      
      this.e.remove(gameprofile);
      this.e.addFirst(gameprofile);
    }
    return usercache_usercacheentry;
  }
  
  /* Error */
  public void b()
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_1
    //   2: aload_0
    //   3: getfield 70	net/minecraft/server/v1_8_R3/UserCache:g	Ljava/io/File;
    //   6: getstatic 292	com/google/common/base/Charsets:UTF_8	Ljava/nio/charset/Charset;
    //   9: invokestatic 298	com/google/common/io/Files:newReader	(Ljava/io/File;Ljava/nio/charset/Charset;)Ljava/io/BufferedReader;
    //   12: astore_1
    //   13: aload_0
    //   14: getfield 86	net/minecraft/server/v1_8_R3/UserCache:b	Lcom/google/gson/Gson;
    //   17: aload_1
    //   18: getstatic 49	net/minecraft/server/v1_8_R3/UserCache:h	Ljava/lang/reflect/ParameterizedType;
    //   21: invokevirtual 304	com/google/gson/Gson:fromJson	(Ljava/io/Reader;Ljava/lang/reflect/Type;)Ljava/lang/Object;
    //   24: checkcast 306	java/util/List
    //   27: astore_2
    //   28: aload_0
    //   29: getfield 59	net/minecraft/server/v1_8_R3/UserCache:c	Ljava/util/Map;
    //   32: invokeinterface 309 1 0
    //   37: aload_0
    //   38: getfield 61	net/minecraft/server/v1_8_R3/UserCache:d	Ljava/util/Map;
    //   41: invokeinterface 309 1 0
    //   46: aload_0
    //   47: getfield 66	net/minecraft/server/v1_8_R3/UserCache:e	Ljava/util/Deque;
    //   50: invokeinterface 310 1 0
    //   55: aload_2
    //   56: invokestatic 314	com/google/common/collect/Lists:reverse	(Ljava/util/List;)Ljava/util/List;
    //   59: invokeinterface 318 1 0
    //   64: astore_3
    //   65: goto +33 -> 98
    //   68: aload_3
    //   69: invokeinterface 326 1 0
    //   74: checkcast 14	net/minecraft/server/v1_8_R3/UserCache$UserCacheEntry
    //   77: astore 4
    //   79: aload 4
    //   81: ifnull +17 -> 98
    //   84: aload_0
    //   85: aload 4
    //   87: invokevirtual 209	net/minecraft/server/v1_8_R3/UserCache$UserCacheEntry:a	()Lcom/mojang/authlib/GameProfile;
    //   90: aload 4
    //   92: invokevirtual 328	net/minecraft/server/v1_8_R3/UserCache$UserCacheEntry:b	()Ljava/util/Date;
    //   95: invokespecial 152	net/minecraft/server/v1_8_R3/UserCache:a	(Lcom/mojang/authlib/GameProfile;Ljava/util/Date;)V
    //   98: aload_3
    //   99: invokeinterface 331 1 0
    //   104: ifne -36 -> 68
    //   107: goto +55 -> 162
    //   110: pop
    //   111: aload_1
    //   112: invokestatic 337	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Reader;)V
    //   115: goto +51 -> 166
    //   118: pop
    //   119: getstatic 342	net/minecraft/server/v1_8_R3/JsonList:a	Lorg/apache/logging/log4j/Logger;
    //   122: ldc_w 344
    //   125: invokeinterface 349 2 0
    //   130: aload_0
    //   131: getfield 70	net/minecraft/server/v1_8_R3/UserCache:g	Ljava/io/File;
    //   134: invokevirtual 354	java/io/File:delete	()Z
    //   137: pop
    //   138: aload_1
    //   139: invokestatic 337	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Reader;)V
    //   142: goto +24 -> 166
    //   145: pop
    //   146: aload_1
    //   147: invokestatic 337	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Reader;)V
    //   150: goto +16 -> 166
    //   153: astore 5
    //   155: aload_1
    //   156: invokestatic 337	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Reader;)V
    //   159: aload 5
    //   161: athrow
    //   162: aload_1
    //   163: invokestatic 337	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Reader;)V
    //   166: return
    // Line number table:
    //   Java source line #182	-> byte code offset #0
    //   Java source line #185	-> byte code offset #2
    //   Java source line #186	-> byte code offset #13
    //   Java source line #188	-> byte code offset #28
    //   Java source line #189	-> byte code offset #37
    //   Java source line #190	-> byte code offset #46
    //   Java source line #191	-> byte code offset #55
    //   Java source line #193	-> byte code offset #65
    //   Java source line #194	-> byte code offset #68
    //   Java source line #196	-> byte code offset #79
    //   Java source line #197	-> byte code offset #84
    //   Java source line #193	-> byte code offset #98
    //   Java source line #200	-> byte code offset #107
    //   Java source line #210	-> byte code offset #111
    //   Java source line #203	-> byte code offset #118
    //   Java source line #204	-> byte code offset #119
    //   Java source line #205	-> byte code offset #130
    //   Java source line #210	-> byte code offset #138
    //   Java source line #207	-> byte code offset #145
    //   Java source line #210	-> byte code offset #146
    //   Java source line #209	-> byte code offset #153
    //   Java source line #210	-> byte code offset #155
    //   Java source line #211	-> byte code offset #159
    //   Java source line #210	-> byte code offset #162
    //   Java source line #213	-> byte code offset #166
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	167	0	this	UserCache
    //   1	162	1	bufferedreader	java.io.BufferedReader
    //   27	29	2	list	List
    //   64	35	3	iterator	Iterator
    //   77	14	4	usercache_usercacheentry	UserCacheEntry
    //   153	7	5	localObject	Object
    //   110	1	6	localFileNotFoundException	FileNotFoundException
    //   118	1	7	localJsonSyntaxException	com.google.gson.JsonSyntaxException
    //   145	1	8	localJsonParseException	JsonParseException
    // Exception table:
    //   from	to	target	type
    //   2	107	110	java/io/FileNotFoundException
    //   2	107	118	com/google/gson/JsonSyntaxException
    //   2	107	145	com/google/gson/JsonParseException
    //   2	111	153	finally
    //   118	138	153	finally
    //   145	146	153	finally
  }
  
  public void c()
  {
    String s = this.b.toJson(a(SpigotConfig.userCacheCap));
    BufferedWriter bufferedwriter = null;
    try
    {
      bufferedwriter = Files.newWriter(this.g, Charsets.UTF_8);
      bufferedwriter.write(s);
      return;
    }
    catch (FileNotFoundException localFileNotFoundException) {}catch (IOException localIOException) {}finally
    {
      IOUtils.closeQuietly(bufferedwriter);
    }
  }
  
  private List<UserCacheEntry> a(int i)
  {
    ArrayList arraylist = Lists.newArrayList();
    ArrayList arraylist1 = Lists.newArrayList(Iterators.limit(this.e.iterator(), i));
    Iterator iterator = arraylist1.iterator();
    while (iterator.hasNext())
    {
      GameProfile gameprofile = (GameProfile)iterator.next();
      UserCacheEntry usercache_usercacheentry = b(gameprofile.getId());
      if (usercache_usercacheentry != null) {
        arraylist.add(usercache_usercacheentry);
      }
    }
    return arraylist;
  }
  
  class UserCacheEntry
  {
    private final GameProfile b;
    private final Date c;
    
    private UserCacheEntry(GameProfile gameprofile, Date date)
    {
      this.b = gameprofile;
      this.c = date;
    }
    
    public GameProfile a()
    {
      return this.b;
    }
    
    public Date b()
    {
      return this.c;
    }
    
    UserCacheEntry(GameProfile gameprofile, Date date, Object object)
    {
      this(gameprofile, date);
    }
  }
  
  class BanEntrySerializer
    implements JsonDeserializer<UserCache.UserCacheEntry>, JsonSerializer<UserCache.UserCacheEntry>
  {
    private BanEntrySerializer() {}
    
    public JsonElement a(UserCache.UserCacheEntry usercache_usercacheentry, Type type, JsonSerializationContext jsonserializationcontext)
    {
      JsonObject jsonobject = new JsonObject();
      
      jsonobject.addProperty("name", usercache_usercacheentry.a().getName());
      UUID uuid = usercache_usercacheentry.a().getId();
      
      jsonobject.addProperty("uuid", uuid == null ? "" : uuid.toString());
      jsonobject.addProperty("expiresOn", UserCache.a.format(usercache_usercacheentry.b()));
      return jsonobject;
    }
    
    public UserCache.UserCacheEntry a(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext)
      throws JsonParseException
    {
      if (jsonelement.isJsonObject())
      {
        JsonObject jsonobject = jsonelement.getAsJsonObject();
        JsonElement jsonelement1 = jsonobject.get("name");
        JsonElement jsonelement2 = jsonobject.get("uuid");
        JsonElement jsonelement3 = jsonobject.get("expiresOn");
        if ((jsonelement1 != null) && (jsonelement2 != null))
        {
          String s = jsonelement2.getAsString();
          String s1 = jsonelement1.getAsString();
          Date date = null;
          if (jsonelement3 != null) {
            try
            {
              date = UserCache.a.parse(jsonelement3.getAsString());
            }
            catch (ParseException localParseException)
            {
              date = null;
            }
          }
          if ((s1 != null) && (s != null))
          {
            try
            {
              uuid = UUID.fromString(s);
            }
            catch (Throwable localThrowable)
            {
              UUID uuid;
              return null;
            }
            UUID uuid;
            UserCache tmp123_120 = UserCache.this;tmp123_120.getClass();UserCache.UserCacheEntry usercache_usercacheentry = new UserCache.UserCacheEntry(tmp123_120, new GameProfile(uuid, s1), date, null);
            
            return usercache_usercacheentry;
          }
          return null;
        }
        return null;
      }
      return null;
    }
    
    public JsonElement serialize(UserCache.UserCacheEntry object, Type type, JsonSerializationContext jsonserializationcontext)
    {
      return a(object, type, jsonserializationcontext);
    }
    
    public UserCache.UserCacheEntry deserialize(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext)
      throws JsonParseException
    {
      return a(jsonelement, type, jsondeserializationcontext);
    }
    
    BanEntrySerializer(Object object)
    {
      this();
    }
  }
}
