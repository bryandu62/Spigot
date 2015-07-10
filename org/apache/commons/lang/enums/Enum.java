package org.apache.commons.lang.enums;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;

public abstract class Enum
  implements Comparable, Serializable
{
  private static final long serialVersionUID = -487045951170455942L;
  private static final Map EMPTY_MAP = Collections.unmodifiableMap(new HashMap(0));
  private static Map cEnumClasses = new WeakHashMap();
  private final String iName;
  private final transient int iHashCode;
  protected transient String iToString = null;
  
  private static class Entry
  {
    final Map map = new HashMap();
    final Map unmodifiableMap = Collections.unmodifiableMap(this.map);
    final List list = new ArrayList(25);
    final List unmodifiableList = Collections.unmodifiableList(this.list);
  }
  
  protected Enum(String name)
  {
    init(name);
    this.iName = name;
    this.iHashCode = (7 + getEnumClass().hashCode() + 3 * name.hashCode());
  }
  
  private void init(String name)
  {
    if (StringUtils.isEmpty(name)) {
      throw new IllegalArgumentException("The Enum name must not be empty or null");
    }
    Class enumClass = getEnumClass();
    if (enumClass == null) {
      throw new IllegalArgumentException("getEnumClass() must not be null");
    }
    Class cls = getClass();
    boolean ok = false;
    while ((cls != null) && (cls != Enum.class) && (cls != ValuedEnum.class))
    {
      if (cls == enumClass)
      {
        ok = true;
        break;
      }
      cls = cls.getSuperclass();
    }
    if (!ok) {
      throw new IllegalArgumentException("getEnumClass() must return a superclass of this class");
    }
    Entry entry;
    synchronized (Enum.class)
    {
      entry = (Entry)cEnumClasses.get(enumClass);
      if (entry == null)
      {
        entry = createEntry(enumClass);
        Map myMap = new WeakHashMap();
        myMap.putAll(cEnumClasses);
        myMap.put(enumClass, entry);
        cEnumClasses = myMap;
      }
    }
    if (entry.map.containsKey(name)) {
      throw new IllegalArgumentException("The Enum name must be unique, '" + name + "' has already been added");
    }
    entry.map.put(name, this);
    entry.list.add(this);
  }
  
  protected Object readResolve()
  {
    Entry entry = (Entry)cEnumClasses.get(getEnumClass());
    if (entry == null) {
      return null;
    }
    return entry.map.get(getName());
  }
  
  protected static Enum getEnum(Class enumClass, String name)
  {
    Entry entry = getEntry(enumClass);
    if (entry == null) {
      return null;
    }
    return (Enum)entry.map.get(name);
  }
  
  protected static Map getEnumMap(Class enumClass)
  {
    Entry entry = getEntry(enumClass);
    if (entry == null) {
      return EMPTY_MAP;
    }
    return entry.unmodifiableMap;
  }
  
  protected static List getEnumList(Class enumClass)
  {
    Entry entry = getEntry(enumClass);
    if (entry == null) {
      return Collections.EMPTY_LIST;
    }
    return entry.unmodifiableList;
  }
  
  protected static Iterator iterator(Class enumClass)
  {
    return getEnumList(enumClass).iterator();
  }
  
  private static Entry getEntry(Class enumClass)
  {
    if (enumClass == null) {
      throw new IllegalArgumentException("The Enum Class must not be null");
    }
    if (!Enum.class.isAssignableFrom(enumClass)) {
      throw new IllegalArgumentException("The Class must be a subclass of Enum");
    }
    Entry entry = (Entry)cEnumClasses.get(enumClass);
    if (entry == null) {
      try
      {
        Class.forName(enumClass.getName(), true, enumClass.getClassLoader());
        entry = (Entry)cEnumClasses.get(enumClass);
      }
      catch (Exception e) {}
    }
    return entry;
  }
  
  private static Entry createEntry(Class enumClass)
  {
    Entry entry = new Entry();
    Class cls = enumClass.getSuperclass();
    while ((cls != null) && (cls != Enum.class) && (cls != ValuedEnum.class))
    {
      Entry loopEntry = (Entry)cEnumClasses.get(cls);
      if (loopEntry != null)
      {
        entry.list.addAll(loopEntry.list);
        entry.map.putAll(loopEntry.map);
        break;
      }
      cls = cls.getSuperclass();
    }
    return entry;
  }
  
  public final String getName()
  {
    return this.iName;
  }
  
  public Class getEnumClass()
  {
    return getClass();
  }
  
  public final boolean equals(Object other)
  {
    if (other == this) {
      return true;
    }
    if (other == null) {
      return false;
    }
    if (other.getClass() == getClass()) {
      return this.iName.equals(((Enum)other).iName);
    }
    if (!other.getClass().getName().equals(getClass().getName())) {
      return false;
    }
    return this.iName.equals(getNameInOtherClassLoader(other));
  }
  
  public final int hashCode()
  {
    return this.iHashCode;
  }
  
  public int compareTo(Object other)
  {
    if (other == this) {
      return 0;
    }
    if (other.getClass() != getClass())
    {
      if (other.getClass().getName().equals(getClass().getName())) {
        return this.iName.compareTo(getNameInOtherClassLoader(other));
      }
      throw new ClassCastException("Different enum class '" + ClassUtils.getShortClassName(other.getClass()) + "'");
    }
    return this.iName.compareTo(((Enum)other).iName);
  }
  
  private String getNameInOtherClassLoader(Object other)
  {
    try
    {
      Method mth = other.getClass().getMethod("getName", null);
      return (String)mth.invoke(other, null);
    }
    catch (NoSuchMethodException e) {}catch (IllegalAccessException e) {}catch (InvocationTargetException e) {}
    throw new IllegalStateException("This should not happen");
  }
  
  public String toString()
  {
    if (this.iToString == null)
    {
      String shortName = ClassUtils.getShortClassName(getEnumClass());
      this.iToString = (shortName + "[" + getName() + "]");
    }
    return this.iToString;
  }
}
