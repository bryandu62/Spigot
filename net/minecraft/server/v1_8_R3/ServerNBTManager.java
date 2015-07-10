package net.minecraft.server.v1_8_R3;

import java.io.File;

public class ServerNBTManager
  extends WorldNBTStorage
{
  public ServerNBTManager(File ☃, String ☃, boolean ☃)
  {
    super(☃, ☃, ☃);
  }
  
  public IChunkLoader createChunkLoader(WorldProvider ☃)
  {
    File ☃ = getDirectory();
    if ((☃ instanceof WorldProviderHell))
    {
      File ☃ = new File(☃, "DIM-1");
      ☃.mkdirs();
      return new ChunkRegionLoader(☃);
    }
    if ((☃ instanceof WorldProviderTheEnd))
    {
      File ☃ = new File(☃, "DIM1");
      ☃.mkdirs();
      return new ChunkRegionLoader(☃);
    }
    return new ChunkRegionLoader(☃);
  }
  
  public void saveWorldData(WorldData ☃, NBTTagCompound ☃)
  {
    ☃.e(19133);
    super.saveWorldData(☃, ☃);
  }
  
  public void a()
  {
    try
    {
      FileIOThread.a().b();
    }
    catch (InterruptedException ☃)
    {
      ☃.printStackTrace();
    }
    RegionFileCache.a();
  }
}
