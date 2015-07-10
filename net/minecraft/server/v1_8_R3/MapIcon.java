package net.minecraft.server.v1_8_R3;

public class MapIcon
{
  private byte type;
  private byte x;
  private byte y;
  private byte rotation;
  
  public MapIcon(byte ☃, byte ☃, byte ☃, byte ☃)
  {
    this.type = ☃;
    this.x = ☃;
    this.y = ☃;
    this.rotation = ☃;
  }
  
  public MapIcon(MapIcon ☃)
  {
    this.type = ☃.type;
    this.x = ☃.x;
    this.y = ☃.y;
    this.rotation = ☃.rotation;
  }
  
  public byte getType()
  {
    return this.type;
  }
  
  public byte getX()
  {
    return this.x;
  }
  
  public byte getY()
  {
    return this.y;
  }
  
  public byte getRotation()
  {
    return this.rotation;
  }
  
  public boolean equals(Object ☃)
  {
    if (this == ☃) {
      return true;
    }
    if (!(☃ instanceof MapIcon)) {
      return false;
    }
    MapIcon ☃ = (MapIcon)☃;
    if (this.type != ☃.type) {
      return false;
    }
    if (this.rotation != ☃.rotation) {
      return false;
    }
    if (this.x != ☃.x) {
      return false;
    }
    if (this.y != ☃.y) {
      return false;
    }
    return true;
  }
  
  public int hashCode()
  {
    int ☃ = this.type;
    ☃ = 31 * ☃ + this.x;
    ☃ = 31 * ☃ + this.y;
    ☃ = 31 * ☃ + this.rotation;
    return ☃;
  }
}
