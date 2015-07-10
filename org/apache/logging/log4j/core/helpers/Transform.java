package org.apache.logging.log4j.core.helpers;

public final class Transform
{
  private static final String CDATA_START = "<![CDATA[";
  private static final String CDATA_END = "]]>";
  private static final String CDATA_PSEUDO_END = "]]&gt;";
  private static final String CDATA_EMBEDED_END = "]]>]]&gt;<![CDATA[";
  private static final int CDATA_END_LEN = "]]>".length();
  
  public static String escapeHtmlTags(String input)
  {
    if ((Strings.isEmpty(input)) || ((input.indexOf('"') == -1) && (input.indexOf('&') == -1) && (input.indexOf('<') == -1) && (input.indexOf('>') == -1))) {
      return input;
    }
    StringBuilder buf = new StringBuilder(input.length() + 6);
    char ch = ' ';
    
    int len = input.length();
    for (int i = 0; i < len; i++)
    {
      ch = input.charAt(i);
      if (ch > '>') {
        buf.append(ch);
      } else if (ch == '<') {
        buf.append("&lt;");
      } else if (ch == '>') {
        buf.append("&gt;");
      } else if (ch == '&') {
        buf.append("&amp;");
      } else if (ch == '"') {
        buf.append("&quot;");
      } else {
        buf.append(ch);
      }
    }
    return buf.toString();
  }
  
  public static void appendEscapingCDATA(StringBuilder buf, String str)
  {
    if (str != null)
    {
      int end = str.indexOf("]]>");
      if (end < 0)
      {
        buf.append(str);
      }
      else
      {
        int start = 0;
        while (end > -1)
        {
          buf.append(str.substring(start, end));
          buf.append("]]>]]&gt;<![CDATA[");
          start = end + CDATA_END_LEN;
          if (start < str.length()) {
            end = str.indexOf("]]>", start);
          } else {
            return;
          }
        }
        buf.append(str.substring(start));
      }
    }
  }
  
  public static String escapeJsonControlCharacters(String input)
  {
    if ((Strings.isEmpty(input)) || ((input.indexOf('"') == -1) && (input.indexOf('\\') == -1) && (input.indexOf('/') == -1) && (input.indexOf('\b') == -1) && (input.indexOf('\f') == -1) && (input.indexOf('\n') == -1) && (input.indexOf('\r') == -1) && (input.indexOf('\t') == -1))) {
      return input;
    }
    StringBuilder buf = new StringBuilder(input.length() + 6);
    
    int len = input.length();
    for (int i = 0; i < len; i++)
    {
      char ch = input.charAt(i);
      String escBs = "\\\\";
      switch (ch)
      {
      case '"': 
        buf.append("\\\\");
        buf.append(ch);
        break;
      case '\\': 
        buf.append("\\\\");
        buf.append(ch);
        break;
      case '/': 
        buf.append("\\\\");
        buf.append(ch);
        break;
      case '\b': 
        buf.append("\\\\");
        buf.append('b');
        break;
      case '\f': 
        buf.append("\\\\");
        buf.append('f');
        break;
      case '\n': 
        buf.append("\\\\");
        buf.append('n');
        break;
      case '\r': 
        buf.append("\\\\");
        buf.append('r');
        break;
      case '\t': 
        buf.append("\\\\");
        buf.append('t');
        break;
      default: 
        buf.append(ch);
      }
    }
    return buf.toString();
  }
}
