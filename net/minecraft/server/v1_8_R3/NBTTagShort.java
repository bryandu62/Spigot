package net.minecraft.server.v1_8_R3;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagShort
  extends NBTBase.NBTNumber
{
  private short data;
  
  public NBTTagShort() {}
  
  public NBTTagShort(short ☃)
  {
    this.data = ☃;
  }
  
  void write(DataOutput ☃)
    throws IOException
  {
    ☃.writeShort(this.data);
  }
  
  void load(DataInput ☃, int ☃, NBTReadLimiter ☃)
    throws IOException
  {
    ☃.a(80L);
    this.data = ☃.readShort();
  }
  
  public byte getTypeId()
  {
    return 2;
  }
  
  public String toString()
  {
    return "" + this.data + "s";
  }
  
  public NBTBase clone()
  {
    return new NBTTagShort(this.data);
  }
  
  public boolean equals(Object ☃)
  {
    if (super.equals(☃))
    {
      NBTTagShort ☃ = (NBTTagShort)☃;
      return this.data == ☃.data;
    }
    return false;
  }
  
  public int hashCode()
  {
    return super.hashCode() ^ this.data;
  }
  
  public long c()
  {
    return this.data;
  }
  
  public int d()
  {
    return this.data;
  }
  
  public short e()
  {
    return this.data;
  }
  
  public byte f()
  {
    return (byte)(this.data & 0xFF);
  }
  
  public double g()
  {
    return this.data;
  }
  
  public float h()
  {
    return this.data;
  }
}
