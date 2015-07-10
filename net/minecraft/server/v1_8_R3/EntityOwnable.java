package net.minecraft.server.v1_8_R3;

public abstract interface EntityOwnable
{
  public abstract String getOwnerUUID();
  
  public abstract Entity getOwner();
}
