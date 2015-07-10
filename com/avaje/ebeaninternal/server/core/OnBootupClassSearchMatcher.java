package com.avaje.ebeaninternal.server.core;

import com.avaje.ebeaninternal.server.util.ClassPathSearchMatcher;

public class OnBootupClassSearchMatcher
  implements ClassPathSearchMatcher
{
  BootupClasses classes = new BootupClasses();
  
  public boolean isMatch(Class<?> cls)
  {
    return this.classes.isMatch(cls);
  }
  
  public BootupClasses getOnBootupClasses()
  {
    return this.classes;
  }
}
