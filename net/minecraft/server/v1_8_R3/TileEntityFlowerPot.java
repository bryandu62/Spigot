package net.minecraft.server.v1_8_R3;

public class TileEntityFlowerPot
  extends TileEntity
{
  private Item a;
  private int f;
  
  public TileEntityFlowerPot() {}
  
  public TileEntityFlowerPot(Item ☃, int ☃)
  {
    this.a = ☃;
    this.f = ☃;
  }
  
  public void b(NBTTagCompound ☃)
  {
    super.b(☃);
    MinecraftKey ☃ = (MinecraftKey)Item.REGISTRY.c(this.a);
    ☃.setString("Item", ☃ == null ? "" : ☃.toString());
    ☃.setInt("Data", this.f);
  }
  
  public void a(NBTTagCompound ☃)
  {
    super.a(☃);
    if (☃.hasKeyOfType("Item", 8)) {
      this.a = Item.d(☃.getString("Item"));
    } else {
      this.a = Item.getById(☃.getInt("Item"));
    }
    this.f = ☃.getInt("Data");
  }
  
  public Packet getUpdatePacket()
  {
    NBTTagCompound ☃ = new NBTTagCompound();
    b(☃);
    
    ☃.remove("Item");
    ☃.setInt("Item", Item.getId(this.a));
    return new PacketPlayOutTileEntityData(this.position, 5, ☃);
  }
  
  public void a(Item ☃, int ☃)
  {
    this.a = ☃;
    this.f = ☃;
  }
  
  public Item b()
  {
    return this.a;
  }
  
  public int c()
  {
    return this.f;
  }
}
