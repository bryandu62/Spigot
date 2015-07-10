package net.minecraft.server.v1_8_R3;

public class ChunkSnapshot
{
  private final short[] a = new short[65536];
  private final IBlockData b = Blocks.AIR.getBlockData();
  
  public IBlockData a(int ☃, int ☃, int ☃)
  {
    int ☃ = ☃ << 12 | ☃ << 8 | ☃;
    return a(☃);
  }
  
  public IBlockData a(int ☃)
  {
    if ((☃ < 0) || (☃ >= this.a.length)) {
      throw new IndexOutOfBoundsException("The coordinate is out of range");
    }
    IBlockData ☃ = (IBlockData)Block.d.a(this.a[☃]);
    if (☃ != null) {
      return ☃;
    }
    return this.b;
  }
  
  public void a(int ☃, int ☃, int ☃, IBlockData ☃)
  {
    int ☃ = ☃ << 12 | ☃ << 8 | ☃;
    
    a(☃, ☃);
  }
  
  public void a(int ☃, IBlockData ☃)
  {
    if ((☃ < 0) || (☃ >= this.a.length)) {
      throw new IndexOutOfBoundsException("The coordinate is out of range");
    }
    this.a[☃] = ((short)Block.d.b(☃));
  }
}
