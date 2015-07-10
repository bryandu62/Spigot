package com.avaje.ebean.enhance.agent;

import com.avaje.ebean.enhance.asm.Type;

public class PrimitiveHelper
{
  private static Type INTEGER_OBJECT = Type.getType(Integer.class);
  private static Type SHORT_OBJECT = Type.getType(Short.class);
  private static Type CHARACTER_OBJECT = Type.getType(Character.class);
  private static Type LONG_OBJECT = Type.getType(Long.class);
  private static Type DOUBLE_OBJECT = Type.getType(Double.class);
  private static Type FLOAT_OBJECT = Type.getType(Float.class);
  private static Type BYTE_OBJECT = Type.getType(Byte.class);
  private static Type BOOLEAN_OBJECT = Type.getType(Boolean.class);
  
  public static Type getObjectWrapper(Type primativeAsmType)
  {
    int sort = primativeAsmType.getSort();
    switch (sort)
    {
    case 5: 
      return INTEGER_OBJECT;
    case 4: 
      return SHORT_OBJECT;
    case 2: 
      return CHARACTER_OBJECT;
    case 7: 
      return LONG_OBJECT;
    case 8: 
      return DOUBLE_OBJECT;
    case 6: 
      return FLOAT_OBJECT;
    case 3: 
      return BYTE_OBJECT;
    case 1: 
      return BOOLEAN_OBJECT;
    }
    throw new RuntimeException("Expected primative? " + primativeAsmType);
  }
}
