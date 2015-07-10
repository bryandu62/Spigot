package com.avaje.ebeaninternal.server.type;

import com.avaje.ebean.text.TextException;
import com.avaje.ebeaninternal.server.text.json.WriteJsonBuffer;
import java.io.IOException;

public class EscapeJson
{
  public static String escapeQuote(String value)
  {
    if (value == null) {
      return "null";
    }
    StringBuilder sb = new StringBuilder(value.length() + 2);
    sb.append("\"");
    escapeAppend(value, sb);
    sb.append("\"");
    return sb.toString();
  }
  
  public static String escape(String s)
  {
    if (s == null) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    escapeAppend(s, sb);
    return sb.toString();
  }
  
  public static void escape(String value, WriteJsonBuffer sb)
  {
    if (value == null) {
      sb.append("null");
    } else {
      escapeAppend(value, sb);
    }
  }
  
  public static void escapeQuote(String value, WriteJsonBuffer sb)
  {
    if (value == null)
    {
      sb.append("null");
    }
    else
    {
      sb.append("\"");
      escapeAppend(value, sb);
      sb.append("\"");
    }
  }
  
  public static void escapeAppend(String s, Appendable sb)
  {
    try
    {
      for (int i = 0; i < s.length(); i++)
      {
        char ch = s.charAt(i);
        switch (ch)
        {
        case '"': 
          sb.append("\\\"");
          break;
        case '\\': 
          sb.append("\\\\");
          break;
        case '\b': 
          sb.append("\\b");
          break;
        case '\f': 
          sb.append("\\f");
          break;
        case '\n': 
          sb.append("\\n");
          break;
        case '\r': 
          sb.append("\\r");
          break;
        case '\t': 
          sb.append("\\t");
          break;
        case '/': 
          sb.append("\\/");
          break;
        default: 
          if (((ch >= 0) && (ch <= '\037')) || ((ch >= '') && (ch <= '')) || ((ch >= ' ') && (ch <= '⃿')))
          {
            String hs = Integer.toHexString(ch);
            sb.append("\\u");
            for (int j = 0; j < 4 - hs.length(); j++) {
              sb.append('0');
            }
            sb.append(hs.toUpperCase());
          }
          else
          {
            sb.append(ch);
          }
          break;
        }
      }
    }
    catch (IOException e)
    {
      throw new TextException(e);
    }
  }
}
