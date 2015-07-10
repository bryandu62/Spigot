package net.minecraft.server.v1_8_R3;

public class ItemSoup
  extends ItemFood
{
  public ItemSoup(int ☃)
  {
    super(☃, false);
    
    c(1);
  }
  
  public ItemStack b(ItemStack ☃, World ☃, EntityHuman ☃)
  {
    super.b(☃, ☃, ☃);
    
    return new ItemStack(Items.BOWL);
  }
}
