package net.minecraft.server.v1_8_R3;

public abstract interface IMojangStatistics
{
  public abstract void a(MojangStatisticsGenerator paramMojangStatisticsGenerator);
  
  public abstract void b(MojangStatisticsGenerator paramMojangStatisticsGenerator);
  
  public abstract boolean getSnooperEnabled();
}
