package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;

public class WorldGenStrongholdPieces
{
  public static void a()
  {
    WorldGenFactory.a(WorldGenStrongholdChestCorridor.class, "SHCC");
    WorldGenFactory.a(WorldGenStrongholdCorridor.class, "SHFC");
    WorldGenFactory.a(WorldGenStrongholdCrossing.class, "SH5C");
    WorldGenFactory.a(WorldGenStrongholdLeftTurn.class, "SHLT");
    WorldGenFactory.a(WorldGenStrongholdLibrary.class, "SHLi");
    WorldGenFactory.a(WorldGenStrongholdPortalRoom.class, "SHPR");
    WorldGenFactory.a(WorldGenStrongholdPrison.class, "SHPH");
    WorldGenFactory.a(WorldGenStrongholdRightTurn.class, "SHRT");
    WorldGenFactory.a(WorldGenStrongholdRoomCrossing.class, "SHRC");
    WorldGenFactory.a(WorldGenStrongholdStairs2.class, "SHSD");
    WorldGenFactory.a(WorldGenStrongholdStart.class, "SHStart");
    WorldGenFactory.a(WorldGenStrongholdStairs.class, "SHS");
    WorldGenFactory.a(WorldGenStrongholdStairsStraight.class, "SHSSD");
  }
  
  static class WorldGenStrongholdPieceWeight
  {
    public Class<? extends WorldGenStrongholdPieces.WorldGenStrongholdPiece> a;
    public final int b;
    public int c;
    public int d;
    
    public WorldGenStrongholdPieceWeight(Class<? extends WorldGenStrongholdPieces.WorldGenStrongholdPiece> ☃, int ☃, int ☃)
    {
      this.a = ☃;
      this.b = ☃;
      this.d = ☃;
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
  
  private static final WorldGenStrongholdPieceWeight[] b = { new WorldGenStrongholdPieceWeight(WorldGenStrongholdStairs.class, 40, 0), new WorldGenStrongholdPieceWeight(WorldGenStrongholdPrison.class, 5, 5), new WorldGenStrongholdPieceWeight(WorldGenStrongholdLeftTurn.class, 20, 0), new WorldGenStrongholdPieceWeight(WorldGenStrongholdRightTurn.class, 20, 0), new WorldGenStrongholdPieceWeight(WorldGenStrongholdRoomCrossing.class, 10, 6), new WorldGenStrongholdPieceWeight(WorldGenStrongholdStairsStraight.class, 5, 5), new WorldGenStrongholdPieceWeight(WorldGenStrongholdStairs2.class, 5, 5), new WorldGenStrongholdPieceWeight(WorldGenStrongholdCrossing.class, 5, 4), new WorldGenStrongholdPieceWeight(WorldGenStrongholdChestCorridor.class, 5, 4), new WorldGenStrongholdPieceWeight(WorldGenStrongholdLibrary.class, 10, 2)new WorldGenStrongholdPieceWeight
  {
    public boolean a(int ☃)
    {
      return (super.a(☃)) && (☃ > 4);
    }
  }, new WorldGenStrongholdPieceWeight(WorldGenStrongholdPortalRoom.class, 20, 1)
  {
    public boolean a(int ☃)
    {
      return (super.a(☃)) && (☃ > 5);
    }
  } };
  private static List<WorldGenStrongholdPieceWeight> c;
  private static Class<? extends WorldGenStrongholdPiece> d;
  static int a;
  
  public static void b()
  {
    c = Lists.newArrayList();
    for (WorldGenStrongholdPieceWeight ☃ : b)
    {
      ☃.c = 0;
      c.add(☃);
    }
    d = null;
  }
  
  private static boolean d()
  {
    boolean ☃ = false;
    a = 0;
    for (WorldGenStrongholdPieceWeight ☃ : c)
    {
      if ((☃.d > 0) && (☃.c < ☃.d)) {
        ☃ = true;
      }
      a += ☃.b;
    }
    return ☃;
  }
  
  private static WorldGenStrongholdPiece a(Class<? extends WorldGenStrongholdPiece> ☃, List<StructurePiece> ☃, Random ☃, int ☃, int ☃, int ☃, EnumDirection ☃, int ☃)
  {
    WorldGenStrongholdPiece ☃ = null;
    if (☃ == WorldGenStrongholdStairs.class) {
      ☃ = WorldGenStrongholdStairs.a(☃, ☃, ☃, ☃, ☃, ☃, ☃);
    } else if (☃ == WorldGenStrongholdPrison.class) {
      ☃ = WorldGenStrongholdPrison.a(☃, ☃, ☃, ☃, ☃, ☃, ☃);
    } else if (☃ == WorldGenStrongholdLeftTurn.class) {
      ☃ = WorldGenStrongholdLeftTurn.a(☃, ☃, ☃, ☃, ☃, ☃, ☃);
    } else if (☃ == WorldGenStrongholdRightTurn.class) {
      ☃ = WorldGenStrongholdRightTurn.a(☃, ☃, ☃, ☃, ☃, ☃, ☃);
    } else if (☃ == WorldGenStrongholdRoomCrossing.class) {
      ☃ = WorldGenStrongholdRoomCrossing.a(☃, ☃, ☃, ☃, ☃, ☃, ☃);
    } else if (☃ == WorldGenStrongholdStairsStraight.class) {
      ☃ = WorldGenStrongholdStairsStraight.a(☃, ☃, ☃, ☃, ☃, ☃, ☃);
    } else if (☃ == WorldGenStrongholdStairs2.class) {
      ☃ = WorldGenStrongholdStairs2.a(☃, ☃, ☃, ☃, ☃, ☃, ☃);
    } else if (☃ == WorldGenStrongholdCrossing.class) {
      ☃ = WorldGenStrongholdCrossing.a(☃, ☃, ☃, ☃, ☃, ☃, ☃);
    } else if (☃ == WorldGenStrongholdChestCorridor.class) {
      ☃ = WorldGenStrongholdChestCorridor.a(☃, ☃, ☃, ☃, ☃, ☃, ☃);
    } else if (☃ == WorldGenStrongholdLibrary.class) {
      ☃ = WorldGenStrongholdLibrary.a(☃, ☃, ☃, ☃, ☃, ☃, ☃);
    } else if (☃ == WorldGenStrongholdPortalRoom.class) {
      ☃ = WorldGenStrongholdPortalRoom.a(☃, ☃, ☃, ☃, ☃, ☃, ☃);
    }
    return ☃;
  }
  
  private static WorldGenStrongholdPiece b(WorldGenStrongholdStart ☃, List<StructurePiece> ☃, Random ☃, int ☃, int ☃, int ☃, EnumDirection ☃, int ☃)
  {
    if (!d()) {
      return null;
    }
    if (d != null)
    {
      WorldGenStrongholdPiece ☃ = a(d, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
      d = null;
      if (☃ != null) {
        return ☃;
      }
    }
    int ☃ = 0;
    int ☃;
    while (☃ < 5)
    {
      ☃++;
      
      ☃ = ☃.nextInt(a);
      for (WorldGenStrongholdPieceWeight ☃ : c)
      {
        ☃ -= ☃.b;
        if (☃ < 0)
        {
          if ((!☃.a(☃)) || (☃ == ☃.a)) {
            break;
          }
          WorldGenStrongholdPiece ☃ = a(☃.a, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
          if (☃ != null)
          {
            ☃.c += 1;
            ☃.a = ☃;
            if (!☃.a()) {
              c.remove(☃);
            }
            return ☃;
          }
        }
      }
    }
    StructureBoundingBox ☃ = WorldGenStrongholdCorridor.a(☃, ☃, ☃, ☃, ☃, ☃);
    if ((☃ != null) && (☃.b > 1)) {
      return new WorldGenStrongholdCorridor(☃, ☃, ☃, ☃);
    }
    return null;
  }
  
  private static StructurePiece c(WorldGenStrongholdStart ☃, List<StructurePiece> ☃, Random ☃, int ☃, int ☃, int ☃, EnumDirection ☃, int ☃)
  {
    if (☃ > 50) {
      return null;
    }
    if ((Math.abs(☃ - ☃.c().a) > 112) || (Math.abs(☃ - ☃.c().c) > 112)) {
      return null;
    }
    StructurePiece ☃ = b(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃ + 1);
    if (☃ != null)
    {
      ☃.add(☃);
      ☃.c.add(☃);
    }
    return ☃;
  }
  
  static abstract class WorldGenStrongholdPiece
    extends StructurePiece
  {
    protected WorldGenStrongholdDoorType d = WorldGenStrongholdDoorType.OPENING;
    
    public WorldGenStrongholdPiece() {}
    
    protected WorldGenStrongholdPiece(int ☃)
    {
      super();
    }
    
    protected void a(NBTTagCompound ☃)
    {
      ☃.setString("EntryDoor", this.d.name());
    }
    
    protected void b(NBTTagCompound ☃)
    {
      this.d = WorldGenStrongholdDoorType.valueOf(☃.getString("EntryDoor"));
    }
    
    protected void a(World ☃, Random ☃, StructureBoundingBox ☃, WorldGenStrongholdDoorType ☃, int ☃, int ☃, int ☃)
    {
      switch (WorldGenStrongholdPieces.3.a[☃.ordinal()])
      {
      case 1: 
      default: 
        a(☃, ☃, ☃, ☃, ☃, ☃ + 3 - 1, ☃ + 3 - 1, ☃, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
        break;
      case 2: 
        a(☃, Blocks.STONEBRICK.getBlockData(), ☃, ☃, ☃, ☃);
        a(☃, Blocks.STONEBRICK.getBlockData(), ☃, ☃ + 1, ☃, ☃);
        a(☃, Blocks.STONEBRICK.getBlockData(), ☃, ☃ + 2, ☃, ☃);
        a(☃, Blocks.STONEBRICK.getBlockData(), ☃ + 1, ☃ + 2, ☃, ☃);
        a(☃, Blocks.STONEBRICK.getBlockData(), ☃ + 2, ☃ + 2, ☃, ☃);
        a(☃, Blocks.STONEBRICK.getBlockData(), ☃ + 2, ☃ + 1, ☃, ☃);
        a(☃, Blocks.STONEBRICK.getBlockData(), ☃ + 2, ☃, ☃, ☃);
        a(☃, Blocks.WOODEN_DOOR.getBlockData(), ☃ + 1, ☃, ☃, ☃);
        a(☃, Blocks.WOODEN_DOOR.fromLegacyData(8), ☃ + 1, ☃ + 1, ☃, ☃);
        break;
      case 3: 
        a(☃, Blocks.AIR.getBlockData(), ☃ + 1, ☃, ☃, ☃);
        a(☃, Blocks.AIR.getBlockData(), ☃ + 1, ☃ + 1, ☃, ☃);
        a(☃, Blocks.IRON_BARS.getBlockData(), ☃, ☃, ☃, ☃);
        a(☃, Blocks.IRON_BARS.getBlockData(), ☃, ☃ + 1, ☃, ☃);
        a(☃, Blocks.IRON_BARS.getBlockData(), ☃, ☃ + 2, ☃, ☃);
        a(☃, Blocks.IRON_BARS.getBlockData(), ☃ + 1, ☃ + 2, ☃, ☃);
        a(☃, Blocks.IRON_BARS.getBlockData(), ☃ + 2, ☃ + 2, ☃, ☃);
        a(☃, Blocks.IRON_BARS.getBlockData(), ☃ + 2, ☃ + 1, ☃, ☃);
        a(☃, Blocks.IRON_BARS.getBlockData(), ☃ + 2, ☃, ☃, ☃);
        break;
      case 4: 
        a(☃, Blocks.STONEBRICK.getBlockData(), ☃, ☃, ☃, ☃);
        a(☃, Blocks.STONEBRICK.getBlockData(), ☃, ☃ + 1, ☃, ☃);
        a(☃, Blocks.STONEBRICK.getBlockData(), ☃, ☃ + 2, ☃, ☃);
        a(☃, Blocks.STONEBRICK.getBlockData(), ☃ + 1, ☃ + 2, ☃, ☃);
        a(☃, Blocks.STONEBRICK.getBlockData(), ☃ + 2, ☃ + 2, ☃, ☃);
        a(☃, Blocks.STONEBRICK.getBlockData(), ☃ + 2, ☃ + 1, ☃, ☃);
        a(☃, Blocks.STONEBRICK.getBlockData(), ☃ + 2, ☃, ☃, ☃);
        a(☃, Blocks.IRON_DOOR.getBlockData(), ☃ + 1, ☃, ☃, ☃);
        a(☃, Blocks.IRON_DOOR.fromLegacyData(8), ☃ + 1, ☃ + 1, ☃, ☃);
        a(☃, Blocks.STONE_BUTTON.fromLegacyData(a(Blocks.STONE_BUTTON, 4)), ☃ + 2, ☃ + 1, ☃ + 1, ☃);
        a(☃, Blocks.STONE_BUTTON.fromLegacyData(a(Blocks.STONE_BUTTON, 3)), ☃ + 2, ☃ + 1, ☃ - 1, ☃);
      }
    }
    
    protected WorldGenStrongholdDoorType a(Random ☃)
    {
      int ☃ = ☃.nextInt(5);
      switch (☃)
      {
      case 0: 
      case 1: 
      default: 
        return WorldGenStrongholdDoorType.OPENING;
      case 2: 
        return WorldGenStrongholdDoorType.WOOD_DOOR;
      case 3: 
        return WorldGenStrongholdDoorType.GRATES;
      }
      return WorldGenStrongholdDoorType.IRON_DOOR;
    }
    
    protected StructurePiece a(WorldGenStrongholdPieces.WorldGenStrongholdStart ☃, List<StructurePiece> ☃, Random ☃, int ☃, int ☃)
    {
      if (this.m != null) {
        switch (WorldGenStrongholdPieces.3.b[this.m.ordinal()])
        {
        case 1: 
          return WorldGenStrongholdPieces.a(☃, ☃, ☃, this.l.a + ☃, this.l.b + ☃, this.l.c - 1, this.m, d());
        case 2: 
          return WorldGenStrongholdPieces.a(☃, ☃, ☃, this.l.a + ☃, this.l.b + ☃, this.l.f + 1, this.m, d());
        case 3: 
          return WorldGenStrongholdPieces.a(☃, ☃, ☃, this.l.a - 1, this.l.b + ☃, this.l.c + ☃, this.m, d());
        case 4: 
          return WorldGenStrongholdPieces.a(☃, ☃, ☃, this.l.d + 1, this.l.b + ☃, this.l.c + ☃, this.m, d());
        }
      }
      return null;
    }
    
    protected StructurePiece b(WorldGenStrongholdPieces.WorldGenStrongholdStart ☃, List<StructurePiece> ☃, Random ☃, int ☃, int ☃)
    {
      if (this.m != null) {
        switch (WorldGenStrongholdPieces.3.b[this.m.ordinal()])
        {
        case 1: 
          return WorldGenStrongholdPieces.a(☃, ☃, ☃, this.l.a - 1, this.l.b + ☃, this.l.c + ☃, EnumDirection.WEST, d());
        case 2: 
          return WorldGenStrongholdPieces.a(☃, ☃, ☃, this.l.a - 1, this.l.b + ☃, this.l.c + ☃, EnumDirection.WEST, d());
        case 3: 
          return WorldGenStrongholdPieces.a(☃, ☃, ☃, this.l.a + ☃, this.l.b + ☃, this.l.c - 1, EnumDirection.NORTH, d());
        case 4: 
          return WorldGenStrongholdPieces.a(☃, ☃, ☃, this.l.a + ☃, this.l.b + ☃, this.l.c - 1, EnumDirection.NORTH, d());
        }
      }
      return null;
    }
    
    protected StructurePiece c(WorldGenStrongholdPieces.WorldGenStrongholdStart ☃, List<StructurePiece> ☃, Random ☃, int ☃, int ☃)
    {
      if (this.m != null) {
        switch (WorldGenStrongholdPieces.3.b[this.m.ordinal()])
        {
        case 1: 
          return WorldGenStrongholdPieces.a(☃, ☃, ☃, this.l.d + 1, this.l.b + ☃, this.l.c + ☃, EnumDirection.EAST, d());
        case 2: 
          return WorldGenStrongholdPieces.a(☃, ☃, ☃, this.l.d + 1, this.l.b + ☃, this.l.c + ☃, EnumDirection.EAST, d());
        case 3: 
          return WorldGenStrongholdPieces.a(☃, ☃, ☃, this.l.a + ☃, this.l.b + ☃, this.l.f + 1, EnumDirection.SOUTH, d());
        case 4: 
          return WorldGenStrongholdPieces.a(☃, ☃, ☃, this.l.a + ☃, this.l.b + ☃, this.l.f + 1, EnumDirection.SOUTH, d());
        }
      }
      return null;
    }
    
    protected static boolean a(StructureBoundingBox ☃)
    {
      return (☃ != null) && (☃.b > 10);
    }
    
    public static enum WorldGenStrongholdDoorType
    {
      private WorldGenStrongholdDoorType() {}
    }
  }
  
  public static class WorldGenStrongholdCorridor
    extends WorldGenStrongholdPieces.WorldGenStrongholdPiece
  {
    private int a;
    
    public WorldGenStrongholdCorridor() {}
    
    public WorldGenStrongholdCorridor(int ☃, Random ☃, StructureBoundingBox ☃, EnumDirection ☃)
    {
      super();
      
      this.m = ☃;
      this.l = ☃;
      this.a = ((☃ == EnumDirection.NORTH) || (☃ == EnumDirection.SOUTH) ? ☃.e() : ☃.c());
    }
    
    protected void a(NBTTagCompound ☃)
    {
      super.a(☃);
      ☃.setInt("Steps", this.a);
    }
    
    protected void b(NBTTagCompound ☃)
    {
      super.b(☃);
      this.a = ☃.getInt("Steps");
    }
    
    public static StructureBoundingBox a(List<StructurePiece> ☃, Random ☃, int ☃, int ☃, int ☃, EnumDirection ☃)
    {
      int ☃ = 3;
      
      StructureBoundingBox ☃ = StructureBoundingBox.a(☃, ☃, ☃, -1, -1, 0, 5, 5, 4, ☃);
      
      StructurePiece ☃ = StructurePiece.a(☃, ☃);
      if (☃ == null) {
        return null;
      }
      if (☃.c().b == ☃.b) {
        for (int ☃ = 3; ☃ >= 1; ☃--)
        {
          ☃ = StructureBoundingBox.a(☃, ☃, ☃, -1, -1, 0, 5, 5, ☃ - 1, ☃);
          if (!☃.c().a(☃)) {
            return StructureBoundingBox.a(☃, ☃, ☃, -1, -1, 0, 5, 5, ☃, ☃);
          }
        }
      }
      return null;
    }
    
    public boolean a(World ☃, Random ☃, StructureBoundingBox ☃)
    {
      if (a(☃, ☃)) {
        return false;
      }
      for (int ☃ = 0; ☃ < this.a; ☃++)
      {
        a(☃, Blocks.STONEBRICK.getBlockData(), 0, 0, ☃, ☃);
        a(☃, Blocks.STONEBRICK.getBlockData(), 1, 0, ☃, ☃);
        a(☃, Blocks.STONEBRICK.getBlockData(), 2, 0, ☃, ☃);
        a(☃, Blocks.STONEBRICK.getBlockData(), 3, 0, ☃, ☃);
        a(☃, Blocks.STONEBRICK.getBlockData(), 4, 0, ☃, ☃);
        for (int ☃ = 1; ☃ <= 3; ☃++)
        {
          a(☃, Blocks.STONEBRICK.getBlockData(), 0, ☃, ☃, ☃);
          a(☃, Blocks.AIR.getBlockData(), 1, ☃, ☃, ☃);
          a(☃, Blocks.AIR.getBlockData(), 2, ☃, ☃, ☃);
          a(☃, Blocks.AIR.getBlockData(), 3, ☃, ☃, ☃);
          a(☃, Blocks.STONEBRICK.getBlockData(), 4, ☃, ☃, ☃);
        }
        a(☃, Blocks.STONEBRICK.getBlockData(), 0, 4, ☃, ☃);
        a(☃, Blocks.STONEBRICK.getBlockData(), 1, 4, ☃, ☃);
        a(☃, Blocks.STONEBRICK.getBlockData(), 2, 4, ☃, ☃);
        a(☃, Blocks.STONEBRICK.getBlockData(), 3, 4, ☃, ☃);
        a(☃, Blocks.STONEBRICK.getBlockData(), 4, 4, ☃, ☃);
      }
      return true;
    }
  }
  
  public static class WorldGenStrongholdStairs2
    extends WorldGenStrongholdPieces.WorldGenStrongholdPiece
  {
    private boolean a;
    
    public WorldGenStrongholdStairs2() {}
    
    public WorldGenStrongholdStairs2(int ☃, Random ☃, int ☃, int ☃)
    {
      super();
      
      this.a = true;
      this.m = EnumDirection.EnumDirectionLimit.HORIZONTAL.a(☃);
      this.d = WorldGenStrongholdPieces.WorldGenStrongholdPiece.WorldGenStrongholdDoorType.OPENING;
      switch (WorldGenStrongholdPieces.3.b[this.m.ordinal()])
      {
      case 1: 
      case 2: 
        this.l = new StructureBoundingBox(☃, 64, ☃, ☃ + 5 - 1, 74, ☃ + 5 - 1);
        break;
      default: 
        this.l = new StructureBoundingBox(☃, 64, ☃, ☃ + 5 - 1, 74, ☃ + 5 - 1);
      }
    }
    
    public WorldGenStrongholdStairs2(int ☃, Random ☃, StructureBoundingBox ☃, EnumDirection ☃)
    {
      super();
      
      this.a = false;
      this.m = ☃;
      this.d = a(☃);
      this.l = ☃;
    }
    
    protected void a(NBTTagCompound ☃)
    {
      super.a(☃);
      ☃.setBoolean("Source", this.a);
    }
    
    protected void b(NBTTagCompound ☃)
    {
      super.b(☃);
      this.a = ☃.getBoolean("Source");
    }
    
    public void a(StructurePiece ☃, List<StructurePiece> ☃, Random ☃)
    {
      if (this.a) {
        WorldGenStrongholdPieces.a(WorldGenStrongholdPieces.WorldGenStrongholdCrossing.class);
      }
      a((WorldGenStrongholdPieces.WorldGenStrongholdStart)☃, ☃, ☃, 1, 1);
    }
    
    public static WorldGenStrongholdStairs2 a(List<StructurePiece> ☃, Random ☃, int ☃, int ☃, int ☃, EnumDirection ☃, int ☃)
    {
      StructureBoundingBox ☃ = StructureBoundingBox.a(☃, ☃, ☃, -1, -7, 0, 5, 11, 5, ☃);
      if ((!a(☃)) || (StructurePiece.a(☃, ☃) != null)) {
        return null;
      }
      return new WorldGenStrongholdStairs2(☃, ☃, ☃, ☃);
    }
    
    public boolean a(World ☃, Random ☃, StructureBoundingBox ☃)
    {
      if (a(☃, ☃)) {
        return false;
      }
      a(☃, ☃, 0, 0, 0, 4, 10, 4, true, ☃, WorldGenStrongholdPieces.c());
      
      a(☃, ☃, ☃, this.d, 1, 7, 0);
      
      a(☃, ☃, ☃, WorldGenStrongholdPieces.WorldGenStrongholdPiece.WorldGenStrongholdDoorType.OPENING, 1, 1, 4);
      
      a(☃, Blocks.STONEBRICK.getBlockData(), 2, 6, 1, ☃);
      a(☃, Blocks.STONEBRICK.getBlockData(), 1, 5, 1, ☃);
      a(☃, Blocks.STONE_SLAB.fromLegacyData(BlockDoubleStepAbstract.EnumStoneSlabVariant.STONE.a()), 1, 6, 1, ☃);
      a(☃, Blocks.STONEBRICK.getBlockData(), 1, 5, 2, ☃);
      a(☃, Blocks.STONEBRICK.getBlockData(), 1, 4, 3, ☃);
      a(☃, Blocks.STONE_SLAB.fromLegacyData(BlockDoubleStepAbstract.EnumStoneSlabVariant.STONE.a()), 1, 5, 3, ☃);
      a(☃, Blocks.STONEBRICK.getBlockData(), 2, 4, 3, ☃);
      a(☃, Blocks.STONEBRICK.getBlockData(), 3, 3, 3, ☃);
      a(☃, Blocks.STONE_SLAB.fromLegacyData(BlockDoubleStepAbstract.EnumStoneSlabVariant.STONE.a()), 3, 4, 3, ☃);
      a(☃, Blocks.STONEBRICK.getBlockData(), 3, 3, 2, ☃);
      a(☃, Blocks.STONEBRICK.getBlockData(), 3, 2, 1, ☃);
      a(☃, Blocks.STONE_SLAB.fromLegacyData(BlockDoubleStepAbstract.EnumStoneSlabVariant.STONE.a()), 3, 3, 1, ☃);
      a(☃, Blocks.STONEBRICK.getBlockData(), 2, 2, 1, ☃);
      a(☃, Blocks.STONEBRICK.getBlockData(), 1, 1, 1, ☃);
      a(☃, Blocks.STONE_SLAB.fromLegacyData(BlockDoubleStepAbstract.EnumStoneSlabVariant.STONE.a()), 1, 2, 1, ☃);
      a(☃, Blocks.STONEBRICK.getBlockData(), 1, 1, 2, ☃);
      a(☃, Blocks.STONE_SLAB.fromLegacyData(BlockDoubleStepAbstract.EnumStoneSlabVariant.STONE.a()), 1, 1, 3, ☃);
      
      return true;
    }
  }
  
  public static class WorldGenStrongholdStart
    extends WorldGenStrongholdPieces.WorldGenStrongholdStairs2
  {
    public WorldGenStrongholdPieces.WorldGenStrongholdPieceWeight a;
    public WorldGenStrongholdPieces.WorldGenStrongholdPortalRoom b;
    public List<StructurePiece> c = Lists.newArrayList();
    
    public WorldGenStrongholdStart() {}
    
    public WorldGenStrongholdStart(int ☃, Random ☃, int ☃, int ☃)
    {
      super(☃, ☃, ☃);
    }
    
    public BlockPosition a()
    {
      if (this.b != null) {
        return this.b.a();
      }
      return super.a();
    }
  }
  
  public static class WorldGenStrongholdStairs
    extends WorldGenStrongholdPieces.WorldGenStrongholdPiece
  {
    private boolean a;
    private boolean b;
    
    public WorldGenStrongholdStairs() {}
    
    public WorldGenStrongholdStairs(int ☃, Random ☃, StructureBoundingBox ☃, EnumDirection ☃)
    {
      super();
      
      this.m = ☃;
      this.d = a(☃);
      this.l = ☃;
      
      this.a = (☃.nextInt(2) == 0);
      this.b = (☃.nextInt(2) == 0);
    }
    
    protected void a(NBTTagCompound ☃)
    {
      super.a(☃);
      ☃.setBoolean("Left", this.a);
      ☃.setBoolean("Right", this.b);
    }
    
    protected void b(NBTTagCompound ☃)
    {
      super.b(☃);
      this.a = ☃.getBoolean("Left");
      this.b = ☃.getBoolean("Right");
    }
    
    public void a(StructurePiece ☃, List<StructurePiece> ☃, Random ☃)
    {
      a((WorldGenStrongholdPieces.WorldGenStrongholdStart)☃, ☃, ☃, 1, 1);
      if (this.a) {
        b((WorldGenStrongholdPieces.WorldGenStrongholdStart)☃, ☃, ☃, 1, 2);
      }
      if (this.b) {
        c((WorldGenStrongholdPieces.WorldGenStrongholdStart)☃, ☃, ☃, 1, 2);
      }
    }
    
    public static WorldGenStrongholdStairs a(List<StructurePiece> ☃, Random ☃, int ☃, int ☃, int ☃, EnumDirection ☃, int ☃)
    {
      StructureBoundingBox ☃ = StructureBoundingBox.a(☃, ☃, ☃, -1, -1, 0, 5, 5, 7, ☃);
      if ((!a(☃)) || (StructurePiece.a(☃, ☃) != null)) {
        return null;
      }
      return new WorldGenStrongholdStairs(☃, ☃, ☃, ☃);
    }
    
    public boolean a(World ☃, Random ☃, StructureBoundingBox ☃)
    {
      if (a(☃, ☃)) {
        return false;
      }
      a(☃, ☃, 0, 0, 0, 4, 4, 6, true, ☃, WorldGenStrongholdPieces.c());
      
      a(☃, ☃, ☃, this.d, 1, 1, 0);
      
      a(☃, ☃, ☃, WorldGenStrongholdPieces.WorldGenStrongholdPiece.WorldGenStrongholdDoorType.OPENING, 1, 1, 6);
      
      a(☃, ☃, ☃, 0.1F, 1, 2, 1, Blocks.TORCH.getBlockData());
      a(☃, ☃, ☃, 0.1F, 3, 2, 1, Blocks.TORCH.getBlockData());
      a(☃, ☃, ☃, 0.1F, 1, 2, 5, Blocks.TORCH.getBlockData());
      a(☃, ☃, ☃, 0.1F, 3, 2, 5, Blocks.TORCH.getBlockData());
      if (this.a) {
        a(☃, ☃, 0, 1, 2, 0, 3, 4, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
      }
      if (this.b) {
        a(☃, ☃, 4, 1, 2, 4, 3, 4, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
      }
      return true;
    }
  }
  
  public static class WorldGenStrongholdChestCorridor
    extends WorldGenStrongholdPieces.WorldGenStrongholdPiece
  {
    private static final List<StructurePieceTreasure> a = Lists.newArrayList(new StructurePieceTreasure[] { new StructurePieceTreasure(Items.ENDER_PEARL, 0, 1, 1, 10), new StructurePieceTreasure(Items.DIAMOND, 0, 1, 3, 3), new StructurePieceTreasure(Items.IRON_INGOT, 0, 1, 5, 10), new StructurePieceTreasure(Items.GOLD_INGOT, 0, 1, 3, 5), new StructurePieceTreasure(Items.REDSTONE, 0, 4, 9, 5), new StructurePieceTreasure(Items.BREAD, 0, 1, 3, 15), new StructurePieceTreasure(Items.APPLE, 0, 1, 3, 15), new StructurePieceTreasure(Items.IRON_PICKAXE, 0, 1, 1, 5), new StructurePieceTreasure(Items.IRON_SWORD, 0, 1, 1, 5), new StructurePieceTreasure(Items.IRON_CHESTPLATE, 0, 1, 1, 5), new StructurePieceTreasure(Items.IRON_HELMET, 0, 1, 1, 5), new StructurePieceTreasure(Items.IRON_LEGGINGS, 0, 1, 1, 5), new StructurePieceTreasure(Items.IRON_BOOTS, 0, 1, 1, 5), new StructurePieceTreasure(Items.GOLDEN_APPLE, 0, 1, 1, 1), new StructurePieceTreasure(Items.SADDLE, 0, 1, 1, 1), new StructurePieceTreasure(Items.IRON_HORSE_ARMOR, 0, 1, 1, 1), new StructurePieceTreasure(Items.GOLDEN_HORSE_ARMOR, 0, 1, 1, 1), new StructurePieceTreasure(Items.DIAMOND_HORSE_ARMOR, 0, 1, 1, 1) });
    private boolean b;
    
    public WorldGenStrongholdChestCorridor() {}
    
    public WorldGenStrongholdChestCorridor(int ☃, Random ☃, StructureBoundingBox ☃, EnumDirection ☃)
    {
      super();
      
      this.m = ☃;
      this.d = a(☃);
      this.l = ☃;
    }
    
    protected void a(NBTTagCompound ☃)
    {
      super.a(☃);
      ☃.setBoolean("Chest", this.b);
    }
    
    protected void b(NBTTagCompound ☃)
    {
      super.b(☃);
      this.b = ☃.getBoolean("Chest");
    }
    
    public void a(StructurePiece ☃, List<StructurePiece> ☃, Random ☃)
    {
      a((WorldGenStrongholdPieces.WorldGenStrongholdStart)☃, ☃, ☃, 1, 1);
    }
    
    public static WorldGenStrongholdChestCorridor a(List<StructurePiece> ☃, Random ☃, int ☃, int ☃, int ☃, EnumDirection ☃, int ☃)
    {
      StructureBoundingBox ☃ = StructureBoundingBox.a(☃, ☃, ☃, -1, -1, 0, 5, 5, 7, ☃);
      if ((!a(☃)) || (StructurePiece.a(☃, ☃) != null)) {
        return null;
      }
      return new WorldGenStrongholdChestCorridor(☃, ☃, ☃, ☃);
    }
    
    public boolean a(World ☃, Random ☃, StructureBoundingBox ☃)
    {
      if (a(☃, ☃)) {
        return false;
      }
      a(☃, ☃, 0, 0, 0, 4, 4, 6, true, ☃, WorldGenStrongholdPieces.c());
      
      a(☃, ☃, ☃, this.d, 1, 1, 0);
      
      a(☃, ☃, ☃, WorldGenStrongholdPieces.WorldGenStrongholdPiece.WorldGenStrongholdDoorType.OPENING, 1, 1, 6);
      
      a(☃, ☃, 3, 1, 2, 3, 1, 4, Blocks.STONEBRICK.getBlockData(), Blocks.STONEBRICK.getBlockData(), false);
      a(☃, Blocks.STONE_SLAB.fromLegacyData(BlockDoubleStepAbstract.EnumStoneSlabVariant.SMOOTHBRICK.a()), 3, 1, 1, ☃);
      a(☃, Blocks.STONE_SLAB.fromLegacyData(BlockDoubleStepAbstract.EnumStoneSlabVariant.SMOOTHBRICK.a()), 3, 1, 5, ☃);
      a(☃, Blocks.STONE_SLAB.fromLegacyData(BlockDoubleStepAbstract.EnumStoneSlabVariant.SMOOTHBRICK.a()), 3, 2, 2, ☃);
      a(☃, Blocks.STONE_SLAB.fromLegacyData(BlockDoubleStepAbstract.EnumStoneSlabVariant.SMOOTHBRICK.a()), 3, 2, 4, ☃);
      for (int ☃ = 2; ☃ <= 4; ☃++) {
        a(☃, Blocks.STONE_SLAB.fromLegacyData(BlockDoubleStepAbstract.EnumStoneSlabVariant.SMOOTHBRICK.a()), 2, 1, ☃, ☃);
      }
      if ((!this.b) && 
        (☃.b(new BlockPosition(a(3, 3), d(2), b(3, 3)))))
      {
        this.b = true;
        a(☃, ☃, ☃, 3, 2, 3, StructurePieceTreasure.a(a, new StructurePieceTreasure[] { Items.ENCHANTED_BOOK.b(☃) }), 2 + ☃.nextInt(2));
      }
      return true;
    }
  }
  
  public static class WorldGenStrongholdStairsStraight
    extends WorldGenStrongholdPieces.WorldGenStrongholdPiece
  {
    public WorldGenStrongholdStairsStraight() {}
    
    public WorldGenStrongholdStairsStraight(int ☃, Random ☃, StructureBoundingBox ☃, EnumDirection ☃)
    {
      super();
      
      this.m = ☃;
      this.d = a(☃);
      this.l = ☃;
    }
    
    public void a(StructurePiece ☃, List<StructurePiece> ☃, Random ☃)
    {
      a((WorldGenStrongholdPieces.WorldGenStrongholdStart)☃, ☃, ☃, 1, 1);
    }
    
    public static WorldGenStrongholdStairsStraight a(List<StructurePiece> ☃, Random ☃, int ☃, int ☃, int ☃, EnumDirection ☃, int ☃)
    {
      StructureBoundingBox ☃ = StructureBoundingBox.a(☃, ☃, ☃, -1, -7, 0, 5, 11, 8, ☃);
      if ((!a(☃)) || (StructurePiece.a(☃, ☃) != null)) {
        return null;
      }
      return new WorldGenStrongholdStairsStraight(☃, ☃, ☃, ☃);
    }
    
    public boolean a(World ☃, Random ☃, StructureBoundingBox ☃)
    {
      if (a(☃, ☃)) {
        return false;
      }
      a(☃, ☃, 0, 0, 0, 4, 10, 7, true, ☃, WorldGenStrongholdPieces.c());
      
      a(☃, ☃, ☃, this.d, 1, 7, 0);
      
      a(☃, ☃, ☃, WorldGenStrongholdPieces.WorldGenStrongholdPiece.WorldGenStrongholdDoorType.OPENING, 1, 1, 7);
      
      int ☃ = a(Blocks.STONE_STAIRS, 2);
      for (int ☃ = 0; ☃ < 6; ☃++)
      {
        a(☃, Blocks.STONE_STAIRS.fromLegacyData(☃), 1, 6 - ☃, 1 + ☃, ☃);
        a(☃, Blocks.STONE_STAIRS.fromLegacyData(☃), 2, 6 - ☃, 1 + ☃, ☃);
        a(☃, Blocks.STONE_STAIRS.fromLegacyData(☃), 3, 6 - ☃, 1 + ☃, ☃);
        if (☃ < 5)
        {
          a(☃, Blocks.STONEBRICK.getBlockData(), 1, 5 - ☃, 1 + ☃, ☃);
          a(☃, Blocks.STONEBRICK.getBlockData(), 2, 5 - ☃, 1 + ☃, ☃);
          a(☃, Blocks.STONEBRICK.getBlockData(), 3, 5 - ☃, 1 + ☃, ☃);
        }
      }
      return true;
    }
  }
  
  public static class WorldGenStrongholdLeftTurn
    extends WorldGenStrongholdPieces.WorldGenStrongholdPiece
  {
    public WorldGenStrongholdLeftTurn() {}
    
    public WorldGenStrongholdLeftTurn(int ☃, Random ☃, StructureBoundingBox ☃, EnumDirection ☃)
    {
      super();
      
      this.m = ☃;
      this.d = a(☃);
      this.l = ☃;
    }
    
    public void a(StructurePiece ☃, List<StructurePiece> ☃, Random ☃)
    {
      if ((this.m == EnumDirection.NORTH) || (this.m == EnumDirection.EAST)) {
        b((WorldGenStrongholdPieces.WorldGenStrongholdStart)☃, ☃, ☃, 1, 1);
      } else {
        c((WorldGenStrongholdPieces.WorldGenStrongholdStart)☃, ☃, ☃, 1, 1);
      }
    }
    
    public static WorldGenStrongholdLeftTurn a(List<StructurePiece> ☃, Random ☃, int ☃, int ☃, int ☃, EnumDirection ☃, int ☃)
    {
      StructureBoundingBox ☃ = StructureBoundingBox.a(☃, ☃, ☃, -1, -1, 0, 5, 5, 5, ☃);
      if ((!a(☃)) || (StructurePiece.a(☃, ☃) != null)) {
        return null;
      }
      return new WorldGenStrongholdLeftTurn(☃, ☃, ☃, ☃);
    }
    
    public boolean a(World ☃, Random ☃, StructureBoundingBox ☃)
    {
      if (a(☃, ☃)) {
        return false;
      }
      a(☃, ☃, 0, 0, 0, 4, 4, 4, true, ☃, WorldGenStrongholdPieces.c());
      
      a(☃, ☃, ☃, this.d, 1, 1, 0);
      if ((this.m == EnumDirection.NORTH) || (this.m == EnumDirection.EAST)) {
        a(☃, ☃, 0, 1, 1, 0, 3, 3, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
      } else {
        a(☃, ☃, 4, 1, 1, 4, 3, 3, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
      }
      return true;
    }
  }
  
  public static class WorldGenStrongholdRightTurn
    extends WorldGenStrongholdPieces.WorldGenStrongholdLeftTurn
  {
    public void a(StructurePiece ☃, List<StructurePiece> ☃, Random ☃)
    {
      if ((this.m == EnumDirection.NORTH) || (this.m == EnumDirection.EAST)) {
        c((WorldGenStrongholdPieces.WorldGenStrongholdStart)☃, ☃, ☃, 1, 1);
      } else {
        b((WorldGenStrongholdPieces.WorldGenStrongholdStart)☃, ☃, ☃, 1, 1);
      }
    }
    
    public boolean a(World ☃, Random ☃, StructureBoundingBox ☃)
    {
      if (a(☃, ☃)) {
        return false;
      }
      a(☃, ☃, 0, 0, 0, 4, 4, 4, true, ☃, WorldGenStrongholdPieces.c());
      
      a(☃, ☃, ☃, this.d, 1, 1, 0);
      if ((this.m == EnumDirection.NORTH) || (this.m == EnumDirection.EAST)) {
        a(☃, ☃, 4, 1, 1, 4, 3, 3, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
      } else {
        a(☃, ☃, 0, 1, 1, 0, 3, 3, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
      }
      return true;
    }
  }
  
  public static class WorldGenStrongholdRoomCrossing
    extends WorldGenStrongholdPieces.WorldGenStrongholdPiece
  {
    private static final List<StructurePieceTreasure> b = Lists.newArrayList(new StructurePieceTreasure[] { new StructurePieceTreasure(Items.IRON_INGOT, 0, 1, 5, 10), new StructurePieceTreasure(Items.GOLD_INGOT, 0, 1, 3, 5), new StructurePieceTreasure(Items.REDSTONE, 0, 4, 9, 5), new StructurePieceTreasure(Items.COAL, 0, 3, 8, 10), new StructurePieceTreasure(Items.BREAD, 0, 1, 3, 15), new StructurePieceTreasure(Items.APPLE, 0, 1, 3, 15), new StructurePieceTreasure(Items.IRON_PICKAXE, 0, 1, 1, 1) });
    protected int a;
    
    public WorldGenStrongholdRoomCrossing() {}
    
    public WorldGenStrongholdRoomCrossing(int ☃, Random ☃, StructureBoundingBox ☃, EnumDirection ☃)
    {
      super();
      
      this.m = ☃;
      this.d = a(☃);
      this.l = ☃;
      this.a = ☃.nextInt(5);
    }
    
    protected void a(NBTTagCompound ☃)
    {
      super.a(☃);
      ☃.setInt("Type", this.a);
    }
    
    protected void b(NBTTagCompound ☃)
    {
      super.b(☃);
      this.a = ☃.getInt("Type");
    }
    
    public void a(StructurePiece ☃, List<StructurePiece> ☃, Random ☃)
    {
      a((WorldGenStrongholdPieces.WorldGenStrongholdStart)☃, ☃, ☃, 4, 1);
      b((WorldGenStrongholdPieces.WorldGenStrongholdStart)☃, ☃, ☃, 1, 4);
      c((WorldGenStrongholdPieces.WorldGenStrongholdStart)☃, ☃, ☃, 1, 4);
    }
    
    public static WorldGenStrongholdRoomCrossing a(List<StructurePiece> ☃, Random ☃, int ☃, int ☃, int ☃, EnumDirection ☃, int ☃)
    {
      StructureBoundingBox ☃ = StructureBoundingBox.a(☃, ☃, ☃, -4, -1, 0, 11, 7, 11, ☃);
      if ((!a(☃)) || (StructurePiece.a(☃, ☃) != null)) {
        return null;
      }
      return new WorldGenStrongholdRoomCrossing(☃, ☃, ☃, ☃);
    }
    
    public boolean a(World ☃, Random ☃, StructureBoundingBox ☃)
    {
      if (a(☃, ☃)) {
        return false;
      }
      a(☃, ☃, 0, 0, 0, 10, 6, 10, true, ☃, WorldGenStrongholdPieces.c());
      
      a(☃, ☃, ☃, this.d, 4, 1, 0);
      
      a(☃, ☃, 4, 1, 10, 6, 3, 10, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
      a(☃, ☃, 0, 1, 4, 0, 3, 6, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
      a(☃, ☃, 10, 1, 4, 10, 3, 6, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
      switch (this.a)
      {
      default: 
        break;
      case 0: 
        a(☃, Blocks.STONEBRICK.getBlockData(), 5, 1, 5, ☃);
        a(☃, Blocks.STONEBRICK.getBlockData(), 5, 2, 5, ☃);
        a(☃, Blocks.STONEBRICK.getBlockData(), 5, 3, 5, ☃);
        a(☃, Blocks.TORCH.getBlockData(), 4, 3, 5, ☃);
        a(☃, Blocks.TORCH.getBlockData(), 6, 3, 5, ☃);
        a(☃, Blocks.TORCH.getBlockData(), 5, 3, 4, ☃);
        a(☃, Blocks.TORCH.getBlockData(), 5, 3, 6, ☃);
        a(☃, Blocks.STONE_SLAB.getBlockData(), 4, 1, 4, ☃);
        a(☃, Blocks.STONE_SLAB.getBlockData(), 4, 1, 5, ☃);
        a(☃, Blocks.STONE_SLAB.getBlockData(), 4, 1, 6, ☃);
        a(☃, Blocks.STONE_SLAB.getBlockData(), 6, 1, 4, ☃);
        a(☃, Blocks.STONE_SLAB.getBlockData(), 6, 1, 5, ☃);
        a(☃, Blocks.STONE_SLAB.getBlockData(), 6, 1, 6, ☃);
        a(☃, Blocks.STONE_SLAB.getBlockData(), 5, 1, 4, ☃);
        a(☃, Blocks.STONE_SLAB.getBlockData(), 5, 1, 6, ☃);
        break;
      case 1: 
        for (int ☃ = 0; ☃ < 5; ☃++)
        {
          a(☃, Blocks.STONEBRICK.getBlockData(), 3, 1, 3 + ☃, ☃);
          a(☃, Blocks.STONEBRICK.getBlockData(), 7, 1, 3 + ☃, ☃);
          a(☃, Blocks.STONEBRICK.getBlockData(), 3 + ☃, 1, 3, ☃);
          a(☃, Blocks.STONEBRICK.getBlockData(), 3 + ☃, 1, 7, ☃);
        }
        a(☃, Blocks.STONEBRICK.getBlockData(), 5, 1, 5, ☃);
        a(☃, Blocks.STONEBRICK.getBlockData(), 5, 2, 5, ☃);
        a(☃, Blocks.STONEBRICK.getBlockData(), 5, 3, 5, ☃);
        a(☃, Blocks.FLOWING_WATER.getBlockData(), 5, 4, 5, ☃);
        break;
      case 2: 
        for (int ☃ = 1; ☃ <= 9; ☃++)
        {
          a(☃, Blocks.COBBLESTONE.getBlockData(), 1, 3, ☃, ☃);
          a(☃, Blocks.COBBLESTONE.getBlockData(), 9, 3, ☃, ☃);
        }
        for (int ☃ = 1; ☃ <= 9; ☃++)
        {
          a(☃, Blocks.COBBLESTONE.getBlockData(), ☃, 3, 1, ☃);
          a(☃, Blocks.COBBLESTONE.getBlockData(), ☃, 3, 9, ☃);
        }
        a(☃, Blocks.COBBLESTONE.getBlockData(), 5, 1, 4, ☃);
        a(☃, Blocks.COBBLESTONE.getBlockData(), 5, 1, 6, ☃);
        a(☃, Blocks.COBBLESTONE.getBlockData(), 5, 3, 4, ☃);
        a(☃, Blocks.COBBLESTONE.getBlockData(), 5, 3, 6, ☃);
        a(☃, Blocks.COBBLESTONE.getBlockData(), 4, 1, 5, ☃);
        a(☃, Blocks.COBBLESTONE.getBlockData(), 6, 1, 5, ☃);
        a(☃, Blocks.COBBLESTONE.getBlockData(), 4, 3, 5, ☃);
        a(☃, Blocks.COBBLESTONE.getBlockData(), 6, 3, 5, ☃);
        for (int ☃ = 1; ☃ <= 3; ☃++)
        {
          a(☃, Blocks.COBBLESTONE.getBlockData(), 4, ☃, 4, ☃);
          a(☃, Blocks.COBBLESTONE.getBlockData(), 6, ☃, 4, ☃);
          a(☃, Blocks.COBBLESTONE.getBlockData(), 4, ☃, 6, ☃);
          a(☃, Blocks.COBBLESTONE.getBlockData(), 6, ☃, 6, ☃);
        }
        a(☃, Blocks.TORCH.getBlockData(), 5, 3, 5, ☃);
        for (int ☃ = 2; ☃ <= 8; ☃++)
        {
          a(☃, Blocks.PLANKS.getBlockData(), 2, 3, ☃, ☃);
          a(☃, Blocks.PLANKS.getBlockData(), 3, 3, ☃, ☃);
          if ((☃ <= 3) || (☃ >= 7))
          {
            a(☃, Blocks.PLANKS.getBlockData(), 4, 3, ☃, ☃);
            a(☃, Blocks.PLANKS.getBlockData(), 5, 3, ☃, ☃);
            a(☃, Blocks.PLANKS.getBlockData(), 6, 3, ☃, ☃);
          }
          a(☃, Blocks.PLANKS.getBlockData(), 7, 3, ☃, ☃);
          a(☃, Blocks.PLANKS.getBlockData(), 8, 3, ☃, ☃);
        }
        a(☃, Blocks.LADDER.fromLegacyData(a(Blocks.LADDER, EnumDirection.WEST.a())), 9, 1, 3, ☃);
        a(☃, Blocks.LADDER.fromLegacyData(a(Blocks.LADDER, EnumDirection.WEST.a())), 9, 2, 3, ☃);
        a(☃, Blocks.LADDER.fromLegacyData(a(Blocks.LADDER, EnumDirection.WEST.a())), 9, 3, 3, ☃);
        
        a(☃, ☃, ☃, 3, 4, 8, StructurePieceTreasure.a(b, new StructurePieceTreasure[] { Items.ENCHANTED_BOOK.b(☃) }), 1 + ☃.nextInt(4));
      }
      return true;
    }
  }
  
  public static class WorldGenStrongholdPrison
    extends WorldGenStrongholdPieces.WorldGenStrongholdPiece
  {
    public WorldGenStrongholdPrison() {}
    
    public WorldGenStrongholdPrison(int ☃, Random ☃, StructureBoundingBox ☃, EnumDirection ☃)
    {
      super();
      
      this.m = ☃;
      this.d = a(☃);
      this.l = ☃;
    }
    
    public void a(StructurePiece ☃, List<StructurePiece> ☃, Random ☃)
    {
      a((WorldGenStrongholdPieces.WorldGenStrongholdStart)☃, ☃, ☃, 1, 1);
    }
    
    public static WorldGenStrongholdPrison a(List<StructurePiece> ☃, Random ☃, int ☃, int ☃, int ☃, EnumDirection ☃, int ☃)
    {
      StructureBoundingBox ☃ = StructureBoundingBox.a(☃, ☃, ☃, -1, -1, 0, 9, 5, 11, ☃);
      if ((!a(☃)) || (StructurePiece.a(☃, ☃) != null)) {
        return null;
      }
      return new WorldGenStrongholdPrison(☃, ☃, ☃, ☃);
    }
    
    public boolean a(World ☃, Random ☃, StructureBoundingBox ☃)
    {
      if (a(☃, ☃)) {
        return false;
      }
      a(☃, ☃, 0, 0, 0, 8, 4, 10, true, ☃, WorldGenStrongholdPieces.c());
      
      a(☃, ☃, ☃, this.d, 1, 1, 0);
      
      a(☃, ☃, 1, 1, 10, 3, 3, 10, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
      
      a(☃, ☃, 4, 1, 1, 4, 3, 1, false, ☃, WorldGenStrongholdPieces.c());
      a(☃, ☃, 4, 1, 3, 4, 3, 3, false, ☃, WorldGenStrongholdPieces.c());
      a(☃, ☃, 4, 1, 7, 4, 3, 7, false, ☃, WorldGenStrongholdPieces.c());
      a(☃, ☃, 4, 1, 9, 4, 3, 9, false, ☃, WorldGenStrongholdPieces.c());
      
      a(☃, ☃, 4, 1, 4, 4, 3, 6, Blocks.IRON_BARS.getBlockData(), Blocks.IRON_BARS.getBlockData(), false);
      a(☃, ☃, 5, 1, 5, 7, 3, 5, Blocks.IRON_BARS.getBlockData(), Blocks.IRON_BARS.getBlockData(), false);
      
      a(☃, Blocks.IRON_BARS.getBlockData(), 4, 3, 2, ☃);
      a(☃, Blocks.IRON_BARS.getBlockData(), 4, 3, 8, ☃);
      a(☃, Blocks.IRON_DOOR.fromLegacyData(a(Blocks.IRON_DOOR, 3)), 4, 1, 2, ☃);
      a(☃, Blocks.IRON_DOOR.fromLegacyData(a(Blocks.IRON_DOOR, 3) + 8), 4, 2, 2, ☃);
      a(☃, Blocks.IRON_DOOR.fromLegacyData(a(Blocks.IRON_DOOR, 3)), 4, 1, 8, ☃);
      a(☃, Blocks.IRON_DOOR.fromLegacyData(a(Blocks.IRON_DOOR, 3) + 8), 4, 2, 8, ☃);
      
      return true;
    }
  }
  
  public static class WorldGenStrongholdLibrary
    extends WorldGenStrongholdPieces.WorldGenStrongholdPiece
  {
    private static final List<StructurePieceTreasure> a = Lists.newArrayList(new StructurePieceTreasure[] { new StructurePieceTreasure(Items.BOOK, 0, 1, 3, 20), new StructurePieceTreasure(Items.PAPER, 0, 2, 7, 20), new StructurePieceTreasure(Items.MAP, 0, 1, 1, 1), new StructurePieceTreasure(Items.COMPASS, 0, 1, 1, 1) });
    private boolean b;
    
    public WorldGenStrongholdLibrary() {}
    
    public WorldGenStrongholdLibrary(int ☃, Random ☃, StructureBoundingBox ☃, EnumDirection ☃)
    {
      super();
      
      this.m = ☃;
      this.d = a(☃);
      this.l = ☃;
      this.b = (☃.d() > 6);
    }
    
    protected void a(NBTTagCompound ☃)
    {
      super.a(☃);
      ☃.setBoolean("Tall", this.b);
    }
    
    protected void b(NBTTagCompound ☃)
    {
      super.b(☃);
      this.b = ☃.getBoolean("Tall");
    }
    
    public static WorldGenStrongholdLibrary a(List<StructurePiece> ☃, Random ☃, int ☃, int ☃, int ☃, EnumDirection ☃, int ☃)
    {
      StructureBoundingBox ☃ = StructureBoundingBox.a(☃, ☃, ☃, -4, -1, 0, 14, 11, 15, ☃);
      if ((!a(☃)) || (StructurePiece.a(☃, ☃) != null))
      {
        ☃ = StructureBoundingBox.a(☃, ☃, ☃, -4, -1, 0, 14, 6, 15, ☃);
        if ((!a(☃)) || (StructurePiece.a(☃, ☃) != null)) {
          return null;
        }
      }
      return new WorldGenStrongholdLibrary(☃, ☃, ☃, ☃);
    }
    
    public boolean a(World ☃, Random ☃, StructureBoundingBox ☃)
    {
      if (a(☃, ☃)) {
        return false;
      }
      int ☃ = 11;
      if (!this.b) {
        ☃ = 6;
      }
      a(☃, ☃, 0, 0, 0, 13, ☃ - 1, 14, true, ☃, WorldGenStrongholdPieces.c());
      
      a(☃, ☃, ☃, this.d, 4, 1, 0);
      
      a(☃, ☃, ☃, 0.07F, 2, 1, 1, 11, 4, 13, Blocks.WEB.getBlockData(), Blocks.WEB.getBlockData(), false);
      
      int ☃ = 1;
      int ☃ = 12;
      for (int ☃ = 1; ☃ <= 13; ☃++) {
        if ((☃ - 1) % 4 == 0)
        {
          a(☃, ☃, 1, 1, ☃, 1, 4, ☃, Blocks.PLANKS.getBlockData(), Blocks.PLANKS.getBlockData(), false);
          a(☃, ☃, 12, 1, ☃, 12, 4, ☃, Blocks.PLANKS.getBlockData(), Blocks.PLANKS.getBlockData(), false);
          
          a(☃, Blocks.TORCH.getBlockData(), 2, 3, ☃, ☃);
          a(☃, Blocks.TORCH.getBlockData(), 11, 3, ☃, ☃);
          if (this.b)
          {
            a(☃, ☃, 1, 6, ☃, 1, 9, ☃, Blocks.PLANKS.getBlockData(), Blocks.PLANKS.getBlockData(), false);
            a(☃, ☃, 12, 6, ☃, 12, 9, ☃, Blocks.PLANKS.getBlockData(), Blocks.PLANKS.getBlockData(), false);
          }
        }
        else
        {
          a(☃, ☃, 1, 1, ☃, 1, 4, ☃, Blocks.BOOKSHELF.getBlockData(), Blocks.BOOKSHELF.getBlockData(), false);
          a(☃, ☃, 12, 1, ☃, 12, 4, ☃, Blocks.BOOKSHELF.getBlockData(), Blocks.BOOKSHELF.getBlockData(), false);
          if (this.b)
          {
            a(☃, ☃, 1, 6, ☃, 1, 9, ☃, Blocks.BOOKSHELF.getBlockData(), Blocks.BOOKSHELF.getBlockData(), false);
            a(☃, ☃, 12, 6, ☃, 12, 9, ☃, Blocks.BOOKSHELF.getBlockData(), Blocks.BOOKSHELF.getBlockData(), false);
          }
        }
      }
      for (int ☃ = 3; ☃ < 12; ☃ += 2)
      {
        a(☃, ☃, 3, 1, ☃, 4, 3, ☃, Blocks.BOOKSHELF.getBlockData(), Blocks.BOOKSHELF.getBlockData(), false);
        a(☃, ☃, 6, 1, ☃, 7, 3, ☃, Blocks.BOOKSHELF.getBlockData(), Blocks.BOOKSHELF.getBlockData(), false);
        a(☃, ☃, 9, 1, ☃, 10, 3, ☃, Blocks.BOOKSHELF.getBlockData(), Blocks.BOOKSHELF.getBlockData(), false);
      }
      if (this.b)
      {
        a(☃, ☃, 1, 5, 1, 3, 5, 13, Blocks.PLANKS.getBlockData(), Blocks.PLANKS.getBlockData(), false);
        a(☃, ☃, 10, 5, 1, 12, 5, 13, Blocks.PLANKS.getBlockData(), Blocks.PLANKS.getBlockData(), false);
        a(☃, ☃, 4, 5, 1, 9, 5, 2, Blocks.PLANKS.getBlockData(), Blocks.PLANKS.getBlockData(), false);
        a(☃, ☃, 4, 5, 12, 9, 5, 13, Blocks.PLANKS.getBlockData(), Blocks.PLANKS.getBlockData(), false);
        
        a(☃, Blocks.PLANKS.getBlockData(), 9, 5, 11, ☃);
        a(☃, Blocks.PLANKS.getBlockData(), 8, 5, 11, ☃);
        a(☃, Blocks.PLANKS.getBlockData(), 9, 5, 10, ☃);
        
        a(☃, ☃, 3, 6, 2, 3, 6, 12, Blocks.FENCE.getBlockData(), Blocks.FENCE.getBlockData(), false);
        a(☃, ☃, 10, 6, 2, 10, 6, 10, Blocks.FENCE.getBlockData(), Blocks.FENCE.getBlockData(), false);
        a(☃, ☃, 4, 6, 2, 9, 6, 2, Blocks.FENCE.getBlockData(), Blocks.FENCE.getBlockData(), false);
        a(☃, ☃, 4, 6, 12, 8, 6, 12, Blocks.FENCE.getBlockData(), Blocks.FENCE.getBlockData(), false);
        a(☃, Blocks.FENCE.getBlockData(), 9, 6, 11, ☃);
        a(☃, Blocks.FENCE.getBlockData(), 8, 6, 11, ☃);
        a(☃, Blocks.FENCE.getBlockData(), 9, 6, 10, ☃);
        
        int ☃ = a(Blocks.LADDER, 3);
        a(☃, Blocks.LADDER.fromLegacyData(☃), 10, 1, 13, ☃);
        a(☃, Blocks.LADDER.fromLegacyData(☃), 10, 2, 13, ☃);
        a(☃, Blocks.LADDER.fromLegacyData(☃), 10, 3, 13, ☃);
        a(☃, Blocks.LADDER.fromLegacyData(☃), 10, 4, 13, ☃);
        a(☃, Blocks.LADDER.fromLegacyData(☃), 10, 5, 13, ☃);
        a(☃, Blocks.LADDER.fromLegacyData(☃), 10, 6, 13, ☃);
        a(☃, Blocks.LADDER.fromLegacyData(☃), 10, 7, 13, ☃);
        
        int ☃ = 7;
        int ☃ = 7;
        a(☃, Blocks.FENCE.getBlockData(), ☃ - 1, 9, ☃, ☃);
        a(☃, Blocks.FENCE.getBlockData(), ☃, 9, ☃, ☃);
        a(☃, Blocks.FENCE.getBlockData(), ☃ - 1, 8, ☃, ☃);
        a(☃, Blocks.FENCE.getBlockData(), ☃, 8, ☃, ☃);
        a(☃, Blocks.FENCE.getBlockData(), ☃ - 1, 7, ☃, ☃);
        a(☃, Blocks.FENCE.getBlockData(), ☃, 7, ☃, ☃);
        
        a(☃, Blocks.FENCE.getBlockData(), ☃ - 2, 7, ☃, ☃);
        a(☃, Blocks.FENCE.getBlockData(), ☃ + 1, 7, ☃, ☃);
        a(☃, Blocks.FENCE.getBlockData(), ☃ - 1, 7, ☃ - 1, ☃);
        a(☃, Blocks.FENCE.getBlockData(), ☃ - 1, 7, ☃ + 1, ☃);
        a(☃, Blocks.FENCE.getBlockData(), ☃, 7, ☃ - 1, ☃);
        a(☃, Blocks.FENCE.getBlockData(), ☃, 7, ☃ + 1, ☃);
        
        a(☃, Blocks.TORCH.getBlockData(), ☃ - 2, 8, ☃, ☃);
        a(☃, Blocks.TORCH.getBlockData(), ☃ + 1, 8, ☃, ☃);
        a(☃, Blocks.TORCH.getBlockData(), ☃ - 1, 8, ☃ - 1, ☃);
        a(☃, Blocks.TORCH.getBlockData(), ☃ - 1, 8, ☃ + 1, ☃);
        a(☃, Blocks.TORCH.getBlockData(), ☃, 8, ☃ - 1, ☃);
        a(☃, Blocks.TORCH.getBlockData(), ☃, 8, ☃ + 1, ☃);
      }
      a(☃, ☃, ☃, 3, 3, 5, StructurePieceTreasure.a(a, new StructurePieceTreasure[] { Items.ENCHANTED_BOOK.a(☃, 1, 5, 2) }), 1 + ☃.nextInt(4));
      if (this.b)
      {
        a(☃, Blocks.AIR.getBlockData(), 12, 9, 1, ☃);
        a(☃, ☃, ☃, 12, 8, 1, StructurePieceTreasure.a(a, new StructurePieceTreasure[] { Items.ENCHANTED_BOOK.a(☃, 1, 5, 2) }), 1 + ☃.nextInt(4));
      }
      return true;
    }
  }
  
  public static class WorldGenStrongholdCrossing
    extends WorldGenStrongholdPieces.WorldGenStrongholdPiece
  {
    private boolean a;
    private boolean b;
    private boolean c;
    private boolean e;
    
    public WorldGenStrongholdCrossing() {}
    
    public WorldGenStrongholdCrossing(int ☃, Random ☃, StructureBoundingBox ☃, EnumDirection ☃)
    {
      super();
      
      this.m = ☃;
      this.d = a(☃);
      this.l = ☃;
      
      this.a = ☃.nextBoolean();
      this.b = ☃.nextBoolean();
      this.c = ☃.nextBoolean();
      this.e = (☃.nextInt(3) > 0);
    }
    
    protected void a(NBTTagCompound ☃)
    {
      super.a(☃);
      ☃.setBoolean("leftLow", this.a);
      ☃.setBoolean("leftHigh", this.b);
      ☃.setBoolean("rightLow", this.c);
      ☃.setBoolean("rightHigh", this.e);
    }
    
    protected void b(NBTTagCompound ☃)
    {
      super.b(☃);
      this.a = ☃.getBoolean("leftLow");
      this.b = ☃.getBoolean("leftHigh");
      this.c = ☃.getBoolean("rightLow");
      this.e = ☃.getBoolean("rightHigh");
    }
    
    public void a(StructurePiece ☃, List<StructurePiece> ☃, Random ☃)
    {
      int ☃ = 3;
      int ☃ = 5;
      if ((this.m == EnumDirection.WEST) || (this.m == EnumDirection.NORTH))
      {
        ☃ = 8 - ☃;
        ☃ = 8 - ☃;
      }
      a((WorldGenStrongholdPieces.WorldGenStrongholdStart)☃, ☃, ☃, 5, 1);
      if (this.a) {
        b((WorldGenStrongholdPieces.WorldGenStrongholdStart)☃, ☃, ☃, ☃, 1);
      }
      if (this.b) {
        b((WorldGenStrongholdPieces.WorldGenStrongholdStart)☃, ☃, ☃, ☃, 7);
      }
      if (this.c) {
        c((WorldGenStrongholdPieces.WorldGenStrongholdStart)☃, ☃, ☃, ☃, 1);
      }
      if (this.e) {
        c((WorldGenStrongholdPieces.WorldGenStrongholdStart)☃, ☃, ☃, ☃, 7);
      }
    }
    
    public static WorldGenStrongholdCrossing a(List<StructurePiece> ☃, Random ☃, int ☃, int ☃, int ☃, EnumDirection ☃, int ☃)
    {
      StructureBoundingBox ☃ = StructureBoundingBox.a(☃, ☃, ☃, -4, -3, 0, 10, 9, 11, ☃);
      if ((!a(☃)) || (StructurePiece.a(☃, ☃) != null)) {
        return null;
      }
      return new WorldGenStrongholdCrossing(☃, ☃, ☃, ☃);
    }
    
    public boolean a(World ☃, Random ☃, StructureBoundingBox ☃)
    {
      if (a(☃, ☃)) {
        return false;
      }
      a(☃, ☃, 0, 0, 0, 9, 8, 10, true, ☃, WorldGenStrongholdPieces.c());
      
      a(☃, ☃, ☃, this.d, 4, 3, 0);
      if (this.a) {
        a(☃, ☃, 0, 3, 1, 0, 5, 3, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
      }
      if (this.c) {
        a(☃, ☃, 9, 3, 1, 9, 5, 3, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
      }
      if (this.b) {
        a(☃, ☃, 0, 5, 7, 0, 7, 9, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
      }
      if (this.e) {
        a(☃, ☃, 9, 5, 7, 9, 7, 9, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
      }
      a(☃, ☃, 5, 1, 10, 7, 3, 10, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
      
      a(☃, ☃, 1, 2, 1, 8, 2, 6, false, ☃, WorldGenStrongholdPieces.c());
      
      a(☃, ☃, 4, 1, 5, 4, 4, 9, false, ☃, WorldGenStrongholdPieces.c());
      a(☃, ☃, 8, 1, 5, 8, 4, 9, false, ☃, WorldGenStrongholdPieces.c());
      
      a(☃, ☃, 1, 4, 7, 3, 4, 9, false, ☃, WorldGenStrongholdPieces.c());
      
      a(☃, ☃, 1, 3, 5, 3, 3, 6, false, ☃, WorldGenStrongholdPieces.c());
      a(☃, ☃, 1, 3, 4, 3, 3, 4, Blocks.STONE_SLAB.getBlockData(), Blocks.STONE_SLAB.getBlockData(), false);
      a(☃, ☃, 1, 4, 6, 3, 4, 6, Blocks.STONE_SLAB.getBlockData(), Blocks.STONE_SLAB.getBlockData(), false);
      
      a(☃, ☃, 5, 1, 7, 7, 1, 8, false, ☃, WorldGenStrongholdPieces.c());
      a(☃, ☃, 5, 1, 9, 7, 1, 9, Blocks.STONE_SLAB.getBlockData(), Blocks.STONE_SLAB.getBlockData(), false);
      a(☃, ☃, 5, 2, 7, 7, 2, 7, Blocks.STONE_SLAB.getBlockData(), Blocks.STONE_SLAB.getBlockData(), false);
      
      a(☃, ☃, 4, 5, 7, 4, 5, 9, Blocks.STONE_SLAB.getBlockData(), Blocks.STONE_SLAB.getBlockData(), false);
      a(☃, ☃, 8, 5, 7, 8, 5, 9, Blocks.STONE_SLAB.getBlockData(), Blocks.STONE_SLAB.getBlockData(), false);
      a(☃, ☃, 5, 5, 7, 7, 5, 9, Blocks.DOUBLE_STONE_SLAB.getBlockData(), Blocks.DOUBLE_STONE_SLAB.getBlockData(), false);
      a(☃, Blocks.TORCH.getBlockData(), 6, 5, 6, ☃);
      
      return true;
    }
  }
  
  public static class WorldGenStrongholdPortalRoom
    extends WorldGenStrongholdPieces.WorldGenStrongholdPiece
  {
    private boolean a;
    
    public WorldGenStrongholdPortalRoom() {}
    
    public WorldGenStrongholdPortalRoom(int ☃, Random ☃, StructureBoundingBox ☃, EnumDirection ☃)
    {
      super();
      
      this.m = ☃;
      this.l = ☃;
    }
    
    protected void a(NBTTagCompound ☃)
    {
      super.a(☃);
      ☃.setBoolean("Mob", this.a);
    }
    
    protected void b(NBTTagCompound ☃)
    {
      super.b(☃);
      this.a = ☃.getBoolean("Mob");
    }
    
    public void a(StructurePiece ☃, List<StructurePiece> ☃, Random ☃)
    {
      if (☃ != null) {
        ((WorldGenStrongholdPieces.WorldGenStrongholdStart)☃).b = this;
      }
    }
    
    public static WorldGenStrongholdPortalRoom a(List<StructurePiece> ☃, Random ☃, int ☃, int ☃, int ☃, EnumDirection ☃, int ☃)
    {
      StructureBoundingBox ☃ = StructureBoundingBox.a(☃, ☃, ☃, -4, -1, 0, 11, 8, 16, ☃);
      if ((!a(☃)) || (StructurePiece.a(☃, ☃) != null)) {
        return null;
      }
      return new WorldGenStrongholdPortalRoom(☃, ☃, ☃, ☃);
    }
    
    public boolean a(World ☃, Random ☃, StructureBoundingBox ☃)
    {
      a(☃, ☃, 0, 0, 0, 10, 7, 15, false, ☃, WorldGenStrongholdPieces.c());
      
      a(☃, ☃, ☃, WorldGenStrongholdPieces.WorldGenStrongholdPiece.WorldGenStrongholdDoorType.GRATES, 4, 1, 0);
      
      int ☃ = 6;
      a(☃, ☃, 1, ☃, 1, 1, ☃, 14, false, ☃, WorldGenStrongholdPieces.c());
      a(☃, ☃, 9, ☃, 1, 9, ☃, 14, false, ☃, WorldGenStrongholdPieces.c());
      a(☃, ☃, 2, ☃, 1, 8, ☃, 2, false, ☃, WorldGenStrongholdPieces.c());
      a(☃, ☃, 2, ☃, 14, 8, ☃, 14, false, ☃, WorldGenStrongholdPieces.c());
      
      a(☃, ☃, 1, 1, 1, 2, 1, 4, false, ☃, WorldGenStrongholdPieces.c());
      a(☃, ☃, 8, 1, 1, 9, 1, 4, false, ☃, WorldGenStrongholdPieces.c());
      a(☃, ☃, 1, 1, 1, 1, 1, 3, Blocks.FLOWING_LAVA.getBlockData(), Blocks.FLOWING_LAVA.getBlockData(), false);
      a(☃, ☃, 9, 1, 1, 9, 1, 3, Blocks.FLOWING_LAVA.getBlockData(), Blocks.FLOWING_LAVA.getBlockData(), false);
      
      a(☃, ☃, 3, 1, 8, 7, 1, 12, false, ☃, WorldGenStrongholdPieces.c());
      a(☃, ☃, 4, 1, 9, 6, 1, 11, Blocks.FLOWING_LAVA.getBlockData(), Blocks.FLOWING_LAVA.getBlockData(), false);
      for (int ☃ = 3; ☃ < 14; ☃ += 2)
      {
        a(☃, ☃, 0, 3, ☃, 0, 4, ☃, Blocks.IRON_BARS.getBlockData(), Blocks.IRON_BARS.getBlockData(), false);
        a(☃, ☃, 10, 3, ☃, 10, 4, ☃, Blocks.IRON_BARS.getBlockData(), Blocks.IRON_BARS.getBlockData(), false);
      }
      for (int ☃ = 2; ☃ < 9; ☃ += 2) {
        a(☃, ☃, ☃, 3, 15, ☃, 4, 15, Blocks.IRON_BARS.getBlockData(), Blocks.IRON_BARS.getBlockData(), false);
      }
      int ☃ = a(Blocks.STONE_BRICK_STAIRS, 3);
      a(☃, ☃, 4, 1, 5, 6, 1, 7, false, ☃, WorldGenStrongholdPieces.c());
      a(☃, ☃, 4, 2, 6, 6, 2, 7, false, ☃, WorldGenStrongholdPieces.c());
      a(☃, ☃, 4, 3, 7, 6, 3, 7, false, ☃, WorldGenStrongholdPieces.c());
      for (int ☃ = 4; ☃ <= 6; ☃++)
      {
        a(☃, Blocks.STONE_BRICK_STAIRS.fromLegacyData(☃), ☃, 1, 4, ☃);
        a(☃, Blocks.STONE_BRICK_STAIRS.fromLegacyData(☃), ☃, 2, 5, ☃);
        a(☃, Blocks.STONE_BRICK_STAIRS.fromLegacyData(☃), ☃, 3, 6, ☃);
      }
      int ☃ = EnumDirection.NORTH.b();
      int ☃ = EnumDirection.SOUTH.b();
      int ☃ = EnumDirection.EAST.b();
      int ☃ = EnumDirection.WEST.b();
      if (this.m != null) {
        switch (WorldGenStrongholdPieces.3.b[this.m.ordinal()])
        {
        case 2: 
          ☃ = EnumDirection.SOUTH.b();
          ☃ = EnumDirection.NORTH.b();
          break;
        case 4: 
          ☃ = EnumDirection.EAST.b();
          ☃ = EnumDirection.WEST.b();
          ☃ = EnumDirection.SOUTH.b();
          ☃ = EnumDirection.NORTH.b();
          break;
        case 3: 
          ☃ = EnumDirection.WEST.b();
          ☃ = EnumDirection.EAST.b();
          ☃ = EnumDirection.SOUTH.b();
          ☃ = EnumDirection.NORTH.b();
        }
      }
      a(☃, Blocks.END_PORTAL_FRAME.fromLegacyData(☃).set(BlockEnderPortalFrame.EYE, Boolean.valueOf(☃.nextFloat() > 0.9F)), 4, 3, 8, ☃);
      a(☃, Blocks.END_PORTAL_FRAME.fromLegacyData(☃).set(BlockEnderPortalFrame.EYE, Boolean.valueOf(☃.nextFloat() > 0.9F)), 5, 3, 8, ☃);
      a(☃, Blocks.END_PORTAL_FRAME.fromLegacyData(☃).set(BlockEnderPortalFrame.EYE, Boolean.valueOf(☃.nextFloat() > 0.9F)), 6, 3, 8, ☃);
      a(☃, Blocks.END_PORTAL_FRAME.fromLegacyData(☃).set(BlockEnderPortalFrame.EYE, Boolean.valueOf(☃.nextFloat() > 0.9F)), 4, 3, 12, ☃);
      a(☃, Blocks.END_PORTAL_FRAME.fromLegacyData(☃).set(BlockEnderPortalFrame.EYE, Boolean.valueOf(☃.nextFloat() > 0.9F)), 5, 3, 12, ☃);
      a(☃, Blocks.END_PORTAL_FRAME.fromLegacyData(☃).set(BlockEnderPortalFrame.EYE, Boolean.valueOf(☃.nextFloat() > 0.9F)), 6, 3, 12, ☃);
      a(☃, Blocks.END_PORTAL_FRAME.fromLegacyData(☃).set(BlockEnderPortalFrame.EYE, Boolean.valueOf(☃.nextFloat() > 0.9F)), 3, 3, 9, ☃);
      a(☃, Blocks.END_PORTAL_FRAME.fromLegacyData(☃).set(BlockEnderPortalFrame.EYE, Boolean.valueOf(☃.nextFloat() > 0.9F)), 3, 3, 10, ☃);
      a(☃, Blocks.END_PORTAL_FRAME.fromLegacyData(☃).set(BlockEnderPortalFrame.EYE, Boolean.valueOf(☃.nextFloat() > 0.9F)), 3, 3, 11, ☃);
      a(☃, Blocks.END_PORTAL_FRAME.fromLegacyData(☃).set(BlockEnderPortalFrame.EYE, Boolean.valueOf(☃.nextFloat() > 0.9F)), 7, 3, 9, ☃);
      a(☃, Blocks.END_PORTAL_FRAME.fromLegacyData(☃).set(BlockEnderPortalFrame.EYE, Boolean.valueOf(☃.nextFloat() > 0.9F)), 7, 3, 10, ☃);
      a(☃, Blocks.END_PORTAL_FRAME.fromLegacyData(☃).set(BlockEnderPortalFrame.EYE, Boolean.valueOf(☃.nextFloat() > 0.9F)), 7, 3, 11, ☃);
      if (!this.a)
      {
        ☃ = d(3);
        BlockPosition ☃ = new BlockPosition(a(5, 6), ☃, b(5, 6));
        if (☃.b(☃))
        {
          this.a = true;
          ☃.setTypeAndData(☃, Blocks.MOB_SPAWNER.getBlockData(), 2);
          
          TileEntity ☃ = ☃.getTileEntity(☃);
          if ((☃ instanceof TileEntityMobSpawner)) {
            ((TileEntityMobSpawner)☃).getSpawner().setMobName("Silverfish");
          }
        }
      }
      return true;
    }
  }
  
  static class WorldGenStrongholdStones
    extends StructurePiece.StructurePieceBlockSelector
  {
    public void a(Random ☃, int ☃, int ☃, int ☃, boolean ☃)
    {
      if (☃)
      {
        float ☃ = ☃.nextFloat();
        if (☃ < 0.2F) {
          this.a = Blocks.STONEBRICK.fromLegacyData(BlockSmoothBrick.O);
        } else if (☃ < 0.5F) {
          this.a = Blocks.STONEBRICK.fromLegacyData(BlockSmoothBrick.N);
        } else if (☃ < 0.55F) {
          this.a = Blocks.MONSTER_EGG.fromLegacyData(BlockMonsterEggs.EnumMonsterEggVarient.STONEBRICK.a());
        } else {
          this.a = Blocks.STONEBRICK.getBlockData();
        }
      }
      else
      {
        this.a = Blocks.AIR.getBlockData();
      }
    }
  }
  
  private static final WorldGenStrongholdStones e = new WorldGenStrongholdStones(null);
}
