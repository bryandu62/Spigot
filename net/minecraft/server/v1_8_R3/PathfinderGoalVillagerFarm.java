package net.minecraft.server.v1_8_R3;

public class PathfinderGoalVillagerFarm
  extends PathfinderGoalGotoTarget
{
  private final EntityVillager c;
  private boolean d;
  private boolean e;
  private int f;
  
  public PathfinderGoalVillagerFarm(EntityVillager ☃, double ☃)
  {
    super(☃, ☃, 16);
    this.c = ☃;
  }
  
  public boolean a()
  {
    if (this.a <= 0)
    {
      if (!this.c.world.getGameRules().getBoolean("mobGriefing")) {
        return false;
      }
      this.f = -1;
      this.d = this.c.cu();
      this.e = this.c.ct();
    }
    return super.a();
  }
  
  public boolean b()
  {
    return (this.f >= 0) && (super.b());
  }
  
  public void c()
  {
    super.c();
  }
  
  public void d()
  {
    super.d();
  }
  
  public void e()
  {
    super.e();
    
    this.c.getControllerLook().a(this.b.getX() + 0.5D, this.b.getY() + 1, this.b.getZ() + 0.5D, 10.0F, this.c.bQ());
    if (f())
    {
      World ☃ = this.c.world;
      BlockPosition ☃ = this.b.up();
      
      IBlockData ☃ = ☃.getType(☃);
      Block ☃ = ☃.getBlock();
      if ((this.f == 0) && ((☃ instanceof BlockCrops)) && (((Integer)☃.get(BlockCrops.AGE)).intValue() == 7))
      {
        ☃.setAir(☃, true);
      }
      else if ((this.f == 1) && (☃ == Blocks.AIR))
      {
        InventorySubcontainer ☃ = this.c.cq();
        for (int ☃ = 0; ☃ < ☃.getSize(); ☃++)
        {
          ItemStack ☃ = ☃.getItem(☃);
          boolean ☃ = false;
          if (☃ != null) {
            if (☃.getItem() == Items.WHEAT_SEEDS)
            {
              ☃.setTypeAndData(☃, Blocks.WHEAT.getBlockData(), 3);
              ☃ = true;
            }
            else if (☃.getItem() == Items.POTATO)
            {
              ☃.setTypeAndData(☃, Blocks.POTATOES.getBlockData(), 3);
              ☃ = true;
            }
            else if (☃.getItem() == Items.CARROT)
            {
              ☃.setTypeAndData(☃, Blocks.CARROTS.getBlockData(), 3);
              ☃ = true;
            }
          }
          if (☃)
          {
            ☃.count -= 1;
            if (☃.count > 0) {
              break;
            }
            ☃.setItem(☃, null); break;
          }
        }
      }
      this.f = -1;
      
      this.a = 10;
    }
  }
  
  protected boolean a(World ☃, BlockPosition ☃)
  {
    Block ☃ = ☃.getType(☃).getBlock();
    if (☃ == Blocks.FARMLAND)
    {
      ☃ = ☃.up();
      IBlockData ☃ = ☃.getType(☃);
      ☃ = ☃.getBlock();
      if (((☃ instanceof BlockCrops)) && (((Integer)☃.get(BlockCrops.AGE)).intValue() == 7) && (this.e) && ((this.f == 0) || (this.f < 0)))
      {
        this.f = 0;
        return true;
      }
      if ((☃ == Blocks.AIR) && (this.d) && ((this.f == 1) || (this.f < 0)))
      {
        this.f = 1;
        return true;
      }
    }
    return false;
  }
}
