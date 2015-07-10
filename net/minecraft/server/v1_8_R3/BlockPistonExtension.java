package net.minecraft.server.v1_8_R3;

import java.util.List;
import java.util.Random;

public class BlockPistonExtension
  extends Block
{
  public static final BlockStateDirection FACING = BlockStateDirection.of("facing");
  public static final BlockStateEnum<EnumPistonType> TYPE = BlockStateEnum.of("type", EnumPistonType.class);
  public static final BlockStateBoolean SHORT = BlockStateBoolean.of("short");
  
  public BlockPistonExtension()
  {
    super(Material.PISTON);
    j(this.blockStateList.getBlockData().set(FACING, EnumDirection.NORTH).set(TYPE, EnumPistonType.DEFAULT).set(SHORT, Boolean.valueOf(false)));
    a(i);
    c(0.5F);
  }
  
  public void a(World ☃, BlockPosition ☃, IBlockData ☃, EntityHuman ☃)
  {
    if (☃.abilities.canInstantlyBuild)
    {
      EnumDirection ☃ = (EnumDirection)☃.get(FACING);
      if (☃ != null)
      {
        BlockPosition ☃ = ☃.shift(☃.opposite());
        Block ☃ = ☃.getType(☃).getBlock();
        if ((☃ == Blocks.PISTON) || (☃ == Blocks.STICKY_PISTON)) {
          ☃.setAir(☃);
        }
      }
    }
    super.a(☃, ☃, ☃, ☃);
  }
  
  public void remove(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    super.remove(☃, ☃, ☃);
    EnumDirection ☃ = ((EnumDirection)☃.get(FACING)).opposite();
    ☃ = ☃.shift(☃);
    
    IBlockData ☃ = ☃.getType(☃);
    if (((☃.getBlock() == Blocks.PISTON) || (☃.getBlock() == Blocks.STICKY_PISTON)) && 
      (((Boolean)☃.get(BlockPiston.EXTENDED)).booleanValue()))
    {
      ☃.getBlock().b(☃, ☃, ☃, 0);
      ☃.setAir(☃);
    }
  }
  
  public boolean c()
  {
    return false;
  }
  
  public boolean d()
  {
    return false;
  }
  
  public boolean canPlace(World ☃, BlockPosition ☃)
  {
    return false;
  }
  
  public boolean canPlace(World ☃, BlockPosition ☃, EnumDirection ☃)
  {
    return false;
  }
  
  public int a(Random ☃)
  {
    return 0;
  }
  
  public void a(World ☃, BlockPosition ☃, IBlockData ☃, AxisAlignedBB ☃, List<AxisAlignedBB> ☃, Entity ☃)
  {
    d(☃);
    super.a(☃, ☃, ☃, ☃, ☃, ☃);
    
    e(☃);
    super.a(☃, ☃, ☃, ☃, ☃, ☃);
    
    a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
  }
  
  private void e(IBlockData ☃)
  {
    float ☃ = 0.25F;
    float ☃ = 0.375F;
    float ☃ = 0.625F;
    float ☃ = 0.25F;
    float ☃ = 0.75F;
    switch (1.a[((EnumDirection)☃.get(FACING)).ordinal()])
    {
    case 1: 
      a(0.375F, 0.25F, 0.375F, 0.625F, 1.0F, 0.625F);
      break;
    case 2: 
      a(0.375F, 0.0F, 0.375F, 0.625F, 0.75F, 0.625F);
      break;
    case 3: 
      a(0.25F, 0.375F, 0.25F, 0.75F, 0.625F, 1.0F);
      break;
    case 4: 
      a(0.25F, 0.375F, 0.0F, 0.75F, 0.625F, 0.75F);
      break;
    case 5: 
      a(0.375F, 0.25F, 0.25F, 0.625F, 0.75F, 1.0F);
      break;
    case 6: 
      a(0.0F, 0.375F, 0.25F, 0.75F, 0.625F, 0.75F);
    }
  }
  
  public void updateShape(IBlockAccess ☃, BlockPosition ☃)
  {
    d(☃.getType(☃));
  }
  
  public void d(IBlockData ☃)
  {
    float ☃ = 0.25F;
    EnumDirection ☃ = (EnumDirection)☃.get(FACING);
    if (☃ == null) {
      return;
    }
    switch (1.a[☃.ordinal()])
    {
    case 1: 
      a(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
      break;
    case 2: 
      a(0.0F, 0.75F, 0.0F, 1.0F, 1.0F, 1.0F);
      break;
    case 3: 
      a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.25F);
      break;
    case 4: 
      a(0.0F, 0.0F, 0.75F, 1.0F, 1.0F, 1.0F);
      break;
    case 5: 
      a(0.0F, 0.0F, 0.0F, 0.25F, 1.0F, 1.0F);
      break;
    case 6: 
      a(0.75F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
  }
  
  public void doPhysics(World ☃, BlockPosition ☃, IBlockData ☃, Block ☃)
  {
    EnumDirection ☃ = (EnumDirection)☃.get(FACING);
    BlockPosition ☃ = ☃.shift(☃.opposite());
    IBlockData ☃ = ☃.getType(☃);
    if ((☃.getBlock() != Blocks.PISTON) && (☃.getBlock() != Blocks.STICKY_PISTON)) {
      ☃.setAir(☃);
    } else {
      ☃.getBlock().doPhysics(☃, ☃, ☃, ☃);
    }
  }
  
  public static EnumDirection b(int ☃)
  {
    int ☃ = ☃ & 0x7;
    if (☃ > 5) {
      return null;
    }
    return EnumDirection.fromType1(☃);
  }
  
  public IBlockData fromLegacyData(int ☃)
  {
    return getBlockData().set(FACING, b(☃)).set(TYPE, (☃ & 0x8) > 0 ? EnumPistonType.STICKY : EnumPistonType.DEFAULT);
  }
  
  public int toLegacyData(IBlockData ☃)
  {
    int ☃ = 0;
    
    ☃ |= ((EnumDirection)☃.get(FACING)).a();
    if (☃.get(TYPE) == EnumPistonType.STICKY) {
      ☃ |= 0x8;
    }
    return ☃;
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { FACING, TYPE, SHORT });
  }
  
  public static enum EnumPistonType
    implements INamable
  {
    private final String c;
    
    private EnumPistonType(String ☃)
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
