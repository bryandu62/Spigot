package net.minecraft.server.v1_8_R3;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagFloat
  extends NBTBase.NBTNumber
{
  private float data;
  
  NBTTagFloat() {}
  
  public NBTTagFloat(float ☃)
  {
    this.data = ☃;
  }
  
  void write(DataOutput ☃)
    throws IOException
  {
    ☃.writeFloat(this.data);
  }
  
  void load(DataInput ☃, int ☃, NBTReadLimiter ☃)
    throws IOException
  {
    ☃.a(96L);
    this.data = ☃.readFloat();
  }
  
  public byte getTypeId()
  {
    return 5;
  }
  
  public String toString()
  {
    return "" + this.data + "f";
  }
  
  public NBTBase clone()
  {
    return new NBTTagFloat(this.data);
  }
  
  public boolean equals(Object ☃)
  {
    if (super.equals(☃))
    {
      NBTTagFloat ☃ = (NBTTagFloat)☃;
      return this.data == ☃.data;
    }
    return false;
  }
  
  public int hashCode()
  {
    return super.hashCode() ^ Float.floatToIntBits(this.data);
  }
  
  public long c()
  {
    return this.data;
  }
  
  public int d()
  {
    return MathHelper.d(this.data);
  }
  
  public short e()
  {
    return (short)(MathHelper.d(this.data) & 0xFFFF);
  }
  
  public byte f()
  {
    return (byte)(MathHelper.d(this.data) & 0xFF);
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
