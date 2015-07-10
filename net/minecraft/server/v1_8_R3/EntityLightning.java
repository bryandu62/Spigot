package net.minecraft.server.v1_8_R3;

import java.util.List;
import java.util.Random;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.event.block.BlockIgniteEvent;

public class EntityLightning
  extends EntityWeather
{
  private int lifeTicks;
  public long a;
  private int c;
  public boolean isEffect = false;
  public boolean isSilent = false;
  
  public EntityLightning(World world, double d0, double d1, double d2)
  {
    this(world, d0, d1, d2, false);
  }
  
  public EntityLightning(World world, double d0, double d1, double d2, boolean isEffect)
  {
    super(world);
    
    this.isEffect = isEffect;
    
    setPositionRotation(d0, d1, d2, 0.0F, 0.0F);
    this.lifeTicks = 2;
    this.a = this.random.nextLong();
    this.c = (this.random.nextInt(3) + 1);
    BlockPosition blockposition = new BlockPosition(this);
    if ((!isEffect) && (!world.isClientSide) && (world.getGameRules().getBoolean("doFireTick")) && ((world.getDifficulty() == EnumDifficulty.NORMAL) || (world.getDifficulty() == EnumDifficulty.HARD)) && (world.areChunksLoaded(blockposition, 10)))
    {
      if ((world.getType(blockposition).getBlock().getMaterial() == Material.AIR) && (Blocks.FIRE.canPlace(world, blockposition))) {
        if (!CraftEventFactory.callBlockIgniteEvent(world, blockposition.getX(), blockposition.getY(), blockposition.getZ(), this).isCancelled()) {
          world.setTypeUpdate(blockposition, Blocks.FIRE.getBlockData());
        }
      }
      for (int i = 0; i < 4; i++)
      {
        BlockPosition blockposition1 = blockposition.a(this.random.nextInt(3) - 1, this.random.nextInt(3) - 1, this.random.nextInt(3) - 1);
        if ((world.getType(blockposition1).getBlock().getMaterial() == Material.AIR) && (Blocks.FIRE.canPlace(world, blockposition1))) {
          if (!CraftEventFactory.callBlockIgniteEvent(world, blockposition1.getX(), blockposition1.getY(), blockposition1.getZ(), this).isCancelled()) {
            world.setTypeUpdate(blockposition1, Blocks.FIRE.getBlockData());
          }
        }
      }
    }
  }
  
  public EntityLightning(World world, double d0, double d1, double d2, boolean isEffect, boolean isSilent)
  {
    this(world, d0, d1, d2, isEffect);
    this.isSilent = isSilent;
  }
  
  public void t_()
  {
    super.t_();
    if ((!this.isSilent) && (this.lifeTicks == 2))
    {
      float pitch = 0.8F + this.random.nextFloat() * 0.2F;
      int viewDistance = ((WorldServer)this.world).getServer().getViewDistance() * 16;
      for (EntityPlayer player : this.world.players)
      {
        double deltaX = this.locX - player.locX;
        double deltaZ = this.locZ - player.locZ;
        double distanceSquared = deltaX * deltaX + deltaZ * deltaZ;
        if (distanceSquared > viewDistance * viewDistance)
        {
          double deltaLength = Math.sqrt(distanceSquared);
          double relativeX = player.locX + deltaX / deltaLength * viewDistance;
          double relativeZ = player.locZ + deltaZ / deltaLength * viewDistance;
          player.playerConnection.sendPacket(new PacketPlayOutNamedSoundEffect("ambient.weather.thunder", relativeX, this.locY, relativeZ, 10000.0F, pitch));
        }
        else
        {
          player.playerConnection.sendPacket(new PacketPlayOutNamedSoundEffect("ambient.weather.thunder", this.locX, this.locY, this.locZ, 10000.0F, pitch));
        }
      }
      this.world.makeSound(this.locX, this.locY, this.locZ, "random.explode", 2.0F, 0.5F + this.random.nextFloat() * 0.2F);
    }
    this.lifeTicks -= 1;
    if (this.lifeTicks < 0) {
      if (this.c == 0)
      {
        die();
      }
      else if (this.lifeTicks < -this.random.nextInt(10))
      {
        this.c -= 1;
        this.lifeTicks = 1;
        this.a = this.random.nextLong();
        BlockPosition blockposition = new BlockPosition(this);
        if ((!this.world.isClientSide) && (this.world.getGameRules().getBoolean("doFireTick")) && (this.world.areChunksLoaded(blockposition, 10)) && (this.world.getType(blockposition).getBlock().getMaterial() == Material.AIR) && (Blocks.FIRE.canPlace(this.world, blockposition))) {
          if ((!this.isEffect) && (!CraftEventFactory.callBlockIgniteEvent(this.world, blockposition.getX(), blockposition.getY(), blockposition.getZ(), this).isCancelled())) {
            this.world.setTypeUpdate(blockposition, Blocks.FIRE.getBlockData());
          }
        }
      }
    }
    if ((this.lifeTicks >= 0) && (!this.isEffect)) {
      if (this.world.isClientSide)
      {
        this.world.d(2);
      }
      else
      {
        double d0 = 3.0D;
        List list = this.world.getEntities(this, new AxisAlignedBB(this.locX - d0, this.locY - d0, this.locZ - d0, this.locX + d0, this.locY + 6.0D + d0, this.locZ + d0));
        for (int i = 0; i < list.size(); i++)
        {
          Entity entity = (Entity)list.get(i);
          
          entity.onLightningStrike(this);
        }
      }
    }
  }
  
  protected void h() {}
  
  protected void a(NBTTagCompound nbttagcompound) {}
  
  protected void b(NBTTagCompound nbttagcompound) {}
}
