package net.minecraft.server.v1_8_R3;

public class ItemFireworks
  extends Item
{
  public boolean interactWith(ItemStack ☃, EntityHuman ☃, World ☃, BlockPosition ☃, EnumDirection ☃, float ☃, float ☃, float ☃)
  {
    if (!☃.isClientSide)
    {
      EntityFireworks ☃ = new EntityFireworks(☃, ☃.getX() + ☃, ☃.getY() + ☃, ☃.getZ() + ☃, ☃);
      ☃.addEntity(☃);
      if (!☃.abilities.canInstantlyBuild) {
        ☃.count -= 1;
      }
      return true;
    }
    return false;
  }
}
