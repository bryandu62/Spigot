package net.minecraft.server.v1_8_R3;

public class DemoWorldServer
  extends WorldServer
{
  private static final long I = "North Carolina".hashCode();
  public static final WorldSettings a = new WorldSettings(I, WorldSettings.EnumGamemode.SURVIVAL, true, false, WorldType.NORMAL).a();
  
  public DemoWorldServer(MinecraftServer ☃, IDataManager ☃, WorldData ☃, int ☃, MethodProfiler ☃)
  {
    super(☃, ☃, ☃, ☃, ☃);
    this.worldData.a(a);
  }
}
