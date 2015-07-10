package org.apache.logging.log4j.core.filter;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;

public abstract interface Filterable
{
  public abstract void addFilter(Filter paramFilter);
  
  public abstract void removeFilter(Filter paramFilter);
  
  public abstract Filter getFilter();
  
  public abstract boolean hasFilter();
  
  public abstract boolean isFiltered(LogEvent paramLogEvent);
}
