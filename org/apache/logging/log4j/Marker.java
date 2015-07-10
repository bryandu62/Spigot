package org.apache.logging.log4j;

import java.io.Serializable;

public abstract interface Marker
  extends Serializable
{
  public abstract String getName();
  
  public abstract Marker getParent();
  
  public abstract boolean isInstanceOf(Marker paramMarker);
  
  public abstract boolean isInstanceOf(String paramString);
}
