package com.avaje.ebean.enhance.ant;

public class StringReplace
{
  public static String replace(String source, String match, String replace)
  {
    return replaceString(source, match, replace, 30, 0, source.length());
  }
  
  private static String replaceString(String source, String match, String replace, int additionalSize, int startPos, int endPos)
  {
    char match0 = match.charAt(0);
    
    int matchLength = match.length();
    if ((matchLength == 1) && (replace.length() == 1))
    {
      char replace0 = replace.charAt(0);
      return source.replace(match0, replace0);
    }
    if (matchLength >= replace.length()) {
      additionalSize = 0;
    }
    int sourceLength = source.length();
    int lastMatch = endPos - matchLength;
    
    StringBuilder sb = new StringBuilder(sourceLength + additionalSize);
    if (startPos > 0) {
      sb.append(source.substring(0, startPos));
    }
    for (int i = startPos; i < sourceLength; i++)
    {
      char sourceChar = source.charAt(i);
      if ((i > lastMatch) || (sourceChar != match0))
      {
        sb.append(sourceChar);
      }
      else
      {
        boolean isMatch = true;
        int sourceMatchPos = i;
        for (int j = 1; j < matchLength; j++)
        {
          sourceMatchPos++;
          if (source.charAt(sourceMatchPos) != match.charAt(j))
          {
            isMatch = false;
            break;
          }
        }
        if (isMatch)
        {
          i = i + matchLength - 1;
          sb.append(replace);
        }
        else
        {
          sb.append(sourceChar);
        }
      }
    }
    return sb.toString();
  }
}
