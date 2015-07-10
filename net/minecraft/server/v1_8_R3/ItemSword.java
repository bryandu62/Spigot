package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Multimap;

public class ItemSword
  extends Item
{
  private float a;
  private final Item.EnumToolMaterial b;
  
  public ItemSword(Item.EnumToolMaterial ☃)
  {
    this.b = ☃;
    this.maxStackSize = 1;
    setMaxDurability(☃.a());
    a(CreativeModeTab.j);
    
    this.a = (4.0F + ☃.c());
  }
  
  public float g()
  {
    return this.b.c();
  }
  
  public float getDestroySpeed(ItemStack ☃, Block ☃)
  {
    if (☃ == Blocks.WEB) {
      return 15.0F;
    }
    Material ☃ = ☃.getMaterial();
    if ((☃ == Material.PLANT) || (☃ == Material.REPLACEABLE_PLANT) || (☃ == Material.CORAL) || (☃ == Material.LEAVES) || (☃ == Material.PUMPKIN)) {
      return 1.5F;
    }
    return 1.0F;
  }
  
  public boolean a(ItemStack ☃, EntityLiving ☃, EntityLiving ☃)
  {
    ☃.damage(1, ☃);
    return true;
  }
  
  public boolean a(ItemStack ☃, World ☃, Block ☃, BlockPosition ☃, EntityLiving ☃)
  {
    if (☃.g(☃, ☃) != 0.0D) {
      ☃.damage(2, ☃);
    }
    return true;
  }
  
  public EnumAnimation e(ItemStack ☃)
  {
    return EnumAnimation.BLOCK;
  }
  
  public int d(ItemStack ☃)
  {
    return 72000;
  }
  
  public ItemStack a(ItemStack ☃, World ☃, EntityHuman ☃)
  {
    ☃.a(☃, d(☃));
    return ☃;
  }
  
  public boolean canDestroySpecialBlock(Block ☃)
  {
    return ☃ == Blocks.WEB;
  }
  
  public int b()
  {
    return this.b.e();
  }
  
  public String h()
  {
    return this.b.toString();
  }
  
  public boolean a(ItemStack ☃, ItemStack ☃)
  {
    if (this.b.f() == ☃.getItem()) {
      return true;
    }
    return super.a(☃, ☃);
  }
  
  public Multimap<String, AttributeModifier> i()
  {
    Multimap<String, AttributeModifier> ☃ = super.i();
    
    ☃.put(GenericAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(f, "Weapon modifier", this.a, 0));
    
    return ☃;
  }
}
