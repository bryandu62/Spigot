package org.bukkit.command.defaults;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

@Deprecated
public class SpreadPlayersCommand
  extends VanillaCommand
{
  private static final Random random = new Random();
  
  public SpreadPlayersCommand()
  {
    super("spreadplayers");
    this.description = "Spreads players around a point";
    this.usageMessage = "/spreadplayers <x> <z> <spreadDistance> <maxRange> <respectTeams true|false> <player ...>";
    setPermission("bukkit.command.spreadplayers");
  }
  
  public boolean execute(CommandSender sender, String commandLabel, String[] args)
  {
    if (!testPermission(sender)) {
      return true;
    }
    if (args.length < 6)
    {
      sender.sendMessage(ChatColor.RED + "Usage: " + this.usageMessage);
      return false;
    }
    double x = getDouble(sender, args[0], -3.0E7D, 3.0E7D);
    double z = getDouble(sender, args[1], -3.0E7D, 3.0E7D);
    double distance = getDouble(sender, args[2]);
    double range = getDouble(sender, args[3]);
    if (distance < 0.0D)
    {
      sender.sendMessage(ChatColor.RED + "Distance is too small.");
      return false;
    }
    if (range < distance + 1.0D)
    {
      sender.sendMessage(ChatColor.RED + "Max range is too small.");
      return false;
    }
    String respectTeams = args[4];
    boolean teams = false;
    if (respectTeams.equalsIgnoreCase("true"))
    {
      teams = true;
    }
    else if (!respectTeams.equalsIgnoreCase("false"))
    {
      sender.sendMessage(String.format(ChatColor.RED + "'%s' is not true or false", new Object[] { args[4] }));
      return false;
    }
    List<Player> players = Lists.newArrayList();
    World world = null;
    for (int i = 5; i < args.length; i++)
    {
      Player player = Bukkit.getPlayerExact(args[i]);
      if (player != null)
      {
        if (world == null) {
          world = player.getWorld();
        }
        players.add(player);
      }
    }
    if (world == null) {
      return true;
    }
    double xRangeMin = x - range;
    double zRangeMin = z - range;
    double xRangeMax = x + range;
    double zRangeMax = z + range;
    
    int spreadSize = teams ? getTeams(players) : players.size();
    
    Location[] locations = getSpreadLocations(world, spreadSize, xRangeMin, zRangeMin, xRangeMax, zRangeMax);
    int rangeSpread = range(world, distance, xRangeMin, zRangeMin, xRangeMax, zRangeMax, locations);
    if (rangeSpread == -1)
    {
      sender.sendMessage(String.format("Could not spread %d %s around %s,%s (too many players for space - try using spread of at most %s)", new Object[] { Integer.valueOf(spreadSize), teams ? "teams" : "players", Double.valueOf(x), Double.valueOf(z) }));
      return false;
    }
    double distanceSpread = spread(world, players, locations, teams);
    
    sender.sendMessage(String.format("Succesfully spread %d %s around %s,%s", new Object[] { Integer.valueOf(locations.length), teams ? "teams" : "players", Double.valueOf(x), Double.valueOf(z) }));
    if (locations.length > 1) {
      sender.sendMessage(String.format("(Average distance between %s is %s blocks apart after %s iterations)", new Object[] { teams ? "teams" : "players", String.format("%.2f", new Object[] { Double.valueOf(distanceSpread) }), Integer.valueOf(rangeSpread) }));
    }
    return true;
  }
  
  private int range(World world, double distance, double xRangeMin, double zRangeMin, double xRangeMax, double zRangeMax, Location[] locations)
  {
    boolean flag = true;
    for (int i = 0; (i < 10000) && (flag); i++)
    {
      flag = false;
      double max = 3.4028234663852886E38D;
      for (int k = 0; k < locations.length; k++)
      {
        Location loc2 = locations[k];
        
        int j = 0;
        Location loc1 = new Location(world, 0.0D, 0.0D, 0.0D);
        for (int l = 0; l < locations.length; l++) {
          if (k != l)
          {
            Location loc3 = locations[l];
            double dis = loc2.distanceSquared(loc3);
            
            max = Math.min(dis, max);
            if (dis < distance)
            {
              j++;
              loc1.add(loc3.getX() - loc2.getX(), 0.0D, 0.0D);
              loc1.add(loc3.getZ() - loc2.getZ(), 0.0D, 0.0D);
            }
          }
        }
        if (j > 0)
        {
          loc2.setX(loc2.getX() / j);
          loc2.setZ(loc2.getZ() / j);
          double d7 = Math.sqrt(loc1.getX() * loc1.getX() + loc1.getZ() * loc1.getZ());
          if (d7 > 0.0D)
          {
            loc1.setX(loc1.getX() / d7);
            loc2.add(-loc1.getX(), 0.0D, -loc1.getZ());
          }
          else
          {
            double x = xRangeMin >= xRangeMax ? xRangeMin : random.nextDouble() * (xRangeMax - xRangeMin) + xRangeMin;
            double z = zRangeMin >= zRangeMax ? zRangeMin : random.nextDouble() * (zRangeMax - zRangeMin) + zRangeMin;
            loc2.setX(x);
            loc2.setZ(z);
          }
          flag = true;
        }
        boolean swap = false;
        if (loc2.getX() < xRangeMin)
        {
          loc2.setX(xRangeMin);
          swap = true;
        }
        else if (loc2.getX() > xRangeMax)
        {
          loc2.setX(xRangeMax);
          swap = true;
        }
        if (loc2.getZ() < zRangeMin)
        {
          loc2.setZ(zRangeMin);
          swap = true;
        }
        else if (loc2.getZ() > zRangeMax)
        {
          loc2.setZ(zRangeMax);
          swap = true;
        }
        if (swap) {
          flag = true;
        }
      }
      if (!flag)
      {
        Location[] locs = locations;
        int i1 = locations.length;
        for (int j = 0; j < i1; j++)
        {
          Location loc1 = locs[j];
          if (world.getHighestBlockYAt(loc1) == 0)
          {
            double x = xRangeMin >= xRangeMax ? xRangeMin : random.nextDouble() * (xRangeMax - xRangeMin) + xRangeMin;
            double z = zRangeMin >= zRangeMax ? zRangeMin : random.nextDouble() * (zRangeMax - zRangeMin) + zRangeMin;
            locations[i] = new Location(world, x, 0.0D, z);
            loc1.setX(x);
            loc1.setZ(z);
            flag = true;
          }
        }
      }
    }
    if (i >= 10000) {
      return -1;
    }
    return i;
  }
  
  private double spread(World world, List<Player> list, Location[] locations, boolean teams)
  {
    double distance = 0.0D;
    int i = 0;
    Map<Team, Location> hashmap = Maps.newHashMap();
    for (int j = 0; j < list.size(); j++)
    {
      Player player = (Player)list.get(j);
      Location location;
      Location location;
      if (teams)
      {
        Team team = player.getScoreboard().getPlayerTeam(player);
        if (!hashmap.containsKey(team)) {
          hashmap.put(team, locations[(i++)]);
        }
        location = (Location)hashmap.get(team);
      }
      else
      {
        location = locations[(i++)];
      }
      player.teleport(new Location(world, Math.floor(location.getX()) + 0.5D, world.getHighestBlockYAt((int)location.getX(), (int)location.getZ()), Math.floor(location.getZ()) + 0.5D));
      double value = Double.MAX_VALUE;
      for (int k = 0; k < locations.length; k++) {
        if (location != locations[k])
        {
          double d = location.distanceSquared(locations[k]);
          value = Math.min(d, value);
        }
      }
      distance += value;
    }
    distance /= list.size();
    return distance;
  }
  
  private int getTeams(List<Player> players)
  {
    Set<Team> teams = Sets.newHashSet();
    for (Player player : players) {
      teams.add(player.getScoreboard().getPlayerTeam(player));
    }
    return teams.size();
  }
  
  private Location[] getSpreadLocations(World world, int size, double xRangeMin, double zRangeMin, double xRangeMax, double zRangeMax)
  {
    Location[] locations = new Location[size];
    for (int i = 0; i < size; i++)
    {
      double x = xRangeMin >= xRangeMax ? xRangeMin : random.nextDouble() * (xRangeMax - xRangeMin) + xRangeMin;
      double z = zRangeMin >= zRangeMax ? zRangeMin : random.nextDouble() * (zRangeMax - zRangeMin) + zRangeMin;
      locations[i] = new Location(world, x, 0.0D, z);
    }
    return locations;
  }
}
