package org.apache.logging.log4j.core.pattern;

import java.util.ArrayList;
import java.util.List;

public abstract class NameAbbreviator
{
  private static final NameAbbreviator DEFAULT = new NOPAbbreviator();
  
  public static NameAbbreviator getAbbreviator(String pattern)
  {
    if (pattern.length() > 0)
    {
      String trimmed = pattern.trim();
      if (trimmed.isEmpty()) {
        return DEFAULT;
      }
      int i = 0;
      while ((i < trimmed.length()) && (trimmed.charAt(i) >= '0') && (trimmed.charAt(i) <= '9')) {
        i++;
      }
      if (i == trimmed.length()) {
        return new MaxElementAbbreviator(Integer.parseInt(trimmed));
      }
      ArrayList<PatternAbbreviatorFragment> fragments = new ArrayList(5);
      
      int pos = 0;
      while ((pos < trimmed.length()) && (pos >= 0))
      {
        int ellipsisPos = pos;
        int charCount;
        if (trimmed.charAt(pos) == '*')
        {
          int charCount = Integer.MAX_VALUE;
          ellipsisPos++;
        }
        else if ((trimmed.charAt(pos) >= '0') && (trimmed.charAt(pos) <= '9'))
        {
          int charCount = trimmed.charAt(pos) - '0';
          ellipsisPos++;
        }
        else
        {
          charCount = 0;
        }
        char ellipsis = '\000';
        if (ellipsisPos < trimmed.length())
        {
          ellipsis = trimmed.charAt(ellipsisPos);
          if (ellipsis == '.') {
            ellipsis = '\000';
          }
        }
        fragments.add(new PatternAbbreviatorFragment(charCount, ellipsis));
        pos = trimmed.indexOf('.', pos);
        if (pos == -1) {
          break;
        }
        pos++;
      }
      return new PatternAbbreviator(fragments);
    }
    return DEFAULT;
  }
  
  public static NameAbbreviator getDefaultAbbreviator()
  {
    return DEFAULT;
  }
  
  public abstract String abbreviate(String paramString);
  
  private static class NOPAbbreviator
    extends NameAbbreviator
  {
    public String abbreviate(String buf)
    {
      return buf;
    }
  }
  
  private static class MaxElementAbbreviator
    extends NameAbbreviator
  {
    private final int count;
    
    public MaxElementAbbreviator(int count)
    {
      this.count = (count < 1 ? 1 : count);
    }
    
    public String abbreviate(String buf)
    {
      int end = buf.length() - 1;
      for (int i = this.count; i > 0; i--)
      {
        end = buf.lastIndexOf('.', end - 1);
        if (end == -1) {
          return buf;
        }
      }
      return buf.substring(end + 1);
    }
  }
  
  private static class PatternAbbreviatorFragment
  {
    private final int charCount;
    private final char ellipsis;
    
    public PatternAbbreviatorFragment(int charCount, char ellipsis)
    {
      this.charCount = charCount;
      this.ellipsis = ellipsis;
    }
    
    public int abbreviate(StringBuilder buf, int startPos)
    {
      int nextDot = buf.toString().indexOf('.', startPos);
      if (nextDot != -1)
      {
        if (nextDot - startPos > this.charCount)
        {
          buf.delete(startPos + this.charCount, nextDot);
          nextDot = startPos + this.charCount;
          if (this.ellipsis != 0)
          {
            buf.insert(nextDot, this.ellipsis);
            nextDot++;
          }
        }
        nextDot++;
      }
      return nextDot;
    }
  }
  
  private static class PatternAbbreviator
    extends NameAbbreviator
  {
    private final NameAbbreviator.PatternAbbreviatorFragment[] fragments;
    
    public PatternAbbreviator(List<NameAbbreviator.PatternAbbreviatorFragment> fragments)
    {
      if (fragments.size() == 0) {
        throw new IllegalArgumentException("fragments must have at least one element");
      }
      this.fragments = new NameAbbreviator.PatternAbbreviatorFragment[fragments.size()];
      fragments.toArray(this.fragments);
    }
    
    public String abbreviate(String buf)
    {
      int pos = 0;
      StringBuilder sb = new StringBuilder(buf);
      for (int i = 0; (i < this.fragments.length - 1) && (pos < buf.length()); i++) {
        pos = this.fragments[i].abbreviate(sb, pos);
      }
      NameAbbreviator.PatternAbbreviatorFragment terminalFragment = this.fragments[(this.fragments.length - 1)];
      while ((pos < buf.length()) && (pos >= 0)) {
        pos = terminalFragment.abbreviate(sb, pos);
      }
      return sb.toString();
    }
  }
}
