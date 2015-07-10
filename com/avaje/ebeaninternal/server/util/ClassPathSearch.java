package com.avaje.ebeaninternal.server.util;

import com.avaje.ebean.config.GlobalProperties;
import com.avaje.ebeaninternal.api.ClassUtil;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClassPathSearch
{
  private static final Logger logger = Logger.getLogger(ClassPathSearch.class.getName());
  ClassLoader classLoader;
  Object[] classPaths;
  ClassPathSearchFilter filter;
  ClassPathSearchMatcher matcher;
  ArrayList<Class<?>> matchList = new ArrayList();
  HashSet<String> jarHits = new HashSet();
  HashSet<String> packageHits = new HashSet();
  ClassPathReader classPathReader = new DefaultClassPathReader();
  
  public ClassPathSearch(ClassLoader classLoader, ClassPathSearchFilter filter, ClassPathSearchMatcher matcher)
  {
    this.classLoader = classLoader;
    this.filter = filter;
    this.matcher = matcher;
    initClassPaths();
  }
  
  private void initClassPaths()
  {
    try
    {
      String cn = GlobalProperties.get("ebean.classpathreader", null);
      if (cn != null)
      {
        logger.info("Using [" + cn + "] to read the searchable class path");
        this.classPathReader = ((ClassPathReader)ClassUtil.newInstance(cn, getClass()));
      }
      this.classPaths = this.classPathReader.readPath(this.classLoader);
      if ((this.classPaths == null) || (this.classPaths.length == 0))
      {
        String msg = "ClassPath is EMPTY using ClassPathReader [" + this.classPathReader + "]";
        logger.warning(msg);
      }
      boolean debug = GlobalProperties.getBoolean("ebean.debug.classpath", false);
      if ((debug) || (logger.isLoggable(Level.FINER)))
      {
        String msg = "Classpath " + Arrays.toString(this.classPaths);
        logger.info(msg);
      }
    }
    catch (Exception e)
    {
      String msg = "Error trying to read the classpath entries";
      throw new RuntimeException(msg, e);
    }
  }
  
  public Set<String> getJarHits()
  {
    return this.jarHits;
  }
  
  public Set<String> getPackageHits()
  {
    return this.packageHits;
  }
  
  private void registerHit(String jarFileName, Class<?> cls)
  {
    if (jarFileName != null) {
      this.jarHits.add(jarFileName);
    }
    Package pkg = cls.getPackage();
    if (pkg != null) {
      this.packageHits.add(pkg.getName());
    } else {
      this.packageHits.add("");
    }
  }
  
  public List<Class<?>> findClasses()
    throws ClassNotFoundException
  {
    if ((this.classPaths == null) || (this.classPaths.length == 0)) {
      return this.matchList;
    }
    String charsetName = Charset.defaultCharset().name();
    for (int h = 0; h < this.classPaths.length; h++)
    {
      String jarFileName = null;
      Enumeration<?> files = null;
      JarFile module = null;
      File classPath;
      File classPath;
      if (URL.class.isInstance(this.classPaths[h])) {
        classPath = new File(((URL)this.classPaths[h]).getFile());
      } else {
        classPath = new File(this.classPaths[h].toString());
      }
      try
      {
        String path = URLDecoder.decode(classPath.getAbsolutePath(), charsetName);
        classPath = new File(path);
      }
      catch (UnsupportedEncodingException e)
      {
        throw new RuntimeException(e);
      }
      if (classPath.isDirectory())
      {
        files = getDirectoryEnumeration(classPath);
      }
      else if (classPath.getName().endsWith(".jar"))
      {
        jarFileName = classPath.getName();
        if (!this.filter.isSearchJar(jarFileName)) {
          continue;
        }
        try
        {
          module = new JarFile(classPath);
          files = module.entries();
        }
        catch (MalformedURLException ex)
        {
          throw new ClassNotFoundException("Bad classpath. Error: ", ex);
        }
        catch (IOException ex)
        {
          String msg = "jar file '" + classPath.getAbsolutePath() + "' could not be instantiate from file path. Error: ";
          
          throw new ClassNotFoundException(msg, ex);
        }
      }
      else
      {
        String msg = "Error: expected classPath entry [" + classPath.getAbsolutePath() + "] to be a directory or a .jar file but it is not either of those?";
        
        logger.log(Level.SEVERE, msg);
      }
      searchFiles(files, jarFileName);
      if (module != null) {
        try
        {
          module.close();
        }
        catch (IOException e)
        {
          String msg = "Error closing jar";
          throw new ClassNotFoundException(msg, e);
        }
      }
    }
    if (this.matchList.isEmpty())
    {
      String msg = "No Entities found in ClassPath using ClassPathReader [" + this.classPathReader + "] Classpath Searched[" + Arrays.toString(this.classPaths) + "]";
      
      logger.warning(msg);
    }
    return this.matchList;
  }
  
  private Enumeration<?> getDirectoryEnumeration(File classPath)
  {
    ArrayList<String> fileNameList = new ArrayList();
    
    Set<String> includePkgs = this.filter.getIncludePackages();
    if (includePkgs.size() > 0)
    {
      Iterator<String> it = includePkgs.iterator();
      while (it.hasNext())
      {
        String pkg = (String)it.next();
        String relPath = pkg.replace('.', '/');
        File dir = new File(classPath, relPath);
        if (dir.exists()) {
          recursivelyListDir(fileNameList, dir, new StringBuilder(relPath));
        }
      }
    }
    else
    {
      recursivelyListDir(fileNameList, classPath, new StringBuilder());
    }
    return Collections.enumeration(fileNameList);
  }
  
  private void searchFiles(Enumeration<?> files, String jarFileName)
  {
    while ((files != null) && (files.hasMoreElements()))
    {
      String fileName = files.nextElement().toString();
      if (fileName.endsWith(".class"))
      {
        String className = fileName.replace('/', '.').substring(0, fileName.length() - 6);
        int lastPeriod = className.lastIndexOf(".");
        String pckgName;
        String pckgName;
        if (lastPeriod > 0) {
          pckgName = className.substring(0, lastPeriod);
        } else {
          pckgName = "";
        }
        if (this.filter.isSearchPackage(pckgName))
        {
          Class<?> theClass = null;
          try
          {
            theClass = Class.forName(className, false, this.classLoader);
            if (this.matcher.isMatch(theClass))
            {
              this.matchList.add(theClass);
              registerHit(jarFileName, theClass);
            }
          }
          catch (ClassNotFoundException e)
          {
            logger.finer("Error searching classpath" + e.getMessage());
            continue;
          }
          catch (NoClassDefFoundError e)
          {
            logger.finer("Error searching classpath: " + e.getMessage());
          }
        }
      }
    }
  }
  
  private void recursivelyListDir(List<String> fileNameList, File dir, StringBuilder relativePath)
  {
    if (dir.isDirectory())
    {
      File[] files = dir.listFiles();
      for (int i = 0; i < files.length; i++)
      {
        int prevLen = relativePath.length();
        relativePath.append(prevLen == 0 ? "" : "/").append(files[i].getName());
        
        recursivelyListDir(fileNameList, files[i], relativePath);
        
        relativePath.delete(prevLen, relativePath.length());
      }
    }
    else
    {
      fileNameList.add(relativePath.toString());
    }
  }
}
