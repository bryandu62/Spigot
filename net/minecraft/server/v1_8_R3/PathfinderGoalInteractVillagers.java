package net.minecraft.server.v1_8_R3;

public class PathfinderGoalInteractVillagers
  extends PathfinderGoalInteract
{
  private int e;
  private EntityVillager f;
  
  public PathfinderGoalInteractVillagers(EntityVillager ☃)
  {
    super(☃, EntityVillager.class, 3.0F, 0.02F);
    this.f = ☃;
  }
  
  public void c()
  {
    super.c();
    if ((this.f.cs()) && ((this.b instanceof EntityVillager)) && (((EntityVillager)this.b).ct())) {
      this.e = 10;
    } else {
      this.e = 0;
    }
  }
  
  public void e()
  {
    super.e();
    if (this.e > 0)
    {
      this.e -= 1;
      if (this.e == 0)
      {
        InventorySubcontainer ☃ = this.f.cq();
        for (int ☃ = 0; ☃ < ☃.getSize(); ☃++)
        {
          ItemStack ☃ = ☃.getItem(☃);
          ItemStack ☃ = null;
          if (☃ != null)
          {
            Item ☃ = ☃.getItem();
            if (((☃ == Items.BREAD) || (☃ == Items.POTATO) || (☃ == Items.CARROT)) && (☃.count > 3))
            {
              int ☃ = ☃.count / 2;
              ☃.count -= ☃;
              ☃ = new ItemStack(☃, ☃, ☃.getData());
            }
            else if ((☃ == Items.WHEAT) && (☃.count > 5))
            {
              int ☃ = ☃.count / 2 / 3 * 3;
              int ☃ = ☃ / 3;
              ☃.count -= ☃;
              ☃ = new ItemStack(Items.BREAD, ☃, 0);
            }
            if (☃.count <= 0) {
              ☃.setItem(☃, null);
            }
          }
          if (☃ != null)
          {
            double ☃ = this.f.locY - 0.30000001192092896D + this.f.getHeadHeight();
            EntityItem ☃ = new EntityItem(this.f.world, this.f.locX, ☃, this.f.locZ, ☃);
            float ☃ = 0.3F;
            float ☃ = this.f.aK;
            float ☃ = this.f.pitch;
            ☃.motX = (-MathHelper.sin(☃ / 180.0F * 3.1415927F) * MathHelper.cos(☃ / 180.0F * 3.1415927F) * ☃);
            ☃.motZ = (MathHelper.cos(☃ / 180.0F * 3.1415927F) * MathHelper.cos(☃ / 180.0F * 3.1415927F) * ☃);
            ☃.motY = (-MathHelper.sin(☃ / 180.0F * 3.1415927F) * ☃ + 0.1F);
            ☃.p();
            
            this.f.world.addEntity(☃);
            
            break;
          }
        }
      }
    }
  }
}
