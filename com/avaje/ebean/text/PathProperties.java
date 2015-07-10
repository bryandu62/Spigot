package com.avaje.ebean.text;

import com.avaje.ebean.Query;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class PathProperties
{
  private final Map<String, Props> pathMap;
  private final Props rootProps;
  
  public static PathProperties parse(String source)
  {
    return PathPropertiesParser.parse(source);
  }
  
  public PathProperties()
  {
    this.rootProps = new Props(this, null, null, null);
    this.pathMap = new LinkedHashMap();
    this.pathMap.put(null, this.rootProps);
  }
  
  private PathProperties(PathProperties orig)
  {
    this.rootProps = orig.rootProps.copy(this);
    this.pathMap = new LinkedHashMap(orig.pathMap.size());
    Set<Map.Entry<String, Props>> entrySet = orig.pathMap.entrySet();
    for (Map.Entry<String, Props> e : entrySet) {
      this.pathMap.put(e.getKey(), ((Props)e.getValue()).copy(this));
    }
  }
  
  public PathProperties copy()
  {
    return new PathProperties(this);
  }
  
  public boolean isEmpty()
  {
    return this.pathMap.isEmpty();
  }
  
  public String toString()
  {
    return this.pathMap.toString();
  }
  
  public boolean hasPath(String path)
  {
    Props props = (Props)this.pathMap.get(path);
    return (props != null) && (!props.isEmpty());
  }
  
  public Set<String> get(String path)
  {
    Props props = (Props)this.pathMap.get(path);
    return props == null ? null : props.getProperties();
  }
  
  public void addToPath(String path, String property)
  {
    Props props = (Props)this.pathMap.get(path);
    if (props == null)
    {
      props = new Props(this, null, path, null);
      this.pathMap.put(path, props);
    }
    props.getProperties().add(property);
  }
  
  public void put(String path, Set<String> properties)
  {
    this.pathMap.put(path, new Props(this, null, path, properties, null));
  }
  
  public Set<String> remove(String path)
  {
    Props props = (Props)this.pathMap.remove(path);
    return props == null ? null : props.getProperties();
  }
  
  public Set<String> getPaths()
  {
    return new LinkedHashSet(this.pathMap.keySet());
  }
  
  public Collection<Props> getPathProps()
  {
    return this.pathMap.values();
  }
  
  public void apply(Query<?> query)
  {
    for (Map.Entry<String, Props> entry : this.pathMap.entrySet())
    {
      String path = (String)entry.getKey();
      String props = ((Props)entry.getValue()).getPropertiesAsString();
      if ((path == null) || (path.length() == 0)) {
        query.select(props);
      } else {
        query.fetch(path, props);
      }
    }
  }
  
  protected Props getRootProperties()
  {
    return this.rootProps;
  }
  
  public static class Props
  {
    private final PathProperties owner;
    private final String parentPath;
    private final String path;
    private final Set<String> propSet;
    
    private Props(PathProperties owner, String parentPath, String path, Set<String> propSet)
    {
      this.owner = owner;
      this.path = path;
      this.parentPath = parentPath;
      this.propSet = propSet;
    }
    
    private Props(PathProperties owner, String parentPath, String path)
    {
      this(owner, parentPath, path, new LinkedHashSet());
    }
    
    public Props copy(PathProperties newOwner)
    {
      return new Props(newOwner, this.parentPath, this.path, new LinkedHashSet(this.propSet));
    }
    
    public String getPath()
    {
      return this.path;
    }
    
    public String toString()
    {
      return this.propSet.toString();
    }
    
    public boolean isEmpty()
    {
      return this.propSet.isEmpty();
    }
    
    public Set<String> getProperties()
    {
      return this.propSet;
    }
    
    public String getPropertiesAsString()
    {
      StringBuilder sb = new StringBuilder();
      
      Iterator<String> it = this.propSet.iterator();
      boolean hasNext = it.hasNext();
      while (hasNext)
      {
        sb.append((String)it.next());
        hasNext = it.hasNext();
        if (hasNext) {
          sb.append(",");
        }
      }
      return sb.toString();
    }
    
    protected Props getParent()
    {
      return (Props)this.owner.pathMap.get(this.parentPath);
    }
    
    protected Props addChild(String subpath)
    {
      subpath = subpath.trim();
      addProperty(subpath);
      
      String p = this.path + "." + subpath;
      Props nested = new Props(this.owner, this.path, p);
      this.owner.pathMap.put(p, nested);
      return nested;
    }
    
    protected void addProperty(String property)
    {
      this.propSet.add(property.trim());
    }
  }
}
