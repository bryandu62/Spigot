package org.apache.logging.log4j.status;

import org.apache.logging.log4j.Level;

public abstract interface StatusListener
{
  public abstract void log(StatusData paramStatusData);
  
  public abstract Level getStatusLevel();
}
