package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import java.util.Random;

public class BiomeBaseSub
  extends BiomeBase
{
  protected BiomeBase aE;
  
  public BiomeBaseSub(int ☃, BiomeBase ☃)
  {
    super(☃);
    this.aE = ☃;
    a(☃.ai, true);
    
    this.ah = (☃.ah + " M");
    
    this.ak = ☃.ak;
    this.al = ☃.al;
    this.am = ☃.am;
    this.an = ☃.an;
    this.ao = ☃.ao;
    this.temperature = ☃.temperature;
    this.humidity = ☃.humidity;
    this.ar = ☃.ar;
    this.ax = ☃.ax;
    this.ay = ☃.ay;
    
    this.au = Lists.newArrayList(☃.au);
    this.at = Lists.newArrayList(☃.at);
    this.aw = Lists.newArrayList(☃.aw);
    this.av = Lists.newArrayList(☃.av);
    
    this.temperature = ☃.temperature;
    this.humidity = ☃.humidity;
    
    this.an = (☃.an + 0.1F);
    this.ao = (☃.ao + 0.2F);
  }
  
  public void a(World ☃, Random ☃, BlockPosition ☃)
  {
    this.aE.as.a(☃, ☃, this, ☃);
  }
  
  public void a(World ☃, Random ☃, ChunkSnapshot ☃, int ☃, int ☃, double ☃)
  {
    this.aE.a(☃, ☃, ☃, ☃, ☃, ☃);
  }
  
  public float g()
  {
    return this.aE.g();
  }
  
  public WorldGenTreeAbstract a(Random ☃)
  {
    return this.aE.a(☃);
  }
  
  public Class<? extends BiomeBase> l()
  {
    return this.aE.l();
  }
  
  public boolean a(BiomeBase ☃)
  {
    return this.aE.a(☃);
  }
  
  public BiomeBase.EnumTemperature m()
  {
    return this.aE.m();
  }
}
