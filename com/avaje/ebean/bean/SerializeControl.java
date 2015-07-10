package com.avaje.ebean.bean;

public class SerializeControl
{
  private static final String BEANS = "com.avaje.ebean.vanillabeans";
  private static final String COLLECTIONS = "com.avaje.ebean.vanillacollections";
  
  private static Boolean getDefault(String key, Boolean dflt)
  {
    String val = System.getProperty(key);
    if (val != null) {
      return Boolean.valueOf(val.equalsIgnoreCase("true"));
    }
    return dflt;
  }
  
  private static ThreadLocal<Boolean> vanillaBeans = new ThreadLocal()
  {
    protected synchronized Boolean initialValue()
    {
      return SerializeControl.getDefault("com.avaje.ebean.vanillabeans", Boolean.TRUE);
    }
  };
  private static ThreadLocal<Boolean> vanillaCollections = new ThreadLocal()
  {
    protected synchronized Boolean initialValue()
    {
      return SerializeControl.getDefault("com.avaje.ebean.vanillacollections", Boolean.TRUE);
    }
  };
  
  public static void setDefaultForBeans(boolean vanillaOn)
  {
    Boolean b = Boolean.valueOf(vanillaOn);
    System.setProperty("com.avaje.ebean.vanillabeans", b.toString());
  }
  
  public static void setDefaultForCollections(boolean vanillaOn)
  {
    Boolean b = Boolean.valueOf(vanillaOn);
    System.setProperty("com.avaje.ebean.vanillacollections", b.toString());
  }
  
  public static void resetToDefault()
  {
    Boolean beans = getDefault("com.avaje.ebean.vanillabeans", Boolean.FALSE);
    setVanillaBeans(beans.booleanValue());
    
    Boolean coll = getDefault("com.avaje.ebean.vanillacollections", Boolean.FALSE);
    setVanillaCollections(coll.booleanValue());
  }
  
  public static void setVanilla(boolean vanillaOn)
  {
    if (vanillaOn)
    {
      vanillaBeans.set(Boolean.TRUE);
      vanillaCollections.set(Boolean.TRUE);
    }
    else
    {
      vanillaBeans.set(Boolean.FALSE);
      vanillaCollections.set(Boolean.FALSE);
    }
  }
  
  public static boolean isVanillaBeans()
  {
    return ((Boolean)vanillaBeans.get()).booleanValue();
  }
  
  public static void setVanillaBeans(boolean vanillaOn)
  {
    vanillaBeans.set(Boolean.valueOf(vanillaOn));
  }
  
  public static boolean isVanillaCollections()
  {
    return ((Boolean)vanillaCollections.get()).booleanValue();
  }
  
  public static void setVanillaCollections(boolean vanillaOn)
  {
    vanillaCollections.set(Boolean.valueOf(vanillaOn));
  }
}
