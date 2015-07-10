package net.minecraft.server.v1_8_R3;

import java.util.Random;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

public class ItemBucket
  extends Item
{
  private Block a;
  
  public ItemBucket(Block block)
  {
    this.maxStackSize = 1;
    this.a = block;
    a(CreativeModeTab.f);
  }
  
  public ItemStack a(ItemStack itemstack, World world, EntityHuman entityhuman)
  {
    boolean flag = this.a == Blocks.AIR;
    MovingObjectPosition movingobjectposition = a(world, entityhuman, flag);
    if (movingobjectposition == null) {
      return itemstack;
    }
    if (movingobjectposition.type == MovingObjectPosition.EnumMovingObjectType.BLOCK)
    {
      BlockPosition blockposition = movingobjectposition.a();
      if (!world.a(entityhuman, blockposition)) {
        return itemstack;
      }
      if (flag)
      {
        if (!entityhuman.a(blockposition.shift(movingobjectposition.direction), movingobjectposition.direction, itemstack)) {
          return itemstack;
        }
        IBlockData iblockdata = world.getType(blockposition);
        Material material = iblockdata.getBlock().getMaterial();
        if ((material == Material.WATER) && (((Integer)iblockdata.get(BlockFluids.LEVEL)).intValue() == 0))
        {
          PlayerBucketFillEvent event = CraftEventFactory.callPlayerBucketFillEvent(entityhuman, blockposition.getX(), blockposition.getY(), blockposition.getZ(), null, itemstack, Items.WATER_BUCKET);
          if (event.isCancelled()) {
            return itemstack;
          }
          world.setAir(blockposition);
          entityhuman.b(StatisticList.USE_ITEM_COUNT[Item.getId(this)]);
          return a(itemstack, entityhuman, Items.WATER_BUCKET, event.getItemStack());
        }
        if ((material == Material.LAVA) && (((Integer)iblockdata.get(BlockFluids.LEVEL)).intValue() == 0))
        {
          PlayerBucketFillEvent event = CraftEventFactory.callPlayerBucketFillEvent(entityhuman, blockposition.getX(), blockposition.getY(), blockposition.getZ(), null, itemstack, Items.LAVA_BUCKET);
          if (event.isCancelled()) {
            return itemstack;
          }
          world.setAir(blockposition);
          entityhuman.b(StatisticList.USE_ITEM_COUNT[Item.getId(this)]);
          return a(itemstack, entityhuman, Items.LAVA_BUCKET, event.getItemStack());
        }
      }
      else
      {
        if (this.a == Blocks.AIR)
        {
          PlayerBucketEmptyEvent event = CraftEventFactory.callPlayerBucketEmptyEvent(entityhuman, blockposition.getX(), blockposition.getY(), blockposition.getZ(), movingobjectposition.direction, itemstack);
          if (event.isCancelled()) {
            return itemstack;
          }
          return CraftItemStack.asNMSCopy(event.getItemStack());
        }
        BlockPosition blockposition1 = blockposition.shift(movingobjectposition.direction);
        if (!entityhuman.a(blockposition1, movingobjectposition.direction, itemstack)) {
          return itemstack;
        }
        PlayerBucketEmptyEvent event = CraftEventFactory.callPlayerBucketEmptyEvent(entityhuman, blockposition.getX(), blockposition.getY(), blockposition.getZ(), movingobjectposition.direction, itemstack);
        if (event.isCancelled()) {
          return itemstack;
        }
        if ((a(world, blockposition1)) && (!entityhuman.abilities.canInstantlyBuild))
        {
          entityhuman.b(StatisticList.USE_ITEM_COUNT[Item.getId(this)]);
          return CraftItemStack.asNMSCopy(event.getItemStack());
        }
      }
    }
    return itemstack;
  }
  
  private ItemStack a(ItemStack itemstack, EntityHuman entityhuman, Item item, org.bukkit.inventory.ItemStack result)
  {
    if (entityhuman.abilities.canInstantlyBuild) {
      return itemstack;
    }
    if (--itemstack.count <= 0) {
      return CraftItemStack.asNMSCopy(result);
    }
    if (!entityhuman.inventory.pickup(CraftItemStack.asNMSCopy(result))) {
      entityhuman.drop(CraftItemStack.asNMSCopy(result), false);
    }
    return itemstack;
  }
  
  public boolean a(World world, BlockPosition blockposition)
  {
    if (this.a == Blocks.AIR) {
      return false;
    }
    Material material = world.getType(blockposition).getBlock().getMaterial();
    boolean flag = !material.isBuildable();
    if ((!world.isEmpty(blockposition)) && (!flag)) {
      return false;
    }
    if ((world.worldProvider.n()) && (this.a == Blocks.FLOWING_WATER))
    {
      int i = blockposition.getX();
      int j = blockposition.getY();
      int k = blockposition.getZ();
      
      world.makeSound(i + 0.5F, j + 0.5F, k + 0.5F, "random.fizz", 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);
      for (int l = 0; l < 8; l++) {
        world.addParticle(EnumParticle.SMOKE_LARGE, i + Math.random(), j + Math.random(), k + Math.random(), 0.0D, 0.0D, 0.0D, new int[0]);
      }
    }
    else
    {
      if ((!world.isClientSide) && (flag) && (!material.isLiquid())) {
        world.setAir(blockposition, true);
      }
      world.setTypeAndData(blockposition, this.a.getBlockData(), 3);
    }
    return true;
  }
}
