package org.apache.logging.log4j.core.config.plugins;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.helpers.Charsets;
import org.apache.logging.log4j.core.helpers.Loader;
import org.apache.logging.log4j.status.StatusLogger;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.wiring.BundleWiring;

public class ResolverUtil
{
  private static final Logger LOGGER = ;
  private static final String VFSZIP = "vfszip";
  private static final String BUNDLE_RESOURCE = "bundleresource";
  private final Set<Class<?>> classMatches;
  private final Set<URI> resourceMatches;
  private ClassLoader classloader;
  
  public ResolverUtil()
  {
    this.classMatches = new HashSet();
    
    this.resourceMatches = new HashSet();
  }
  
  public Set<Class<?>> getClasses()
  {
    return this.classMatches;
  }
  
  public Set<URI> getResources()
  {
    return this.resourceMatches;
  }
  
  public ClassLoader getClassLoader()
  {
    return this.classloader != null ? this.classloader : (this.classloader = Loader.getClassLoader(ResolverUtil.class, null));
  }
  
  public void setClassLoader(ClassLoader classloader)
  {
    this.classloader = classloader;
  }
  
  public void findImplementations(Class<?> parent, String... packageNames)
  {
    if (packageNames == null) {
      return;
    }
    Test test = new IsA(parent);
    for (String pkg : packageNames) {
      findInPackage(test, pkg);
    }
  }
  
  public void findSuffix(String suffix, String... packageNames)
  {
    if (packageNames == null) {
      return;
    }
    Test test = new NameEndsWith(suffix);
    for (String pkg : packageNames) {
      findInPackage(test, pkg);
    }
  }
  
  public void findAnnotated(Class<? extends Annotation> annotation, String... packageNames)
  {
    if (packageNames == null) {
      return;
    }
    Test test = new AnnotatedWith(annotation);
    for (String pkg : packageNames) {
      findInPackage(test, pkg);
    }
  }
  
  public void findNamedResource(String name, String... pathNames)
  {
    if (pathNames == null) {
      return;
    }
    Test test = new NameIs(name);
    for (String pkg : pathNames) {
      findInPackage(test, pkg);
    }
  }
  
  public void find(Test test, String... packageNames)
  {
    if (packageNames == null) {
      return;
    }
    for (String pkg : packageNames) {
      findInPackage(test, pkg);
    }
  }
  
  public void findInPackage(Test test, String packageName)
  {
    packageName = packageName.replace('.', '/');
    ClassLoader loader = getClassLoader();
    Enumeration<URL> urls;
    try
    {
      urls = loader.getResources(packageName);
    }
    catch (IOException ioe)
    {
      LOGGER.warn("Could not read package: " + packageName, ioe);
      return;
    }
    while (urls.hasMoreElements()) {
      try
      {
        URL url = (URL)urls.nextElement();
        String urlPath = url.getFile();
        urlPath = URLDecoder.decode(urlPath, Charsets.UTF_8.name());
        if (urlPath.startsWith("file:")) {
          urlPath = urlPath.substring(5);
        }
        if (urlPath.indexOf('!') > 0) {
          urlPath = urlPath.substring(0, urlPath.indexOf('!'));
        }
        LOGGER.info("Scanning for classes in [" + urlPath + "] matching criteria: " + test);
        if ("vfszip".equals(url.getProtocol()))
        {
          String path = urlPath.substring(0, urlPath.length() - packageName.length() - 2);
          URL newURL = new URL(url.getProtocol(), url.getHost(), path);
          
          JarInputStream stream = new JarInputStream(newURL.openStream());
          try
          {
            loadImplementationsInJar(test, packageName, path, stream);
          }
          finally
          {
            close(stream, newURL);
          }
        }
        else if ("bundleresource".equals(url.getProtocol()))
        {
          loadImplementationsInBundle(test, packageName);
        }
        else
        {
          File file = new File(urlPath);
          if (file.isDirectory()) {
            loadImplementationsInDirectory(test, packageName, file);
          } else {
            loadImplementationsInJar(test, packageName, file);
          }
        }
      }
      catch (IOException ioe)
      {
        LOGGER.warn("could not read entries", ioe);
      }
    }
  }
  
  private void loadImplementationsInBundle(Test test, String packageName)
  {
    BundleWiring wiring = (BundleWiring)FrameworkUtil.getBundle(ResolverUtil.class).adapt(BundleWiring.class);
    
    Collection<String> list = wiring.listResources(packageName, "*.class", 1);
    for (String name : list) {
      addIfMatching(test, name);
    }
  }
  
  private void loadImplementationsInDirectory(Test test, String parent, File location)
  {
    File[] files = location.listFiles();
    if (files == null) {
      return;
    }
    for (File file : files)
    {
      StringBuilder builder = new StringBuilder();
      builder.append(parent).append("/").append(file.getName());
      String packageOrClass = parent == null ? file.getName() : builder.toString();
      if (file.isDirectory()) {
        loadImplementationsInDirectory(test, packageOrClass, file);
      } else if (isTestApplicable(test, file.getName())) {
        addIfMatching(test, packageOrClass);
      }
    }
  }
  
  private boolean isTestApplicable(Test test, String path)
  {
    return (test.doesMatchResource()) || ((path.endsWith(".class")) && (test.doesMatchClass()));
  }
  
  private void loadImplementationsInJar(Test test, String parent, File jarFile)
  {
    JarInputStream jarStream = null;
    try
    {
      jarStream = new JarInputStream(new FileInputStream(jarFile));
      loadImplementationsInJar(test, parent, jarFile.getPath(), jarStream);
    }
    catch (FileNotFoundException ex)
    {
      LOGGER.error("Could not search jar file '" + jarFile + "' for classes matching criteria: " + test + " file not found");
    }
    catch (IOException ioe)
    {
      LOGGER.error("Could not search jar file '" + jarFile + "' for classes matching criteria: " + test + " due to an IOException", ioe);
    }
    finally
    {
      close(jarStream, jarFile);
    }
  }
  
  private void close(JarInputStream jarStream, Object source)
  {
    if (jarStream != null) {
      try
      {
        jarStream.close();
      }
      catch (IOException e)
      {
        LOGGER.error("Error closing JAR file stream for {}", new Object[] { source, e });
      }
    }
  }
  
  private void loadImplementationsInJar(Test test, String parent, String path, JarInputStream stream)
  {
    try
    {
      JarEntry entry;
      while ((entry = stream.getNextJarEntry()) != null)
      {
        String name = entry.getName();
        if ((!entry.isDirectory()) && (name.startsWith(parent)) && (isTestApplicable(test, name))) {
          addIfMatching(test, name);
        }
      }
    }
    catch (IOException ioe)
    {
      LOGGER.error("Could not search jar file '" + path + "' for classes matching criteria: " + test + " due to an IOException", ioe);
    }
  }
  
  protected void addIfMatching(Test test, String fqn)
  {
    try
    {
      ClassLoader loader = getClassLoader();
      if (test.doesMatchClass())
      {
        String externalName = fqn.substring(0, fqn.indexOf('.')).replace('/', '.');
        if (LOGGER.isDebugEnabled()) {
          LOGGER.debug("Checking to see if class " + externalName + " matches criteria [" + test + "]");
        }
        Class<?> type = loader.loadClass(externalName);
        if (test.matches(type)) {
          this.classMatches.add(type);
        }
      }
      if (test.doesMatchResource())
      {
        URL url = loader.getResource(fqn);
        if (url == null) {
          url = loader.getResource(fqn.substring(1));
        }
        if ((url != null) && (test.matches(url.toURI()))) {
          this.resourceMatches.add(url.toURI());
        }
      }
    }
    catch (Throwable t)
    {
      LOGGER.warn("Could not examine class '" + fqn + "' due to a " + t.getClass().getName() + " with message: " + t.getMessage());
    }
  }
  
  public static abstract interface Test
  {
    public abstract boolean matches(Class<?> paramClass);
    
    public abstract boolean matches(URI paramURI);
    
    public abstract boolean doesMatchClass();
    
    public abstract boolean doesMatchResource();
  }
  
  public static abstract class ClassTest
    implements ResolverUtil.Test
  {
    public boolean matches(URI resource)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean doesMatchClass()
    {
      return true;
    }
    
    public boolean doesMatchResource()
    {
      return false;
    }
  }
  
  public static abstract class ResourceTest
    implements ResolverUtil.Test
  {
    public boolean matches(Class<?> cls)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean doesMatchClass()
    {
      return false;
    }
    
    public boolean doesMatchResource()
    {
      return true;
    }
  }
  
  public static class IsA
    extends ResolverUtil.ClassTest
  {
    private final Class<?> parent;
    
    public IsA(Class<?> parentType)
    {
      this.parent = parentType;
    }
    
    public boolean matches(Class<?> type)
    {
      return (type != null) && (this.parent.isAssignableFrom(type));
    }
    
    public String toString()
    {
      return "is assignable to " + this.parent.getSimpleName();
    }
  }
  
  public static class NameEndsWith
    extends ResolverUtil.ClassTest
  {
    private final String suffix;
    
    public NameEndsWith(String suffix)
    {
      this.suffix = suffix;
    }
    
    public boolean matches(Class<?> type)
    {
      return (type != null) && (type.getName().endsWith(this.suffix));
    }
    
    public String toString()
    {
      return "ends with the suffix " + this.suffix;
    }
  }
  
  public static class AnnotatedWith
    extends ResolverUtil.ClassTest
  {
    private final Class<? extends Annotation> annotation;
    
    public AnnotatedWith(Class<? extends Annotation> annotation)
    {
      this.annotation = annotation;
    }
    
    public boolean matches(Class<?> type)
    {
      return (type != null) && (type.isAnnotationPresent(this.annotation));
    }
    
    public String toString()
    {
      return "annotated with @" + this.annotation.getSimpleName();
    }
  }
  
  public static class NameIs
    extends ResolverUtil.ResourceTest
  {
    private final String name;
    
    public NameIs(String name)
    {
      this.name = ("/" + name);
    }
    
    public boolean matches(URI resource)
    {
      return resource.getPath().endsWith(this.name);
    }
    
    public String toString()
    {
      return "named " + this.name;
    }
  }
}
