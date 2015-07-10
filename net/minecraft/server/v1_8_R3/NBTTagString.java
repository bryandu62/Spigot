package net.minecraft.server.v1_8_R3;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagString
  extends NBTBase
{
  private String data;
  
  public NBTTagString()
  {
    this.data = "";
  }
  
  public NBTTagString(String ☃)
  {
    this.data = ☃;
    if (☃ == null) {
      throw new IllegalArgumentException("Empty string not allowed");
    }
  }
  
  void write(DataOutput ☃)
    throws IOException
  {
    ☃.writeUTF(this.data);
  }
  
  void load(DataInput ☃, int ☃, NBTReadLimiter ☃)
    throws IOException
  {
    ☃.a(288L);
    
    this.data = ☃.readUTF();
    ☃.a(16 * this.data.length());
  }
  
  public byte getTypeId()
  {
    return 8;
  }
  
  public String toString()
  {
    return "\"" + this.data.replace("\"", "\\\"") + "\"";
  }
  
  public NBTBase clone()
  {
    return new NBTTagString(this.data);
  }
  
  public boolean isEmpty()
  {
    return this.data.isEmpty();
  }
  
  public boolean equals(Object ☃)
  {
    if (super.equals(☃))
    {
      NBTTagString ☃ = (NBTTagString)☃;
      return ((this.data == null) && (☃.data == null)) || ((this.data != null) && (this.data.equals(☃.data)));
    }
    return false;
  }
  
  public int hashCode()
  {
    return super.hashCode() ^ this.data.hashCode();
  }
  
  public String a_()
  {
    return this.data;
  }
}
