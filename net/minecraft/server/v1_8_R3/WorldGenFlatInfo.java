package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class WorldGenFlatInfo
{
  private final List<WorldGenFlatLayerInfo> layers = Lists.newArrayList();
  private final Map<String, Map<String, String>> structures = Maps.newHashMap();
  private int c;
  
  public int a()
  {
    return this.c;
  }
  
  public void a(int ☃)
  {
    this.c = ☃;
  }
  
  public Map<String, Map<String, String>> b()
  {
    return this.structures;
  }
  
  public List<WorldGenFlatLayerInfo> c()
  {
    return this.layers;
  }
  
  public void d()
  {
    int ☃ = 0;
    for (WorldGenFlatLayerInfo ☃ : this.layers)
    {
      ☃.b(☃);
      ☃ += ☃.b();
    }
  }
  
  public String toString()
  {
    StringBuilder ☃ = new StringBuilder();
    
    ☃.append(3);
    ☃.append(";");
    for (int ☃ = 0; ☃ < this.layers.size(); ☃++)
    {
      if (☃ > 0) {
        ☃.append(",");
      }
      ☃.append(((WorldGenFlatLayerInfo)this.layers.get(☃)).toString());
    }
    ☃.append(";");
    ☃.append(this.c);
    int ☃;
    if (!this.structures.isEmpty())
    {
      ☃.append(";");
      ☃ = 0;
      for (Map.Entry<String, Map<String, String>> ☃ : this.structures.entrySet())
      {
        if (☃++ > 0) {
          ☃.append(",");
        }
        ☃.append(((String)☃.getKey()).toLowerCase());
        
        Map<String, String> ☃ = (Map)☃.getValue();
        if (!☃.isEmpty())
        {
          ☃.append("(");
          int ☃ = 0;
          for (Map.Entry<String, String> ☃ : ☃.entrySet())
          {
            if (☃++ > 0) {
              ☃.append(" ");
            }
            ☃.append((String)☃.getKey());
            ☃.append("=");
            ☃.append((String)☃.getValue());
          }
          ☃.append(")");
        }
      }
    }
    else
    {
      ☃.append(";");
    }
    return ☃.toString();
  }
  
  private static WorldGenFlatLayerInfo a(int ☃, String ☃, int ☃)
  {
    String[] ☃ = ☃ >= 3 ? ☃.split("\\*", 2) : ☃.split("x", 2);
    int ☃ = 1;
    int ☃ = 0;
    if (☃.length == 2) {
      try
      {
        ☃ = Integer.parseInt(☃[0]);
        if (☃ + ☃ >= 256) {
          ☃ = 256 - ☃;
        }
        if (☃ < 0) {
          ☃ = 0;
        }
      }
      catch (Throwable ☃)
      {
        return null;
      }
    }
    Block ☃ = null;
    try
    {
      String ☃ = ☃[(☃.length - 1)];
      if (☃ < 3)
      {
        ☃ = ☃.split(":", 2);
        if (☃.length > 1) {
          ☃ = Integer.parseInt(☃[1]);
        }
        ☃ = Block.getById(Integer.parseInt(☃[0]));
      }
      else
      {
        ☃ = ☃.split(":", 3);
        ☃ = ☃.length > 1 ? Block.getByName(☃[0] + ":" + ☃[1]) : null;
        if (☃ != null)
        {
          ☃ = ☃.length > 2 ? Integer.parseInt(☃[2]) : 0;
        }
        else
        {
          ☃ = Block.getByName(☃[0]);
          if (☃ != null) {
            ☃ = ☃.length > 1 ? Integer.parseInt(☃[1]) : 0;
          }
        }
        if (☃ == null) {
          return null;
        }
      }
      if (☃ == Blocks.AIR) {
        ☃ = 0;
      }
      if ((☃ < 0) || (☃ > 15)) {
        ☃ = 0;
      }
    }
    catch (Throwable ☃)
    {
      return null;
    }
    WorldGenFlatLayerInfo ☃ = new WorldGenFlatLayerInfo(☃, ☃, ☃, ☃);
    ☃.b(☃);
    return ☃;
  }
  
  private static List<WorldGenFlatLayerInfo> a(int ☃, String ☃)
  {
    if ((☃ == null) || (☃.length() < 1)) {
      return null;
    }
    List<WorldGenFlatLayerInfo> ☃ = Lists.newArrayList();
    String[] ☃ = ☃.split(",");
    int ☃ = 0;
    for (String ☃ : ☃)
    {
      WorldGenFlatLayerInfo ☃ = a(☃, ☃, ☃);
      if (☃ == null) {
        return null;
      }
      ☃.add(☃);
      ☃ += ☃.b();
    }
    return ☃;
  }
  
  public static WorldGenFlatInfo a(String ☃)
  {
    if (☃ == null) {
      return e();
    }
    String[] ☃ = ☃.split(";", -1);
    int ☃ = ☃.length == 1 ? 0 : MathHelper.a(☃[0], 0);
    if ((☃ < 0) || (☃ > 3)) {
      return e();
    }
    WorldGenFlatInfo ☃ = new WorldGenFlatInfo();
    int ☃ = ☃.length == 1 ? 0 : 1;
    List<WorldGenFlatLayerInfo> ☃ = a(☃, ☃[(☃++)]);
    if ((☃ == null) || (☃.isEmpty())) {
      return e();
    }
    ☃.c().addAll(☃);
    ☃.d();
    
    int ☃ = BiomeBase.PLAINS.id;
    if ((☃ > 0) && (☃.length > ☃)) {
      ☃ = MathHelper.a(☃[(☃++)], ☃);
    }
    ☃.a(☃);
    if ((☃ > 0) && (☃.length > ☃))
    {
      String[] ☃ = ☃[(☃++)].toLowerCase().split(",");
      for (String ☃ : ☃)
      {
        String[] ☃ = ☃.split("\\(", 2);
        Map<String, String> ☃ = Maps.newHashMap();
        if (☃[0].length() > 0)
        {
          ☃.b().put(☃[0], ☃);
          if ((☃.length > 1) && (☃[1].endsWith(")")) && (☃[1].length() > 1))
          {
            String[] ☃ = ☃[1].substring(0, ☃[1].length() - 1).split(" ");
            for (int ☃ = 0; ☃ < ☃.length; ☃++)
            {
              String[] ☃ = ☃[☃].split("=", 2);
              if (☃.length == 2) {
                ☃.put(☃[0], ☃[1]);
              }
            }
          }
        }
      }
    }
    else
    {
      ☃.b().put("village", Maps.newHashMap());
    }
    return ☃;
  }
  
  public static WorldGenFlatInfo e()
  {
    WorldGenFlatInfo ☃ = new WorldGenFlatInfo();
    
    ☃.a(BiomeBase.PLAINS.id);
    ☃.c().add(new WorldGenFlatLayerInfo(1, Blocks.BEDROCK));
    ☃.c().add(new WorldGenFlatLayerInfo(2, Blocks.DIRT));
    ☃.c().add(new WorldGenFlatLayerInfo(1, Blocks.GRASS));
    ☃.d();
    ☃.b().put("village", Maps.newHashMap());
    
    return ☃;
  }
}
