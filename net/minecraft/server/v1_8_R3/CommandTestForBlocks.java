package net.minecraft.server.v1_8_R3;

import java.util.List;

public class CommandTestForBlocks
  extends CommandAbstract
{
  public String getCommand()
  {
    return "testforblocks";
  }
  
  public int a()
  {
    return 2;
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.compare.usage";
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    if (☃.length < 9) {
      throw new ExceptionUsage("commands.compare.usage", new Object[0]);
    }
    ☃.a(CommandObjectiveExecutor.EnumCommandResult.AFFECTED_BLOCKS, 0);
    
    BlockPosition ☃ = a(☃, ☃, 0, false);
    BlockPosition ☃ = a(☃, ☃, 3, false);
    BlockPosition ☃ = a(☃, ☃, 6, false);
    
    StructureBoundingBox ☃ = new StructureBoundingBox(☃, ☃);
    StructureBoundingBox ☃ = new StructureBoundingBox(☃, ☃.a(☃.b()));
    
    int ☃ = ☃.c() * ☃.d() * ☃.e();
    if (☃ > 524288) {
      throw new CommandException("commands.compare.tooManyBlocks", new Object[] { Integer.valueOf(☃), Integer.valueOf(524288) });
    }
    if ((☃.b < 0) || (☃.e >= 256) || (☃.b < 0) || (☃.e >= 256)) {
      throw new CommandException("commands.compare.outOfWorld", new Object[0]);
    }
    World ☃ = ☃.getWorld();
    if ((!☃.a(☃)) || (!☃.a(☃))) {
      throw new CommandException("commands.compare.outOfWorld", new Object[0]);
    }
    boolean ☃ = false;
    if ((☃.length > 9) && 
      (☃[9].equals("masked"))) {
      ☃ = true;
    }
    ☃ = 0;
    BlockPosition ☃ = new BlockPosition(☃.a - ☃.a, ☃.b - ☃.b, ☃.c - ☃.c);
    BlockPosition.MutableBlockPosition ☃ = new BlockPosition.MutableBlockPosition();
    BlockPosition.MutableBlockPosition ☃ = new BlockPosition.MutableBlockPosition();
    for (int ☃ = ☃.c; ☃ <= ☃.f; ☃++) {
      for (int ☃ = ☃.b; ☃ <= ☃.e; ☃++) {
        for (int ☃ = ☃.a; ☃ <= ☃.d; ☃++)
        {
          ☃.c(☃, ☃, ☃);
          ☃.c(☃ + ☃.getX(), ☃ + ☃.getY(), ☃ + ☃.getZ());
          
          boolean ☃ = false;
          IBlockData ☃ = ☃.getType(☃);
          if ((!☃) || (☃.getBlock() != Blocks.AIR))
          {
            if (☃ == ☃.getType(☃))
            {
              TileEntity ☃ = ☃.getTileEntity(☃);
              TileEntity ☃ = ☃.getTileEntity(☃);
              if ((☃ != null) && (☃ != null))
              {
                NBTTagCompound ☃ = new NBTTagCompound();
                ☃.b(☃);
                ☃.remove("x");
                ☃.remove("y");
                ☃.remove("z");
                
                NBTTagCompound ☃ = new NBTTagCompound();
                ☃.b(☃);
                ☃.remove("x");
                ☃.remove("y");
                ☃.remove("z");
                if (!☃.equals(☃)) {
                  ☃ = true;
                }
              }
              else if (☃ != null)
              {
                ☃ = true;
              }
            }
            else
            {
              ☃ = true;
            }
            ☃++;
            if (☃) {
              throw new CommandException("commands.compare.failed", new Object[0]);
            }
          }
        }
      }
    }
    ☃.a(CommandObjectiveExecutor.EnumCommandResult.AFFECTED_BLOCKS, ☃);
    a(☃, this, "commands.compare.success", new Object[] { Integer.valueOf(☃) });
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
      return a(☃, new String[] { "masked", "all" });
    }
    return null;
  }
}
