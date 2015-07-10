package com.avaje.ebeaninternal.server.text.json;

import com.avaje.ebean.text.TextException;

public class ReadBasicJsonContext
  implements ReadJsonInterface
{
  private final ReadJsonSource src;
  private char tokenStart;
  private String tokenKey;
  
  public ReadBasicJsonContext(ReadJsonSource src)
  {
    this.src = src;
  }
  
  public char getToken()
  {
    return this.tokenStart;
  }
  
  public String getTokenKey()
  {
    return this.tokenKey;
  }
  
  public boolean isTokenKey()
  {
    return '"' == this.tokenStart;
  }
  
  public boolean isTokenObjectEnd()
  {
    return '}' == this.tokenStart;
  }
  
  public boolean readObjectBegin()
  {
    readNextToken();
    if ('{' == this.tokenStart) {
      return true;
    }
    if ('n' == this.tokenStart) {
      return false;
    }
    if (']' == this.tokenStart) {
      return false;
    }
    throw new RuntimeException("Expected object begin at " + this.src.getErrorHelp());
  }
  
  public boolean readKeyNext()
  {
    readNextToken();
    if ('"' == this.tokenStart) {
      return true;
    }
    if ('}' == this.tokenStart) {
      return false;
    }
    throw new RuntimeException("Expected '\"' or '}' at " + this.src.getErrorHelp());
  }
  
  public boolean readValueNext()
  {
    readNextToken();
    if (',' == this.tokenStart) {
      return true;
    }
    if ('}' == this.tokenStart) {
      return false;
    }
    throw new RuntimeException("Expected ',' or '}' at " + this.src.getErrorHelp() + " but got " + this.tokenStart);
  }
  
  public boolean readArrayBegin()
  {
    readNextToken();
    if ('[' == this.tokenStart) {
      return true;
    }
    if ('n' == this.tokenStart) {
      return false;
    }
    throw new RuntimeException("Expected array begin at " + this.src.getErrorHelp());
  }
  
  public boolean readArrayNext()
  {
    readNextToken();
    if (',' == this.tokenStart) {
      return true;
    }
    if (']' == this.tokenStart) {
      return false;
    }
    throw new RuntimeException("Expected ',' or ']' at " + this.src.getErrorHelp());
  }
  
  public void readNextToken()
  {
    ignoreWhiteSpace();
    
    this.tokenStart = this.src.nextChar("EOF finding next token");
    switch (this.tokenStart)
    {
    case '"': 
      internalReadKey();
      break;
    case '{': 
      break;
    case '}': 
      break;
    case '[': 
      break;
    case ']': 
      break;
    case ',': 
      break;
    case ':': 
      break;
    case 'n': 
      internalReadNull();
      break;
    default: 
      throw new RuntimeException("Unexpected tokenStart[" + this.tokenStart + "] " + this.src.getErrorHelp());
    }
  }
  
  public String readQuotedValue()
  {
    boolean escape = false;
    StringBuilder sb = new StringBuilder();
    for (;;)
    {
      char ch = this.src.nextChar("EOF reading quoted value");
      if (escape)
      {
        escape = false;
        switch (ch)
        {
        case 'n': 
          sb.append('\n');
          break;
        case 'r': 
          sb.append('\r');
          break;
        case 't': 
          sb.append('\t');
          break;
        case 'f': 
          sb.append('\f');
          break;
        case 'b': 
          sb.append('\b');
          break;
        case '"': 
          sb.append('"');
          break;
        default: 
          sb.append('\\');
          sb.append(ch);
          break;
        }
      }
      else
      {
        switch (ch)
        {
        case '\\': 
          escape = true;
          break;
        case '"': 
          return sb.toString();
        default: 
          sb.append(ch);
        }
      }
    }
  }
  
  public String readUnquotedValue(char c)
  {
    String v = readUnquotedValueRaw(c);
    if ("null".equals(v)) {
      return null;
    }
    return v;
  }
  
  private String readUnquotedValueRaw(char c)
  {
    StringBuilder sb = new StringBuilder();
    sb.append(c);
    for (;;)
    {
      this.tokenStart = this.src.nextChar("EOF reading unquoted value");
      switch (this.tokenStart)
      {
      case ',': 
        this.src.back();
        return sb.toString();
      case '}': 
        this.src.back();
        return sb.toString();
      case ' ': 
        return sb.toString();
      case '\t': 
        return sb.toString();
      case '\r': 
        return sb.toString();
      case '\n': 
        return sb.toString();
      }
      sb.append(this.tokenStart);
    }
  }
  
  private void internalReadNull()
  {
    StringBuilder sb = new StringBuilder(4);
    sb.append(this.tokenStart);
    for (int i = 0; i < 3; i++)
    {
      char c = this.src.nextChar("EOF reading null ");
      sb.append(c);
    }
    if (!"null".equals(sb.toString())) {
      throw new TextException("Expected 'null' but got " + sb.toString() + " " + this.src.getErrorHelp());
    }
  }
  
  private void internalReadKey()
  {
    StringBuilder sb = new StringBuilder();
    for (;;)
    {
      char c = this.src.nextChar("EOF reading key");
      if ('"' == c)
      {
        this.tokenKey = sb.toString();
        break;
      }
      sb.append(c);
    }
    ignoreWhiteSpace();
    
    char c = this.src.nextChar("EOF reading ':'");
    if (':' != c) {
      throw new TextException("Expected to find colon after key at " + (this.src.pos() - 1) + " but found [" + c + "]" + this.src.getErrorHelp());
    }
  }
  
  public void ignoreWhiteSpace()
  {
    this.src.ignoreWhiteSpace();
  }
  
  public char nextChar()
  {
    this.tokenStart = this.src.nextChar("EOF getting nextChar for raw json");
    return this.tokenStart;
  }
}
