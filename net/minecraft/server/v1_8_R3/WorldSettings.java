package net.minecraft.server.v1_8_R3;

public final class WorldSettings
{
  private final long a;
  private final EnumGamemode b;
  private final boolean c;
  private final boolean d;
  private final WorldType e;
  private boolean f;
  private boolean g;
  
  public static enum EnumGamemode
  {
    int f;
    String g;
    
    private EnumGamemode(int ☃, String ☃)
    {
      this.f = ☃;
      this.g = ☃;
    }
    
    public int getId()
    {
      return this.f;
    }
    
    public String b()
    {
      return this.g;
    }
    
    public void a(PlayerAbilities ☃)
    {
      if (this == CREATIVE)
      {
        ☃.canFly = true;
        ☃.canInstantlyBuild = true;
        ☃.isInvulnerable = true;
      }
      else if (this == SPECTATOR)
      {
        ☃.canFly = true;
        ☃.canInstantlyBuild = false;
        ☃.isInvulnerable = true;
        ☃.isFlying = true;
      }
      else
      {
        ☃.canFly = false;
        ☃.canInstantlyBuild = false;
        ☃.isInvulnerable = false;
        ☃.isFlying = false;
      }
      ☃.mayBuild = (!c());
    }
    
    public boolean c()
    {
      return (this == ADVENTURE) || (this == SPECTATOR);
    }
    
    public boolean d()
    {
      return this == CREATIVE;
    }
    
    public boolean e()
    {
      return (this == SURVIVAL) || (this == ADVENTURE);
    }
    
    public static EnumGamemode getById(int ☃)
    {
      for (EnumGamemode ☃ : ) {
        if (☃.f == ☃) {
          return ☃;
        }
      }
      return SURVIVAL;
    }
  }
  
  private String h = "";
  
  public WorldSettings(long ☃, EnumGamemode ☃, boolean ☃, boolean ☃, WorldType ☃)
  {
    this.a = ☃;
    this.b = ☃;
    this.c = ☃;
    this.d = ☃;
    this.e = ☃;
  }
  
  public WorldSettings(WorldData ☃)
  {
    this(☃.getSeed(), ☃.getGameType(), ☃.shouldGenerateMapFeatures(), ☃.isHardcore(), ☃.getType());
  }
  
  public WorldSettings a()
  {
    this.g = true;
    return this;
  }
  
  public WorldSettings setGeneratorSettings(String ☃)
  {
    this.h = ☃;
    return this;
  }
  
  public boolean c()
  {
    return this.g;
  }
  
  public long d()
  {
    return this.a;
  }
  
  public EnumGamemode e()
  {
    return this.b;
  }
  
  public boolean f()
  {
    return this.d;
  }
  
  public boolean g()
  {
    return this.c;
  }
  
  public WorldType h()
  {
    return this.e;
  }
  
  public boolean i()
  {
    return this.f;
  }
  
  public static EnumGamemode a(int ☃)
  {
    return EnumGamemode.getById(☃);
  }
  
  public String j()
  {
    return this.h;
  }
}
