package net.minecraft.server.v1_8_R3;

public abstract class EntityGolem
  extends EntityCreature
  implements IAnimal
{
  public EntityGolem(World ☃)
  {
    super(☃);
  }
  
  public void e(float ☃, float ☃) {}
  
  protected String z()
  {
    return "none";
  }
  
  protected String bo()
  {
    return "none";
  }
  
  protected String bp()
  {
    return "none";
  }
  
  public int w()
  {
    return 120;
  }
  
  protected boolean isTypeNotPersistent()
  {
    return false;
  }
}
