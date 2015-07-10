package net.minecraft.server.v1_8_R3;

import com.google.common.base.Function;

public class ItemMultiTexture
  extends ItemBlock
{
  protected final Block b;
  protected final Function<ItemStack, String> c;
  
  public ItemMultiTexture(Block ☃, Block ☃, Function<ItemStack, String> ☃)
  {
    super(☃);
    
    this.b = ☃;
    this.c = ☃;
    
    setMaxDurability(0);
    a(true);
  }
  
  public ItemMultiTexture(Block ☃, Block ☃, String[] ☃)
  {
    this(☃, ☃, new Function()
    {
      public String a(ItemStack ☃)
      {
        int ☃ = ☃.getData();
        if ((☃ < 0) || (☃ >= ItemMultiTexture.this.length)) {
          ☃ = 0;
        }
        return ItemMultiTexture.this[☃];
      }
    });
  }
  
  public int filterData(int ☃)
  {
    return ☃;
  }
  
  public String e_(ItemStack ☃)
  {
    return super.getName() + "." + (String)this.c.apply(☃);
  }
}
