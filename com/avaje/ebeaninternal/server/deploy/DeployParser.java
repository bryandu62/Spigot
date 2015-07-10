package com.avaje.ebeaninternal.server.deploy;

import java.util.Set;

public abstract class DeployParser
{
  protected static final char SINGLE_QUOTE = '\'';
  protected static final char COLON = ':';
  protected static final char UNDERSCORE = '_';
  protected static final char PERIOD = '.';
  protected boolean encrypted;
  protected String source;
  protected StringBuilder sb;
  protected int sourceLength;
  protected int pos;
  protected String word;
  protected char wordTerminator;
  
  protected abstract String convertWord();
  
  public abstract String getDeployWord(String paramString);
  
  public abstract Set<String> getIncludes();
  
  public void setEncrypted(boolean encrytped)
  {
    this.encrypted = encrytped;
  }
  
  public String parse(String source)
  {
    if (source == null) {
      return source;
    }
    this.pos = -1;
    this.source = source;
    this.sourceLength = source.length();
    this.sb = new StringBuilder(source.length() + 20);
    while (nextWord())
    {
      String deployWord = convertWord();
      this.sb.append(deployWord);
      if (this.pos < this.sourceLength) {
        this.sb.append(this.wordTerminator);
      }
    }
    return this.sb.toString();
  }
  
  private boolean nextWord()
  {
    if (!findWordStart()) {
      return false;
    }
    StringBuilder wordBuffer = new StringBuilder();
    wordBuffer.append(this.source.charAt(this.pos));
    while (++this.pos < this.sourceLength)
    {
      char ch = this.source.charAt(this.pos);
      if (isWordPart(ch))
      {
        wordBuffer.append(ch);
      }
      else
      {
        this.wordTerminator = ch;
        break;
      }
    }
    this.word = wordBuffer.toString();
    
    return true;
  }
  
  private boolean findWordStart()
  {
    while (++this.pos < this.sourceLength)
    {
      char ch = this.source.charAt(this.pos);
      if (ch == '\'')
      {
        this.sb.append(ch);
        readLiteral();
      }
      else if (ch == ':')
      {
        this.sb.append(ch);
        readNamedParameter();
      }
      else
      {
        if (isWordStart(ch)) {
          return true;
        }
        this.sb.append(ch);
      }
    }
    return false;
  }
  
  private void readLiteral()
  {
    while (++this.pos < this.sourceLength)
    {
      char ch = this.source.charAt(this.pos);
      this.sb.append(ch);
      if (ch == '\'') {
        break;
      }
    }
  }
  
  private void readNamedParameter()
  {
    while (++this.pos < this.sourceLength)
    {
      char ch = this.source.charAt(this.pos);
      this.sb.append(ch);
      if (Character.isWhitespace(ch)) {
        break;
      }
      if (ch == ',') {
        break;
      }
    }
  }
  
  private boolean isWordPart(char ch)
  {
    if (Character.isLetterOrDigit(ch)) {
      return true;
    }
    if (ch == '_') {
      return true;
    }
    if (ch == '.') {
      return true;
    }
    return false;
  }
  
  private boolean isWordStart(char ch)
  {
    if (Character.isLetter(ch)) {
      return true;
    }
    return false;
  }
}
