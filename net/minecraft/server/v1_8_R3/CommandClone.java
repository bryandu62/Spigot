package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import java.util.LinkedList;
import java.util.List;

public class CommandClone
  extends CommandAbstract
{
  public String getCommand()
  {
    return "clone";
  }
  
  public int a()
  {
    return 2;
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.clone.usage";
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    if (☃.length < 9) {
      throw new ExceptionUsage("commands.clone.usage", new Object[0]);
    }
    ☃.a(CommandObjectiveExecutor.EnumCommandResult.AFFECTED_BLOCKS, 0);
    
    BlockPosition ☃ = a(☃, ☃, 0, false);
    BlockPosition ☃ = a(☃, ☃, 3, false);
    BlockPosition ☃ = a(☃, ☃, 6, false);
    
    StructureBoundingBox ☃ = new StructureBoundingBox(☃, ☃);
    StructureBoundingBox ☃ = new StructureBoundingBox(☃, ☃.a(☃.b()));
    
    int ☃ = ☃.c() * ☃.d() * ☃.e();
    if (☃ > 32768) {
      throw new CommandException("commands.clone.tooManyBlocks", new Object[] { Integer.valueOf(☃), Integer.valueOf(32768) });
    }
    boolean ☃ = false;
    Block ☃ = null;
    int ☃ = -1;
    if (((☃.length < 11) || ((!☃[10].equals("force")) && (!☃[10].equals("move")))) && (☃.a(☃))) {
      throw new CommandException("commands.clone.noOverlap", new Object[0]);
    }
    if ((☃.length >= 11) && (☃[10].equals("move"))) {
      ☃ = true;
    }
    if ((☃.b < 0) || (☃.e >= 256) || (☃.b < 0) || (☃.e >= 256)) {
      throw new CommandException("commands.clone.outOfWorld", new Object[0]);
    }
    World ☃ = ☃.getWorld();
    if ((!☃.a(☃)) || (!☃.a(☃))) {
      throw new CommandException("commands.clone.outOfWorld", new Object[0]);
    }
    boolean ☃ = false;
    if (☃.length >= 10) {
      if (☃[9].equals("masked"))
      {
        ☃ = true;
      }
      else if (☃[9].equals("filtered"))
      {
        if (☃.length >= 12) {
          ☃ = g(☃, ☃[11]);
        } else {
          throw new ExceptionUsage("commands.clone.usage", new Object[0]);
        }
        if (☃.length >= 13) {
          ☃ = a(☃[12], 0, 15);
        }
      }
    }
    List<CommandCloneStoredTileEntity> ☃ = Lists.newArrayList();
    List<CommandCloneStoredTileEntity> ☃ = Lists.newArrayList();
    List<CommandCloneStoredTileEntity> ☃ = Lists.newArrayList();
    LinkedList<BlockPosition> ☃ = Lists.newLinkedList();
    
    BlockPosition ☃ = new BlockPosition(☃.a - ☃.a, ☃.b - ☃.b, ☃.c - ☃.c);
    for (int ☃ = ☃.c; ☃ <= ☃.f; ☃++) {
      for (int ☃ = ☃.b; ☃ <= ☃.e; ☃++) {
        for (int ☃ = ☃.a; ☃ <= ☃.d; ☃++)
        {
          BlockPosition ☃ = new BlockPosition(☃, ☃, ☃);
          BlockPosition ☃ = ☃.a(☃);
          
          IBlockData ☃ = ☃.getType(☃);
          if ((!☃) || (☃.getBlock() != Blocks.AIR)) {
            if (☃ != null)
            {
              if (☃.getBlock() == ☃) {
                if ((☃ >= 0) && (☃.getBlock().toLegacyData(☃) != ☃)) {}
              }
            }
            else
            {
              TileEntity ☃ = ☃.getTileEntity(☃);
              if (☃ != null)
              {
                NBTTagCompound ☃ = new NBTTagCompound();
                ☃.b(☃);
                ☃.add(new CommandCloneStoredTileEntity(☃, ☃, ☃));
                ☃.addLast(☃);
              }
              else if ((☃.getBlock().o()) || (☃.getBlock().d()))
              {
                ☃.add(new CommandCloneStoredTileEntity(☃, ☃, null));
                ☃.addLast(☃);
              }
              else
              {
                ☃.add(new CommandCloneStoredTileEntity(☃, ☃, null));
                ☃.addFirst(☃);
              }
            }
          }
        }
      }
    }
    if (☃)
    {
      for (BlockPosition ☃ : ☃)
      {
        TileEntity ☃ = ☃.getTileEntity(☃);
        if ((☃ instanceof IInventory)) {
          ((IInventory)☃).l();
        }
        ☃.setTypeAndData(☃, Blocks.BARRIER.getBlockData(), 2);
      }
      for (BlockPosition ☃ : ☃) {
        ☃.setTypeAndData(☃, Blocks.AIR.getBlockData(), 3);
      }
    }
    List<CommandCloneStoredTileEntity> ☃ = Lists.newArrayList();
    ☃.addAll(☃);
    ☃.addAll(☃);
    ☃.addAll(☃);
    
    List<CommandCloneStoredTileEntity> ☃ = Lists.reverse(☃);
    for (CommandCloneStoredTileEntity ☃ : ☃)
    {
      TileEntity ☃ = ☃.getTileEntity(☃.a);
      if ((☃ instanceof IInventory)) {
        ((IInventory)☃).l();
      }
      ☃.setTypeAndData(☃.a, Blocks.BARRIER.getBlockData(), 2);
    }
    ☃ = 0;
    for (CommandCloneStoredTileEntity ☃ : ☃) {
      if (☃.setTypeAndData(☃.a, ☃.b, 2)) {
        ☃++;
      }
    }
    for (CommandCloneStoredTileEntity ☃ : ☃)
    {
      TileEntity ☃ = ☃.getTileEntity(☃.a);
      if ((☃.c != null) && (☃ != null))
      {
        ☃.c.setInt("x", ☃.a.getX());
        ☃.c.setInt("y", ☃.a.getY());
        ☃.c.setInt("z", ☃.a.getZ());
        ☃.a(☃.c);
        ☃.update();
      }
      ☃.setTypeAndData(☃.a, ☃.b, 2);
    }
    for (CommandCloneStoredTileEntity ☃ : ☃) {
      ☃.update(☃.a, ☃.b.getBlock());
    }
    List<NextTickListEntry> ☃ = ☃.a(☃, false);
    if (☃ != null) {
      for (NextTickListEntry ☃ : ☃) {
        if (☃.b(☃.a))
        {
          BlockPosition ☃ = ☃.a.a(☃);
          ☃.b(☃, ☃.a(), (int)(☃.b - ☃.getWorldData().getTime()), ☃.c);
        }
      }
    }
    if (☃ <= 0) {
      throw new CommandException("commands.clone.failed", new Object[0]);
    }
    ☃.a(CommandObjectiveExecutor.EnumCommandResult.AFFECTED_BLOCKS, ☃);
    a(☃, this, "commands.clone.success", new Object[] { Integer.valueOf(☃) });
  }
  
  public List<String> tabComplete(ICommandListener ☃, String[] ☃, BlockPosition ☃)
  {
    if ((☃.length > 0) && (☃.length <= 3)) {
      return a(☃, 0, ☃);
    }
    if ((☃.length > 3) && (☃.length <= 6)) {
      return a(☃, 3, ☃);
    }
    if ((☃.length > 6) && (☃.length <= 9)) {
      return a(☃, 6, ☃);
    }
    if (☃.length == 10) {
      return a(☃, new String[] { "replace", "masked", "filtered" });
    }
    if (☃.length == 11) {
      return a(☃, new String[] { "normal", "force", "move" });
    }
    if ((☃.length == 12) && ("filtered".equals(☃[9]))) {
      return a(☃, Block.REGISTRY.keySet());
    }
    return null;
  }
  
  static class CommandCloneStoredTileEntity
  {
    public final BlockPosition a;
    public final IBlockData b;
    public final NBTTagCompound c;
    
    public CommandCloneStoredTileEntity(BlockPosition ☃, IBlockData ☃, NBTTagCompound ☃)
    {
      this.a = ☃;
      this.b = ☃;
      this.c = ☃;
    }
  }
}
