package net.minecraft.server.v1_8_R3;

public class SourceBlock
  implements ISourceBlock
{
  private final World a;
  private final BlockPosition b;
  
  public SourceBlock(World ☃, BlockPosition ☃)
  {
    this.a = ☃;
    this.b = ☃;
  }
  
  public World i()
  {
    return this.a;
  }
  
  public double getX()
  {
    return this.b.getX() + 0.5D;
  }
  
  public double getY()
  {
    return this.b.getY() + 0.5D;
  }
  
  public double getZ()
  {
    return this.b.getZ() + 0.5D;
  }
  
  public BlockPosition getBlockPosition()
  {
    return this.b;
  }
  
  public int f()
  {
    IBlockData ☃ = this.a.getType(this.b);
    return ☃.getBlock().toLegacyData(☃);
  }
  
  public <T extends TileEntity> T getTileEntity()
  {
    return this.a.getTileEntity(this.b);
  }
}
