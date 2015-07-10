package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Maps;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorldGenFactory
{
  private static final Logger a = ;
  private static Map<String, Class<? extends StructureStart>> b = Maps.newHashMap();
  private static Map<Class<? extends StructureStart>, String> c = Maps.newHashMap();
  private static Map<String, Class<? extends StructurePiece>> d = Maps.newHashMap();
  private static Map<Class<? extends StructurePiece>, String> e = Maps.newHashMap();
  
  private static void b(Class<? extends StructureStart> ☃, String ☃)
  {
    b.put(☃, ☃);
    c.put(☃, ☃);
  }
  
  static void a(Class<? extends StructurePiece> ☃, String ☃)
  {
    d.put(☃, ☃);
    e.put(☃, ☃);
  }
  
  static
  {
    b(WorldGenMineshaftStart.class, "Mineshaft");
    b(WorldGenVillage.WorldGenVillageStart.class, "Village");
    b(WorldGenNether.WorldGenNetherStart.class, "Fortress");
    b(WorldGenStronghold.WorldGenStronghold2Start.class, "Stronghold");
    b(WorldGenLargeFeature.WorldGenLargeFeatureStart.class, "Temple");
    b(WorldGenMonument.WorldGenMonumentStart.class, "Monument");
    
    WorldGenMineshaftPieces.a();
    WorldGenVillagePieces.a();
    WorldGenNetherPieces.a();
    WorldGenStrongholdPieces.a();
    WorldGenRegistration.a();
    WorldGenMonumentPieces.a();
  }
  
  public static String a(StructureStart ☃)
  {
    return (String)c.get(☃.getClass());
  }
  
  public static String a(StructurePiece ☃)
  {
    return (String)e.get(☃.getClass());
  }
  
  public static StructureStart a(NBTTagCompound ☃, World ☃)
  {
    StructureStart ☃ = null;
    try
    {
      Class<? extends StructureStart> ☃ = (Class)b.get(☃.getString("id"));
      if (☃ != null) {
        ☃ = (StructureStart)☃.newInstance();
      }
    }
    catch (Exception ☃)
    {
      a.warn("Failed Start with id " + ☃.getString("id"));
      ☃.printStackTrace();
    }
    if (☃ != null) {
      ☃.a(☃, ☃);
    } else {
      a.warn("Skipping Structure with id " + ☃.getString("id"));
    }
    return ☃;
  }
  
  public static StructurePiece b(NBTTagCompound ☃, World ☃)
  {
    StructurePiece ☃ = null;
    try
    {
      Class<? extends StructurePiece> ☃ = (Class)d.get(☃.getString("id"));
      if (☃ != null) {
        ☃ = (StructurePiece)☃.newInstance();
      }
    }
    catch (Exception ☃)
    {
      a.warn("Failed Piece with id " + ☃.getString("id"));
      ☃.printStackTrace();
    }
    if (☃ != null) {
      ☃.a(☃, ☃);
    } else {
      a.warn("Skipping Piece with id " + ☃.getString("id"));
    }
    return ☃;
  }
}
