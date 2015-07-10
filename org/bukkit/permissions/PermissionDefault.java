package org.bukkit.permissions;

import java.util.HashMap;
import java.util.Map;

public enum PermissionDefault
{
  TRUE(new String[] { "true" }),  FALSE(new String[] { "false" }),  OP(new String[] { "op", "isop", "operator", "isoperator", "admin", "isadmin" }),  NOT_OP(new String[] { "!op", "notop", "!operator", "notoperator", "!admin", "notadmin" });
  
  private final String[] names;
  private static final Map<String, PermissionDefault> lookup;
  
  private PermissionDefault(String... names)
  {
    this.names = names;
  }
  
  public boolean getValue(boolean op)
  {
    switch (this)
    {
    case FALSE: 
      return true;
    case NOT_OP: 
      return false;
    case OP: 
      return op;
    case TRUE: 
      return !op;
    }
    return false;
  }
  
  public static PermissionDefault getByName(String name)
  {
    return (PermissionDefault)lookup.get(name.toLowerCase().replaceAll("[^a-z!]", ""));
  }
  
  public String toString()
  {
    return this.names[0];
  }
  
  static
  {
    lookup = new HashMap();
    PermissionDefault[] arrayOfPermissionDefault;
    int i = (arrayOfPermissionDefault = values()).length;
    for (int j = 0; j < i; j++)
    {
      PermissionDefault value = arrayOfPermissionDefault[j];
      String[] arrayOfString;
      int k = (arrayOfString = value.names).length;
      for (int m = 0; m < k; m++)
      {
        String name = arrayOfString[m];
        lookup.put(name, value);
      }
    }
  }
}
