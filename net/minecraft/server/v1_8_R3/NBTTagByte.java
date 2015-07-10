package net.minecraft.server.v1_8_R3;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagByte
  extends NBTBase.NBTNumber
{
  private byte data;
  
  NBTTagByte() {}
  
  public NBTTagByte(byte ☃)
  {
    this.data = ☃;
  }
  
  void write(DataOutput ☃)
    throws IOException
  {
    ☃.writeByte(this.data);
  }
  
  void load(DataInput ☃, int ☃, NBTReadLimiter ☃)
    throws IOException
  {
    ☃.a(72L);
    this.data = ☃.readByte();
  }
  
  public byte getTypeId()
  {
    return 1;
  }
  
  public String toString()
  {
    return "" + this.data + "b";
  }
  
  public NBTBase clone()
  {
    return new NBTTagByte(this.data);
  }
  
  public boolean equals(Object ☃)
  {
    if (super.equals(☃))
    {
      NBTTagByte ☃ = (NBTTagByte)☃;
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
    return (short)this.data;
  }
  
  public byte f()
  {
    return this.data;
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
