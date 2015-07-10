package net.minecraft.server.v1_8_R3;

public abstract interface IDispenseBehavior
{
  public static final IDispenseBehavior a = new IDispenseBehavior()
  {
    public ItemStack a(ISourceBlock ☃, ItemStack ☃)
    {
      return ☃;
    }
  };
  
  public abstract ItemStack a(ISourceBlock paramISourceBlock, ItemStack paramItemStack);
}
