package net.minecraft.server.v1_8_R3;

public class TileEntityLightDetector
  extends TileEntity
  implements IUpdatePlayerListBox
{
  public void c()
  {
    if ((this.world != null) && (!this.world.isClientSide) && (this.world.getTime() % 20L == 0L))
    {
      this.e = w();
      if ((this.e instanceof BlockDaylightDetector)) {
        ((BlockDaylightDetector)this.e).f(this.world, this.position);
      }
    }
  }
}
