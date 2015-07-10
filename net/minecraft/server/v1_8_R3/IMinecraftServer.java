package net.minecraft.server.v1_8_R3;

public abstract interface IMinecraftServer
{
  public abstract int a(String paramString, int paramInt);
  
  public abstract String a(String paramString1, String paramString2);
  
  public abstract void a(String paramString, Object paramObject);
  
  public abstract void a();
  
  public abstract String b();
  
  public abstract String E();
  
  public abstract int F();
  
  public abstract String G();
  
  public abstract String getVersion();
  
  public abstract int I();
  
  public abstract int J();
  
  public abstract String[] getPlayers();
  
  public abstract String U();
  
  public abstract String getPlugins();
  
  public abstract String executeRemoteCommand(String paramString);
  
  public abstract boolean isDebugging();
  
  public abstract void info(String paramString);
  
  public abstract void warning(String paramString);
  
  public abstract void g(String paramString);
  
  public abstract void h(String paramString);
}
