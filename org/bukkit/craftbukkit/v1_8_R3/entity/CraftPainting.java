package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityPainting;
import net.minecraft.server.v1_8_R3.EntityPainting.EnumArt;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Art;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.CraftArt;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Painting;

public class CraftPainting
  extends CraftHanging
  implements Painting
{
  public CraftPainting(CraftServer server, EntityPainting entity)
  {
    super(server, entity);
  }
  
  public Art getArt()
  {
    EntityPainting.EnumArt art = getHandle().art;
    return CraftArt.NotchToBukkit(art);
  }
  
  public boolean setArt(Art art)
  {
    return setArt(art, false);
  }
  
  public boolean setArt(Art art, boolean force)
  {
    EntityPainting painting = getHandle();
    EntityPainting.EnumArt oldArt = painting.art;
    painting.art = CraftArt.BukkitToNotch(art);
    painting.setDirection(painting.direction);
    if ((!force) && (!painting.survives()))
    {
      painting.art = oldArt;
      painting.setDirection(painting.direction);
      return false;
    }
    update();
    return true;
  }
  
  public boolean setFacingDirection(BlockFace face, boolean force)
  {
    if (super.setFacingDirection(face, force))
    {
      update();
      return true;
    }
    return false;
  }
  
  private void update()
  {
    WorldServer world = ((CraftWorld)getWorld()).getHandle();
    EntityPainting painting = new EntityPainting(world);
    painting.blockPosition = getHandle().blockPosition;
    painting.art = getHandle().art;
    painting.setDirection(getHandle().direction);
    getHandle().die();
    getHandle().velocityChanged = true;
    world.addEntity(painting);
    this.entity = painting;
  }
  
  public EntityPainting getHandle()
  {
    return (EntityPainting)this.entity;
  }
  
  public String toString()
  {
    return "CraftPainting{art=" + getArt() + "}";
  }
  
  public EntityType getType()
  {
    return EntityType.PAINTING;
  }
}
