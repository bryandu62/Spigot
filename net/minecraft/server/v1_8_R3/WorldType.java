package net.minecraft.server.v1_8_R3;

public class WorldType
{
  public static final WorldType[] types = new WorldType[16];
  public static final WorldType NORMAL = new WorldType(0, "default", 1).i();
  public static final WorldType FLAT = new WorldType(1, "flat");
  public static final WorldType LARGE_BIOMES = new WorldType(2, "largeBiomes");
  public static final WorldType AMPLIFIED = new WorldType(3, "amplified").j();
  public static final WorldType CUSTOMIZED = new WorldType(4, "customized");
  public static final WorldType DEBUG_ALL_BLOCK_STATES = new WorldType(5, "debug_all_block_states");
  public static final WorldType NORMAL_1_1 = new WorldType(8, "default_1_1", 0).a(false);
  private final int i;
  private final String name;
  private final int version;
  private boolean l;
  private boolean m;
  private boolean n;
  
  private WorldType(int ☃, String ☃)
  {
    this(☃, ☃, 0);
  }
  
  private WorldType(int ☃, String ☃, int ☃)
  {
    this.name = ☃;
    this.version = ☃;
    this.l = true;
    this.i = ☃;
    types[☃] = this;
  }
  
  public String name()
  {
    return this.name;
  }
  
  public int getVersion()
  {
    return this.version;
  }
  
  public WorldType a(int ☃)
  {
    if ((this == NORMAL) && (☃ == 0)) {
      return NORMAL_1_1;
    }
    return this;
  }
  
  private WorldType a(boolean ☃)
  {
    this.l = ☃;
    return this;
  }
  
  private WorldType i()
  {
    this.m = true;
    return this;
  }
  
  public boolean f()
  {
    return this.m;
  }
  
  public static WorldType getType(String ☃)
  {
    for (int ☃ = 0; ☃ < types.length; ☃++) {
      if ((types[☃] != null) && (types[☃].name.equalsIgnoreCase(☃))) {
        return types[☃];
      }
    }
    return null;
  }
  
  public int g()
  {
    return this.i;
  }
  
  private WorldType j()
  {
    this.n = true;
    return this;
  }
}
