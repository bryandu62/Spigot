package net.minecraft.server.v1_8_R3;

public class BlockActionData
{
  private BlockPosition a;
  private Block b;
  private int c;
  private int d;
  
  public BlockActionData(BlockPosition ☃, Block ☃, int ☃, int ☃)
  {
    this.a = ☃;
    this.c = ☃;
    this.d = ☃;
    this.b = ☃;
  }
  
  public BlockPosition a()
  {
    return this.a;
  }
  
  public int b()
  {
    return this.c;
  }
  
  public int c()
  {
    return this.d;
  }
  
  public Block d()
  {
    return this.b;
  }
  
  public boolean equals(Object ☃)
  {
    if ((☃ instanceof BlockActionData))
    {
      BlockActionData ☃ = (BlockActionData)☃;
      return (this.a.equals(☃.a)) && (this.c == ☃.c) && (this.d == ☃.d) && (this.b == ☃.b);
    }
    return false;
  }
  
  public String toString()
  {
    return "TE(" + this.a + ")," + this.c + "," + this.d + "," + this.b;
  }
}
