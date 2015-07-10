package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;

public class WorldGenNetherPieces
{
  public static void a()
  {
    WorldGenFactory.a(WorldGenNetherPiece1.class, "NeBCr");
    WorldGenFactory.a(WorldGenNetherPiece2.class, "NeBEF");
    WorldGenFactory.a(WorldGenNetherPiece3.class, "NeBS");
    WorldGenFactory.a(WorldGenNetherPiece4.class, "NeCCS");
    WorldGenFactory.a(WorldGenNetherPiece5.class, "NeCTB");
    WorldGenFactory.a(WorldGenNetherPiece6.class, "NeCE");
    WorldGenFactory.a(WorldGenNetherPiece7.class, "NeSCSC");
    WorldGenFactory.a(WorldGenNetherPiece8.class, "NeSCLT");
    WorldGenFactory.a(WorldGenNetherPiece9.class, "NeSC");
    WorldGenFactory.a(WorldGenNetherPiece10.class, "NeSCRT");
    WorldGenFactory.a(WorldGenNetherPiece11.class, "NeCSR");
    WorldGenFactory.a(WorldGenNetherPiece12.class, "NeMT");
    WorldGenFactory.a(WorldGenNetherPiece13.class, "NeRC");
    WorldGenFactory.a(WorldGenNetherPiece14.class, "NeSR");
    WorldGenFactory.a(WorldGenNetherPiece15.class, "NeStart");
  }
  
  static class WorldGenNetherPieceWeight
  {
    public Class<? extends WorldGenNetherPieces.WorldGenNetherPiece> a;
    public final int b;
    public int c;
    public int d;
    public boolean e;
    
    public WorldGenNetherPieceWeight(Class<? extends WorldGenNetherPieces.WorldGenNetherPiece> ☃, int ☃, int ☃, boolean ☃)
    {
      this.a = ☃;
      this.b = ☃;
      this.d = ☃;
      this.e = ☃;
    }
    
    public WorldGenNetherPieceWeight(Class<? extends WorldGenNetherPieces.WorldGenNetherPiece> ☃, int ☃, int ☃)
    {
      this(☃, ☃, ☃, false);
    }
    
    public boolean a(int ☃)
    {
      return (this.d == 0) || (this.c < this.d);
    }
    
    public boolean a()
    {
      return (this.d == 0) || (this.c < this.d);
    }
  }
  
  private static final WorldGenNetherPieceWeight[] a = { new WorldGenNetherPieceWeight(WorldGenNetherPiece3.class, 30, 0, true), new WorldGenNetherPieceWeight(WorldGenNetherPiece1.class, 10, 4), new WorldGenNetherPieceWeight(WorldGenNetherPiece13.class, 10, 4), new WorldGenNetherPieceWeight(WorldGenNetherPiece14.class, 10, 3), new WorldGenNetherPieceWeight(WorldGenNetherPiece12.class, 5, 2), new WorldGenNetherPieceWeight(WorldGenNetherPiece6.class, 5, 1) };
  private static final WorldGenNetherPieceWeight[] b = { new WorldGenNetherPieceWeight(WorldGenNetherPiece9.class, 25, 0, true), new WorldGenNetherPieceWeight(WorldGenNetherPiece7.class, 15, 5), new WorldGenNetherPieceWeight(WorldGenNetherPiece10.class, 5, 10), new WorldGenNetherPieceWeight(WorldGenNetherPiece8.class, 5, 10), new WorldGenNetherPieceWeight(WorldGenNetherPiece4.class, 10, 3, true), new WorldGenNetherPieceWeight(WorldGenNetherPiece5.class, 7, 2), new WorldGenNetherPieceWeight(WorldGenNetherPiece11.class, 5, 2) };
  
  private static WorldGenNetherPiece b(WorldGenNetherPieceWeight ☃, List<StructurePiece> ☃, Random ☃, int ☃, int ☃, int ☃, EnumDirection ☃, int ☃)
  {
    Class<? extends WorldGenNetherPiece> ☃ = ☃.a;
    WorldGenNetherPiece ☃ = null;
    if (☃ == WorldGenNetherPiece3.class) {
      ☃ = WorldGenNetherPiece3.a(☃, ☃, ☃, ☃, ☃, ☃, ☃);
    } else if (☃ == WorldGenNetherPiece1.class) {
      ☃ = WorldGenNetherPiece1.a(☃, ☃, ☃, ☃, ☃, ☃, ☃);
    } else if (☃ == WorldGenNetherPiece13.class) {
      ☃ = WorldGenNetherPiece13.a(☃, ☃, ☃, ☃, ☃, ☃, ☃);
    } else if (☃ == WorldGenNetherPiece14.class) {
      ☃ = WorldGenNetherPiece14.a(☃, ☃, ☃, ☃, ☃, ☃, ☃);
    } else if (☃ == WorldGenNetherPiece12.class) {
      ☃ = WorldGenNetherPiece12.a(☃, ☃, ☃, ☃, ☃, ☃, ☃);
    } else if (☃ == WorldGenNetherPiece6.class) {
      ☃ = WorldGenNetherPiece6.a(☃, ☃, ☃, ☃, ☃, ☃, ☃);
    } else if (☃ == WorldGenNetherPiece9.class) {
      ☃ = WorldGenNetherPiece9.a(☃, ☃, ☃, ☃, ☃, ☃, ☃);
    } else if (☃ == WorldGenNetherPiece10.class) {
      ☃ = WorldGenNetherPiece10.a(☃, ☃, ☃, ☃, ☃, ☃, ☃);
    } else if (☃ == WorldGenNetherPiece8.class) {
      ☃ = WorldGenNetherPiece8.a(☃, ☃, ☃, ☃, ☃, ☃, ☃);
    } else if (☃ == WorldGenNetherPiece4.class) {
      ☃ = WorldGenNetherPiece4.a(☃, ☃, ☃, ☃, ☃, ☃, ☃);
    } else if (☃ == WorldGenNetherPiece5.class) {
      ☃ = WorldGenNetherPiece5.a(☃, ☃, ☃, ☃, ☃, ☃, ☃);
    } else if (☃ == WorldGenNetherPiece7.class) {
      ☃ = WorldGenNetherPiece7.a(☃, ☃, ☃, ☃, ☃, ☃, ☃);
    } else if (☃ == WorldGenNetherPiece11.class) {
      ☃ = WorldGenNetherPiece11.a(☃, ☃, ☃, ☃, ☃, ☃, ☃);
    }
    return ☃;
  }
  
  static abstract class WorldGenNetherPiece
    extends StructurePiece
  {
    protected static final List<StructurePieceTreasure> a = Lists.newArrayList(new StructurePieceTreasure[] { new StructurePieceTreasure(Items.DIAMOND, 0, 1, 3, 5), new StructurePieceTreasure(Items.IRON_INGOT, 0, 1, 5, 5), new StructurePieceTreasure(Items.GOLD_INGOT, 0, 1, 3, 15), new StructurePieceTreasure(Items.GOLDEN_SWORD, 0, 1, 1, 5), new StructurePieceTreasure(Items.GOLDEN_CHESTPLATE, 0, 1, 1, 5), new StructurePieceTreasure(Items.FLINT_AND_STEEL, 0, 1, 1, 5), new StructurePieceTreasure(Items.NETHER_WART, 0, 3, 7, 5), new StructurePieceTreasure(Items.SADDLE, 0, 1, 1, 10), new StructurePieceTreasure(Items.GOLDEN_HORSE_ARMOR, 0, 1, 1, 8), new StructurePieceTreasure(Items.IRON_HORSE_ARMOR, 0, 1, 1, 5), new StructurePieceTreasure(Items.DIAMOND_HORSE_ARMOR, 0, 1, 1, 3), new StructurePieceTreasure(Item.getItemOf(Blocks.OBSIDIAN), 0, 2, 4, 2) });
    
    public WorldGenNetherPiece() {}
    
    protected WorldGenNetherPiece(int ☃)
    {
      super();
    }
    
    protected void b(NBTTagCompound ☃) {}
    
    protected void a(NBTTagCompound ☃) {}
    
    private int a(List<WorldGenNetherPieces.WorldGenNetherPieceWeight> ☃)
    {
      boolean ☃ = false;
      int ☃ = 0;
      for (WorldGenNetherPieces.WorldGenNetherPieceWeight ☃ : ☃)
      {
        if ((☃.d > 0) && (☃.c < ☃.d)) {
          ☃ = true;
        }
        ☃ += ☃.b;
      }
      return ☃ ? ☃ : -1;
    }
    
    private WorldGenNetherPiece a(WorldGenNetherPieces.WorldGenNetherPiece15 ☃, List<WorldGenNetherPieces.WorldGenNetherPieceWeight> ☃, List<StructurePiece> ☃, Random ☃, int ☃, int ☃, int ☃, EnumDirection ☃, int ☃)
    {
      int ☃ = a(☃);
      boolean ☃ = (☃ > 0) && (☃ <= 30);
      
      int ☃ = 0;
      int ☃;
      while ((☃ < 5) && (☃))
      {
        ☃++;
        
        ☃ = ☃.nextInt(☃);
        for (WorldGenNetherPieces.WorldGenNetherPieceWeight ☃ : ☃)
        {
          ☃ -= ☃.b;
          if (☃ < 0)
          {
            if ((!☃.a(☃)) || ((☃ == ☃.b) && (!☃.e))) {
              break;
            }
            WorldGenNetherPiece ☃ = WorldGenNetherPieces.a(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
            if (☃ != null)
            {
              ☃.c += 1;
              ☃.b = ☃;
              if (!☃.a()) {
                ☃.remove(☃);
              }
              return ☃;
            }
          }
        }
      }
      return WorldGenNetherPieces.WorldGenNetherPiece2.a(☃, ☃, ☃, ☃, ☃, ☃, ☃);
    }
    
    private StructurePiece a(WorldGenNetherPieces.WorldGenNetherPiece15 ☃, List<StructurePiece> ☃, Random ☃, int ☃, int ☃, int ☃, EnumDirection ☃, int ☃, boolean ☃)
    {
      if ((Math.abs(☃ - ☃.c().a) > 112) || (Math.abs(☃ - ☃.c().c) > 112)) {
        return WorldGenNetherPieces.WorldGenNetherPiece2.a(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      }
      List<WorldGenNetherPieces.WorldGenNetherPieceWeight> ☃ = ☃.c;
      if (☃) {
        ☃ = ☃.d;
      }
      StructurePiece ☃ = a(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃ + 1);
      if (☃ != null)
      {
        ☃.add(☃);
        ☃.e.add(☃);
      }
      return ☃;
    }
    
    protected StructurePiece a(WorldGenNetherPieces.WorldGenNetherPiece15 ☃, List<StructurePiece> ☃, Random ☃, int ☃, int ☃, boolean ☃)
    {
      if (this.m != null) {
        switch (WorldGenNetherPieces.1.a[this.m.ordinal()])
        {
        case 1: 
          return a(☃, ☃, ☃, this.l.a + ☃, this.l.b + ☃, this.l.c - 1, this.m, d(), ☃);
        case 2: 
          return a(☃, ☃, ☃, this.l.a + ☃, this.l.b + ☃, this.l.f + 1, this.m, d(), ☃);
        case 3: 
          return a(☃, ☃, ☃, this.l.a - 1, this.l.b + ☃, this.l.c + ☃, this.m, d(), ☃);
        case 4: 
          return a(☃, ☃, ☃, this.l.d + 1, this.l.b + ☃, this.l.c + ☃, this.m, d(), ☃);
        }
      }
      return null;
    }
    
    protected StructurePiece b(WorldGenNetherPieces.WorldGenNetherPiece15 ☃, List<StructurePiece> ☃, Random ☃, int ☃, int ☃, boolean ☃)
    {
      if (this.m != null) {
        switch (WorldGenNetherPieces.1.a[this.m.ordinal()])
        {
        case 1: 
          return a(☃, ☃, ☃, this.l.a - 1, this.l.b + ☃, this.l.c + ☃, EnumDirection.WEST, d(), ☃);
        case 2: 
          return a(☃, ☃, ☃, this.l.a - 1, this.l.b + ☃, this.l.c + ☃, EnumDirection.WEST, d(), ☃);
        case 3: 
          return a(☃, ☃, ☃, this.l.a + ☃, this.l.b + ☃, this.l.c - 1, EnumDirection.NORTH, d(), ☃);
        case 4: 
          return a(☃, ☃, ☃, this.l.a + ☃, this.l.b + ☃, this.l.c - 1, EnumDirection.NORTH, d(), ☃);
        }
      }
      return null;
    }
    
    protected StructurePiece c(WorldGenNetherPieces.WorldGenNetherPiece15 ☃, List<StructurePiece> ☃, Random ☃, int ☃, int ☃, boolean ☃)
    {
      if (this.m != null) {
        switch (WorldGenNetherPieces.1.a[this.m.ordinal()])
        {
        case 1: 
          return a(☃, ☃, ☃, this.l.d + 1, this.l.b + ☃, this.l.c + ☃, EnumDirection.EAST, d(), ☃);
        case 2: 
          return a(☃, ☃, ☃, this.l.d + 1, this.l.b + ☃, this.l.c + ☃, EnumDirection.EAST, d(), ☃);
        case 3: 
          return a(☃, ☃, ☃, this.l.a + ☃, this.l.b + ☃, this.l.f + 1, EnumDirection.SOUTH, d(), ☃);
        case 4: 
          return a(☃, ☃, ☃, this.l.a + ☃, this.l.b + ☃, this.l.f + 1, EnumDirection.SOUTH, d(), ☃);
        }
      }
      return null;
    }
    
    protected static boolean a(StructureBoundingBox ☃)
    {
      return (☃ != null) && (☃.b > 10);
    }
  }
  
  public static class WorldGenNetherPiece15
    extends WorldGenNetherPieces.WorldGenNetherPiece1
  {
    public WorldGenNetherPieces.WorldGenNetherPieceWeight b;
    public List<WorldGenNetherPieces.WorldGenNetherPieceWeight> c;
    public List<WorldGenNetherPieces.WorldGenNetherPieceWeight> d;
    public List<StructurePiece> e = Lists.newArrayList();
    
    public WorldGenNetherPiece15() {}
    
    public WorldGenNetherPiece15(Random ☃, int ☃, int ☃)
    {
      super(☃, ☃);
      
      this.c = Lists.newArrayList();
      for (WorldGenNetherPieces.WorldGenNetherPieceWeight ☃ : WorldGenNetherPieces.b())
      {
        ☃.c = 0;
        this.c.add(☃);
      }
      this.d = Lists.newArrayList();
      for (WorldGenNetherPieces.WorldGenNetherPieceWeight ☃ : WorldGenNetherPieces.c())
      {
        ☃.c = 0;
        this.d.add(☃);
      }
    }
    
    protected void b(NBTTagCompound ☃)
    {
      super.b(☃);
    }
    
    protected void a(NBTTagCompound ☃)
    {
      super.a(☃);
    }
  }
  
  public static class WorldGenNetherPiece3
    extends WorldGenNetherPieces.WorldGenNetherPiece
  {
    public WorldGenNetherPiece3() {}
    
    public WorldGenNetherPiece3(int ☃, Random ☃, StructureBoundingBox ☃, EnumDirection ☃)
    {
      super();
      
      this.m = ☃;
      this.l = ☃;
    }
    
    public void a(StructurePiece ☃, List<StructurePiece> ☃, Random ☃)
    {
      a((WorldGenNetherPieces.WorldGenNetherPiece15)☃, ☃, ☃, 1, 3, false);
    }
    
    public static WorldGenNetherPiece3 a(List<StructurePiece> ☃, Random ☃, int ☃, int ☃, int ☃, EnumDirection ☃, int ☃)
    {
      StructureBoundingBox ☃ = StructureBoundingBox.a(☃, ☃, ☃, -1, -3, 0, 5, 10, 19, ☃);
      if ((!a(☃)) || (StructurePiece.a(☃, ☃) != null)) {
        return null;
      }
      return new WorldGenNetherPiece3(☃, ☃, ☃, ☃);
    }
    
    public boolean a(World ☃, Random ☃, StructureBoundingBox ☃)
    {
      a(☃, ☃, 0, 3, 0, 4, 4, 18, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      
      a(☃, ☃, 1, 5, 0, 3, 7, 18, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
      
      a(☃, ☃, 0, 5, 0, 0, 5, 18, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 4, 5, 0, 4, 5, 18, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      
      a(☃, ☃, 0, 2, 0, 4, 2, 5, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 0, 2, 13, 4, 2, 18, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 0, 0, 0, 4, 1, 3, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 0, 0, 15, 4, 1, 18, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      for (int ☃ = 0; ☃ <= 4; ☃++) {
        for (int ☃ = 0; ☃ <= 2; ☃++)
        {
          b(☃, Blocks.NETHER_BRICK.getBlockData(), ☃, -1, ☃, ☃);
          b(☃, Blocks.NETHER_BRICK.getBlockData(), ☃, -1, 18 - ☃, ☃);
        }
      }
      a(☃, ☃, 0, 1, 1, 0, 4, 1, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
      a(☃, ☃, 0, 3, 4, 0, 4, 4, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
      a(☃, ☃, 0, 3, 14, 0, 4, 14, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
      a(☃, ☃, 0, 1, 17, 0, 4, 17, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
      a(☃, ☃, 4, 1, 1, 4, 4, 1, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
      a(☃, ☃, 4, 3, 4, 4, 4, 4, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
      a(☃, ☃, 4, 3, 14, 4, 4, 14, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
      a(☃, ☃, 4, 1, 17, 4, 4, 17, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
      
      return true;
    }
  }
  
  public static class WorldGenNetherPiece2
    extends WorldGenNetherPieces.WorldGenNetherPiece
  {
    private int b;
    
    public WorldGenNetherPiece2() {}
    
    public WorldGenNetherPiece2(int ☃, Random ☃, StructureBoundingBox ☃, EnumDirection ☃)
    {
      super();
      
      this.m = ☃;
      this.l = ☃;
      this.b = ☃.nextInt();
    }
    
    public static WorldGenNetherPiece2 a(List<StructurePiece> ☃, Random ☃, int ☃, int ☃, int ☃, EnumDirection ☃, int ☃)
    {
      StructureBoundingBox ☃ = StructureBoundingBox.a(☃, ☃, ☃, -1, -3, 0, 5, 10, 8, ☃);
      if ((!a(☃)) || (StructurePiece.a(☃, ☃) != null)) {
        return null;
      }
      return new WorldGenNetherPiece2(☃, ☃, ☃, ☃);
    }
    
    protected void b(NBTTagCompound ☃)
    {
      super.b(☃);
      
      this.b = ☃.getInt("Seed");
    }
    
    protected void a(NBTTagCompound ☃)
    {
      super.a(☃);
      
      ☃.setInt("Seed", this.b);
    }
    
    public boolean a(World ☃, Random ☃, StructureBoundingBox ☃)
    {
      Random ☃ = new Random(this.b);
      for (int ☃ = 0; ☃ <= 4; ☃++) {
        for (int ☃ = 3; ☃ <= 4; ☃++)
        {
          int ☃ = ☃.nextInt(8);
          a(☃, ☃, ☃, ☃, 0, ☃, ☃, ☃, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
        }
      }
      int ☃ = ☃.nextInt(8);
      a(☃, ☃, 0, 5, 0, 0, 5, ☃, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      
      int ☃ = ☃.nextInt(8);
      a(☃, ☃, 4, 5, 0, 4, 5, ☃, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      for (int ☃ = 0; ☃ <= 4; ☃++)
      {
        int ☃ = ☃.nextInt(5);
        a(☃, ☃, ☃, 2, 0, ☃, 2, ☃, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      }
      for (int ☃ = 0; ☃ <= 4; ☃++) {
        for (int ☃ = 0; ☃ <= 1; ☃++)
        {
          int ☃ = ☃.nextInt(3);
          a(☃, ☃, ☃, ☃, 0, ☃, ☃, ☃, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
        }
      }
      return true;
    }
  }
  
  public static class WorldGenNetherPiece1
    extends WorldGenNetherPieces.WorldGenNetherPiece
  {
    public WorldGenNetherPiece1() {}
    
    public WorldGenNetherPiece1(int ☃, Random ☃, StructureBoundingBox ☃, EnumDirection ☃)
    {
      super();
      
      this.m = ☃;
      this.l = ☃;
    }
    
    protected WorldGenNetherPiece1(Random ☃, int ☃, int ☃)
    {
      super();
      
      this.m = EnumDirection.EnumDirectionLimit.HORIZONTAL.a(☃);
      switch (WorldGenNetherPieces.1.a[this.m.ordinal()])
      {
      case 1: 
      case 2: 
        this.l = new StructureBoundingBox(☃, 64, ☃, ☃ + 19 - 1, 73, ☃ + 19 - 1);
        break;
      default: 
        this.l = new StructureBoundingBox(☃, 64, ☃, ☃ + 19 - 1, 73, ☃ + 19 - 1);
      }
    }
    
    public void a(StructurePiece ☃, List<StructurePiece> ☃, Random ☃)
    {
      a((WorldGenNetherPieces.WorldGenNetherPiece15)☃, ☃, ☃, 8, 3, false);
      b((WorldGenNetherPieces.WorldGenNetherPiece15)☃, ☃, ☃, 3, 8, false);
      c((WorldGenNetherPieces.WorldGenNetherPiece15)☃, ☃, ☃, 3, 8, false);
    }
    
    public static WorldGenNetherPiece1 a(List<StructurePiece> ☃, Random ☃, int ☃, int ☃, int ☃, EnumDirection ☃, int ☃)
    {
      StructureBoundingBox ☃ = StructureBoundingBox.a(☃, ☃, ☃, -8, -3, 0, 19, 10, 19, ☃);
      if ((!a(☃)) || (StructurePiece.a(☃, ☃) != null)) {
        return null;
      }
      return new WorldGenNetherPiece1(☃, ☃, ☃, ☃);
    }
    
    public boolean a(World ☃, Random ☃, StructureBoundingBox ☃)
    {
      a(☃, ☃, 7, 3, 0, 11, 4, 18, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 0, 3, 7, 18, 4, 11, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      
      a(☃, ☃, 8, 5, 0, 10, 7, 18, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
      a(☃, ☃, 0, 5, 8, 18, 7, 10, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
      
      a(☃, ☃, 7, 5, 0, 7, 5, 7, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 7, 5, 11, 7, 5, 18, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 11, 5, 0, 11, 5, 7, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 11, 5, 11, 11, 5, 18, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 0, 5, 7, 7, 5, 7, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 11, 5, 7, 18, 5, 7, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 0, 5, 11, 7, 5, 11, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 11, 5, 11, 18, 5, 11, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      
      a(☃, ☃, 7, 2, 0, 11, 2, 5, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 7, 2, 13, 11, 2, 18, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 7, 0, 0, 11, 1, 3, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 7, 0, 15, 11, 1, 18, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      for (int ☃ = 7; ☃ <= 11; ☃++) {
        for (int ☃ = 0; ☃ <= 2; ☃++)
        {
          b(☃, Blocks.NETHER_BRICK.getBlockData(), ☃, -1, ☃, ☃);
          b(☃, Blocks.NETHER_BRICK.getBlockData(), ☃, -1, 18 - ☃, ☃);
        }
      }
      a(☃, ☃, 0, 2, 7, 5, 2, 11, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 13, 2, 7, 18, 2, 11, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 0, 0, 7, 3, 1, 11, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 15, 0, 7, 18, 1, 11, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      for (int ☃ = 0; ☃ <= 2; ☃++) {
        for (int ☃ = 7; ☃ <= 11; ☃++)
        {
          b(☃, Blocks.NETHER_BRICK.getBlockData(), ☃, -1, ☃, ☃);
          b(☃, Blocks.NETHER_BRICK.getBlockData(), 18 - ☃, -1, ☃, ☃);
        }
      }
      return true;
    }
  }
  
  public static class WorldGenNetherPiece13
    extends WorldGenNetherPieces.WorldGenNetherPiece
  {
    public WorldGenNetherPiece13() {}
    
    public WorldGenNetherPiece13(int ☃, Random ☃, StructureBoundingBox ☃, EnumDirection ☃)
    {
      super();
      
      this.m = ☃;
      this.l = ☃;
    }
    
    public void a(StructurePiece ☃, List<StructurePiece> ☃, Random ☃)
    {
      a((WorldGenNetherPieces.WorldGenNetherPiece15)☃, ☃, ☃, 2, 0, false);
      b((WorldGenNetherPieces.WorldGenNetherPiece15)☃, ☃, ☃, 0, 2, false);
      c((WorldGenNetherPieces.WorldGenNetherPiece15)☃, ☃, ☃, 0, 2, false);
    }
    
    public static WorldGenNetherPiece13 a(List<StructurePiece> ☃, Random ☃, int ☃, int ☃, int ☃, EnumDirection ☃, int ☃)
    {
      StructureBoundingBox ☃ = StructureBoundingBox.a(☃, ☃, ☃, -2, 0, 0, 7, 9, 7, ☃);
      if ((!a(☃)) || (StructurePiece.a(☃, ☃) != null)) {
        return null;
      }
      return new WorldGenNetherPiece13(☃, ☃, ☃, ☃);
    }
    
    public boolean a(World ☃, Random ☃, StructureBoundingBox ☃)
    {
      a(☃, ☃, 0, 0, 0, 6, 1, 6, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      
      a(☃, ☃, 0, 2, 0, 6, 7, 6, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
      
      a(☃, ☃, 0, 2, 0, 1, 6, 0, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 0, 2, 6, 1, 6, 6, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 5, 2, 0, 6, 6, 0, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 5, 2, 6, 6, 6, 6, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 0, 2, 0, 0, 6, 1, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 0, 2, 5, 0, 6, 6, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 6, 2, 0, 6, 6, 1, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 6, 2, 5, 6, 6, 6, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      
      a(☃, ☃, 2, 6, 0, 4, 6, 0, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 2, 5, 0, 4, 5, 0, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
      a(☃, ☃, 2, 6, 6, 4, 6, 6, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 2, 5, 6, 4, 5, 6, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
      a(☃, ☃, 0, 6, 2, 0, 6, 4, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 0, 5, 2, 0, 5, 4, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
      a(☃, ☃, 6, 6, 2, 6, 6, 4, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 6, 5, 2, 6, 5, 4, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
      for (int ☃ = 0; ☃ <= 6; ☃++) {
        for (int ☃ = 0; ☃ <= 6; ☃++) {
          b(☃, Blocks.NETHER_BRICK.getBlockData(), ☃, -1, ☃, ☃);
        }
      }
      return true;
    }
  }
  
  public static class WorldGenNetherPiece14
    extends WorldGenNetherPieces.WorldGenNetherPiece
  {
    public WorldGenNetherPiece14() {}
    
    public WorldGenNetherPiece14(int ☃, Random ☃, StructureBoundingBox ☃, EnumDirection ☃)
    {
      super();
      
      this.m = ☃;
      this.l = ☃;
    }
    
    public void a(StructurePiece ☃, List<StructurePiece> ☃, Random ☃)
    {
      c((WorldGenNetherPieces.WorldGenNetherPiece15)☃, ☃, ☃, 6, 2, false);
    }
    
    public static WorldGenNetherPiece14 a(List<StructurePiece> ☃, Random ☃, int ☃, int ☃, int ☃, int ☃, EnumDirection ☃)
    {
      StructureBoundingBox ☃ = StructureBoundingBox.a(☃, ☃, ☃, -2, 0, 0, 7, 11, 7, ☃);
      if ((!a(☃)) || (StructurePiece.a(☃, ☃) != null)) {
        return null;
      }
      return new WorldGenNetherPiece14(☃, ☃, ☃, ☃);
    }
    
    public boolean a(World ☃, Random ☃, StructureBoundingBox ☃)
    {
      a(☃, ☃, 0, 0, 0, 6, 1, 6, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      
      a(☃, ☃, 0, 2, 0, 6, 10, 6, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
      
      a(☃, ☃, 0, 2, 0, 1, 8, 0, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 5, 2, 0, 6, 8, 0, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 0, 2, 1, 0, 8, 6, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 6, 2, 1, 6, 8, 6, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 1, 2, 6, 5, 8, 6, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      
      a(☃, ☃, 0, 3, 2, 0, 5, 4, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
      a(☃, ☃, 6, 3, 2, 6, 5, 2, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
      a(☃, ☃, 6, 3, 4, 6, 5, 4, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
      
      a(☃, Blocks.NETHER_BRICK.getBlockData(), 5, 2, 5, ☃);
      a(☃, ☃, 4, 2, 5, 4, 3, 5, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 3, 2, 5, 3, 4, 5, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 2, 2, 5, 2, 5, 5, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 1, 2, 5, 1, 6, 5, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      
      a(☃, ☃, 1, 7, 1, 5, 7, 4, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 6, 8, 2, 6, 8, 4, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
      
      a(☃, ☃, 2, 6, 0, 4, 8, 0, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 2, 5, 0, 4, 5, 0, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
      for (int ☃ = 0; ☃ <= 6; ☃++) {
        for (int ☃ = 0; ☃ <= 6; ☃++) {
          b(☃, Blocks.NETHER_BRICK.getBlockData(), ☃, -1, ☃, ☃);
        }
      }
      return true;
    }
  }
  
  public static class WorldGenNetherPiece12
    extends WorldGenNetherPieces.WorldGenNetherPiece
  {
    private boolean b;
    
    public WorldGenNetherPiece12() {}
    
    public WorldGenNetherPiece12(int ☃, Random ☃, StructureBoundingBox ☃, EnumDirection ☃)
    {
      super();
      
      this.m = ☃;
      this.l = ☃;
    }
    
    protected void b(NBTTagCompound ☃)
    {
      super.b(☃);
      
      this.b = ☃.getBoolean("Mob");
    }
    
    protected void a(NBTTagCompound ☃)
    {
      super.a(☃);
      
      ☃.setBoolean("Mob", this.b);
    }
    
    public static WorldGenNetherPiece12 a(List<StructurePiece> ☃, Random ☃, int ☃, int ☃, int ☃, int ☃, EnumDirection ☃)
    {
      StructureBoundingBox ☃ = StructureBoundingBox.a(☃, ☃, ☃, -2, 0, 0, 7, 8, 9, ☃);
      if ((!a(☃)) || (StructurePiece.a(☃, ☃) != null)) {
        return null;
      }
      return new WorldGenNetherPiece12(☃, ☃, ☃, ☃);
    }
    
    public boolean a(World ☃, Random ☃, StructureBoundingBox ☃)
    {
      a(☃, ☃, 0, 2, 0, 6, 7, 7, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
      
      a(☃, ☃, 1, 0, 0, 5, 1, 7, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 1, 2, 1, 5, 2, 7, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 1, 3, 2, 5, 3, 7, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 1, 4, 3, 5, 4, 7, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      
      a(☃, ☃, 1, 2, 0, 1, 4, 2, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 5, 2, 0, 5, 4, 2, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 1, 5, 2, 1, 5, 3, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 5, 5, 2, 5, 5, 3, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 0, 5, 3, 0, 5, 8, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 6, 5, 3, 6, 5, 8, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 1, 5, 8, 5, 5, 8, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      
      a(☃, Blocks.NETHER_BRICK_FENCE.getBlockData(), 1, 6, 3, ☃);
      a(☃, Blocks.NETHER_BRICK_FENCE.getBlockData(), 5, 6, 3, ☃);
      a(☃, ☃, 0, 6, 3, 0, 6, 8, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
      a(☃, ☃, 6, 6, 3, 6, 6, 8, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
      a(☃, ☃, 1, 6, 8, 5, 7, 8, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
      a(☃, ☃, 2, 8, 8, 4, 8, 8, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
      if (!this.b)
      {
        BlockPosition ☃ = new BlockPosition(a(3, 5), d(5), b(3, 5));
        if (☃.b(☃))
        {
          this.b = true;
          ☃.setTypeAndData(☃, Blocks.MOB_SPAWNER.getBlockData(), 2);
          
          TileEntity ☃ = ☃.getTileEntity(☃);
          if ((☃ instanceof TileEntityMobSpawner)) {
            ((TileEntityMobSpawner)☃).getSpawner().setMobName("Blaze");
          }
        }
      }
      for (int ☃ = 0; ☃ <= 6; ☃++) {
        for (int ☃ = 0; ☃ <= 6; ☃++) {
          b(☃, Blocks.NETHER_BRICK.getBlockData(), ☃, -1, ☃, ☃);
        }
      }
      return true;
    }
  }
  
  public static class WorldGenNetherPiece6
    extends WorldGenNetherPieces.WorldGenNetherPiece
  {
    public WorldGenNetherPiece6() {}
    
    public WorldGenNetherPiece6(int ☃, Random ☃, StructureBoundingBox ☃, EnumDirection ☃)
    {
      super();
      
      this.m = ☃;
      this.l = ☃;
    }
    
    public void a(StructurePiece ☃, List<StructurePiece> ☃, Random ☃)
    {
      a((WorldGenNetherPieces.WorldGenNetherPiece15)☃, ☃, ☃, 5, 3, true);
    }
    
    public static WorldGenNetherPiece6 a(List<StructurePiece> ☃, Random ☃, int ☃, int ☃, int ☃, EnumDirection ☃, int ☃)
    {
      StructureBoundingBox ☃ = StructureBoundingBox.a(☃, ☃, ☃, -5, -3, 0, 13, 14, 13, ☃);
      if ((!a(☃)) || (StructurePiece.a(☃, ☃) != null)) {
        return null;
      }
      return new WorldGenNetherPiece6(☃, ☃, ☃, ☃);
    }
    
    public boolean a(World ☃, Random ☃, StructureBoundingBox ☃)
    {
      a(☃, ☃, 0, 3, 0, 12, 4, 12, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      
      a(☃, ☃, 0, 5, 0, 12, 13, 12, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
      
      a(☃, ☃, 0, 5, 0, 1, 12, 12, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 11, 5, 0, 12, 12, 12, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 2, 5, 11, 4, 12, 12, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 8, 5, 11, 10, 12, 12, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 5, 9, 11, 7, 12, 12, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 2, 5, 0, 4, 12, 1, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 8, 5, 0, 10, 12, 1, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 5, 9, 0, 7, 12, 1, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      
      a(☃, ☃, 2, 11, 2, 10, 12, 10, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      
      a(☃, ☃, 5, 8, 0, 7, 8, 0, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
      for (int ☃ = 1; ☃ <= 11; ☃ += 2)
      {
        a(☃, ☃, ☃, 10, 0, ☃, 11, 0, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
        a(☃, ☃, ☃, 10, 12, ☃, 11, 12, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
        a(☃, ☃, 0, 10, ☃, 0, 11, ☃, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
        a(☃, ☃, 12, 10, ☃, 12, 11, ☃, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
        a(☃, Blocks.NETHER_BRICK.getBlockData(), ☃, 13, 0, ☃);
        a(☃, Blocks.NETHER_BRICK.getBlockData(), ☃, 13, 12, ☃);
        a(☃, Blocks.NETHER_BRICK.getBlockData(), 0, 13, ☃, ☃);
        a(☃, Blocks.NETHER_BRICK.getBlockData(), 12, 13, ☃, ☃);
        a(☃, Blocks.NETHER_BRICK_FENCE.getBlockData(), ☃ + 1, 13, 0, ☃);
        a(☃, Blocks.NETHER_BRICK_FENCE.getBlockData(), ☃ + 1, 13, 12, ☃);
        a(☃, Blocks.NETHER_BRICK_FENCE.getBlockData(), 0, 13, ☃ + 1, ☃);
        a(☃, Blocks.NETHER_BRICK_FENCE.getBlockData(), 12, 13, ☃ + 1, ☃);
      }
      a(☃, Blocks.NETHER_BRICK_FENCE.getBlockData(), 0, 13, 0, ☃);
      a(☃, Blocks.NETHER_BRICK_FENCE.getBlockData(), 0, 13, 12, ☃);
      a(☃, Blocks.NETHER_BRICK_FENCE.getBlockData(), 0, 13, 0, ☃);
      a(☃, Blocks.NETHER_BRICK_FENCE.getBlockData(), 12, 13, 0, ☃);
      for (int ☃ = 3; ☃ <= 9; ☃ += 2)
      {
        a(☃, ☃, 1, 7, ☃, 1, 8, ☃, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
        a(☃, ☃, 11, 7, ☃, 11, 8, ☃, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
      }
      a(☃, ☃, 4, 2, 0, 8, 2, 12, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 0, 2, 4, 12, 2, 8, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      
      a(☃, ☃, 4, 0, 0, 8, 1, 3, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 4, 0, 9, 8, 1, 12, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 0, 0, 4, 3, 1, 8, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 9, 0, 4, 12, 1, 8, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      for (int ☃ = 4; ☃ <= 8; ☃++) {
        for (int ☃ = 0; ☃ <= 2; ☃++)
        {
          b(☃, Blocks.NETHER_BRICK.getBlockData(), ☃, -1, ☃, ☃);
          b(☃, Blocks.NETHER_BRICK.getBlockData(), ☃, -1, 12 - ☃, ☃);
        }
      }
      for (int ☃ = 0; ☃ <= 2; ☃++) {
        for (int ☃ = 4; ☃ <= 8; ☃++)
        {
          b(☃, Blocks.NETHER_BRICK.getBlockData(), ☃, -1, ☃, ☃);
          b(☃, Blocks.NETHER_BRICK.getBlockData(), 12 - ☃, -1, ☃, ☃);
        }
      }
      a(☃, ☃, 5, 5, 5, 7, 5, 7, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 6, 1, 6, 6, 4, 6, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
      a(☃, Blocks.NETHER_BRICK.getBlockData(), 6, 0, 6, ☃);
      a(☃, Blocks.FLOWING_LAVA.getBlockData(), 6, 5, 6, ☃);
      
      BlockPosition ☃ = new BlockPosition(a(6, 6), d(5), b(6, 6));
      if (☃.b(☃)) {
        ☃.a(Blocks.FLOWING_LAVA, ☃, ☃);
      }
      return true;
    }
  }
  
  public static class WorldGenNetherPiece11
    extends WorldGenNetherPieces.WorldGenNetherPiece
  {
    public WorldGenNetherPiece11() {}
    
    public WorldGenNetherPiece11(int ☃, Random ☃, StructureBoundingBox ☃, EnumDirection ☃)
    {
      super();
      
      this.m = ☃;
      this.l = ☃;
    }
    
    public void a(StructurePiece ☃, List<StructurePiece> ☃, Random ☃)
    {
      a((WorldGenNetherPieces.WorldGenNetherPiece15)☃, ☃, ☃, 5, 3, true);
      a((WorldGenNetherPieces.WorldGenNetherPiece15)☃, ☃, ☃, 5, 11, true);
    }
    
    public static WorldGenNetherPiece11 a(List<StructurePiece> ☃, Random ☃, int ☃, int ☃, int ☃, EnumDirection ☃, int ☃)
    {
      StructureBoundingBox ☃ = StructureBoundingBox.a(☃, ☃, ☃, -5, -3, 0, 13, 14, 13, ☃);
      if ((!a(☃)) || (StructurePiece.a(☃, ☃) != null)) {
        return null;
      }
      return new WorldGenNetherPiece11(☃, ☃, ☃, ☃);
    }
    
    public boolean a(World ☃, Random ☃, StructureBoundingBox ☃)
    {
      a(☃, ☃, 0, 3, 0, 12, 4, 12, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      
      a(☃, ☃, 0, 5, 0, 12, 13, 12, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
      
      a(☃, ☃, 0, 5, 0, 1, 12, 12, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 11, 5, 0, 12, 12, 12, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 2, 5, 11, 4, 12, 12, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 8, 5, 11, 10, 12, 12, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 5, 9, 11, 7, 12, 12, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 2, 5, 0, 4, 12, 1, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 8, 5, 0, 10, 12, 1, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 5, 9, 0, 7, 12, 1, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      
      a(☃, ☃, 2, 11, 2, 10, 12, 10, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      for (int ☃ = 1; ☃ <= 11; ☃ += 2)
      {
        a(☃, ☃, ☃, 10, 0, ☃, 11, 0, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
        a(☃, ☃, ☃, 10, 12, ☃, 11, 12, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
        a(☃, ☃, 0, 10, ☃, 0, 11, ☃, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
        a(☃, ☃, 12, 10, ☃, 12, 11, ☃, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
        a(☃, Blocks.NETHER_BRICK.getBlockData(), ☃, 13, 0, ☃);
        a(☃, Blocks.NETHER_BRICK.getBlockData(), ☃, 13, 12, ☃);
        a(☃, Blocks.NETHER_BRICK.getBlockData(), 0, 13, ☃, ☃);
        a(☃, Blocks.NETHER_BRICK.getBlockData(), 12, 13, ☃, ☃);
        a(☃, Blocks.NETHER_BRICK_FENCE.getBlockData(), ☃ + 1, 13, 0, ☃);
        a(☃, Blocks.NETHER_BRICK_FENCE.getBlockData(), ☃ + 1, 13, 12, ☃);
        a(☃, Blocks.NETHER_BRICK_FENCE.getBlockData(), 0, 13, ☃ + 1, ☃);
        a(☃, Blocks.NETHER_BRICK_FENCE.getBlockData(), 12, 13, ☃ + 1, ☃);
      }
      a(☃, Blocks.NETHER_BRICK_FENCE.getBlockData(), 0, 13, 0, ☃);
      a(☃, Blocks.NETHER_BRICK_FENCE.getBlockData(), 0, 13, 12, ☃);
      a(☃, Blocks.NETHER_BRICK_FENCE.getBlockData(), 0, 13, 0, ☃);
      a(☃, Blocks.NETHER_BRICK_FENCE.getBlockData(), 12, 13, 0, ☃);
      for (int ☃ = 3; ☃ <= 9; ☃ += 2)
      {
        a(☃, ☃, 1, 7, ☃, 1, 8, ☃, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
        a(☃, ☃, 11, 7, ☃, 11, 8, ☃, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
      }
      int ☃ = a(Blocks.NETHER_BRICK_STAIRS, 3);
      for (int ☃ = 0; ☃ <= 6; ☃++)
      {
        int ☃ = ☃ + 4;
        for (int ☃ = 5; ☃ <= 7; ☃++) {
          a(☃, Blocks.NETHER_BRICK_STAIRS.fromLegacyData(☃), ☃, 5 + ☃, ☃, ☃);
        }
        if ((☃ >= 5) && (☃ <= 8)) {
          a(☃, ☃, 5, 5, ☃, 7, ☃ + 4, ☃, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
        } else if ((☃ >= 9) && (☃ <= 10)) {
          a(☃, ☃, 5, 8, ☃, 7, ☃ + 4, ☃, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
        }
        if (☃ >= 1) {
          a(☃, ☃, 5, 6 + ☃, ☃, 7, 9 + ☃, ☃, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
        }
      }
      for (int ☃ = 5; ☃ <= 7; ☃++) {
        a(☃, Blocks.NETHER_BRICK_STAIRS.fromLegacyData(☃), ☃, 12, 11, ☃);
      }
      a(☃, ☃, 5, 6, 7, 5, 7, 7, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
      a(☃, ☃, 7, 6, 7, 7, 7, 7, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
      a(☃, ☃, 5, 13, 12, 7, 13, 12, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
      
      a(☃, ☃, 2, 5, 2, 3, 5, 3, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 2, 5, 9, 3, 5, 10, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 2, 5, 4, 2, 5, 8, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 9, 5, 2, 10, 5, 3, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 9, 5, 9, 10, 5, 10, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 10, 5, 4, 10, 5, 8, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      int ☃ = a(Blocks.NETHER_BRICK_STAIRS, 0);
      int ☃ = a(Blocks.NETHER_BRICK_STAIRS, 1);
      a(☃, Blocks.NETHER_BRICK_STAIRS.fromLegacyData(☃), 4, 5, 2, ☃);
      a(☃, Blocks.NETHER_BRICK_STAIRS.fromLegacyData(☃), 4, 5, 3, ☃);
      a(☃, Blocks.NETHER_BRICK_STAIRS.fromLegacyData(☃), 4, 5, 9, ☃);
      a(☃, Blocks.NETHER_BRICK_STAIRS.fromLegacyData(☃), 4, 5, 10, ☃);
      a(☃, Blocks.NETHER_BRICK_STAIRS.fromLegacyData(☃), 8, 5, 2, ☃);
      a(☃, Blocks.NETHER_BRICK_STAIRS.fromLegacyData(☃), 8, 5, 3, ☃);
      a(☃, Blocks.NETHER_BRICK_STAIRS.fromLegacyData(☃), 8, 5, 9, ☃);
      a(☃, Blocks.NETHER_BRICK_STAIRS.fromLegacyData(☃), 8, 5, 10, ☃);
      
      a(☃, ☃, 3, 4, 4, 4, 4, 8, Blocks.SOUL_SAND.getBlockData(), Blocks.SOUL_SAND.getBlockData(), false);
      a(☃, ☃, 8, 4, 4, 9, 4, 8, Blocks.SOUL_SAND.getBlockData(), Blocks.SOUL_SAND.getBlockData(), false);
      a(☃, ☃, 3, 5, 4, 4, 5, 8, Blocks.NETHER_WART.getBlockData(), Blocks.NETHER_WART.getBlockData(), false);
      a(☃, ☃, 8, 5, 4, 9, 5, 8, Blocks.NETHER_WART.getBlockData(), Blocks.NETHER_WART.getBlockData(), false);
      
      a(☃, ☃, 4, 2, 0, 8, 2, 12, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 0, 2, 4, 12, 2, 8, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      
      a(☃, ☃, 4, 0, 0, 8, 1, 3, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 4, 0, 9, 8, 1, 12, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 0, 0, 4, 3, 1, 8, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 9, 0, 4, 12, 1, 8, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      for (int ☃ = 4; ☃ <= 8; ☃++) {
        for (int ☃ = 0; ☃ <= 2; ☃++)
        {
          b(☃, Blocks.NETHER_BRICK.getBlockData(), ☃, -1, ☃, ☃);
          b(☃, Blocks.NETHER_BRICK.getBlockData(), ☃, -1, 12 - ☃, ☃);
        }
      }
      for (int ☃ = 0; ☃ <= 2; ☃++) {
        for (int ☃ = 4; ☃ <= 8; ☃++)
        {
          b(☃, Blocks.NETHER_BRICK.getBlockData(), ☃, -1, ☃, ☃);
          b(☃, Blocks.NETHER_BRICK.getBlockData(), 12 - ☃, -1, ☃, ☃);
        }
      }
      return true;
    }
  }
  
  public static class WorldGenNetherPiece9
    extends WorldGenNetherPieces.WorldGenNetherPiece
  {
    public WorldGenNetherPiece9() {}
    
    public WorldGenNetherPiece9(int ☃, Random ☃, StructureBoundingBox ☃, EnumDirection ☃)
    {
      super();
      
      this.m = ☃;
      this.l = ☃;
    }
    
    public void a(StructurePiece ☃, List<StructurePiece> ☃, Random ☃)
    {
      a((WorldGenNetherPieces.WorldGenNetherPiece15)☃, ☃, ☃, 1, 0, true);
    }
    
    public static WorldGenNetherPiece9 a(List<StructurePiece> ☃, Random ☃, int ☃, int ☃, int ☃, EnumDirection ☃, int ☃)
    {
      StructureBoundingBox ☃ = StructureBoundingBox.a(☃, ☃, ☃, -1, 0, 0, 5, 7, 5, ☃);
      if ((!a(☃)) || (StructurePiece.a(☃, ☃) != null)) {
        return null;
      }
      return new WorldGenNetherPiece9(☃, ☃, ☃, ☃);
    }
    
    public boolean a(World ☃, Random ☃, StructureBoundingBox ☃)
    {
      a(☃, ☃, 0, 0, 0, 4, 1, 4, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      
      a(☃, ☃, 0, 2, 0, 4, 5, 4, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
      
      a(☃, ☃, 0, 2, 0, 0, 5, 4, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 4, 2, 0, 4, 5, 4, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 0, 3, 1, 0, 4, 1, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
      a(☃, ☃, 0, 3, 3, 0, 4, 3, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
      a(☃, ☃, 4, 3, 1, 4, 4, 1, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
      a(☃, ☃, 4, 3, 3, 4, 4, 3, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
      
      a(☃, ☃, 0, 6, 0, 4, 6, 4, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      for (int ☃ = 0; ☃ <= 4; ☃++) {
        for (int ☃ = 0; ☃ <= 4; ☃++) {
          b(☃, Blocks.NETHER_BRICK.getBlockData(), ☃, -1, ☃, ☃);
        }
      }
      return true;
    }
  }
  
  public static class WorldGenNetherPiece7
    extends WorldGenNetherPieces.WorldGenNetherPiece
  {
    public WorldGenNetherPiece7() {}
    
    public WorldGenNetherPiece7(int ☃, Random ☃, StructureBoundingBox ☃, EnumDirection ☃)
    {
      super();
      
      this.m = ☃;
      this.l = ☃;
    }
    
    public void a(StructurePiece ☃, List<StructurePiece> ☃, Random ☃)
    {
      a((WorldGenNetherPieces.WorldGenNetherPiece15)☃, ☃, ☃, 1, 0, true);
      b((WorldGenNetherPieces.WorldGenNetherPiece15)☃, ☃, ☃, 0, 1, true);
      c((WorldGenNetherPieces.WorldGenNetherPiece15)☃, ☃, ☃, 0, 1, true);
    }
    
    public static WorldGenNetherPiece7 a(List<StructurePiece> ☃, Random ☃, int ☃, int ☃, int ☃, EnumDirection ☃, int ☃)
    {
      StructureBoundingBox ☃ = StructureBoundingBox.a(☃, ☃, ☃, -1, 0, 0, 5, 7, 5, ☃);
      if ((!a(☃)) || (StructurePiece.a(☃, ☃) != null)) {
        return null;
      }
      return new WorldGenNetherPiece7(☃, ☃, ☃, ☃);
    }
    
    public boolean a(World ☃, Random ☃, StructureBoundingBox ☃)
    {
      a(☃, ☃, 0, 0, 0, 4, 1, 4, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      
      a(☃, ☃, 0, 2, 0, 4, 5, 4, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
      
      a(☃, ☃, 0, 2, 0, 0, 5, 0, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 4, 2, 0, 4, 5, 0, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 0, 2, 4, 0, 5, 4, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 4, 2, 4, 4, 5, 4, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      
      a(☃, ☃, 0, 6, 0, 4, 6, 4, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      for (int ☃ = 0; ☃ <= 4; ☃++) {
        for (int ☃ = 0; ☃ <= 4; ☃++) {
          b(☃, Blocks.NETHER_BRICK.getBlockData(), ☃, -1, ☃, ☃);
        }
      }
      return true;
    }
  }
  
  public static class WorldGenNetherPiece10
    extends WorldGenNetherPieces.WorldGenNetherPiece
  {
    private boolean b;
    
    public WorldGenNetherPiece10() {}
    
    public WorldGenNetherPiece10(int ☃, Random ☃, StructureBoundingBox ☃, EnumDirection ☃)
    {
      super();
      
      this.m = ☃;
      this.l = ☃;
      
      this.b = (☃.nextInt(3) == 0);
    }
    
    protected void b(NBTTagCompound ☃)
    {
      super.b(☃);
      
      this.b = ☃.getBoolean("Chest");
    }
    
    protected void a(NBTTagCompound ☃)
    {
      super.a(☃);
      
      ☃.setBoolean("Chest", this.b);
    }
    
    public void a(StructurePiece ☃, List<StructurePiece> ☃, Random ☃)
    {
      c((WorldGenNetherPieces.WorldGenNetherPiece15)☃, ☃, ☃, 0, 1, true);
    }
    
    public static WorldGenNetherPiece10 a(List<StructurePiece> ☃, Random ☃, int ☃, int ☃, int ☃, EnumDirection ☃, int ☃)
    {
      StructureBoundingBox ☃ = StructureBoundingBox.a(☃, ☃, ☃, -1, 0, 0, 5, 7, 5, ☃);
      if ((!a(☃)) || (StructurePiece.a(☃, ☃) != null)) {
        return null;
      }
      return new WorldGenNetherPiece10(☃, ☃, ☃, ☃);
    }
    
    public boolean a(World ☃, Random ☃, StructureBoundingBox ☃)
    {
      a(☃, ☃, 0, 0, 0, 4, 1, 4, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      
      a(☃, ☃, 0, 2, 0, 4, 5, 4, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
      
      a(☃, ☃, 0, 2, 0, 0, 5, 4, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 0, 3, 1, 0, 4, 1, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
      a(☃, ☃, 0, 3, 3, 0, 4, 3, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
      
      a(☃, ☃, 4, 2, 0, 4, 5, 0, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      
      a(☃, ☃, 1, 2, 4, 4, 5, 4, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 1, 3, 4, 1, 4, 4, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 3, 3, 4, 3, 4, 4, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      if ((this.b) && 
        (☃.b(new BlockPosition(a(1, 3), d(2), b(1, 3)))))
      {
        this.b = false;
        a(☃, ☃, ☃, 1, 2, 3, a, 2 + ☃.nextInt(4));
      }
      a(☃, ☃, 0, 6, 0, 4, 6, 4, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      for (int ☃ = 0; ☃ <= 4; ☃++) {
        for (int ☃ = 0; ☃ <= 4; ☃++) {
          b(☃, Blocks.NETHER_BRICK.getBlockData(), ☃, -1, ☃, ☃);
        }
      }
      return true;
    }
  }
  
  public static class WorldGenNetherPiece8
    extends WorldGenNetherPieces.WorldGenNetherPiece
  {
    private boolean b;
    
    public WorldGenNetherPiece8() {}
    
    public WorldGenNetherPiece8(int ☃, Random ☃, StructureBoundingBox ☃, EnumDirection ☃)
    {
      super();
      
      this.m = ☃;
      this.l = ☃;
      
      this.b = (☃.nextInt(3) == 0);
    }
    
    protected void b(NBTTagCompound ☃)
    {
      super.b(☃);
      
      this.b = ☃.getBoolean("Chest");
    }
    
    protected void a(NBTTagCompound ☃)
    {
      super.a(☃);
      
      ☃.setBoolean("Chest", this.b);
    }
    
    public void a(StructurePiece ☃, List<StructurePiece> ☃, Random ☃)
    {
      b((WorldGenNetherPieces.WorldGenNetherPiece15)☃, ☃, ☃, 0, 1, true);
    }
    
    public static WorldGenNetherPiece8 a(List<StructurePiece> ☃, Random ☃, int ☃, int ☃, int ☃, EnumDirection ☃, int ☃)
    {
      StructureBoundingBox ☃ = StructureBoundingBox.a(☃, ☃, ☃, -1, 0, 0, 5, 7, 5, ☃);
      if ((!a(☃)) || (StructurePiece.a(☃, ☃) != null)) {
        return null;
      }
      return new WorldGenNetherPiece8(☃, ☃, ☃, ☃);
    }
    
    public boolean a(World ☃, Random ☃, StructureBoundingBox ☃)
    {
      a(☃, ☃, 0, 0, 0, 4, 1, 4, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      
      a(☃, ☃, 0, 2, 0, 4, 5, 4, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
      
      a(☃, ☃, 4, 2, 0, 4, 5, 4, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 4, 3, 1, 4, 4, 1, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
      a(☃, ☃, 4, 3, 3, 4, 4, 3, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
      
      a(☃, ☃, 0, 2, 0, 0, 5, 0, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      
      a(☃, ☃, 0, 2, 4, 3, 5, 4, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 1, 3, 4, 1, 4, 4, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 3, 3, 4, 3, 4, 4, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      if ((this.b) && 
        (☃.b(new BlockPosition(a(3, 3), d(2), b(3, 3)))))
      {
        this.b = false;
        a(☃, ☃, ☃, 3, 2, 3, a, 2 + ☃.nextInt(4));
      }
      a(☃, ☃, 0, 6, 0, 4, 6, 4, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      for (int ☃ = 0; ☃ <= 4; ☃++) {
        for (int ☃ = 0; ☃ <= 4; ☃++) {
          b(☃, Blocks.NETHER_BRICK.getBlockData(), ☃, -1, ☃, ☃);
        }
      }
      return true;
    }
  }
  
  public static class WorldGenNetherPiece4
    extends WorldGenNetherPieces.WorldGenNetherPiece
  {
    public WorldGenNetherPiece4() {}
    
    public WorldGenNetherPiece4(int ☃, Random ☃, StructureBoundingBox ☃, EnumDirection ☃)
    {
      super();
      
      this.m = ☃;
      this.l = ☃;
    }
    
    public void a(StructurePiece ☃, List<StructurePiece> ☃, Random ☃)
    {
      a((WorldGenNetherPieces.WorldGenNetherPiece15)☃, ☃, ☃, 1, 0, true);
    }
    
    public static WorldGenNetherPiece4 a(List<StructurePiece> ☃, Random ☃, int ☃, int ☃, int ☃, EnumDirection ☃, int ☃)
    {
      StructureBoundingBox ☃ = StructureBoundingBox.a(☃, ☃, ☃, -1, -7, 0, 5, 14, 10, ☃);
      if ((!a(☃)) || (StructurePiece.a(☃, ☃) != null)) {
        return null;
      }
      return new WorldGenNetherPiece4(☃, ☃, ☃, ☃);
    }
    
    public boolean a(World ☃, Random ☃, StructureBoundingBox ☃)
    {
      int ☃ = a(Blocks.NETHER_BRICK_STAIRS, 2);
      for (int ☃ = 0; ☃ <= 9; ☃++)
      {
        int ☃ = Math.max(1, 7 - ☃);
        int ☃ = Math.min(Math.max(☃ + 5, 14 - ☃), 13);
        int ☃ = ☃;
        
        a(☃, ☃, 0, 0, ☃, 4, ☃, ☃, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
        
        a(☃, ☃, 1, ☃ + 1, ☃, 3, ☃ - 1, ☃, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
        if (☃ <= 6)
        {
          a(☃, Blocks.NETHER_BRICK_STAIRS.fromLegacyData(☃), 1, ☃ + 1, ☃, ☃);
          a(☃, Blocks.NETHER_BRICK_STAIRS.fromLegacyData(☃), 2, ☃ + 1, ☃, ☃);
          a(☃, Blocks.NETHER_BRICK_STAIRS.fromLegacyData(☃), 3, ☃ + 1, ☃, ☃);
        }
        a(☃, ☃, 0, ☃, ☃, 4, ☃, ☃, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
        
        a(☃, ☃, 0, ☃ + 1, ☃, 0, ☃ - 1, ☃, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
        a(☃, ☃, 4, ☃ + 1, ☃, 4, ☃ - 1, ☃, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
        if ((☃ & 0x1) == 0)
        {
          a(☃, ☃, 0, ☃ + 2, ☃, 0, ☃ + 3, ☃, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
          a(☃, ☃, 4, ☃ + 2, ☃, 4, ☃ + 3, ☃, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
        }
        for (int ☃ = 0; ☃ <= 4; ☃++) {
          b(☃, Blocks.NETHER_BRICK.getBlockData(), ☃, -1, ☃, ☃);
        }
      }
      return true;
    }
  }
  
  public static class WorldGenNetherPiece5
    extends WorldGenNetherPieces.WorldGenNetherPiece
  {
    public WorldGenNetherPiece5() {}
    
    public WorldGenNetherPiece5(int ☃, Random ☃, StructureBoundingBox ☃, EnumDirection ☃)
    {
      super();
      
      this.m = ☃;
      this.l = ☃;
    }
    
    public void a(StructurePiece ☃, List<StructurePiece> ☃, Random ☃)
    {
      int ☃ = 1;
      if ((this.m == EnumDirection.WEST) || (this.m == EnumDirection.NORTH)) {
        ☃ = 5;
      }
      b((WorldGenNetherPieces.WorldGenNetherPiece15)☃, ☃, ☃, 0, ☃, ☃.nextInt(8) > 0);
      c((WorldGenNetherPieces.WorldGenNetherPiece15)☃, ☃, ☃, 0, ☃, ☃.nextInt(8) > 0);
    }
    
    public static WorldGenNetherPiece5 a(List<StructurePiece> ☃, Random ☃, int ☃, int ☃, int ☃, EnumDirection ☃, int ☃)
    {
      StructureBoundingBox ☃ = StructureBoundingBox.a(☃, ☃, ☃, -3, 0, 0, 9, 7, 9, ☃);
      if ((!a(☃)) || (StructurePiece.a(☃, ☃) != null)) {
        return null;
      }
      return new WorldGenNetherPiece5(☃, ☃, ☃, ☃);
    }
    
    public boolean a(World ☃, Random ☃, StructureBoundingBox ☃)
    {
      a(☃, ☃, 0, 0, 0, 8, 1, 8, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      
      a(☃, ☃, 0, 2, 0, 8, 5, 8, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
      
      a(☃, ☃, 0, 6, 0, 8, 6, 5, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      
      a(☃, ☃, 0, 2, 0, 2, 5, 0, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 6, 2, 0, 8, 5, 0, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 1, 3, 0, 1, 4, 0, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
      a(☃, ☃, 7, 3, 0, 7, 4, 0, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
      
      a(☃, ☃, 0, 2, 4, 8, 2, 8, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 1, 1, 4, 2, 2, 4, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
      a(☃, ☃, 6, 1, 4, 7, 2, 4, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
      
      a(☃, ☃, 0, 3, 8, 8, 3, 8, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
      a(☃, ☃, 0, 3, 6, 0, 3, 7, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
      a(☃, ☃, 8, 3, 6, 8, 3, 7, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
      
      a(☃, ☃, 0, 3, 4, 0, 5, 5, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 8, 3, 4, 8, 5, 5, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 1, 3, 5, 2, 5, 5, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 6, 3, 5, 7, 5, 5, Blocks.NETHER_BRICK.getBlockData(), Blocks.NETHER_BRICK.getBlockData(), false);
      a(☃, ☃, 1, 4, 5, 1, 5, 5, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
      a(☃, ☃, 7, 4, 5, 7, 5, 5, Blocks.NETHER_BRICK_FENCE.getBlockData(), Blocks.NETHER_BRICK_FENCE.getBlockData(), false);
      for (int ☃ = 0; ☃ <= 5; ☃++) {
        for (int ☃ = 0; ☃ <= 8; ☃++) {
          b(☃, Blocks.NETHER_BRICK.getBlockData(), ☃, -1, ☃, ☃);
        }
      }
      return true;
    }
  }
}
