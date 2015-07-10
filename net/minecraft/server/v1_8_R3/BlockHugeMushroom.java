package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class BlockHugeMushroom
  extends Block
{
  public static final BlockStateEnum<EnumHugeMushroomVariant> VARIANT = BlockStateEnum.of("variant", EnumHugeMushroomVariant.class);
  private final Block b;
  
  public BlockHugeMushroom(Material ☃, MaterialMapColor ☃, Block ☃)
  {
    super(☃, ☃);
    j(this.blockStateList.getBlockData().set(VARIANT, EnumHugeMushroomVariant.ALL_OUTSIDE));
    this.b = ☃;
  }
  
  public int a(Random ☃)
  {
    return Math.max(0, ☃.nextInt(10) - 7);
  }
  
  public MaterialMapColor g(IBlockData ☃)
  {
    switch (1.a[((EnumHugeMushroomVariant)☃.get(VARIANT)).ordinal()])
    {
    case 1: 
      return MaterialMapColor.e;
    case 2: 
      return MaterialMapColor.d;
    case 3: 
      return MaterialMapColor.d;
    }
    return super.g(☃);
  }
  
  public Item getDropType(IBlockData ☃, Random ☃, int ☃)
  {
    return Item.getItemOf(this.b);
  }
  
  public IBlockData getPlacedState(World ☃, BlockPosition ☃, EnumDirection ☃, float ☃, float ☃, float ☃, int ☃, EntityLiving ☃)
  {
    return getBlockData();
  }
  
  public IBlockData fromLegacyData(int ☃)
  {
    return getBlockData().set(VARIANT, EnumHugeMushroomVariant.a(☃));
  }
  
  public int toLegacyData(IBlockData ☃)
  {
    return ((EnumHugeMushroomVariant)☃.get(VARIANT)).a();
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { VARIANT });
  }
  
  public static enum EnumHugeMushroomVariant
    implements INamable
  {
    private static final EnumHugeMushroomVariant[] n;
    private final int o;
    private final String p;
    
    static
    {
      n = new EnumHugeMushroomVariant[16];
      for (EnumHugeMushroomVariant ☃ : values()) {
        n[☃.a()] = ☃;
      }
    }
    
    private EnumHugeMushroomVariant(int ☃, String ☃)
    {
      this.o = ☃;
      this.p = ☃;
    }
    
    public int a()
    {
      return this.o;
    }
    
    public String toString()
    {
      return this.p;
    }
    
    public static EnumHugeMushroomVariant a(int ☃)
    {
      if ((☃ < 0) || (☃ >= n.length)) {
        ☃ = 0;
      }
      EnumHugeMushroomVariant ☃ = n[☃];
      return ☃ == null ? n[0] : ☃;
    }
    
    public String getName()
    {
      return this.p;
    }
  }
}
