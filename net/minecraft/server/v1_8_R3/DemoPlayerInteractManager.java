package net.minecraft.server.v1_8_R3;

public class DemoPlayerInteractManager
  extends PlayerInteractManager
{
  private boolean c;
  private boolean d;
  private int e;
  private int f;
  
  public DemoPlayerInteractManager(World ☃)
  {
    super(☃);
  }
  
  public void a()
  {
    super.a();
    this.f += 1;
    
    long ☃ = this.world.getTime();
    long ☃ = ☃ / 24000L + 1L;
    if ((!this.c) && (this.f > 20))
    {
      this.c = true;
      this.player.playerConnection.sendPacket(new PacketPlayOutGameStateChange(5, 0.0F));
    }
    this.d = (☃ > 120500L);
    if (this.d) {
      this.e += 1;
    }
    if (☃ % 24000L == 500L)
    {
      if (☃ <= 6L) {
        this.player.sendMessage(new ChatMessage("demo.day." + ☃, new Object[0]));
      }
    }
    else if (☃ == 1L)
    {
      if (☃ == 100L) {
        this.player.playerConnection.sendPacket(new PacketPlayOutGameStateChange(5, 101.0F));
      } else if (☃ == 175L) {
        this.player.playerConnection.sendPacket(new PacketPlayOutGameStateChange(5, 102.0F));
      } else if (☃ == 250L) {
        this.player.playerConnection.sendPacket(new PacketPlayOutGameStateChange(5, 103.0F));
      }
    }
    else if ((☃ == 5L) && 
      (☃ % 24000L == 22000L)) {
      this.player.sendMessage(new ChatMessage("demo.day.warning", new Object[0]));
    }
  }
  
  private void f()
  {
    if (this.e > 100)
    {
      this.player.sendMessage(new ChatMessage("demo.reminder", new Object[0]));
      this.e = 0;
    }
  }
  
  public void a(BlockPosition ☃, EnumDirection ☃)
  {
    if (this.d)
    {
      f();
      return;
    }
    super.a(☃, ☃);
  }
  
  public void a(BlockPosition ☃)
  {
    if (this.d) {
      return;
    }
    super.a(☃);
  }
  
  public boolean breakBlock(BlockPosition ☃)
  {
    if (this.d) {
      return false;
    }
    return super.breakBlock(☃);
  }
  
  public boolean useItem(EntityHuman ☃, World ☃, ItemStack ☃)
  {
    if (this.d)
    {
      f();
      return false;
    }
    return super.useItem(☃, ☃, ☃);
  }
  
  public boolean interact(EntityHuman ☃, World ☃, ItemStack ☃, BlockPosition ☃, EnumDirection ☃, float ☃, float ☃, float ☃)
  {
    if (this.d)
    {
      f();
      return false;
    }
    return super.interact(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
  }
}
