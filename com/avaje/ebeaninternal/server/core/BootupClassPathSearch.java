package com.avaje.ebeaninternal.server.core;

import com.avaje.ebeaninternal.server.util.ClassPathSearch;
import com.avaje.ebeaninternal.server.util.ClassPathSearchFilter;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class BootupClassPathSearch
{
  private static final Logger logger = Logger.getLogger(BootupClassPathSearch.class.getName());
  private final Object monitor = new Object();
  private final ClassLoader classLoader;
  private final List<String> packages;
  private final List<String> jars;
  private BootupClasses bootupClasses;
  
  public BootupClassPathSearch(ClassLoader classLoader, List<String> packages, List<String> jars)
  {
    this.classLoader = (classLoader == null ? getClass().getClassLoader() : classLoader);
    this.packages = packages;
    this.jars = jars;
  }
  
  public BootupClasses getBootupClasses()
  {
    synchronized (this.monitor)
    {
      if (this.bootupClasses == null) {
        this.bootupClasses = search();
      }
      return this.bootupClasses;
    }
  }
  
  private BootupClasses search()
  {
    synchronized (this.monitor)
    {
      try
      {
        BootupClasses bc = new BootupClasses();
        
        long st = System.currentTimeMillis();
        
        ClassPathSearchFilter filter = createFilter();
        
        ClassPathSearch finder = new ClassPathSearch(this.classLoader, filter, bc);
        
        finder.findClasses();
        Set<String> jars = finder.getJarHits();
        Set<String> pkgs = finder.getPackageHits();
        
        long searchTime = System.currentTimeMillis() - st;
        
        String msg = "Classpath search hits in jars" + jars + " pkgs" + pkgs + "  searchTime[" + searchTime + "]";
        logger.info(msg);
        
        return bc;
      }
      catch (Exception ex)
      {
        String msg = "Error in classpath search (looking for entities etc)";
        throw new RuntimeException(msg, ex);
      }
    }
  }
  
  private ClassPathSearchFilter createFilter()
  {
    ClassPathSearchFilter filter = new ClassPathSearchFilter();
    filter.addDefaultExcludePackages();
    if (this.packages != null) {
      for (String packageName : this.packages) {
        filter.includePackage(packageName);
      }
    }
    if (this.jars != null) {
      for (String jarName : this.jars) {
        filter.includeJar(jarName);
      }
    }
    return filter;
  }
}
