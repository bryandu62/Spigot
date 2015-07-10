package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class StructurePieceTreasure
  extends WeightedRandom.WeightedRandomChoice
{
  private ItemStack b;
  private int c;
  private int d;
  
  public StructurePieceTreasure(Item ☃, int ☃, int ☃, int ☃, int ☃)
  {
    super(☃);
    this.b = new ItemStack(☃, 1, ☃);
    this.c = ☃;
    this.d = ☃;
  }
  
  public StructurePieceTreasure(ItemStack ☃, int ☃, int ☃, int ☃)
  {
    super(☃);
    this.b = ☃;
    this.c = ☃;
    this.d = ☃;
  }
  
  public static void a(Random ☃, List<StructurePieceTreasure> ☃, IInventory ☃, int ☃)
  {
    for (int ☃ = 0; ☃ < ☃; ☃++)
    {
      StructurePieceTreasure ☃ = (StructurePieceTreasure)WeightedRandom.a(☃, ☃);
      int ☃ = ☃.c + ☃.nextInt(☃.d - ☃.c + 1);
      if (☃.b.getMaxStackSize() >= ☃)
      {
        ItemStack ☃ = ☃.b.cloneItemStack();
        ☃.count = ☃;
        ☃.setItem(☃.nextInt(☃.getSize()), ☃);
      }
      else
      {
        for (int ☃ = 0; ☃ < ☃; ☃++)
        {
          ItemStack ☃ = ☃.b.cloneItemStack();
          ☃.count = 1;
          ☃.setItem(☃.nextInt(☃.getSize()), ☃);
        }
      }
    }
  }
  
  public static void a(Random ☃, List<StructurePieceTreasure> ☃, TileEntityDispenser ☃, int ☃)
  {
    for (int ☃ = 0; ☃ < ☃; ☃++)
    {
      StructurePieceTreasure ☃ = (StructurePieceTreasure)WeightedRandom.a(☃, ☃);
      int ☃ = ☃.c + ☃.nextInt(☃.d - ☃.c + 1);
      if (☃.b.getMaxStackSize() >= ☃)
      {
        ItemStack ☃ = ☃.b.cloneItemStack();
        ☃.count = ☃;
        ☃.setItem(☃.nextInt(☃.getSize()), ☃);
      }
      else
      {
        for (int ☃ = 0; ☃ < ☃; ☃++)
        {
          ItemStack ☃ = ☃.b.cloneItemStack();
          ☃.count = 1;
          ☃.setItem(☃.nextInt(☃.getSize()), ☃);
        }
      }
    }
  }
  
  public static List<StructurePieceTreasure> a(List<StructurePieceTreasure> ☃, StructurePieceTreasure... ☃)
  {
    List<StructurePieceTreasure> ☃ = Lists.newArrayList(☃);
    Collections.addAll(☃, ☃);
    
    return ☃;
  }
}
