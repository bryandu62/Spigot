package com.avaje.ebeaninternal.server.deploy;

public class ScalaSetConverter
  implements CollectionTypeConverter
{
  public Object toUnderlying(Object wrapped)
  {
    throw new IllegalArgumentException("Scala types not supported in this build");
  }
  
  public Object toWrapped(Object wrapped)
  {
    throw new IllegalArgumentException("Scala types not supported in this build");
  }
}
