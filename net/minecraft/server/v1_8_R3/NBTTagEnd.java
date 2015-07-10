package net.minecraft.server.v1_8_R3;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagEnd
  extends NBTBase
{
  void load(DataInput ☃, int ☃, NBTReadLimiter ☃)
    throws IOException
  {
    ☃.a(64L);
  }
  
  void write(DataOutput ☃)
    throws IOException
  {}
  
  public byte getTypeId()
  {
    return 0;
  }
  
  public String toString()
  {
    return "END";
  }
  
  public NBTBase clone()
  {
    return new NBTTagEnd();
  }
}
