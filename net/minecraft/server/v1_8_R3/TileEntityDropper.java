package net.minecraft.server.v1_8_R3;

public class TileEntityDropper
  extends TileEntityDispenser
{
  public String getName()
  {
    return hasCustomName() ? this.a : "container.dropper";
  }
  
  public String getContainerName()
  {
    return "minecraft:dropper";
  }
}
