package com.avaje.ebeaninternal.server.type.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.logging.Logger;

public class CheckImmutable
{
  private static Logger logger = Logger.getLogger(CheckImmutable.class.getName());
  private final KnownImmutable knownImmutable;
  
  public CheckImmutable(KnownImmutable knownImmutable)
  {
    this.knownImmutable = knownImmutable;
  }
  
  public CheckImmutableResponse checkImmutable(Class<?> cls)
  {
    CheckImmutableResponse res = new CheckImmutableResponse();
    
    isImmutable(cls, res);
    if (res.isImmutable()) {
      res.setCompoundType(isCompoundType(cls));
    }
    return res;
  }
  
  private boolean isCompoundType(Class<?> cls)
  {
    int maxLength = 0;
    Constructor<?> chosen = null;
    
    Constructor<?>[] constructors = cls.getConstructors();
    for (int i = 0; i < constructors.length; i++)
    {
      Class<?>[] parameterTypes = constructors[i].getParameterTypes();
      if (parameterTypes.length > maxLength)
      {
        maxLength = parameterTypes.length;
        chosen = constructors[i];
      }
    }
    logger.fine("checkImmutable " + cls + " constructor " + chosen);
    
    return maxLength > 1;
  }
  
  private boolean isImmutable(Class<?> cls, CheckImmutableResponse res)
  {
    if (this.knownImmutable.isKnownImmutable(cls)) {
      return true;
    }
    if (cls.isArray()) {
      return false;
    }
    if (hasDefaultConstructor(cls))
    {
      res.setReasonNotImmutable(cls + " has a default constructor");
      return false;
    }
    Class<?> superClass = cls.getSuperclass();
    if (!isImmutable(superClass, res))
    {
      res.setReasonNotImmutable("Super not Immutable " + superClass);
      return false;
    }
    if (!hasAllFinalFields(cls, res)) {
      return false;
    }
    return true;
  }
  
  private boolean hasAllFinalFields(Class<?> cls, CheckImmutableResponse res)
  {
    Field[] objFields = cls.getDeclaredFields();
    for (int i = 0; i < objFields.length; i++) {
      if (!Modifier.isStatic(objFields[i].getModifiers()))
      {
        if (!Modifier.isFinal(objFields[i].getModifiers()))
        {
          res.setReasonNotImmutable("Non final field " + cls + "." + objFields[i].getName());
          return false;
        }
        if (!isImmutable(objFields[i].getType(), res))
        {
          res.setReasonNotImmutable("Non Immutable field type " + objFields[i].getType());
          return false;
        }
      }
    }
    return true;
  }
  
  private boolean hasDefaultConstructor(Class<?> cls)
  {
    Class<?>[] noParams = new Class[0];
    try
    {
      cls.getDeclaredConstructor(noParams);
      return true;
    }
    catch (SecurityException e)
    {
      return false;
    }
    catch (NoSuchMethodException e) {}
    return false;
  }
}
