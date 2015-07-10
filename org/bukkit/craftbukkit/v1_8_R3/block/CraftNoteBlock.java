package org.bukkit.craftbukkit.v1_8_R3.block;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.TileEntityNote;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.block.Block;
import org.bukkit.block.NoteBlock;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;

public class CraftNoteBlock
  extends CraftBlockState
  implements NoteBlock
{
  private final CraftWorld world;
  private final TileEntityNote note;
  
  public CraftNoteBlock(Block block)
  {
    super(block);
    
    this.world = ((CraftWorld)block.getWorld());
    this.note = ((TileEntityNote)this.world.getTileEntityAt(getX(), getY(), getZ()));
  }
  
  public CraftNoteBlock(Material material, TileEntityNote te)
  {
    super(material);
    this.world = null;
    this.note = te;
  }
  
  public Note getNote()
  {
    return new Note(this.note.note);
  }
  
  public byte getRawNote()
  {
    return this.note.note;
  }
  
  public void setNote(Note n)
  {
    this.note.note = n.getId();
  }
  
  public void setRawNote(byte n)
  {
    this.note.note = n;
  }
  
  public boolean play()
  {
    Block block = getBlock();
    if (block.getType() == Material.NOTE_BLOCK)
    {
      this.note.play(this.world.getHandle(), new BlockPosition(getX(), getY(), getZ()));
      return true;
    }
    return false;
  }
  
  public boolean play(byte instrument, byte note)
  {
    Block block = getBlock();
    if (block.getType() == Material.NOTE_BLOCK)
    {
      this.world.getHandle().playBlockAction(new BlockPosition(getX(), getY(), getZ()), CraftMagicNumbers.getBlock(block), instrument, note);
      return true;
    }
    return false;
  }
  
  public boolean play(Instrument instrument, Note note)
  {
    Block block = getBlock();
    if (block.getType() == Material.NOTE_BLOCK)
    {
      this.world.getHandle().playBlockAction(new BlockPosition(getX(), getY(), getZ()), CraftMagicNumbers.getBlock(block), instrument.getType(), note.getId());
      return true;
    }
    return false;
  }
  
  public TileEntityNote getTileEntity()
  {
    return this.note;
  }
}
