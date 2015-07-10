package net.minecraft.server.v1_8_R3;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BlockStairs
  extends Block
{
  public static final BlockStateDirection FACING = BlockStateDirection.of("facing", EnumDirection.EnumDirectionLimit.HORIZONTAL);
  public static final BlockStateEnum<EnumHalf> HALF = BlockStateEnum.of("half", EnumHalf.class);
  public static final BlockStateEnum<EnumStairShape> SHAPE = BlockStateEnum.of("shape", EnumStairShape.class);
  private static final int[][] O = { { 4, 5 }, { 5, 7 }, { 6, 7 }, { 4, 6 }, { 0, 1 }, { 1, 3 }, { 2, 3 }, { 0, 2 } };
  private final Block P;
  private final IBlockData Q;
  private boolean R;
  private int S;
  
  protected BlockStairs(IBlockData ☃)
  {
    super(☃.getBlock().material);
    j(this.blockStateList.getBlockData().set(FACING, EnumDirection.NORTH).set(HALF, EnumHalf.BOTTOM).set(SHAPE, EnumStairShape.STRAIGHT));
    this.P = ☃.getBlock();
    this.Q = ☃;
    c(this.P.strength);
    b(this.P.durability / 3.0F);
    a(this.P.stepSound);
    e(255);
    a(CreativeModeTab.b);
  }
  
  public void updateShape(IBlockAccess ☃, BlockPosition ☃)
  {
    if (this.R) {
      a(0.5F * (this.S % 2), 0.5F * (this.S / 4 % 2), 0.5F * (this.S / 2 % 2), 0.5F + 0.5F * (this.S % 2), 0.5F + 0.5F * (this.S / 4 % 2), 0.5F + 0.5F * (this.S / 2 % 2));
    } else {
      a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
  }
  
  public boolean c()
  {
    return false;
  }
  
  public boolean d()
  {
    return false;
  }
  
  public void e(IBlockAccess ☃, BlockPosition ☃)
  {
    if (☃.getType(☃).get(HALF) == EnumHalf.TOP) {
      a(0.0F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F);
    } else {
      a(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
    }
  }
  
  public static boolean c(Block ☃)
  {
    return ☃ instanceof BlockStairs;
  }
  
  public static boolean a(IBlockAccess ☃, BlockPosition ☃, IBlockData ☃)
  {
    IBlockData ☃ = ☃.getType(☃);
    Block ☃ = ☃.getBlock();
    if ((c(☃)) && (☃.get(HALF) == ☃.get(HALF)) && (☃.get(FACING) == ☃.get(FACING))) {
      return true;
    }
    return false;
  }
  
  public int f(IBlockAccess ☃, BlockPosition ☃)
  {
    IBlockData ☃ = ☃.getType(☃);
    EnumDirection ☃ = (EnumDirection)☃.get(FACING);
    EnumHalf ☃ = (EnumHalf)☃.get(HALF);
    boolean ☃ = ☃ == EnumHalf.TOP;
    if (☃ == EnumDirection.EAST)
    {
      IBlockData ☃ = ☃.getType(☃.east());
      Block ☃ = ☃.getBlock();
      if ((c(☃)) && (☃ == ☃.get(HALF)))
      {
        EnumDirection ☃ = (EnumDirection)☃.get(FACING);
        if ((☃ == EnumDirection.NORTH) && (!a(☃, ☃.south(), ☃))) {
          return ☃ ? 1 : 2;
        }
        if ((☃ == EnumDirection.SOUTH) && (!a(☃, ☃.north(), ☃))) {
          return ☃ ? 2 : 1;
        }
      }
    }
    else if (☃ == EnumDirection.WEST)
    {
      IBlockData ☃ = ☃.getType(☃.west());
      Block ☃ = ☃.getBlock();
      if ((c(☃)) && (☃ == ☃.get(HALF)))
      {
        EnumDirection ☃ = (EnumDirection)☃.get(FACING);
        if ((☃ == EnumDirection.NORTH) && (!a(☃, ☃.south(), ☃))) {
          return ☃ ? 2 : 1;
        }
        if ((☃ == EnumDirection.SOUTH) && (!a(☃, ☃.north(), ☃))) {
          return ☃ ? 1 : 2;
        }
      }
    }
    else if (☃ == EnumDirection.SOUTH)
    {
      IBlockData ☃ = ☃.getType(☃.south());
      Block ☃ = ☃.getBlock();
      if ((c(☃)) && (☃ == ☃.get(HALF)))
      {
        EnumDirection ☃ = (EnumDirection)☃.get(FACING);
        if ((☃ == EnumDirection.WEST) && (!a(☃, ☃.east(), ☃))) {
          return ☃ ? 2 : 1;
        }
        if ((☃ == EnumDirection.EAST) && (!a(☃, ☃.west(), ☃))) {
          return ☃ ? 1 : 2;
        }
      }
    }
    else if (☃ == EnumDirection.NORTH)
    {
      IBlockData ☃ = ☃.getType(☃.north());
      Block ☃ = ☃.getBlock();
      if ((c(☃)) && (☃ == ☃.get(HALF)))
      {
        EnumDirection ☃ = (EnumDirection)☃.get(FACING);
        if ((☃ == EnumDirection.WEST) && (!a(☃, ☃.east(), ☃))) {
          return ☃ ? 1 : 2;
        }
        if ((☃ == EnumDirection.EAST) && (!a(☃, ☃.west(), ☃))) {
          return ☃ ? 2 : 1;
        }
      }
    }
    return 0;
  }
  
  public int g(IBlockAccess ☃, BlockPosition ☃)
  {
    IBlockData ☃ = ☃.getType(☃);
    EnumDirection ☃ = (EnumDirection)☃.get(FACING);
    EnumHalf ☃ = (EnumHalf)☃.get(HALF);
    boolean ☃ = ☃ == EnumHalf.TOP;
    if (☃ == EnumDirection.EAST)
    {
      IBlockData ☃ = ☃.getType(☃.west());
      Block ☃ = ☃.getBlock();
      if ((c(☃)) && (☃ == ☃.get(HALF)))
      {
        EnumDirection ☃ = (EnumDirection)☃.get(FACING);
        if ((☃ == EnumDirection.NORTH) && (!a(☃, ☃.north(), ☃))) {
          return ☃ ? 1 : 2;
        }
        if ((☃ == EnumDirection.SOUTH) && (!a(☃, ☃.south(), ☃))) {
          return ☃ ? 2 : 1;
        }
      }
    }
    else if (☃ == EnumDirection.WEST)
    {
      IBlockData ☃ = ☃.getType(☃.east());
      Block ☃ = ☃.getBlock();
      if ((c(☃)) && (☃ == ☃.get(HALF)))
      {
        EnumDirection ☃ = (EnumDirection)☃.get(FACING);
        if ((☃ == EnumDirection.NORTH) && (!a(☃, ☃.north(), ☃))) {
          return ☃ ? 2 : 1;
        }
        if ((☃ == EnumDirection.SOUTH) && (!a(☃, ☃.south(), ☃))) {
          return ☃ ? 1 : 2;
        }
      }
    }
    else if (☃ == EnumDirection.SOUTH)
    {
      IBlockData ☃ = ☃.getType(☃.north());
      Block ☃ = ☃.getBlock();
      if ((c(☃)) && (☃ == ☃.get(HALF)))
      {
        EnumDirection ☃ = (EnumDirection)☃.get(FACING);
        if ((☃ == EnumDirection.WEST) && (!a(☃, ☃.west(), ☃))) {
          return ☃ ? 2 : 1;
        }
        if ((☃ == EnumDirection.EAST) && (!a(☃, ☃.east(), ☃))) {
          return ☃ ? 1 : 2;
        }
      }
    }
    else if (☃ == EnumDirection.NORTH)
    {
      IBlockData ☃ = ☃.getType(☃.south());
      Block ☃ = ☃.getBlock();
      if ((c(☃)) && (☃ == ☃.get(HALF)))
      {
        EnumDirection ☃ = (EnumDirection)☃.get(FACING);
        if ((☃ == EnumDirection.WEST) && (!a(☃, ☃.west(), ☃))) {
          return ☃ ? 1 : 2;
        }
        if ((☃ == EnumDirection.EAST) && (!a(☃, ☃.east(), ☃))) {
          return ☃ ? 2 : 1;
        }
      }
    }
    return 0;
  }
  
  public boolean h(IBlockAccess ☃, BlockPosition ☃)
  {
    IBlockData ☃ = ☃.getType(☃);
    EnumDirection ☃ = (EnumDirection)☃.get(FACING);
    EnumHalf ☃ = (EnumHalf)☃.get(HALF);
    boolean ☃ = ☃ == EnumHalf.TOP;
    
    float ☃ = 0.5F;
    float ☃ = 1.0F;
    if (☃)
    {
      ☃ = 0.0F;
      ☃ = 0.5F;
    }
    float ☃ = 0.0F;
    float ☃ = 1.0F;
    float ☃ = 0.0F;
    float ☃ = 0.5F;
    
    boolean ☃ = true;
    if (☃ == EnumDirection.EAST)
    {
      ☃ = 0.5F;
      ☃ = 1.0F;
      
      IBlockData ☃ = ☃.getType(☃.east());
      Block ☃ = ☃.getBlock();
      if ((c(☃)) && (☃ == ☃.get(HALF)))
      {
        EnumDirection ☃ = (EnumDirection)☃.get(FACING);
        if ((☃ == EnumDirection.NORTH) && (!a(☃, ☃.south(), ☃)))
        {
          ☃ = 0.5F;
          ☃ = false;
        }
        else if ((☃ == EnumDirection.SOUTH) && (!a(☃, ☃.north(), ☃)))
        {
          ☃ = 0.5F;
          ☃ = false;
        }
      }
    }
    else if (☃ == EnumDirection.WEST)
    {
      ☃ = 0.5F;
      ☃ = 1.0F;
      
      IBlockData ☃ = ☃.getType(☃.west());
      Block ☃ = ☃.getBlock();
      if ((c(☃)) && (☃ == ☃.get(HALF)))
      {
        EnumDirection ☃ = (EnumDirection)☃.get(FACING);
        if ((☃ == EnumDirection.NORTH) && (!a(☃, ☃.south(), ☃)))
        {
          ☃ = 0.5F;
          ☃ = false;
        }
        else if ((☃ == EnumDirection.SOUTH) && (!a(☃, ☃.north(), ☃)))
        {
          ☃ = 0.5F;
          ☃ = false;
        }
      }
    }
    else if (☃ == EnumDirection.SOUTH)
    {
      ☃ = 0.5F;
      ☃ = 1.0F;
      
      IBlockData ☃ = ☃.getType(☃.south());
      Block ☃ = ☃.getBlock();
      if ((c(☃)) && (☃ == ☃.get(HALF)))
      {
        EnumDirection ☃ = (EnumDirection)☃.get(FACING);
        if ((☃ == EnumDirection.WEST) && (!a(☃, ☃.east(), ☃)))
        {
          ☃ = 0.5F;
          ☃ = false;
        }
        else if ((☃ == EnumDirection.EAST) && (!a(☃, ☃.west(), ☃)))
        {
          ☃ = 0.5F;
          ☃ = false;
        }
      }
    }
    else if (☃ == EnumDirection.NORTH)
    {
      IBlockData ☃ = ☃.getType(☃.north());
      Block ☃ = ☃.getBlock();
      if ((c(☃)) && (☃ == ☃.get(HALF)))
      {
        EnumDirection ☃ = (EnumDirection)☃.get(FACING);
        if ((☃ == EnumDirection.WEST) && (!a(☃, ☃.east(), ☃)))
        {
          ☃ = 0.5F;
          ☃ = false;
        }
        else if ((☃ == EnumDirection.EAST) && (!a(☃, ☃.west(), ☃)))
        {
          ☃ = 0.5F;
          ☃ = false;
        }
      }
    }
    a(☃, ☃, ☃, ☃, ☃, ☃);
    return ☃;
  }
  
  public boolean i(IBlockAccess ☃, BlockPosition ☃)
  {
    IBlockData ☃ = ☃.getType(☃);
    EnumDirection ☃ = (EnumDirection)☃.get(FACING);
    EnumHalf ☃ = (EnumHalf)☃.get(HALF);
    boolean ☃ = ☃ == EnumHalf.TOP;
    
    float ☃ = 0.5F;
    float ☃ = 1.0F;
    if (☃)
    {
      ☃ = 0.0F;
      ☃ = 0.5F;
    }
    float ☃ = 0.0F;
    float ☃ = 0.5F;
    float ☃ = 0.5F;
    float ☃ = 1.0F;
    
    boolean ☃ = false;
    if (☃ == EnumDirection.EAST)
    {
      IBlockData ☃ = ☃.getType(☃.west());
      Block ☃ = ☃.getBlock();
      if ((c(☃)) && (☃ == ☃.get(HALF)))
      {
        EnumDirection ☃ = (EnumDirection)☃.get(FACING);
        if ((☃ == EnumDirection.NORTH) && (!a(☃, ☃.north(), ☃)))
        {
          ☃ = 0.0F;
          ☃ = 0.5F;
          ☃ = true;
        }
        else if ((☃ == EnumDirection.SOUTH) && (!a(☃, ☃.south(), ☃)))
        {
          ☃ = 0.5F;
          ☃ = 1.0F;
          ☃ = true;
        }
      }
    }
    else if (☃ == EnumDirection.WEST)
    {
      IBlockData ☃ = ☃.getType(☃.east());
      Block ☃ = ☃.getBlock();
      if ((c(☃)) && (☃ == ☃.get(HALF)))
      {
        ☃ = 0.5F;
        ☃ = 1.0F;
        EnumDirection ☃ = (EnumDirection)☃.get(FACING);
        if ((☃ == EnumDirection.NORTH) && (!a(☃, ☃.north(), ☃)))
        {
          ☃ = 0.0F;
          ☃ = 0.5F;
          ☃ = true;
        }
        else if ((☃ == EnumDirection.SOUTH) && (!a(☃, ☃.south(), ☃)))
        {
          ☃ = 0.5F;
          ☃ = 1.0F;
          ☃ = true;
        }
      }
    }
    else if (☃ == EnumDirection.SOUTH)
    {
      IBlockData ☃ = ☃.getType(☃.north());
      Block ☃ = ☃.getBlock();
      if ((c(☃)) && (☃ == ☃.get(HALF)))
      {
        ☃ = 0.0F;
        ☃ = 0.5F;
        
        EnumDirection ☃ = (EnumDirection)☃.get(FACING);
        if ((☃ == EnumDirection.WEST) && (!a(☃, ☃.west(), ☃)))
        {
          ☃ = true;
        }
        else if ((☃ == EnumDirection.EAST) && (!a(☃, ☃.east(), ☃)))
        {
          ☃ = 0.5F;
          ☃ = 1.0F;
          ☃ = true;
        }
      }
    }
    else if (☃ == EnumDirection.NORTH)
    {
      IBlockData ☃ = ☃.getType(☃.south());
      Block ☃ = ☃.getBlock();
      if ((c(☃)) && (☃ == ☃.get(HALF)))
      {
        EnumDirection ☃ = (EnumDirection)☃.get(FACING);
        if ((☃ == EnumDirection.WEST) && (!a(☃, ☃.west(), ☃)))
        {
          ☃ = true;
        }
        else if ((☃ == EnumDirection.EAST) && (!a(☃, ☃.east(), ☃)))
        {
          ☃ = 0.5F;
          ☃ = 1.0F;
          ☃ = true;
        }
      }
    }
    if (☃) {
      a(☃, ☃, ☃, ☃, ☃, ☃);
    }
    return ☃;
  }
  
  public void a(World ☃, BlockPosition ☃, IBlockData ☃, AxisAlignedBB ☃, List<AxisAlignedBB> ☃, Entity ☃)
  {
    e(☃, ☃);
    super.a(☃, ☃, ☃, ☃, ☃, ☃);
    
    boolean ☃ = h(☃, ☃);
    super.a(☃, ☃, ☃, ☃, ☃, ☃);
    if ((☃) && 
      (i(☃, ☃))) {
      super.a(☃, ☃, ☃, ☃, ☃, ☃);
    }
    a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
  }
  
  public void attack(World ☃, BlockPosition ☃, EntityHuman ☃)
  {
    this.P.attack(☃, ☃, ☃);
  }
  
  public void postBreak(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    this.P.postBreak(☃, ☃, ☃);
  }
  
  public float a(Entity ☃)
  {
    return this.P.a(☃);
  }
  
  public int a(World ☃)
  {
    return this.P.a(☃);
  }
  
  public Vec3D a(World ☃, BlockPosition ☃, Entity ☃, Vec3D ☃)
  {
    return this.P.a(☃, ☃, ☃, ☃);
  }
  
  public boolean A()
  {
    return this.P.A();
  }
  
  public boolean a(IBlockData ☃, boolean ☃)
  {
    return this.P.a(☃, ☃);
  }
  
  public boolean canPlace(World ☃, BlockPosition ☃)
  {
    return this.P.canPlace(☃, ☃);
  }
  
  public void onPlace(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    doPhysics(☃, ☃, this.Q, Blocks.AIR);
    this.P.onPlace(☃, ☃, this.Q);
  }
  
  public void remove(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    this.P.remove(☃, ☃, this.Q);
  }
  
  public void a(World ☃, BlockPosition ☃, Entity ☃)
  {
    this.P.a(☃, ☃, ☃);
  }
  
  public void b(World ☃, BlockPosition ☃, IBlockData ☃, Random ☃)
  {
    this.P.b(☃, ☃, ☃, ☃);
  }
  
  public boolean interact(World ☃, BlockPosition ☃, IBlockData ☃, EntityHuman ☃, EnumDirection ☃, float ☃, float ☃, float ☃)
  {
    return this.P.interact(☃, ☃, this.Q, ☃, EnumDirection.DOWN, 0.0F, 0.0F, 0.0F);
  }
  
  public void wasExploded(World ☃, BlockPosition ☃, Explosion ☃)
  {
    this.P.wasExploded(☃, ☃, ☃);
  }
  
  public MaterialMapColor g(IBlockData ☃)
  {
    return this.P.g(this.Q);
  }
  
  public IBlockData getPlacedState(World ☃, BlockPosition ☃, EnumDirection ☃, float ☃, float ☃, float ☃, int ☃, EntityLiving ☃)
  {
    IBlockData ☃ = super.getPlacedState(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
    
    ☃ = ☃.set(FACING, ☃.getDirection()).set(SHAPE, EnumStairShape.STRAIGHT);
    if ((☃ == EnumDirection.DOWN) || ((☃ != EnumDirection.UP) && (☃ > 0.5D))) {
      return ☃.set(HALF, EnumHalf.TOP);
    }
    return ☃.set(HALF, EnumHalf.BOTTOM);
  }
  
  public MovingObjectPosition a(World ☃, BlockPosition ☃, Vec3D ☃, Vec3D ☃)
  {
    MovingObjectPosition[] ☃ = new MovingObjectPosition[8];
    IBlockData ☃ = ☃.getType(☃);
    int ☃ = ((EnumDirection)☃.get(FACING)).b();
    boolean ☃ = ☃.get(HALF) == EnumHalf.TOP;
    int[] ☃ = O[(☃ + 0)];
    
    this.R = true;
    for (int ☃ = 0; ☃ < 8; ☃++)
    {
      this.S = ☃;
      if (Arrays.binarySearch(☃, ☃) < 0) {
        ☃[☃] = super.a(☃, ☃, ☃, ☃);
      }
    }
    for (int ☃ : ☃) {
      ☃[☃] = null;
    }
    MovingObjectPosition ☃ = null;
    double ☃ = 0.0D;
    for (MovingObjectPosition ☃ : ☃) {
      if (☃ != null)
      {
        double ☃ = ☃.pos.distanceSquared(☃);
        if (☃ > ☃)
        {
          ☃ = ☃;
          ☃ = ☃;
        }
      }
    }
    return ☃;
  }
  
  public IBlockData fromLegacyData(int ☃)
  {
    IBlockData ☃ = getBlockData().set(HALF, (☃ & 0x4) > 0 ? EnumHalf.TOP : EnumHalf.BOTTOM);
    
    ☃ = ☃.set(FACING, EnumDirection.fromType1(5 - (☃ & 0x3)));
    
    return ☃;
  }
  
  public int toLegacyData(IBlockData ☃)
  {
    int ☃ = 0;
    if (☃.get(HALF) == EnumHalf.TOP) {
      ☃ |= 0x4;
    }
    ☃ |= 5 - ((EnumDirection)☃.get(FACING)).a();
    
    return ☃;
  }
  
  public IBlockData updateState(IBlockData ☃, IBlockAccess ☃, BlockPosition ☃)
  {
    if (h(☃, ☃)) {
      switch (g(☃, ☃))
      {
      case 0: 
        ☃ = ☃.set(SHAPE, EnumStairShape.STRAIGHT);
        break;
      case 1: 
        ☃ = ☃.set(SHAPE, EnumStairShape.INNER_RIGHT);
        break;
      case 2: 
        ☃ = ☃.set(SHAPE, EnumStairShape.INNER_LEFT);
      }
    } else {
      switch (f(☃, ☃))
      {
      case 0: 
        ☃ = ☃.set(SHAPE, EnumStairShape.STRAIGHT);
        break;
      case 1: 
        ☃ = ☃.set(SHAPE, EnumStairShape.OUTER_RIGHT);
        break;
      case 2: 
        ☃ = ☃.set(SHAPE, EnumStairShape.OUTER_LEFT);
      }
    }
    return ☃;
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { FACING, HALF, SHAPE });
  }
  
  public static enum EnumHalf
    implements INamable
  {
    private final String c;
    
    private EnumHalf(String ☃)
    {
      this.c = ☃;
    }
    
    public String toString()
    {
      return this.c;
    }
    
    public String getName()
    {
      return this.c;
    }
  }
  
  public static enum EnumStairShape
    implements INamable
  {
    private final String f;
    
    private EnumStairShape(String ☃)
    {
      this.f = ☃;
    }
    
    public String toString()
    {
      return this.f;
    }
    
    public String getName()
    {
      return this.f;
    }
  }
}
