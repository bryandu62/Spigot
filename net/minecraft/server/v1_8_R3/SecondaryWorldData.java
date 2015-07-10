package net.minecraft.server.v1_8_R3;

public class SecondaryWorldData
  extends WorldData
{
  private final WorldData b;
  
  public SecondaryWorldData(WorldData ☃)
  {
    this.b = ☃;
  }
  
  public NBTTagCompound a()
  {
    return this.b.a();
  }
  
  public NBTTagCompound a(NBTTagCompound ☃)
  {
    return this.b.a(☃);
  }
  
  public long getSeed()
  {
    return this.b.getSeed();
  }
  
  public int c()
  {
    return this.b.c();
  }
  
  public int d()
  {
    return this.b.d();
  }
  
  public int e()
  {
    return this.b.e();
  }
  
  public long getTime()
  {
    return this.b.getTime();
  }
  
  public long getDayTime()
  {
    return this.b.getDayTime();
  }
  
  public NBTTagCompound i()
  {
    return this.b.i();
  }
  
  public String getName()
  {
    return this.b.getName();
  }
  
  public int l()
  {
    return this.b.l();
  }
  
  public boolean isThundering()
  {
    return this.b.isThundering();
  }
  
  public int getThunderDuration()
  {
    return this.b.getThunderDuration();
  }
  
  public boolean hasStorm()
  {
    return this.b.hasStorm();
  }
  
  public int getWeatherDuration()
  {
    return this.b.getWeatherDuration();
  }
  
  public WorldSettings.EnumGamemode getGameType()
  {
    return this.b.getGameType();
  }
  
  public void setTime(long ☃) {}
  
  public void setDayTime(long ☃) {}
  
  public void setSpawn(BlockPosition ☃) {}
  
  public void a(String ☃) {}
  
  public void e(int ☃) {}
  
  public void setThundering(boolean ☃) {}
  
  public void setThunderDuration(int ☃) {}
  
  public void setStorm(boolean ☃) {}
  
  public void setWeatherDuration(int ☃) {}
  
  public boolean shouldGenerateMapFeatures()
  {
    return this.b.shouldGenerateMapFeatures();
  }
  
  public boolean isHardcore()
  {
    return this.b.isHardcore();
  }
  
  public WorldType getType()
  {
    return this.b.getType();
  }
  
  public void a(WorldType ☃) {}
  
  public boolean v()
  {
    return this.b.v();
  }
  
  public void c(boolean ☃) {}
  
  public boolean w()
  {
    return this.b.w();
  }
  
  public void d(boolean ☃) {}
  
  public GameRules x()
  {
    return this.b.x();
  }
  
  public EnumDifficulty getDifficulty()
  {
    return this.b.getDifficulty();
  }
  
  public void setDifficulty(EnumDifficulty ☃) {}
  
  public boolean isDifficultyLocked()
  {
    return this.b.isDifficultyLocked();
  }
  
  public void e(boolean ☃) {}
}
