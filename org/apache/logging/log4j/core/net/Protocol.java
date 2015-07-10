package org.apache.logging.log4j.core.net;

public enum Protocol
{
  TCP,  UDP;
  
  private Protocol() {}
  
  public boolean isEqual(String name)
  {
    return name().equalsIgnoreCase(name);
  }
}
