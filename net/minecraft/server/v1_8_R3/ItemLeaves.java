package net.minecraft.server.v1_8_R3;

public class ItemLeaves
  extends ItemBlock
{
  private final BlockLeaves b;
  
  public ItemLeaves(BlockLeaves ☃)
  {
    super(☃);
    this.b = ☃;
    
    setMaxDurability(0);
    a(true);
  }
  
  public int filterData(int ☃)
  {
    return ☃ | 0x4;
  }
  
  public String e_(ItemStack ☃)
  {
    return super.getName() + "." + this.b.b(☃.getData()).d();
  }
}
