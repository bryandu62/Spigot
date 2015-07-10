package net.minecraft.server.v1_8_R3;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftMinecartCommand;

public class EntityMinecartCommandBlock
  extends EntityMinecartAbstract
{
  private final CommandBlockListenerAbstract a = new CommandBlockListenerAbstract()
  {
    public void h()
    {
      EntityMinecartCommandBlock.this.getDataWatcher().watch(23, getCommand());
      EntityMinecartCommandBlock.this.getDataWatcher().watch(24, IChatBaseComponent.ChatSerializer.a(k()));
    }
    
    public BlockPosition getChunkCoordinates()
    {
      return new BlockPosition(EntityMinecartCommandBlock.this.locX, EntityMinecartCommandBlock.this.locY + 0.5D, EntityMinecartCommandBlock.this.locZ);
    }
    
    public Vec3D d()
    {
      return new Vec3D(EntityMinecartCommandBlock.this.locX, EntityMinecartCommandBlock.this.locY, EntityMinecartCommandBlock.this.locZ);
    }
    
    public World getWorld()
    {
      return EntityMinecartCommandBlock.this.world;
    }
    
    public Entity f()
    {
      return EntityMinecartCommandBlock.this;
    }
  };
  private int b = 0;
  
  public EntityMinecartCommandBlock(World world)
  {
    super(world);
  }
  
  public EntityMinecartCommandBlock(World world, double d0, double d1, double d2)
  {
    super(world, d0, d1, d2);
  }
  
  protected void h()
  {
    super.h();
    getDataWatcher().a(23, "");
    getDataWatcher().a(24, "");
  }
  
  protected void a(NBTTagCompound nbttagcompound)
  {
    super.a(nbttagcompound);
    this.a.b(nbttagcompound);
    getDataWatcher().watch(23, getCommandBlock().getCommand());
    getDataWatcher().watch(24, IChatBaseComponent.ChatSerializer.a(getCommandBlock().k()));
  }
  
  protected void b(NBTTagCompound nbttagcompound)
  {
    super.b(nbttagcompound);
    this.a.a(nbttagcompound);
  }
  
  public EntityMinecartAbstract.EnumMinecartType s()
  {
    return EntityMinecartAbstract.EnumMinecartType.COMMAND_BLOCK;
  }
  
  public IBlockData u()
  {
    return Blocks.COMMAND_BLOCK.getBlockData();
  }
  
  public CommandBlockListenerAbstract getCommandBlock()
  {
    return this.a;
  }
  
  public void a(int i, int j, int k, boolean flag)
  {
    if ((flag) && (this.ticksLived - this.b >= 4))
    {
      getCommandBlock().a(this.world);
      this.b = this.ticksLived;
    }
  }
  
  public boolean e(EntityHuman entityhuman)
  {
    this.a.a(entityhuman);
    return false;
  }
  
  public void i(int i)
  {
    super.i(i);
    if (i == 24) {
      try
      {
        this.a.b(IChatBaseComponent.ChatSerializer.a(getDataWatcher().getString(24)));
      }
      catch (Throwable localThrowable) {}
    } else if (i == 23) {
      this.a.setCommand(getDataWatcher().getString(23));
    }
  }
}