package net.minecraft.server.v1_8_R3;

public class ChestLock
{
  public static final ChestLock a = new ChestLock("");
  private final String b;
  
  public ChestLock(String ☃)
  {
    this.b = ☃;
  }
  
  public boolean a()
  {
    return (this.b == null) || (this.b.isEmpty());
  }
  
  public String b()
  {
    return this.b;
  }
  
  public void a(NBTTagCompound ☃)
  {
    ☃.setString("Lock", this.b);
  }
  
  public static ChestLock b(NBTTagCompound ☃)
  {
    if (☃.hasKeyOfType("Lock", 8))
    {
      String ☃ = ☃.getString("Lock");
      return new ChestLock(☃);
    }
    return a;
  }
}
