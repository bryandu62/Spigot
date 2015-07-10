package net.minecraft.server.v1_8_R3;

import com.google.common.base.Predicate;

public class BlockPredicate
  implements Predicate<IBlockData>
{
  private final Block a;
  
  private BlockPredicate(Block ☃)
  {
    this.a = ☃;
  }
  
  public static BlockPredicate a(Block ☃)
  {
    return new BlockPredicate(☃);
  }
  
  public boolean a(IBlockData ☃)
  {
    if ((☃ == null) || (☃.getBlock() != this.a)) {
      return false;
    }
    return true;
  }
}
