package com.avaje.ebeaninternal.server.text.json;

import com.avaje.ebean.text.TextException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class ReadJsonSourceReader
  implements ReadJsonSource
{
  private final Reader reader;
  private char[] localBuffer;
  private int totalPos;
  private int localPos;
  private int localPosEnd;
  
  public ReadJsonSourceReader(Reader reader, int localBufferSize, int bufferSize)
  {
    this.reader = new BufferedReader(reader, bufferSize);
    this.localBuffer = new char[localBufferSize];
  }
  
  public String toString()
  {
    return String.valueOf(this.localBuffer);
  }
  
  public String getErrorHelp()
  {
    int prev = this.localPos - 30;
    if (prev < 0) {
      prev = 0;
    }
    String c = new String(this.localBuffer, prev, this.localPos - prev);
    return "pos:" + pos() + " preceding:" + c;
  }
  
  public int pos()
  {
    return this.totalPos + this.localPos;
  }
  
  public void ignoreWhiteSpace()
  {
    for (;;)
    {
      char c = nextChar("EOF ignoring whitespace");
      if (!Character.isWhitespace(c))
      {
        this.localPos -= 1;
        break;
      }
    }
  }
  
  public void back()
  {
    this.localPos -= 1;
  }
  
  public char nextChar(String eofMsg)
  {
    if ((this.localPos >= this.localPosEnd) && 
      (!loadLocalBuffer())) {
      throw new TextException(eofMsg + " at pos:" + (this.totalPos + this.localPos));
    }
    return this.localBuffer[(this.localPos++)];
  }
  
  private boolean loadLocalBuffer()
  {
    try
    {
      this.localPosEnd = this.reader.read(this.localBuffer);
      if (this.localPosEnd > 0)
      {
        this.totalPos += this.localPos;
        this.localPos = 0;
        return true;
      }
      this.localBuffer = null;
      return false;
    }
    catch (IOException e)
    {
      throw new TextException(e);
    }
  }
}
