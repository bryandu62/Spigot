package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import java.util.List;

public class CommandFill
  extends CommandAbstract
{
  public String getCommand()
  {
    return "fill";
  }
  
  public int a()
  {
    return 2;
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.fill.usage";
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    if (☃.length < 7) {
      throw new ExceptionUsage("commands.fill.usage", new Object[0]);
    }
    ☃.a(CommandObjectiveExecutor.EnumCommandResult.AFFECTED_BLOCKS, 0);
    
    BlockPosition ☃ = a(☃, ☃, 0, false);
    BlockPosition ☃ = a(☃, ☃, 3, false);
    Block ☃ = CommandAbstract.g(☃, ☃[6]);
    
    int ☃ = 0;
    if (☃.length >= 8) {
      ☃ = a(☃[7], 0, 15);
    }
    BlockPosition ☃ = new BlockPosition(Math.min(☃.getX(), ☃.getX()), Math.min(☃.getY(), ☃.getY()), Math.min(☃.getZ(), ☃.getZ()));
    BlockPosition ☃ = new BlockPosition(Math.max(☃.getX(), ☃.getX()), Math.max(☃.getY(), ☃.getY()), Math.max(☃.getZ(), ☃.getZ()));
    
    int ☃ = (☃.getX() - ☃.getX() + 1) * (☃.getY() - ☃.getY() + 1) * (☃.getZ() - ☃.getZ() + 1);
    if (☃ > 32768) {
      throw new CommandException("commands.fill.tooManyBlocks", new Object[] { Integer.valueOf(☃), Integer.valueOf(32768) });
    }
    if ((☃.getY() < 0) || (☃.getY() >= 256)) {
      throw new CommandException("commands.fill.outOfWorld", new Object[0]);
    }
    World ☃ = ☃.getWorld();
    for (int ☃ = ☃.getZ(); ☃ < ☃.getZ() + 16; ☃ += 16) {
      for (int ☃ = ☃.getX(); ☃ < ☃.getX() + 16; ☃ += 16) {
        if (!☃.isLoaded(new BlockPosition(☃, ☃.getY() - ☃.getY(), ☃))) {
          throw new CommandException("commands.fill.outOfWorld", new Object[0]);
        }
      }
    }
    NBTTagCompound ☃ = new NBTTagCompound();
    boolean ☃ = false;
    if ((☃.length >= 10) && (☃.isTileEntity()))
    {
      String ☃ = a(☃, ☃, 9).c();
      try
      {
        ☃ = MojangsonParser.parse(☃);
        ☃ = true;
      }
      catch (MojangsonParseException ☃)
      {
        throw new CommandException("commands.fill.tagError", new Object[] { ☃.getMessage() });
      }
    }
    List<BlockPosition> ☃ = Lists.newArrayList();
    
    ☃ = 0;
    for (int ☃ = ☃.getZ(); ☃ <= ☃.getZ(); ☃++) {
      for (int ☃ = ☃.getY(); ☃ <= ☃.getY(); ☃++) {
        for (int ☃ = ☃.getX(); ☃ <= ☃.getX(); ☃++)
        {
          BlockPosition ☃ = new BlockPosition(☃, ☃, ☃);
          if (☃.length >= 9) {
            if ((☃[8].equals("outline")) || (☃[8].equals("hollow")))
            {
              if ((☃ != ☃.getX()) && (☃ != ☃.getX()) && (☃ != ☃.getY()) && (☃ != ☃.getY()) && (☃ != ☃.getZ()) && (☃ != ☃.getZ()))
              {
                if (!☃[8].equals("hollow")) {
                  continue;
                }
                ☃.setTypeAndData(☃, Blocks.AIR.getBlockData(), 2);
                ☃.add(☃); continue;
              }
            }
            else if (☃[8].equals("destroy")) {
              ☃.setAir(☃, true);
            } else if (☃[8].equals("keep"))
            {
              if (!☃.isEmpty(☃)) {
                continue;
              }
            }
            else if ((☃[8].equals("replace")) && (!☃.isTileEntity())) {
              if (☃.length > 9)
              {
                Block ☃ = CommandAbstract.g(☃, ☃[9]);
                if (☃.getType(☃).getBlock() != ☃) {}
              }
              else if (☃.length > 10)
              {
                int ☃ = CommandAbstract.a(☃[10]);
                IBlockData ☃ = ☃.getType(☃);
                if (☃.getBlock().toLegacyData(☃) != ☃) {
                  continue;
                }
              }
            }
          }
          TileEntity ☃ = ☃.getTileEntity(☃);
          if (☃ != null)
          {
            if ((☃ instanceof IInventory)) {
              ((IInventory)☃).l();
            }
            ☃.setTypeAndData(☃, Blocks.BARRIER.getBlockData(), ☃ == Blocks.BARRIER ? 2 : 4);
          }
          IBlockData ☃ = ☃.fromLegacyData(☃);
          if (☃.setTypeAndData(☃, ☃, 2))
          {
            ☃.add(☃);
            ☃++;
            if (☃)
            {
              TileEntity ☃ = ☃.getTileEntity(☃);
              if (☃ != null)
              {
                ☃.setInt("x", ☃.getX());
                ☃.setInt("y", ☃.getY());
                ☃.setInt("z", ☃.getZ());
                
                ☃.a(☃);
              }
            }
          }
        }
      }
    }
    for (BlockPosition ☃ : ☃)
    {
      Block ☃ = ☃.getType(☃).getBlock();
      ☃.update(☃, ☃);
    }
    if (☃ <= 0) {
      throw new CommandException("commands.fill.failed", new Object[0]);
    }
    ☃.a(CommandObjectiveExecutor.EnumCommandResult.AFFECTED_BLOCKS, ☃);
    a(☃, this, "commands.fill.success", new Object[] { Integer.valueOf(☃) });
  }
  
  public List<String> tabComplete(ICommandListener ☃, String[] ☃, BlockPosition ☃)
  {
    if ((☃.length > 0) && (☃.length <= 3)) {
      return a(☃, 0, ☃);
    }
    if ((☃.length > 3) && (☃.length <= 6)) {
      return a(☃, 3, ☃);
    }
    if (☃.length == 7) {
      return a(☃, Block.REGISTRY.keySet());
    }
    if (☃.length == 9) {
      return a(☃, new String[] { "replace", "destroy", "keep", "hollow", "outline" });
    }
    if ((☃.length == 10) && ("replace".equals(☃[8]))) {
      return a(☃, Block.REGISTRY.keySet());
    }
    return null;
  }
}
