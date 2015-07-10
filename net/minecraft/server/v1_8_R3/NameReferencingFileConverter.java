package net.minecraft.server.v1_8_R3;

import com.google.common.base.Charsets;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.ProfileLookupCallback;
import com.mojang.authlib.yggdrasil.ProfileNotFoundException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.spigotmc.SpigotConfig;

public class NameReferencingFileConverter
{
  private static final Logger e = ;
  public static final File a = new File("banned-ips.txt");
  public static final File b = new File("banned-players.txt");
  public static final File c = new File("ops.txt");
  public static final File d = new File("white-list.txt");
  
  static List<String> a(File file, Map<String, String[]> map)
    throws IOException
  {
    List list = Files.readLines(file, Charsets.UTF_8);
    Iterator iterator = list.iterator();
    while (iterator.hasNext())
    {
      String s = (String)iterator.next();
      
      s = s.trim();
      if ((!s.startsWith("#")) && (s.length() >= 1))
      {
        String[] astring = s.split("\\|");
        
        map.put(astring[0].toLowerCase(Locale.ROOT), astring);
      }
    }
    return list;
  }
  
  private static void a(MinecraftServer minecraftserver, Collection<String> collection, ProfileLookupCallback profilelookupcallback)
  {
    String[] astring = (String[])Iterators.toArray(Iterators.filter(collection.iterator(), new Predicate()
    {
      public boolean a(String s)
      {
        return !UtilColor.b(s);
      }
      
      public boolean apply(Object object)
      {
        return a((String)object);
      }
    }), String.class);
    if ((minecraftserver.getOnlineMode()) || (SpigotConfig.bungee))
    {
      minecraftserver.getGameProfileRepository().findProfilesByNames(astring, Agent.MINECRAFT, profilelookupcallback);
    }
    else
    {
      String[] astring1 = astring;
      int i = astring.length;
      for (int j = 0; j < i; j++)
      {
        String s = astring1[j];
        UUID uuid = EntityHuman.a(new GameProfile(null, s));
        GameProfile gameprofile = new GameProfile(uuid, s);
        
        profilelookupcallback.onProfileLookupSucceeded(gameprofile);
      }
    }
  }
  
  public static boolean a(MinecraftServer minecraftserver)
  {
    final GameProfileBanList gameprofilebanlist = new GameProfileBanList(PlayerList.a);
    if ((b.exists()) && (b.isFile()))
    {
      if (gameprofilebanlist.c().exists()) {
        try
        {
          gameprofilebanlist.load();
        }
        catch (IOException localIOException1)
        {
          e.warn("Could not load existing file " + gameprofilebanlist.c().getName());
        }
      }
      try
      {
        final HashMap hashmap = Maps.newHashMap();
        
        a(b, hashmap);
        ProfileLookupCallback profilelookupcallback = new ProfileLookupCallback()
        {
          public void onProfileLookupSucceeded(GameProfile gameprofile)
          {
            NameReferencingFileConverter.this.getUserCache().a(gameprofile);
            String[] astring = (String[])hashmap.get(gameprofile.getName().toLowerCase(Locale.ROOT));
            if (astring == null)
            {
              NameReferencingFileConverter.e.warn("Could not convert user banlist entry for " + gameprofile.getName());
              throw new NameReferencingFileConverter.FileConversionException("Profile not in the conversionlist", null, null);
            }
            Date date = astring.length > 1 ? NameReferencingFileConverter.b(astring[1], null) : null;
            String s = astring.length > 2 ? astring[2] : null;
            Date date1 = astring.length > 3 ? NameReferencingFileConverter.b(astring[3], null) : null;
            String s1 = astring.length > 4 ? astring[4] : null;
            
            gameprofilebanlist.add(new GameProfileBanEntry(gameprofile, date, s, date1, s1));
          }
          
          public void onProfileLookupFailed(GameProfile gameprofile, Exception exception)
          {
            NameReferencingFileConverter.e.warn("Could not lookup user banlist entry for " + gameprofile.getName(), exception);
            if (!(exception instanceof ProfileNotFoundException)) {
              throw new NameReferencingFileConverter.FileConversionException("Could not request user " + gameprofile.getName() + " from backend systems", exception, null);
            }
          }
        };
        a(minecraftserver, hashmap.keySet(), profilelookupcallback);
        gameprofilebanlist.save();
        c(b);
        return true;
      }
      catch (IOException ioexception)
      {
        e.warn("Could not read old user banlist to convert it!", ioexception);
        return false;
      }
      catch (FileConversionException namereferencingfileconverter_fileconversionexception)
      {
        e.error("Conversion failed, please try again later", namereferencingfileconverter_fileconversionexception);
        return false;
      }
    }
    return true;
  }
  
  public static boolean b(MinecraftServer minecraftserver)
  {
    IpBanList ipbanlist = new IpBanList(PlayerList.b);
    if ((a.exists()) && (a.isFile()))
    {
      if (ipbanlist.c().exists()) {
        try
        {
          ipbanlist.load();
        }
        catch (IOException localIOException1)
        {
          e.warn("Could not load existing file " + ipbanlist.c().getName());
        }
      }
      try
      {
        HashMap hashmap = Maps.newHashMap();
        
        a(a, hashmap);
        Iterator iterator = hashmap.keySet().iterator();
        while (iterator.hasNext())
        {
          String s = (String)iterator.next();
          String[] astring = (String[])hashmap.get(s);
          Date date = astring.length > 1 ? b(astring[1], null) : null;
          String s1 = astring.length > 2 ? astring[2] : null;
          Date date1 = astring.length > 3 ? b(astring[3], null) : null;
          String s2 = astring.length > 4 ? astring[4] : null;
          
          ipbanlist.add(new IpBanEntry(s, date, s1, date1, s2));
        }
        ipbanlist.save();
        c(a);
        return true;
      }
      catch (IOException ioexception)
      {
        e.warn("Could not parse old ip banlist to convert it!", ioexception);
        return false;
      }
    }
    return true;
  }
  
  public static boolean c(MinecraftServer minecraftserver)
  {
    final OpList oplist = new OpList(PlayerList.c);
    if ((c.exists()) && (c.isFile()))
    {
      if (oplist.c().exists()) {
        try
        {
          oplist.load();
        }
        catch (IOException localIOException1)
        {
          e.warn("Could not load existing file " + oplist.c().getName());
        }
      }
      try
      {
        List list = Files.readLines(c, Charsets.UTF_8);
        ProfileLookupCallback profilelookupcallback = new ProfileLookupCallback()
        {
          public void onProfileLookupSucceeded(GameProfile gameprofile)
          {
            NameReferencingFileConverter.this.getUserCache().a(gameprofile);
            oplist.add(new OpListEntry(gameprofile, NameReferencingFileConverter.this.p(), false));
          }
          
          public void onProfileLookupFailed(GameProfile gameprofile, Exception exception)
          {
            NameReferencingFileConverter.e.warn("Could not lookup oplist entry for " + gameprofile.getName(), exception);
            if (!(exception instanceof ProfileNotFoundException)) {
              throw new NameReferencingFileConverter.FileConversionException("Could not request user " + gameprofile.getName() + " from backend systems", exception, null);
            }
          }
        };
        a(minecraftserver, list, profilelookupcallback);
        oplist.save();
        c(c);
        return true;
      }
      catch (IOException ioexception)
      {
        e.warn("Could not read old oplist to convert it!", ioexception);
        return false;
      }
      catch (FileConversionException namereferencingfileconverter_fileconversionexception)
      {
        e.error("Conversion failed, please try again later", namereferencingfileconverter_fileconversionexception);
        return false;
      }
    }
    return true;
  }
  
  public static boolean d(MinecraftServer minecraftserver)
  {
    final WhiteList whitelist = new WhiteList(PlayerList.d);
    if ((d.exists()) && (d.isFile()))
    {
      if (whitelist.c().exists()) {
        try
        {
          whitelist.load();
        }
        catch (IOException localIOException1)
        {
          e.warn("Could not load existing file " + whitelist.c().getName());
        }
      }
      try
      {
        List list = Files.readLines(d, Charsets.UTF_8);
        ProfileLookupCallback profilelookupcallback = new ProfileLookupCallback()
        {
          public void onProfileLookupSucceeded(GameProfile gameprofile)
          {
            NameReferencingFileConverter.this.getUserCache().a(gameprofile);
            whitelist.add(new WhiteListEntry(gameprofile));
          }
          
          public void onProfileLookupFailed(GameProfile gameprofile, Exception exception)
          {
            NameReferencingFileConverter.e.warn("Could not lookup user whitelist entry for " + gameprofile.getName(), exception);
            if (!(exception instanceof ProfileNotFoundException)) {
              throw new NameReferencingFileConverter.FileConversionException("Could not request user " + gameprofile.getName() + " from backend systems", exception, null);
            }
          }
        };
        a(minecraftserver, list, profilelookupcallback);
        whitelist.save();
        c(d);
        return true;
      }
      catch (IOException ioexception)
      {
        e.warn("Could not read old whitelist to convert it!", ioexception);
        return false;
      }
      catch (FileConversionException namereferencingfileconverter_fileconversionexception)
      {
        e.error("Conversion failed, please try again later", namereferencingfileconverter_fileconversionexception);
        return false;
      }
    }
    return true;
  }
  
  public static String a(String s)
  {
    if ((!UtilColor.b(s)) && (s.length() <= 16))
    {
      MinecraftServer minecraftserver = MinecraftServer.getServer();
      GameProfile gameprofile = minecraftserver.getUserCache().getProfile(s);
      if ((gameprofile != null) && (gameprofile.getId() != null)) {
        return gameprofile.getId().toString();
      }
      if ((!minecraftserver.T()) && (minecraftserver.getOnlineMode()))
      {
        final ArrayList arraylist = Lists.newArrayList();
        ProfileLookupCallback profilelookupcallback = new ProfileLookupCallback()
        {
          public void onProfileLookupSucceeded(GameProfile gameprofile)
          {
            NameReferencingFileConverter.this.getUserCache().a(gameprofile);
            arraylist.add(gameprofile);
          }
          
          public void onProfileLookupFailed(GameProfile gameprofile, Exception exception)
          {
            NameReferencingFileConverter.e.warn("Could not lookup user whitelist entry for " + gameprofile.getName(), exception);
          }
        };
        a(minecraftserver, Lists.newArrayList(new String[] { s }), profilelookupcallback);
        return (arraylist.size() > 0) && (((GameProfile)arraylist.get(0)).getId() != null) ? ((GameProfile)arraylist.get(0)).getId().toString() : "";
      }
      return EntityHuman.a(new GameProfile(null, s)).toString();
    }
    return s;
  }
  
  public static boolean a(DedicatedServer dedicatedserver, PropertyManager propertymanager)
  {
    final File file = d(propertymanager);
    new File(file.getParentFile(), "playerdata");
    final File file2 = new File(file.getParentFile(), "unknownplayers");
    if ((file.exists()) && (file.isDirectory()))
    {
      File[] afile = file.listFiles();
      ArrayList arraylist = Lists.newArrayList();
      File[] afile1 = afile;
      int i = afile.length;
      for (int j = 0; j < i; j++)
      {
        File file3 = afile1[j];
        String s = file3.getName();
        if (s.toLowerCase(Locale.ROOT).endsWith(".dat"))
        {
          String s1 = s.substring(0, s.length() - ".dat".length());
          if (s1.length() > 0) {
            arraylist.add(s1);
          }
        }
      }
      try
      {
        final String[] astring = (String[])arraylist.toArray(new String[arraylist.size()]);
        ProfileLookupCallback profilelookupcallback = new ProfileLookupCallback()
        {
          public void onProfileLookupSucceeded(GameProfile gameprofile)
          {
            NameReferencingFileConverter.this.getUserCache().a(gameprofile);
            UUID uuid = gameprofile.getId();
            if (uuid == null) {
              throw new NameReferencingFileConverter.FileConversionException("Missing UUID for user profile " + gameprofile.getName(), null, null);
            }
            a(file, a(gameprofile), uuid.toString());
          }
          
          public void onProfileLookupFailed(GameProfile gameprofile, Exception exception)
          {
            NameReferencingFileConverter.e.warn("Could not lookup user uuid for " + gameprofile.getName(), exception);
            if ((exception instanceof ProfileNotFoundException))
            {
              String s = a(gameprofile);
              
              a(file, s, s);
            }
            else
            {
              throw new NameReferencingFileConverter.FileConversionException("Could not request user " + gameprofile.getName() + " from backend systems", exception, null);
            }
          }
          
          private void a(File file, String s, String s1)
          {
            File file1 = new File(file2, s + ".dat");
            File file3 = new File(file, s1 + ".dat");
            
            NBTTagCompound root = null;
            try
            {
              root = NBTCompressedStreamTools.a(new FileInputStream(file1));
            }
            catch (Exception exception)
            {
              exception.printStackTrace();
            }
            if (root != null)
            {
              if (!root.hasKey("bukkit")) {
                root.set("bukkit", new NBTTagCompound());
              }
              NBTTagCompound data = root.getCompound("bukkit");
              data.setString("lastKnownName", s);
              try
              {
                NBTCompressedStreamTools.a(root, new FileOutputStream(file2));
              }
              catch (Exception exception)
              {
                exception.printStackTrace();
              }
            }
            NameReferencingFileConverter.b(file);
            if (!file1.renameTo(file3)) {
              throw new NameReferencingFileConverter.FileConversionException("Could not convert file for " + s, null, null);
            }
          }
          
          private String a(GameProfile gameprofile)
          {
            String s = null;
            for (int i = 0; i < astring.length; i++) {
              if ((astring[i] != null) && (astring[i].equalsIgnoreCase(gameprofile.getName())))
              {
                s = astring[i];
                break;
              }
            }
            if (s == null) {
              throw new NameReferencingFileConverter.FileConversionException("Could not find the filename for " + gameprofile.getName() + " anymore", null, null);
            }
            return s;
          }
        };
        a(dedicatedserver, Lists.newArrayList(astring), profilelookupcallback);
        return true;
      }
      catch (FileConversionException namereferencingfileconverter_fileconversionexception)
      {
        e.error("Conversion failed, please try again later", namereferencingfileconverter_fileconversionexception);
        return false;
      }
    }
    return true;
  }
  
  private static void b(File file)
  {
    if (file.exists())
    {
      if (!file.isDirectory()) {
        throw new FileConversionException("Can't create directory " + file.getName() + " in world save directory.", null, null);
      }
    }
    else if (!file.mkdirs()) {
      throw new FileConversionException("Can't create directory " + file.getName() + " in world save directory.", null, null);
    }
  }
  
  public static boolean a(PropertyManager propertymanager)
  {
    boolean flag = b(propertymanager);
    
    flag = (flag) && (c(propertymanager));
    return flag;
  }
  
  private static boolean b(PropertyManager propertymanager)
  {
    boolean flag = false;
    if ((b.exists()) && (b.isFile())) {
      flag = true;
    }
    boolean flag1 = false;
    if ((a.exists()) && (a.isFile())) {
      flag1 = true;
    }
    boolean flag2 = false;
    if ((c.exists()) && (c.isFile())) {
      flag2 = true;
    }
    boolean flag3 = false;
    if ((d.exists()) && (d.isFile())) {
      flag3 = true;
    }
    if ((!flag) && (!flag1) && (!flag2) && (!flag3)) {
      return true;
    }
    e.warn("**** FAILED TO START THE SERVER AFTER ACCOUNT CONVERSION!");
    e.warn("** please remove the following files and restart the server:");
    if (flag) {
      e.warn("* " + b.getName());
    }
    if (flag1) {
      e.warn("* " + a.getName());
    }
    if (flag2) {
      e.warn("* " + c.getName());
    }
    if (flag3) {
      e.warn("* " + d.getName());
    }
    return false;
  }
  
  private static boolean c(PropertyManager propertymanager)
  {
    File file = d(propertymanager);
    if ((file.exists()) && (file.isDirectory()) && ((file.list().length > 0) || (!file.delete())))
    {
      e.warn("**** DETECTED OLD PLAYER DIRECTORY IN THE WORLD SAVE");
      e.warn("**** THIS USUALLY HAPPENS WHEN THE AUTOMATIC CONVERSION FAILED IN SOME WAY");
      e.warn("** please restart the server and if the problem persists, remove the directory '{}'", new Object[] { file.getPath() });
      return false;
    }
    return true;
  }
  
  private static File d(PropertyManager propertymanager)
  {
    String s = propertymanager.getString("level-name", "world");
    File file = new File(MinecraftServer.getServer().server.getWorldContainer(), s);
    
    return new File(file, "players");
  }
  
  private static void c(File file)
  {
    File file1 = new File(file.getName() + ".converted");
    
    file.renameTo(file1);
  }
  
  private static Date b(String s, Date date)
  {
    Date date1;
    try
    {
      date1 = ExpirableListEntry.a.parse(s);
    }
    catch (ParseException localParseException)
    {
      Date date1;
      date1 = date;
    }
    return date1;
  }
  
  static class FileConversionException
    extends RuntimeException
  {
    private FileConversionException(String s, Throwable throwable)
    {
      super(throwable);
    }
    
    private FileConversionException(String s)
    {
      super();
    }
    
    FileConversionException(String s, Object object)
    {
      this(s);
    }
    
    FileConversionException(String s, Throwable throwable, Object object)
    {
      this(s, throwable);
    }
  }
}
