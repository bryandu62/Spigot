package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class CommandScoreboard
  extends CommandAbstract
{
  public String getCommand()
  {
    return "scoreboard";
  }
  
  public int a()
  {
    return 2;
  }
  
  public String getUsage(ICommandListener ☃)
  {
    return "commands.scoreboard.usage";
  }
  
  public void execute(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    if (b(☃, ☃)) {
      return;
    }
    if (☃.length < 1) {
      throw new ExceptionUsage("commands.scoreboard.usage", new Object[0]);
    }
    if (☃[0].equalsIgnoreCase("objectives"))
    {
      if (☃.length == 1) {
        throw new ExceptionUsage("commands.scoreboard.objectives.usage", new Object[0]);
      }
      if (☃[1].equalsIgnoreCase("list")) {
        d(☃);
      } else if (☃[1].equalsIgnoreCase("add"))
      {
        if (☃.length >= 4) {
          b(☃, ☃, 2);
        } else {
          throw new ExceptionUsage("commands.scoreboard.objectives.add.usage", new Object[0]);
        }
      }
      else if (☃[1].equalsIgnoreCase("remove"))
      {
        if (☃.length == 3) {
          h(☃, ☃[2]);
        } else {
          throw new ExceptionUsage("commands.scoreboard.objectives.remove.usage", new Object[0]);
        }
      }
      else if (☃[1].equalsIgnoreCase("setdisplay"))
      {
        if ((☃.length == 3) || (☃.length == 4)) {
          j(☃, ☃, 2);
        } else {
          throw new ExceptionUsage("commands.scoreboard.objectives.setdisplay.usage", new Object[0]);
        }
      }
      else {
        throw new ExceptionUsage("commands.scoreboard.objectives.usage", new Object[0]);
      }
    }
    else if (☃[0].equalsIgnoreCase("players"))
    {
      if (☃.length == 1) {
        throw new ExceptionUsage("commands.scoreboard.players.usage", new Object[0]);
      }
      if (☃[1].equalsIgnoreCase("list"))
      {
        if (☃.length <= 3) {
          k(☃, ☃, 2);
        } else {
          throw new ExceptionUsage("commands.scoreboard.players.list.usage", new Object[0]);
        }
      }
      else if (☃[1].equalsIgnoreCase("add"))
      {
        if (☃.length >= 5) {
          l(☃, ☃, 2);
        } else {
          throw new ExceptionUsage("commands.scoreboard.players.add.usage", new Object[0]);
        }
      }
      else if (☃[1].equalsIgnoreCase("remove"))
      {
        if (☃.length >= 5) {
          l(☃, ☃, 2);
        } else {
          throw new ExceptionUsage("commands.scoreboard.players.remove.usage", new Object[0]);
        }
      }
      else if (☃[1].equalsIgnoreCase("set"))
      {
        if (☃.length >= 5) {
          l(☃, ☃, 2);
        } else {
          throw new ExceptionUsage("commands.scoreboard.players.set.usage", new Object[0]);
        }
      }
      else if (☃[1].equalsIgnoreCase("reset"))
      {
        if ((☃.length == 3) || (☃.length == 4)) {
          m(☃, ☃, 2);
        } else {
          throw new ExceptionUsage("commands.scoreboard.players.reset.usage", new Object[0]);
        }
      }
      else if (☃[1].equalsIgnoreCase("enable"))
      {
        if (☃.length == 4) {
          n(☃, ☃, 2);
        } else {
          throw new ExceptionUsage("commands.scoreboard.players.enable.usage", new Object[0]);
        }
      }
      else if (☃[1].equalsIgnoreCase("test"))
      {
        if ((☃.length == 5) || (☃.length == 6)) {
          o(☃, ☃, 2);
        } else {
          throw new ExceptionUsage("commands.scoreboard.players.test.usage", new Object[0]);
        }
      }
      else if (☃[1].equalsIgnoreCase("operation"))
      {
        if (☃.length == 7) {
          p(☃, ☃, 2);
        } else {
          throw new ExceptionUsage("commands.scoreboard.players.operation.usage", new Object[0]);
        }
      }
      else {
        throw new ExceptionUsage("commands.scoreboard.players.usage", new Object[0]);
      }
    }
    else if (☃[0].equalsIgnoreCase("teams"))
    {
      if (☃.length == 1) {
        throw new ExceptionUsage("commands.scoreboard.teams.usage", new Object[0]);
      }
      if (☃[1].equalsIgnoreCase("list"))
      {
        if (☃.length <= 3) {
          f(☃, ☃, 2);
        } else {
          throw new ExceptionUsage("commands.scoreboard.teams.list.usage", new Object[0]);
        }
      }
      else if (☃[1].equalsIgnoreCase("add"))
      {
        if (☃.length >= 3) {
          c(☃, ☃, 2);
        } else {
          throw new ExceptionUsage("commands.scoreboard.teams.add.usage", new Object[0]);
        }
      }
      else if (☃[1].equalsIgnoreCase("remove"))
      {
        if (☃.length == 3) {
          e(☃, ☃, 2);
        } else {
          throw new ExceptionUsage("commands.scoreboard.teams.remove.usage", new Object[0]);
        }
      }
      else if (☃[1].equalsIgnoreCase("empty"))
      {
        if (☃.length == 3) {
          i(☃, ☃, 2);
        } else {
          throw new ExceptionUsage("commands.scoreboard.teams.empty.usage", new Object[0]);
        }
      }
      else if (☃[1].equalsIgnoreCase("join"))
      {
        if ((☃.length >= 4) || ((☃.length == 3) && ((☃ instanceof EntityHuman)))) {
          g(☃, ☃, 2);
        } else {
          throw new ExceptionUsage("commands.scoreboard.teams.join.usage", new Object[0]);
        }
      }
      else if (☃[1].equalsIgnoreCase("leave"))
      {
        if ((☃.length >= 3) || ((☃ instanceof EntityHuman))) {
          h(☃, ☃, 2);
        } else {
          throw new ExceptionUsage("commands.scoreboard.teams.leave.usage", new Object[0]);
        }
      }
      else if (☃[1].equalsIgnoreCase("option"))
      {
        if ((☃.length == 4) || (☃.length == 5)) {
          d(☃, ☃, 2);
        } else {
          throw new ExceptionUsage("commands.scoreboard.teams.option.usage", new Object[0]);
        }
      }
      else {
        throw new ExceptionUsage("commands.scoreboard.teams.usage", new Object[0]);
      }
    }
    else
    {
      throw new ExceptionUsage("commands.scoreboard.usage", new Object[0]);
    }
  }
  
  private boolean b(ICommandListener ☃, String[] ☃)
    throws CommandException
  {
    int ☃ = -1;
    for (int ☃ = 0; ☃ < ☃.length; ☃++) {
      if (isListStart(☃, ☃)) {
        if ("*".equals(☃[☃])) {
          if (☃ < 0) {
            ☃ = ☃;
          } else {
            throw new CommandException("commands.scoreboard.noMultiWildcard", new Object[0]);
          }
        }
      }
    }
    if (☃ < 0) {
      return false;
    }
    List<String> ☃ = Lists.newArrayList(d().getPlayers());
    String ☃ = ☃[☃];
    List<String> ☃ = Lists.newArrayList();
    for (String ☃ : ☃)
    {
      ☃[☃] = ☃;
      try
      {
        execute(☃, ☃);
        ☃.add(☃);
      }
      catch (CommandException ☃)
      {
        ChatMessage ☃ = new ChatMessage(☃.getMessage(), ☃.getArgs());
        ☃.getChatModifier().setColor(EnumChatFormat.RED);
        ☃.sendMessage(☃);
      }
    }
    ☃[☃] = ☃;
    
    ☃.a(CommandObjectiveExecutor.EnumCommandResult.AFFECTED_ENTITIES, ☃.size());
    if (☃.size() == 0) {
      throw new ExceptionUsage("commands.scoreboard.allMatchesFailed", new Object[0]);
    }
    return true;
  }
  
  protected Scoreboard d()
  {
    return MinecraftServer.getServer().getWorldServer(0).getScoreboard();
  }
  
  protected ScoreboardObjective a(String ☃, boolean ☃)
    throws CommandException
  {
    Scoreboard ☃ = d();
    ScoreboardObjective ☃ = ☃.getObjective(☃);
    if (☃ == null) {
      throw new CommandException("commands.scoreboard.objectiveNotFound", new Object[] { ☃ });
    }
    if ((☃) && (☃.getCriteria().isReadOnly())) {
      throw new CommandException("commands.scoreboard.objectiveReadOnly", new Object[] { ☃ });
    }
    return ☃;
  }
  
  protected ScoreboardTeam e(String ☃)
    throws CommandException
  {
    Scoreboard ☃ = d();
    ScoreboardTeam ☃ = ☃.getTeam(☃);
    if (☃ == null) {
      throw new CommandException("commands.scoreboard.teamNotFound", new Object[] { ☃ });
    }
    return ☃;
  }
  
  protected void b(ICommandListener ☃, String[] ☃, int ☃)
    throws CommandException
  {
    String ☃ = ☃[(☃++)];
    String ☃ = ☃[(☃++)];
    Scoreboard ☃ = d();
    IScoreboardCriteria ☃ = (IScoreboardCriteria)IScoreboardCriteria.criteria.get(☃);
    if (☃ == null) {
      throw new ExceptionUsage("commands.scoreboard.objectives.add.wrongType", new Object[] { ☃ });
    }
    if (☃.getObjective(☃) != null) {
      throw new CommandException("commands.scoreboard.objectives.add.alreadyExists", new Object[] { ☃ });
    }
    if (☃.length() > 16) {
      throw new ExceptionInvalidSyntax("commands.scoreboard.objectives.add.tooLong", new Object[] { ☃, Integer.valueOf(16) });
    }
    if (☃.length() == 0) {
      throw new ExceptionUsage("commands.scoreboard.objectives.add.usage", new Object[0]);
    }
    if (☃.length > ☃)
    {
      String ☃ = a(☃, ☃, ☃).c();
      if (☃.length() > 32) {
        throw new ExceptionInvalidSyntax("commands.scoreboard.objectives.add.displayTooLong", new Object[] { ☃, Integer.valueOf(32) });
      }
      if (☃.length() > 0) {
        ☃.registerObjective(☃, ☃).setDisplayName(☃);
      } else {
        ☃.registerObjective(☃, ☃);
      }
    }
    else
    {
      ☃.registerObjective(☃, ☃);
    }
    a(☃, this, "commands.scoreboard.objectives.add.success", new Object[] { ☃ });
  }
  
  protected void c(ICommandListener ☃, String[] ☃, int ☃)
    throws CommandException
  {
    String ☃ = ☃[(☃++)];
    Scoreboard ☃ = d();
    if (☃.getTeam(☃) != null) {
      throw new CommandException("commands.scoreboard.teams.add.alreadyExists", new Object[] { ☃ });
    }
    if (☃.length() > 16) {
      throw new ExceptionInvalidSyntax("commands.scoreboard.teams.add.tooLong", new Object[] { ☃, Integer.valueOf(16) });
    }
    if (☃.length() == 0) {
      throw new ExceptionUsage("commands.scoreboard.teams.add.usage", new Object[0]);
    }
    if (☃.length > ☃)
    {
      String ☃ = a(☃, ☃, ☃).c();
      if (☃.length() > 32) {
        throw new ExceptionInvalidSyntax("commands.scoreboard.teams.add.displayTooLong", new Object[] { ☃, Integer.valueOf(32) });
      }
      if (☃.length() > 0) {
        ☃.createTeam(☃).setDisplayName(☃);
      } else {
        ☃.createTeam(☃);
      }
    }
    else
    {
      ☃.createTeam(☃);
    }
    a(☃, this, "commands.scoreboard.teams.add.success", new Object[] { ☃ });
  }
  
  protected void d(ICommandListener ☃, String[] ☃, int ☃)
    throws CommandException
  {
    ScoreboardTeam ☃ = e(☃[(☃++)]);
    if (☃ == null) {
      return;
    }
    String ☃ = ☃[(☃++)].toLowerCase();
    if ((!☃.equalsIgnoreCase("color")) && (!☃.equalsIgnoreCase("friendlyfire")) && (!☃.equalsIgnoreCase("seeFriendlyInvisibles")) && (!☃.equalsIgnoreCase("nametagVisibility")) && (!☃.equalsIgnoreCase("deathMessageVisibility"))) {
      throw new ExceptionUsage("commands.scoreboard.teams.option.usage", new Object[0]);
    }
    if (☃.length == 4)
    {
      if (☃.equalsIgnoreCase("color")) {
        throw new ExceptionUsage("commands.scoreboard.teams.option.noValue", new Object[] { ☃, a(EnumChatFormat.a(true, false)) });
      }
      if ((☃.equalsIgnoreCase("friendlyfire")) || (☃.equalsIgnoreCase("seeFriendlyInvisibles"))) {
        throw new ExceptionUsage("commands.scoreboard.teams.option.noValue", new Object[] { ☃, a(Arrays.asList(new String[] { "true", "false" })) });
      }
      if ((☃.equalsIgnoreCase("nametagVisibility")) || (☃.equalsIgnoreCase("deathMessageVisibility"))) {
        throw new ExceptionUsage("commands.scoreboard.teams.option.noValue", new Object[] { ☃, a(ScoreboardTeamBase.EnumNameTagVisibility.a()) });
      }
      throw new ExceptionUsage("commands.scoreboard.teams.option.usage", new Object[0]);
    }
    String ☃ = ☃[☃];
    if (☃.equalsIgnoreCase("color"))
    {
      EnumChatFormat ☃ = EnumChatFormat.b(☃);
      if ((☃ == null) || (☃.isFormat())) {
        throw new ExceptionUsage("commands.scoreboard.teams.option.noValue", new Object[] { ☃, a(EnumChatFormat.a(true, false)) });
      }
      ☃.a(☃);
      ☃.setPrefix(☃.toString());
      ☃.setSuffix(EnumChatFormat.RESET.toString());
    }
    else if (☃.equalsIgnoreCase("friendlyfire"))
    {
      if ((!☃.equalsIgnoreCase("true")) && (!☃.equalsIgnoreCase("false"))) {
        throw new ExceptionUsage("commands.scoreboard.teams.option.noValue", new Object[] { ☃, a(Arrays.asList(new String[] { "true", "false" })) });
      }
      ☃.setAllowFriendlyFire(☃.equalsIgnoreCase("true"));
    }
    else if (☃.equalsIgnoreCase("seeFriendlyInvisibles"))
    {
      if ((!☃.equalsIgnoreCase("true")) && (!☃.equalsIgnoreCase("false"))) {
        throw new ExceptionUsage("commands.scoreboard.teams.option.noValue", new Object[] { ☃, a(Arrays.asList(new String[] { "true", "false" })) });
      }
      ☃.setCanSeeFriendlyInvisibles(☃.equalsIgnoreCase("true"));
    }
    else if (☃.equalsIgnoreCase("nametagVisibility"))
    {
      ScoreboardTeamBase.EnumNameTagVisibility ☃ = ScoreboardTeamBase.EnumNameTagVisibility.a(☃);
      if (☃ == null) {
        throw new ExceptionUsage("commands.scoreboard.teams.option.noValue", new Object[] { ☃, a(ScoreboardTeamBase.EnumNameTagVisibility.a()) });
      }
      ☃.setNameTagVisibility(☃);
    }
    else if (☃.equalsIgnoreCase("deathMessageVisibility"))
    {
      ScoreboardTeamBase.EnumNameTagVisibility ☃ = ScoreboardTeamBase.EnumNameTagVisibility.a(☃);
      if (☃ == null) {
        throw new ExceptionUsage("commands.scoreboard.teams.option.noValue", new Object[] { ☃, a(ScoreboardTeamBase.EnumNameTagVisibility.a()) });
      }
      ☃.b(☃);
    }
    a(☃, this, "commands.scoreboard.teams.option.success", new Object[] { ☃, ☃.getName(), ☃ });
  }
  
  protected void e(ICommandListener ☃, String[] ☃, int ☃)
    throws CommandException
  {
    Scoreboard ☃ = d();
    ScoreboardTeam ☃ = e(☃[☃]);
    if (☃ == null) {
      return;
    }
    ☃.removeTeam(☃);
    a(☃, this, "commands.scoreboard.teams.remove.success", new Object[] { ☃.getName() });
  }
  
  protected void f(ICommandListener ☃, String[] ☃, int ☃)
    throws CommandException
  {
    Scoreboard ☃ = d();
    if (☃.length > ☃)
    {
      ScoreboardTeam ☃ = e(☃[☃]);
      if (☃ == null) {
        return;
      }
      Collection<String> ☃ = ☃.getPlayerNameSet();
      ☃.a(CommandObjectiveExecutor.EnumCommandResult.QUERY_RESULT, ☃.size());
      if (☃.size() > 0)
      {
        ChatMessage ☃ = new ChatMessage("commands.scoreboard.teams.list.player.count", new Object[] { Integer.valueOf(☃.size()), ☃.getName() });
        ☃.getChatModifier().setColor(EnumChatFormat.DARK_GREEN);
        ☃.sendMessage(☃);
        ☃.sendMessage(new ChatComponentText(a(☃.toArray())));
      }
      else
      {
        throw new CommandException("commands.scoreboard.teams.list.player.empty", new Object[] { ☃.getName() });
      }
    }
    else
    {
      Collection<ScoreboardTeam> ☃ = ☃.getTeams();
      
      ☃.a(CommandObjectiveExecutor.EnumCommandResult.QUERY_RESULT, ☃.size());
      if (☃.size() > 0)
      {
        ChatMessage ☃ = new ChatMessage("commands.scoreboard.teams.list.count", new Object[] { Integer.valueOf(☃.size()) });
        ☃.getChatModifier().setColor(EnumChatFormat.DARK_GREEN);
        ☃.sendMessage(☃);
        for (ScoreboardTeam ☃ : ☃) {
          ☃.sendMessage(new ChatMessage("commands.scoreboard.teams.list.entry", new Object[] { ☃.getName(), ☃.getDisplayName(), Integer.valueOf(☃.getPlayerNameSet().size()) }));
        }
      }
      else
      {
        throw new CommandException("commands.scoreboard.teams.list.empty", new Object[0]);
      }
    }
  }
  
  protected void g(ICommandListener ☃, String[] ☃, int ☃)
    throws CommandException
  {
    Scoreboard ☃ = d();
    String ☃ = ☃[(☃++)];
    Set<String> ☃ = Sets.newHashSet();
    Set<String> ☃ = Sets.newHashSet();
    if (((☃ instanceof EntityHuman)) && (☃ == ☃.length))
    {
      String ☃ = b(☃).getName();
      if (☃.addPlayerToTeam(☃, ☃)) {
        ☃.add(☃);
      } else {
        ☃.add(☃);
      }
    }
    else
    {
      while (☃ < ☃.length)
      {
        String ☃ = ☃[(☃++)];
        if (☃.startsWith("@"))
        {
          List<Entity> ☃ = c(☃, ☃);
          for (Entity ☃ : ☃)
          {
            String ☃ = e(☃, ☃.getUniqueID().toString());
            if (☃.addPlayerToTeam(☃, ☃)) {
              ☃.add(☃);
            } else {
              ☃.add(☃);
            }
          }
        }
        else
        {
          String ☃ = e(☃, ☃);
          if (☃.addPlayerToTeam(☃, ☃)) {
            ☃.add(☃);
          } else {
            ☃.add(☃);
          }
        }
      }
    }
    if (!☃.isEmpty())
    {
      ☃.a(CommandObjectiveExecutor.EnumCommandResult.AFFECTED_ENTITIES, ☃.size());
      a(☃, this, "commands.scoreboard.teams.join.success", new Object[] { Integer.valueOf(☃.size()), ☃, a(☃.toArray(new String[☃.size()])) });
    }
    if (!☃.isEmpty()) {
      throw new CommandException("commands.scoreboard.teams.join.failure", new Object[] { Integer.valueOf(☃.size()), ☃, a(☃.toArray(new String[☃.size()])) });
    }
  }
  
  protected void h(ICommandListener ☃, String[] ☃, int ☃)
    throws CommandException
  {
    Scoreboard ☃ = d();
    Set<String> ☃ = Sets.newHashSet();
    Set<String> ☃ = Sets.newHashSet();
    if (((☃ instanceof EntityHuman)) && (☃ == ☃.length))
    {
      String ☃ = b(☃).getName();
      if (☃.removePlayerFromTeam(☃)) {
        ☃.add(☃);
      } else {
        ☃.add(☃);
      }
    }
    else
    {
      while (☃ < ☃.length)
      {
        String ☃ = ☃[(☃++)];
        if (☃.startsWith("@"))
        {
          List<Entity> ☃ = c(☃, ☃);
          for (Entity ☃ : ☃)
          {
            String ☃ = e(☃, ☃.getUniqueID().toString());
            if (☃.removePlayerFromTeam(☃)) {
              ☃.add(☃);
            } else {
              ☃.add(☃);
            }
          }
        }
        else
        {
          String ☃ = e(☃, ☃);
          if (☃.removePlayerFromTeam(☃)) {
            ☃.add(☃);
          } else {
            ☃.add(☃);
          }
        }
      }
    }
    if (!☃.isEmpty())
    {
      ☃.a(CommandObjectiveExecutor.EnumCommandResult.AFFECTED_ENTITIES, ☃.size());
      a(☃, this, "commands.scoreboard.teams.leave.success", new Object[] { Integer.valueOf(☃.size()), a(☃.toArray(new String[☃.size()])) });
    }
    if (!☃.isEmpty()) {
      throw new CommandException("commands.scoreboard.teams.leave.failure", new Object[] { Integer.valueOf(☃.size()), a(☃.toArray(new String[☃.size()])) });
    }
  }
  
  protected void i(ICommandListener ☃, String[] ☃, int ☃)
    throws CommandException
  {
    Scoreboard ☃ = d();
    ScoreboardTeam ☃ = e(☃[☃]);
    if (☃ == null) {
      return;
    }
    Collection<String> ☃ = Lists.newArrayList(☃.getPlayerNameSet());
    ☃.a(CommandObjectiveExecutor.EnumCommandResult.AFFECTED_ENTITIES, ☃.size());
    if (☃.isEmpty()) {
      throw new CommandException("commands.scoreboard.teams.empty.alreadyEmpty", new Object[] { ☃.getName() });
    }
    for (String ☃ : ☃) {
      ☃.removePlayerFromTeam(☃, ☃);
    }
    a(☃, this, "commands.scoreboard.teams.empty.success", new Object[] { Integer.valueOf(☃.size()), ☃.getName() });
  }
  
  protected void h(ICommandListener ☃, String ☃)
    throws CommandException
  {
    Scoreboard ☃ = d();
    ScoreboardObjective ☃ = a(☃, false);
    
    ☃.unregisterObjective(☃);
    
    a(☃, this, "commands.scoreboard.objectives.remove.success", new Object[] { ☃ });
  }
  
  protected void d(ICommandListener ☃)
    throws CommandException
  {
    Scoreboard ☃ = d();
    Collection<ScoreboardObjective> ☃ = ☃.getObjectives();
    if (☃.size() > 0)
    {
      ChatMessage ☃ = new ChatMessage("commands.scoreboard.objectives.list.count", new Object[] { Integer.valueOf(☃.size()) });
      ☃.getChatModifier().setColor(EnumChatFormat.DARK_GREEN);
      ☃.sendMessage(☃);
      for (ScoreboardObjective ☃ : ☃) {
        ☃.sendMessage(new ChatMessage("commands.scoreboard.objectives.list.entry", new Object[] { ☃.getName(), ☃.getDisplayName(), ☃.getCriteria().getName() }));
      }
    }
    else
    {
      throw new CommandException("commands.scoreboard.objectives.list.empty", new Object[0]);
    }
  }
  
  protected void j(ICommandListener ☃, String[] ☃, int ☃)
    throws CommandException
  {
    Scoreboard ☃ = d();
    String ☃ = ☃[(☃++)];
    int ☃ = Scoreboard.getSlotForName(☃);
    ScoreboardObjective ☃ = null;
    if (☃.length == 4) {
      ☃ = a(☃[☃], false);
    }
    if (☃ < 0) {
      throw new CommandException("commands.scoreboard.objectives.setdisplay.invalidSlot", new Object[] { ☃ });
    }
    ☃.setDisplaySlot(☃, ☃);
    if (☃ != null) {
      a(☃, this, "commands.scoreboard.objectives.setdisplay.successSet", new Object[] { Scoreboard.getSlotName(☃), ☃.getName() });
    } else {
      a(☃, this, "commands.scoreboard.objectives.setdisplay.successCleared", new Object[] { Scoreboard.getSlotName(☃) });
    }
  }
  
  protected void k(ICommandListener ☃, String[] ☃, int ☃)
    throws CommandException
  {
    Scoreboard ☃ = d();
    if (☃.length > ☃)
    {
      String ☃ = e(☃, ☃[☃]);
      Map<ScoreboardObjective, ScoreboardScore> ☃ = ☃.getPlayerObjectives(☃);
      
      ☃.a(CommandObjectiveExecutor.EnumCommandResult.QUERY_RESULT, ☃.size());
      if (☃.size() > 0)
      {
        ChatMessage ☃ = new ChatMessage("commands.scoreboard.players.list.player.count", new Object[] { Integer.valueOf(☃.size()), ☃ });
        ☃.getChatModifier().setColor(EnumChatFormat.DARK_GREEN);
        ☃.sendMessage(☃);
        for (ScoreboardScore ☃ : ☃.values()) {
          ☃.sendMessage(new ChatMessage("commands.scoreboard.players.list.player.entry", new Object[] { Integer.valueOf(☃.getScore()), ☃.getObjective().getDisplayName(), ☃.getObjective().getName() }));
        }
      }
      else
      {
        throw new CommandException("commands.scoreboard.players.list.player.empty", new Object[] { ☃ });
      }
    }
    else
    {
      Collection<String> ☃ = ☃.getPlayers();
      
      ☃.a(CommandObjectiveExecutor.EnumCommandResult.QUERY_RESULT, ☃.size());
      if (☃.size() > 0)
      {
        ChatMessage ☃ = new ChatMessage("commands.scoreboard.players.list.count", new Object[] { Integer.valueOf(☃.size()) });
        ☃.getChatModifier().setColor(EnumChatFormat.DARK_GREEN);
        ☃.sendMessage(☃);
        ☃.sendMessage(new ChatComponentText(a(☃.toArray())));
      }
      else
      {
        throw new CommandException("commands.scoreboard.players.list.empty", new Object[0]);
      }
    }
  }
  
  protected void l(ICommandListener ☃, String[] ☃, int ☃)
    throws CommandException
  {
    String ☃ = ☃[(☃ - 1)];
    int ☃ = ☃;
    String ☃ = e(☃, ☃[(☃++)]);
    if (☃.length() > 40) {
      throw new ExceptionInvalidSyntax("commands.scoreboard.players.name.tooLong", new Object[] { ☃, Integer.valueOf(40) });
    }
    ScoreboardObjective ☃ = a(☃[(☃++)], true);
    int ☃ = ☃.equalsIgnoreCase("set") ? a(☃[(☃++)]) : a(☃[(☃++)], 0);
    if (☃.length > ☃)
    {
      Entity ☃ = b(☃, ☃[☃]);
      try
      {
        NBTTagCompound ☃ = MojangsonParser.parse(a(☃, ☃));
        NBTTagCompound ☃ = new NBTTagCompound();
        ☃.e(☃);
        if (!GameProfileSerializer.a(☃, ☃, true)) {
          throw new CommandException("commands.scoreboard.players.set.tagMismatch", new Object[] { ☃ });
        }
      }
      catch (MojangsonParseException ☃)
      {
        throw new CommandException("commands.scoreboard.players.set.tagError", new Object[] { ☃.getMessage() });
      }
    }
    Scoreboard ☃ = d();
    ScoreboardScore ☃ = ☃.getPlayerScoreForObjective(☃, ☃);
    if (☃.equalsIgnoreCase("set")) {
      ☃.setScore(☃);
    } else if (☃.equalsIgnoreCase("add")) {
      ☃.addScore(☃);
    } else {
      ☃.removeScore(☃);
    }
    a(☃, this, "commands.scoreboard.players.set.success", new Object[] { ☃.getName(), ☃, Integer.valueOf(☃.getScore()) });
  }
  
  protected void m(ICommandListener ☃, String[] ☃, int ☃)
    throws CommandException
  {
    Scoreboard ☃ = d();
    String ☃ = e(☃, ☃[(☃++)]);
    if (☃.length > ☃)
    {
      ScoreboardObjective ☃ = a(☃[(☃++)], false);
      ☃.resetPlayerScores(☃, ☃);
      a(☃, this, "commands.scoreboard.players.resetscore.success", new Object[] { ☃.getName(), ☃ });
    }
    else
    {
      ☃.resetPlayerScores(☃, null);
      a(☃, this, "commands.scoreboard.players.reset.success", new Object[] { ☃ });
    }
  }
  
  protected void n(ICommandListener ☃, String[] ☃, int ☃)
    throws CommandException
  {
    Scoreboard ☃ = d();
    String ☃ = d(☃, ☃[(☃++)]);
    if (☃.length() > 40) {
      throw new ExceptionInvalidSyntax("commands.scoreboard.players.name.tooLong", new Object[] { ☃, Integer.valueOf(40) });
    }
    ScoreboardObjective ☃ = a(☃[☃], false);
    if (☃.getCriteria() != IScoreboardCriteria.c) {
      throw new CommandException("commands.scoreboard.players.enable.noTrigger", new Object[] { ☃.getName() });
    }
    ScoreboardScore ☃ = ☃.getPlayerScoreForObjective(☃, ☃);
    ☃.a(false);
    a(☃, this, "commands.scoreboard.players.enable.success", new Object[] { ☃.getName(), ☃ });
  }
  
  protected void o(ICommandListener ☃, String[] ☃, int ☃)
    throws CommandException
  {
    Scoreboard ☃ = d();
    String ☃ = e(☃, ☃[(☃++)]);
    if (☃.length() > 40) {
      throw new ExceptionInvalidSyntax("commands.scoreboard.players.name.tooLong", new Object[] { ☃, Integer.valueOf(40) });
    }
    ScoreboardObjective ☃ = a(☃[(☃++)], false);
    if (!☃.b(☃, ☃)) {
      throw new CommandException("commands.scoreboard.players.test.notFound", new Object[] { ☃.getName(), ☃ });
    }
    int ☃ = ☃[☃].equals("*") ? Integer.MIN_VALUE : a(☃[☃]);
    ☃++;
    int ☃ = (☃ >= ☃.length) || (☃[☃].equals("*")) ? Integer.MAX_VALUE : a(☃[☃], ☃);
    
    ScoreboardScore ☃ = ☃.getPlayerScoreForObjective(☃, ☃);
    if ((☃.getScore() < ☃) || (☃.getScore() > ☃)) {
      throw new CommandException("commands.scoreboard.players.test.failed", new Object[] { Integer.valueOf(☃.getScore()), Integer.valueOf(☃), Integer.valueOf(☃) });
    }
    a(☃, this, "commands.scoreboard.players.test.success", new Object[] { Integer.valueOf(☃.getScore()), Integer.valueOf(☃), Integer.valueOf(☃) });
  }
  
  protected void p(ICommandListener ☃, String[] ☃, int ☃)
    throws CommandException
  {
    Scoreboard ☃ = d();
    String ☃ = e(☃, ☃[(☃++)]);
    ScoreboardObjective ☃ = a(☃[(☃++)], true);
    
    String ☃ = ☃[(☃++)];
    String ☃ = e(☃, ☃[(☃++)]);
    ScoreboardObjective ☃ = a(☃[☃], false);
    if (☃.length() > 40) {
      throw new ExceptionInvalidSyntax("commands.scoreboard.players.name.tooLong", new Object[] { ☃, Integer.valueOf(40) });
    }
    if (☃.length() > 40) {
      throw new ExceptionInvalidSyntax("commands.scoreboard.players.name.tooLong", new Object[] { ☃, Integer.valueOf(40) });
    }
    ScoreboardScore ☃ = ☃.getPlayerScoreForObjective(☃, ☃);
    if (!☃.b(☃, ☃)) {
      throw new CommandException("commands.scoreboard.players.operation.notFound", new Object[] { ☃.getName(), ☃ });
    }
    ScoreboardScore ☃ = ☃.getPlayerScoreForObjective(☃, ☃);
    if (☃.equals("+="))
    {
      ☃.setScore(☃.getScore() + ☃.getScore());
    }
    else if (☃.equals("-="))
    {
      ☃.setScore(☃.getScore() - ☃.getScore());
    }
    else if (☃.equals("*="))
    {
      ☃.setScore(☃.getScore() * ☃.getScore());
    }
    else if (☃.equals("/="))
    {
      if (☃.getScore() != 0) {
        ☃.setScore(☃.getScore() / ☃.getScore());
      }
    }
    else if (☃.equals("%="))
    {
      if (☃.getScore() != 0) {
        ☃.setScore(☃.getScore() % ☃.getScore());
      }
    }
    else if (☃.equals("="))
    {
      ☃.setScore(☃.getScore());
    }
    else if (☃.equals("<"))
    {
      ☃.setScore(Math.min(☃.getScore(), ☃.getScore()));
    }
    else if (☃.equals(">"))
    {
      ☃.setScore(Math.max(☃.getScore(), ☃.getScore()));
    }
    else if (☃.equals("><"))
    {
      int ☃ = ☃.getScore();
      ☃.setScore(☃.getScore());
      ☃.setScore(☃);
    }
    else
    {
      throw new CommandException("commands.scoreboard.players.operation.invalidOperation", new Object[] { ☃ });
    }
    a(☃, this, "commands.scoreboard.players.operation.success", new Object[0]);
  }
  
  public List<String> tabComplete(ICommandListener ☃, String[] ☃, BlockPosition ☃)
  {
    if (☃.length == 1) {
      return a(☃, new String[] { "objectives", "players", "teams" });
    }
    if (☃[0].equalsIgnoreCase("objectives"))
    {
      if (☃.length == 2) {
        return a(☃, new String[] { "list", "add", "remove", "setdisplay" });
      }
      if (☃[1].equalsIgnoreCase("add"))
      {
        if (☃.length == 4)
        {
          Set<String> ☃ = IScoreboardCriteria.criteria.keySet();
          return a(☃, ☃);
        }
      }
      else if (☃[1].equalsIgnoreCase("remove"))
      {
        if (☃.length == 3) {
          return a(☃, a(false));
        }
      }
      else if (☃[1].equalsIgnoreCase("setdisplay"))
      {
        if (☃.length == 3) {
          return a(☃, Scoreboard.h());
        }
        if (☃.length == 4) {
          return a(☃, a(false));
        }
      }
    }
    else if (☃[0].equalsIgnoreCase("players"))
    {
      if (☃.length == 2) {
        return a(☃, new String[] { "set", "add", "remove", "reset", "list", "enable", "test", "operation" });
      }
      if ((☃[1].equalsIgnoreCase("set")) || (☃[1].equalsIgnoreCase("add")) || (☃[1].equalsIgnoreCase("remove")) || (☃[1].equalsIgnoreCase("reset")))
      {
        if (☃.length == 3) {
          return a(☃, MinecraftServer.getServer().getPlayers());
        }
        if (☃.length == 4) {
          return a(☃, a(true));
        }
      }
      else if (☃[1].equalsIgnoreCase("enable"))
      {
        if (☃.length == 3) {
          return a(☃, MinecraftServer.getServer().getPlayers());
        }
        if (☃.length == 4) {
          return a(☃, e());
        }
      }
      else if ((☃[1].equalsIgnoreCase("list")) || (☃[1].equalsIgnoreCase("test")))
      {
        if (☃.length == 3) {
          return a(☃, d().getPlayers());
        }
        if ((☃.length == 4) && (☃[1].equalsIgnoreCase("test"))) {
          return a(☃, a(false));
        }
      }
      else if (☃[1].equalsIgnoreCase("operation"))
      {
        if (☃.length == 3) {
          return a(☃, d().getPlayers());
        }
        if (☃.length == 4) {
          return a(☃, a(true));
        }
        if (☃.length == 5) {
          return a(☃, new String[] { "+=", "-=", "*=", "/=", "%=", "=", "<", ">", "><" });
        }
        if (☃.length == 6) {
          return a(☃, MinecraftServer.getServer().getPlayers());
        }
        if (☃.length == 7) {
          return a(☃, a(false));
        }
      }
    }
    else if (☃[0].equalsIgnoreCase("teams"))
    {
      if (☃.length == 2) {
        return a(☃, new String[] { "add", "remove", "join", "leave", "empty", "list", "option" });
      }
      if (☃[1].equalsIgnoreCase("join"))
      {
        if (☃.length == 3) {
          return a(☃, d().getTeamNames());
        }
        if (☃.length >= 4) {
          return a(☃, MinecraftServer.getServer().getPlayers());
        }
      }
      else
      {
        if (☃[1].equalsIgnoreCase("leave")) {
          return a(☃, MinecraftServer.getServer().getPlayers());
        }
        if ((☃[1].equalsIgnoreCase("empty")) || (☃[1].equalsIgnoreCase("list")) || (☃[1].equalsIgnoreCase("remove")))
        {
          if (☃.length == 3) {
            return a(☃, d().getTeamNames());
          }
        }
        else if (☃[1].equalsIgnoreCase("option"))
        {
          if (☃.length == 3) {
            return a(☃, d().getTeamNames());
          }
          if (☃.length == 4) {
            return a(☃, new String[] { "color", "friendlyfire", "seeFriendlyInvisibles", "nametagVisibility", "deathMessageVisibility" });
          }
          if (☃.length == 5)
          {
            if (☃[3].equalsIgnoreCase("color")) {
              return a(☃, EnumChatFormat.a(true, false));
            }
            if ((☃[3].equalsIgnoreCase("nametagVisibility")) || (☃[3].equalsIgnoreCase("deathMessageVisibility"))) {
              return a(☃, ScoreboardTeamBase.EnumNameTagVisibility.a());
            }
            if ((☃[3].equalsIgnoreCase("friendlyfire")) || (☃[3].equalsIgnoreCase("seeFriendlyInvisibles"))) {
              return a(☃, new String[] { "true", "false" });
            }
          }
        }
      }
    }
    return null;
  }
  
  protected List<String> a(boolean ☃)
  {
    Collection<ScoreboardObjective> ☃ = d().getObjectives();
    List<String> ☃ = Lists.newArrayList();
    for (ScoreboardObjective ☃ : ☃) {
      if ((!☃) || (!☃.getCriteria().isReadOnly())) {
        ☃.add(☃.getName());
      }
    }
    return ☃;
  }
  
  protected List<String> e()
  {
    Collection<ScoreboardObjective> ☃ = d().getObjectives();
    List<String> ☃ = Lists.newArrayList();
    for (ScoreboardObjective ☃ : ☃) {
      if (☃.getCriteria() == IScoreboardCriteria.c) {
        ☃.add(☃.getName());
      }
    }
    return ☃;
  }
  
  public boolean isListStart(String[] ☃, int ☃)
  {
    if (☃[0].equalsIgnoreCase("players"))
    {
      if ((☃.length > 1) && (☃[1].equalsIgnoreCase("operation"))) {
        return (☃ == 2) || (☃ == 5);
      }
      return ☃ == 2;
    }
    if (☃[0].equalsIgnoreCase("teams")) {
      return ☃ == 2;
    }
    return false;
  }
}
