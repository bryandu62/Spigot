package net.minecraft.server.v1_8_R3;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import java.util.Collection;

public abstract class BlockFlowers
  extends BlockPlant
{
  protected BlockStateEnum<EnumFlowerVarient> TYPE;
  
  protected BlockFlowers()
  {
    j(this.blockStateList.getBlockData().set(n(), l() == EnumFlowerType.RED ? EnumFlowerVarient.POPPY : EnumFlowerVarient.DANDELION));
  }
  
  public int getDropData(IBlockData ☃)
  {
    return ((EnumFlowerVarient)☃.get(n())).b();
  }
  
  public IBlockData fromLegacyData(int ☃)
  {
    return getBlockData().set(n(), EnumFlowerVarient.a(l(), ☃));
  }
  
  public abstract EnumFlowerType l();
  
  public IBlockState<EnumFlowerVarient> n()
  {
    if (this.TYPE == null) {
      this.TYPE = BlockStateEnum.a("type", EnumFlowerVarient.class, new Predicate()
      {
        public boolean a(BlockFlowers.EnumFlowerVarient ☃)
        {
          return ☃.a() == BlockFlowers.this.l();
        }
      });
    }
    return this.TYPE;
  }
  
  public int toLegacyData(IBlockData ☃)
  {
    return ((EnumFlowerVarient)☃.get(n())).b();
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { n() });
  }
  
  public static enum EnumFlowerType
  {
    private EnumFlowerType() {}
    
    public BlockFlowers a()
    {
      if (this == YELLOW) {
        return Blocks.YELLOW_FLOWER;
      }
      return Blocks.RED_FLOWER;
    }
  }
  
  public static enum EnumFlowerVarient
    implements INamable
  {
    private static final EnumFlowerVarient[][] k;
    private final BlockFlowers.EnumFlowerType l;
    private final int m;
    private final String n;
    private final String o;
    
    static
    {
      k = new EnumFlowerVarient[BlockFlowers.EnumFlowerType.values().length][];
      for (BlockFlowers.EnumFlowerType ☃ : BlockFlowers.EnumFlowerType.values())
      {
        Collection<EnumFlowerVarient> ☃ = Collections2.filter(Lists.newArrayList(values()), new Predicate()
        {
          public boolean a(BlockFlowers.EnumFlowerVarient ☃)
          {
            return ☃.a() == this.a;
          }
        });
        k[☃.ordinal()] = ((EnumFlowerVarient[])☃.toArray(new EnumFlowerVarient[☃.size()]));
      }
    }
    
    private EnumFlowerVarient(BlockFlowers.EnumFlowerType ☃, int ☃, String ☃)
    {
      this(☃, ☃, ☃, ☃);
    }
    
    private EnumFlowerVarient(BlockFlowers.EnumFlowerType ☃, int ☃, String ☃, String ☃)
    {
      this.l = ☃;
      this.m = ☃;
      this.n = ☃;
      this.o = ☃;
    }
    
    public BlockFlowers.EnumFlowerType a()
    {
      return this.l;
    }
    
    public int b()
    {
      return this.m;
    }
    
    public static EnumFlowerVarient a(BlockFlowers.EnumFlowerType ☃, int ☃)
    {
      EnumFlowerVarient[] ☃ = k[☃.ordinal()];
      if ((☃ < 0) || (☃ >= ☃.length)) {
        ☃ = 0;
      }
      return ☃[☃];
    }
    
    public String toString()
    {
      return this.n;
    }
    
    public String getName()
    {
      return this.n;
    }
    
    public String d()
    {
      return this.o;
    }
  }
}
