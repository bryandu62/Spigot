package org.apache.logging.log4j.core.appender.db.jpa.converter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.apache.logging.log4j.core.helpers.Strings;

@Converter(autoApply=false)
public class ThrowableAttributeConverter
  implements AttributeConverter<Throwable, String>
{
  private static final int CAUSED_BY_STRING_LENGTH = 10;
  private static final Field THROWABLE_CAUSE;
  private static final Field THROWABLE_MESSAGE;
  
  static
  {
    try
    {
      THROWABLE_CAUSE = Throwable.class.getDeclaredField("cause");
      THROWABLE_CAUSE.setAccessible(true);
      THROWABLE_MESSAGE = Throwable.class.getDeclaredField("detailMessage");
      THROWABLE_MESSAGE.setAccessible(true);
    }
    catch (NoSuchFieldException e)
    {
      throw new IllegalStateException("Something is wrong with java.lang.Throwable.", e);
    }
  }
  
  public String convertToDatabaseColumn(Throwable throwable)
  {
    if (throwable == null) {
      return null;
    }
    StringBuilder builder = new StringBuilder();
    convertThrowable(builder, throwable);
    return builder.toString();
  }
  
  private void convertThrowable(StringBuilder builder, Throwable throwable)
  {
    builder.append(throwable.toString()).append('\n');
    for (StackTraceElement element : throwable.getStackTrace()) {
      builder.append("\tat ").append(element).append('\n');
    }
    if (throwable.getCause() != null)
    {
      builder.append("Caused by ");
      convertThrowable(builder, throwable.getCause());
    }
  }
  
  public Throwable convertToEntityAttribute(String s)
  {
    if (Strings.isEmpty(s)) {
      return null;
    }
    List<String> lines = Arrays.asList(s.split("(\n|\r\n)"));
    return convertString(lines.listIterator(), false);
  }
  
  private Throwable convertString(ListIterator<String> lines, boolean removeCausedBy)
  {
    String firstLine = (String)lines.next();
    if (removeCausedBy) {
      firstLine = firstLine.substring(10);
    }
    int colon = firstLine.indexOf(":");
    
    String message = null;
    String throwableClassName;
    if (colon > 1)
    {
      String throwableClassName = firstLine.substring(0, colon);
      if (firstLine.length() > colon + 1) {
        message = firstLine.substring(colon + 1).trim();
      }
    }
    else
    {
      throwableClassName = firstLine;
    }
    List<StackTraceElement> stackTrace = new ArrayList();
    Throwable cause = null;
    while (lines.hasNext())
    {
      String line = (String)lines.next();
      if (line.startsWith("Caused by "))
      {
        lines.previous();
        cause = convertString(lines, true);
        break;
      }
      stackTrace.add(StackTraceElementAttributeConverter.convertString(line.trim().substring(3).trim()));
    }
    return getThrowable(throwableClassName, message, cause, (StackTraceElement[])stackTrace.toArray(new StackTraceElement[stackTrace.size()]));
  }
  
  private Throwable getThrowable(String throwableClassName, String message, Throwable cause, StackTraceElement[] stackTrace)
  {
    try
    {
      Class<Throwable> throwableClass = Class.forName(throwableClassName);
      if (!Throwable.class.isAssignableFrom(throwableClass)) {
        return null;
      }
      Throwable throwable;
      if ((message != null) && (cause != null))
      {
        Throwable throwable = getThrowable(throwableClass, message, cause);
        if (throwable == null)
        {
          throwable = getThrowable(throwableClass, cause);
          if (throwable == null)
          {
            throwable = getThrowable(throwableClass, message);
            if (throwable == null)
            {
              throwable = getThrowable(throwableClass);
              if (throwable != null)
              {
                THROWABLE_MESSAGE.set(throwable, message);
                THROWABLE_CAUSE.set(throwable, cause);
              }
            }
            else
            {
              THROWABLE_CAUSE.set(throwable, cause);
            }
          }
          else
          {
            THROWABLE_MESSAGE.set(throwable, message);
          }
        }
      }
      else if (cause != null)
      {
        Throwable throwable = getThrowable(throwableClass, cause);
        if (throwable == null)
        {
          throwable = getThrowable(throwableClass);
          if (throwable != null) {
            THROWABLE_CAUSE.set(throwable, cause);
          }
        }
      }
      else if (message != null)
      {
        Throwable throwable = getThrowable(throwableClass, message);
        if (throwable == null)
        {
          throwable = getThrowable(throwableClass);
          if (throwable != null) {
            THROWABLE_MESSAGE.set(throwable, cause);
          }
        }
      }
      else
      {
        throwable = getThrowable(throwableClass);
      }
      if (throwable == null) {
        return null;
      }
      throwable.setStackTrace(stackTrace);
      return throwable;
    }
    catch (Exception e) {}
    return null;
  }
  
  private Throwable getThrowable(Class<Throwable> throwableClass, String message, Throwable cause)
  {
    try
    {
      Constructor<Throwable>[] constructors = (Constructor[])throwableClass.getConstructors();
      for (Constructor<Throwable> constructor : constructors)
      {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        if (parameterTypes.length == 2)
        {
          if ((String.class == parameterTypes[0]) && (Throwable.class.isAssignableFrom(parameterTypes[1]))) {
            return (Throwable)constructor.newInstance(new Object[] { message, cause });
          }
          if ((String.class == parameterTypes[1]) && (Throwable.class.isAssignableFrom(parameterTypes[0]))) {
            return (Throwable)constructor.newInstance(new Object[] { cause, message });
          }
        }
      }
      return null;
    }
    catch (Exception e) {}
    return null;
  }
  
  private Throwable getThrowable(Class<Throwable> throwableClass, Throwable cause)
  {
    try
    {
      Constructor<Throwable>[] constructors = (Constructor[])throwableClass.getConstructors();
      for (Constructor<Throwable> constructor : constructors)
      {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        if ((parameterTypes.length == 1) && (Throwable.class.isAssignableFrom(parameterTypes[0]))) {
          return (Throwable)constructor.newInstance(new Object[] { cause });
        }
      }
      return null;
    }
    catch (Exception e) {}
    return null;
  }
  
  private Throwable getThrowable(Class<Throwable> throwableClass, String message)
  {
    try
    {
      return (Throwable)throwableClass.getConstructor(new Class[] { String.class }).newInstance(new Object[] { message });
    }
    catch (Exception e) {}
    return null;
  }
  
  private Throwable getThrowable(Class<Throwable> throwableClass)
  {
    try
    {
      return (Throwable)throwableClass.newInstance();
    }
    catch (Exception e) {}
    return null;
  }
}
