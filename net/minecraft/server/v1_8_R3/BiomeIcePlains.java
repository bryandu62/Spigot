package net.minecraft.server.v1_8_R3;

import java.util.List;
import java.util.Random;

public class BiomeIcePlains
  extends BiomeBase
{
  private boolean aD;
  private WorldGenPackedIce2 aE = new WorldGenPackedIce2();
  private WorldGenPackedIce1 aF = new WorldGenPackedIce1(4);
  
  public BiomeIcePlains(int ☃, boolean ☃)
  {
    super(☃);
    this.aD = ☃;
    if (☃) {
      this.ak = Blocks.SNOW.getBlockData();
    }
    this.au.clear();
  }
  
  public void a(World ☃, Random ☃, BlockPosition ☃)
  {
    if (this.aD)
    {
      for (int ☃ = 0; ☃ < 3; ☃++)
      {
        int ☃ = ☃.nextInt(16) + 8;
        int ☃ = ☃.nextInt(16) + 8;
        this.aE.generate(☃, ☃, ☃.getHighestBlockYAt(☃.a(☃, 0, ☃)));
      }
      for (int ☃ = 0; ☃ < 2; ☃++)
      {
        int ☃ = ☃.nextInt(16) + 8;
        int ☃ = ☃.nextInt(16) + 8;
        this.aF.generate(☃, ☃, ☃.getHighestBlockYAt(☃.a(☃, 0, ☃)));
      }
    }
    super.a(☃, ☃, ☃);
  }
  
  public WorldGenTreeAbstract a(Random ☃)
  {
    return new WorldGenTaiga2(false);
  }
  
  protected BiomeBase d(int ☃)
  {
    BiomeBase ☃ = new BiomeIcePlains(☃, true).a(13828095, true).a(this.ah + " Spikes").c().a(0.0F, 0.5F).a(new BiomeBase.BiomeTemperature(this.an + 0.1F, this.ao + 0.1F));
    
    ☃.an = (this.an + 0.3F);
    ☃.ao = (this.ao + 0.4F);
    
    return ☃;
  }
}
