package net.minecraft.server.v1_8_R3;

public class MovingObjectPosition
{
  private BlockPosition e;
  public EnumMovingObjectType type;
  public EnumDirection direction;
  public Vec3D pos;
  public Entity entity;
  
  public MovingObjectPosition(Vec3D ☃, EnumDirection ☃, BlockPosition ☃)
  {
    this(EnumMovingObjectType.BLOCK, ☃, ☃, ☃);
  }
  
  public MovingObjectPosition(Vec3D ☃, EnumDirection ☃)
  {
    this(EnumMovingObjectType.BLOCK, ☃, ☃, BlockPosition.ZERO);
  }
  
  public MovingObjectPosition(Entity ☃)
  {
    this(☃, new Vec3D(☃.locX, ☃.locY, ☃.locZ));
  }
  
  public MovingObjectPosition(EnumMovingObjectType ☃, Vec3D ☃, EnumDirection ☃, BlockPosition ☃)
  {
    this.type = ☃;
    this.e = ☃;
    this.direction = ☃;
    this.pos = new Vec3D(☃.a, ☃.b, ☃.c);
  }
  
  public MovingObjectPosition(Entity ☃, Vec3D ☃)
  {
    this.type = EnumMovingObjectType.ENTITY;
    this.entity = ☃;
    this.pos = ☃;
  }
  
  public BlockPosition a()
  {
    return this.e;
  }
  
  public String toString()
  {
    return "HitResult{type=" + this.type + ", blockpos=" + this.e + ", f=" + this.direction + ", pos=" + this.pos + ", entity=" + this.entity + '}';
  }
  
  public static enum EnumMovingObjectType
  {
    private EnumMovingObjectType() {}
  }
}
