package net.minecraft.server.v1_8_R3;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagInt
  extends NBTBase.NBTNumber
{
  private int data;
  
  NBTTagInt() {}
  
  public NBTTagInt(int ☃)
  {
    this.data = ☃;
  }
  
  void write(DataOutput ☃)
    throws IOException
  {
    ☃.writeInt(this.data);
  }
  
  void load(DataInput ☃, int ☃, NBTReadLimiter ☃)
    throws IOException
  {
    ☃.a(96L);
    this.data = ☃.readInt();
  }
  
  public byte getTypeId()
  {
    return 3;
  }
  
  public String toString()
  {
    return "" + this.data;
  }
  
  public NBTBase clone()
  {
    return new NBTTagInt(this.data);
  }
  
  public boolean equals(Object ☃)
  {
    if (super.equals(☃))
    {
      NBTTagInt ☃ = (NBTTagInt)☃;
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
    return (short)(this.data & 0xFFFF);
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
