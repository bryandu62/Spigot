package com.avaje.ebeaninternal.server.text.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class CsvUtilReader
{
  private BufferedReader br;
  private boolean hasNext = true;
  private char separator;
  private char quotechar;
  private int skipLines;
  private boolean linesSkiped;
  public static final char DEFAULT_SEPARATOR = ',';
  public static final char DEFAULT_QUOTE_CHARACTER = '"';
  public static final int DEFAULT_SKIP_LINES = 0;
  
  public CsvUtilReader(Reader reader)
  {
    this(reader, ',');
  }
  
  public CsvUtilReader(Reader reader, char separator)
  {
    this(reader, separator, '"');
  }
  
  public CsvUtilReader(Reader reader, char separator, char quotechar)
  {
    this(reader, separator, quotechar, 0);
  }
  
  public CsvUtilReader(Reader reader, char separator, char quotechar, int line)
  {
    this.br = new BufferedReader(reader);
    this.separator = separator;
    this.quotechar = quotechar;
    this.skipLines = line;
  }
  
  public List<String[]> readAll()
    throws IOException
  {
    List<String[]> allElements = new ArrayList();
    while (this.hasNext)
    {
      String[] nextLineAsTokens = readNext();
      if (nextLineAsTokens != null) {
        allElements.add(nextLineAsTokens);
      }
    }
    return allElements;
  }
  
  public String[] readNext()
    throws IOException
  {
    String nextLine = getNextLine();
    return this.hasNext ? parseLine(nextLine) : null;
  }
  
  private String getNextLine()
    throws IOException
  {
    if (!this.linesSkiped)
    {
      for (int i = 0; i < this.skipLines; i++) {
        this.br.readLine();
      }
      this.linesSkiped = true;
    }
    String nextLine = this.br.readLine();
    if (nextLine == null) {
      this.hasNext = false;
    }
    return this.hasNext ? nextLine : null;
  }
  
  private String[] parseLine(String nextLine)
    throws IOException
  {
    if (nextLine == null) {
      return null;
    }
    List<String> tokensOnThisLine = new ArrayList();
    
    StringBuilder sb = new StringBuilder();
    boolean inQuotes = false;
    do
    {
      if (inQuotes)
      {
        sb.append("\n");
        nextLine = getNextLine();
        if (nextLine == null) {
          break;
        }
      }
      for (int i = 0; i < nextLine.length(); i++)
      {
        char c = nextLine.charAt(i);
        if (c == this.quotechar)
        {
          if ((inQuotes) && (nextLine.length() > i + 1) && (nextLine.charAt(i + 1) == this.quotechar))
          {
            sb.append(nextLine.charAt(i + 1));
            i++;
          }
          else
          {
            inQuotes = !inQuotes;
            if ((i > 2) && (nextLine.charAt(i - 1) != this.separator) && (nextLine.length() > i + 1) && (nextLine.charAt(i + 1) != this.separator)) {
              sb.append(c);
            }
          }
        }
        else if ((c == this.separator) && (!inQuotes))
        {
          tokensOnThisLine.add(sb.toString().trim());
          sb = new StringBuilder();
        }
        else
        {
          sb.append(c);
        }
      }
    } while (inQuotes);
    tokensOnThisLine.add(sb.toString().trim());
    return (String[])tokensOnThisLine.toArray(new String[tokensOnThisLine.size()]);
  }
  
  public void close()
    throws IOException
  {
    this.br.close();
  }
}
