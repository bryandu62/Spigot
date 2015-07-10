package net.minecraft.server.v1_8_R3;

public class BlockWorkbench
  extends Block
{
  protected BlockWorkbench()
  {
    super(Material.WOOD);
    a(CreativeModeTab.c);
  }
  
  public boolean interact(World ☃, BlockPosition ☃, IBlockData ☃, EntityHuman ☃, EnumDirection ☃, float ☃, float ☃, float ☃)
  {
    if (☃.isClientSide) {
      return true;
    }
    ☃.openTileEntity(new TileEntityContainerWorkbench(☃, ☃));
    ☃.b(StatisticList.Z);
    return true;
  }
  
  public static class TileEntityContainerWorkbench
    implements ITileEntityContainer
  {
    private final World a;
    private final BlockPosition b;
    
    public TileEntityContainerWorkbench(World ☃, BlockPosition ☃)
    {
      this.a = ☃;
      this.b = ☃;
    }
    
    public String getName()
    {
      return null;
    }
    
    public boolean hasCustomName()
    {
      return false;
    }
    
    public IChatBaseComponent getScoreboardDisplayName()
    {
      return new ChatMessage(Blocks.CRAFTING_TABLE.a() + ".name", new Object[0]);
    }
    
    public Container createContainer(PlayerInventory ☃, EntityHuman ☃)
    {
      return new ContainerWorkbench(☃, this.a, this.b);
    }
    
    public String getContainerName()
    {
      return "minecraft:crafting_table";
    }
  }
}
