package net.minecraft.server.v1_8_R3;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagDouble
  extends NBTBase.NBTNumber
{
  private double data;
  
  NBTTagDouble() {}
  
  public NBTTagDouble(double ☃)
  {
    this.data = ☃;
  }
  
  void write(DataOutput ☃)
    throws IOException
  {
    ☃.writeDouble(this.data);
  }
  
  void load(DataInput ☃, int ☃, NBTReadLimiter ☃)
    throws IOException
  {
    ☃.a(128L);
    this.data = ☃.readDouble();
  }
  
  public byte getTypeId()
  {
    return 6;
  }
  
  public String toString()
  {
    return "" + this.data + "d";
  }
  
  public NBTBase clone()
  {
    return new NBTTagDouble(this.data);
  }
  
  public boolean equals(Object ☃)
  {
    if (super.equals(☃))
    {
      NBTTagDouble ☃ = (NBTTagDouble)☃;
      return this.data == ☃.data;
    }
    return false;
  }
  
  public int hashCode()
  {
    long ☃ = Double.doubleToLongBits(this.data);
    return super.hashCode() ^ (int)(☃ ^ ☃ >>> 32);
  }
  
  public long c()
  {
    return Math.floor(this.data);
  }
  
  public int d()
  {
    return MathHelper.floor(this.data);
  }
  
  public short e()
  {
    return (short)(MathHelper.floor(this.data) & 0xFFFF);
  }
  
  public byte f()
  {
    return (byte)(MathHelper.floor(this.data) & 0xFF);
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
