package net.minecraft.server.v1_8_R3;

public class TileEntityMobSpawnerData
  extends WeightedRandom.WeightedRandomChoice
{
  private final NBTTagCompound c;
  private final String d;
  
  public TileEntityMobSpawnerData(MobSpawnerAbstract paramMobSpawnerAbstract, NBTTagCompound ☃)
  {
    this(paramMobSpawnerAbstract, ☃.getCompound("Properties"), ☃.getString("Type"), ☃.getInt("Weight"));
  }
  
  public TileEntityMobSpawnerData(MobSpawnerAbstract paramMobSpawnerAbstract, NBTTagCompound ☃, String ☃)
  {
    this(paramMobSpawnerAbstract, ☃, ☃, 1);
  }
  
  private TileEntityMobSpawnerData(MobSpawnerAbstract paramMobSpawnerAbstract, NBTTagCompound ☃, String ☃, int ☃)
  {
    super(☃);
    if (☃.equals("Minecart")) {
      if (☃ != null) {
        ☃ = EntityMinecartAbstract.EnumMinecartType.a(☃.getInt("Type")).b();
      } else {
        ☃ = "MinecartRideable";
      }
    }
    this.c = ☃;
    this.d = ☃;
  }
  
  public NBTTagCompound a()
  {
    NBTTagCompound ☃ = new NBTTagCompound();
    
    ☃.set("Properties", this.c);
    ☃.setString("Type", this.d);
    ☃.setInt("Weight", this.a);
    
    return ☃;
  }
}
