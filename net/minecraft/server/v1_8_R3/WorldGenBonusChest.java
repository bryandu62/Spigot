package net.minecraft.server.v1_8_R3;

import java.util.List;
import java.util.Random;

public class WorldGenBonusChest
  extends WorldGenerator
{
  private final List<StructurePieceTreasure> a;
  private final int b;
  
  public WorldGenBonusChest(List<StructurePieceTreasure> ☃, int ☃)
  {
    this.a = ☃;
    this.b = ☃;
  }
  
  public boolean generate(World ☃, Random ☃, BlockPosition ☃)
  {
    Block ☃;
    while ((((☃ = ☃.getType(☃).getBlock()).getMaterial() == Material.AIR) || (☃.getMaterial() == Material.LEAVES)) && (☃.getY() > 1)) {
      ☃ = ☃.down();
    }
    if (☃.getY() < 1) {
      return false;
    }
    ☃ = ☃.up();
    for (int ☃ = 0; ☃ < 4; ☃++)
    {
      BlockPosition ☃ = ☃.a(☃.nextInt(4) - ☃.nextInt(4), ☃.nextInt(3) - ☃.nextInt(3), ☃.nextInt(4) - ☃.nextInt(4));
      if ((☃.isEmpty(☃)) && (World.a(☃, ☃.down())))
      {
        ☃.setTypeAndData(☃, Blocks.CHEST.getBlockData(), 2);
        
        TileEntity ☃ = ☃.getTileEntity(☃);
        if ((☃ instanceof TileEntityChest)) {
          StructurePieceTreasure.a(☃, this.a, (TileEntityChest)☃, this.b);
        }
        BlockPosition ☃ = ☃.east();
        BlockPosition ☃ = ☃.west();
        BlockPosition ☃ = ☃.north();
        BlockPosition ☃ = ☃.south();
        if ((☃.isEmpty(☃)) && (World.a(☃, ☃.down()))) {
          ☃.setTypeAndData(☃, Blocks.TORCH.getBlockData(), 2);
        }
        if ((☃.isEmpty(☃)) && (World.a(☃, ☃.down()))) {
          ☃.setTypeAndData(☃, Blocks.TORCH.getBlockData(), 2);
        }
        if ((☃.isEmpty(☃)) && (World.a(☃, ☃.down()))) {
          ☃.setTypeAndData(☃, Blocks.TORCH.getBlockData(), 2);
        }
        if ((☃.isEmpty(☃)) && (World.a(☃, ☃.down()))) {
          ☃.setTypeAndData(☃, Blocks.TORCH.getBlockData(), 2);
        }
        return true;
      }
    }
    return false;
  }
}
