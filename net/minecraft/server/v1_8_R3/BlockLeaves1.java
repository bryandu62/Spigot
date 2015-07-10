package net.minecraft.server.v1_8_R3;

import com.google.common.base.Predicate;
import java.util.Random;

public class BlockLeaves1
  extends BlockLeaves
{
  public static final BlockStateEnum<BlockWood.EnumLogVariant> VARIANT = BlockStateEnum.a("variant", BlockWood.EnumLogVariant.class, new Predicate()
  {
    public boolean a(BlockWood.EnumLogVariant ☃)
    {
      return ☃.a() < 4;
    }
  });
  
  public BlockLeaves1()
  {
    j(this.blockStateList.getBlockData().set(VARIANT, BlockWood.EnumLogVariant.OAK).set(CHECK_DECAY, Boolean.valueOf(true)).set(DECAYABLE, Boolean.valueOf(true)));
  }
  
  protected void a(World ☃, BlockPosition ☃, IBlockData ☃, int ☃)
  {
    if ((☃.get(VARIANT) == BlockWood.EnumLogVariant.OAK) && (☃.random.nextInt(☃) == 0)) {
      a(☃, ☃, new ItemStack(Items.APPLE, 1, 0));
    }
  }
  
  protected int d(IBlockData ☃)
  {
    if (☃.get(VARIANT) == BlockWood.EnumLogVariant.JUNGLE) {
      return 40;
    }
    return super.d(☃);
  }
  
  protected ItemStack i(IBlockData ☃)
  {
    return new ItemStack(Item.getItemOf(this), 1, ((BlockWood.EnumLogVariant)☃.get(VARIANT)).a());
  }
  
  public IBlockData fromLegacyData(int ☃)
  {
    return getBlockData().set(VARIANT, b(☃)).set(DECAYABLE, Boolean.valueOf((☃ & 0x4) == 0)).set(CHECK_DECAY, Boolean.valueOf((☃ & 0x8) > 0));
  }
  
  public int toLegacyData(IBlockData ☃)
  {
    int ☃ = 0;
    
    ☃ |= ((BlockWood.EnumLogVariant)☃.get(VARIANT)).a();
    if (!((Boolean)☃.get(DECAYABLE)).booleanValue()) {
      ☃ |= 0x4;
    }
    if (((Boolean)☃.get(CHECK_DECAY)).booleanValue()) {
      ☃ |= 0x8;
    }
    return ☃;
  }
  
  public BlockWood.EnumLogVariant b(int ☃)
  {
    return BlockWood.EnumLogVariant.a((☃ & 0x3) % 4);
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { VARIANT, CHECK_DECAY, DECAYABLE });
  }
  
  public int getDropData(IBlockData ☃)
  {
    return ((BlockWood.EnumLogVariant)☃.get(VARIANT)).a();
  }
  
  public void a(World ☃, EntityHuman ☃, BlockPosition ☃, IBlockData ☃, TileEntity ☃)
  {
    if ((!☃.isClientSide) && (☃.bZ() != null) && (☃.bZ().getItem() == Items.SHEARS))
    {
      ☃.b(StatisticList.MINE_BLOCK_COUNT[Block.getId(this)]);
      
      a(☃, ☃, new ItemStack(Item.getItemOf(this), 1, ((BlockWood.EnumLogVariant)☃.get(VARIANT)).a()));
      return;
    }
    super.a(☃, ☃, ☃, ☃, ☃);
  }
}
