package com.avaje.ebeaninternal.server.el;

public final class CharMatch
{
  private final char[] upperChars;
  private final int maxLength;
  
  public CharMatch(String s)
  {
    this.upperChars = s.toUpperCase().toCharArray();
    this.maxLength = this.upperChars.length;
  }
  
  public boolean startsWith(String other)
  {
    if ((other == null) || (other.length() < this.maxLength)) {
      return false;
    }
    char[] ta = other.toCharArray();
    
    int pos = -1;
    for (;;)
    {
      pos++;
      if (pos >= this.maxLength) {
        break;
      }
      char c1 = this.upperChars[pos];
      char c2 = Character.toUpperCase(ta[pos]);
      if (c1 != c2) {
        return false;
      }
    }
    return true;
  }
  
  public boolean endsWith(String other)
  {
    if ((other == null) || (other.length() < this.maxLength)) {
      return false;
    }
    char[] ta = other.toCharArray();
    
    int offset = ta.length - this.maxLength;
    int pos = this.maxLength;
    for (;;)
    {
      pos--;
      if (pos < 0) {
        break;
      }
      char c1 = this.upperChars[pos];
      char c2 = Character.toUpperCase(ta[(offset + pos)]);
      if (c1 != c2) {
        return false;
      }
    }
    return true;
  }
}
