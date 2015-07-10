package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class BlockBed
  extends BlockDirectional
{
  public static final BlockStateEnum<EnumBedPart> PART = BlockStateEnum.of("part", EnumBedPart.class);
  public static final BlockStateBoolean OCCUPIED = BlockStateBoolean.of("occupied");
  
  public BlockBed()
  {
    super(Material.CLOTH);
    j(this.blockStateList.getBlockData().set(PART, EnumBedPart.FOOT).set(OCCUPIED, Boolean.valueOf(false)));
    l();
  }
  
  public boolean interact(World ☃, BlockPosition ☃, IBlockData ☃, EntityHuman ☃, EnumDirection ☃, float ☃, float ☃, float ☃)
  {
    if (☃.isClientSide) {
      return true;
    }
    if (☃.get(PART) != EnumBedPart.HEAD)
    {
      ☃ = ☃.shift((EnumDirection)☃.get(FACING));
      ☃ = ☃.getType(☃);
      if (☃.getBlock() != this) {
        return true;
      }
    }
    if ((!☃.worldProvider.e()) || (☃.getBiome(☃) == BiomeBase.HELL))
    {
      ☃.setAir(☃);
      
      BlockPosition ☃ = ☃.shift(((EnumDirection)☃.get(FACING)).opposite());
      if (☃.getType(☃).getBlock() == this) {
        ☃.setAir(☃);
      }
      ☃.createExplosion(null, ☃.getX() + 0.5D, ☃.getY() + 0.5D, ☃.getZ() + 0.5D, 5.0F, true, true);
      return true;
    }
    if (((Boolean)☃.get(OCCUPIED)).booleanValue())
    {
      EntityHuman ☃ = f(☃, ☃);
      if (☃ == null)
      {
        ☃ = ☃.set(OCCUPIED, Boolean.valueOf(false));
        ☃.setTypeAndData(☃, ☃, 4);
      }
      else
      {
        ☃.b(new ChatMessage("tile.bed.occupied", new Object[0]));
        return true;
      }
    }
    EntityHuman.EnumBedResult ☃ = ☃.a(☃);
    if (☃ == EntityHuman.EnumBedResult.OK)
    {
      ☃ = ☃.set(OCCUPIED, Boolean.valueOf(true));
      ☃.setTypeAndData(☃, ☃, 4);
      return true;
    }
    if (☃ == EntityHuman.EnumBedResult.NOT_POSSIBLE_NOW) {
      ☃.b(new ChatMessage("tile.bed.noSleep", new Object[0]));
    } else if (☃ == EntityHuman.EnumBedResult.NOT_SAFE) {
      ☃.b(new ChatMessage("tile.bed.notSafe", new Object[0]));
    }
    return true;
  }
  
  private EntityHuman f(World ☃, BlockPosition ☃)
  {
    for (EntityHuman ☃ : ☃.players) {
      if ((☃.isSleeping()) && (☃.bx.equals(☃))) {
        return ☃;
      }
    }
    return null;
  }
  
  public boolean d()
  {
    return false;
  }
  
  public boolean c()
  {
    return false;
  }
  
  public void updateShape(IBlockAccess ☃, BlockPosition ☃)
  {
    l();
  }
  
  public void doPhysics(World ☃, BlockPosition ☃, IBlockData ☃, Block ☃)
  {
    EnumDirection ☃ = (EnumDirection)☃.get(FACING);
    if (☃.get(PART) == EnumBedPart.HEAD)
    {
      if (☃.getType(☃.shift(☃.opposite())).getBlock() != this) {
        ☃.setAir(☃);
      }
    }
    else if (☃.getType(☃.shift(☃)).getBlock() != this)
    {
      ☃.setAir(☃);
      if (!☃.isClientSide) {
        b(☃, ☃, ☃, 0);
      }
    }
  }
  
  public Item getDropType(IBlockData ☃, Random ☃, int ☃)
  {
    if (☃.get(PART) == EnumBedPart.HEAD) {
      return null;
    }
    return Items.BED;
  }
  
  private void l()
  {
    a(0.0F, 0.0F, 0.0F, 1.0F, 0.5625F, 1.0F);
  }
  
  public static BlockPosition a(World ☃, BlockPosition ☃, int ☃)
  {
    EnumDirection ☃ = (EnumDirection)☃.getType(☃).get(FACING);
    
    int ☃ = ☃.getX();
    int ☃ = ☃.getY();
    int ☃ = ☃.getZ();
    for (int ☃ = 0; ☃ <= 1; ☃++)
    {
      int ☃ = ☃ - ☃.getAdjacentX() * ☃ - 1;
      int ☃ = ☃ - ☃.getAdjacentZ() * ☃ - 1;
      int ☃ = ☃ + 2;
      int ☃ = ☃ + 2;
      for (int ☃ = ☃; ☃ <= ☃; ☃++) {
        for (int ☃ = ☃; ☃ <= ☃; ☃++)
        {
          BlockPosition ☃ = new BlockPosition(☃, ☃, ☃);
          if (e(☃, ☃)) {
            if (☃ > 0) {
              ☃--;
            } else {
              return ☃;
            }
          }
        }
      }
    }
    return null;
  }
  
  protected static boolean e(World ☃, BlockPosition ☃)
  {
    return (World.a(☃, ☃.down())) && (!☃.getType(☃).getBlock().getMaterial().isBuildable()) && (!☃.getType(☃.up()).getBlock().getMaterial().isBuildable());
  }
  
  public void dropNaturally(World ☃, BlockPosition ☃, IBlockData ☃, float ☃, int ☃)
  {
    if (☃.get(PART) == EnumBedPart.FOOT) {
      super.dropNaturally(☃, ☃, ☃, ☃, 0);
    }
  }
  
  public int k()
  {
    return 1;
  }
  
  public void a(World ☃, BlockPosition ☃, IBlockData ☃, EntityHuman ☃)
  {
    if ((☃.abilities.canInstantlyBuild) && 
      (☃.get(PART) == EnumBedPart.HEAD))
    {
      BlockPosition ☃ = ☃.shift(((EnumDirection)☃.get(FACING)).opposite());
      if (☃.getType(☃).getBlock() == this) {
        ☃.setAir(☃);
      }
    }
  }
  
  public IBlockData fromLegacyData(int ☃)
  {
    EnumDirection ☃ = EnumDirection.fromType2(☃);
    if ((☃ & 0x8) > 0) {
      return getBlockData().set(PART, EnumBedPart.HEAD).set(FACING, ☃).set(OCCUPIED, Boolean.valueOf((☃ & 0x4) > 0));
    }
    return getBlockData().set(PART, EnumBedPart.FOOT).set(FACING, ☃);
  }
  
  public IBlockData updateState(IBlockData ☃, IBlockAccess ☃, BlockPosition ☃)
  {
    if (☃.get(PART) == EnumBedPart.FOOT)
    {
      IBlockData ☃ = ☃.getType(☃.shift((EnumDirection)☃.get(FACING)));
      if (☃.getBlock() == this) {
        ☃ = ☃.set(OCCUPIED, ☃.get(OCCUPIED));
      }
    }
    return ☃;
  }
  
  public int toLegacyData(IBlockData ☃)
  {
    int ☃ = 0;
    
    ☃ |= ((EnumDirection)☃.get(FACING)).b();
    if (☃.get(PART) == EnumBedPart.HEAD)
    {
      ☃ |= 0x8;
      if (((Boolean)☃.get(OCCUPIED)).booleanValue()) {
        ☃ |= 0x4;
      }
    }
    return ☃;
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { FACING, PART, OCCUPIED });
  }
  
  public static enum EnumBedPart
    implements INamable
  {
    private final String c;
    
    private EnumBedPart(String ☃)
    {
      this.c = ☃;
    }
    
    public String toString()
    {
      return this.c;
    }
    
    public String getName()
    {
      return this.c;
    }
  }
}
