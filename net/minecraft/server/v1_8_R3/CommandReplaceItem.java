package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;

public class CommandReplaceItem
  extends CommandAbstract
{
  private static final Map<String, Integer> a = ;
  
  static
  {
    for (int ☃ = 0; ☃ < 54; ☃++) {
      a.put("slot.container." + ☃, Integer.valueOf(☃));
    }
    for (int ☃ = 0; ☃ < 9; ☃++) {
      a.put("slot.hotbar." + ☃, Integer.valueOf(☃));
    }
    for (int ☃ = 0; ☃ < 27; ☃++) {
      a.put("slot.inventory." + ☃, Integer.valueOf(9 + ☃));
    }
    for (int ☃ = 0; ☃ < 27; ☃++) {
      a.put("slot.enderchest." + ☃, Integer.valueOf(200 + ☃));
    }
    for (int ☃ = 0; ☃ < 8; ☃++) {
      a.put("slot.villager." + ☃, Integer.valueOf(300 + ☃));
    }
    for (int ☃ = 0; ☃ < 15; ☃++) {
      a.put("slot.horse." + ☃, Integer.valueOf(500 + ☃));
    }
    a.put("slot.weapon", Integer.valueOf(99));
    a.put("slot.armor.head", Integer.valueOf(103));
    a.put("slot.armor.chest", Integer.valueOf(102));
    a.put("slot.armor.legs", Integer.valueOf(101));
    a.put("slot.armor.feet", Integer.valueOf(100));
    a.put("slot.horse.saddle", Integer.valueOf(400));
    a.put("slot.horse.armor", Integer.valueOf(401));
    a.put("slot.horse.chest", Integer.valueOf(499));
  }
  
  public String getCommand()
  {
    return "replaceitem";
  }
  
  public int a()
  {
    return 2;
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.replaceitem.usage";
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    if (☃.length < 1) {
      throw new ExceptionUsage("commands.replaceitem.usage", new Object[0]);
    }
    boolean ☃;
    if (☃[0].equals("entity"))
    {
      ☃ = false;
    }
    else
    {
      boolean ☃;
      if (☃[0].equals("block")) {
        ☃ = true;
      } else {
        throw new ExceptionUsage("commands.replaceitem.usage", new Object[0]);
      }
    }
    boolean ☃;
    int ☃;
    int ☃;
    if (☃)
    {
      if (☃.length < 6) {
        throw new ExceptionUsage("commands.replaceitem.block.usage", new Object[0]);
      }
      ☃ = 4;
    }
    else
    {
      if (☃.length < 4) {
        throw new ExceptionUsage("commands.replaceitem.entity.usage", new Object[0]);
      }
      ☃ = 2;
    }
    int ☃ = e(☃[(☃++)]);
    try
    {
      ☃ = f(☃, ☃[☃]);
    }
    catch (ExceptionInvalidNumber ☃)
    {
      Item ☃;
      if (Block.getByName(☃[☃]) == Blocks.AIR) {
        ☃ = null;
      } else {
        throw ☃;
      }
    }
    Item ☃;
    ☃++;
    
    int ☃ = ☃.length > ☃ ? a(☃[(☃++)], 1, 64) : 1;
    int ☃ = ☃.length > ☃ ? a(☃[(☃++)]) : 0;
    ItemStack ☃ = new ItemStack(☃, ☃, ☃);
    if (☃.length > ☃)
    {
      String ☃ = a(☃, ☃, ☃).c();
      try
      {
        ☃.setTag(MojangsonParser.parse(☃));
      }
      catch (MojangsonParseException ☃)
      {
        throw new CommandException("commands.replaceitem.tagError", new Object[] { ☃.getMessage() });
      }
    }
    if (☃.getItem() == null) {
      ☃ = null;
    }
    if (☃)
    {
      ☃.a(CommandObjectiveExecutor.EnumCommandResult.AFFECTED_ITEMS, 0);
      BlockPosition ☃ = a(☃, ☃, 1, false);
      World ☃ = ☃.getWorld();
      TileEntity ☃ = ☃.getTileEntity(☃);
      if ((☃ == null) || (!(☃ instanceof IInventory))) {
        throw new CommandException("commands.replaceitem.noContainer", new Object[] { Integer.valueOf(☃.getX()), Integer.valueOf(☃.getY()), Integer.valueOf(☃.getZ()) });
      }
      IInventory ☃ = (IInventory)☃;
      if ((☃ >= 0) && (☃ < ☃.getSize())) {
        ☃.setItem(☃, ☃);
      }
    }
    else
    {
      Entity ☃ = b(☃, ☃[1]);
      ☃.a(CommandObjectiveExecutor.EnumCommandResult.AFFECTED_ITEMS, 0);
      if ((☃ instanceof EntityHuman)) {
        ((EntityHuman)☃).defaultContainer.b();
      }
      if (!☃.d(☃, ☃)) {
        throw new CommandException("commands.replaceitem.failed", new Object[] { Integer.valueOf(☃), Integer.valueOf(☃), ☃ == null ? "Air" : ☃.C() });
      }
      if ((☃ instanceof EntityHuman)) {
        ((EntityHuman)☃).defaultContainer.b();
      }
    }
    ☃.a(CommandObjectiveExecutor.EnumCommandResult.AFFECTED_ITEMS, ☃);
    a(☃, this, "commands.replaceitem.success", new Object[] { Integer.valueOf(☃), Integer.valueOf(☃), ☃ == null ? "Air" : ☃.C() });
  }
  
  private int e(String ☃)
    throws CommandException
  {
    if (!a.containsKey(☃)) {
      throw new CommandException("commands.generic.parameter.invalid", new Object[] { ☃ });
    }
    return ((Integer)a.get(☃)).intValue();
  }
  
  public List<String> tabComplete(ICommandListener ☃, String[] ☃, BlockPosition ☃)
  {
    if (☃.length == 1) {
      return a(☃, new String[] { "entity", "block" });
    }
    if ((☃.length == 2) && (☃[0].equals("entity"))) {
      return a(☃, d());
    }
    if ((☃.length >= 2) && (☃.length <= 4) && (☃[0].equals("block"))) {
      return a(☃, 1, ☃);
    }
    if (((☃.length == 3) && (☃[0].equals("entity"))) || ((☃.length == 5) && (☃[0].equals("block")))) {
      return a(☃, a.keySet());
    }
    if (((☃.length == 4) && (☃[0].equals("entity"))) || ((☃.length == 6) && (☃[0].equals("block")))) {
      return a(☃, Item.REGISTRY.keySet());
    }
    return null;
  }
  
  protected String[] d()
  {
    return MinecraftServer.getServer().getPlayers();
  }
  
  public boolean isListStart(String[] ☃, int ☃)
  {
    return (☃.length > 0) && (☃[0].equals("entity")) && (☃ == 1);
  }
}
