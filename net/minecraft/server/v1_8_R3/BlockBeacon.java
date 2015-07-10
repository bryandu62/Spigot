package net.minecraft.server.v1_8_R3;

import com.google.common.util.concurrent.ListeningExecutorService;

public class BlockBeacon
  extends BlockContainer
{
  public BlockBeacon()
  {
    super(Material.SHATTERABLE, MaterialMapColor.G);
    c(3.0F);
    a(CreativeModeTab.f);
  }
  
  public TileEntity a(World ☃, int ☃)
  {
    return new TileEntityBeacon();
  }
  
  public boolean interact(World ☃, BlockPosition ☃, IBlockData ☃, EntityHuman ☃, EnumDirection ☃, float ☃, float ☃, float ☃)
  {
    if (☃.isClientSide) {
      return true;
    }
    TileEntity ☃ = ☃.getTileEntity(☃);
    if ((☃ instanceof TileEntityBeacon))
    {
      ☃.openContainer((TileEntityBeacon)☃);
      ☃.b(StatisticList.N);
    }
    return true;
  }
  
  public boolean c()
  {
    return false;
  }
  
  public boolean d()
  {
    return false;
  }
  
  public int b()
  {
    return 3;
  }
  
  public void postPlace(World ☃, BlockPosition ☃, IBlockData ☃, EntityLiving ☃, ItemStack ☃)
  {
    super.postPlace(☃, ☃, ☃, ☃, ☃);
    if (☃.hasName())
    {
      TileEntity ☃ = ☃.getTileEntity(☃);
      if ((☃ instanceof TileEntityBeacon)) {
        ((TileEntityBeacon)☃).a(☃.getName());
      }
    }
  }
  
  public void doPhysics(World ☃, BlockPosition ☃, IBlockData ☃, Block ☃)
  {
    TileEntity ☃ = ☃.getTileEntity(☃);
    if ((☃ instanceof TileEntityBeacon))
    {
      ((TileEntityBeacon)☃).m();
      ☃.playBlockAction(☃, this, 1, 0);
    }
  }
  
  public static void f(World ☃, final BlockPosition ☃)
  {
    HttpUtilities.a.submit(new Runnable()
    {
      public void run()
      {
        Chunk ☃ = this.a.getChunkAtWorldCoords(☃);
        for (int ☃ = ☃.getY() - 1; ☃ >= 0; ☃--)
        {
          final BlockPosition ☃ = new BlockPosition(☃.getX(), ☃, ☃.getZ());
          if (!☃.d(☃)) {
            break;
          }
          IBlockData ☃ = this.a.getType(☃);
          if (☃.getBlock() == Blocks.BEACON) {
            ((WorldServer)this.a).postToMainThread(new Runnable()
            {
              public void run()
              {
                TileEntity ☃ = BlockBeacon.1.this.a.getTileEntity(☃);
                if ((☃ instanceof TileEntityBeacon))
                {
                  ((TileEntityBeacon)☃).m();
                  BlockBeacon.1.this.a.playBlockAction(☃, Blocks.BEACON, 1, 0);
                }
              }
            });
          }
        }
      }
    });
  }
}
