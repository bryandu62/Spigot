package net.minecraft.server.v1_8_R3;

public class ItemBanner
  extends ItemBlock
{
  public ItemBanner()
  {
    super(Blocks.STANDING_BANNER);
    this.maxStackSize = 16;
    a(CreativeModeTab.c);
    a(true);
    setMaxDurability(0);
  }
  
  public boolean interactWith(ItemStack ☃, EntityHuman ☃, World ☃, BlockPosition ☃, EnumDirection ☃, float ☃, float ☃, float ☃)
  {
    if (☃ == EnumDirection.DOWN) {
      return false;
    }
    if (!☃.getType(☃).getBlock().getMaterial().isBuildable()) {
      return false;
    }
    ☃ = ☃.shift(☃);
    if (!☃.a(☃, ☃, ☃)) {
      return false;
    }
    if (!Blocks.STANDING_BANNER.canPlace(☃, ☃)) {
      return false;
    }
    if (☃.isClientSide) {
      return true;
    }
    if (☃ == EnumDirection.UP)
    {
      int ☃ = MathHelper.floor((☃.yaw + 180.0F) * 16.0F / 360.0F + 0.5D) & 0xF;
      ☃.setTypeAndData(☃, Blocks.STANDING_BANNER.getBlockData().set(BlockFloorSign.ROTATION, Integer.valueOf(☃)), 3);
    }
    else
    {
      ☃.setTypeAndData(☃, Blocks.WALL_BANNER.getBlockData().set(BlockWallSign.FACING, ☃), 3);
    }
    ☃.count -= 1;
    TileEntity ☃ = ☃.getTileEntity(☃);
    if ((☃ instanceof TileEntityBanner)) {
      ((TileEntityBanner)☃).a(☃);
    }
    return true;
  }
  
  public String a(ItemStack ☃)
  {
    String ☃ = "item.banner.";
    
    EnumColor ☃ = h(☃);
    ☃ = ☃ + ☃.d() + ".name";
    return LocaleI18n.get(☃);
  }
  
  private EnumColor h(ItemStack ☃)
  {
    NBTTagCompound ☃ = ☃.a("BlockEntityTag", false);
    EnumColor ☃ = null;
    if ((☃ != null) && (☃.hasKey("Base"))) {
      ☃ = EnumColor.fromInvColorIndex(☃.getInt("Base"));
    } else {
      ☃ = EnumColor.fromInvColorIndex(☃.getData());
    }
    return ☃;
  }
}
