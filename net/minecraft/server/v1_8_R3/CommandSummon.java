package net.minecraft.server.v1_8_R3;

import java.util.List;

public class CommandSummon
  extends CommandAbstract
{
  public String getCommand()
  {
    return "summon";
  }
  
  public int a()
  {
    return 2;
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.summon.usage";
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    if (☃.length < 1) {
      throw new ExceptionUsage("commands.summon.usage", new Object[0]);
    }
    String ☃ = ☃[0];
    BlockPosition ☃ = ☃.getChunkCoordinates();
    Vec3D ☃ = ☃.d();
    
    double ☃ = ☃.a;
    double ☃ = ☃.b;
    double ☃ = ☃.c;
    if (☃.length >= 4)
    {
      ☃ = b(☃, ☃[1], true);
      ☃ = b(☃, ☃[2], false);
      ☃ = b(☃, ☃[3], true);
      ☃ = new BlockPosition(☃, ☃, ☃);
    }
    World ☃ = ☃.getWorld();
    if (!☃.isLoaded(☃)) {
      throw new CommandException("commands.summon.outOfWorld", new Object[0]);
    }
    if ("LightningBolt".equals(☃))
    {
      ☃.strikeLightning(new EntityLightning(☃, ☃, ☃, ☃));
      a(☃, this, "commands.summon.success", new Object[0]);
      return;
    }
    NBTTagCompound ☃ = new NBTTagCompound();
    boolean ☃ = false;
    if (☃.length >= 5)
    {
      IChatBaseComponent ☃ = a(☃, ☃, 4);
      try
      {
        ☃ = MojangsonParser.parse(☃.c());
        ☃ = true;
      }
      catch (MojangsonParseException ☃)
      {
        throw new CommandException("commands.summon.tagError", new Object[] { ☃.getMessage() });
      }
    }
    ☃.setString("id", ☃);
    Entity ☃;
    try
    {
      ☃ = EntityTypes.a(☃, ☃);
    }
    catch (RuntimeException ☃)
    {
      throw new CommandException("commands.summon.failed", new Object[0]);
    }
    if (☃ == null) {
      throw new CommandException("commands.summon.failed", new Object[0]);
    }
    ☃.setPositionRotation(☃, ☃, ☃, ☃.yaw, ☃.pitch);
    if (!☃) {
      if ((☃ instanceof EntityInsentient)) {
        ((EntityInsentient)☃).prepare(☃.E(new BlockPosition(☃)), null);
      }
    }
    ☃.addEntity(☃);
    
    Entity ☃ = ☃;
    NBTTagCompound ☃ = ☃;
    while ((☃ != null) && (☃.hasKeyOfType("Riding", 10)))
    {
      Entity ☃ = EntityTypes.a(☃.getCompound("Riding"), ☃);
      if (☃ != null)
      {
        ☃.setPositionRotation(☃, ☃, ☃, ☃.yaw, ☃.pitch);
        ☃.addEntity(☃);
        ☃.mount(☃);
      }
      ☃ = ☃;
      ☃ = ☃.getCompound("Riding");
    }
    a(☃, this, "commands.summon.success", new Object[0]);
  }
  
  public List<String> tabComplete(ICommandListener ☃, String[] ☃, BlockPosition ☃)
  {
    if (☃.length == 1) {
      return a(☃, EntityTypes.b());
    }
    if ((☃.length > 1) && (☃.length <= 4)) {
      return a(☃, 1, ☃);
    }
    return null;
  }
}
