package net.minecraft.server.v1_8_R3;

public class ItemWithAuxData
  extends ItemBlock
{
  private final Block b;
  private String[] c;
  
  public ItemWithAuxData(Block ☃, boolean ☃)
  {
    super(☃);
    this.b = ☃;
    if (☃)
    {
      setMaxDurability(0);
      a(true);
    }
  }
  
  public int filterData(int ☃)
  {
    return ☃;
  }
  
  public ItemWithAuxData a(String[] ☃)
  {
    this.c = ☃;
    return this;
  }
  
  public String e_(ItemStack ☃)
  {
    if (this.c == null) {
      return super.e_(☃);
    }
    int ☃ = ☃.getData();
    if ((☃ >= 0) && (☃ < this.c.length)) {
      return super.e_(☃) + "." + this.c[☃];
    }
    return super.e_(☃);
  }
}
