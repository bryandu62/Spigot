package net.minecraft.server.v1_8_R3;

import java.util.List;
import java.util.Random;

public abstract class BlockStepAbstract
  extends Block
{
  public static final BlockStateEnum<EnumSlabHalf> HALF = BlockStateEnum.of("half", EnumSlabHalf.class);
  
  public BlockStepAbstract(Material ☃)
  {
    super(☃);
    if (l()) {
      this.r = true;
    } else {
      a(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
    }
    e(255);
  }
  
  protected boolean I()
  {
    return false;
  }
  
  public void updateShape(IBlockAccess ☃, BlockPosition ☃)
  {
    if (l())
    {
      a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      return;
    }
    IBlockData ☃ = ☃.getType(☃);
    if (☃.getBlock() == this) {
      if (☃.get(HALF) == EnumSlabHalf.TOP) {
        a(0.0F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F);
      } else {
        a(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
      }
    }
  }
  
  public void j()
  {
    if (l()) {
      a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    } else {
      a(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
    }
  }
  
  public void a(World ☃, BlockPosition ☃, IBlockData ☃, AxisAlignedBB ☃, List<AxisAlignedBB> ☃, Entity ☃)
  {
    updateShape(☃, ☃);
    super.a(☃, ☃, ☃, ☃, ☃, ☃);
  }
  
  public boolean c()
  {
    return l();
  }
  
  public IBlockData getPlacedState(World ☃, BlockPosition ☃, EnumDirection ☃, float ☃, float ☃, float ☃, int ☃, EntityLiving ☃)
  {
    IBlockData ☃ = super.getPlacedState(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃).set(HALF, EnumSlabHalf.BOTTOM);
    if (l()) {
      return ☃;
    }
    if ((☃ == EnumDirection.DOWN) || ((☃ != EnumDirection.UP) && (☃ > 0.5D))) {
      return ☃.set(HALF, EnumSlabHalf.TOP);
    }
    return ☃;
  }
  
  public int a(Random ☃)
  {
    if (l()) {
      return 2;
    }
    return 1;
  }
  
  public boolean d()
  {
    return l();
  }
  
  public abstract String b(int paramInt);
  
  public int getDropData(World ☃, BlockPosition ☃)
  {
    return super.getDropData(☃, ☃) & 0x7;
  }
  
  public abstract boolean l();
  
  public abstract IBlockState<?> n();
  
  public abstract Object a(ItemStack paramItemStack);
  
  public static enum EnumSlabHalf
    implements INamable
  {
    private final String c;
    
    private EnumSlabHalf(String ☃)
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
