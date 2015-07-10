package net.minecraft.server.v1_8_R3;

import java.util.List;
import java.util.Random;

public abstract class StructurePiece
{
  protected StructureBoundingBox l;
  protected EnumDirection m;
  protected int n;
  
  public StructurePiece() {}
  
  protected StructurePiece(int ☃)
  {
    this.n = ☃;
  }
  
  public NBTTagCompound b()
  {
    NBTTagCompound ☃ = new NBTTagCompound();
    
    ☃.setString("id", WorldGenFactory.a(this));
    ☃.set("BB", this.l.g());
    ☃.setInt("O", this.m == null ? -1 : this.m.b());
    ☃.setInt("GD", this.n);
    
    a(☃);
    
    return ☃;
  }
  
  protected abstract void a(NBTTagCompound paramNBTTagCompound);
  
  public void a(World ☃, NBTTagCompound ☃)
  {
    if (☃.hasKey("BB")) {
      this.l = new StructureBoundingBox(☃.getIntArray("BB"));
    }
    int ☃ = ☃.getInt("O");
    this.m = (☃ == -1 ? null : EnumDirection.fromType2(☃));
    this.n = ☃.getInt("GD");
    
    b(☃);
  }
  
  protected abstract void b(NBTTagCompound paramNBTTagCompound);
  
  public void a(StructurePiece ☃, List<StructurePiece> ☃, Random ☃) {}
  
  public abstract boolean a(World paramWorld, Random paramRandom, StructureBoundingBox paramStructureBoundingBox);
  
  public StructureBoundingBox c()
  {
    return this.l;
  }
  
  public int d()
  {
    return this.n;
  }
  
  public static StructurePiece a(List<StructurePiece> ☃, StructureBoundingBox ☃)
  {
    for (StructurePiece ☃ : ☃) {
      if ((☃.c() != null) && (☃.c().a(☃))) {
        return ☃;
      }
    }
    return null;
  }
  
  public BlockPosition a()
  {
    return new BlockPosition(this.l.f());
  }
  
  protected boolean a(World ☃, StructureBoundingBox ☃)
  {
    int ☃ = Math.max(this.l.a - 1, ☃.a);
    int ☃ = Math.max(this.l.b - 1, ☃.b);
    int ☃ = Math.max(this.l.c - 1, ☃.c);
    int ☃ = Math.min(this.l.d + 1, ☃.d);
    int ☃ = Math.min(this.l.e + 1, ☃.e);
    int ☃ = Math.min(this.l.f + 1, ☃.f);
    
    BlockPosition.MutableBlockPosition ☃ = new BlockPosition.MutableBlockPosition();
    for (int ☃ = ☃; ☃ <= ☃; ☃++) {
      for (int ☃ = ☃; ☃ <= ☃; ☃++)
      {
        if (☃.getType(☃.c(☃, ☃, ☃)).getBlock().getMaterial().isLiquid()) {
          return true;
        }
        if (☃.getType(☃.c(☃, ☃, ☃)).getBlock().getMaterial().isLiquid()) {
          return true;
        }
      }
    }
    for (int ☃ = ☃; ☃ <= ☃; ☃++) {
      for (int ☃ = ☃; ☃ <= ☃; ☃++)
      {
        if (☃.getType(☃.c(☃, ☃, ☃)).getBlock().getMaterial().isLiquid()) {
          return true;
        }
        if (☃.getType(☃.c(☃, ☃, ☃)).getBlock().getMaterial().isLiquid()) {
          return true;
        }
      }
    }
    for (int ☃ = ☃; ☃ <= ☃; ☃++) {
      for (int ☃ = ☃; ☃ <= ☃; ☃++)
      {
        if (☃.getType(☃.c(☃, ☃, ☃)).getBlock().getMaterial().isLiquid()) {
          return true;
        }
        if (☃.getType(☃.c(☃, ☃, ☃)).getBlock().getMaterial().isLiquid()) {
          return true;
        }
      }
    }
    return false;
  }
  
  protected int a(int ☃, int ☃)
  {
    if (this.m == null) {
      return ☃;
    }
    switch (1.a[this.m.ordinal()])
    {
    case 1: 
    case 2: 
      return this.l.a + ☃;
    case 3: 
      return this.l.d - ☃;
    case 4: 
      return this.l.a + ☃;
    }
    return ☃;
  }
  
  protected int d(int ☃)
  {
    if (this.m == null) {
      return ☃;
    }
    return ☃ + this.l.b;
  }
  
  protected int b(int ☃, int ☃)
  {
    if (this.m == null) {
      return ☃;
    }
    switch (1.a[this.m.ordinal()])
    {
    case 1: 
      return this.l.f - ☃;
    case 2: 
      return this.l.c + ☃;
    case 3: 
    case 4: 
      return this.l.c + ☃;
    }
    return ☃;
  }
  
  protected int a(Block ☃, int ☃)
  {
    if (☃ == Blocks.RAIL)
    {
      if ((this.m == EnumDirection.WEST) || (this.m == EnumDirection.EAST))
      {
        if (☃ == 1) {
          return 0;
        }
        return 1;
      }
    }
    else if ((☃ instanceof BlockDoor))
    {
      if (this.m == EnumDirection.SOUTH)
      {
        if (☃ == 0) {
          return 2;
        }
        if (☃ == 2) {
          return 0;
        }
      }
      else
      {
        if (this.m == EnumDirection.WEST) {
          return ☃ + 1 & 0x3;
        }
        if (this.m == EnumDirection.EAST) {
          return ☃ + 3 & 0x3;
        }
      }
    }
    else if ((☃ == Blocks.STONE_STAIRS) || (☃ == Blocks.OAK_STAIRS) || (☃ == Blocks.NETHER_BRICK_STAIRS) || (☃ == Blocks.STONE_BRICK_STAIRS) || (☃ == Blocks.SANDSTONE_STAIRS))
    {
      if (this.m == EnumDirection.SOUTH)
      {
        if (☃ == 2) {
          return 3;
        }
        if (☃ == 3) {
          return 2;
        }
      }
      else if (this.m == EnumDirection.WEST)
      {
        if (☃ == 0) {
          return 2;
        }
        if (☃ == 1) {
          return 3;
        }
        if (☃ == 2) {
          return 0;
        }
        if (☃ == 3) {
          return 1;
        }
      }
      else if (this.m == EnumDirection.EAST)
      {
        if (☃ == 0) {
          return 2;
        }
        if (☃ == 1) {
          return 3;
        }
        if (☃ == 2) {
          return 1;
        }
        if (☃ == 3) {
          return 0;
        }
      }
    }
    else if (☃ == Blocks.LADDER)
    {
      if (this.m == EnumDirection.SOUTH)
      {
        if (☃ == EnumDirection.NORTH.a()) {
          return EnumDirection.SOUTH.a();
        }
        if (☃ == EnumDirection.SOUTH.a()) {
          return EnumDirection.NORTH.a();
        }
      }
      else if (this.m == EnumDirection.WEST)
      {
        if (☃ == EnumDirection.NORTH.a()) {
          return EnumDirection.WEST.a();
        }
        if (☃ == EnumDirection.SOUTH.a()) {
          return EnumDirection.EAST.a();
        }
        if (☃ == EnumDirection.WEST.a()) {
          return EnumDirection.NORTH.a();
        }
        if (☃ == EnumDirection.EAST.a()) {
          return EnumDirection.SOUTH.a();
        }
      }
      else if (this.m == EnumDirection.EAST)
      {
        if (☃ == EnumDirection.NORTH.a()) {
          return EnumDirection.EAST.a();
        }
        if (☃ == EnumDirection.SOUTH.a()) {
          return EnumDirection.WEST.a();
        }
        if (☃ == EnumDirection.WEST.a()) {
          return EnumDirection.NORTH.a();
        }
        if (☃ == EnumDirection.EAST.a()) {
          return EnumDirection.SOUTH.a();
        }
      }
    }
    else if (☃ == Blocks.STONE_BUTTON)
    {
      if (this.m == EnumDirection.SOUTH)
      {
        if (☃ == 3) {
          return 4;
        }
        if (☃ == 4) {
          return 3;
        }
      }
      else if (this.m == EnumDirection.WEST)
      {
        if (☃ == 3) {
          return 1;
        }
        if (☃ == 4) {
          return 2;
        }
        if (☃ == 2) {
          return 3;
        }
        if (☃ == 1) {
          return 4;
        }
      }
      else if (this.m == EnumDirection.EAST)
      {
        if (☃ == 3) {
          return 2;
        }
        if (☃ == 4) {
          return 1;
        }
        if (☃ == 2) {
          return 3;
        }
        if (☃ == 1) {
          return 4;
        }
      }
    }
    else if ((☃ == Blocks.TRIPWIRE_HOOK) || ((☃ instanceof BlockDirectional)))
    {
      EnumDirection ☃ = EnumDirection.fromType2(☃);
      if (this.m == EnumDirection.SOUTH)
      {
        if ((☃ == EnumDirection.SOUTH) || (☃ == EnumDirection.NORTH)) {
          return ☃.opposite().b();
        }
      }
      else if (this.m == EnumDirection.WEST)
      {
        if (☃ == EnumDirection.NORTH) {
          return EnumDirection.WEST.b();
        }
        if (☃ == EnumDirection.SOUTH) {
          return EnumDirection.EAST.b();
        }
        if (☃ == EnumDirection.WEST) {
          return EnumDirection.NORTH.b();
        }
        if (☃ == EnumDirection.EAST) {
          return EnumDirection.SOUTH.b();
        }
      }
      else if (this.m == EnumDirection.EAST)
      {
        if (☃ == EnumDirection.NORTH) {
          return EnumDirection.EAST.b();
        }
        if (☃ == EnumDirection.SOUTH) {
          return EnumDirection.WEST.b();
        }
        if (☃ == EnumDirection.WEST) {
          return EnumDirection.NORTH.b();
        }
        if (☃ == EnumDirection.EAST) {
          return EnumDirection.SOUTH.b();
        }
      }
    }
    else if ((☃ == Blocks.PISTON) || (☃ == Blocks.STICKY_PISTON) || (☃ == Blocks.LEVER) || (☃ == Blocks.DISPENSER))
    {
      if (this.m == EnumDirection.SOUTH)
      {
        if ((☃ == EnumDirection.NORTH.a()) || (☃ == EnumDirection.SOUTH.a())) {
          return EnumDirection.fromType1(☃).opposite().a();
        }
      }
      else if (this.m == EnumDirection.WEST)
      {
        if (☃ == EnumDirection.NORTH.a()) {
          return EnumDirection.WEST.a();
        }
        if (☃ == EnumDirection.SOUTH.a()) {
          return EnumDirection.EAST.a();
        }
        if (☃ == EnumDirection.WEST.a()) {
          return EnumDirection.NORTH.a();
        }
        if (☃ == EnumDirection.EAST.a()) {
          return EnumDirection.SOUTH.a();
        }
      }
      else if (this.m == EnumDirection.EAST)
      {
        if (☃ == EnumDirection.NORTH.a()) {
          return EnumDirection.EAST.a();
        }
        if (☃ == EnumDirection.SOUTH.a()) {
          return EnumDirection.WEST.a();
        }
        if (☃ == EnumDirection.WEST.a()) {
          return EnumDirection.NORTH.a();
        }
        if (☃ == EnumDirection.EAST.a()) {
          return EnumDirection.SOUTH.a();
        }
      }
    }
    return ☃;
  }
  
  protected void a(World ☃, IBlockData ☃, int ☃, int ☃, int ☃, StructureBoundingBox ☃)
  {
    BlockPosition ☃ = new BlockPosition(a(☃, ☃), d(☃), b(☃, ☃));
    if (!☃.b(☃)) {
      return;
    }
    ☃.setTypeAndData(☃, ☃, 2);
  }
  
  protected IBlockData a(World ☃, int ☃, int ☃, int ☃, StructureBoundingBox ☃)
  {
    int ☃ = a(☃, ☃);
    int ☃ = d(☃);
    int ☃ = b(☃, ☃);
    
    BlockPosition ☃ = new BlockPosition(☃, ☃, ☃);
    if (!☃.b(☃)) {
      return Blocks.AIR.getBlockData();
    }
    return ☃.getType(☃);
  }
  
  protected void a(World ☃, StructureBoundingBox ☃, int ☃, int ☃, int ☃, int ☃, int ☃, int ☃)
  {
    for (int ☃ = ☃; ☃ <= ☃; ☃++) {
      for (int ☃ = ☃; ☃ <= ☃; ☃++) {
        for (int ☃ = ☃; ☃ <= ☃; ☃++) {
          a(☃, Blocks.AIR.getBlockData(), ☃, ☃, ☃, ☃);
        }
      }
    }
  }
  
  protected void a(World ☃, StructureBoundingBox ☃, int ☃, int ☃, int ☃, int ☃, int ☃, int ☃, IBlockData ☃, IBlockData ☃, boolean ☃)
  {
    for (int ☃ = ☃; ☃ <= ☃; ☃++) {
      for (int ☃ = ☃; ☃ <= ☃; ☃++) {
        for (int ☃ = ☃; ☃ <= ☃; ☃++) {
          if ((!☃) || (a(☃, ☃, ☃, ☃, ☃).getBlock().getMaterial() != Material.AIR)) {
            if ((☃ == ☃) || (☃ == ☃) || (☃ == ☃) || (☃ == ☃) || (☃ == ☃) || (☃ == ☃)) {
              a(☃, ☃, ☃, ☃, ☃, ☃);
            } else {
              a(☃, ☃, ☃, ☃, ☃, ☃);
            }
          }
        }
      }
    }
  }
  
  protected void a(World ☃, StructureBoundingBox ☃, int ☃, int ☃, int ☃, int ☃, int ☃, int ☃, boolean ☃, Random ☃, StructurePieceBlockSelector ☃)
  {
    for (int ☃ = ☃; ☃ <= ☃; ☃++) {
      for (int ☃ = ☃; ☃ <= ☃; ☃++) {
        for (int ☃ = ☃; ☃ <= ☃; ☃++) {
          if ((!☃) || (a(☃, ☃, ☃, ☃, ☃).getBlock().getMaterial() != Material.AIR))
          {
            ☃.a(☃, ☃, ☃, ☃, (☃ == ☃) || (☃ == ☃) || (☃ == ☃) || (☃ == ☃) || (☃ == ☃) || (☃ == ☃));
            a(☃, ☃.a(), ☃, ☃, ☃, ☃);
          }
        }
      }
    }
  }
  
  protected void a(World ☃, StructureBoundingBox ☃, Random ☃, float ☃, int ☃, int ☃, int ☃, int ☃, int ☃, int ☃, IBlockData ☃, IBlockData ☃, boolean ☃)
  {
    for (int ☃ = ☃; ☃ <= ☃; ☃++) {
      for (int ☃ = ☃; ☃ <= ☃; ☃++) {
        for (int ☃ = ☃; ☃ <= ☃; ☃++) {
          if (☃.nextFloat() <= ☃) {
            if ((!☃) || (a(☃, ☃, ☃, ☃, ☃).getBlock().getMaterial() != Material.AIR)) {
              if ((☃ == ☃) || (☃ == ☃) || (☃ == ☃) || (☃ == ☃) || (☃ == ☃) || (☃ == ☃)) {
                a(☃, ☃, ☃, ☃, ☃, ☃);
              } else {
                a(☃, ☃, ☃, ☃, ☃, ☃);
              }
            }
          }
        }
      }
    }
  }
  
  protected void a(World ☃, StructureBoundingBox ☃, Random ☃, float ☃, int ☃, int ☃, int ☃, IBlockData ☃)
  {
    if (☃.nextFloat() < ☃) {
      a(☃, ☃, ☃, ☃, ☃, ☃);
    }
  }
  
  protected void a(World ☃, StructureBoundingBox ☃, int ☃, int ☃, int ☃, int ☃, int ☃, int ☃, IBlockData ☃, boolean ☃)
  {
    float ☃ = ☃ - ☃ + 1;
    float ☃ = ☃ - ☃ + 1;
    float ☃ = ☃ - ☃ + 1;
    float ☃ = ☃ + ☃ / 2.0F;
    float ☃ = ☃ + ☃ / 2.0F;
    for (int ☃ = ☃; ☃ <= ☃; ☃++)
    {
      float ☃ = (☃ - ☃) / ☃;
      for (int ☃ = ☃; ☃ <= ☃; ☃++)
      {
        float ☃ = (☃ - ☃) / (☃ * 0.5F);
        for (int ☃ = ☃; ☃ <= ☃; ☃++)
        {
          float ☃ = (☃ - ☃) / (☃ * 0.5F);
          if ((!☃) || (a(☃, ☃, ☃, ☃, ☃).getBlock().getMaterial() != Material.AIR))
          {
            float ☃ = ☃ * ☃ + ☃ * ☃ + ☃ * ☃;
            if (☃ <= 1.05F) {
              a(☃, ☃, ☃, ☃, ☃, ☃);
            }
          }
        }
      }
    }
  }
  
  protected void b(World ☃, int ☃, int ☃, int ☃, StructureBoundingBox ☃)
  {
    BlockPosition ☃ = new BlockPosition(a(☃, ☃), d(☃), b(☃, ☃));
    if (!☃.b(☃)) {
      return;
    }
    while ((!☃.isEmpty(☃)) && (☃.getY() < 255))
    {
      ☃.setTypeAndData(☃, Blocks.AIR.getBlockData(), 2);
      ☃ = ☃.up();
    }
  }
  
  protected void b(World ☃, IBlockData ☃, int ☃, int ☃, int ☃, StructureBoundingBox ☃)
  {
    int ☃ = a(☃, ☃);
    int ☃ = d(☃);
    int ☃ = b(☃, ☃);
    if (!☃.b(new BlockPosition(☃, ☃, ☃))) {
      return;
    }
    while (((☃.isEmpty(new BlockPosition(☃, ☃, ☃))) || (☃.getType(new BlockPosition(☃, ☃, ☃)).getBlock().getMaterial().isLiquid())) && (☃ > 1))
    {
      ☃.setTypeAndData(new BlockPosition(☃, ☃, ☃), ☃, 2);
      ☃--;
    }
  }
  
  protected boolean a(World ☃, StructureBoundingBox ☃, Random ☃, int ☃, int ☃, int ☃, List<StructurePieceTreasure> ☃, int ☃)
  {
    BlockPosition ☃ = new BlockPosition(a(☃, ☃), d(☃), b(☃, ☃));
    if ((☃.b(☃)) && 
      (☃.getType(☃).getBlock() != Blocks.CHEST))
    {
      IBlockData ☃ = Blocks.CHEST.getBlockData();
      ☃.setTypeAndData(☃, Blocks.CHEST.f(☃, ☃, ☃), 2);
      
      TileEntity ☃ = ☃.getTileEntity(☃);
      if ((☃ instanceof TileEntityChest)) {
        StructurePieceTreasure.a(☃, ☃, (TileEntityChest)☃, ☃);
      }
      return true;
    }
    return false;
  }
  
  protected boolean a(World ☃, StructureBoundingBox ☃, Random ☃, int ☃, int ☃, int ☃, int ☃, List<StructurePieceTreasure> ☃, int ☃)
  {
    BlockPosition ☃ = new BlockPosition(a(☃, ☃), d(☃), b(☃, ☃));
    if ((☃.b(☃)) && 
      (☃.getType(☃).getBlock() != Blocks.DISPENSER))
    {
      ☃.setTypeAndData(☃, Blocks.DISPENSER.fromLegacyData(a(Blocks.DISPENSER, ☃)), 2);
      
      TileEntity ☃ = ☃.getTileEntity(☃);
      if ((☃ instanceof TileEntityDispenser)) {
        StructurePieceTreasure.a(☃, ☃, (TileEntityDispenser)☃, ☃);
      }
      return true;
    }
    return false;
  }
  
  protected void a(World ☃, StructureBoundingBox ☃, Random ☃, int ☃, int ☃, int ☃, EnumDirection ☃)
  {
    BlockPosition ☃ = new BlockPosition(a(☃, ☃), d(☃), b(☃, ☃));
    if (☃.b(☃)) {
      ItemDoor.a(☃, ☃, ☃.f(), Blocks.WOODEN_DOOR);
    }
  }
  
  public void a(int ☃, int ☃, int ☃)
  {
    this.l.a(☃, ☃, ☃);
  }
  
  public static abstract class StructurePieceBlockSelector
  {
    protected IBlockData a = Blocks.AIR.getBlockData();
    
    public abstract void a(Random paramRandom, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean);
    
    public IBlockData a()
    {
      return this.a;
    }
  }
}
