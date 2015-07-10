package net.minecraft.server.v1_8_R3;

import java.util.Random;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class BlockMonsterEggs
  extends Block
{
  public static final BlockStateEnum<EnumMonsterEggVarient> VARIANT = BlockStateEnum.of("variant", EnumMonsterEggVarient.class);
  
  public BlockMonsterEggs()
  {
    super(Material.CLAY);
    j(this.blockStateList.getBlockData().set(VARIANT, EnumMonsterEggVarient.STONE));
    c(0.0F);
    a(CreativeModeTab.c);
  }
  
  public int a(Random random)
  {
    return 0;
  }
  
  public static boolean d(IBlockData iblockdata)
  {
    Block block = iblockdata.getBlock();
    
    return (iblockdata == Blocks.STONE.getBlockData().set(BlockStone.VARIANT, BlockStone.EnumStoneVariant.STONE)) || (block == Blocks.COBBLESTONE) || (block == Blocks.STONEBRICK);
  }
  
  protected ItemStack i(IBlockData iblockdata)
  {
    switch (SyntheticClass_1.a[((EnumMonsterEggVarient)iblockdata.get(VARIANT)).ordinal()])
    {
    case 1: 
      return new ItemStack(Blocks.COBBLESTONE);
    case 2: 
      return new ItemStack(Blocks.STONEBRICK);
    case 3: 
      return new ItemStack(Blocks.STONEBRICK, 1, BlockSmoothBrick.EnumStonebrickType.MOSSY.a());
    case 4: 
      return new ItemStack(Blocks.STONEBRICK, 1, BlockSmoothBrick.EnumStonebrickType.CRACKED.a());
    case 5: 
      return new ItemStack(Blocks.STONEBRICK, 1, BlockSmoothBrick.EnumStonebrickType.CHISELED.a());
    }
    return new ItemStack(Blocks.STONE);
  }
  
  public void dropNaturally(World world, BlockPosition blockposition, IBlockData iblockdata, float f, int i)
  {
    if ((!world.isClientSide) && (world.getGameRules().getBoolean("doTileDrops")))
    {
      EntitySilverfish entitysilverfish = new EntitySilverfish(world);
      
      entitysilverfish.setPositionRotation(blockposition.getX() + 0.5D, blockposition.getY(), blockposition.getZ() + 0.5D, 0.0F, 0.0F);
      world.addEntity(entitysilverfish, CreatureSpawnEvent.SpawnReason.SILVERFISH_BLOCK);
      entitysilverfish.y();
    }
  }
  
  public int getDropData(World world, BlockPosition blockposition)
  {
    IBlockData iblockdata = world.getType(blockposition);
    
    return iblockdata.getBlock().toLegacyData(iblockdata);
  }
  
  public IBlockData fromLegacyData(int i)
  {
    return getBlockData().set(VARIANT, EnumMonsterEggVarient.a(i));
  }
  
  public int toLegacyData(IBlockData iblockdata)
  {
    return ((EnumMonsterEggVarient)iblockdata.get(VARIANT)).a();
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { VARIANT });
  }
  
  static class SyntheticClass_1
  {
    static final int[] a = new int[BlockMonsterEggs.EnumMonsterEggVarient.values().length];
    
    static
    {
      try
      {
        a[BlockMonsterEggs.EnumMonsterEggVarient.COBBLESTONE.ordinal()] = 1;
      }
      catch (NoSuchFieldError localNoSuchFieldError1) {}
      try
      {
        a[BlockMonsterEggs.EnumMonsterEggVarient.STONEBRICK.ordinal()] = 2;
      }
      catch (NoSuchFieldError localNoSuchFieldError2) {}
      try
      {
        a[BlockMonsterEggs.EnumMonsterEggVarient.MOSSY_STONEBRICK.ordinal()] = 3;
      }
      catch (NoSuchFieldError localNoSuchFieldError3) {}
      try
      {
        a[BlockMonsterEggs.EnumMonsterEggVarient.CRACKED_STONEBRICK.ordinal()] = 4;
      }
      catch (NoSuchFieldError localNoSuchFieldError4) {}
      try
      {
        a[BlockMonsterEggs.EnumMonsterEggVarient.CHISELED_STONEBRICK.ordinal()] = 5;
      }
      catch (NoSuchFieldError localNoSuchFieldError5) {}
    }
  }
  
  public static abstract enum EnumMonsterEggVarient
    implements INamable
  {
    STONE(0, "stone"),  COBBLESTONE(1, "cobblestone", "cobble"),  STONEBRICK(2, "stone_brick", "brick"),  MOSSY_STONEBRICK(3, "mossy_brick", "mossybrick"),  CRACKED_STONEBRICK(4, "cracked_brick", "crackedbrick"),  CHISELED_STONEBRICK(5, "chiseled_brick", "chiseledbrick");
    
    private static final EnumMonsterEggVarient[] g;
    private final int h;
    private final String i;
    private final String j;
    
    private EnumMonsterEggVarient(int i, String s)
    {
      this(i, s, s);
    }
    
    private EnumMonsterEggVarient(int i, String s, String s1)
    {
      this.h = i;
      this.i = s;
      this.j = s1;
    }
    
    public int a()
    {
      return this.h;
    }
    
    public String toString()
    {
      return this.i;
    }
    
    public static EnumMonsterEggVarient a(int i)
    {
      if ((i < 0) || (i >= g.length)) {
        i = 0;
      }
      return g[i];
    }
    
    public String getName()
    {
      return this.i;
    }
    
    public String c()
    {
      return this.j;
    }
    
    public static EnumMonsterEggVarient a(IBlockData iblockdata)
    {
      EnumMonsterEggVarient[] ablockmonstereggs_enummonstereggvarient = values();
      int i = ablockmonstereggs_enummonstereggvarient.length;
      for (int j = 0; j < i; j++)
      {
        EnumMonsterEggVarient blockmonstereggs_enummonstereggvarient = ablockmonstereggs_enummonstereggvarient[j];
        if (iblockdata == blockmonstereggs_enummonstereggvarient.d()) {
          return blockmonstereggs_enummonstereggvarient;
        }
      }
      return STONE;
    }
    
    private EnumMonsterEggVarient(int i, String s, BlockMonsterEggs.SyntheticClass_1 blockmonstereggs_syntheticclass_1)
    {
      this(i, s);
    }
    
    private EnumMonsterEggVarient(int i, String s, String s1, BlockMonsterEggs.SyntheticClass_1 blockmonstereggs_syntheticclass_1)
    {
      this(i, s, s1);
    }
    
    static
    {
      g = new EnumMonsterEggVarient[values().length];
      
      EnumMonsterEggVarient[] ablockmonstereggs_enummonstereggvarient = values();
      int i = ablockmonstereggs_enummonstereggvarient.length;
      for (int j = 0; j < i; j++)
      {
        EnumMonsterEggVarient blockmonstereggs_enummonstereggvarient = ablockmonstereggs_enummonstereggvarient[j];
        
        g[blockmonstereggs_enummonstereggvarient.a()] = blockmonstereggs_enummonstereggvarient;
      }
    }
    
    public abstract IBlockData d();
  }
}
