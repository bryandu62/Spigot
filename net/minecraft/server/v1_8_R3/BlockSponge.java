package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Queue;

public class BlockSponge
  extends Block
{
  public static final BlockStateBoolean WET = BlockStateBoolean.of("wet");
  
  protected BlockSponge()
  {
    super(Material.SPONGE);
    j(this.blockStateList.getBlockData().set(WET, Boolean.valueOf(false)));
    a(CreativeModeTab.b);
  }
  
  public String getName()
  {
    return LocaleI18n.get(a() + ".dry.name");
  }
  
  public int getDropData(IBlockData ☃)
  {
    return ((Boolean)☃.get(WET)).booleanValue() ? 1 : 0;
  }
  
  public void onPlace(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    e(☃, ☃, ☃);
  }
  
  public void doPhysics(World ☃, BlockPosition ☃, IBlockData ☃, Block ☃)
  {
    e(☃, ☃, ☃);
    super.doPhysics(☃, ☃, ☃, ☃);
  }
  
  protected void e(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    if ((!((Boolean)☃.get(WET)).booleanValue()) && (e(☃, ☃)))
    {
      ☃.setTypeAndData(☃, ☃.set(WET, Boolean.valueOf(true)), 2);
      ☃.triggerEffect(2001, ☃, Block.getId(Blocks.WATER));
    }
  }
  
  private boolean e(World ☃, BlockPosition ☃)
  {
    Queue<Tuple<BlockPosition, Integer>> ☃ = Lists.newLinkedList();
    ArrayList<BlockPosition> ☃ = Lists.newArrayList();
    ☃.add(new Tuple(☃, Integer.valueOf(0)));
    
    int ☃ = 0;
    while (!☃.isEmpty())
    {
      Tuple<BlockPosition, Integer> ☃ = (Tuple)☃.poll();
      BlockPosition ☃ = (BlockPosition)☃.a();
      int ☃ = ((Integer)☃.b()).intValue();
      for (EnumDirection ☃ : EnumDirection.values())
      {
        BlockPosition ☃ = ☃.shift(☃);
        if (☃.getType(☃).getBlock().getMaterial() == Material.WATER)
        {
          ☃.setTypeAndData(☃, Blocks.AIR.getBlockData(), 2);
          ☃.add(☃);
          ☃++;
          if (☃ < 6) {
            ☃.add(new Tuple(☃, Integer.valueOf(☃ + 1)));
          }
        }
      }
      if (☃ > 64) {
        break;
      }
    }
    for (BlockPosition ☃ : ☃) {
      ☃.applyPhysics(☃, Blocks.AIR);
    }
    return ☃ > 0;
  }
  
  public IBlockData fromLegacyData(int ☃)
  {
    return getBlockData().set(WET, Boolean.valueOf((☃ & 0x1) == 1));
  }
  
  public int toLegacyData(IBlockData ☃)
  {
    return ((Boolean)☃.get(WET)).booleanValue() ? 1 : 0;
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { WET });
  }
}
