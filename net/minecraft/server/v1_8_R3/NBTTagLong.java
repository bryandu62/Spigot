package net.minecraft.server.v1_8_R3;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagLong
  extends NBTBase.NBTNumber
{
  private long data;
  
  NBTTagLong() {}
  
  public NBTTagLong(long ☃)
  {
    this.data = ☃;
  }
  
  void write(DataOutput ☃)
    throws IOException
  {
    ☃.writeLong(this.data);
  }
  
  void load(DataInput ☃, int ☃, NBTReadLimiter ☃)
    throws IOException
  {
    ☃.a(128L);
    this.data = ☃.readLong();
  }
  
  public byte getTypeId()
  {
    return 4;
  }
  
  public String toString()
  {
    return "" + this.data + "L";
  }
  
  public NBTBase clone()
  {
    return new NBTTagLong(this.data);
  }
  
  public boolean equals(Object ☃)
  {
    if (super.equals(☃))
    {
      NBTTagLong ☃ = (NBTTagLong)☃;
      return this.data == ☃.data;
    }
    return false;
  }
  
  public int hashCode()
  {
    return super.hashCode() ^ (int)(this.data ^ this.data >>> 32);
  }
  
  public long c()
  {
    return this.data;
  }
  
  public int d()
  {
    return (int)(this.data & 0xFFFFFFFFFFFFFFFF);
  }
  
  public short e()
  {
    return (short)(int)(this.data & 0xFFFF);
  }
  
  public byte f()
  {
    return (byte)(int)(this.data & 0xFF);
  }
  
  public double g()
  {
    return this.data;
  }
  
  public float h()
  {
    return (float)this.data;
  }
}
