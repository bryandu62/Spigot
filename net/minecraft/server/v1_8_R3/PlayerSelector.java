package net.minecraft.server.v1_8_R3;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerSelector
{
  private static final Pattern a = Pattern.compile("^@([pare])(?:\\[([\\w=,!-]*)\\])?$");
  private static final Pattern b = Pattern.compile("\\G([-!]?[\\w-]*)(?:$|,)");
  private static final Pattern c = Pattern.compile("\\G(\\w+)=([-!]?[\\w-]*)(?:$|,)");
  private static final Set<String> d = Sets.newHashSet(new String[] { "x", "y", "z", "dx", "dy", "dz", "rm", "r" });
  
  public static EntityPlayer getPlayer(ICommandListener ☃, String ☃)
  {
    return (EntityPlayer)getEntity(☃, ☃, EntityPlayer.class);
  }
  
  public static <T extends Entity> T getEntity(ICommandListener ☃, String ☃, Class<? extends T> ☃)
  {
    List<T> ☃ = getPlayers(☃, ☃, ☃);
    return ☃.size() == 1 ? (Entity)☃.get(0) : null;
  }
  
  public static IChatBaseComponent getPlayerNames(ICommandListener ☃, String ☃)
  {
    List<Entity> ☃ = getPlayers(☃, ☃, Entity.class);
    if (☃.isEmpty()) {
      return null;
    }
    List<IChatBaseComponent> ☃ = Lists.newArrayList();
    for (Entity ☃ : ☃) {
      ☃.add(☃.getScoreboardDisplayName());
    }
    return CommandAbstract.a(☃);
  }
  
  public static <T extends Entity> List<T> getPlayers(ICommandListener ☃, String ☃, Class<? extends T> ☃)
  {
    Matcher ☃ = a.matcher(☃);
    if ((☃.matches()) && (☃.a(1, "@")))
    {
      Map<String, String> ☃ = c(☃.group(2));
      if (!b(☃, ☃)) {
        return Collections.emptyList();
      }
      String ☃ = ☃.group(1);
      BlockPosition ☃ = b(☃, ☃.getChunkCoordinates());
      List<World> ☃ = a(☃, ☃);
      List<T> ☃ = Lists.newArrayList();
      for (World ☃ : ☃) {
        if (☃ != null)
        {
          List<Predicate<Entity>> ☃ = Lists.newArrayList();
          ☃.addAll(a(☃, ☃));
          ☃.addAll(b(☃));
          ☃.addAll(c(☃));
          ☃.addAll(d(☃));
          ☃.addAll(e(☃));
          ☃.addAll(f(☃));
          ☃.addAll(a(☃, ☃));
          ☃.addAll(g(☃));
          
          ☃.addAll(a(☃, ☃, ☃, ☃, ☃, ☃));
        }
      }
      return a(☃, ☃, ☃, ☃, ☃, ☃);
    }
    return Collections.emptyList();
  }
  
  private static List<World> a(ICommandListener ☃, Map<String, String> ☃)
  {
    List<World> ☃ = Lists.newArrayList();
    if (h(☃)) {
      ☃.add(☃.getWorld());
    } else {
      Collections.addAll(☃, MinecraftServer.getServer().worldServer);
    }
    return ☃;
  }
  
  private static <T extends Entity> boolean b(ICommandListener ☃, Map<String, String> ☃)
  {
    String ☃ = b(☃, "type");
    ☃ = (☃ != null) && (☃.startsWith("!")) ? ☃.substring(1) : ☃;
    if ((☃ != null) && (!EntityTypes.b(☃)))
    {
      ChatMessage ☃ = new ChatMessage("commands.generic.entity.invalidType", new Object[] { ☃ });
      ☃.getChatModifier().setColor(EnumChatFormat.RED);
      ☃.sendMessage(☃);
      return false;
    }
    return true;
  }
  
  private static List<Predicate<Entity>> a(Map<String, String> ☃, String ☃)
  {
    List<Predicate<Entity>> ☃ = Lists.newArrayList();
    String ☃ = b(☃, "type");
    final boolean ☃ = (☃ != null) && (☃.startsWith("!"));
    if (☃) {
      ☃ = ☃.substring(1);
    }
    String ☃ = ☃;
    
    boolean ☃ = !☃.equals("e");
    boolean ☃ = (☃.equals("r")) && (☃ != null);
    if (((☃ != null) && (☃.equals("e"))) || (☃)) {
      ☃.add(new Predicate()
      {
        public boolean a(Entity ☃)
        {
          return EntityTypes.a(☃, this.a) != ☃;
        }
      });
    } else if (☃) {
      ☃.add(new Predicate()
      {
        public boolean a(Entity ☃)
        {
          return ☃ instanceof EntityHuman;
        }
      });
    }
    return ☃;
  }
  
  private static List<Predicate<Entity>> b(Map<String, String> ☃)
  {
    List<Predicate<Entity>> ☃ = Lists.newArrayList();
    int ☃ = a(☃, "lm", -1);
    final int ☃ = a(☃, "l", -1);
    if ((☃ > -1) || (☃ > -1)) {
      ☃.add(new Predicate()
      {
        public boolean a(Entity ☃)
        {
          if (!(☃ instanceof EntityPlayer)) {
            return false;
          }
          EntityPlayer ☃ = (EntityPlayer)☃;
          return ((this.a <= -1) || (☃.expLevel >= this.a)) && ((☃ <= -1) || (☃.expLevel <= ☃));
        }
      });
    }
    return ☃;
  }
  
  private static List<Predicate<Entity>> c(Map<String, String> ☃)
  {
    List<Predicate<Entity>> ☃ = Lists.newArrayList();
    int ☃ = a(☃, "m", WorldSettings.EnumGamemode.NOT_SET.getId());
    if (☃ != WorldSettings.EnumGamemode.NOT_SET.getId()) {
      ☃.add(new Predicate()
      {
        public boolean a(Entity ☃)
        {
          if (!(☃ instanceof EntityPlayer)) {
            return false;
          }
          EntityPlayer ☃ = (EntityPlayer)☃;
          return ☃.playerInteractManager.getGameMode().getId() == this.a;
        }
      });
    }
    return ☃;
  }
  
  private static List<Predicate<Entity>> d(Map<String, String> ☃)
  {
    List<Predicate<Entity>> ☃ = Lists.newArrayList();
    String ☃ = b(☃, "team");
    final boolean ☃ = (☃ != null) && (☃.startsWith("!"));
    if (☃) {
      ☃ = ☃.substring(1);
    }
    String ☃ = ☃;
    if (☃ != null) {
      ☃.add(new Predicate()
      {
        public boolean a(Entity ☃)
        {
          if (!(☃ instanceof EntityLiving)) {
            return false;
          }
          EntityLiving ☃ = (EntityLiving)☃;
          ScoreboardTeamBase ☃ = ☃.getScoreboardTeam();
          String ☃ = ☃ == null ? "" : ☃.getName();
          return ☃.equals(this.a) != ☃;
        }
      });
    }
    return ☃;
  }
  
  private static List<Predicate<Entity>> e(Map<String, String> ☃)
  {
    List<Predicate<Entity>> ☃ = Lists.newArrayList();
    Map<String, Integer> ☃ = a(☃);
    if ((☃ != null) && (☃.size() > 0)) {
      ☃.add(new Predicate()
      {
        public boolean a(Entity ☃)
        {
          Scoreboard ☃ = MinecraftServer.getServer().getWorldServer(0).getScoreboard();
          for (Map.Entry<String, Integer> ☃ : this.a.entrySet())
          {
            String ☃ = (String)☃.getKey();
            boolean ☃ = false;
            if ((☃.endsWith("_min")) && (☃.length() > 4))
            {
              ☃ = true;
              ☃ = ☃.substring(0, ☃.length() - 4);
            }
            ScoreboardObjective ☃ = ☃.getObjective(☃);
            if (☃ == null) {
              return false;
            }
            String ☃ = (☃ instanceof EntityPlayer) ? ☃.getName() : ☃.getUniqueID().toString();
            if (!☃.b(☃, ☃)) {
              return false;
            }
            ScoreboardScore ☃ = ☃.getPlayerScoreForObjective(☃, ☃);
            int ☃ = ☃.getScore();
            if ((☃ < ((Integer)☃.getValue()).intValue()) && (☃)) {
              return false;
            }
            if ((☃ > ((Integer)☃.getValue()).intValue()) && (!☃)) {
              return false;
            }
          }
          return true;
        }
      });
    }
    return ☃;
  }
  
  private static List<Predicate<Entity>> f(Map<String, String> ☃)
  {
    List<Predicate<Entity>> ☃ = Lists.newArrayList();
    String ☃ = b(☃, "name");
    final boolean ☃ = (☃ != null) && (☃.startsWith("!"));
    if (☃) {
      ☃ = ☃.substring(1);
    }
    String ☃ = ☃;
    if (☃ != null) {
      ☃.add(new Predicate()
      {
        public boolean a(Entity ☃)
        {
          return ☃.getName().equals(this.a) != ☃;
        }
      });
    }
    return ☃;
  }
  
  private static List<Predicate<Entity>> a(Map<String, String> ☃, BlockPosition ☃)
  {
    List<Predicate<Entity>> ☃ = Lists.newArrayList();
    final int ☃ = a(☃, "rm", -1);
    final int ☃ = a(☃, "r", -1);
    if ((☃ != null) && ((☃ >= 0) || (☃ >= 0)))
    {
      final int ☃ = ☃ * ☃;
      final int ☃ = ☃ * ☃;
      
      ☃.add(new Predicate()
      {
        public boolean a(Entity ☃)
        {
          int ☃ = (int)☃.c(this.a);
          return ((☃ < 0) || (☃ >= ☃)) && ((☃ < 0) || (☃ <= ☃));
        }
      });
    }
    return ☃;
  }
  
  private static List<Predicate<Entity>> g(Map<String, String> ☃)
  {
    List<Predicate<Entity>> ☃ = Lists.newArrayList();
    if ((☃.containsKey("rym")) || (☃.containsKey("ry")))
    {
      int ☃ = a(a(☃, "rym", 0));
      final int ☃ = a(a(☃, "ry", 359));
      
      ☃.add(new Predicate()
      {
        public boolean a(Entity ☃)
        {
          int ☃ = PlayerSelector.a((int)Math.floor(☃.yaw));
          if (this.a > ☃) {
            return (☃ >= this.a) || (☃ <= ☃);
          }
          return (☃ >= this.a) && (☃ <= ☃);
        }
      });
    }
    if ((☃.containsKey("rxm")) || (☃.containsKey("rx")))
    {
      int ☃ = a(a(☃, "rxm", 0));
      final int ☃ = a(a(☃, "rx", 359));
      
      ☃.add(new Predicate()
      {
        public boolean a(Entity ☃)
        {
          int ☃ = PlayerSelector.a((int)Math.floor(☃.pitch));
          if (this.a > ☃) {
            return (☃ >= this.a) || (☃ <= ☃);
          }
          return (☃ >= this.a) && (☃ <= ☃);
        }
      });
    }
    return ☃;
  }
  
  private static <T extends Entity> List<T> a(Map<String, String> ☃, Class<? extends T> ☃, List<Predicate<Entity>> ☃, String ☃, World ☃, BlockPosition ☃)
  {
    List<T> ☃ = Lists.newArrayList();
    String ☃ = b(☃, "type");
    ☃ = (☃ != null) && (☃.startsWith("!")) ? ☃.substring(1) : ☃;
    
    boolean ☃ = !☃.equals("e");
    boolean ☃ = (☃.equals("r")) && (☃ != null);
    
    int ☃ = a(☃, "dx", 0);
    int ☃ = a(☃, "dy", 0);
    int ☃ = a(☃, "dz", 0);
    
    int ☃ = a(☃, "r", -1);
    
    Predicate<Entity> ☃ = Predicates.and(☃);
    Predicate<Entity> ☃ = Predicates.and(IEntitySelector.a, ☃);
    if (☃ != null)
    {
      int ☃ = ☃.players.size();
      int ☃ = ☃.entityList.size();
      boolean ☃ = ☃ < ☃ / 16;
      if ((☃.containsKey("dx")) || (☃.containsKey("dy")) || (☃.containsKey("dz")))
      {
        AxisAlignedBB ☃ = a(☃, ☃, ☃, ☃);
        if ((☃) && (☃) && (!☃))
        {
          Predicate<Entity> ☃ = new Predicate()
          {
            public boolean a(Entity ☃)
            {
              if ((☃.locX < this.a.a) || (☃.locY < this.a.b) || (☃.locZ < this.a.c)) {
                return false;
              }
              if ((☃.locX >= this.a.d) || (☃.locY >= this.a.e) || (☃.locZ >= this.a.f)) {
                return false;
              }
              return true;
            }
          };
          ☃.addAll(☃.b(☃, Predicates.and(☃, ☃)));
        }
        else
        {
          ☃.addAll(☃.a(☃, ☃, ☃));
        }
      }
      else if (☃ >= 0)
      {
        AxisAlignedBB ☃ = new AxisAlignedBB(☃.getX() - ☃, ☃.getY() - ☃, ☃.getZ() - ☃, ☃.getX() + ☃ + 1, ☃.getY() + ☃ + 1, ☃.getZ() + ☃ + 1);
        if ((☃) && (☃) && (!☃)) {
          ☃.addAll(☃.b(☃, ☃));
        } else {
          ☃.addAll(☃.a(☃, ☃, ☃));
        }
      }
      else if (☃.equals("a"))
      {
        ☃.addAll(☃.b(☃, ☃));
      }
      else if ((☃.equals("p")) || ((☃.equals("r")) && (!☃)))
      {
        ☃.addAll(☃.b(☃, ☃));
      }
      else
      {
        ☃.addAll(☃.a(☃, ☃));
      }
    }
    else if (☃.equals("a"))
    {
      ☃.addAll(☃.b(☃, ☃));
    }
    else if ((☃.equals("p")) || ((☃.equals("r")) && (!☃)))
    {
      ☃.addAll(☃.b(☃, ☃));
    }
    else
    {
      ☃.addAll(☃.a(☃, ☃));
    }
    return ☃;
  }
  
  private static <T extends Entity> List<T> a(List<T> ☃, Map<String, String> ☃, ICommandListener ☃, Class<? extends T> ☃, String ☃, BlockPosition ☃)
  {
    int ☃ = a(☃, "c", (☃.equals("a")) || (☃.equals("e")) ? 0 : 1);
    if ((☃.equals("p")) || (☃.equals("a")) || (☃.equals("e")))
    {
      if (☃ != null) {
        Collections.sort(☃, new Comparator()
        {
          public int a(Entity ☃, Entity ☃)
          {
            return ComparisonChain.start().compare(☃.b(this.a), ☃.b(this.a)).result();
          }
        });
      }
    }
    else if (☃.equals("r")) {
      Collections.shuffle(☃);
    }
    Entity ☃ = ☃.f();
    if ((☃ != null) && (☃.isAssignableFrom(☃.getClass())) && (☃ == 1) && (☃.contains(☃)) && (!"r".equals(☃))) {
      ☃ = Lists.newArrayList(new Entity[] { ☃ });
    }
    if (☃ != 0)
    {
      if (☃ < 0) {
        Collections.reverse(☃);
      }
      ☃ = ☃.subList(0, Math.min(Math.abs(☃), ☃.size()));
    }
    return ☃;
  }
  
  private static AxisAlignedBB a(BlockPosition ☃, int ☃, int ☃, int ☃)
  {
    boolean ☃ = ☃ < 0;
    boolean ☃ = ☃ < 0;
    boolean ☃ = ☃ < 0;
    int ☃ = ☃.getX() + (☃ ? ☃ : 0);
    int ☃ = ☃.getY() + (☃ ? ☃ : 0);
    int ☃ = ☃.getZ() + (☃ ? ☃ : 0);
    int ☃ = ☃.getX() + (☃ ? 0 : ☃) + 1;
    int ☃ = ☃.getY() + (☃ ? 0 : ☃) + 1;
    int ☃ = ☃.getZ() + (☃ ? 0 : ☃) + 1;
    return new AxisAlignedBB(☃, ☃, ☃, ☃, ☃, ☃);
  }
  
  public static int a(int ☃)
  {
    ☃ %= 360;
    if (☃ >= 160) {
      ☃ -= 360;
    }
    if (☃ < 0) {
      ☃ += 360;
    }
    return ☃;
  }
  
  private static BlockPosition b(Map<String, String> ☃, BlockPosition ☃)
  {
    return new BlockPosition(a(☃, "x", ☃.getX()), a(☃, "y", ☃.getY()), a(☃, "z", ☃.getZ()));
  }
  
  private static boolean h(Map<String, String> ☃)
  {
    for (String ☃ : d) {
      if (☃.containsKey(☃)) {
        return true;
      }
    }
    return false;
  }
  
  private static int a(Map<String, String> ☃, String ☃, int ☃)
  {
    return ☃.containsKey(☃) ? MathHelper.a((String)☃.get(☃), ☃) : ☃;
  }
  
  private static String b(Map<String, String> ☃, String ☃)
  {
    return (String)☃.get(☃);
  }
  
  public static Map<String, Integer> a(Map<String, String> ☃)
  {
    Map<String, Integer> ☃ = Maps.newHashMap();
    for (String ☃ : ☃.keySet()) {
      if ((☃.startsWith("score_")) && (☃.length() > "score_".length())) {
        ☃.put(☃.substring("score_".length()), Integer.valueOf(MathHelper.a((String)☃.get(☃), 1)));
      }
    }
    return ☃;
  }
  
  public static boolean isList(String ☃)
  {
    Matcher ☃ = a.matcher(☃);
    if (☃.matches())
    {
      Map<String, String> ☃ = c(☃.group(2));
      String ☃ = ☃.group(1);
      int ☃ = ("a".equals(☃)) || ("e".equals(☃)) ? 0 : 1;
      return a(☃, "c", ☃) != 1;
    }
    return false;
  }
  
  public static boolean isPattern(String ☃)
  {
    return a.matcher(☃).matches();
  }
  
  private static Map<String, String> c(String ☃)
  {
    Map<String, String> ☃ = Maps.newHashMap();
    if (☃ == null) {
      return ☃;
    }
    int ☃ = 0;
    int ☃ = -1;
    
    Matcher ☃ = b.matcher(☃);
    while (☃.find())
    {
      String ☃ = null;
      switch (☃++)
      {
      case 0: 
        ☃ = "x";
        break;
      case 1: 
        ☃ = "y";
        break;
      case 2: 
        ☃ = "z";
        break;
      case 3: 
        ☃ = "r";
      }
      if ((☃ != null) && (☃.group(1).length() > 0)) {
        ☃.put(☃, ☃.group(1));
      }
      ☃ = ☃.end();
    }
    if (☃ < ☃.length())
    {
      Matcher ☃ = c.matcher(☃ == -1 ? ☃ : ☃.substring(☃));
      while (☃.find()) {
        ☃.put(☃.group(1), ☃.group(2));
      }
    }
    return ☃;
  }
}
