package org.bukkit.craftbukkit.v1_8_R3.block;

import net.minecraft.server.v1_8_R3.BlockJukeBox;
import net.minecraft.server.v1_8_R3.BlockJukeBox.TileEntityRecordPlayer;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.IBlockData;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Jukebox;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;

public class CraftJukebox
  extends CraftBlockState
  implements Jukebox
{
  private final CraftWorld world;
  private final BlockJukeBox.TileEntityRecordPlayer jukebox;
  
  public CraftJukebox(org.bukkit.block.Block block)
  {
    super(block);
    
    this.world = ((CraftWorld)block.getWorld());
    this.jukebox = ((BlockJukeBox.TileEntityRecordPlayer)this.world.getTileEntityAt(getX(), getY(), getZ()));
  }
  
  public CraftJukebox(Material material, BlockJukeBox.TileEntityRecordPlayer te)
  {
    super(material);
    this.world = null;
    this.jukebox = te;
  }
  
  public Material getPlaying()
  {
    ItemStack record = this.jukebox.getRecord();
    if (record == null) {
      return Material.AIR;
    }
    return CraftMagicNumbers.getMaterial(record.getItem());
  }
  
  public void setPlaying(Material record)
  {
    if ((record == null) || (CraftMagicNumbers.getItem(record) == null))
    {
      record = Material.AIR;
      this.jukebox.setRecord(null);
    }
    else
    {
      this.jukebox.setRecord(new ItemStack(CraftMagicNumbers.getItem(record), 1));
    }
    if (!isPlaced()) {
      return;
    }
    this.jukebox.update();
    if (record == Material.AIR) {
      this.world.getHandle().setTypeAndData(new BlockPosition(getX(), getY(), getZ()), 
        Blocks.JUKEBOX.getBlockData()
        .set(BlockJukeBox.HAS_RECORD, Boolean.valueOf(false)), 3);
    } else {
      this.world.getHandle().setTypeAndData(new BlockPosition(getX(), getY(), getZ()), 
        Blocks.JUKEBOX.getBlockData()
        .set(BlockJukeBox.HAS_RECORD, Boolean.valueOf(true)), 3);
    }
    this.world.playEffect(getLocation(), Effect.RECORD_PLAY, record.getId());
  }
  
  public boolean isPlaying()
  {
    return getRawData() == 1;
  }
  
  public boolean eject()
  {
    requirePlaced();
    boolean result = isPlaying();
    ((BlockJukeBox)Blocks.JUKEBOX).dropRecord(this.world.getHandle(), new BlockPosition(getX(), getY(), getZ()), null);
    return result;
  }
  
  public BlockJukeBox.TileEntityRecordPlayer getTileEntity()
  {
    return this.jukebox;
  }
}
