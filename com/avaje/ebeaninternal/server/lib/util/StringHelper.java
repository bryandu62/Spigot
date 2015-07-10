package com.avaje.ebeaninternal.server.lib.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StringHelper
{
  private static final char SINGLE_QUOTE = '\'';
  private static final char DOUBLE_QUOTE = '"';
  
  public static HashMap<String, String> parseNameQuotedValue(String tag)
    throws RuntimeException
  {
    if ((tag == null) || (tag.length() < 1)) {
      return null;
    }
    if (tag.charAt(tag.length() - 1) == '=') {
      throw new RuntimeException("missing quoted value at the end of " + tag);
    }
    HashMap<String, String> map = new HashMap();
    
    return parseNameQuotedValue(map, tag, 0);
  }
  
  private static HashMap<String, String> parseNameQuotedValue(HashMap<String, String> map, String tag, int pos)
    throws RuntimeException
  {
    int equalsPos = tag.indexOf("=", pos);
    if (equalsPos > -1)
    {
      char firstQuote = tag.charAt(equalsPos + 1);
      if ((firstQuote != '\'') && (firstQuote != '"')) {
        throw new RuntimeException("missing begin quote at " + equalsPos + "[" + tag.charAt(equalsPos + 1) + "] in [" + tag + "]");
      }
      int endQuotePos = tag.indexOf(firstQuote, equalsPos + 2);
      if (endQuotePos == -1) {
        throw new RuntimeException("missing end quote [" + firstQuote + "] after " + pos + " in [" + tag + "]");
      }
      String name = tag.substring(pos, equalsPos);
      String value = tag.substring(equalsPos + 2, endQuotePos);
      
      name = trimFront(name, " ");
      if ((name.indexOf('\'') > -1) || (name.indexOf('"') > -1)) {
        throw new RuntimeException("attribute name contains a quote [" + name + "]");
      }
      map.put(name, value);
      
      return parseNameQuotedValue(map, tag, endQuotePos + 1);
    }
    return map;
  }
  
  public static int countOccurances(String content, String occurs)
  {
    return countOccurances(content, occurs, 0, 0);
  }
  
  private static int countOccurances(String content, String occurs, int pos, int countSoFar)
  {
    int equalsPos = content.indexOf(occurs, pos);
    if (equalsPos > -1)
    {
      countSoFar += 1;
      pos = equalsPos + occurs.length();
      
      return countOccurances(content, occurs, pos, countSoFar);
    }
    return countSoFar;
  }
  
  public static Map<String, String> delimitedToMap(String allNameValuePairs, String listDelimiter, String nameValueSeparator)
  {
    HashMap<String, String> params = new HashMap();
    if ((allNameValuePairs == null) || (allNameValuePairs.length() == 0)) {
      return params;
    }
    allNameValuePairs = trimFront(allNameValuePairs, listDelimiter);
    return getKeyValue(params, 0, allNameValuePairs, listDelimiter, nameValueSeparator);
  }
  
  public static String trimFront(String source, String trim)
  {
    if (source == null) {
      return null;
    }
    if (source.indexOf(trim) == 0) {
      return trimFront(source.substring(trim.length()), trim);
    }
    return source;
  }
  
  public static boolean isNull(String value)
  {
    if ((value == null) || (value.trim().length() == 0)) {
      return true;
    }
    return false;
  }
  
  private static HashMap<String, String> getKeyValue(HashMap<String, String> map, int pos, String allNameValuePairs, String listDelimiter, String nameValueSeparator)
  {
    if (pos >= allNameValuePairs.length()) {
      return map;
    }
    int equalsPos = allNameValuePairs.indexOf(nameValueSeparator, pos);
    int delimPos = allNameValuePairs.indexOf(listDelimiter, pos);
    if (delimPos == -1) {
      delimPos = allNameValuePairs.length();
    }
    if (equalsPos == -1) {
      return map;
    }
    if (delimPos == equalsPos + 1) {
      return getKeyValue(map, delimPos + 1, allNameValuePairs, listDelimiter, nameValueSeparator);
    }
    if (equalsPos > delimPos)
    {
      String key = allNameValuePairs.substring(pos, delimPos);
      key = key.trim();
      if (key.length() > 0) {
        map.put(key, null);
      }
      return getKeyValue(map, delimPos + 1, allNameValuePairs, listDelimiter, nameValueSeparator);
    }
    String key = allNameValuePairs.substring(pos, equalsPos);
    if (delimPos > -1)
    {
      String value = allNameValuePairs.substring(equalsPos + 1, delimPos);
      
      key = key.trim();
      
      map.put(key, value);
      pos = delimPos + 1;
      
      return getKeyValue(map, pos, allNameValuePairs, listDelimiter, nameValueSeparator);
    }
    return map;
  }
  
  public static String[] delimitedToArray(String str, String delimiter, boolean keepEmpties)
  {
    ArrayList<String> list = new ArrayList();
    int startPos = 0;
    delimiter(str, delimiter, keepEmpties, startPos, list);
    String[] result = new String[list.size()];
    return (String[])list.toArray(result);
  }
  
  private static void delimiter(String str, String delimiter, boolean keepEmpties, int startPos, ArrayList<String> list)
  {
    int endPos = str.indexOf(delimiter, startPos);
    if (endPos == -1)
    {
      if (startPos <= str.length())
      {
        String lastValue = str.substring(startPos, str.length());
        if ((keepEmpties) || (lastValue.length() != 0)) {
          list.add(lastValue);
        }
      }
      return;
    }
    String value = str.substring(startPos, endPos);
    if ((keepEmpties) || (value.length() != 0)) {
      list.add(value);
    }
    delimiter(str, delimiter, keepEmpties, endPos + 1, list);
  }
  
  public static String getBoundedString(String str, String leftBound, String rightBound)
    throws RuntimeException
  {
    if (str == null) {
      throw new RuntimeException("string to parse is null?");
    }
    int startPos = str.indexOf(leftBound);
    if (startPos > -1)
    {
      startPos += leftBound.length();
      int endPos = str.indexOf(rightBound, startPos);
      if (endPos == -1) {
        throw new RuntimeException("Can't find rightBound: " + rightBound);
      }
      return str.substring(startPos, endPos);
    }
    return null;
  }
  
  public static String setBoundedString(String str, String leftBound, String rightBound, String replaceString)
  {
    int startPos = str.indexOf(leftBound);
    if (startPos > -1)
    {
      int endPos = str.indexOf(rightBound, startPos + leftBound.length());
      if (endPos > -1)
      {
        String toReplace = str.substring(startPos, endPos + 1);
        return replaceString(str, toReplace, replaceString);
      }
      return str;
    }
    return str;
  }
  
  public static String replaceString(String source, String match, String replace)
  {
    if (source == null) {
      return null;
    }
    if (replace == null) {
      return source;
    }
    if (match == null) {
      throw new NullPointerException("match is null?");
    }
    if (match.equals(replace)) {
      return source;
    }
    return replaceString(source, match, replace, 30, 0, source.length());
  }
  
  public static String replaceString(String source, String match, String replace, int additionalSize, int startPos, int endPos)
  {
    if (source == null) {
      return source;
    }
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
  
  public static String replaceStringMulti(String source, String[] match, String replace)
  {
    return replaceStringMulti(source, match, replace, 30, 0, source.length());
  }
  
  public static String replaceStringMulti(String source, String[] match, String replace, int additionalSize, int startPos, int endPos)
  {
    int shortestMatch = match[0].length();
    
    char[] match0 = new char[match.length];
    for (int i = 0; i < match0.length; i++)
    {
      match0[i] = match[i].charAt(0);
      if (match[i].length() < shortestMatch) {
        shortestMatch = match[i].length();
      }
    }
    StringBuilder sb = new StringBuilder(source.length() + additionalSize);
    
    int len = source.length();
    int lastMatch = endPos - shortestMatch;
    if (startPos > 0) {
      sb.append(source.substring(0, startPos));
    }
    int matchCount = 0;
    for (int i = startPos; i < len; i++)
    {
      char sourceChar = source.charAt(i);
      if (i > lastMatch)
      {
        sb.append(sourceChar);
      }
      else
      {
        matchCount = 0;
        for (int k = 0; k < match0.length; k++) {
          if ((matchCount == 0) && (sourceChar == match0[k]) && 
            (match[k].length() + i <= len))
          {
            matchCount++;
            for (int j = 1; j < match[k].length(); j++) {
              if (source.charAt(i + j) != match[k].charAt(j))
              {
                matchCount--;
                break;
              }
            }
            if (matchCount > 0)
            {
              i = i + j - 1;
              sb.append(replace);
              break;
            }
          }
        }
        if (matchCount == 0) {
          sb.append(sourceChar);
        }
      }
    }
    return sb.toString();
  }
  
  public static String removeChar(String s, char chr)
  {
    StringBuilder sb = new StringBuilder(s.length());
    for (int i = 0; i < s.length(); i++)
    {
      char c = s.charAt(i);
      if (c != chr) {
        sb.append(c);
      }
    }
    return sb.toString();
  }
  
  public static String removeChars(String s, char[] chr)
  {
    StringBuilder sb = new StringBuilder(s.length());
    for (int i = 0; i < s.length(); i++)
    {
      char c = s.charAt(i);
      if (!charMatch(c, chr)) {
        sb.append(c);
      }
    }
    return sb.toString();
  }
  
  private static boolean charMatch(int iChr, char[] chr)
  {
    for (int i = 0; i < chr.length; i++) {
      if (iChr == chr[i]) {
        return true;
      }
    }
    return false;
  }
}
