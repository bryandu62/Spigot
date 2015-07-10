package net.minecraft.server.v1_8_R3;

public class CommandObjectiveExecutor
{
  private static final int a = EnumCommandResult.values().length;
  private static final String[] b = new String[a];
  private String[] c = b;
  private String[] d = b;
  
  public void a(final ICommandListener ☃, EnumCommandResult ☃, int ☃)
  {
    String ☃ = this.c[☃.a()];
    if (☃ == null) {
      return;
    }
    ICommandListener ☃ = new ICommandListener()
    {
      public String getName()
      {
        return ☃.getName();
      }
      
      public IChatBaseComponent getScoreboardDisplayName()
      {
        return ☃.getScoreboardDisplayName();
      }
      
      public void sendMessage(IChatBaseComponent ☃)
      {
        ☃.sendMessage(☃);
      }
      
      public boolean a(int ☃, String ☃)
      {
        return true;
      }
      
      public BlockPosition getChunkCoordinates()
      {
        return ☃.getChunkCoordinates();
      }
      
      public Vec3D d()
      {
        return ☃.d();
      }
      
      public World getWorld()
      {
        return ☃.getWorld();
      }
      
      public Entity f()
      {
        return ☃.f();
      }
      
      public boolean getSendCommandFeedback()
      {
        return ☃.getSendCommandFeedback();
      }
      
      public void a(CommandObjectiveExecutor.EnumCommandResult ☃, int ☃)
      {
        ☃.a(☃, ☃);
      }
    };
    String ☃;
    try
    {
      ☃ = CommandAbstract.e(☃, ☃);
    }
    catch (ExceptionEntityNotFound ☃)
    {
      return;
    }
    String ☃ = this.d[☃.a()];
    if (☃ == null) {
      return;
    }
    Scoreboard ☃ = ☃.getWorld().getScoreboard();
    ScoreboardObjective ☃ = ☃.getObjective(☃);
    if (☃ == null) {
      return;
    }
    if (!☃.b(☃, ☃)) {
      return;
    }
    ScoreboardScore ☃ = ☃.getPlayerScoreForObjective(☃, ☃);
    ☃.setScore(☃);
  }
  
  public void a(NBTTagCompound ☃)
  {
    if (!☃.hasKeyOfType("CommandStats", 10)) {
      return;
    }
    NBTTagCompound ☃ = ☃.getCompound("CommandStats");
    for (EnumCommandResult ☃ : EnumCommandResult.values())
    {
      String ☃ = ☃.b() + "Name";
      String ☃ = ☃.b() + "Objective";
      if ((☃.hasKeyOfType(☃, 8)) && (☃.hasKeyOfType(☃, 8)))
      {
        String ☃ = ☃.getString(☃);
        String ☃ = ☃.getString(☃);
        a(this, ☃, ☃, ☃);
      }
    }
  }
  
  public void b(NBTTagCompound ☃)
  {
    NBTTagCompound ☃ = new NBTTagCompound();
    for (EnumCommandResult ☃ : EnumCommandResult.values())
    {
      String ☃ = this.c[☃.a()];
      String ☃ = this.d[☃.a()];
      if ((☃ != null) && (☃ != null))
      {
        ☃.setString(☃.b() + "Name", ☃);
        ☃.setString(☃.b() + "Objective", ☃);
      }
    }
    if (!☃.isEmpty()) {
      ☃.set("CommandStats", ☃);
    }
  }
  
  public static void a(CommandObjectiveExecutor ☃, EnumCommandResult ☃, String ☃, String ☃)
  {
    if ((☃ == null) || (☃.length() == 0) || (☃ == null) || (☃.length() == 0))
    {
      a(☃, ☃);
      return;
    }
    if ((☃.c == b) || (☃.d == b))
    {
      ☃.c = new String[a];
      ☃.d = new String[a];
    }
    ☃.c[☃.a()] = ☃;
    ☃.d[☃.a()] = ☃;
  }
  
  private static void a(CommandObjectiveExecutor ☃, EnumCommandResult ☃)
  {
    if ((☃.c == b) || (☃.d == b)) {
      return;
    }
    ☃.c[☃.a()] = null;
    ☃.d[☃.a()] = null;
    
    boolean ☃ = true;
    for (EnumCommandResult ☃ : EnumCommandResult.values()) {
      if ((☃.c[☃.a()] != null) && (☃.d[☃.a()] != null))
      {
        ☃ = false;
        break;
      }
    }
    if (☃)
    {
      ☃.c = b;
      ☃.d = b;
    }
  }
  
  public void a(CommandObjectiveExecutor ☃)
  {
    for (EnumCommandResult ☃ : ) {
      a(this, ☃, ☃.c[☃.a()], ☃.d[☃.a()]);
    }
  }
  
  public static enum EnumCommandResult
  {
    final int f;
    final String g;
    
    private EnumCommandResult(int ☃, String ☃)
    {
      this.f = ☃;
      this.g = ☃;
    }
    
    public int a()
    {
      return this.f;
    }
    
    public String b()
    {
      return this.g;
    }
    
    public static String[] c()
    {
      String[] ☃ = new String[values().length];
      int ☃ = 0;
      for (EnumCommandResult ☃ : values()) {
        ☃[(☃++)] = ☃.b();
      }
      return ☃;
    }
    
    public static EnumCommandResult a(String ☃)
    {
      for (EnumCommandResult ☃ : ) {
        if (☃.b().equals(☃)) {
          return ☃;
        }
      }
      return null;
    }
  }
}
