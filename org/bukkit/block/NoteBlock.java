package org.bukkit.block;

import org.bukkit.Instrument;
import org.bukkit.Note;

public abstract interface NoteBlock
  extends BlockState
{
  public abstract Note getNote();
  
  @Deprecated
  public abstract byte getRawNote();
  
  public abstract void setNote(Note paramNote);
  
  @Deprecated
  public abstract void setRawNote(byte paramByte);
  
  public abstract boolean play();
  
  @Deprecated
  public abstract boolean play(byte paramByte1, byte paramByte2);
  
  public abstract boolean play(Instrument paramInstrument, Note paramNote);
}
