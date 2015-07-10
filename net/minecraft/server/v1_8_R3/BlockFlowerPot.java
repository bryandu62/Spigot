package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class BlockFlowerPot
  extends BlockContainer
{
  public static final BlockStateInteger LEGACY_DATA = BlockStateInteger.of("legacy_data", 0, 15);
  public static final BlockStateEnum<EnumFlowerPotContents> CONTENTS = BlockStateEnum.of("contents", EnumFlowerPotContents.class);
  
  public BlockFlowerPot()
  {
    super(Material.ORIENTABLE);
    j(this.blockStateList.getBlockData().set(CONTENTS, EnumFlowerPotContents.EMPTY).set(LEGACY_DATA, Integer.valueOf(0)));
    j();
  }
  
  public String getName()
  {
    return LocaleI18n.get("item.flowerPot.name");
  }
  
  public void j()
  {
    float f = 0.375F;
    float f1 = f / 2.0F;
    
    a(0.5F - f1, 0.0F, 0.5F - f1, 0.5F + f1, f, 0.5F + f1);
  }
  
  public boolean c()
  {
    return false;
  }
  
  public int b()
  {
    return 3;
  }
  
  public boolean d()
  {
    return false;
  }
  
  public boolean interact(World world, BlockPosition blockposition, IBlockData iblockdata, EntityHuman entityhuman, EnumDirection enumdirection, float f, float f1, float f2)
  {
    ItemStack itemstack = entityhuman.inventory.getItemInHand();
    if ((itemstack != null) && ((itemstack.getItem() instanceof ItemBlock)))
    {
      TileEntityFlowerPot tileentityflowerpot = f(world, blockposition);
      if (tileentityflowerpot == null) {
        return false;
      }
      if (tileentityflowerpot.b() != null) {
        return false;
      }
      Block block = Block.asBlock(itemstack.getItem());
      if (!a(block, itemstack.getData())) {
        return false;
      }
      tileentityflowerpot.a(itemstack.getItem(), itemstack.getData());
      tileentityflowerpot.update();
      world.notify(blockposition);
      entityhuman.b(StatisticList.T);
      if (!entityhuman.abilities.canInstantlyBuild) {
        if (--itemstack.count <= 0) {
          entityhuman.inventory.setItem(entityhuman.inventory.itemInHandIndex, null);
        }
      }
      return true;
    }
    return false;
  }
  
  private boolean a(Block block, int i)
  {
    return (block == Blocks.TALLGRASS) && (i == BlockLongGrass.EnumTallGrassType.FERN.a());
  }
  
  public int getDropData(World world, BlockPosition blockposition)
  {
    TileEntityFlowerPot tileentityflowerpot = f(world, blockposition);
    
    return (tileentityflowerpot != null) && (tileentityflowerpot.b() != null) ? tileentityflowerpot.c() : 0;
  }
  
  public boolean canPlace(World world, BlockPosition blockposition)
  {
    return (super.canPlace(world, blockposition)) && (World.a(world, blockposition.down()));
  }
  
  public void doPhysics(World world, BlockPosition blockposition, IBlockData iblockdata, Block block)
  {
    if (!World.a(world, blockposition.down()))
    {
      b(world, blockposition, iblockdata, 0);
      world.setAir(blockposition);
    }
  }
  
  public void remove(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    TileEntityFlowerPot tileentityflowerpot = f(world, blockposition);
    if ((tileentityflowerpot != null) && (tileentityflowerpot.b() != null))
    {
      a(world, blockposition, new ItemStack(tileentityflowerpot.b(), 1, tileentityflowerpot.c()));
      tileentityflowerpot.a(null, 0);
    }
    super.remove(world, blockposition, iblockdata);
  }
  
  public void a(World world, BlockPosition blockposition, IBlockData iblockdata, EntityHuman entityhuman)
  {
    super.a(world, blockposition, iblockdata, entityhuman);
    if (entityhuman.abilities.canInstantlyBuild)
    {
      TileEntityFlowerPot tileentityflowerpot = f(world, blockposition);
      if (tileentityflowerpot != null) {
        tileentityflowerpot.a(null, 0);
      }
    }
  }
  
  public Item getDropType(IBlockData iblockdata, Random random, int i)
  {
    return Items.FLOWER_POT;
  }
  
  private TileEntityFlowerPot f(World world, BlockPosition blockposition)
  {
    TileEntity tileentity = world.getTileEntity(blockposition);
    
    return (tileentity instanceof TileEntityFlowerPot) ? (TileEntityFlowerPot)tileentity : null;
  }
  
  public TileEntity a(World world, int i)
  {
    Object object = null;
    int j = 0;
    switch (i)
    {
    case 1: 
      object = Blocks.RED_FLOWER;
      j = BlockFlowers.EnumFlowerVarient.POPPY.b();
      break;
    case 2: 
      object = Blocks.YELLOW_FLOWER;
      break;
    case 3: 
      object = Blocks.SAPLING;
      j = BlockWood.EnumLogVariant.OAK.a();
      break;
    case 4: 
      object = Blocks.SAPLING;
      j = BlockWood.EnumLogVariant.SPRUCE.a();
      break;
    case 5: 
      object = Blocks.SAPLING;
      j = BlockWood.EnumLogVariant.BIRCH.a();
      break;
    case 6: 
      object = Blocks.SAPLING;
      j = BlockWood.EnumLogVariant.JUNGLE.a();
      break;
    case 7: 
      object = Blocks.RED_MUSHROOM;
      break;
    case 8: 
      object = Blocks.BROWN_MUSHROOM;
      break;
    case 9: 
      object = Blocks.CACTUS;
      break;
    case 10: 
      object = Blocks.DEADBUSH;
      break;
    case 11: 
      object = Blocks.TALLGRASS;
      j = BlockLongGrass.EnumTallGrassType.FERN.a();
      break;
    case 12: 
      object = Blocks.SAPLING;
      j = BlockWood.EnumLogVariant.ACACIA.a();
      break;
    case 13: 
      object = Blocks.SAPLING;
      j = BlockWood.EnumLogVariant.DARK_OAK.a();
    }
    return new TileEntityFlowerPot(Item.getItemOf((Block)object), j);
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { CONTENTS, LEGACY_DATA });
  }
  
  public int toLegacyData(IBlockData iblockdata)
  {
    return ((Integer)iblockdata.get(LEGACY_DATA)).intValue();
  }
  
  public IBlockData updateState(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition)
  {
    EnumFlowerPotContents blockflowerpot_enumflowerpotcontents = EnumFlowerPotContents.EMPTY;
    TileEntity tileentity = iblockaccess.getTileEntity(blockposition);
    if ((tileentity instanceof TileEntityFlowerPot))
    {
      TileEntityFlowerPot tileentityflowerpot = (TileEntityFlowerPot)tileentity;
      Item item = tileentityflowerpot.b();
      if ((item instanceof ItemBlock))
      {
        int i = tileentityflowerpot.c();
        Block block = Block.asBlock(item);
        if (block == Blocks.SAPLING) {
          switch (SyntheticClass_1.a[BlockWood.EnumLogVariant.a(i).ordinal()])
          {
          case 1: 
            blockflowerpot_enumflowerpotcontents = EnumFlowerPotContents.OAK_SAPLING;
            break;
          case 2: 
            blockflowerpot_enumflowerpotcontents = EnumFlowerPotContents.SPRUCE_SAPLING;
            break;
          case 3: 
            blockflowerpot_enumflowerpotcontents = EnumFlowerPotContents.BIRCH_SAPLING;
            break;
          case 4: 
            blockflowerpot_enumflowerpotcontents = EnumFlowerPotContents.JUNGLE_SAPLING;
            break;
          case 5: 
            blockflowerpot_enumflowerpotcontents = EnumFlowerPotContents.ACACIA_SAPLING;
            break;
          case 6: 
            blockflowerpot_enumflowerpotcontents = EnumFlowerPotContents.DARK_OAK_SAPLING;
            break;
          default: 
            blockflowerpot_enumflowerpotcontents = EnumFlowerPotContents.EMPTY;
            
            break;
          }
        } else if (block == Blocks.TALLGRASS) {
          switch (i)
          {
          case 0: 
            blockflowerpot_enumflowerpotcontents = EnumFlowerPotContents.DEAD_BUSH;
            break;
          case 2: 
            blockflowerpot_enumflowerpotcontents = EnumFlowerPotContents.FERN;
            break;
          case 1: 
          default: 
            blockflowerpot_enumflowerpotcontents = EnumFlowerPotContents.EMPTY;
            
            break;
          }
        } else if (block == Blocks.YELLOW_FLOWER) {
          blockflowerpot_enumflowerpotcontents = EnumFlowerPotContents.DANDELION;
        } else if (block == Blocks.RED_FLOWER) {
          switch (SyntheticClass_1.b[BlockFlowers.EnumFlowerVarient.a(BlockFlowers.EnumFlowerType.RED, i).ordinal()])
          {
          case 1: 
            blockflowerpot_enumflowerpotcontents = EnumFlowerPotContents.POPPY;
            break;
          case 2: 
            blockflowerpot_enumflowerpotcontents = EnumFlowerPotContents.BLUE_ORCHID;
            break;
          case 3: 
            blockflowerpot_enumflowerpotcontents = EnumFlowerPotContents.ALLIUM;
            break;
          case 4: 
            blockflowerpot_enumflowerpotcontents = EnumFlowerPotContents.HOUSTONIA;
            break;
          case 5: 
            blockflowerpot_enumflowerpotcontents = EnumFlowerPotContents.RED_TULIP;
            break;
          case 6: 
            blockflowerpot_enumflowerpotcontents = EnumFlowerPotContents.ORANGE_TULIP;
            break;
          case 7: 
            blockflowerpot_enumflowerpotcontents = EnumFlowerPotContents.WHITE_TULIP;
            break;
          case 8: 
            blockflowerpot_enumflowerpotcontents = EnumFlowerPotContents.PINK_TULIP;
            break;
          case 9: 
            blockflowerpot_enumflowerpotcontents = EnumFlowerPotContents.OXEYE_DAISY;
            break;
          default: 
            blockflowerpot_enumflowerpotcontents = EnumFlowerPotContents.EMPTY;
            
            break;
          }
        } else if (block == Blocks.RED_MUSHROOM) {
          blockflowerpot_enumflowerpotcontents = EnumFlowerPotContents.MUSHROOM_RED;
        } else if (block == Blocks.BROWN_MUSHROOM) {
          blockflowerpot_enumflowerpotcontents = EnumFlowerPotContents.MUSHROOM_BROWN;
        } else if (block == Blocks.DEADBUSH) {
          blockflowerpot_enumflowerpotcontents = EnumFlowerPotContents.DEAD_BUSH;
        } else if (block == Blocks.CACTUS) {
          blockflowerpot_enumflowerpotcontents = EnumFlowerPotContents.CACTUS;
        }
      }
    }
    return iblockdata.set(CONTENTS, blockflowerpot_enumflowerpotcontents);
  }
  
  static class SyntheticClass_1
  {
    static final int[] a;
    static final int[] b = new int[BlockFlowers.EnumFlowerVarient.values().length];
    
    static
    {
      try
      {
        b[BlockFlowers.EnumFlowerVarient.POPPY.ordinal()] = 1;
      }
      catch (NoSuchFieldError localNoSuchFieldError1) {}
      try
      {
        b[BlockFlowers.EnumFlowerVarient.BLUE_ORCHID.ordinal()] = 2;
      }
      catch (NoSuchFieldError localNoSuchFieldError2) {}
      try
      {
        b[BlockFlowers.EnumFlowerVarient.ALLIUM.ordinal()] = 3;
      }
      catch (NoSuchFieldError localNoSuchFieldError3) {}
      try
      {
        b[BlockFlowers.EnumFlowerVarient.HOUSTONIA.ordinal()] = 4;
      }
      catch (NoSuchFieldError localNoSuchFieldError4) {}
      try
      {
        b[BlockFlowers.EnumFlowerVarient.RED_TULIP.ordinal()] = 5;
      }
      catch (NoSuchFieldError localNoSuchFieldError5) {}
      try
      {
        b[BlockFlowers.EnumFlowerVarient.ORANGE_TULIP.ordinal()] = 6;
      }
      catch (NoSuchFieldError localNoSuchFieldError6) {}
      try
      {
        b[BlockFlowers.EnumFlowerVarient.WHITE_TULIP.ordinal()] = 7;
      }
      catch (NoSuchFieldError localNoSuchFieldError7) {}
      try
      {
        b[BlockFlowers.EnumFlowerVarient.PINK_TULIP.ordinal()] = 8;
      }
      catch (NoSuchFieldError localNoSuchFieldError8) {}
      try
      {
        b[BlockFlowers.EnumFlowerVarient.OXEYE_DAISY.ordinal()] = 9;
      }
      catch (NoSuchFieldError localNoSuchFieldError9) {}
      a = new int[BlockWood.EnumLogVariant.values().length];
      try
      {
        a[BlockWood.EnumLogVariant.OAK.ordinal()] = 1;
      }
      catch (NoSuchFieldError localNoSuchFieldError10) {}
      try
      {
        a[BlockWood.EnumLogVariant.SPRUCE.ordinal()] = 2;
      }
      catch (NoSuchFieldError localNoSuchFieldError11) {}
      try
      {
        a[BlockWood.EnumLogVariant.BIRCH.ordinal()] = 3;
      }
      catch (NoSuchFieldError localNoSuchFieldError12) {}
      try
      {
        a[BlockWood.EnumLogVariant.JUNGLE.ordinal()] = 4;
      }
      catch (NoSuchFieldError localNoSuchFieldError13) {}
      try
      {
        a[BlockWood.EnumLogVariant.ACACIA.ordinal()] = 5;
      }
      catch (NoSuchFieldError localNoSuchFieldError14) {}
      try
      {
        a[BlockWood.EnumLogVariant.DARK_OAK.ordinal()] = 6;
      }
      catch (NoSuchFieldError localNoSuchFieldError15) {}
    }
  }
  
  public static enum EnumFlowerPotContents
    implements INamable
  {
    EMPTY("empty"),  POPPY("rose"),  BLUE_ORCHID("blue_orchid"),  ALLIUM("allium"),  HOUSTONIA("houstonia"),  RED_TULIP("red_tulip"),  ORANGE_TULIP("orange_tulip"),  WHITE_TULIP("white_tulip"),  PINK_TULIP("pink_tulip"),  OXEYE_DAISY("oxeye_daisy"),  DANDELION("dandelion"),  OAK_SAPLING("oak_sapling"),  SPRUCE_SAPLING("spruce_sapling"),  BIRCH_SAPLING("birch_sapling"),  JUNGLE_SAPLING("jungle_sapling"),  ACACIA_SAPLING("acacia_sapling"),  DARK_OAK_SAPLING("dark_oak_sapling"),  MUSHROOM_RED("mushroom_red"),  MUSHROOM_BROWN("mushroom_brown"),  DEAD_BUSH("dead_bush"),  FERN("fern"),  CACTUS("cactus");
    
    private final String w;
    
    private EnumFlowerPotContents(String s)
    {
      this.w = s;
    }
    
    public String toString()
    {
      return this.w;
    }
    
    public String getName()
    {
      return this.w;
    }
  }
}
