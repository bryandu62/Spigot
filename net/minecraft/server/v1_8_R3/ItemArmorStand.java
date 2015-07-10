package net.minecraft.server.v1_8_R3;

import java.util.List;
import java.util.Random;

public class ItemArmorStand
  extends Item
{
  public ItemArmorStand()
  {
    a(CreativeModeTab.c);
  }
  
  public boolean interactWith(ItemStack ☃, EntityHuman ☃, World ☃, BlockPosition ☃, EnumDirection ☃, float ☃, float ☃, float ☃)
  {
    if (☃ == EnumDirection.DOWN) {
      return false;
    }
    boolean ☃ = ☃.getType(☃).getBlock().a(☃, ☃);
    BlockPosition ☃ = ☃ ? ☃ : ☃.shift(☃);
    if (!☃.a(☃, ☃, ☃)) {
      return false;
    }
    BlockPosition ☃ = ☃.up();
    boolean ☃ = (!☃.isEmpty(☃)) && (!☃.getType(☃).getBlock().a(☃, ☃));
    ☃ |= ((!☃.isEmpty(☃)) && (!☃.getType(☃).getBlock().a(☃, ☃)));
    if (☃) {
      return false;
    }
    double ☃ = ☃.getX();
    double ☃ = ☃.getY();
    double ☃ = ☃.getZ();
    
    List<Entity> ☃ = ☃.getEntities(null, AxisAlignedBB.a(☃, ☃, ☃, ☃ + 1.0D, ☃ + 2.0D, ☃ + 1.0D));
    if (☃.size() > 0) {
      return false;
    }
    if (!☃.isClientSide)
    {
      ☃.setAir(☃);
      ☃.setAir(☃);
      
      EntityArmorStand ☃ = new EntityArmorStand(☃, ☃ + 0.5D, ☃, ☃ + 0.5D);
      float ☃ = MathHelper.d((MathHelper.g(☃.yaw - 180.0F) + 22.5F) / 45.0F) * 45.0F;
      ☃.setPositionRotation(☃ + 0.5D, ☃, ☃ + 0.5D, ☃, 0.0F);
      a(☃, ☃.random);
      NBTTagCompound ☃ = ☃.getTag();
      if ((☃ != null) && (☃.hasKeyOfType("EntityTag", 10)))
      {
        NBTTagCompound ☃ = new NBTTagCompound();
        ☃.d(☃);
        ☃.a(☃.getCompound("EntityTag"));
        ☃.f(☃);
      }
      ☃.addEntity(☃);
    }
    ☃.count -= 1;
    return true;
  }
  
  private void a(EntityArmorStand ☃, Random ☃)
  {
    Vector3f ☃ = ☃.t();
    float ☃ = ☃.nextFloat() * 5.0F;
    float ☃ = ☃.nextFloat() * 20.0F - 10.0F;
    Vector3f ☃ = new Vector3f(☃.getX() + ☃, ☃.getY() + ☃, ☃.getZ());
    ☃.setHeadPose(☃);
    
    ☃ = ☃.u();
    ☃ = ☃.nextFloat() * 10.0F - 5.0F;
    ☃ = new Vector3f(☃.getX(), ☃.getY() + ☃, ☃.getZ());
    ☃.setBodyPose(☃);
  }
}
