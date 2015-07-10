package com.avaje.ebean.enhance.agent;

import java.util.HashMap;

public class IgnoreClassHelper
{
  private final String[] processPackages;
  
  public IgnoreClassHelper(String agentArgs)
  {
    HashMap<String, String> args = ArgParser.parse(agentArgs);
    String packages = (String)args.get("packages");
    if (packages != null)
    {
      String[] pkgs = packages.split(",");
      this.processPackages = new String[pkgs.length];
      for (int i = 0; i < pkgs.length; i++) {
        this.processPackages[i] = convertPackage(pkgs[i]);
      }
    }
    else
    {
      this.processPackages = new String[0];
    }
  }
  
  private String convertPackage(String pkg)
  {
    pkg = pkg.trim().replace('.', '/');
    if (pkg.endsWith("*")) {
      return pkg.substring(0, pkg.length() - 1);
    }
    if (pkg.endsWith("/")) {
      return pkg;
    }
    return pkg + "/";
  }
  
  private boolean specificMatching(String className)
  {
    for (int i = 0; i < this.processPackages.length; i++) {
      if (className.startsWith(this.processPackages[i])) {
        return false;
      }
    }
    return true;
  }
  
  public boolean isIgnoreClass(String className)
  {
    className = className.replace('.', '/');
    if (className.startsWith("com/avaje/ebean/meta/")) {
      return false;
    }
    if (this.processPackages.length > 0) {
      return specificMatching(className);
    }
    if (className.startsWith("com/avaje/ebean")) {
      return true;
    }
    if ((className.startsWith("java/")) || (className.startsWith("javax/"))) {
      return true;
    }
    if ((className.startsWith("sun/")) || (className.startsWith("sunw/")) || (className.startsWith("com/sun/"))) {
      return true;
    }
    if ((className.startsWith("org/wc3/")) || (className.startsWith("org/xml/"))) {
      return true;
    }
    if ((className.startsWith("org/junit/")) || (className.startsWith("junit/"))) {
      return true;
    }
    if (className.startsWith("org/apache/")) {
      return true;
    }
    if (className.startsWith("org/eclipse/")) {
      return true;
    }
    if (className.startsWith("org/joda/")) {
      return true;
    }
    if (className.startsWith("com/mysql/jdbc")) {
      return true;
    }
    if (className.startsWith("org/postgresql/")) {
      return true;
    }
    if (className.startsWith("org/h2/")) {
      return true;
    }
    if (className.startsWith("oracle/")) {
      return true;
    }
    if (className.startsWith("groovy/")) {
      return true;
    }
    if (className.startsWith("$")) {
      return true;
    }
    return false;
  }
}
