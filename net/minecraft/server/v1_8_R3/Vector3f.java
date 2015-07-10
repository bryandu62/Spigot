package net.minecraft.server.v1_8_R3;

public class Vector3f
{
  protected final float x;
  protected final float y;
  protected final float z;
  
  public Vector3f(float ☃, float ☃, float ☃)
  {
    this.x = ☃;
    this.y = ☃;
    this.z = ☃;
  }
  
  public Vector3f(NBTTagList ☃)
  {
    this.x = ☃.e(0);
    this.y = ☃.e(1);
    this.z = ☃.e(2);
  }
  
  public NBTTagList a()
  {
    NBTTagList ☃ = new NBTTagList();
    ☃.add(new NBTTagFloat(this.x));
    ☃.add(new NBTTagFloat(this.y));
    ☃.add(new NBTTagFloat(this.z));
    return ☃;
  }
  
  public boolean equals(Object ☃)
  {
    if (!(☃ instanceof Vector3f)) {
      return false;
    }
    Vector3f ☃ = (Vector3f)☃;
    return (this.x == ☃.x) && (this.y == ☃.y) && (this.z == ☃.z);
  }
  
  public float getX()
  {
    return this.x;
  }
  
  public float getY()
  {
    return this.y;
  }
  
  public float getZ()
  {
    return this.z;
  }
}
