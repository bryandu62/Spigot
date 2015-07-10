package net.minecraft.server.v1_8_R3;

import com.google.common.base.Predicate;
import com.mojang.authlib.GameProfile;

public class ItemSkull
  extends Item
{
  private static final String[] a = { "skeleton", "wither", "zombie", "char", "creeper" };
  
  public ItemSkull()
  {
    a(CreativeModeTab.c);
    setMaxDurability(0);
    a(true);
  }
  
  public boolean interactWith(ItemStack itemstack, EntityHuman entityhuman, World world, BlockPosition blockposition, EnumDirection enumdirection, float f, float f1, float f2)
  {
    if (enumdirection == EnumDirection.DOWN) {
      return false;
    }
    IBlockData iblockdata = world.getType(blockposition);
    Block block = iblockdata.getBlock();
    boolean flag = block.a(world, blockposition);
    if (!flag)
    {
      if (!world.getType(blockposition).getBlock().getMaterial().isBuildable()) {
        return false;
      }
      blockposition = blockposition.shift(enumdirection);
    }
    if (!entityhuman.a(blockposition, enumdirection, itemstack)) {
      return false;
    }
    if (!Blocks.SKULL.canPlace(world, blockposition)) {
      return false;
    }
    if (!world.isClientSide)
    {
      if (!Blocks.SKULL.canPlace(world, blockposition)) {
        return false;
      }
      world.setTypeAndData(blockposition, Blocks.SKULL.getBlockData().set(BlockSkull.FACING, enumdirection), 3);
      int i = 0;
      if (enumdirection == EnumDirection.UP) {
        i = MathHelper.floor(entityhuman.yaw * 16.0F / 360.0F + 0.5D) & 0xF;
      }
      TileEntity tileentity = world.getTileEntity(blockposition);
      if ((tileentity instanceof TileEntitySkull))
      {
        TileEntitySkull tileentityskull = (TileEntitySkull)tileentity;
        if (itemstack.getData() == 3)
        {
          GameProfile gameprofile = null;
          if (itemstack.hasTag())
          {
            NBTTagCompound nbttagcompound = itemstack.getTag();
            if (nbttagcompound.hasKeyOfType("SkullOwner", 10)) {
              gameprofile = GameProfileSerializer.deserialize(nbttagcompound.getCompound("SkullOwner"));
            } else if ((nbttagcompound.hasKeyOfType("SkullOwner", 8)) && (nbttagcompound.getString("SkullOwner").length() > 0)) {
              gameprofile = new GameProfile(null, nbttagcompound.getString("SkullOwner"));
            }
          }
          tileentityskull.setGameProfile(gameprofile);
        }
        else
        {
          tileentityskull.setSkullType(itemstack.getData());
        }
        tileentityskull.setRotation(i);
        Blocks.SKULL.a(world, blockposition, tileentityskull);
      }
      itemstack.count -= 1;
    }
    return true;
  }
  
  public int filterData(int i)
  {
    return i;
  }
  
  public String e_(ItemStack itemstack)
  {
    int i = itemstack.getData();
    if ((i < 0) || (i >= a.length)) {
      i = 0;
    }
    return super.getName() + "." + a[i];
  }
  
  public String a(ItemStack itemstack)
  {
    if ((itemstack.getData() == 3) && (itemstack.hasTag()))
    {
      if (itemstack.getTag().hasKeyOfType("SkullOwner", 8)) {
        return LocaleI18n.a("item.skull.player.name", new Object[] { itemstack.getTag().getString("SkullOwner") });
      }
      if (itemstack.getTag().hasKeyOfType("SkullOwner", 10))
      {
        NBTTagCompound nbttagcompound = itemstack.getTag().getCompound("SkullOwner");
        if (nbttagcompound.hasKeyOfType("Name", 8)) {
          return LocaleI18n.a("item.skull.player.name", new Object[] { nbttagcompound.getString("Name") });
        }
      }
    }
    return super.a(itemstack);
  }
  
  public boolean a(final NBTTagCompound nbttagcompound)
  {
    super.a(nbttagcompound);
    if ((nbttagcompound.hasKeyOfType("SkullOwner", 8)) && (nbttagcompound.getString("SkullOwner").length() > 0))
    {
      GameProfile gameprofile = new GameProfile(null, nbttagcompound.getString("SkullOwner"));
      
      TileEntitySkull.b(gameprofile, new Predicate()
      {
        public boolean apply(GameProfile gameprofile)
        {
          nbttagcompound.set("SkullOwner", GameProfileSerializer.serialize(new NBTTagCompound(), gameprofile));
          return false;
        }
      });
      return true;
    }
    return false;
  }
}
