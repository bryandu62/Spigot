package net.minecraft.server.v1_8_R3;

import java.util.List;
import java.util.Random;

public class EntityMagmaCube
  extends EntitySlime
{
  public EntityMagmaCube(World ☃)
  {
    super(☃);
    this.fireProof = true;
  }
  
  protected void initAttributes()
  {
    super.initAttributes();
    
    getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.20000000298023224D);
  }
  
  public boolean bR()
  {
    return this.world.getDifficulty() != EnumDifficulty.PEACEFUL;
  }
  
  public boolean canSpawn()
  {
    return (this.world.a(getBoundingBox(), this)) && (this.world.getCubes(this, getBoundingBox()).isEmpty()) && (!this.world.containsLiquid(getBoundingBox()));
  }
  
  public int br()
  {
    return getSize() * 3;
  }
  
  public float c(float ☃)
  {
    return 1.0F;
  }
  
  protected EnumParticle n()
  {
    return EnumParticle.FLAME;
  }
  
  protected EntitySlime cf()
  {
    return new EntityMagmaCube(this.world);
  }
  
  protected Item getLoot()
  {
    return Items.MAGMA_CREAM;
  }
  
  protected void dropDeathLoot(boolean ☃, int ☃)
  {
    Item ☃ = getLoot();
    if ((☃ != null) && (getSize() > 1))
    {
      int ☃ = this.random.nextInt(4) - 2;
      if (☃ > 0) {
        ☃ += this.random.nextInt(☃ + 1);
      }
      for (int ☃ = 0; ☃ < ☃; ☃++) {
        a(☃, 1);
      }
    }
  }
  
  public boolean isBurning()
  {
    return false;
  }
  
  protected int cg()
  {
    return super.cg() * 4;
  }
  
  protected void ch()
  {
    this.a *= 0.9F;
  }
  
  protected void bF()
  {
    this.motY = (0.42F + getSize() * 0.1F);
    this.ai = true;
  }
  
  protected void bH()
  {
    this.motY = (0.22F + getSize() * 0.05F);
    this.ai = true;
  }
  
  public void e(float ☃, float ☃) {}
  
  protected boolean ci()
  {
    return true;
  }
  
  protected int cj()
  {
    return super.cj() + 2;
  }
  
  protected String ck()
  {
    if (getSize() > 1) {
      return "mob.magmacube.big";
    }
    return "mob.magmacube.small";
  }
  
  protected boolean cl()
  {
    return true;
  }
}
