package net.minecraft.server.v1_8_R3;

public abstract class PersistentBase
{
  public final String id;
  private boolean b;
  
  public PersistentBase(String ☃)
  {
    this.id = ☃;
  }
  
  public abstract void a(NBTTagCompound paramNBTTagCompound);
  
  public abstract void b(NBTTagCompound paramNBTTagCompound);
  
  public void c()
  {
    a(true);
  }
  
  public void a(boolean ☃)
  {
    this.b = ☃;
  }
  
  public boolean d()
  {
    return this.b;
  }
}
