package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class InventoryUtils
{
  private static final Random a = new Random();
  
  public static void dropInventory(World ☃, BlockPosition ☃, IInventory ☃)
  {
    dropInventory(☃, ☃.getX(), ☃.getY(), ☃.getZ(), ☃);
  }
  
  public static void dropEntity(World ☃, Entity ☃, IInventory ☃)
  {
    dropInventory(☃, ☃.locX, ☃.locY, ☃.locZ, ☃);
  }
  
  private static void dropInventory(World ☃, double ☃, double ☃, double ☃, IInventory ☃)
  {
    for (int ☃ = 0; ☃ < ☃.getSize(); ☃++)
    {
      ItemStack ☃ = ☃.getItem(☃);
      if (☃ != null) {
        dropItem(☃, ☃, ☃, ☃, ☃);
      }
    }
  }
  
  private static void dropItem(World ☃, double ☃, double ☃, double ☃, ItemStack ☃)
  {
    float ☃ = a.nextFloat() * 0.8F + 0.1F;
    float ☃ = a.nextFloat() * 0.8F + 0.1F;
    float ☃ = a.nextFloat() * 0.8F + 0.1F;
    while (☃.count > 0)
    {
      int ☃ = a.nextInt(21) + 10;
      if (☃ > ☃.count) {
        ☃ = ☃.count;
      }
      ☃.count -= ☃;
      
      EntityItem ☃ = new EntityItem(☃, ☃ + ☃, ☃ + ☃, ☃ + ☃, new ItemStack(☃.getItem(), ☃, ☃.getData()));
      if (☃.hasTag()) {
        ☃.getItemStack().setTag((NBTTagCompound)☃.getTag().clone());
      }
      float ☃ = 0.05F;
      ☃.motX = (a.nextGaussian() * ☃);
      ☃.motY = (a.nextGaussian() * ☃ + 0.20000000298023224D);
      ☃.motZ = (a.nextGaussian() * ☃);
      
      ☃.addEntity(☃);
    }
  }
}
