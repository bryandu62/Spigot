package net.minecraft.server.v1_8_R3;

import java.io.File;
import java.util.UUID;

public abstract interface IDataManager
{
  public abstract WorldData getWorldData();
  
  public abstract void checkSession()
    throws ExceptionWorldConflict;
  
  public abstract IChunkLoader createChunkLoader(WorldProvider paramWorldProvider);
  
  public abstract void saveWorldData(WorldData paramWorldData, NBTTagCompound paramNBTTagCompound);
  
  public abstract void saveWorldData(WorldData paramWorldData);
  
  public abstract IPlayerFileData getPlayerFileData();
  
  public abstract void a();
  
  public abstract File getDirectory();
  
  public abstract File getDataFile(String paramString);
  
  public abstract String g();
  
  public abstract UUID getUUID();
}
