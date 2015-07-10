package net.minecraft.server.v1_8_R3;

public class TileEntityMobSpawner
  extends TileEntity
  implements IUpdatePlayerListBox
{
  private final MobSpawnerAbstract a = new MobSpawnerAbstract()
  {
    public void a(int ☃)
    {
      TileEntityMobSpawner.this.world.playBlockAction(TileEntityMobSpawner.this.position, Blocks.MOB_SPAWNER, ☃, 0);
    }
    
    public World a()
    {
      return TileEntityMobSpawner.this.world;
    }
    
    public BlockPosition b()
    {
      return TileEntityMobSpawner.this.position;
    }
    
    public void a(TileEntityMobSpawnerData ☃)
    {
      super.a(☃);
      if (a() != null) {
        a().notify(TileEntityMobSpawner.this.position);
      }
    }
  };
  
  public void a(NBTTagCompound ☃)
  {
    super.a(☃);
    this.a.a(☃);
  }
  
  public void b(NBTTagCompound ☃)
  {
    super.b(☃);
    this.a.b(☃);
  }
  
  public void c()
  {
    this.a.c();
  }
  
  public Packet getUpdatePacket()
  {
    NBTTagCompound ☃ = new NBTTagCompound();
    b(☃);
    ☃.remove("SpawnPotentials");
    return new PacketPlayOutTileEntityData(this.position, 1, ☃);
  }
  
  public boolean c(int ☃, int ☃)
  {
    if (this.a.b(☃)) {
      return true;
    }
    return super.c(☃, ☃);
  }
  
  public boolean F()
  {
    return true;
  }
  
  public MobSpawnerAbstract getSpawner()
  {
    return this.a;
  }
}
