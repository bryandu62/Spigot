package net.minecraft.server.v1_8_R3;

public class ItemNameTag
  extends Item
{
  public ItemNameTag()
  {
    a(CreativeModeTab.i);
  }
  
  public boolean a(ItemStack ☃, EntityHuman ☃, EntityLiving ☃)
  {
    if (!☃.hasName()) {
      return false;
    }
    if ((☃ instanceof EntityInsentient))
    {
      EntityInsentient ☃ = (EntityInsentient)☃;
      ☃.setCustomName(☃.getName());
      ☃.bX();
      ☃.count -= 1;
      return true;
    }
    return super.a(☃, ☃, ☃);
  }
}
