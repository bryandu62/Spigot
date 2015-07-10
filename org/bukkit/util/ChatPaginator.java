package org.bukkit.util;

import java.util.LinkedList;
import java.util.List;
import org.bukkit.ChatColor;

public class ChatPaginator
{
  public static final int GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH = 55;
  public static final int AVERAGE_CHAT_PAGE_WIDTH = 65;
  public static final int UNBOUNDED_PAGE_WIDTH = Integer.MAX_VALUE;
  public static final int OPEN_CHAT_PAGE_HEIGHT = 20;
  public static final int CLOSED_CHAT_PAGE_HEIGHT = 10;
  public static final int UNBOUNDED_PAGE_HEIGHT = Integer.MAX_VALUE;
  
  public static ChatPage paginate(String unpaginatedString, int pageNumber)
  {
    return paginate(unpaginatedString, pageNumber, 55, 10);
  }
  
  public static ChatPage paginate(String unpaginatedString, int pageNumber, int lineLength, int pageHeight)
  {
    String[] lines = wordWrap(unpaginatedString, lineLength);
    
    int totalPages = lines.length / pageHeight + (lines.length % pageHeight == 0 ? 0 : 1);
    int actualPageNumber = pageNumber <= totalPages ? pageNumber : totalPages;
    
    int from = (actualPageNumber - 1) * pageHeight;
    int to = from + pageHeight <= lines.length ? from + pageHeight : lines.length;
    String[] selectedLines = (String[])Java15Compat.Arrays_copyOfRange(lines, from, to);
    
    return new ChatPage(selectedLines, actualPageNumber, totalPages);
  }
  
  public static String[] wordWrap(String rawString, int lineLength)
  {
    if (rawString == null) {
      return new String[] { "" };
    }
    if ((rawString.length() <= lineLength) && (!rawString.contains("\n"))) {
      return new String[] { rawString };
    }
    char[] rawChars = (rawString + ' ').toCharArray();
    StringBuilder word = new StringBuilder();
    StringBuilder line = new StringBuilder();
    List<String> lines = new LinkedList();
    int lineColorChars = 0;
    for (int i = 0; i < rawChars.length; i++)
    {
      char c = rawChars[i];
      if (c == '§')
      {
        word.append(ChatColor.getByChar(rawChars[(i + 1)]));
        lineColorChars += 2;
        i++;
      }
      else if ((c == ' ') || (c == '\n'))
      {
        String[] arrayOfString;
        int i;
        int j;
        if ((line.length() == 0) && (word.length() > lineLength))
        {
          i = (arrayOfString = word.toString().split("(?<=\\G.{" + lineLength + "})")).length;
          for (j = 0; j < i; j++)
          {
            String partialWord = arrayOfString[j];
            lines.add(partialWord);
          }
        }
        else if (line.length() + word.length() - lineColorChars == lineLength)
        {
          line.append(word);
          lines.add(line.toString());
          line = new StringBuilder();
          lineColorChars = 0;
        }
        else if (line.length() + 1 + word.length() - lineColorChars > lineLength)
        {
          i = (arrayOfString = word.toString().split("(?<=\\G.{" + lineLength + "})")).length;
          for (j = 0; j < i; j++)
          {
            String partialWord = arrayOfString[j];
            lines.add(line.toString());
            line = new StringBuilder(partialWord);
          }
          lineColorChars = 0;
        }
        else
        {
          if (line.length() > 0) {
            line.append(' ');
          }
          line.append(word);
        }
        word = new StringBuilder();
        if (c == '\n')
        {
          lines.add(line.toString());
          line = new StringBuilder();
        }
      }
      else
      {
        word.append(c);
      }
    }
    if (line.length() > 0) {
      lines.add(line.toString());
    }
    if ((((String)lines.get(0)).length() == 0) || (((String)lines.get(0)).charAt(0) != '§')) {
      lines.set(0, ChatColor.WHITE + (String)lines.get(0));
    }
    for (int i = 1; i < lines.size(); i++)
    {
      String pLine = (String)lines.get(i - 1);
      String subLine = (String)lines.get(i);
      
      char color = pLine.charAt(pLine.lastIndexOf('§') + 1);
      if ((subLine.length() == 0) || (subLine.charAt(0) != '§')) {
        lines.set(i, ChatColor.getByChar(color) + subLine);
      }
    }
    return (String[])lines.toArray(new String[lines.size()]);
  }
  
  public static class ChatPage
  {
    private String[] lines;
    private int pageNumber;
    private int totalPages;
    
    public ChatPage(String[] lines, int pageNumber, int totalPages)
    {
      this.lines = lines;
      this.pageNumber = pageNumber;
      this.totalPages = totalPages;
    }
    
    public int getPageNumber()
    {
      return this.pageNumber;
    }
    
    public int getTotalPages()
    {
      return this.totalPages;
    }
    
    public String[] getLines()
    {
      return this.lines;
    }
  }
}
