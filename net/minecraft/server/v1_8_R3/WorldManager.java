package net.minecraft.server.v1_8_R3;

import java.util.Iterator;
import java.util.List;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;

public class WorldManager
  implements IWorldAccess
{
  private MinecraftServer a;
  private WorldServer world;
  
  public WorldManager(MinecraftServer minecraftserver, WorldServer worldserver)
  {
    this.a = minecraftserver;
    this.world = worldserver;
  }
  
  public void a(int i, boolean flag, double d0, double d1, double d2, double d3, double d4, double d5, int... aint) {}
  
  public void a(Entity entity)
  {
    this.world.getTracker().track(entity);
  }
  
  public void b(Entity entity)
  {
    this.world.getTracker().untrackEntity(entity);
    this.world.getScoreboard().a(entity);
  }
  
  public void a(String s, double d0, double d1, double d2, float f, float f1)
  {
    this.a.getPlayerList().sendPacketNearby(d0, d1, d2, f > 1.0F ? 16.0F * f : 16.0D, this.world.dimension, new PacketPlayOutNamedSoundEffect(s, d0, d1, d2, f, f1));
  }
  
  public void a(EntityHuman entityhuman, String s, double d0, double d1, double d2, float f, float f1)
  {
    this.a.getPlayerList().sendPacketNearby(entityhuman, d0, d1, d2, f > 1.0F ? 16.0F * f : 16.0D, this.world.dimension, new PacketPlayOutNamedSoundEffect(s, d0, d1, d2, f, f1));
  }
  
  public void a(int i, int j, int k, int l, int i1, int j1) {}
  
  public void a(BlockPosition blockposition)
  {
    this.world.getPlayerChunkMap().flagDirty(blockposition);
  }
  
  public void b(BlockPosition blockposition) {}
  
  public void a(String s, BlockPosition blockposition) {}
  
  public void a(EntityHuman entityhuman, int i, BlockPosition blockposition, int j)
  {
    this.a.getPlayerList().sendPacketNearby(entityhuman, blockposition.getX(), blockposition.getY(), blockposition.getZ(), 64.0D, this.world.dimension, new PacketPlayOutWorldEvent(i, blockposition, j, false));
  }
  
  public void a(int i, BlockPosition blockposition, int j)
  {
    this.a.getPlayerList().sendAll(new PacketPlayOutWorldEvent(i, blockposition, j, true));
  }
  
  public void b(int i, BlockPosition blockposition, int j)
  {
    Iterator iterator = this.a.getPlayerList().v().iterator();
    
    EntityHuman entityhuman = null;
    Entity entity = this.world.a(i);
    if ((entity instanceof EntityHuman)) {
      entityhuman = (EntityHuman)entity;
    }
    while (iterator.hasNext())
    {
      EntityPlayer entityplayer = (EntityPlayer)iterator.next();
      if ((entityplayer != null) && (entityplayer.world == this.world) && (entityplayer.getId() != i))
      {
        double d0 = blockposition.getX() - entityplayer.locX;
        double d1 = blockposition.getY() - entityplayer.locY;
        double d2 = blockposition.getZ() - entityplayer.locZ;
        if ((entityhuman == null) || (!(entityhuman instanceof EntityPlayer)) || (entityplayer.getBukkitEntity().canSee(((EntityPlayer)entityhuman).getBukkitEntity()))) {
          if (d0 * d0 + d1 * d1 + d2 * d2 < 1024.0D) {
            entityplayer.playerConnection.sendPacket(new PacketPlayOutBlockBreakAnimation(i, blockposition, j));
          }
        }
      }
    }
  }
}
