package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class BlockStone
  extends Block
{
  public static final BlockStateEnum<EnumStoneVariant> VARIANT = BlockStateEnum.of("variant", EnumStoneVariant.class);
  
  public BlockStone()
  {
    super(Material.STONE);
    j(this.blockStateList.getBlockData().set(VARIANT, EnumStoneVariant.STONE));
    a(CreativeModeTab.b);
  }
  
  public String getName()
  {
    return LocaleI18n.get(a() + "." + EnumStoneVariant.STONE.d() + ".name");
  }
  
  public MaterialMapColor g(IBlockData ☃)
  {
    return ((EnumStoneVariant)☃.get(VARIANT)).c();
  }
  
  public Item getDropType(IBlockData ☃, Random ☃, int ☃)
  {
    if (☃.get(VARIANT) == EnumStoneVariant.STONE) {
      return Item.getItemOf(Blocks.COBBLESTONE);
    }
    return Item.getItemOf(Blocks.STONE);
  }
  
  public int getDropData(IBlockData ☃)
  {
    return ((EnumStoneVariant)☃.get(VARIANT)).a();
  }
  
  public IBlockData fromLegacyData(int ☃)
  {
    return getBlockData().set(VARIANT, EnumStoneVariant.a(☃));
  }
  
  public int toLegacyData(IBlockData ☃)
  {
    return ((EnumStoneVariant)☃.get(VARIANT)).a();
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { VARIANT });
  }
  
  public static enum EnumStoneVariant
    implements INamable
  {
    private static final EnumStoneVariant[] h;
    private final int i;
    private final String j;
    private final String k;
    private final MaterialMapColor l;
    
    static
    {
      h = new EnumStoneVariant[values().length];
      for (EnumStoneVariant ☃ : values()) {
        h[☃.a()] = ☃;
      }
    }
    
    private EnumStoneVariant(int ☃, MaterialMapColor ☃, String ☃)
    {
      this(☃, ☃, ☃, ☃);
    }
    
    private EnumStoneVariant(int ☃, MaterialMapColor ☃, String ☃, String ☃)
    {
      this.i = ☃;
      this.j = ☃;
      this.k = ☃;
      this.l = ☃;
    }
    
    public int a()
    {
      return this.i;
    }
    
    public MaterialMapColor c()
    {
      return this.l;
    }
    
    public String toString()
    {
      return this.j;
    }
    
    public static EnumStoneVariant a(int ☃)
    {
      if ((☃ < 0) || (☃ >= h.length)) {
        ☃ = 0;
      }
      return h[☃];
    }
    
    public String getName()
    {
      return this.j;
    }
    
    public String d()
    {
      return this.k;
    }
  }
}
