package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class BiomeDecorator
{
  protected World a;
  protected Random b;
  protected BlockPosition c;
  protected CustomWorldSettingsFinal d;
  
  public void a(World ☃, Random ☃, BiomeBase ☃, BlockPosition ☃)
  {
    if (this.a != null) {
      throw new RuntimeException("Already decorating");
    }
    this.a = ☃;
    String ☃ = ☃.getWorldData().getGeneratorOptions();
    if (☃ != null) {
      this.d = CustomWorldSettingsFinal.CustomWorldSettings.a(☃).b();
    } else {
      this.d = CustomWorldSettingsFinal.CustomWorldSettings.a("").b();
    }
    this.b = ☃;
    this.c = ☃;
    
    this.h = new WorldGenMinable(Blocks.DIRT.getBlockData(), this.d.I);
    this.i = new WorldGenMinable(Blocks.GRAVEL.getBlockData(), this.d.M);
    this.j = new WorldGenMinable(Blocks.STONE.getBlockData().set(BlockStone.VARIANT, BlockStone.EnumStoneVariant.GRANITE), this.d.Q);
    this.k = new WorldGenMinable(Blocks.STONE.getBlockData().set(BlockStone.VARIANT, BlockStone.EnumStoneVariant.DIORITE), this.d.U);
    this.l = new WorldGenMinable(Blocks.STONE.getBlockData().set(BlockStone.VARIANT, BlockStone.EnumStoneVariant.ANDESITE), this.d.Y);
    this.m = new WorldGenMinable(Blocks.COAL_ORE.getBlockData(), this.d.ac);
    this.n = new WorldGenMinable(Blocks.IRON_ORE.getBlockData(), this.d.ag);
    this.o = new WorldGenMinable(Blocks.GOLD_ORE.getBlockData(), this.d.ak);
    this.p = new WorldGenMinable(Blocks.REDSTONE_ORE.getBlockData(), this.d.ao);
    this.q = new WorldGenMinable(Blocks.DIAMOND_ORE.getBlockData(), this.d.as);
    this.r = new WorldGenMinable(Blocks.LAPIS_ORE.getBlockData(), this.d.aw);
    
    a(☃);
    
    this.a = null;
    this.b = null;
  }
  
  protected WorldGenerator e = new WorldGenClay(4);
  protected WorldGenerator f = new WorldGenSand(Blocks.SAND, 7);
  protected WorldGenerator g = new WorldGenSand(Blocks.GRAVEL, 6);
  protected WorldGenerator h;
  protected WorldGenerator i;
  protected WorldGenerator j;
  protected WorldGenerator k;
  protected WorldGenerator l;
  protected WorldGenerator m;
  protected WorldGenerator n;
  protected WorldGenerator o;
  protected WorldGenerator p;
  protected WorldGenerator q;
  protected WorldGenerator r;
  protected WorldGenFlowers s = new WorldGenFlowers(Blocks.YELLOW_FLOWER, BlockFlowers.EnumFlowerVarient.DANDELION);
  protected WorldGenerator t = new WorldGenMushrooms(Blocks.BROWN_MUSHROOM);
  protected WorldGenerator u = new WorldGenMushrooms(Blocks.RED_MUSHROOM);
  protected WorldGenerator v = new WorldGenHugeMushroom();
  protected WorldGenerator w = new WorldGenReed();
  protected WorldGenerator x = new WorldGenCactus();
  protected WorldGenerator y = new WorldGenWaterLily();
  protected int z;
  protected int A;
  protected int B = 2;
  protected int C = 1;
  protected int D;
  protected int E;
  protected int F;
  protected int G;
  protected int H = 1;
  protected int I = 3;
  protected int J = 1;
  protected int K;
  public boolean L = true;
  
  protected void a(BiomeBase ☃)
  {
    a();
    for (int ☃ = 0; ☃ < this.I; ☃++)
    {
      int ☃ = this.b.nextInt(16) + 8;
      int ☃ = this.b.nextInt(16) + 8;
      this.f.generate(this.a, this.b, this.a.r(this.c.a(☃, 0, ☃)));
    }
    for (int ☃ = 0; ☃ < this.J; ☃++)
    {
      int ☃ = this.b.nextInt(16) + 8;
      int ☃ = this.b.nextInt(16) + 8;
      this.e.generate(this.a, this.b, this.a.r(this.c.a(☃, 0, ☃)));
    }
    for (int ☃ = 0; ☃ < this.H; ☃++)
    {
      int ☃ = this.b.nextInt(16) + 8;
      int ☃ = this.b.nextInt(16) + 8;
      this.g.generate(this.a, this.b, this.a.r(this.c.a(☃, 0, ☃)));
    }
    int ☃ = this.A;
    if (this.b.nextInt(10) == 0) {
      ☃++;
    }
    for (int ☃ = 0; ☃ < ☃; ☃++)
    {
      int ☃ = this.b.nextInt(16) + 8;
      int ☃ = this.b.nextInt(16) + 8;
      
      WorldGenTreeAbstract ☃ = ☃.a(this.b);
      ☃.e();
      
      BlockPosition ☃ = this.a.getHighestBlockYAt(this.c.a(☃, 0, ☃));
      if (☃.generate(this.a, this.b, ☃)) {
        ☃.a(this.a, this.b, ☃);
      }
    }
    for (int ☃ = 0; ☃ < this.K; ☃++)
    {
      int ☃ = this.b.nextInt(16) + 8;
      int ☃ = this.b.nextInt(16) + 8;
      this.v.generate(this.a, this.b, this.a.getHighestBlockYAt(this.c.a(☃, 0, ☃)));
    }
    for (int ☃ = 0; ☃ < this.B; ☃++)
    {
      int ☃ = this.b.nextInt(16) + 8;
      int ☃ = this.b.nextInt(16) + 8;
      int ☃ = this.a.getHighestBlockYAt(this.c.a(☃, 0, ☃)).getY() + 32;
      if (☃ > 0)
      {
        int ☃ = this.b.nextInt(☃);
        
        BlockPosition ☃ = this.c.a(☃, ☃, ☃);
        BlockFlowers.EnumFlowerVarient ☃ = ☃.a(this.b, ☃);
        BlockFlowers ☃ = ☃.a().a();
        if (☃.getMaterial() != Material.AIR)
        {
          this.s.a(☃, ☃);
          this.s.generate(this.a, this.b, ☃);
        }
      }
    }
    for (int ☃ = 0; ☃ < this.C; ☃++)
    {
      int ☃ = this.b.nextInt(16) + 8;
      int ☃ = this.b.nextInt(16) + 8;
      int ☃ = this.a.getHighestBlockYAt(this.c.a(☃, 0, ☃)).getY() * 2;
      if (☃ > 0)
      {
        int ☃ = this.b.nextInt(☃);
        
        ☃.b(this.b).generate(this.a, this.b, this.c.a(☃, ☃, ☃));
      }
    }
    for (int ☃ = 0; ☃ < this.D; ☃++)
    {
      int ☃ = this.b.nextInt(16) + 8;
      int ☃ = this.b.nextInt(16) + 8;
      int ☃ = this.a.getHighestBlockYAt(this.c.a(☃, 0, ☃)).getY() * 2;
      if (☃ > 0)
      {
        int ☃ = this.b.nextInt(☃);
        
        new WorldGenDeadBush().generate(this.a, this.b, this.c.a(☃, ☃, ☃));
      }
    }
    for (int ☃ = 0; ☃ < this.z; ☃++)
    {
      int ☃ = this.b.nextInt(16) + 8;
      int ☃ = this.b.nextInt(16) + 8;
      int ☃ = this.a.getHighestBlockYAt(this.c.a(☃, 0, ☃)).getY() * 2;
      if (☃ > 0)
      {
        int ☃ = this.b.nextInt(☃);
        
        BlockPosition ☃ = this.c.a(☃, ☃, ☃);
        while (☃.getY() > 0)
        {
          BlockPosition ☃ = ☃.down();
          if (!this.a.isEmpty(☃)) {
            break;
          }
          ☃ = ☃;
        }
        this.y.generate(this.a, this.b, ☃);
      }
    }
    for (int ☃ = 0; ☃ < this.E; ☃++)
    {
      if (this.b.nextInt(4) == 0)
      {
        int ☃ = this.b.nextInt(16) + 8;
        int ☃ = this.b.nextInt(16) + 8;
        BlockPosition ☃ = this.a.getHighestBlockYAt(this.c.a(☃, 0, ☃));
        this.t.generate(this.a, this.b, ☃);
      }
      if (this.b.nextInt(8) == 0)
      {
        int ☃ = this.b.nextInt(16) + 8;
        int ☃ = this.b.nextInt(16) + 8;
        int ☃ = this.a.getHighestBlockYAt(this.c.a(☃, 0, ☃)).getY() * 2;
        if (☃ > 0)
        {
          int ☃ = this.b.nextInt(☃);
          BlockPosition ☃ = this.c.a(☃, ☃, ☃);
          this.u.generate(this.a, this.b, ☃);
        }
      }
    }
    if (this.b.nextInt(4) == 0)
    {
      int ☃ = this.b.nextInt(16) + 8;
      int ☃ = this.b.nextInt(16) + 8;
      int ☃ = this.a.getHighestBlockYAt(this.c.a(☃, 0, ☃)).getY() * 2;
      if (☃ > 0)
      {
        int ☃ = this.b.nextInt(☃);
        this.t.generate(this.a, this.b, this.c.a(☃, ☃, ☃));
      }
    }
    if (this.b.nextInt(8) == 0)
    {
      int ☃ = this.b.nextInt(16) + 8;
      int ☃ = this.b.nextInt(16) + 8;
      int ☃ = this.a.getHighestBlockYAt(this.c.a(☃, 0, ☃)).getY() * 2;
      if (☃ > 0)
      {
        int ☃ = this.b.nextInt(☃);
        this.u.generate(this.a, this.b, this.c.a(☃, ☃, ☃));
      }
    }
    for (int ☃ = 0; ☃ < this.F; ☃++)
    {
      int ☃ = this.b.nextInt(16) + 8;
      int ☃ = this.b.nextInt(16) + 8;
      int ☃ = this.a.getHighestBlockYAt(this.c.a(☃, 0, ☃)).getY() * 2;
      if (☃ > 0)
      {
        int ☃ = this.b.nextInt(☃);
        this.w.generate(this.a, this.b, this.c.a(☃, ☃, ☃));
      }
    }
    for (int ☃ = 0; ☃ < 10; ☃++)
    {
      int ☃ = this.b.nextInt(16) + 8;
      int ☃ = this.b.nextInt(16) + 8;
      int ☃ = this.a.getHighestBlockYAt(this.c.a(☃, 0, ☃)).getY() * 2;
      if (☃ > 0)
      {
        int ☃ = this.b.nextInt(☃);
        this.w.generate(this.a, this.b, this.c.a(☃, ☃, ☃));
      }
    }
    if (this.b.nextInt(32) == 0)
    {
      int ☃ = this.b.nextInt(16) + 8;
      int ☃ = this.b.nextInt(16) + 8;
      int ☃ = this.a.getHighestBlockYAt(this.c.a(☃, 0, ☃)).getY() * 2;
      if (☃ > 0)
      {
        int ☃ = this.b.nextInt(☃);
        new WorldGenPumpkin().generate(this.a, this.b, this.c.a(☃, ☃, ☃));
      }
    }
    for (int ☃ = 0; ☃ < this.G; ☃++)
    {
      int ☃ = this.b.nextInt(16) + 8;
      int ☃ = this.b.nextInt(16) + 8;
      int ☃ = this.a.getHighestBlockYAt(this.c.a(☃, 0, ☃)).getY() * 2;
      if (☃ > 0)
      {
        int ☃ = this.b.nextInt(☃);
        this.x.generate(this.a, this.b, this.c.a(☃, ☃, ☃));
      }
    }
    if (this.L)
    {
      for (int ☃ = 0; ☃ < 50; ☃++)
      {
        int ☃ = this.b.nextInt(16) + 8;
        int ☃ = this.b.nextInt(16) + 8;
        int ☃ = this.b.nextInt(248) + 8;
        if (☃ > 0)
        {
          int ☃ = this.b.nextInt(☃);
          BlockPosition ☃ = this.c.a(☃, ☃, ☃);
          new WorldGenLiquids(Blocks.FLOWING_WATER).generate(this.a, this.b, ☃);
        }
      }
      for (int ☃ = 0; ☃ < 20; ☃++)
      {
        int ☃ = this.b.nextInt(16) + 8;
        int ☃ = this.b.nextInt(16) + 8;
        int ☃ = this.b.nextInt(this.b.nextInt(this.b.nextInt(240) + 8) + 8);
        BlockPosition ☃ = this.c.a(☃, ☃, ☃);
        
        new WorldGenLiquids(Blocks.FLOWING_LAVA).generate(this.a, this.b, ☃);
      }
    }
  }
  
  protected void a(int ☃, WorldGenerator ☃, int ☃, int ☃)
  {
    if (☃ < ☃)
    {
      int ☃ = ☃;
      ☃ = ☃;
      ☃ = ☃;
    }
    else if (☃ == ☃)
    {
      if (☃ < 255) {
        ☃++;
      } else {
        ☃--;
      }
    }
    for (int ☃ = 0; ☃ < ☃; ☃++)
    {
      BlockPosition ☃ = this.c.a(this.b.nextInt(16), this.b.nextInt(☃ - ☃) + ☃, this.b.nextInt(16));
      ☃.generate(this.a, this.b, ☃);
    }
  }
  
  protected void b(int ☃, WorldGenerator ☃, int ☃, int ☃)
  {
    for (int ☃ = 0; ☃ < ☃; ☃++)
    {
      BlockPosition ☃ = this.c.a(this.b.nextInt(16), this.b.nextInt(☃) + this.b.nextInt(☃) + ☃ - ☃, this.b.nextInt(16));
      ☃.generate(this.a, this.b, ☃);
    }
  }
  
  protected void a()
  {
    a(this.d.J, this.h, this.d.K, this.d.L);
    a(this.d.N, this.i, this.d.O, this.d.P);
    a(this.d.V, this.k, this.d.W, this.d.X);
    a(this.d.R, this.j, this.d.S, this.d.T);
    a(this.d.Z, this.l, this.d.aa, this.d.ab);
    a(this.d.ad, this.m, this.d.ae, this.d.af);
    a(this.d.ah, this.n, this.d.ai, this.d.aj);
    a(this.d.al, this.o, this.d.am, this.d.an);
    a(this.d.ap, this.p, this.d.aq, this.d.ar);
    a(this.d.at, this.q, this.d.au, this.d.av);
    b(this.d.ax, this.r, this.d.ay, this.d.az);
  }
}
