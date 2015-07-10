package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ItemPotion
  extends Item
{
  private Map<Integer, List<MobEffect>> a = Maps.newHashMap();
  private static final Map<List<MobEffect>, Integer> b = ;
  
  public ItemPotion()
  {
    c(1);
    a(true);
    setMaxDurability(0);
    a(CreativeModeTab.k);
  }
  
  public List<MobEffect> h(ItemStack ☃)
  {
    if ((!☃.hasTag()) || (!☃.getTag().hasKeyOfType("CustomPotionEffects", 9)))
    {
      List<MobEffect> ☃ = (List)this.a.get(Integer.valueOf(☃.getData()));
      if (☃ == null)
      {
        ☃ = PotionBrewer.getEffects(☃.getData(), false);
        this.a.put(Integer.valueOf(☃.getData()), ☃);
      }
      return ☃;
    }
    List<MobEffect> ☃ = Lists.newArrayList();
    NBTTagList ☃ = ☃.getTag().getList("CustomPotionEffects", 10);
    for (int ☃ = 0; ☃ < ☃.size(); ☃++)
    {
      NBTTagCompound ☃ = ☃.get(☃);
      MobEffect ☃ = MobEffect.b(☃);
      if (☃ != null) {
        ☃.add(☃);
      }
    }
    return ☃;
  }
  
  public List<MobEffect> e(int ☃)
  {
    List<MobEffect> ☃ = (List)this.a.get(Integer.valueOf(☃));
    if (☃ == null)
    {
      ☃ = PotionBrewer.getEffects(☃, false);
      this.a.put(Integer.valueOf(☃), ☃);
    }
    return ☃;
  }
  
  public ItemStack b(ItemStack ☃, World ☃, EntityHuman ☃)
  {
    if (!☃.abilities.canInstantlyBuild) {
      ☃.count -= 1;
    }
    if (!☃.isClientSide)
    {
      List<MobEffect> ☃ = h(☃);
      if (☃ != null) {
        for (MobEffect ☃ : ☃) {
          ☃.addEffect(new MobEffect(☃));
        }
      }
    }
    ☃.b(StatisticList.USE_ITEM_COUNT[Item.getId(this)]);
    if (!☃.abilities.canInstantlyBuild)
    {
      if (☃.count <= 0) {
        return new ItemStack(Items.GLASS_BOTTLE);
      }
      ☃.inventory.pickup(new ItemStack(Items.GLASS_BOTTLE));
    }
    return ☃;
  }
  
  public int d(ItemStack ☃)
  {
    return 32;
  }
  
  public EnumAnimation e(ItemStack ☃)
  {
    return EnumAnimation.DRINK;
  }
  
  public ItemStack a(ItemStack ☃, World ☃, EntityHuman ☃)
  {
    if (f(☃.getData()))
    {
      if (!☃.abilities.canInstantlyBuild) {
        ☃.count -= 1;
      }
      ☃.makeSound(☃, "random.bow", 0.5F, 0.4F / (g.nextFloat() * 0.4F + 0.8F));
      if (!☃.isClientSide) {
        ☃.addEntity(new EntityPotion(☃, ☃, ☃));
      }
      ☃.b(StatisticList.USE_ITEM_COUNT[Item.getId(this)]);
      return ☃;
    }
    ☃.a(☃, d(☃));
    return ☃;
  }
  
  public static boolean f(int ☃)
  {
    return (☃ & 0x4000) != 0;
  }
  
  public String a(ItemStack ☃)
  {
    if (☃.getData() == 0) {
      return LocaleI18n.get("item.emptyPotion.name").trim();
    }
    String ☃ = "";
    if (f(☃.getData())) {
      ☃ = LocaleI18n.get("potion.prefix.grenade").trim() + " ";
    }
    List<MobEffect> ☃ = Items.POTION.h(☃);
    if ((☃ != null) && (!☃.isEmpty()))
    {
      String ☃ = ((MobEffect)☃.get(0)).g();
      ☃ = ☃ + ".postfix";
      return ☃ + LocaleI18n.get(☃).trim();
    }
    String ☃ = PotionBrewer.c(☃.getData());
    return LocaleI18n.get(☃).trim() + " " + super.a(☃);
  }
}
