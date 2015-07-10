package com.avaje.ebeaninternal.server.lib.util;

public class InvalidDataException
  extends RuntimeException
{
  static final long serialVersionUID = 7061559938704539846L;
  
  public InvalidDataException(Exception cause)
  {
    super(cause);
  }
  
  public InvalidDataException(String s, Exception cause)
  {
    super(s, cause);
  }
  
  public InvalidDataException(String s)
  {
    super(s);
  }
}
