package net.minecraft.server.v1_8_R3;

public class PlayerAbilities
{
  public boolean isInvulnerable;
  public boolean isFlying;
  public boolean canFly;
  public boolean canInstantlyBuild;
  public boolean mayBuild = true;
  public float flySpeed = 0.05F;
  public float walkSpeed = 0.1F;
  
  public void a(NBTTagCompound ☃)
  {
    NBTTagCompound ☃ = new NBTTagCompound();
    
    ☃.setBoolean("invulnerable", this.isInvulnerable);
    ☃.setBoolean("flying", this.isFlying);
    ☃.setBoolean("mayfly", this.canFly);
    ☃.setBoolean("instabuild", this.canInstantlyBuild);
    ☃.setBoolean("mayBuild", this.mayBuild);
    ☃.setFloat("flySpeed", this.flySpeed);
    ☃.setFloat("walkSpeed", this.walkSpeed);
    ☃.set("abilities", ☃);
  }
  
  public void b(NBTTagCompound ☃)
  {
    if (☃.hasKeyOfType("abilities", 10))
    {
      NBTTagCompound ☃ = ☃.getCompound("abilities");
      
      this.isInvulnerable = ☃.getBoolean("invulnerable");
      this.isFlying = ☃.getBoolean("flying");
      this.canFly = ☃.getBoolean("mayfly");
      this.canInstantlyBuild = ☃.getBoolean("instabuild");
      if (☃.hasKeyOfType("flySpeed", 99))
      {
        this.flySpeed = ☃.getFloat("flySpeed");
        this.walkSpeed = ☃.getFloat("walkSpeed");
      }
      if (☃.hasKeyOfType("mayBuild", 1)) {
        this.mayBuild = ☃.getBoolean("mayBuild");
      }
    }
  }
  
  public float a()
  {
    return this.flySpeed;
  }
  
  public float b()
  {
    return this.walkSpeed;
  }
}
