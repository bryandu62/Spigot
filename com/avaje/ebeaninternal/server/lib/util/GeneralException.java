package com.avaje.ebeaninternal.server.lib.util;

public class GeneralException
  extends RuntimeException
{
  private static final long serialVersionUID = 5783084420007103280L;
  
  public GeneralException(Exception cause)
  {
    super(cause);
  }
  
  public GeneralException(String s, Exception cause)
  {
    super(s, cause);
  }
  
  public GeneralException(String s)
  {
    super(s);
  }
}
