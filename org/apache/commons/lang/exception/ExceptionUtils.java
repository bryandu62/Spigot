package org.apache.commons.lang.exception;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;

public class ExceptionUtils
{
  static final String WRAPPED_MARKER = " [wrapped] ";
  private static final Object CAUSE_METHOD_NAMES_LOCK = new Object();
  private static String[] CAUSE_METHOD_NAMES = { "getCause", "getNextException", "getTargetException", "getException", "getSourceException", "getRootCause", "getCausedByException", "getNested", "getLinkedException", "getNestedException", "getLinkedCause", "getThrowable" };
  private static final Method THROWABLE_CAUSE_METHOD;
  private static final Method THROWABLE_INITCAUSE_METHOD;
  
  static
  {
    Method causeMethod;
    try
    {
      causeMethod = Throwable.class.getMethod("getCause", null);
    }
    catch (Exception e)
    {
      causeMethod = null;
    }
    THROWABLE_CAUSE_METHOD = causeMethod;
    try
    {
      causeMethod = class$java$lang$Throwable.getMethod("initCause", new Class[] { Throwable.class });
    }
    catch (Exception e)
    {
      causeMethod = null;
    }
    THROWABLE_INITCAUSE_METHOD = causeMethod;
  }
  
  public static void addCauseMethodName(String methodName)
  {
    if ((StringUtils.isNotEmpty(methodName)) && (!isCauseMethodName(methodName)))
    {
      List list = getCauseMethodNameList();
      if (list.add(methodName)) {
        synchronized (CAUSE_METHOD_NAMES_LOCK)
        {
          CAUSE_METHOD_NAMES = toArray(list);
        }
      }
    }
  }
  
  public static void removeCauseMethodName(String methodName)
  {
    if (StringUtils.isNotEmpty(methodName))
    {
      List list = getCauseMethodNameList();
      if (list.remove(methodName)) {
        synchronized (CAUSE_METHOD_NAMES_LOCK)
        {
          CAUSE_METHOD_NAMES = toArray(list);
        }
      }
    }
  }
  
  public static boolean setCause(Throwable target, Throwable cause)
  {
    if (target == null) {
      throw new NullArgumentException("target");
    }
    Object[] causeArgs = { cause };
    boolean modifiedTarget = false;
    if (THROWABLE_INITCAUSE_METHOD != null) {
      try
      {
        THROWABLE_INITCAUSE_METHOD.invoke(target, causeArgs);
        modifiedTarget = true;
      }
      catch (IllegalAccessException ignored) {}catch (InvocationTargetException ignored) {}
    }
    try
    {
      Method setCauseMethod = target.getClass().getMethod("setCause", new Class[] { Throwable.class });
      setCauseMethod.invoke(target, causeArgs);
      modifiedTarget = true;
    }
    catch (NoSuchMethodException ignored) {}catch (IllegalAccessException ignored) {}catch (InvocationTargetException ignored) {}
    return modifiedTarget;
  }
  
  private static String[] toArray(List list)
  {
    return (String[])list.toArray(new String[list.size()]);
  }
  
  public static boolean isCauseMethodName(String methodName)
  {
    synchronized (CAUSE_METHOD_NAMES_LOCK)
    {
      return ArrayUtils.indexOf(CAUSE_METHOD_NAMES, methodName) >= 0;
    }
  }
  
  public static Throwable getCause(Throwable throwable, String[] methodNames)
  {
    if (throwable == null) {
      return null;
    }
    Throwable cause = getCauseUsingWellKnownTypes(throwable);
    if (cause == null)
    {
      if (methodNames == null) {
        synchronized (CAUSE_METHOD_NAMES_LOCK)
        {
          methodNames = CAUSE_METHOD_NAMES;
        }
      }
      for (int i = 0; i < methodNames.length; i++)
      {
        String methodName = methodNames[i];
        if (methodName != null)
        {
          cause = getCauseUsingMethodName(throwable, methodName);
          if (cause != null) {
            break;
          }
        }
      }
      if (cause == null) {
        cause = getCauseUsingFieldName(throwable, "detail");
      }
    }
    return cause;
  }
  
  public static Throwable getRootCause(Throwable throwable)
  {
    List list = getThrowableList(throwable);
    return list.size() < 2 ? null : (Throwable)list.get(list.size() - 1);
  }
  
  private static Throwable getCauseUsingWellKnownTypes(Throwable throwable)
  {
    if ((throwable instanceof Nestable)) {
      return ((Nestable)throwable).getCause();
    }
    if ((throwable instanceof SQLException)) {
      return ((SQLException)throwable).getNextException();
    }
    if ((throwable instanceof InvocationTargetException)) {
      return ((InvocationTargetException)throwable).getTargetException();
    }
    return null;
  }
  
  private static Throwable getCauseUsingMethodName(Throwable throwable, String methodName)
  {
    Method method = null;
    try
    {
      method = throwable.getClass().getMethod(methodName, null);
    }
    catch (NoSuchMethodException ignored) {}catch (SecurityException ignored) {}
    if ((method != null) && (Throwable.class.isAssignableFrom(method.getReturnType()))) {
      try
      {
        return (Throwable)method.invoke(throwable, ArrayUtils.EMPTY_OBJECT_ARRAY);
      }
      catch (IllegalAccessException ignored) {}catch (IllegalArgumentException ignored) {}catch (InvocationTargetException ignored) {}
    }
    return null;
  }
  
  private static Throwable getCauseUsingFieldName(Throwable throwable, String fieldName)
  {
    Field field = null;
    try
    {
      field = throwable.getClass().getField(fieldName);
    }
    catch (NoSuchFieldException ignored) {}catch (SecurityException ignored) {}
    if ((field != null) && (Throwable.class.isAssignableFrom(field.getType()))) {
      try
      {
        return (Throwable)field.get(throwable);
      }
      catch (IllegalAccessException ignored) {}catch (IllegalArgumentException ignored) {}
    }
    return null;
  }
  
  public static boolean isThrowableNested()
  {
    return THROWABLE_CAUSE_METHOD != null;
  }
  
  public static boolean isNestedThrowable(Throwable throwable)
  {
    if (throwable == null) {
      return false;
    }
    if ((throwable instanceof Nestable)) {
      return true;
    }
    if ((throwable instanceof SQLException)) {
      return true;
    }
    if ((throwable instanceof InvocationTargetException)) {
      return true;
    }
    if (isThrowableNested()) {
      return true;
    }
    Class cls = throwable.getClass();
    synchronized (CAUSE_METHOD_NAMES_LOCK)
    {
      int i = 0;
      for (int isize = CAUSE_METHOD_NAMES.length; i < isize; i++) {
        try
        {
          Method method = cls.getMethod(CAUSE_METHOD_NAMES[i], null);
          if ((method != null) && (Throwable.class.isAssignableFrom(method.getReturnType()))) {
            return true;
          }
        }
        catch (NoSuchMethodException ignored) {}catch (SecurityException ignored) {}
      }
    }
    try
    {
      Field field = cls.getField("detail");
      if (field != null) {
        return true;
      }
    }
    catch (NoSuchFieldException ignored) {}catch (SecurityException ignored) {}
    return false;
  }
  
  public static int getThrowableCount(Throwable throwable)
  {
    return getThrowableList(throwable).size();
  }
  
  public static Throwable[] getThrowables(Throwable throwable)
  {
    List list = getThrowableList(throwable);
    return (Throwable[])list.toArray(new Throwable[list.size()]);
  }
  
  public static List getThrowableList(Throwable throwable)
  {
    List list = new ArrayList();
    while ((throwable != null) && (!list.contains(throwable)))
    {
      list.add(throwable);
      throwable = getCause(throwable);
    }
    return list;
  }
  
  public static int indexOfThrowable(Throwable throwable, Class clazz)
  {
    return indexOf(throwable, clazz, 0, false);
  }
  
  public static int indexOfThrowable(Throwable throwable, Class clazz, int fromIndex)
  {
    return indexOf(throwable, clazz, fromIndex, false);
  }
  
  public static int indexOfType(Throwable throwable, Class type)
  {
    return indexOf(throwable, type, 0, true);
  }
  
  public static int indexOfType(Throwable throwable, Class type, int fromIndex)
  {
    return indexOf(throwable, type, fromIndex, true);
  }
  
  private static int indexOf(Throwable throwable, Class type, int fromIndex, boolean subclass)
  {
    if ((throwable == null) || (type == null)) {
      return -1;
    }
    if (fromIndex < 0) {
      fromIndex = 0;
    }
    Throwable[] throwables = getThrowables(throwable);
    if (fromIndex >= throwables.length) {
      return -1;
    }
    if (subclass) {
      for (int i = fromIndex; i < throwables.length; i++) {
        if (type.isAssignableFrom(throwables[i].getClass())) {
          return i;
        }
      }
    } else {
      for (int i = fromIndex; i < throwables.length; i++) {
        if (type.equals(throwables[i].getClass())) {
          return i;
        }
      }
    }
    return -1;
  }
  
  public static void printRootCauseStackTrace(Throwable throwable)
  {
    printRootCauseStackTrace(throwable, System.err);
  }
  
  public static void printRootCauseStackTrace(Throwable throwable, PrintStream stream)
  {
    if (throwable == null) {
      return;
    }
    if (stream == null) {
      throw new IllegalArgumentException("The PrintStream must not be null");
    }
    String[] trace = getRootCauseStackTrace(throwable);
    for (int i = 0; i < trace.length; i++) {
      stream.println(trace[i]);
    }
    stream.flush();
  }
  
  public static void printRootCauseStackTrace(Throwable throwable, PrintWriter writer)
  {
    if (throwable == null) {
      return;
    }
    if (writer == null) {
      throw new IllegalArgumentException("The PrintWriter must not be null");
    }
    String[] trace = getRootCauseStackTrace(throwable);
    for (int i = 0; i < trace.length; i++) {
      writer.println(trace[i]);
    }
    writer.flush();
  }
  
  public static String[] getRootCauseStackTrace(Throwable throwable)
  {
    if (throwable == null) {
      return ArrayUtils.EMPTY_STRING_ARRAY;
    }
    Throwable[] throwables = getThrowables(throwable);
    int count = throwables.length;
    ArrayList frames = new ArrayList();
    List nextTrace = getStackFrameList(throwables[(count - 1)]);
    int i = count;
    for (;;)
    {
      i--;
      if (i < 0) {
        break;
      }
      List trace = nextTrace;
      if (i != 0)
      {
        nextTrace = getStackFrameList(throwables[(i - 1)]);
        removeCommonFrames(trace, nextTrace);
      }
      if (i == count - 1) {
        frames.add(throwables[i].toString());
      } else {
        frames.add(" [wrapped] " + throwables[i].toString());
      }
      for (int j = 0; j < trace.size(); j++) {
        frames.add(trace.get(j));
      }
    }
    return (String[])frames.toArray(new String[0]);
  }
  
  public static void removeCommonFrames(List causeFrames, List wrapperFrames)
  {
    if ((causeFrames == null) || (wrapperFrames == null)) {
      throw new IllegalArgumentException("The List must not be null");
    }
    int causeFrameIndex = causeFrames.size() - 1;
    int wrapperFrameIndex = wrapperFrames.size() - 1;
    while ((causeFrameIndex >= 0) && (wrapperFrameIndex >= 0))
    {
      String causeFrame = (String)causeFrames.get(causeFrameIndex);
      String wrapperFrame = (String)wrapperFrames.get(wrapperFrameIndex);
      if (causeFrame.equals(wrapperFrame)) {
        causeFrames.remove(causeFrameIndex);
      }
      causeFrameIndex--;
      wrapperFrameIndex--;
    }
  }
  
  public static String getFullStackTrace(Throwable throwable)
  {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw, true);
    Throwable[] ts = getThrowables(throwable);
    for (int i = 0; i < ts.length; i++)
    {
      ts[i].printStackTrace(pw);
      if (isNestedThrowable(ts[i])) {
        break;
      }
    }
    return sw.getBuffer().toString();
  }
  
  public static String getStackTrace(Throwable throwable)
  {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw, true);
    throwable.printStackTrace(pw);
    return sw.getBuffer().toString();
  }
  
  public static String[] getStackFrames(Throwable throwable)
  {
    if (throwable == null) {
      return ArrayUtils.EMPTY_STRING_ARRAY;
    }
    return getStackFrames(getStackTrace(throwable));
  }
  
  static String[] getStackFrames(String stackTrace)
  {
    String linebreak = SystemUtils.LINE_SEPARATOR;
    StringTokenizer frames = new StringTokenizer(stackTrace, linebreak);
    List list = new ArrayList();
    while (frames.hasMoreTokens()) {
      list.add(frames.nextToken());
    }
    return toArray(list);
  }
  
  static List getStackFrameList(Throwable t)
  {
    String stackTrace = getStackTrace(t);
    String linebreak = SystemUtils.LINE_SEPARATOR;
    StringTokenizer frames = new StringTokenizer(stackTrace, linebreak);
    List list = new ArrayList();
    boolean traceStarted = false;
    while (frames.hasMoreTokens())
    {
      String token = frames.nextToken();
      
      int at = token.indexOf("at");
      if ((at != -1) && (token.substring(0, at).trim().length() == 0))
      {
        traceStarted = true;
        list.add(token);
      }
      else
      {
        if (traceStarted) {
          break;
        }
      }
    }
    return list;
  }
  
  public static String getMessage(Throwable th)
  {
    if (th == null) {
      return "";
    }
    String clsName = ClassUtils.getShortClassName(th, null);
    String msg = th.getMessage();
    return clsName + ": " + StringUtils.defaultString(msg);
  }
  
  public static String getRootCauseMessage(Throwable th)
  {
    Throwable root = getRootCause(th);
    root = root == null ? th : root;
    return getMessage(root);
  }
  
  /* Error */
  private static ArrayList getCauseMethodNameList()
  {
    // Byte code:
    //   0: getstatic 47	org/apache/commons/lang/exception/ExceptionUtils:CAUSE_METHOD_NAMES_LOCK	Ljava/lang/Object;
    //   3: dup
    //   4: astore_0
    //   5: monitorenter
    //   6: new 124	java/util/ArrayList
    //   9: dup
    //   10: getstatic 53	org/apache/commons/lang/exception/ExceptionUtils:CAUSE_METHOD_NAMES	[Ljava/lang/String;
    //   13: invokestatic 130	java/util/Arrays:asList	([Ljava/lang/Object;)Ljava/util/List;
    //   16: invokespecial 133	java/util/ArrayList:<init>	(Ljava/util/Collection;)V
    //   19: aload_0
    //   20: monitorexit
    //   21: areturn
    //   22: astore_1
    //   23: aload_0
    //   24: monitorexit
    //   25: aload_1
    //   26: athrow
    // Line number table:
    //   Java source line #228	-> byte code offset #0
    //   Java source line #229	-> byte code offset #6
    //   Java source line #230	-> byte code offset #22
    // Local variable table:
    //   start	length	slot	name	signature
    //   4	20	0	Ljava/lang/Object;	Object
    //   22	4	1	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   6	21	22	finally
    //   22	25	22	finally
  }
  
  /* Error */
  public static Throwable getCause(Throwable throwable)
  {
    // Byte code:
    //   0: getstatic 47	org/apache/commons/lang/exception/ExceptionUtils:CAUSE_METHOD_NAMES_LOCK	Ljava/lang/Object;
    //   3: dup
    //   4: astore_1
    //   5: monitorenter
    //   6: aload_0
    //   7: getstatic 53	org/apache/commons/lang/exception/ExceptionUtils:CAUSE_METHOD_NAMES	[Ljava/lang/String;
    //   10: invokestatic 144	org/apache/commons/lang/exception/ExceptionUtils:getCause	(Ljava/lang/Throwable;[Ljava/lang/String;)Ljava/lang/Throwable;
    //   13: aload_1
    //   14: monitorexit
    //   15: areturn
    //   16: astore_2
    //   17: aload_1
    //   18: monitorexit
    //   19: aload_2
    //   20: athrow
    // Line number table:
    //   Java source line #281	-> byte code offset #0
    //   Java source line #282	-> byte code offset #6
    //   Java source line #283	-> byte code offset #16
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	21	0	throwable	Throwable
    //   4	14	1	Ljava/lang/Object;	Object
    //   16	4	2	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   6	15	16	finally
    //   16	19	16	finally
  }
}
