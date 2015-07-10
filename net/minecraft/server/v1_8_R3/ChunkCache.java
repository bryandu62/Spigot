package net.minecraft.server.v1_8_R3;

public class ChunkCache
  implements IBlockAccess
{
  protected int a;
  protected int b;
  protected Chunk[][] c;
  protected boolean d;
  protected World e;
  
  public ChunkCache(World ☃, BlockPosition ☃, BlockPosition ☃, int ☃)
  {
    this.e = ☃;
    
    this.a = (☃.getX() - ☃ >> 4);
    this.b = (☃.getZ() - ☃ >> 4);
    int ☃ = ☃.getX() + ☃ >> 4;
    int ☃ = ☃.getZ() + ☃ >> 4;
    
    this.c = new Chunk[☃ - this.a + 1][☃ - this.b + 1];
    
    this.d = true;
    for (int ☃ = this.a; ☃ <= ☃; ☃++) {
      for (int ☃ = this.b; ☃ <= ☃; ☃++) {
        this.c[(☃ - this.a)][(☃ - this.b)] = ☃.getChunkAt(☃, ☃);
      }
    }
    for (int ☃ = ☃.getX() >> 4; ☃ <= ☃.getX() >> 4; ☃++) {
      for (int ☃ = ☃.getZ() >> 4; ☃ <= ☃.getZ() >> 4; ☃++)
      {
        Chunk ☃ = this.c[(☃ - this.a)][(☃ - this.b)];
        if ((☃ != null) && 
          (!☃.c(☃.getY(), ☃.getY()))) {
          this.d = false;
        }
      }
    }
  }
  
  public TileEntity getTileEntity(BlockPosition ☃)
  {
    int ☃ = (☃.getX() >> 4) - this.a;
    int ☃ = (☃.getZ() >> 4) - this.b;
    
    return this.c[☃][☃].a(☃, Chunk.EnumTileEntityState.IMMEDIATE);
  }
  
  public IBlockData getType(BlockPosition ☃)
  {
    if ((☃.getY() >= 0) && 
      (☃.getY() < 256))
    {
      int ☃ = (☃.getX() >> 4) - this.a;
      int ☃ = (☃.getZ() >> 4) - this.b;
      if ((☃ >= 0) && (☃ < this.c.length) && (☃ >= 0) && (☃ < this.c[☃].length))
      {
        Chunk ☃ = this.c[☃][☃];
        if (☃ != null) {
          return ☃.getBlockData(☃);
        }
      }
    }
    return Blocks.AIR.getBlockData();
  }
  
  public boolean isEmpty(BlockPosition ☃)
  {
    return getType(☃).getBlock().getMaterial() == Material.AIR;
  }
  
  public int getBlockPower(BlockPosition ☃, EnumDirection ☃)
  {
    IBlockData ☃ = getType(☃);
    return ☃.getBlock().b(this, ☃, ☃, ☃);
  }
}
