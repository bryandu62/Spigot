package net.minecraft.server.v1_8_R3;

public class ItemSaddle
  extends Item
{
  public ItemSaddle()
  {
    this.maxStackSize = 1;
    a(CreativeModeTab.e);
  }
  
  public boolean a(ItemStack ☃, EntityHuman ☃, EntityLiving ☃)
  {
    if ((☃ instanceof EntityPig))
    {
      EntityPig ☃ = (EntityPig)☃;
      if ((!☃.hasSaddle()) && (!☃.isBaby()))
      {
        ☃.setSaddle(true);
        ☃.world.makeSound(☃, "mob.horse.leather", 0.5F, 1.0F);
        ☃.count -= 1;
      }
      return true;
    }
    return false;
  }
  
  public boolean a(ItemStack ☃, EntityLiving ☃, EntityLiving ☃)
  {
    a(☃, null, ☃);
    return true;
  }
}
