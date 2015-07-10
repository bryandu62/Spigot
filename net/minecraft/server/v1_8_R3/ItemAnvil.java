package net.minecraft.server.v1_8_R3;

public class ItemAnvil
  extends ItemMultiTexture
{
  public ItemAnvil(Block ☃)
  {
    super(☃, ☃, new String[] { "intact", "slightlyDamaged", "veryDamaged" });
  }
  
  public int filterData(int ☃)
  {
    return ☃ << 2;
  }
}
