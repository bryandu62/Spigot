package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Multimap;
import java.util.Set;

public class ItemTool
  extends Item
{
  private Set<Block> c;
  protected float a = 4.0F;
  private float d;
  protected Item.EnumToolMaterial b;
  
  protected ItemTool(float ☃, Item.EnumToolMaterial ☃, Set<Block> ☃)
  {
    this.b = ☃;
    this.c = ☃;
    this.maxStackSize = 1;
    setMaxDurability(☃.a());
    this.a = ☃.b();
    this.d = (☃ + ☃.c());
    a(CreativeModeTab.i);
  }
  
  public float getDestroySpeed(ItemStack ☃, Block ☃)
  {
    return this.c.contains(☃) ? this.a : 1.0F;
  }
  
  public boolean a(ItemStack ☃, EntityLiving ☃, EntityLiving ☃)
  {
    ☃.damage(2, ☃);
    return true;
  }
  
  public boolean a(ItemStack ☃, World ☃, Block ☃, BlockPosition ☃, EntityLiving ☃)
  {
    if (☃.g(☃, ☃) != 0.0D) {
      ☃.damage(1, ☃);
    }
    return true;
  }
  
  public Item.EnumToolMaterial g()
  {
    return this.b;
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
    
    ☃.put(GenericAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(f, "Tool modifier", this.d, 0));
    
    return ☃;
  }
}
