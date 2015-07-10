package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class WorldGenNether
  extends StructureGenerator
{
  private List<BiomeBase.BiomeMeta> d = Lists.newArrayList();
  
  public WorldGenNether()
  {
    this.d.add(new BiomeBase.BiomeMeta(EntityBlaze.class, 10, 2, 3));
    this.d.add(new BiomeBase.BiomeMeta(EntityPigZombie.class, 5, 4, 4));
    this.d.add(new BiomeBase.BiomeMeta(EntitySkeleton.class, 10, 4, 4));
    this.d.add(new BiomeBase.BiomeMeta(EntityMagmaCube.class, 3, 4, 4));
  }
  
  public String a()
  {
    return "Fortress";
  }
  
  public List<BiomeBase.BiomeMeta> b()
  {
    return this.d;
  }
  
  protected boolean a(int ☃, int ☃)
  {
    int ☃ = ☃ >> 4;
    int ☃ = ☃ >> 4;
    
    this.b.setSeed(☃ ^ ☃ << 4 ^ this.c.getSeed());
    this.b.nextInt();
    if (this.b.nextInt(3) != 0) {
      return false;
    }
    if (☃ != (☃ << 4) + 4 + this.b.nextInt(8)) {
      return false;
    }
    if (☃ != (☃ << 4) + 4 + this.b.nextInt(8)) {
      return false;
    }
    return true;
  }
  
  protected StructureStart b(int ☃, int ☃)
  {
    return new WorldGenNetherStart(this.c, this.b, ☃, ☃);
  }
  
  public static class WorldGenNetherStart
    extends StructureStart
  {
    public WorldGenNetherStart() {}
    
    public WorldGenNetherStart(World ☃, Random ☃, int ☃, int ☃)
    {
      super(☃);
      
      WorldGenNetherPieces.WorldGenNetherPiece15 ☃ = new WorldGenNetherPieces.WorldGenNetherPiece15(☃, (☃ << 4) + 2, (☃ << 4) + 2);
      this.a.add(☃);
      ☃.a(☃, this.a, ☃);
      
      List<StructurePiece> ☃ = ☃.e;
      while (!☃.isEmpty())
      {
        int ☃ = ☃.nextInt(☃.size());
        StructurePiece ☃ = (StructurePiece)☃.remove(☃);
        ☃.a(☃, this.a, ☃);
      }
      c();
      a(☃, ☃, 48, 70);
    }
  }
}
