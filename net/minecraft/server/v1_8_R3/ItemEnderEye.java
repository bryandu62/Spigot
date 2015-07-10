package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class ItemEnderEye
  extends Item
{
  public ItemEnderEye()
  {
    a(CreativeModeTab.f);
  }
  
  public boolean interactWith(ItemStack ☃, EntityHuman ☃, World ☃, BlockPosition ☃, EnumDirection ☃, float ☃, float ☃, float ☃)
  {
    IBlockData ☃ = ☃.getType(☃);
    if ((☃.a(☃.shift(☃), ☃, ☃)) && (☃.getBlock() == Blocks.END_PORTAL_FRAME) && (!((Boolean)☃.get(BlockEnderPortalFrame.EYE)).booleanValue()))
    {
      if (☃.isClientSide) {
        return true;
      }
      ☃.setTypeAndData(☃, ☃.set(BlockEnderPortalFrame.EYE, Boolean.valueOf(true)), 2);
      ☃.updateAdjacentComparators(☃, Blocks.END_PORTAL_FRAME);
      ☃.count -= 1;
      for (int ☃ = 0; ☃ < 16; ☃++)
      {
        double ☃ = ☃.getX() + (5.0F + g.nextFloat() * 6.0F) / 16.0F;
        double ☃ = ☃.getY() + 0.8125F;
        double ☃ = ☃.getZ() + (5.0F + g.nextFloat() * 6.0F) / 16.0F;
        double ☃ = 0.0D;
        double ☃ = 0.0D;
        double ☃ = 0.0D;
        
        ☃.addParticle(EnumParticle.SMOKE_NORMAL, ☃, ☃, ☃, ☃, ☃, ☃, new int[0]);
      }
      EnumDirection ☃ = (EnumDirection)☃.get(BlockEnderPortalFrame.FACING);
      
      int ☃ = 0;
      int ☃ = 0;
      boolean ☃ = false;
      boolean ☃ = true;
      EnumDirection ☃ = ☃.e();
      for (int ☃ = -2; ☃ <= 2; ☃++)
      {
        BlockPosition ☃ = ☃.shift(☃, ☃);
        IBlockData ☃ = ☃.getType(☃);
        if (☃.getBlock() == Blocks.END_PORTAL_FRAME)
        {
          if (!((Boolean)☃.get(BlockEnderPortalFrame.EYE)).booleanValue())
          {
            ☃ = false;
            break;
          }
          ☃ = ☃;
          if (!☃)
          {
            ☃ = ☃;
            ☃ = true;
          }
        }
      }
      if ((☃) && (☃ == ☃ + 2))
      {
        BlockPosition ☃ = ☃.shift(☃, 4);
        for (int ☃ = ☃; ☃ <= ☃; ☃++)
        {
          BlockPosition ☃ = ☃.shift(☃, ☃);
          IBlockData ☃ = ☃.getType(☃);
          if ((☃.getBlock() != Blocks.END_PORTAL_FRAME) || (!((Boolean)☃.get(BlockEnderPortalFrame.EYE)).booleanValue()))
          {
            ☃ = false;
            break;
          }
        }
        for (int ☃ = ☃ - 1; ☃ <= ☃ + 1; ☃ += 4)
        {
          ☃ = ☃.shift(☃, ☃);
          for (int ☃ = 1; ☃ <= 3; ☃++)
          {
            BlockPosition ☃ = ☃.shift(☃, ☃);
            IBlockData ☃ = ☃.getType(☃);
            if ((☃.getBlock() != Blocks.END_PORTAL_FRAME) || (!((Boolean)☃.get(BlockEnderPortalFrame.EYE)).booleanValue()))
            {
              ☃ = false;
              break;
            }
          }
        }
        if (☃) {
          for (int ☃ = ☃; ☃ <= ☃; ☃++)
          {
            ☃ = ☃.shift(☃, ☃);
            for (int ☃ = 1; ☃ <= 3; ☃++)
            {
              BlockPosition ☃ = ☃.shift(☃, ☃);
              
              ☃.setTypeAndData(☃, Blocks.END_PORTAL.getBlockData(), 2);
            }
          }
        }
      }
      return true;
    }
    return false;
  }
  
  public ItemStack a(ItemStack ☃, World ☃, EntityHuman ☃)
  {
    MovingObjectPosition ☃ = a(☃, ☃, false);
    if ((☃ != null) && (☃.type == MovingObjectPosition.EnumMovingObjectType.BLOCK) && 
      (☃.getType(☃.a()).getBlock() == Blocks.END_PORTAL_FRAME)) {
      return ☃;
    }
    if (!☃.isClientSide)
    {
      BlockPosition ☃ = ☃.a("Stronghold", new BlockPosition(☃));
      if (☃ != null)
      {
        EntityEnderSignal ☃ = new EntityEnderSignal(☃, ☃.locX, ☃.locY, ☃.locZ);
        ☃.a(☃);
        ☃.addEntity(☃);
        
        ☃.makeSound(☃, "random.bow", 0.5F, 0.4F / (g.nextFloat() * 0.4F + 0.8F));
        ☃.a(null, 1002, new BlockPosition(☃), 0);
        if (!☃.abilities.canInstantlyBuild) {
          ☃.count -= 1;
        }
        ☃.b(StatisticList.USE_ITEM_COUNT[Item.getId(this)]);
      }
    }
    return ☃;
  }
}
