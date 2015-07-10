package org.apache.logging.log4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class MarkerManager
{
  private static ConcurrentMap<String, Marker> markerMap = new ConcurrentHashMap();
  
  public static Marker getMarker(String name)
  {
    markerMap.putIfAbsent(name, new Log4jMarker(name));
    return (Marker)markerMap.get(name);
  }
  
  public static Marker getMarker(String name, String parent)
  {
    Marker parentMarker = (Marker)markerMap.get(parent);
    if (parentMarker == null) {
      throw new IllegalArgumentException("Parent Marker " + parent + " has not been defined");
    }
    return getMarker(name, parentMarker);
  }
  
  public static Marker getMarker(String name, Marker parent)
  {
    markerMap.putIfAbsent(name, new Log4jMarker(name, parent));
    return (Marker)markerMap.get(name);
  }
  
  private static class Log4jMarker
    implements Marker
  {
    private static final long serialVersionUID = 100L;
    private final String name;
    private final Marker parent;
    
    public Log4jMarker(String name)
    {
      this.name = name;
      this.parent = null;
    }
    
    public Log4jMarker(String name, Marker parent)
    {
      this.name = name;
      this.parent = parent;
    }
    
    public String getName()
    {
      return this.name;
    }
    
    public Marker getParent()
    {
      return this.parent;
    }
    
    public boolean isInstanceOf(Marker m)
    {
      if (m == null) {
        throw new IllegalArgumentException("A marker parameter is required");
      }
      Marker test = this;
      do
      {
        if (test == m) {
          return true;
        }
        test = test.getParent();
      } while (test != null);
      return false;
    }
    
    public boolean isInstanceOf(String name)
    {
      if (name == null) {
        throw new IllegalArgumentException("A marker name is required");
      }
      Marker toTest = this;
      do
      {
        if (name.equals(toTest.getName())) {
          return true;
        }
        toTest = toTest.getParent();
      } while (toTest != null);
      return false;
    }
    
    public boolean equals(Object o)
    {
      if (this == o) {
        return true;
      }
      if ((o == null) || (!(o instanceof Marker))) {
        return false;
      }
      Marker marker = (Marker)o;
      if (this.name != null ? !this.name.equals(marker.getName()) : marker.getName() != null) {
        return false;
      }
      return true;
    }
    
    public int hashCode()
    {
      return this.name != null ? this.name.hashCode() : 0;
    }
    
    public String toString()
    {
      StringBuilder sb = new StringBuilder(this.name);
      if (this.parent != null)
      {
        Marker m = this.parent;
        sb.append("[ ");
        boolean first = true;
        while (m != null)
        {
          if (!first) {
            sb.append(", ");
          }
          sb.append(m.getName());
          first = false;
          m = m.getParent();
        }
        sb.append(" ]");
      }
      return sb.toString();
    }
  }
}
