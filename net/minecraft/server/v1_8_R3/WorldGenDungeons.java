package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorldGenDungeons
  extends WorldGenerator
{
  private static final Logger a = ;
  private static final String[] b = { "Skeleton", "Zombie", "Zombie", "Spider" };
  private static final List<StructurePieceTreasure> c = Lists.newArrayList(new StructurePieceTreasure[] { new StructurePieceTreasure(Items.SADDLE, 0, 1, 1, 10), new StructurePieceTreasure(Items.IRON_INGOT, 0, 1, 4, 10), new StructurePieceTreasure(Items.BREAD, 0, 1, 1, 10), new StructurePieceTreasure(Items.WHEAT, 0, 1, 4, 10), new StructurePieceTreasure(Items.GUNPOWDER, 0, 1, 4, 10), new StructurePieceTreasure(Items.STRING, 0, 1, 4, 10), new StructurePieceTreasure(Items.BUCKET, 0, 1, 1, 10), new StructurePieceTreasure(Items.GOLDEN_APPLE, 0, 1, 1, 1), new StructurePieceTreasure(Items.REDSTONE, 0, 1, 4, 10), new StructurePieceTreasure(Items.RECORD_13, 0, 1, 1, 4), new StructurePieceTreasure(Items.RECORD_CAT, 0, 1, 1, 4), new StructurePieceTreasure(Items.NAME_TAG, 0, 1, 1, 10), new StructurePieceTreasure(Items.GOLDEN_HORSE_ARMOR, 0, 1, 1, 2), new StructurePieceTreasure(Items.IRON_HORSE_ARMOR, 0, 1, 1, 5), new StructurePieceTreasure(Items.DIAMOND_HORSE_ARMOR, 0, 1, 1, 1) });
  
  public boolean generate(World ☃, Random ☃, BlockPosition ☃)
  {
    int ☃ = 3;
    int ☃ = ☃.nextInt(2) + 2;
    int ☃ = -☃ - 1;
    int ☃ = ☃ + 1;
    
    int ☃ = -1;
    int ☃ = 4;
    
    int ☃ = ☃.nextInt(2) + 2;
    int ☃ = -☃ - 1;
    int ☃ = ☃ + 1;
    
    int ☃ = 0;
    for (int ☃ = ☃; ☃ <= ☃; ☃++) {
      for (int ☃ = -1; ☃ <= 4; ☃++) {
        for (int ☃ = ☃; ☃ <= ☃; ☃++)
        {
          BlockPosition ☃ = ☃.a(☃, ☃, ☃);
          Material ☃ = ☃.getType(☃).getBlock().getMaterial();
          boolean ☃ = ☃.isBuildable();
          if ((☃ == -1) && (!☃)) {
            return false;
          }
          if ((☃ == 4) && (!☃)) {
            return false;
          }
          if (((☃ == ☃) || (☃ == ☃) || (☃ == ☃) || (☃ == ☃)) && 
            (☃ == 0) && (☃.isEmpty(☃)) && (☃.isEmpty(☃.up()))) {
            ☃++;
          }
        }
      }
    }
    if ((☃ < 1) || (☃ > 5)) {
      return false;
    }
    for (int ☃ = ☃; ☃ <= ☃; ☃++) {
      for (int ☃ = 3; ☃ >= -1; ☃--) {
        for (int ☃ = ☃; ☃ <= ☃; ☃++)
        {
          BlockPosition ☃ = ☃.a(☃, ☃, ☃);
          if ((☃ == ☃) || (☃ == -1) || (☃ == ☃) || (☃ == ☃) || (☃ == 4) || (☃ == ☃))
          {
            if ((☃.getY() >= 0) && (!☃.getType(☃.down()).getBlock().getMaterial().isBuildable())) {
              ☃.setAir(☃);
            } else if ((☃.getType(☃).getBlock().getMaterial().isBuildable()) && 
              (☃.getType(☃).getBlock() != Blocks.CHEST)) {
              if ((☃ == -1) && (☃.nextInt(4) != 0)) {
                ☃.setTypeAndData(☃, Blocks.MOSSY_COBBLESTONE.getBlockData(), 2);
              } else {
                ☃.setTypeAndData(☃, Blocks.COBBLESTONE.getBlockData(), 2);
              }
            }
          }
          else if (☃.getType(☃).getBlock() != Blocks.CHEST) {
            ☃.setAir(☃);
          }
        }
      }
    }
    for (int ☃ = 0; ☃ < 2; ☃++) {
      for (int ☃ = 0; ☃ < 3; ☃++)
      {
        int ☃ = ☃.getX() + ☃.nextInt(☃ * 2 + 1) - ☃;
        int ☃ = ☃.getY();
        int ☃ = ☃.getZ() + ☃.nextInt(☃ * 2 + 1) - ☃;
        BlockPosition ☃ = new BlockPosition(☃, ☃, ☃);
        if (☃.isEmpty(☃))
        {
          int ☃ = 0;
          for (EnumDirection ☃ : EnumDirection.EnumDirectionLimit.HORIZONTAL) {
            if (☃.getType(☃.shift(☃)).getBlock().getMaterial().isBuildable()) {
              ☃++;
            }
          }
          if (☃ == 1)
          {
            ☃.setTypeAndData(☃, Blocks.CHEST.f(☃, ☃, Blocks.CHEST.getBlockData()), 2);
            
            List<StructurePieceTreasure> ☃ = StructurePieceTreasure.a(c, new StructurePieceTreasure[] { Items.ENCHANTED_BOOK.b(☃) });
            
            TileEntity ☃ = ☃.getTileEntity(☃);
            if (!(☃ instanceof TileEntityChest)) {
              break;
            }
            StructurePieceTreasure.a(☃, ☃, (TileEntityChest)☃, 8); break;
          }
        }
      }
    }
    ☃.setTypeAndData(☃, Blocks.MOB_SPAWNER.getBlockData(), 2);
    TileEntity ☃ = ☃.getTileEntity(☃);
    if ((☃ instanceof TileEntityMobSpawner)) {
      ((TileEntityMobSpawner)☃).getSpawner().setMobName(a(☃));
    } else {
      a.error("Failed to fetch mob spawner entity at (" + ☃.getX() + ", " + ☃.getY() + ", " + ☃.getZ() + ")");
    }
    return true;
  }
  
  private String a(Random ☃)
  {
    return b[☃.nextInt(b.length)];
  }
}
