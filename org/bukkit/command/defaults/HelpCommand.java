package org.bukkit.command.defaults;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.help.HelpMap;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.HelpTopicComparator;
import org.bukkit.help.IndexHelpTopic;
import org.bukkit.util.ChatPaginator;
import org.bukkit.util.ChatPaginator.ChatPage;

public class HelpCommand
  extends VanillaCommand
{
  public HelpCommand()
  {
    super("help");
    this.description = "Shows the help menu";
    this.usageMessage = "/help <pageNumber>\n/help <topic>\n/help <topic> <pageNumber>";
    setAliases(Arrays.asList(new String[] { "?" }));
    setPermission("bukkit.command.help");
  }
  
  public boolean execute(CommandSender sender, String currentAlias, String[] args)
  {
    if (!testPermission(sender)) {
      return true;
    }
    int pageNumber;
    String command;
    int pageNumber;
    if (args.length == 0)
    {
      String command = "";
      pageNumber = 1;
    }
    else if (NumberUtils.isDigits(args[(args.length - 1)]))
    {
      String command = StringUtils.join(ArrayUtils.subarray(args, 0, args.length - 1), " ");
      int pageNumber;
      try
      {
        pageNumber = NumberUtils.createInteger(args[(args.length - 1)]).intValue();
      }
      catch (NumberFormatException localNumberFormatException)
      {
        int pageNumber;
        pageNumber = 1;
      }
      if (pageNumber <= 0) {
        pageNumber = 1;
      }
    }
    else
    {
      command = StringUtils.join(args, " ");
      pageNumber = 1;
    }
    int pageWidth;
    int pageHeight;
    int pageWidth;
    if ((sender instanceof ConsoleCommandSender))
    {
      int pageHeight = Integer.MAX_VALUE;
      pageWidth = Integer.MAX_VALUE;
    }
    else
    {
      pageHeight = 9;
      pageWidth = 55;
    }
    HelpMap helpMap = Bukkit.getServer().getHelpMap();
    HelpTopic topic = helpMap.getHelpTopic(command);
    if (topic == null) {
      topic = helpMap.getHelpTopic("/" + command);
    }
    if (topic == null) {
      topic = findPossibleMatches(command);
    }
    if ((topic == null) || (!topic.canSee(sender)))
    {
      sender.sendMessage(ChatColor.RED + "No help for " + command);
      return true;
    }
    ChatPaginator.ChatPage page = ChatPaginator.paginate(topic.getFullText(sender), pageNumber, pageWidth, pageHeight);
    
    StringBuilder header = new StringBuilder();
    header.append(ChatColor.YELLOW);
    header.append("--------- ");
    header.append(ChatColor.WHITE);
    header.append("Help: ");
    header.append(topic.getName());
    header.append(" ");
    if (page.getTotalPages() > 1)
    {
      header.append("(");
      header.append(page.getPageNumber());
      header.append("/");
      header.append(page.getTotalPages());
      header.append(") ");
    }
    header.append(ChatColor.YELLOW);
    for (int i = header.length(); i < 55; i++) {
      header.append("-");
    }
    sender.sendMessage(header.toString());
    
    sender.sendMessage(page.getLines());
    
    return true;
  }
  
  public List<String> tabComplete(CommandSender sender, String alias, String[] args)
  {
    Validate.notNull(sender, "Sender cannot be null");
    Validate.notNull(args, "Arguments cannot be null");
    Validate.notNull(alias, "Alias cannot be null");
    if (args.length == 1)
    {
      List<String> matchedTopics = new ArrayList();
      String searchString = args[0];
      for (HelpTopic topic : Bukkit.getServer().getHelpMap().getHelpTopics())
      {
        String trimmedTopic = topic.getName().startsWith("/") ? topic.getName().substring(1) : topic.getName();
        if (trimmedTopic.startsWith(searchString)) {
          matchedTopics.add(trimmedTopic);
        }
      }
      return matchedTopics;
    }
    return ImmutableList.of();
  }
  
  protected HelpTopic findPossibleMatches(String searchString)
  {
    int maxDistance = searchString.length() / 5 + 3;
    Set<HelpTopic> possibleMatches = new TreeSet(HelpTopicComparator.helpTopicComparatorInstance());
    if (searchString.startsWith("/")) {
      searchString = searchString.substring(1);
    }
    for (HelpTopic topic : Bukkit.getServer().getHelpMap().getHelpTopics())
    {
      String trimmedTopic = topic.getName().startsWith("/") ? topic.getName().substring(1) : topic.getName();
      if (trimmedTopic.length() >= searchString.length()) {
        if (Character.toLowerCase(trimmedTopic.charAt(0)) == Character.toLowerCase(searchString.charAt(0))) {
          if (damerauLevenshteinDistance(searchString, trimmedTopic.substring(0, searchString.length())) < maxDistance) {
            possibleMatches.add(topic);
          }
        }
      }
    }
    if (possibleMatches.size() > 0) {
      return new IndexHelpTopic("Search", null, null, possibleMatches, "Search for: " + searchString);
    }
    return null;
  }
  
  protected static int damerauLevenshteinDistance(String s1, String s2)
  {
    if ((s1 == null) && (s2 == null)) {
      return 0;
    }
    if ((s1 != null) && (s2 == null)) {
      return s1.length();
    }
    if ((s1 == null) && (s2 != null)) {
      return s2.length();
    }
    int s1Len = s1.length();
    int s2Len = s2.length();
    int[][] H = new int[s1Len + 2][s2Len + 2];
    
    int INF = s1Len + s2Len;
    H[0][0] = INF;
    for (int i = 0; i <= s1Len; i++)
    {
      H[(i + 1)][1] = i;
      H[(i + 1)][0] = INF;
    }
    for (int j = 0; j <= s2Len; j++)
    {
      H[1][(j + 1)] = j;
      H[0][(j + 1)] = INF;
    }
    Map<Character, Integer> sd = new HashMap();
    char[] arrayOfChar;
    int i = (arrayOfChar = (s1 + s2).toCharArray()).length;
    for (int j = 0; j < i; j++)
    {
      char Letter = arrayOfChar[j];
      if (!sd.containsKey(Character.valueOf(Letter))) {
        sd.put(Character.valueOf(Letter), Integer.valueOf(0));
      }
    }
    for (int i = 1; i <= s1Len; i++)
    {
      int DB = 0;
      for (int j = 1; j <= s2Len; j++)
      {
        int i1 = ((Integer)sd.get(Character.valueOf(s2.charAt(j - 1)))).intValue();
        int j1 = DB;
        if (s1.charAt(i - 1) == s2.charAt(j - 1))
        {
          H[(i + 1)][(j + 1)] = H[i][j];
          DB = j;
        }
        else
        {
          H[(i + 1)][(j + 1)] = (Math.min(H[i][j], Math.min(H[(i + 1)][j], H[i][(j + 1)])) + 1);
        }
        H[(i + 1)][(j + 1)] = Math.min(H[(i + 1)][(j + 1)], H[i1][j1] + (i - i1 - 1) + 1 + (j - j1 - 1));
      }
      sd.put(Character.valueOf(s1.charAt(i - 1)), Integer.valueOf(i));
    }
    return H[(s1Len + 1)][(s2Len + 1)];
  }
}
