package org.apache.commons.lang.enums;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.ClassUtils;

public abstract class ValuedEnum
  extends Enum
{
  private static final long serialVersionUID = -7129650521543789085L;
  private final int iValue;
  
  protected ValuedEnum(String name, int value)
  {
    super(name);
    this.iValue = value;
  }
  
  protected static Enum getEnum(Class enumClass, int value)
  {
    if (enumClass == null) {
      throw new IllegalArgumentException("The Enum Class must not be null");
    }
    List list = Enum.getEnumList(enumClass);
    for (Iterator it = list.iterator(); it.hasNext();)
    {
      ValuedEnum enumeration = (ValuedEnum)it.next();
      if (enumeration.getValue() == value) {
        return enumeration;
      }
    }
    return null;
  }
  
  public final int getValue()
  {
    return this.iValue;
  }
  
  public int compareTo(Object other)
  {
    if (other == this) {
      return 0;
    }
    if (other.getClass() != getClass())
    {
      if (other.getClass().getName().equals(getClass().getName())) {
        return this.iValue - getValueInOtherClassLoader(other);
      }
      throw new ClassCastException("Different enum class '" + ClassUtils.getShortClassName(other.getClass()) + "'");
    }
    return this.iValue - ((ValuedEnum)other).iValue;
  }
  
  private int getValueInOtherClassLoader(Object other)
  {
    try
    {
      Method mth = other.getClass().getMethod("getValue", null);
      Integer value = (Integer)mth.invoke(other, null);
      return value.intValue();
    }
    catch (NoSuchMethodException e) {}catch (IllegalAccessException e) {}catch (InvocationTargetException e) {}
    throw new IllegalStateException("This should not happen");
  }
  
  public String toString()
  {
    if (this.iToString == null)
    {
      String shortName = ClassUtils.getShortClassName(getEnumClass());
      this.iToString = (shortName + "[" + getName() + "=" + getValue() + "]");
    }
    return this.iToString;
  }
}
