package net.minecraft.server.v1_8_R3;

public class TileEntityComparator
  extends TileEntity
{
  private int a;
  
  public void b(NBTTagCompound ☃)
  {
    super.b(☃);
    ☃.setInt("OutputSignal", this.a);
  }
  
  public void a(NBTTagCompound ☃)
  {
    super.a(☃);
    this.a = ☃.getInt("OutputSignal");
  }
  
  public int b()
  {
    return this.a;
  }
  
  public void a(int ☃)
  {
    this.a = ☃;
  }
}
