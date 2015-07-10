package com.avaje.ebean.text;

public class TextException
  extends RuntimeException
{
  private static final long serialVersionUID = 1601310159486033148L;
  
  public TextException(String msg)
  {
    super(msg);
  }
  
  public TextException(String msg, Exception e)
  {
    super(msg, e);
  }
  
  public TextException(Exception e)
  {
    super(e);
  }
}
