package net.minecraft.server.v1_8_R3;

public class ChunkSection
{
  private int yPos;
  private int nonEmptyBlockCount;
  private int tickingBlockCount;
  private char[] blockIds;
  private NibbleArray emittedLight;
  private NibbleArray skyLight;
  
  public ChunkSection(int i, boolean flag)
  {
    this.yPos = i;
    this.blockIds = new char['က'];
    this.emittedLight = new NibbleArray();
    if (flag) {
      this.skyLight = new NibbleArray();
    }
  }
  
  public ChunkSection(int y, boolean flag, char[] blockIds)
  {
    this.yPos = y;
    this.blockIds = blockIds;
    this.emittedLight = new NibbleArray();
    if (flag) {
      this.skyLight = new NibbleArray();
    }
    recalcBlockCounts();
  }
  
  public IBlockData getType(int i, int j, int k)
  {
    IBlockData iblockdata = (IBlockData)Block.d.a(this.blockIds[(j << 8 | k << 4 | i)]);
    
    return iblockdata != null ? iblockdata : Blocks.AIR.getBlockData();
  }
  
  public void setType(int i, int j, int k, IBlockData iblockdata)
  {
    IBlockData iblockdata1 = getType(i, j, k);
    Block block = iblockdata1.getBlock();
    Block block1 = iblockdata.getBlock();
    if (block != Blocks.AIR)
    {
      this.nonEmptyBlockCount -= 1;
      if (block.isTicking()) {
        this.tickingBlockCount -= 1;
      }
    }
    if (block1 != Blocks.AIR)
    {
      this.nonEmptyBlockCount += 1;
      if (block1.isTicking()) {
        this.tickingBlockCount += 1;
      }
    }
    this.blockIds[(j << 8 | k << 4 | i)] = ((char)Block.d.b(iblockdata));
  }
  
  public Block b(int i, int j, int k)
  {
    return getType(i, j, k).getBlock();
  }
  
  public int c(int i, int j, int k)
  {
    IBlockData iblockdata = getType(i, j, k);
    
    return iblockdata.getBlock().toLegacyData(iblockdata);
  }
  
  public boolean a()
  {
    return this.nonEmptyBlockCount == 0;
  }
  
  public boolean shouldTick()
  {
    return this.tickingBlockCount > 0;
  }
  
  public int getYPosition()
  {
    return this.yPos;
  }
  
  public void a(int i, int j, int k, int l)
  {
    this.skyLight.a(i, j, k, l);
  }
  
  public int d(int i, int j, int k)
  {
    return this.skyLight.a(i, j, k);
  }
  
  public void b(int i, int j, int k, int l)
  {
    this.emittedLight.a(i, j, k, l);
  }
  
  public int e(int i, int j, int k)
  {
    return this.emittedLight.a(i, j, k);
  }
  
  public void recalcBlockCounts()
  {
    this.nonEmptyBlockCount = 0;
    this.tickingBlockCount = 0;
    for (int i = 0; i < 16; i++) {
      for (int j = 0; j < 16; j++) {
        for (int k = 0; k < 16; k++)
        {
          Block block = b(i, j, k);
          if (block != Blocks.AIR)
          {
            this.nonEmptyBlockCount += 1;
            if (block.isTicking()) {
              this.tickingBlockCount += 1;
            }
          }
        }
      }
    }
  }
  
  public char[] getIdArray()
  {
    return this.blockIds;
  }
  
  public void a(char[] achar)
  {
    this.blockIds = achar;
  }
  
  public NibbleArray getEmittedLightArray()
  {
    return this.emittedLight;
  }
  
  public NibbleArray getSkyLightArray()
  {
    return this.skyLight;
  }
  
  public void a(NibbleArray nibblearray)
  {
    this.emittedLight = nibblearray;
  }
  
  public void b(NibbleArray nibblearray)
  {
    this.skyLight = nibblearray;
  }
}
