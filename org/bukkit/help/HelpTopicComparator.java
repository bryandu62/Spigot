package org.bukkit.help;

import java.util.Comparator;

public class HelpTopicComparator
  implements Comparator<HelpTopic>
{
  private static final TopicNameComparator tnc = new TopicNameComparator(null);
  
  public static TopicNameComparator topicNameComparatorInstance()
  {
    return tnc;
  }
  
  private static final HelpTopicComparator htc = new HelpTopicComparator();
  
  public static HelpTopicComparator helpTopicComparatorInstance()
  {
    return htc;
  }
  
  public int compare(HelpTopic lhs, HelpTopic rhs)
  {
    return tnc.compare(lhs.getName(), rhs.getName());
  }
  
  public static class TopicNameComparator
    implements Comparator<String>
  {
    public int compare(String lhs, String rhs)
    {
      boolean lhsStartSlash = lhs.startsWith("/");
      boolean rhsStartSlash = rhs.startsWith("/");
      if ((lhsStartSlash) && (!rhsStartSlash)) {
        return 1;
      }
      if ((!lhsStartSlash) && (rhsStartSlash)) {
        return -1;
      }
      return lhs.compareToIgnoreCase(rhs);
    }
  }
}
