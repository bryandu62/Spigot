package net.minecraft.server.v1_8_R3;

import java.util.Collection;
import java.util.List;

public class CommandEffect
  extends CommandAbstract
{
  public String getCommand()
  {
    return "effect";
  }
  
  public int a()
  {
    return 2;
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.effect.usage";
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    if (☃.length < 2) {
      throw new ExceptionUsage("commands.effect.usage", new Object[0]);
    }
    EntityLiving ☃ = (EntityLiving)a(☃, ☃[0], EntityLiving.class);
    if (☃[1].equals("clear"))
    {
      if (☃.getEffects().isEmpty()) {
        throw new CommandException("commands.effect.failure.notActive.all", new Object[] { ☃.getName() });
      }
      ☃.removeAllEffects();
      a(☃, this, "commands.effect.success.removed.all", new Object[] { ☃.getName() }); return;
    }
    int ☃;
    try
    {
      ☃ = a(☃[1], 1);
    }
    catch (ExceptionInvalidNumber ☃)
    {
      MobEffectList ☃ = MobEffectList.b(☃[1]);
      if (☃ == null) {
        throw ☃;
      }
      ☃ = ☃.id;
    }
    int ☃ = 600;
    int ☃ = 30;
    int ☃ = 0;
    if ((☃ < 0) || (☃ >= MobEffectList.byId.length) || (MobEffectList.byId[☃] == null)) {
      throw new ExceptionInvalidNumber("commands.effect.notFound", new Object[] { Integer.valueOf(☃) });
    }
    MobEffectList ☃ = MobEffectList.byId[☃];
    if (☃.length >= 3)
    {
      ☃ = a(☃[2], 0, 1000000);
      if (☃.isInstant()) {
        ☃ = ☃;
      } else {
        ☃ = ☃ * 20;
      }
    }
    else if (☃.isInstant())
    {
      ☃ = 1;
    }
    if (☃.length >= 4) {
      ☃ = a(☃[3], 0, 255);
    }
    boolean ☃ = true;
    if ((☃.length >= 5) && 
      ("true".equalsIgnoreCase(☃[4]))) {
      ☃ = false;
    }
    if (☃ > 0)
    {
      MobEffect ☃ = new MobEffect(☃, ☃, ☃, false, ☃);
      ☃.addEffect(☃);
      a(☃, this, "commands.effect.success", new Object[] { new ChatMessage(☃.g(), new Object[0]), Integer.valueOf(☃), Integer.valueOf(☃), ☃.getName(), Integer.valueOf(☃) });
      return;
    }
    if (☃.hasEffect(☃))
    {
      ☃.removeEffect(☃);
      a(☃, this, "commands.effect.success.removed", new Object[] { new ChatMessage(☃.a(), new Object[0]), ☃.getName() });
    }
    else
    {
      throw new CommandException("commands.effect.failure.notActive", new Object[] { new ChatMessage(☃.a(), new Object[0]), ☃.getName() });
    }
  }
  
  public List<String> tabComplete(ICommandListener ☃, String[] ☃, BlockPosition ☃)
  {
    if (☃.length == 1) {
      return a(☃, d());
    }
    if (☃.length == 2) {
      return a(☃, MobEffectList.c());
    }
    if (☃.length == 5) {
      return a(☃, new String[] { "true", "false" });
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
