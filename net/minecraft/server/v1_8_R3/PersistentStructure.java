package net.minecraft.server.v1_8_R3;

public class PersistentStructure
  extends PersistentBase
{
  private NBTTagCompound b;
  
  public PersistentStructure(String ☃)
  {
    super(☃);
    this.b = new NBTTagCompound();
  }
  
  public void a(NBTTagCompound ☃)
  {
    this.b = ☃.getCompound("Features");
  }
  
  public void b(NBTTagCompound ☃)
  {
    ☃.set("Features", this.b);
  }
  
  public void a(NBTTagCompound ☃, int ☃, int ☃)
  {
    this.b.set(b(☃, ☃), ☃);
  }
  
  public static String b(int ☃, int ☃)
  {
    return "[" + ☃ + "," + ☃ + "]";
  }
  
  public NBTTagCompound a()
  {
    return this.b;
  }
}
