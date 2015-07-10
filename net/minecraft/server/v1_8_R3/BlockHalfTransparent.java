package net.minecraft.server.v1_8_R3;

public class BlockHalfTransparent
  extends Block
{
  private boolean a;
  
  protected BlockHalfTransparent(Material ☃, boolean ☃)
  {
    this(☃, ☃, ☃.r());
  }
  
  protected BlockHalfTransparent(Material ☃, boolean ☃, MaterialMapColor ☃)
  {
    super(☃, ☃);
    this.a = ☃;
  }
  
  public boolean c()
  {
    return false;
  }
}
