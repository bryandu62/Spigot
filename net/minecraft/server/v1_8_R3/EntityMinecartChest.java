package net.minecraft.server.v1_8_R3;

public class EntityMinecartChest
  extends EntityMinecartContainer
{
  public EntityMinecartChest(World ☃)
  {
    super(☃);
  }
  
  public EntityMinecartChest(World ☃, double ☃, double ☃, double ☃)
  {
    super(☃, ☃, ☃, ☃);
  }
  
  public void a(DamageSource ☃)
  {
    super.a(☃);
    if (this.world.getGameRules().getBoolean("doEntityDrops")) {
      a(Item.getItemOf(Blocks.CHEST), 1, 0.0F);
    }
  }
  
  public int getSize()
  {
    return 27;
  }
  
  public EntityMinecartAbstract.EnumMinecartType s()
  {
    return EntityMinecartAbstract.EnumMinecartType.CHEST;
  }
  
  public IBlockData u()
  {
    return Blocks.CHEST.getBlockData().set(BlockChest.FACING, EnumDirection.NORTH);
  }
  
  public int w()
  {
    return 8;
  }
  
  public String getContainerName()
  {
    return "minecraft:chest";
  }
  
  public Container createContainer(PlayerInventory ☃, EntityHuman ☃)
  {
    return new ContainerChest(☃, this, ☃);
  }
}
