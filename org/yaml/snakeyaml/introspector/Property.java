package org.yaml.snakeyaml.introspector;

public abstract class Property
  implements Comparable<Property>
{
  private final String name;
  private final Class<?> type;
  
  public Property(String name, Class<?> type)
  {
    this.name = name;
    this.type = type;
  }
  
  public Class<?> getType()
  {
    return this.type;
  }
  
  public abstract Class<?>[] getActualTypeArguments();
  
  public String getName()
  {
    return this.name;
  }
  
  public String toString()
  {
    return getName() + " of " + getType();
  }
  
  public int compareTo(Property o)
  {
    return this.name.compareTo(o.name);
  }
  
  public boolean isWritable()
  {
    return true;
  }
  
  public boolean isReadable()
  {
    return true;
  }
  
  public abstract void set(Object paramObject1, Object paramObject2)
    throws Exception;
  
  public abstract Object get(Object paramObject);
  
  public int hashCode()
  {
    return this.name.hashCode() + this.type.hashCode();
  }
  
  public boolean equals(Object other)
  {
    if ((other instanceof Property))
    {
      Property p = (Property)other;
      return (this.name.equals(p.getName())) && (this.type.equals(p.getType()));
    }
    return false;
  }
}
