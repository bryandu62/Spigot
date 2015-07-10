package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class BlockRepeater
  extends BlockDiodeAbstract
{
  public static final BlockStateBoolean LOCKED = BlockStateBoolean.of("locked");
  public static final BlockStateInteger DELAY = BlockStateInteger.of("delay", 1, 4);
  
  protected BlockRepeater(boolean ☃)
  {
    super(☃);
    j(this.blockStateList.getBlockData().set(FACING, EnumDirection.NORTH).set(DELAY, Integer.valueOf(1)).set(LOCKED, Boolean.valueOf(false)));
  }
  
  public String getName()
  {
    return LocaleI18n.get("item.diode.name");
  }
  
  public IBlockData updateState(IBlockData ☃, IBlockAccess ☃, BlockPosition ☃)
  {
    return ☃.set(LOCKED, Boolean.valueOf(b(☃, ☃, ☃)));
  }
  
  public boolean interact(World ☃, BlockPosition ☃, IBlockData ☃, EntityHuman ☃, EnumDirection ☃, float ☃, float ☃, float ☃)
  {
    if (!☃.abilities.mayBuild) {
      return false;
    }
    ☃.setTypeAndData(☃, ☃.a(DELAY), 3);
    return true;
  }
  
  protected int d(IBlockData ☃)
  {
    return ((Integer)☃.get(DELAY)).intValue() * 2;
  }
  
  protected IBlockData e(IBlockData ☃)
  {
    Integer ☃ = (Integer)☃.get(DELAY);
    Boolean ☃ = (Boolean)☃.get(LOCKED);
    EnumDirection ☃ = (EnumDirection)☃.get(FACING);
    return Blocks.POWERED_REPEATER.getBlockData().set(FACING, ☃).set(DELAY, ☃).set(LOCKED, ☃);
  }
  
  protected IBlockData k(IBlockData ☃)
  {
    Integer ☃ = (Integer)☃.get(DELAY);
    Boolean ☃ = (Boolean)☃.get(LOCKED);
    EnumDirection ☃ = (EnumDirection)☃.get(FACING);
    return Blocks.UNPOWERED_REPEATER.getBlockData().set(FACING, ☃).set(DELAY, ☃).set(LOCKED, ☃);
  }
  
  public Item getDropType(IBlockData ☃, Random ☃, int ☃)
  {
    return Items.REPEATER;
  }
  
  public boolean b(IBlockAccess ☃, BlockPosition ☃, IBlockData ☃)
  {
    return c(☃, ☃, ☃) > 0;
  }
  
  protected boolean c(Block ☃)
  {
    return d(☃);
  }
  
  public void remove(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    super.remove(☃, ☃, ☃);
    h(☃, ☃, ☃);
  }
  
  public IBlockData fromLegacyData(int ☃)
  {
    return getBlockData().set(FACING, EnumDirection.fromType2(☃)).set(LOCKED, Boolean.valueOf(false)).set(DELAY, Integer.valueOf(1 + (☃ >> 2)));
  }
  
  public int toLegacyData(IBlockData ☃)
  {
    int ☃ = 0;
    
    ☃ |= ((EnumDirection)☃.get(FACING)).b();
    ☃ |= ((Integer)☃.get(DELAY)).intValue() - 1 << 2;
    
    return ☃;
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { FACING, DELAY, LOCKED });
  }
}
