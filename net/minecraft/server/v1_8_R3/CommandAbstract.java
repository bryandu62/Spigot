package net.minecraft.server.v1_8_R3;

import com.google.common.base.Functions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.primitives.Doubles;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public abstract class CommandAbstract
  implements ICommand
{
  private static ICommandDispatcher a;
  
  public int a()
  {
    return 4;
  }
  
  public List<String> b()
  {
    return Collections.emptyList();
  }
  
  public boolean canUse(ICommandListener ☃)
  {
    return ☃.a(a(), getCommand());
  }
  
  public List<String> tabComplete(ICommandListener ☃, String[] ☃, BlockPosition ☃)
  {
    return null;
  }
  
  public static int a(String ☃)
    throws ExceptionInvalidNumber
  {
    try
    {
      return Integer.parseInt(☃);
    }
    catch (NumberFormatException ☃)
    {
      throw new ExceptionInvalidNumber("commands.generic.num.invalid", new Object[] { ☃ });
    }
  }
  
  public static int a(String ☃, int ☃)
    throws ExceptionInvalidNumber
  {
    return a(☃, ☃, Integer.MAX_VALUE);
  }
  
  public static int a(String ☃, int ☃, int ☃)
    throws ExceptionInvalidNumber
  {
    int ☃ = a(☃);
    if (☃ < ☃) {
      throw new ExceptionInvalidNumber("commands.generic.num.tooSmall", new Object[] { Integer.valueOf(☃), Integer.valueOf(☃) });
    }
    if (☃ > ☃) {
      throw new ExceptionInvalidNumber("commands.generic.num.tooBig", new Object[] { Integer.valueOf(☃), Integer.valueOf(☃) });
    }
    return ☃;
  }
  
  public static long b(String ☃)
    throws ExceptionInvalidNumber
  {
    try
    {
      return Long.parseLong(☃);
    }
    catch (NumberFormatException ☃)
    {
      throw new ExceptionInvalidNumber("commands.generic.num.invalid", new Object[] { ☃ });
    }
  }
  
  public static long a(String ☃, long ☃, long ☃)
    throws ExceptionInvalidNumber
  {
    long ☃ = b(☃);
    if (☃ < ☃) {
      throw new ExceptionInvalidNumber("commands.generic.num.tooSmall", new Object[] { Long.valueOf(☃), Long.valueOf(☃) });
    }
    if (☃ > ☃) {
      throw new ExceptionInvalidNumber("commands.generic.num.tooBig", new Object[] { Long.valueOf(☃), Long.valueOf(☃) });
    }
    return ☃;
  }
  
  public static BlockPosition a(ICommandListener ☃, String[] ☃, int ☃, boolean ☃)
    throws ExceptionInvalidNumber
  {
    BlockPosition ☃ = ☃.getChunkCoordinates();
    return new BlockPosition(b(☃.getX(), ☃[☃], -30000000, 30000000, ☃), b(☃.getY(), ☃[(☃ + 1)], 0, 256, false), b(☃.getZ(), ☃[(☃ + 2)], -30000000, 30000000, ☃));
  }
  
  public static double c(String ☃)
    throws ExceptionInvalidNumber
  {
    try
    {
      double ☃ = Double.parseDouble(☃);
      if (!Doubles.isFinite(☃)) {
        throw new ExceptionInvalidNumber("commands.generic.num.invalid", new Object[] { ☃ });
      }
      return ☃;
    }
    catch (NumberFormatException ☃)
    {
      throw new ExceptionInvalidNumber("commands.generic.num.invalid", new Object[] { ☃ });
    }
  }
  
  public static double a(String ☃, double ☃)
    throws ExceptionInvalidNumber
  {
    return a(☃, ☃, Double.MAX_VALUE);
  }
  
  public static double a(String ☃, double ☃, double ☃)
    throws ExceptionInvalidNumber
  {
    double ☃ = c(☃);
    if (☃ < ☃) {
      throw new ExceptionInvalidNumber("commands.generic.double.tooSmall", new Object[] { Double.valueOf(☃), Double.valueOf(☃) });
    }
    if (☃ > ☃) {
      throw new ExceptionInvalidNumber("commands.generic.double.tooBig", new Object[] { Double.valueOf(☃), Double.valueOf(☃) });
    }
    return ☃;
  }
  
  public static boolean d(String ☃)
    throws CommandException
  {
    if ((☃.equals("true")) || (☃.equals("1"))) {
      return true;
    }
    if ((☃.equals("false")) || (☃.equals("0"))) {
      return false;
    }
    throw new CommandException("commands.generic.boolean.invalid", new Object[] { ☃ });
  }
  
  public static EntityPlayer b(ICommandListener ☃)
    throws ExceptionPlayerNotFound
  {
    if ((☃ instanceof EntityPlayer)) {
      return (EntityPlayer)☃;
    }
    throw new ExceptionPlayerNotFound("You must specify which player you wish to perform this action on.", new Object[0]);
  }
  
  public static EntityPlayer a(ICommandListener ☃, String ☃)
    throws ExceptionPlayerNotFound
  {
    EntityPlayer ☃ = PlayerSelector.getPlayer(☃, ☃);
    if (☃ == null) {
      try
      {
        ☃ = MinecraftServer.getServer().getPlayerList().a(UUID.fromString(☃));
      }
      catch (IllegalArgumentException localIllegalArgumentException) {}
    }
    if (☃ == null) {
      ☃ = MinecraftServer.getServer().getPlayerList().getPlayer(☃);
    }
    if (☃ == null) {
      throw new ExceptionPlayerNotFound();
    }
    return ☃;
  }
  
  public static Entity b(ICommandListener ☃, String ☃)
    throws ExceptionEntityNotFound
  {
    return a(☃, ☃, Entity.class);
  }
  
  public static <T extends Entity> T a(ICommandListener ☃, String ☃, Class<? extends T> ☃)
    throws ExceptionEntityNotFound
  {
    Entity ☃ = PlayerSelector.getEntity(☃, ☃, ☃);
    
    MinecraftServer ☃ = MinecraftServer.getServer();
    if (☃ == null) {
      ☃ = ☃.getPlayerList().getPlayer(☃);
    }
    if (☃ == null) {
      try
      {
        UUID ☃ = UUID.fromString(☃);
        ☃ = ☃.a(☃);
        if (☃ == null) {
          ☃ = ☃.getPlayerList().a(☃);
        }
      }
      catch (IllegalArgumentException ☃)
      {
        throw new ExceptionEntityNotFound("commands.generic.entity.invalidUuid", new Object[0]);
      }
    }
    if ((☃ == null) || (!☃.isAssignableFrom(☃.getClass()))) {
      throw new ExceptionEntityNotFound();
    }
    return ☃;
  }
  
  public static List<Entity> c(ICommandListener ☃, String ☃)
    throws ExceptionEntityNotFound
  {
    if (PlayerSelector.isPattern(☃)) {
      return PlayerSelector.getPlayers(☃, ☃, Entity.class);
    }
    return Lists.newArrayList(new Entity[] { b(☃, ☃) });
  }
  
  public static String d(ICommandListener ☃, String ☃)
    throws ExceptionPlayerNotFound
  {
    try
    {
      return a(☃, ☃).getName();
    }
    catch (ExceptionPlayerNotFound ☃)
    {
      if (PlayerSelector.isPattern(☃)) {
        throw ☃;
      }
    }
    return ☃;
  }
  
  public static String e(ICommandListener ☃, String ☃)
    throws ExceptionEntityNotFound
  {
    try
    {
      return a(☃, ☃).getName();
    }
    catch (ExceptionPlayerNotFound ☃)
    {
      try
      {
        return b(☃, ☃).getUniqueID().toString();
      }
      catch (ExceptionEntityNotFound ☃)
      {
        if (PlayerSelector.isPattern(☃)) {
          throw ☃;
        }
      }
    }
    return ☃;
  }
  
  public static IChatBaseComponent a(ICommandListener ☃, String[] ☃, int ☃)
    throws ExceptionPlayerNotFound
  {
    return b(☃, ☃, ☃, false);
  }
  
  public static IChatBaseComponent b(ICommandListener ☃, String[] ☃, int ☃, boolean ☃)
    throws ExceptionPlayerNotFound
  {
    IChatBaseComponent ☃ = new ChatComponentText("");
    for (int ☃ = ☃; ☃ < ☃.length; ☃++)
    {
      if (☃ > ☃) {
        ☃.a(" ");
      }
      IChatBaseComponent ☃ = new ChatComponentText(☃[☃]);
      if (☃)
      {
        IChatBaseComponent ☃ = PlayerSelector.getPlayerNames(☃, ☃[☃]);
        if (☃ == null)
        {
          if (PlayerSelector.isPattern(☃[☃])) {
            throw new ExceptionPlayerNotFound();
          }
        }
        else {
          ☃ = ☃;
        }
      }
      ☃.addSibling(☃);
    }
    return ☃;
  }
  
  public static String a(String[] ☃, int ☃)
  {
    StringBuilder ☃ = new StringBuilder();
    for (int ☃ = ☃; ☃ < ☃.length; ☃++)
    {
      if (☃ > ☃) {
        ☃.append(" ");
      }
      String ☃ = ☃[☃];
      
      ☃.append(☃);
    }
    return ☃.toString();
  }
  
  public static CommandNumber a(double ☃, String ☃, boolean ☃)
    throws ExceptionInvalidNumber
  {
    return a(☃, ☃, -30000000, 30000000, ☃);
  }
  
  public static CommandNumber a(double ☃, String ☃, int ☃, int ☃, boolean ☃)
    throws ExceptionInvalidNumber
  {
    boolean ☃ = ☃.startsWith("~");
    if ((☃) && (Double.isNaN(☃))) {
      throw new ExceptionInvalidNumber("commands.generic.num.invalid", new Object[] { Double.valueOf(☃) });
    }
    double ☃ = 0.0D;
    if ((!☃) || (☃.length() > 1))
    {
      boolean ☃ = ☃.contains(".");
      if (☃) {
        ☃ = ☃.substring(1);
      }
      ☃ += c(☃);
      if ((!☃) && (!☃) && (☃)) {
        ☃ += 0.5D;
      }
    }
    if ((☃ != 0) || (☃ != 0))
    {
      if (☃ < ☃) {
        throw new ExceptionInvalidNumber("commands.generic.double.tooSmall", new Object[] { Double.valueOf(☃), Integer.valueOf(☃) });
      }
      if (☃ > ☃) {
        throw new ExceptionInvalidNumber("commands.generic.double.tooBig", new Object[] { Double.valueOf(☃), Integer.valueOf(☃) });
      }
    }
    return new CommandNumber(☃ + (☃ ? ☃ : 0.0D), ☃, ☃);
  }
  
  public static double b(double ☃, String ☃, boolean ☃)
    throws ExceptionInvalidNumber
  {
    return b(☃, ☃, -30000000, 30000000, ☃);
  }
  
  public static double b(double ☃, String ☃, int ☃, int ☃, boolean ☃)
    throws ExceptionInvalidNumber
  {
    boolean ☃ = ☃.startsWith("~");
    if ((☃) && (Double.isNaN(☃))) {
      throw new ExceptionInvalidNumber("commands.generic.num.invalid", new Object[] { Double.valueOf(☃) });
    }
    double ☃ = ☃ ? ☃ : 0.0D;
    if ((!☃) || (☃.length() > 1))
    {
      boolean ☃ = ☃.contains(".");
      if (☃) {
        ☃ = ☃.substring(1);
      }
      ☃ += c(☃);
      if ((!☃) && (!☃) && (☃)) {
        ☃ += 0.5D;
      }
    }
    if ((☃ != 0) || (☃ != 0))
    {
      if (☃ < ☃) {
        throw new ExceptionInvalidNumber("commands.generic.double.tooSmall", new Object[] { Double.valueOf(☃), Integer.valueOf(☃) });
      }
      if (☃ > ☃) {
        throw new ExceptionInvalidNumber("commands.generic.double.tooBig", new Object[] { Double.valueOf(☃), Integer.valueOf(☃) });
      }
    }
    return ☃;
  }
  
  public static class CommandNumber
  {
    private final double a;
    private final double b;
    private final boolean c;
    
    protected CommandNumber(double ☃, double ☃, boolean ☃)
    {
      this.a = ☃;
      this.b = ☃;
      this.c = ☃;
    }
    
    public double a()
    {
      return this.a;
    }
    
    public double b()
    {
      return this.b;
    }
    
    public boolean c()
    {
      return this.c;
    }
  }
  
  public static Item f(ICommandListener ☃, String ☃)
    throws ExceptionInvalidNumber
  {
    MinecraftKey ☃ = new MinecraftKey(☃);
    Item ☃ = (Item)Item.REGISTRY.get(☃);
    if (☃ == null) {
      throw new ExceptionInvalidNumber("commands.give.item.notFound", new Object[] { ☃ });
    }
    return ☃;
  }
  
  public static Block g(ICommandListener ☃, String ☃)
    throws ExceptionInvalidNumber
  {
    MinecraftKey ☃ = new MinecraftKey(☃);
    if (!Block.REGISTRY.d(☃)) {
      throw new ExceptionInvalidNumber("commands.give.block.notFound", new Object[] { ☃ });
    }
    Block ☃ = (Block)Block.REGISTRY.get(☃);
    if (☃ == null) {
      throw new ExceptionInvalidNumber("commands.give.block.notFound", new Object[] { ☃ });
    }
    return ☃;
  }
  
  public static String a(Object[] ☃)
  {
    StringBuilder ☃ = new StringBuilder();
    for (int ☃ = 0; ☃ < ☃.length; ☃++)
    {
      String ☃ = ☃[☃].toString();
      if (☃ > 0) {
        if (☃ == ☃.length - 1) {
          ☃.append(" and ");
        } else {
          ☃.append(", ");
        }
      }
      ☃.append(☃);
    }
    return ☃.toString();
  }
  
  public static IChatBaseComponent a(List<IChatBaseComponent> ☃)
  {
    IChatBaseComponent ☃ = new ChatComponentText("");
    for (int ☃ = 0; ☃ < ☃.size(); ☃++)
    {
      if (☃ > 0) {
        if (☃ == ☃.size() - 1) {
          ☃.a(" and ");
        } else if (☃ > 0) {
          ☃.a(", ");
        }
      }
      ☃.addSibling((IChatBaseComponent)☃.get(☃));
    }
    return ☃;
  }
  
  public static String a(Collection<String> ☃)
  {
    return a(☃.toArray(new String[☃.size()]));
  }
  
  public static List<String> a(String[] ☃, int ☃, BlockPosition ☃)
  {
    if (☃ == null) {
      return null;
    }
    int ☃ = ☃.length - 1;
    String ☃;
    if (☃ == ☃)
    {
      ☃ = Integer.toString(☃.getX());
    }
    else
    {
      String ☃;
      if (☃ == ☃ + 1)
      {
        ☃ = Integer.toString(☃.getY());
      }
      else
      {
        String ☃;
        if (☃ == ☃ + 2) {
          ☃ = Integer.toString(☃.getZ());
        } else {
          return null;
        }
      }
    }
    String ☃;
    return Lists.newArrayList(new String[] { ☃ });
  }
  
  public static List<String> b(String[] ☃, int ☃, BlockPosition ☃)
  {
    if (☃ == null) {
      return null;
    }
    int ☃ = ☃.length - 1;
    String ☃;
    if (☃ == ☃)
    {
      ☃ = Integer.toString(☃.getX());
    }
    else
    {
      String ☃;
      if (☃ == ☃ + 1) {
        ☃ = Integer.toString(☃.getZ());
      } else {
        return null;
      }
    }
    String ☃;
    return Lists.newArrayList(new String[] { ☃ });
  }
  
  public static boolean a(String ☃, String ☃)
  {
    return ☃.regionMatches(true, 0, ☃, 0, ☃.length());
  }
  
  public static List<String> a(String[] ☃, String... ☃)
  {
    return a(☃, Arrays.asList(☃));
  }
  
  public static List<String> a(String[] ☃, Collection<?> ☃)
  {
    String ☃ = ☃[(☃.length - 1)];
    List<String> ☃ = Lists.newArrayList();
    if (!☃.isEmpty())
    {
      for (String ☃ : Iterables.transform(☃, Functions.toStringFunction())) {
        if (a(☃, ☃)) {
          ☃.add(☃);
        }
      }
      if (☃.isEmpty()) {
        for (Object ☃ : ☃) {
          if (((☃ instanceof MinecraftKey)) && 
            (a(☃, ((MinecraftKey)☃).a()))) {
            ☃.add(String.valueOf(☃));
          }
        }
      }
    }
    return ☃;
  }
  
  public boolean isListStart(String[] ☃, int ☃)
  {
    return false;
  }
  
  public static void a(ICommandListener ☃, ICommand ☃, String ☃, Object... ☃)
  {
    a(☃, ☃, 0, ☃, ☃);
  }
  
  public static void a(ICommandListener ☃, ICommand ☃, int ☃, String ☃, Object... ☃)
  {
    if (a != null) {
      a.a(☃, ☃, ☃, ☃, ☃);
    }
  }
  
  public static void a(ICommandDispatcher ☃)
  {
    a = ☃;
  }
  
  public int a(ICommand ☃)
  {
    return getCommand().compareTo(☃.getCommand());
  }
}
