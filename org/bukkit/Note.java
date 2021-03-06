package org.bukkit;

import com.google.common.collect.Maps;
import java.util.Map;
import org.apache.commons.lang.Validate;

public class Note
{
  private final byte note;
  
  public static enum Tone
  {
    G(1, true),  A(3, true),  B(5, false),  C(6, true),  D(8, true),  E(10, false),  F(11, true);
    
    private final boolean sharpable;
    private final byte id;
    private static final Map<Byte, Tone> BY_DATA;
    public static final byte TONES_COUNT = 12;
    
    private Tone(int id, boolean sharpable)
    {
      this.id = ((byte)(id % 12));
      this.sharpable = sharpable;
    }
    
    @Deprecated
    public byte getId()
    {
      return getId(false);
    }
    
    @Deprecated
    public byte getId(boolean sharped)
    {
      byte id = (byte)((sharped) && (this.sharpable) ? this.id + 1 : this.id);
      
      return (byte)(id % 12);
    }
    
    public boolean isSharpable()
    {
      return this.sharpable;
    }
    
    @Deprecated
    public boolean isSharped(byte id)
    {
      if (id == getId(false)) {
        return false;
      }
      if (id == getId(true)) {
        return true;
      }
      throw new IllegalArgumentException("The id isn't matching to the tone.");
    }
    
    @Deprecated
    public static Tone getById(byte id)
    {
      return (Tone)BY_DATA.get(Byte.valueOf(id));
    }
    
    static
    {
      BY_DATA = Maps.newHashMap();
      Tone[] arrayOfTone;
      int i = (arrayOfTone = values()).length;
      for (int j = 0; j < i; j++)
      {
        Tone tone = arrayOfTone[j];
        int id = tone.id % 12;
        BY_DATA.put(Byte.valueOf((byte)id), tone);
        if (tone.isSharpable())
        {
          id = (id + 1) % 12;
          BY_DATA.put(Byte.valueOf((byte)id), tone);
        }
      }
    }
  }
  
  public Note(int note)
  {
    Validate.isTrue((note >= 0) && (note <= 24), "The note value has to be between 0 and 24.");
    
    this.note = ((byte)note);
  }
  
  public Note(int octave, Tone tone, boolean sharped)
  {
    if ((sharped) && (!tone.isSharpable()))
    {
      tone = Tone.values()[(tone.ordinal() + 1)];
      sharped = false;
    }
    if ((octave < 0) || (octave > 2) || ((octave == 2) && ((tone != Tone.F) || (!sharped)))) {
      throw new IllegalArgumentException("Tone and octave have to be between F#0 and F#2");
    }
    this.note = ((byte)(octave * 12 + tone.getId(sharped)));
  }
  
  public static Note flat(int octave, Tone tone)
  {
    Validate.isTrue(octave != 2, "Octave cannot be 2 for flats");
    tone = tone == Tone.G ? Tone.F : Tone.values()[(tone.ordinal() - 1)];
    return new Note(octave, tone, tone.isSharpable());
  }
  
  public static Note sharp(int octave, Tone tone)
  {
    return new Note(octave, tone, true);
  }
  
  public static Note natural(int octave, Tone tone)
  {
    Validate.isTrue(octave != 2, "Octave cannot be 2 for naturals");
    return new Note(octave, tone, false);
  }
  
  public Note sharped()
  {
    Validate.isTrue(this.note < 24, "This note cannot be sharped because it is the highest known note!");
    return new Note(this.note + 1);
  }
  
  public Note flattened()
  {
    Validate.isTrue(this.note > 0, "This note cannot be flattened because it is the lowest known note!");
    return new Note(this.note - 1);
  }
  
  @Deprecated
  public byte getId()
  {
    return this.note;
  }
  
  public int getOctave()
  {
    return this.note / 12;
  }
  
  private byte getToneByte()
  {
    return (byte)(this.note % 12);
  }
  
  public Tone getTone()
  {
    return Tone.getById(getToneByte());
  }
  
  public boolean isSharped()
  {
    byte note = getToneByte();
    return Tone.getById(note).isSharped(note);
  }
  
  public int hashCode()
  {
    int result = 1;
    result = 31 * result + this.note;
    return result;
  }
  
  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Note other = (Note)obj;
    if (this.note != other.note) {
      return false;
    }
    return true;
  }
  
  public String toString()
  {
    return "Note{" + getTone().toString() + (isSharped() ? "#" : "") + "}";
  }
}
