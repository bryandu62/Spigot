package net.minecraft.server.v1_8_R3;

import java.util.Iterator;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.plugin.PluginManager;

public class BlockLever
  extends Block
{
  public static final BlockStateEnum<EnumLeverPosition> FACING = BlockStateEnum.of("facing", EnumLeverPosition.class);
  public static final BlockStateBoolean POWERED = BlockStateBoolean.of("powered");
  
  protected BlockLever()
  {
    super(Material.ORIENTABLE);
    j(this.blockStateList.getBlockData().set(FACING, EnumLeverPosition.NORTH).set(POWERED, Boolean.valueOf(false)));
    a(CreativeModeTab.d);
  }
  
  public AxisAlignedBB a(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    return null;
  }
  
  public boolean c()
  {
    return false;
  }
  
  public boolean d()
  {
    return false;
  }
  
  public boolean canPlace(World world, BlockPosition blockposition, EnumDirection enumdirection)
  {
    return a(world, blockposition, enumdirection.opposite());
  }
  
  public boolean canPlace(World world, BlockPosition blockposition)
  {
    EnumDirection[] aenumdirection = EnumDirection.values();
    int i = aenumdirection.length;
    for (int j = 0; j < i; j++)
    {
      EnumDirection enumdirection = aenumdirection[j];
      if (a(world, blockposition, enumdirection)) {
        return true;
      }
    }
    return false;
  }
  
  protected static boolean a(World world, BlockPosition blockposition, EnumDirection enumdirection)
  {
    return BlockButtonAbstract.a(world, blockposition, enumdirection);
  }
  
  public IBlockData getPlacedState(World world, BlockPosition blockposition, EnumDirection enumdirection, float f, float f1, float f2, int i, EntityLiving entityliving)
  {
    IBlockData iblockdata = getBlockData().set(POWERED, Boolean.valueOf(false));
    if (a(world, blockposition, enumdirection.opposite())) {
      return iblockdata.set(FACING, EnumLeverPosition.a(enumdirection, entityliving.getDirection()));
    }
    Iterator iterator = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();
    EnumDirection enumdirection1;
    do
    {
      if (!iterator.hasNext())
      {
        if (World.a(world, blockposition.down())) {
          return iblockdata.set(FACING, EnumLeverPosition.a(EnumDirection.UP, entityliving.getDirection()));
        }
        return iblockdata;
      }
      enumdirection1 = (EnumDirection)iterator.next();
    } while ((enumdirection1 == enumdirection) || (!a(world, blockposition, enumdirection1.opposite())));
    return iblockdata.set(FACING, EnumLeverPosition.a(enumdirection1, entityliving.getDirection()));
  }
  
  public static int a(EnumDirection enumdirection)
  {
    switch (SyntheticClass_1.a[enumdirection.ordinal()])
    {
    case 1: 
      return 0;
    case 2: 
      return 5;
    case 3: 
      return 4;
    case 4: 
      return 3;
    case 5: 
      return 2;
    case 6: 
      return 1;
    }
    return -1;
  }
  
  public void doPhysics(World world, BlockPosition blockposition, IBlockData iblockdata, Block block)
  {
    if ((e(world, blockposition, iblockdata)) && (!a(world, blockposition, ((EnumLeverPosition)iblockdata.get(FACING)).c().opposite())))
    {
      b(world, blockposition, iblockdata, 0);
      world.setAir(blockposition);
    }
  }
  
  private boolean e(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    if (canPlace(world, blockposition)) {
      return true;
    }
    b(world, blockposition, iblockdata, 0);
    world.setAir(blockposition);
    return false;
  }
  
  public void updateShape(IBlockAccess iblockaccess, BlockPosition blockposition)
  {
    float f = 0.1875F;
    switch (SyntheticClass_1.b[((EnumLeverPosition)iblockaccess.getType(blockposition).get(FACING)).ordinal()])
    {
    case 1: 
      a(0.0F, 0.2F, 0.5F - f, f * 2.0F, 0.8F, 0.5F + f);
      break;
    case 2: 
      a(1.0F - f * 2.0F, 0.2F, 0.5F - f, 1.0F, 0.8F, 0.5F + f);
      break;
    case 3: 
      a(0.5F - f, 0.2F, 0.0F, 0.5F + f, 0.8F, f * 2.0F);
      break;
    case 4: 
      a(0.5F - f, 0.2F, 1.0F - f * 2.0F, 0.5F + f, 0.8F, 1.0F);
      break;
    case 5: 
    case 6: 
      f = 0.25F;
      a(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.6F, 0.5F + f);
      break;
    case 7: 
    case 8: 
      f = 0.25F;
      a(0.5F - f, 0.4F, 0.5F - f, 0.5F + f, 1.0F, 0.5F + f);
    }
  }
  
  public boolean interact(World world, BlockPosition blockposition, IBlockData iblockdata, EntityHuman entityhuman, EnumDirection enumdirection, float f, float f1, float f2)
  {
    if (world.isClientSide) {
      return true;
    }
    boolean powered = ((Boolean)iblockdata.get(POWERED)).booleanValue();
    org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
    int old = powered ? 15 : 0;
    int current = !powered ? 15 : 0;
    
    BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, old, current);
    world.getServer().getPluginManager().callEvent(eventRedstone);
    if ((eventRedstone.getNewCurrent() > 0 ? 1 : 0) != (powered ? 0 : 1)) {
      return true;
    }
    iblockdata = iblockdata.a(POWERED);
    world.setTypeAndData(blockposition, iblockdata, 3);
    world.makeSound(blockposition.getX() + 0.5D, blockposition.getY() + 0.5D, blockposition.getZ() + 0.5D, "random.click", 0.3F, ((Boolean)iblockdata.get(POWERED)).booleanValue() ? 0.6F : 0.5F);
    world.applyPhysics(blockposition, this);
    EnumDirection enumdirection1 = ((EnumLeverPosition)iblockdata.get(FACING)).c();
    
    world.applyPhysics(blockposition.shift(enumdirection1.opposite()), this);
    return true;
  }
  
  public void remove(World world, BlockPosition blockposition, IBlockData iblockdata)
  {
    if (((Boolean)iblockdata.get(POWERED)).booleanValue())
    {
      world.applyPhysics(blockposition, this);
      EnumDirection enumdirection = ((EnumLeverPosition)iblockdata.get(FACING)).c();
      
      world.applyPhysics(blockposition.shift(enumdirection.opposite()), this);
    }
    super.remove(world, blockposition, iblockdata);
  }
  
  public int a(IBlockAccess iblockaccess, BlockPosition blockposition, IBlockData iblockdata, EnumDirection enumdirection)
  {
    return ((Boolean)iblockdata.get(POWERED)).booleanValue() ? 15 : 0;
  }
  
  public int b(IBlockAccess iblockaccess, BlockPosition blockposition, IBlockData iblockdata, EnumDirection enumdirection)
  {
    return ((EnumLeverPosition)iblockdata.get(FACING)).c() == enumdirection ? 15 : !((Boolean)iblockdata.get(POWERED)).booleanValue() ? 0 : 0;
  }
  
  public boolean isPowerSource()
  {
    return true;
  }
  
  public IBlockData fromLegacyData(int i)
  {
    return getBlockData().set(FACING, EnumLeverPosition.a(i & 0x7)).set(POWERED, Boolean.valueOf((i & 0x8) > 0));
  }
  
  public int toLegacyData(IBlockData iblockdata)
  {
    byte b0 = 0;
    int i = b0 | ((EnumLeverPosition)iblockdata.get(FACING)).a();
    if (((Boolean)iblockdata.get(POWERED)).booleanValue()) {
      i |= 0x8;
    }
    return i;
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { FACING, POWERED });
  }
  
  static class SyntheticClass_1
  {
    static final int[] a;
    static final int[] b;
    static final int[] c = new int[EnumDirection.EnumAxis.values().length];
    
    static
    {
      try
      {
        c[EnumDirection.EnumAxis.X.ordinal()] = 1;
      }
      catch (NoSuchFieldError localNoSuchFieldError1) {}
      try
      {
        c[EnumDirection.EnumAxis.Z.ordinal()] = 2;
      }
      catch (NoSuchFieldError localNoSuchFieldError2) {}
      b = new int[BlockLever.EnumLeverPosition.values().length];
      try
      {
        b[BlockLever.EnumLeverPosition.EAST.ordinal()] = 1;
      }
      catch (NoSuchFieldError localNoSuchFieldError3) {}
      try
      {
        b[BlockLever.EnumLeverPosition.WEST.ordinal()] = 2;
      }
      catch (NoSuchFieldError localNoSuchFieldError4) {}
      try
      {
        b[BlockLever.EnumLeverPosition.SOUTH.ordinal()] = 3;
      }
      catch (NoSuchFieldError localNoSuchFieldError5) {}
      try
      {
        b[BlockLever.EnumLeverPosition.NORTH.ordinal()] = 4;
      }
      catch (NoSuchFieldError localNoSuchFieldError6) {}
      try
      {
        b[BlockLever.EnumLeverPosition.UP_Z.ordinal()] = 5;
      }
      catch (NoSuchFieldError localNoSuchFieldError7) {}
      try
      {
        b[BlockLever.EnumLeverPosition.UP_X.ordinal()] = 6;
      }
      catch (NoSuchFieldError localNoSuchFieldError8) {}
      try
      {
        b[BlockLever.EnumLeverPosition.DOWN_X.ordinal()] = 7;
      }
      catch (NoSuchFieldError localNoSuchFieldError9) {}
      try
      {
        b[BlockLever.EnumLeverPosition.DOWN_Z.ordinal()] = 8;
      }
      catch (NoSuchFieldError localNoSuchFieldError10) {}
      a = new int[EnumDirection.values().length];
      try
      {
        a[EnumDirection.DOWN.ordinal()] = 1;
      }
      catch (NoSuchFieldError localNoSuchFieldError11) {}
      try
      {
        a[EnumDirection.UP.ordinal()] = 2;
      }
      catch (NoSuchFieldError localNoSuchFieldError12) {}
      try
      {
        a[EnumDirection.NORTH.ordinal()] = 3;
      }
      catch (NoSuchFieldError localNoSuchFieldError13) {}
      try
      {
        a[EnumDirection.SOUTH.ordinal()] = 4;
      }
      catch (NoSuchFieldError localNoSuchFieldError14) {}
      try
      {
        a[EnumDirection.WEST.ordinal()] = 5;
      }
      catch (NoSuchFieldError localNoSuchFieldError15) {}
      try
      {
        a[EnumDirection.EAST.ordinal()] = 6;
      }
      catch (NoSuchFieldError localNoSuchFieldError16) {}
    }
  }
  
  public static enum EnumLeverPosition
    implements INamable
  {
    DOWN_X(0, "down_x", EnumDirection.DOWN),  EAST(1, "east", EnumDirection.EAST),  WEST(2, "west", EnumDirection.WEST),  SOUTH(3, "south", EnumDirection.SOUTH),  NORTH(4, "north", EnumDirection.NORTH),  UP_Z(5, "up_z", EnumDirection.UP),  UP_X(6, "up_x", EnumDirection.UP),  DOWN_Z(7, "down_z", EnumDirection.DOWN);
    
    private static final EnumLeverPosition[] i;
    private final int j;
    private final String k;
    private final EnumDirection l;
    
    private EnumLeverPosition(int i, String s, EnumDirection enumdirection)
    {
      this.j = i;
      this.k = s;
      this.l = enumdirection;
    }
    
    public int a()
    {
      return this.j;
    }
    
    public EnumDirection c()
    {
      return this.l;
    }
    
    public String toString()
    {
      return this.k;
    }
    
    public static EnumLeverPosition a(int i)
    {
      if ((i < 0) || (i >= i.length)) {
        i = 0;
      }
      return i[i];
    }
    
    public static EnumLeverPosition a(EnumDirection enumdirection, EnumDirection enumdirection1)
    {
      switch (BlockLever.SyntheticClass_1.a[enumdirection.ordinal()])
      {
      case 1: 
        switch (BlockLever.SyntheticClass_1.c[enumdirection1.k().ordinal()])
        {
        case 1: 
          return DOWN_X;
        case 2: 
          return DOWN_Z;
        }
        throw new IllegalArgumentException("Invalid entityFacing " + enumdirection1 + " for facing " + enumdirection);
      case 2: 
        switch (BlockLever.SyntheticClass_1.c[enumdirection1.k().ordinal()])
        {
        case 1: 
          return UP_X;
        case 2: 
          return UP_Z;
        }
        throw new IllegalArgumentException("Invalid entityFacing " + enumdirection1 + " for facing " + enumdirection);
      case 3: 
        return NORTH;
      case 4: 
        return SOUTH;
      case 5: 
        return WEST;
      case 6: 
        return EAST;
      }
      throw new IllegalArgumentException("Invalid facing: " + enumdirection);
    }
    
    public String getName()
    {
      return this.k;
    }
    
    static
    {
      i = new EnumLeverPosition[values().length];
      
      EnumLeverPosition[] ablocklever_enumleverposition = values();
      int i = ablocklever_enumleverposition.length;
      for (int j = 0; j < i; j++)
      {
        EnumLeverPosition blocklever_enumleverposition = ablocklever_enumleverposition[j];
        
        i[blocklever_enumleverposition.a()] = blocklever_enumleverposition;
      }
    }
  }
}
