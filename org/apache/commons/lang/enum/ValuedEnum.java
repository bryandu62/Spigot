package org.apache.commons.lang.enum;

import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.ClassUtils;

/**
 * @deprecated
 */
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
    return this.iValue - ((ValuedEnum)other).iValue;
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
