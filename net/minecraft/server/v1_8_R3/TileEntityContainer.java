package net.minecraft.server.v1_8_R3;

public abstract class TileEntityContainer
  extends TileEntity
  implements ITileEntityContainer, ITileInventory
{
  private ChestLock a = ChestLock.a;
  
  public void a(NBTTagCompound ☃)
  {
    super.a(☃);
    
    this.a = ChestLock.b(☃);
  }
  
  public void b(NBTTagCompound ☃)
  {
    super.b(☃);
    if (this.a != null) {
      this.a.a(☃);
    }
  }
  
  public boolean r_()
  {
    return (this.a != null) && (!this.a.a());
  }
  
  public ChestLock i()
  {
    return this.a;
  }
  
  public void a(ChestLock ☃)
  {
    this.a = ☃;
  }
  
  public IChatBaseComponent getScoreboardDisplayName()
  {
    if (hasCustomName()) {
      return new ChatComponentText(getName());
    }
    return new ChatMessage(getName(), new Object[0]);
  }
}
