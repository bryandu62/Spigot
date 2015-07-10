package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Maps;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class RegionFileCache
{
  public static final Map<File, RegionFile> a = ;
  
  public static synchronized RegionFile a(File file, int i, int j)
  {
    File file1 = new File(file, "region");
    File file2 = new File(file1, "r." + (i >> 5) + "." + (j >> 5) + ".mca");
    RegionFile regionfile = (RegionFile)a.get(file2);
    if (regionfile != null) {
      return regionfile;
    }
    if (!file1.exists()) {
      file1.mkdirs();
    }
    if (a.size() >= 256) {
      a();
    }
    RegionFile regionfile1 = new RegionFile(file2);
    
    a.put(file2, regionfile1);
    return regionfile1;
  }
  
  public static synchronized void a()
  {
    Iterator iterator = a.values().iterator();
    while (iterator.hasNext())
    {
      RegionFile regionfile = (RegionFile)iterator.next();
      try
      {
        if (regionfile != null) {
          regionfile.c();
        }
      }
      catch (IOException ioexception)
      {
        ioexception.printStackTrace();
      }
    }
    a.clear();
  }
  
  public static DataInputStream c(File file, int i, int j)
  {
    RegionFile regionfile = a(file, i, j);
    
    return regionfile.a(i & 0x1F, j & 0x1F);
  }
  
  public static DataOutputStream d(File file, int i, int j)
  {
    RegionFile regionfile = a(file, i, j);
    
    return regionfile.b(i & 0x1F, j & 0x1F);
  }
}
