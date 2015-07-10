package net.minecraft.server.v1_8_R3;

public class ItemBlock
  extends Item
{
  protected final Block a;
  
  public ItemBlock(Block ☃)
  {
    this.a = ☃;
  }
  
  public ItemBlock b(String ☃)
  {
    super.c(☃);
    return this;
  }
  
  public boolean interactWith(ItemStack ☃, EntityHuman ☃, World ☃, BlockPosition ☃, EnumDirection ☃, float ☃, float ☃, float ☃)
  {
    IBlockData ☃ = ☃.getType(☃);
    Block ☃ = ☃.getBlock();
    if (!☃.a(☃, ☃)) {
      ☃ = ☃.shift(☃);
    }
    if (☃.count == 0) {
      return false;
    }
    if (!☃.a(☃, ☃, ☃)) {
      return false;
    }
    if (☃.a(this.a, ☃, false, ☃, null, ☃))
    {
      int ☃ = filterData(☃.getData());
      IBlockData ☃ = this.a.getPlacedState(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
      if (☃.setTypeAndData(☃, ☃, 3))
      {
        ☃ = ☃.getType(☃);
        if (☃.getBlock() == this.a)
        {
          a(☃, ☃, ☃, ☃);
          this.a.postPlace(☃, ☃, ☃, ☃, ☃);
        }
        ☃.makeSound(☃.getX() + 0.5F, ☃.getY() + 0.5F, ☃.getZ() + 0.5F, this.a.stepSound.getPlaceSound(), (this.a.stepSound.getVolume1() + 1.0F) / 2.0F, this.a.stepSound.getVolume2() * 0.8F);
        ☃.count -= 1;
      }
      return true;
    }
    return false;
  }
  
  public static boolean a(World ☃, EntityHuman ☃, BlockPosition ☃, ItemStack ☃)
  {
    MinecraftServer ☃ = MinecraftServer.getServer();
    if (☃ == null) {
      return false;
    }
    if ((☃.hasTag()) && (☃.getTag().hasKeyOfType("BlockEntityTag", 10)))
    {
      TileEntity ☃ = ☃.getTileEntity(☃);
      if (☃ != null)
      {
        if ((!☃.isClientSide) && (☃.F()) && (!☃.getPlayerList().isOp(☃.getProfile()))) {
          return false;
        }
        NBTTagCompound ☃ = new NBTTagCompound();
        NBTTagCompound ☃ = (NBTTagCompound)☃.clone();
        ☃.b(☃);
        
        NBTTagCompound ☃ = (NBTTagCompound)☃.getTag().get("BlockEntityTag");
        ☃.a(☃);
        ☃.setInt("x", ☃.getX());
        ☃.setInt("y", ☃.getY());
        ☃.setInt("z", ☃.getZ());
        if (!☃.equals(☃))
        {
          ☃.a(☃);
          ☃.update();
          return true;
        }
      }
    }
    return false;
  }
  
  public String e_(ItemStack ☃)
  {
    return this.a.a();
  }
  
  public String getName()
  {
    return this.a.a();
  }
  
  public Block d()
  {
    return this.a;
  }
}
