package net.minecraft.server.v1_8_R3;

import java.util.List;

public class CommandEnchant
  extends CommandAbstract
{
  public String getCommand()
  {
    return "enchant";
  }
  
  public int a()
  {
    return 2;
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.enchant.usage";
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    if (☃.length < 2) {
      throw new ExceptionUsage("commands.enchant.usage", new Object[0]);
    }
    EntityHuman ☃ = a(☃, ☃[0]);
    ☃.a(CommandObjectiveExecutor.EnumCommandResult.AFFECTED_ITEMS, 0);
    int ☃;
    try
    {
      ☃ = a(☃[1], 0);
    }
    catch (ExceptionInvalidNumber ☃)
    {
      Enchantment ☃ = Enchantment.getByName(☃[1]);
      if (☃ == null) {
        throw ☃;
      }
      ☃ = ☃.id;
    }
    int ☃ = 1;
    
    ItemStack ☃ = ☃.bZ();
    if (☃ == null) {
      throw new CommandException("commands.enchant.noItem", new Object[0]);
    }
    Enchantment ☃ = Enchantment.getById(☃);
    if (☃ == null) {
      throw new ExceptionInvalidNumber("commands.enchant.notFound", new Object[] { Integer.valueOf(☃) });
    }
    if (!☃.canEnchant(☃)) {
      throw new CommandException("commands.enchant.cantEnchant", new Object[0]);
    }
    if (☃.length >= 3) {
      ☃ = a(☃[2], ☃.getStartLevel(), ☃.getMaxLevel());
    }
    if (☃.hasTag())
    {
      NBTTagList ☃ = ☃.getEnchantments();
      if (☃ != null) {
        for (int ☃ = 0; ☃ < ☃.size(); ☃++)
        {
          int ☃ = ☃.get(☃).getShort("id");
          if (Enchantment.getById(☃) != null)
          {
            Enchantment ☃ = Enchantment.getById(☃);
            if (!☃.a(☃)) {
              throw new CommandException("commands.enchant.cantCombine", new Object[] { ☃.d(☃), ☃.d(☃.get(☃).getShort("lvl")) });
            }
          }
        }
      }
    }
    ☃.addEnchantment(☃, ☃);
    a(☃, this, "commands.enchant.success", new Object[0]);
    ☃.a(CommandObjectiveExecutor.EnumCommandResult.AFFECTED_ITEMS, 1);
  }
  
  public List<String> tabComplete(ICommandListener ☃, String[] ☃, BlockPosition ☃)
  {
    if (☃.length == 1) {
      return a(☃, d());
    }
    if (☃.length == 2) {
      return a(☃, Enchantment.getEffects());
    }
    return null;
  }
  
  protected String[] d()
  {
    return MinecraftServer.getServer().getPlayers();
  }
  
  public boolean isListStart(String[] ☃, int ☃)
  {
    return ☃ == 0;
  }
}
