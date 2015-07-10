package net.minecraft.server.v1_8_R3;

public abstract interface IPlayerFileData
{
  public abstract void save(EntityHuman paramEntityHuman);
  
  public abstract NBTTagCompound load(EntityHuman paramEntityHuman);
  
  public abstract String[] getSeenPlayers();
}
