package net.minecraft.server.v1_8_R3;

public class EntityMinecartMobSpawner
  extends EntityMinecartAbstract
{
  private final MobSpawnerAbstract a = new MobSpawnerAbstract()
  {
    public void a(int ☃)
    {
      EntityMinecartMobSpawner.this.world.broadcastEntityEffect(EntityMinecartMobSpawner.this, (byte)☃);
    }
    
    public World a()
    {
      return EntityMinecartMobSpawner.this.world;
    }
    
    public BlockPosition b()
    {
      return new BlockPosition(EntityMinecartMobSpawner.this);
    }
  };
  
  public EntityMinecartMobSpawner(World ☃)
  {
    super(☃);
  }
  
  public EntityMinecartMobSpawner(World ☃, double ☃, double ☃, double ☃)
  {
    super(☃, ☃, ☃, ☃);
  }
  
  public EntityMinecartAbstract.EnumMinecartType s()
  {
    return EntityMinecartAbstract.EnumMinecartType.SPAWNER;
  }
  
  public IBlockData u()
  {
    return Blocks.MOB_SPAWNER.getBlockData();
  }
  
  protected void a(NBTTagCompound ☃)
  {
    super.a(☃);
    this.a.a(☃);
  }
  
  protected void b(NBTTagCompound ☃)
  {
    super.b(☃);
    this.a.b(☃);
  }
  
  public void t_()
  {
    super.t_();
    this.a.c();
  }
}
