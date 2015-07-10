package com.avaje.ebeaninternal.server.querydefn;

public class SimpleTextParser
{
  private final String oql;
  private final char[] chars;
  private final int eof;
  private int pos;
  private String word;
  private String lowerWord;
  private int openParenthesisCount;
  
  public SimpleTextParser(String oql)
  {
    this.oql = oql;
    this.chars = oql.toCharArray();
    this.eof = oql.length();
  }
  
  public int getPos()
  {
    return this.pos;
  }
  
  public String getOql()
  {
    return this.oql;
  }
  
  public String getWord()
  {
    return this.word;
  }
  
  public String peekNextWord()
  {
    int origPos = this.pos;
    String nw = nextWordInternal();
    this.pos = origPos;
    return nw;
  }
  
  public boolean isMatch(String lowerMatch, String nextWordMatch)
  {
    if (isMatch(lowerMatch))
    {
      String nw = peekNextWord();
      if (nw != null)
      {
        nw = nw.toLowerCase();
        return nw.equals(nextWordMatch);
      }
    }
    return false;
  }
  
  public boolean isFinished()
  {
    return this.word == null;
  }
  
  public int findWordLower(String lowerMatch, int afterPos)
  {
    this.pos = afterPos;
    return findWordLower(lowerMatch);
  }
  
  public int findWordLower(String lowerMatch)
  {
    do
    {
      if (nextWord() == null) {
        return -1;
      }
    } while (!lowerMatch.equals(this.lowerWord));
    return this.pos - this.lowerWord.length();
  }
  
  public boolean isMatch(String lowerMatch)
  {
    return lowerMatch.equals(this.lowerWord);
  }
  
  public String nextWord()
  {
    this.word = nextWordInternal();
    if (this.word != null) {
      this.lowerWord = this.word.toLowerCase();
    }
    return this.word;
  }
  
  private String nextWordInternal()
  {
    trimLeadingWhitespace();
    if (this.pos >= this.eof) {
      return null;
    }
    int start = this.pos;
    if (this.chars[this.pos] == '(') {
      moveToClose();
    } else {
      moveToEndOfWord();
    }
    return this.oql.substring(start, this.pos);
  }
  
  private void moveToClose()
  {
    this.pos += 1;
    this.openParenthesisCount = 0;
    for (; this.pos < this.eof; this.pos += 1)
    {
      char c = this.chars[this.pos];
      if (c == '(') {
        this.openParenthesisCount += 1;
      } else if (c == ')') {
        if (this.openParenthesisCount > 0)
        {
          this.openParenthesisCount -= 1;
        }
        else
        {
          this.pos += 1;
          return;
        }
      }
    }
  }
  
  private void moveToEndOfWord()
  {
    char c = this.chars[this.pos];
    boolean isOperator = isOperator(c);
    for (; this.pos < this.eof; this.pos += 1)
    {
      c = this.chars[this.pos];
      if (isWordTerminator(c, isOperator)) {
        return;
      }
    }
  }
  
  private boolean isWordTerminator(char c, boolean isOperator)
  {
    if (Character.isWhitespace(c)) {
      return true;
    }
    if (isOperator(c)) {
      return !isOperator;
    }
    if (c == '(') {
      return true;
    }
    return isOperator;
  }
  
  private boolean isOperator(char c)
  {
    switch (c)
    {
    case '<': 
      return true;
    case '>': 
      return true;
    case '=': 
      return true;
    case '!': 
      return true;
    }
    return false;
  }
  
  private void trimLeadingWhitespace()
  {
    for (; this.pos < this.eof; this.pos += 1)
    {
      char c = this.chars[this.pos];
      if (!Character.isWhitespace(c)) {
        break;
      }
    }
  }
}
