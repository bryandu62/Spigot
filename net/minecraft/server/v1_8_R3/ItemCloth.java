package net.minecraft.server.v1_8_R3;

public class ItemCloth
  extends ItemBlock
{
  public ItemCloth(Block ☃)
  {
    super(☃);
    
    setMaxDurability(0);
    a(true);
  }
  
  public int filterData(int ☃)
  {
    return ☃;
  }
  
  public String e_(ItemStack ☃)
  {
    return super.getName() + "." + EnumColor.fromColorIndex(☃.getData()).d();
  }
}
