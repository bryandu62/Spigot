package net.minecraft.server.v1_8_R3;

public class PathfinderGoalJumpOnBlock
  extends PathfinderGoalGotoTarget
{
  private final EntityOcelot c;
  
  public PathfinderGoalJumpOnBlock(EntityOcelot ☃, double ☃)
  {
    super(☃, ☃, 8);
    this.c = ☃;
  }
  
  public boolean a()
  {
    return (this.c.isTamed()) && (!this.c.isSitting()) && (super.a());
  }
  
  public boolean b()
  {
    return super.b();
  }
  
  public void c()
  {
    super.c();
    this.c.getGoalSit().setSitting(false);
  }
  
  public void d()
  {
    super.d();
    this.c.setSitting(false);
  }
  
  public void e()
  {
    super.e();
    
    this.c.getGoalSit().setSitting(false);
    if (!f()) {
      this.c.setSitting(false);
    } else if (!this.c.isSitting()) {
      this.c.setSitting(true);
    }
  }
  
  protected boolean a(World ☃, BlockPosition ☃)
  {
    if (!☃.isEmpty(☃.up())) {
      return false;
    }
    IBlockData ☃ = ☃.getType(☃);
    Block ☃ = ☃.getBlock();
    if (☃ == Blocks.CHEST)
    {
      TileEntity ☃ = ☃.getTileEntity(☃);
      if (((☃ instanceof TileEntityChest)) && (((TileEntityChest)☃).l < 1)) {
        return true;
      }
    }
    else
    {
      if (☃ == Blocks.LIT_FURNACE) {
        return true;
      }
      if ((☃ == Blocks.BED) && (☃.get(BlockBed.PART) != BlockBed.EnumBedPart.HEAD)) {
        return true;
      }
    }
    return false;
  }
}
