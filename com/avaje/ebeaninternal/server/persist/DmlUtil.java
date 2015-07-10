package com.avaje.ebeaninternal.server.persist;

import com.avaje.ebeaninternal.server.deploy.parse.DetectScala;
import scala.Option;

public class DmlUtil
{
  private static final boolean hasScalaSupport = ;
  
  public static boolean isNullOrZero(Object value)
  {
    if (value == null) {
      return true;
    }
    if ((value instanceof Number)) {
      return ((Number)value).longValue() == 0L;
    }
    if ((hasScalaSupport) && 
      ((value instanceof Option)) && 
      (((Option)value).isEmpty())) {
      return true;
    }
    return false;
  }
}
