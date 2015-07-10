package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;

public class WorldGenMineshaftPieces
{
  public static void a()
  {
    WorldGenFactory.a(WorldGenMineshaftCorridor.class, "MSCorridor");
    WorldGenFactory.a(WorldGenMineshaftCross.class, "MSCrossing");
    WorldGenFactory.a(WorldGenMineshaftRoom.class, "MSRoom");
    WorldGenFactory.a(WorldGenMineshaftStairs.class, "MSStairs");
  }
  
  private static StructurePiece a(List<StructurePiece> ☃, Random ☃, int ☃, int ☃, int ☃, EnumDirection ☃, int ☃)
  {
    int ☃ = ☃.nextInt(100);
    if (☃ >= 80)
    {
      StructureBoundingBox ☃ = WorldGenMineshaftCross.a(☃, ☃, ☃, ☃, ☃, ☃);
      if (☃ != null) {
        return new WorldGenMineshaftCross(☃, ☃, ☃, ☃);
      }
    }
    else if (☃ >= 70)
    {
      StructureBoundingBox ☃ = WorldGenMineshaftStairs.a(☃, ☃, ☃, ☃, ☃, ☃);
      if (☃ != null) {
        return new WorldGenMineshaftStairs(☃, ☃, ☃, ☃);
      }
    }
    else
    {
      StructureBoundingBox ☃ = WorldGenMineshaftCorridor.a(☃, ☃, ☃, ☃, ☃, ☃);
      if (☃ != null) {
        return new WorldGenMineshaftCorridor(☃, ☃, ☃, ☃);
      }
    }
    return null;
  }
  
  private static StructurePiece b(StructurePiece ☃, List<StructurePiece> ☃, Random ☃, int ☃, int ☃, int ☃, EnumDirection ☃, int ☃)
  {
    if (☃ > 8) {
      return null;
    }
    if ((Math.abs(☃ - ☃.c().a) > 80) || (Math.abs(☃ - ☃.c().c) > 80)) {
      return null;
    }
    StructurePiece ☃ = a(☃, ☃, ☃, ☃, ☃, ☃, ☃ + 1);
    if (☃ != null)
    {
      ☃.add(☃);
      ☃.a(☃, ☃, ☃);
    }
    return ☃;
  }
  
  public static class WorldGenMineshaftRoom
    extends StructurePiece
  {
    private List<StructureBoundingBox> a = Lists.newLinkedList();
    
    public WorldGenMineshaftRoom() {}
    
    public WorldGenMineshaftRoom(int ☃, Random ☃, int ☃, int ☃)
    {
      super();
      
      this.l = new StructureBoundingBox(☃, 50, ☃, ☃ + 7 + ☃.nextInt(6), 54 + ☃.nextInt(6), ☃ + 7 + ☃.nextInt(6));
    }
    
    public void a(StructurePiece ☃, List<StructurePiece> ☃, Random ☃)
    {
      int ☃ = d();
      
      int ☃ = this.l.d() - 3 - 1;
      if (☃ <= 0) {
        ☃ = 1;
      }
      int ☃ = 0;
      while (☃ < this.l.c())
      {
        ☃ += ☃.nextInt(this.l.c());
        if (☃ + 3 > this.l.c()) {
          break;
        }
        StructurePiece ☃ = WorldGenMineshaftPieces.a(☃, ☃, ☃, this.l.a + ☃, this.l.b + ☃.nextInt(☃) + 1, this.l.c - 1, EnumDirection.NORTH, ☃);
        if (☃ != null)
        {
          StructureBoundingBox ☃ = ☃.c();
          this.a.add(new StructureBoundingBox(☃.a, ☃.b, this.l.c, ☃.d, ☃.e, this.l.c + 1));
        }
        ☃ += 4;
      }
      ☃ = 0;
      while (☃ < this.l.c())
      {
        ☃ += ☃.nextInt(this.l.c());
        if (☃ + 3 > this.l.c()) {
          break;
        }
        StructurePiece ☃ = WorldGenMineshaftPieces.a(☃, ☃, ☃, this.l.a + ☃, this.l.b + ☃.nextInt(☃) + 1, this.l.f + 1, EnumDirection.SOUTH, ☃);
        if (☃ != null)
        {
          StructureBoundingBox ☃ = ☃.c();
          this.a.add(new StructureBoundingBox(☃.a, ☃.b, this.l.f - 1, ☃.d, ☃.e, this.l.f));
        }
        ☃ += 4;
      }
      ☃ = 0;
      while (☃ < this.l.e())
      {
        ☃ += ☃.nextInt(this.l.e());
        if (☃ + 3 > this.l.e()) {
          break;
        }
        StructurePiece ☃ = WorldGenMineshaftPieces.a(☃, ☃, ☃, this.l.a - 1, this.l.b + ☃.nextInt(☃) + 1, this.l.c + ☃, EnumDirection.WEST, ☃);
        if (☃ != null)
        {
          StructureBoundingBox ☃ = ☃.c();
          this.a.add(new StructureBoundingBox(this.l.a, ☃.b, ☃.c, this.l.a + 1, ☃.e, ☃.f));
        }
        ☃ += 4;
      }
      ☃ = 0;
      while (☃ < this.l.e())
      {
        ☃ += ☃.nextInt(this.l.e());
        if (☃ + 3 > this.l.e()) {
          break;
        }
        StructurePiece ☃ = WorldGenMineshaftPieces.a(☃, ☃, ☃, this.l.d + 1, this.l.b + ☃.nextInt(☃) + 1, this.l.c + ☃, EnumDirection.EAST, ☃);
        if (☃ != null)
        {
          StructureBoundingBox ☃ = ☃.c();
          this.a.add(new StructureBoundingBox(this.l.d - 1, ☃.b, ☃.c, this.l.d, ☃.e, ☃.f));
        }
        ☃ += 4;
      }
    }
    
    public boolean a(World ☃, Random ☃, StructureBoundingBox ☃)
    {
      if (a(☃, ☃)) {
        return false;
      }
      a(☃, ☃, this.l.a, this.l.b, this.l.c, this.l.d, this.l.b, this.l.f, Blocks.DIRT.getBlockData(), Blocks.AIR.getBlockData(), true);
      
      a(☃, ☃, this.l.a, this.l.b + 1, this.l.c, this.l.d, Math.min(this.l.b + 3, this.l.e), this.l.f, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
      for (StructureBoundingBox ☃ : this.a) {
        a(☃, ☃, ☃.a, ☃.e - 2, ☃.c, ☃.d, ☃.e, ☃.f, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
      }
      a(☃, ☃, this.l.a, this.l.b + 4, this.l.c, this.l.d, this.l.e, this.l.f, Blocks.AIR.getBlockData(), false);
      
      return true;
    }
    
    public void a(int ☃, int ☃, int ☃)
    {
      super.a(☃, ☃, ☃);
      for (StructureBoundingBox ☃ : this.a) {
        ☃.a(☃, ☃, ☃);
      }
    }
    
    protected void a(NBTTagCompound ☃)
    {
      NBTTagList ☃ = new NBTTagList();
      for (StructureBoundingBox ☃ : this.a) {
        ☃.add(☃.g());
      }
      ☃.set("Entrances", ☃);
    }
    
    protected void b(NBTTagCompound ☃)
    {
      NBTTagList ☃ = ☃.getList("Entrances", 11);
      for (int ☃ = 0; ☃ < ☃.size(); ☃++) {
        this.a.add(new StructureBoundingBox(☃.c(☃)));
      }
    }
  }
  
  public static class WorldGenMineshaftCorridor
    extends StructurePiece
  {
    private boolean a;
    private boolean b;
    private boolean c;
    private int d;
    
    public WorldGenMineshaftCorridor() {}
    
    protected void a(NBTTagCompound ☃)
    {
      ☃.setBoolean("hr", this.a);
      ☃.setBoolean("sc", this.b);
      ☃.setBoolean("hps", this.c);
      ☃.setInt("Num", this.d);
    }
    
    protected void b(NBTTagCompound ☃)
    {
      this.a = ☃.getBoolean("hr");
      this.b = ☃.getBoolean("sc");
      this.c = ☃.getBoolean("hps");
      this.d = ☃.getInt("Num");
    }
    
    public WorldGenMineshaftCorridor(int ☃, Random ☃, StructureBoundingBox ☃, EnumDirection ☃)
    {
      super();
      this.m = ☃;
      this.l = ☃;
      this.a = (☃.nextInt(3) == 0);
      this.b = ((!this.a) && (☃.nextInt(23) == 0));
      if ((this.m == EnumDirection.NORTH) || (this.m == EnumDirection.SOUTH)) {
        this.d = (☃.e() / 5);
      } else {
        this.d = (☃.c() / 5);
      }
    }
    
    public static StructureBoundingBox a(List<StructurePiece> ☃, Random ☃, int ☃, int ☃, int ☃, EnumDirection ☃)
    {
      StructureBoundingBox ☃ = new StructureBoundingBox(☃, ☃, ☃, ☃, ☃ + 2, ☃);
      
      int ☃ = ☃.nextInt(3) + 2;
      while (☃ > 0)
      {
        int ☃ = ☃ * 5;
        switch (WorldGenMineshaftPieces.1.a[☃.ordinal()])
        {
        case 1: 
          ☃.d = (☃ + 2);
          ☃.c = (☃ - (☃ - 1));
          break;
        case 2: 
          ☃.d = (☃ + 2);
          ☃.f = (☃ + (☃ - 1));
          break;
        case 3: 
          ☃.a = (☃ - (☃ - 1));
          ☃.f = (☃ + 2);
          break;
        case 4: 
          ☃.d = (☃ + (☃ - 1));
          ☃.f = (☃ + 2);
        }
        if (StructurePiece.a(☃, ☃) == null) {
          break;
        }
        ☃--;
      }
      if (☃ > 0) {
        return ☃;
      }
      return null;
    }
    
    public void a(StructurePiece ☃, List<StructurePiece> ☃, Random ☃)
    {
      int ☃ = d();
      int ☃ = ☃.nextInt(4);
      if (this.m != null) {
        switch (WorldGenMineshaftPieces.1.a[this.m.ordinal()])
        {
        case 1: 
          if (☃ <= 1) {
            WorldGenMineshaftPieces.a(☃, ☃, ☃, this.l.a, this.l.b - 1 + ☃.nextInt(3), this.l.c - 1, this.m, ☃);
          } else if (☃ == 2) {
            WorldGenMineshaftPieces.a(☃, ☃, ☃, this.l.a - 1, this.l.b - 1 + ☃.nextInt(3), this.l.c, EnumDirection.WEST, ☃);
          } else {
            WorldGenMineshaftPieces.a(☃, ☃, ☃, this.l.d + 1, this.l.b - 1 + ☃.nextInt(3), this.l.c, EnumDirection.EAST, ☃);
          }
          break;
        case 2: 
          if (☃ <= 1) {
            WorldGenMineshaftPieces.a(☃, ☃, ☃, this.l.a, this.l.b - 1 + ☃.nextInt(3), this.l.f + 1, this.m, ☃);
          } else if (☃ == 2) {
            WorldGenMineshaftPieces.a(☃, ☃, ☃, this.l.a - 1, this.l.b - 1 + ☃.nextInt(3), this.l.f - 3, EnumDirection.WEST, ☃);
          } else {
            WorldGenMineshaftPieces.a(☃, ☃, ☃, this.l.d + 1, this.l.b - 1 + ☃.nextInt(3), this.l.f - 3, EnumDirection.EAST, ☃);
          }
          break;
        case 3: 
          if (☃ <= 1) {
            WorldGenMineshaftPieces.a(☃, ☃, ☃, this.l.a - 1, this.l.b - 1 + ☃.nextInt(3), this.l.c, this.m, ☃);
          } else if (☃ == 2) {
            WorldGenMineshaftPieces.a(☃, ☃, ☃, this.l.a, this.l.b - 1 + ☃.nextInt(3), this.l.c - 1, EnumDirection.NORTH, ☃);
          } else {
            WorldGenMineshaftPieces.a(☃, ☃, ☃, this.l.a, this.l.b - 1 + ☃.nextInt(3), this.l.f + 1, EnumDirection.SOUTH, ☃);
          }
          break;
        case 4: 
          if (☃ <= 1) {
            WorldGenMineshaftPieces.a(☃, ☃, ☃, this.l.d + 1, this.l.b - 1 + ☃.nextInt(3), this.l.c, this.m, ☃);
          } else if (☃ == 2) {
            WorldGenMineshaftPieces.a(☃, ☃, ☃, this.l.d - 3, this.l.b - 1 + ☃.nextInt(3), this.l.c - 1, EnumDirection.NORTH, ☃);
          } else {
            WorldGenMineshaftPieces.a(☃, ☃, ☃, this.l.d - 3, this.l.b - 1 + ☃.nextInt(3), this.l.f + 1, EnumDirection.SOUTH, ☃);
          }
          break;
        }
      }
      if (☃ < 8) {
        if ((this.m == EnumDirection.NORTH) || (this.m == EnumDirection.SOUTH)) {
          for (int ☃ = this.l.c + 3; ☃ + 3 <= this.l.f; ☃ += 5)
          {
            int ☃ = ☃.nextInt(5);
            if (☃ == 0) {
              WorldGenMineshaftPieces.a(☃, ☃, ☃, this.l.a - 1, this.l.b, ☃, EnumDirection.WEST, ☃ + 1);
            } else if (☃ == 1) {
              WorldGenMineshaftPieces.a(☃, ☃, ☃, this.l.d + 1, this.l.b, ☃, EnumDirection.EAST, ☃ + 1);
            }
          }
        } else {
          for (int ☃ = this.l.a + 3; ☃ + 3 <= this.l.d; ☃ += 5)
          {
            int ☃ = ☃.nextInt(5);
            if (☃ == 0) {
              WorldGenMineshaftPieces.a(☃, ☃, ☃, ☃, this.l.b, this.l.c - 1, EnumDirection.NORTH, ☃ + 1);
            } else if (☃ == 1) {
              WorldGenMineshaftPieces.a(☃, ☃, ☃, ☃, this.l.b, this.l.f + 1, EnumDirection.SOUTH, ☃ + 1);
            }
          }
        }
      }
    }
    
    protected boolean a(World ☃, StructureBoundingBox ☃, Random ☃, int ☃, int ☃, int ☃, List<StructurePieceTreasure> ☃, int ☃)
    {
      BlockPosition ☃ = new BlockPosition(a(☃, ☃), d(☃), b(☃, ☃));
      if ((☃.b(☃)) && 
        (☃.getType(☃).getBlock().getMaterial() == Material.AIR))
      {
        int ☃ = ☃.nextBoolean() ? 1 : 0;
        ☃.setTypeAndData(☃, Blocks.RAIL.fromLegacyData(a(Blocks.RAIL, ☃)), 2);
        EntityMinecartChest ☃ = new EntityMinecartChest(☃, ☃.getX() + 0.5F, ☃.getY() + 0.5F, ☃.getZ() + 0.5F);
        StructurePieceTreasure.a(☃, ☃, ☃, ☃);
        ☃.addEntity(☃);
        return true;
      }
      return false;
    }
    
    public boolean a(World ☃, Random ☃, StructureBoundingBox ☃)
    {
      if (a(☃, ☃)) {
        return false;
      }
      int ☃ = 0;
      int ☃ = 2;
      int ☃ = 0;
      int ☃ = 2;
      int ☃ = this.d * 5 - 1;
      
      a(☃, ☃, 0, 0, 0, 2, 1, ☃, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
      a(☃, ☃, ☃, 0.8F, 0, 2, 0, 2, 2, ☃, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
      if (this.b) {
        a(☃, ☃, ☃, 0.6F, 0, 0, 0, 2, 1, ☃, Blocks.WEB.getBlockData(), Blocks.AIR.getBlockData(), false);
      }
      for (int ☃ = 0; ☃ < this.d; ☃++)
      {
        int ☃ = 2 + ☃ * 5;
        
        a(☃, ☃, 0, 0, ☃, 0, 1, ☃, Blocks.FENCE.getBlockData(), Blocks.AIR.getBlockData(), false);
        a(☃, ☃, 2, 0, ☃, 2, 1, ☃, Blocks.FENCE.getBlockData(), Blocks.AIR.getBlockData(), false);
        if (☃.nextInt(4) == 0)
        {
          a(☃, ☃, 0, 2, ☃, 0, 2, ☃, Blocks.PLANKS.getBlockData(), Blocks.AIR.getBlockData(), false);
          a(☃, ☃, 2, 2, ☃, 2, 2, ☃, Blocks.PLANKS.getBlockData(), Blocks.AIR.getBlockData(), false);
        }
        else
        {
          a(☃, ☃, 0, 2, ☃, 2, 2, ☃, Blocks.PLANKS.getBlockData(), Blocks.AIR.getBlockData(), false);
        }
        a(☃, ☃, ☃, 0.1F, 0, 2, ☃ - 1, Blocks.WEB.getBlockData());
        a(☃, ☃, ☃, 0.1F, 2, 2, ☃ - 1, Blocks.WEB.getBlockData());
        a(☃, ☃, ☃, 0.1F, 0, 2, ☃ + 1, Blocks.WEB.getBlockData());
        a(☃, ☃, ☃, 0.1F, 2, 2, ☃ + 1, Blocks.WEB.getBlockData());
        a(☃, ☃, ☃, 0.05F, 0, 2, ☃ - 2, Blocks.WEB.getBlockData());
        a(☃, ☃, ☃, 0.05F, 2, 2, ☃ - 2, Blocks.WEB.getBlockData());
        a(☃, ☃, ☃, 0.05F, 0, 2, ☃ + 2, Blocks.WEB.getBlockData());
        a(☃, ☃, ☃, 0.05F, 2, 2, ☃ + 2, Blocks.WEB.getBlockData());
        
        a(☃, ☃, ☃, 0.05F, 1, 2, ☃ - 1, Blocks.TORCH.fromLegacyData(EnumDirection.UP.a()));
        a(☃, ☃, ☃, 0.05F, 1, 2, ☃ + 1, Blocks.TORCH.fromLegacyData(EnumDirection.UP.a()));
        if (☃.nextInt(100) == 0) {
          a(☃, ☃, ☃, 2, 0, ☃ - 1, StructurePieceTreasure.a(WorldGenMineshaftPieces.b(), new StructurePieceTreasure[] { Items.ENCHANTED_BOOK.b(☃) }), 3 + ☃.nextInt(4));
        }
        if (☃.nextInt(100) == 0) {
          a(☃, ☃, ☃, 0, 0, ☃ + 1, StructurePieceTreasure.a(WorldGenMineshaftPieces.b(), new StructurePieceTreasure[] { Items.ENCHANTED_BOOK.b(☃) }), 3 + ☃.nextInt(4));
        }
        if ((this.b) && (!this.c))
        {
          int ☃ = d(0);int ☃ = ☃ - 1 + ☃.nextInt(3);
          int ☃ = a(1, ☃);
          ☃ = b(1, ☃);
          BlockPosition ☃ = new BlockPosition(☃, ☃, ☃);
          if (☃.b(☃))
          {
            this.c = true;
            ☃.setTypeAndData(☃, Blocks.MOB_SPAWNER.getBlockData(), 2);
            
            TileEntity ☃ = ☃.getTileEntity(☃);
            if ((☃ instanceof TileEntityMobSpawner)) {
              ((TileEntityMobSpawner)☃).getSpawner().setMobName("CaveSpider");
            }
          }
        }
      }
      for (int ☃ = 0; ☃ <= 2; ☃++) {
        for (int ☃ = 0; ☃ <= ☃; ☃++)
        {
          int ☃ = -1;
          IBlockData ☃ = a(☃, ☃, ☃, ☃, ☃);
          if (☃.getBlock().getMaterial() == Material.AIR)
          {
            int ☃ = -1;
            a(☃, Blocks.PLANKS.getBlockData(), ☃, ☃, ☃, ☃);
          }
        }
      }
      if (this.a) {
        for (int ☃ = 0; ☃ <= ☃; ☃++)
        {
          IBlockData ☃ = a(☃, 1, -1, ☃, ☃);
          if ((☃.getBlock().getMaterial() != Material.AIR) && (☃.getBlock().o())) {
            a(☃, ☃, ☃, 0.7F, 1, 0, ☃, Blocks.RAIL.fromLegacyData(a(Blocks.RAIL, 0)));
          }
        }
      }
      return true;
    }
  }
  
  public static class WorldGenMineshaftCross
    extends StructurePiece
  {
    private EnumDirection a;
    private boolean b;
    
    public WorldGenMineshaftCross() {}
    
    protected void a(NBTTagCompound ☃)
    {
      ☃.setBoolean("tf", this.b);
      ☃.setInt("D", this.a.b());
    }
    
    protected void b(NBTTagCompound ☃)
    {
      this.b = ☃.getBoolean("tf");
      this.a = EnumDirection.fromType2(☃.getInt("D"));
    }
    
    public WorldGenMineshaftCross(int ☃, Random ☃, StructureBoundingBox ☃, EnumDirection ☃)
    {
      super();
      
      this.a = ☃;
      this.l = ☃;
      this.b = (☃.d() > 3);
    }
    
    public static StructureBoundingBox a(List<StructurePiece> ☃, Random ☃, int ☃, int ☃, int ☃, EnumDirection ☃)
    {
      StructureBoundingBox ☃ = new StructureBoundingBox(☃, ☃, ☃, ☃, ☃ + 2, ☃);
      if (☃.nextInt(4) == 0) {
        ☃.e += 4;
      }
      switch (WorldGenMineshaftPieces.1.a[☃.ordinal()])
      {
      case 1: 
        ☃.a = (☃ - 1);
        ☃.d = (☃ + 3);
        ☃.c = (☃ - 4);
        break;
      case 2: 
        ☃.a = (☃ - 1);
        ☃.d = (☃ + 3);
        ☃.f = (☃ + 4);
        break;
      case 3: 
        ☃.a = (☃ - 4);
        ☃.c = (☃ - 1);
        ☃.f = (☃ + 3);
        break;
      case 4: 
        ☃.d = (☃ + 4);
        ☃.c = (☃ - 1);
        ☃.f = (☃ + 3);
      }
      if (StructurePiece.a(☃, ☃) != null) {
        return null;
      }
      return ☃;
    }
    
    public void a(StructurePiece ☃, List<StructurePiece> ☃, Random ☃)
    {
      int ☃ = d();
      switch (WorldGenMineshaftPieces.1.a[this.a.ordinal()])
      {
      case 1: 
        WorldGenMineshaftPieces.a(☃, ☃, ☃, this.l.a + 1, this.l.b, this.l.c - 1, EnumDirection.NORTH, ☃);
        WorldGenMineshaftPieces.a(☃, ☃, ☃, this.l.a - 1, this.l.b, this.l.c + 1, EnumDirection.WEST, ☃);
        WorldGenMineshaftPieces.a(☃, ☃, ☃, this.l.d + 1, this.l.b, this.l.c + 1, EnumDirection.EAST, ☃);
        break;
      case 2: 
        WorldGenMineshaftPieces.a(☃, ☃, ☃, this.l.a + 1, this.l.b, this.l.f + 1, EnumDirection.SOUTH, ☃);
        WorldGenMineshaftPieces.a(☃, ☃, ☃, this.l.a - 1, this.l.b, this.l.c + 1, EnumDirection.WEST, ☃);
        WorldGenMineshaftPieces.a(☃, ☃, ☃, this.l.d + 1, this.l.b, this.l.c + 1, EnumDirection.EAST, ☃);
        break;
      case 3: 
        WorldGenMineshaftPieces.a(☃, ☃, ☃, this.l.a + 1, this.l.b, this.l.c - 1, EnumDirection.NORTH, ☃);
        WorldGenMineshaftPieces.a(☃, ☃, ☃, this.l.a + 1, this.l.b, this.l.f + 1, EnumDirection.SOUTH, ☃);
        WorldGenMineshaftPieces.a(☃, ☃, ☃, this.l.a - 1, this.l.b, this.l.c + 1, EnumDirection.WEST, ☃);
        break;
      case 4: 
        WorldGenMineshaftPieces.a(☃, ☃, ☃, this.l.a + 1, this.l.b, this.l.c - 1, EnumDirection.NORTH, ☃);
        WorldGenMineshaftPieces.a(☃, ☃, ☃, this.l.a + 1, this.l.b, this.l.f + 1, EnumDirection.SOUTH, ☃);
        WorldGenMineshaftPieces.a(☃, ☃, ☃, this.l.d + 1, this.l.b, this.l.c + 1, EnumDirection.EAST, ☃);
      }
      if (this.b)
      {
        if (☃.nextBoolean()) {
          WorldGenMineshaftPieces.a(☃, ☃, ☃, this.l.a + 1, this.l.b + 3 + 1, this.l.c - 1, EnumDirection.NORTH, ☃);
        }
        if (☃.nextBoolean()) {
          WorldGenMineshaftPieces.a(☃, ☃, ☃, this.l.a - 1, this.l.b + 3 + 1, this.l.c + 1, EnumDirection.WEST, ☃);
        }
        if (☃.nextBoolean()) {
          WorldGenMineshaftPieces.a(☃, ☃, ☃, this.l.d + 1, this.l.b + 3 + 1, this.l.c + 1, EnumDirection.EAST, ☃);
        }
        if (☃.nextBoolean()) {
          WorldGenMineshaftPieces.a(☃, ☃, ☃, this.l.a + 1, this.l.b + 3 + 1, this.l.f + 1, EnumDirection.SOUTH, ☃);
        }
      }
    }
    
    public boolean a(World ☃, Random ☃, StructureBoundingBox ☃)
    {
      if (a(☃, ☃)) {
        return false;
      }
      if (this.b)
      {
        a(☃, ☃, this.l.a + 1, this.l.b, this.l.c, this.l.d - 1, this.l.b + 3 - 1, this.l.f, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
        a(☃, ☃, this.l.a, this.l.b, this.l.c + 1, this.l.d, this.l.b + 3 - 1, this.l.f - 1, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
        a(☃, ☃, this.l.a + 1, this.l.e - 2, this.l.c, this.l.d - 1, this.l.e, this.l.f, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
        a(☃, ☃, this.l.a, this.l.e - 2, this.l.c + 1, this.l.d, this.l.e, this.l.f - 1, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
        a(☃, ☃, this.l.a + 1, this.l.b + 3, this.l.c + 1, this.l.d - 1, this.l.b + 3, this.l.f - 1, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
      }
      else
      {
        a(☃, ☃, this.l.a + 1, this.l.b, this.l.c, this.l.d - 1, this.l.e, this.l.f, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
        a(☃, ☃, this.l.a, this.l.b, this.l.c + 1, this.l.d, this.l.e, this.l.f - 1, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
      }
      a(☃, ☃, this.l.a + 1, this.l.b, this.l.c + 1, this.l.a + 1, this.l.e, this.l.c + 1, Blocks.PLANKS.getBlockData(), Blocks.AIR.getBlockData(), false);
      a(☃, ☃, this.l.a + 1, this.l.b, this.l.f - 1, this.l.a + 1, this.l.e, this.l.f - 1, Blocks.PLANKS.getBlockData(), Blocks.AIR.getBlockData(), false);
      a(☃, ☃, this.l.d - 1, this.l.b, this.l.c + 1, this.l.d - 1, this.l.e, this.l.c + 1, Blocks.PLANKS.getBlockData(), Blocks.AIR.getBlockData(), false);
      a(☃, ☃, this.l.d - 1, this.l.b, this.l.f - 1, this.l.d - 1, this.l.e, this.l.f - 1, Blocks.PLANKS.getBlockData(), Blocks.AIR.getBlockData(), false);
      for (int ☃ = this.l.a; ☃ <= this.l.d; ☃++) {
        for (int ☃ = this.l.c; ☃ <= this.l.f; ☃++) {
          if (a(☃, ☃, this.l.b - 1, ☃, ☃).getBlock().getMaterial() == Material.AIR) {
            a(☃, Blocks.PLANKS.getBlockData(), ☃, this.l.b - 1, ☃, ☃);
          }
        }
      }
      return true;
    }
  }
  
  public static class WorldGenMineshaftStairs
    extends StructurePiece
  {
    public WorldGenMineshaftStairs() {}
    
    public WorldGenMineshaftStairs(int ☃, Random ☃, StructureBoundingBox ☃, EnumDirection ☃)
    {
      super();
      this.m = ☃;
      this.l = ☃;
    }
    
    protected void a(NBTTagCompound ☃) {}
    
    protected void b(NBTTagCompound ☃) {}
    
    public static StructureBoundingBox a(List<StructurePiece> ☃, Random ☃, int ☃, int ☃, int ☃, EnumDirection ☃)
    {
      StructureBoundingBox ☃ = new StructureBoundingBox(☃, ☃ - 5, ☃, ☃, ☃ + 2, ☃);
      switch (WorldGenMineshaftPieces.1.a[☃.ordinal()])
      {
      case 1: 
        ☃.d = (☃ + 2);
        ☃.c = (☃ - 8);
        break;
      case 2: 
        ☃.d = (☃ + 2);
        ☃.f = (☃ + 8);
        break;
      case 3: 
        ☃.a = (☃ - 8);
        ☃.f = (☃ + 2);
        break;
      case 4: 
        ☃.d = (☃ + 8);
        ☃.f = (☃ + 2);
      }
      if (StructurePiece.a(☃, ☃) != null) {
        return null;
      }
      return ☃;
    }
    
    public void a(StructurePiece ☃, List<StructurePiece> ☃, Random ☃)
    {
      int ☃ = d();
      if (this.m != null) {
        switch (WorldGenMineshaftPieces.1.a[this.m.ordinal()])
        {
        case 1: 
          WorldGenMineshaftPieces.a(☃, ☃, ☃, this.l.a, this.l.b, this.l.c - 1, EnumDirection.NORTH, ☃);
          break;
        case 2: 
          WorldGenMineshaftPieces.a(☃, ☃, ☃, this.l.a, this.l.b, this.l.f + 1, EnumDirection.SOUTH, ☃);
          break;
        case 3: 
          WorldGenMineshaftPieces.a(☃, ☃, ☃, this.l.a - 1, this.l.b, this.l.c, EnumDirection.WEST, ☃);
          break;
        case 4: 
          WorldGenMineshaftPieces.a(☃, ☃, ☃, this.l.d + 1, this.l.b, this.l.c, EnumDirection.EAST, ☃);
        }
      }
    }
    
    public boolean a(World ☃, Random ☃, StructureBoundingBox ☃)
    {
      if (a(☃, ☃)) {
        return false;
      }
      a(☃, ☃, 0, 5, 0, 2, 7, 1, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
      
      a(☃, ☃, 0, 0, 7, 2, 2, 8, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
      for (int ☃ = 0; ☃ < 5; ☃++) {
        a(☃, ☃, 0, 5 - ☃ - (☃ < 4 ? 1 : 0), 2 + ☃, 2, 7 - ☃, 2 + ☃, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
      }
      return true;
    }
  }
  
  private static final List<StructurePieceTreasure> a = Lists.newArrayList(new StructurePieceTreasure[] { new StructurePieceTreasure(Items.IRON_INGOT, 0, 1, 5, 10), new StructurePieceTreasure(Items.GOLD_INGOT, 0, 1, 3, 5), new StructurePieceTreasure(Items.REDSTONE, 0, 4, 9, 5), new StructurePieceTreasure(Items.DYE, EnumColor.BLUE.getInvColorIndex(), 4, 9, 5), new StructurePieceTreasure(Items.DIAMOND, 0, 1, 2, 3), new StructurePieceTreasure(Items.COAL, 0, 3, 8, 10), new StructurePieceTreasure(Items.BREAD, 0, 1, 3, 15), new StructurePieceTreasure(Items.IRON_PICKAXE, 0, 1, 1, 1), new StructurePieceTreasure(Item.getItemOf(Blocks.RAIL), 0, 4, 8, 1), new StructurePieceTreasure(Items.MELON_SEEDS, 0, 2, 4, 10), new StructurePieceTreasure(Items.PUMPKIN_SEEDS, 0, 2, 4, 10), new StructurePieceTreasure(Items.SADDLE, 0, 1, 1, 3), new StructurePieceTreasure(Items.IRON_HORSE_ARMOR, 0, 1, 1, 1) });
}
