package com.avaje.ebeaninternal.server.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ClassPathSearchFilter
{
  private static final String COM_AVAJE_EBEANINTERNAL_SERVER_BEAN = "com.avaje.ebeaninternal.server.bean";
  private static final String COM_AVAJE_EBEAN_META = "com.avaje.ebean.meta";
  private boolean defaultPackageMatch = true;
  private boolean defaultJarMatch = false;
  private String ebeanJarPrefix = "ebean";
  private HashSet<String> includePackageSet = new HashSet();
  private HashSet<String> excludePackageSet = new HashSet();
  private HashSet<String> includeJarSet = new HashSet();
  private HashSet<String> excludeJarSet = new HashSet();
  
  public ClassPathSearchFilter()
  {
    addDefaultExcludePackages();
  }
  
  public void setEbeanJarPrefix(String ebeanJarPrefix)
  {
    this.ebeanJarPrefix = ebeanJarPrefix;
  }
  
  public Set<String> getIncludePackages()
  {
    return this.includePackageSet;
  }
  
  public void addDefaultExcludePackages()
  {
    excludePackage("sun");
    excludePackage("com.sun");
    excludePackage("java");
    excludePackage("javax");
    excludePackage("junit");
    excludePackage("org.w3c");
    excludePackage("org.xml");
    excludePackage("org.apache");
    excludePackage("com.mysql");
    excludePackage("oracle.jdbc");
    excludePackage("com.microsoft.sqlserver");
    excludePackage("com.avaje.ebean");
    excludePackage("com.avaje.lib");
  }
  
  public void clearExcludePackages()
  {
    this.excludePackageSet.clear();
  }
  
  public void setDefaultJarMatch(boolean defaultJarMatch)
  {
    this.defaultJarMatch = defaultJarMatch;
  }
  
  public void setDefaultPackageMatch(boolean defaultPackageMatch)
  {
    this.defaultPackageMatch = defaultPackageMatch;
  }
  
  public void includePackage(String pckgName)
  {
    this.includePackageSet.add(pckgName);
  }
  
  public void excludePackage(String pckgName)
  {
    this.excludePackageSet.add(pckgName);
  }
  
  public void excludeJar(String jarName)
  {
    this.includeJarSet.add(jarName);
  }
  
  public void includeJar(String jarName)
  {
    this.includeJarSet.add(jarName);
  }
  
  public boolean isSearchPackage(String packageName)
  {
    if ("com.avaje.ebean.meta".equals(packageName)) {
      return true;
    }
    if ("com.avaje.ebeaninternal.server.bean".equals(packageName)) {
      return true;
    }
    if ((this.includePackageSet != null) && (!this.includePackageSet.isEmpty())) {
      return containedIn(this.includePackageSet, packageName);
    }
    if (containedIn(this.excludePackageSet, packageName)) {
      return false;
    }
    return this.defaultPackageMatch;
  }
  
  public boolean isSearchJar(String jarName)
  {
    if (jarName.startsWith(this.ebeanJarPrefix)) {
      return true;
    }
    if (containedIn(this.includeJarSet, jarName)) {
      return true;
    }
    if (containedIn(this.excludeJarSet, jarName)) {
      return false;
    }
    return this.defaultJarMatch;
  }
  
  protected boolean containedIn(HashSet<String> set, String match)
  {
    if (set.contains(match)) {
      return true;
    }
    Iterator<String> incIt = set.iterator();
    while (incIt.hasNext())
    {
      String val = (String)incIt.next();
      if (match.startsWith(val)) {
        return true;
      }
    }
    return false;
  }
}
