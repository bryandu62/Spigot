package net.minecraft.server.v1_8_R3;

import java.util.List;
import java.util.Random;

public class BlockCauldron
  extends Block
{
  public static final BlockStateInteger LEVEL = BlockStateInteger.of("level", 0, 3);
  
  public BlockCauldron()
  {
    super(Material.ORE, MaterialMapColor.m);
    j(this.blockStateList.getBlockData().set(LEVEL, Integer.valueOf(0)));
  }
  
  public void a(World ☃, BlockPosition ☃, IBlockData ☃, AxisAlignedBB ☃, List<AxisAlignedBB> ☃, Entity ☃)
  {
    a(0.0F, 0.0F, 0.0F, 1.0F, 0.3125F, 1.0F);
    super.a(☃, ☃, ☃, ☃, ☃, ☃);
    float ☃ = 0.125F;
    a(0.0F, 0.0F, 0.0F, ☃, 1.0F, 1.0F);
    super.a(☃, ☃, ☃, ☃, ☃, ☃);
    a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, ☃);
    super.a(☃, ☃, ☃, ☃, ☃, ☃);
    a(1.0F - ☃, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    super.a(☃, ☃, ☃, ☃, ☃, ☃);
    a(0.0F, 0.0F, 1.0F - ☃, 1.0F, 1.0F, 1.0F);
    super.a(☃, ☃, ☃, ☃, ☃, ☃);
    
    j();
  }
  
  public void j()
  {
    a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
  }
  
  public boolean c()
  {
    return false;
  }
  
  public boolean d()
  {
    return false;
  }
  
  public void a(World ☃, BlockPosition ☃, IBlockData ☃, Entity ☃)
  {
    int ☃ = ((Integer)☃.get(LEVEL)).intValue();
    float ☃ = ☃.getY() + (6.0F + 3 * ☃) / 16.0F;
    if ((!☃.isClientSide) && (☃.isBurning()) && (☃ > 0) && (☃.getBoundingBox().b <= ☃))
    {
      ☃.extinguish();
      
      a(☃, ☃, ☃, ☃ - 1);
    }
  }
  
  public boolean interact(World ☃, BlockPosition ☃, IBlockData ☃, EntityHuman ☃, EnumDirection ☃, float ☃, float ☃, float ☃)
  {
    if (☃.isClientSide) {
      return true;
    }
    ItemStack ☃ = ☃.inventory.getItemInHand();
    if (☃ == null) {
      return true;
    }
    int ☃ = ((Integer)☃.get(LEVEL)).intValue();
    Item ☃ = ☃.getItem();
    if (☃ == Items.WATER_BUCKET)
    {
      if (☃ < 3)
      {
        if (!☃.abilities.canInstantlyBuild) {
          ☃.inventory.setItem(☃.inventory.itemInHandIndex, new ItemStack(Items.BUCKET));
        }
        ☃.b(StatisticList.I);
        
        a(☃, ☃, ☃, 3);
      }
      return true;
    }
    if (☃ == Items.GLASS_BOTTLE)
    {
      if (☃ > 0)
      {
        if (!☃.abilities.canInstantlyBuild)
        {
          ItemStack ☃ = new ItemStack(Items.POTION, 1, 0);
          if (!☃.inventory.pickup(☃)) {
            ☃.addEntity(new EntityItem(☃, ☃.getX() + 0.5D, ☃.getY() + 1.5D, ☃.getZ() + 0.5D, ☃));
          } else if ((☃ instanceof EntityPlayer)) {
            ((EntityPlayer)☃).updateInventory(☃.defaultContainer);
          }
          ☃.b(StatisticList.J);
          
          ☃.count -= 1;
          if (☃.count <= 0) {
            ☃.inventory.setItem(☃.inventory.itemInHandIndex, null);
          }
        }
        a(☃, ☃, ☃, ☃ - 1);
      }
      return true;
    }
    if ((☃ > 0) && ((☃ instanceof ItemArmor)))
    {
      ItemArmor ☃ = (ItemArmor)☃;
      if ((☃.x_() == ItemArmor.EnumArmorMaterial.LEATHER) && (☃.d_(☃)))
      {
        ☃.c(☃);
        a(☃, ☃, ☃, ☃ - 1);
        ☃.b(StatisticList.K);
        return true;
      }
    }
    if ((☃ > 0) && ((☃ instanceof ItemBanner)) && 
      (TileEntityBanner.c(☃) > 0))
    {
      ItemStack ☃ = ☃.cloneItemStack();
      ☃.count = 1;
      TileEntityBanner.e(☃);
      if ((☃.count > 1) || (☃.abilities.canInstantlyBuild))
      {
        if (!☃.inventory.pickup(☃)) {
          ☃.addEntity(new EntityItem(☃, ☃.getX() + 0.5D, ☃.getY() + 1.5D, ☃.getZ() + 0.5D, ☃));
        } else if ((☃ instanceof EntityPlayer)) {
          ((EntityPlayer)☃).updateInventory(☃.defaultContainer);
        }
        ☃.b(StatisticList.L);
        if (!☃.abilities.canInstantlyBuild) {
          ☃.count -= 1;
        }
      }
      else
      {
        ☃.inventory.setItem(☃.inventory.itemInHandIndex, ☃);
      }
      if (!☃.abilities.canInstantlyBuild) {
        a(☃, ☃, ☃, ☃ - 1);
      }
      return true;
    }
    return false;
  }
  
  public void a(World ☃, BlockPosition ☃, IBlockData ☃, int ☃)
  {
    ☃.setTypeAndData(☃, ☃.set(LEVEL, Integer.valueOf(MathHelper.clamp(☃, 0, 3))), 2);
    ☃.updateAdjacentComparators(☃, this);
  }
  
  public void k(World ☃, BlockPosition ☃)
  {
    if (☃.random.nextInt(20) != 1) {
      return;
    }
    IBlockData ☃ = ☃.getType(☃);
    if (((Integer)☃.get(LEVEL)).intValue() < 3) {
      ☃.setTypeAndData(☃, ☃.a(LEVEL), 2);
    }
  }
  
  public Item getDropType(IBlockData ☃, Random ☃, int ☃)
  {
    return Items.CAULDRON;
  }
  
  public boolean isComplexRedstone()
  {
    return true;
  }
  
  public int l(World ☃, BlockPosition ☃)
  {
    return ((Integer)☃.getType(☃).get(LEVEL)).intValue();
  }
  
  public IBlockData fromLegacyData(int ☃)
  {
    return getBlockData().set(LEVEL, Integer.valueOf(☃));
  }
  
  public int toLegacyData(IBlockData ☃)
  {
    return ((Integer)☃.get(LEVEL)).intValue();
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { LEVEL });
  }
}
