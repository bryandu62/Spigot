package com.avaje.ebeaninternal.server.core;

import com.avaje.ebean.config.GlobalProperties;
import com.avaje.ebeaninternal.api.ClassUtil;
import com.avaje.ebeaninternal.server.lib.util.Dnode;
import com.avaje.ebeaninternal.server.lib.util.DnodeReader;
import com.avaje.ebeaninternal.server.util.ClassPathReader;
import com.avaje.ebeaninternal.server.util.DefaultClassPathReader;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;

public class XmlConfigLoader
{
  private static final Logger logger = Logger.getLogger(XmlConfigLoader.class.getName());
  private final ClassPathReader classPathReader;
  private final Object[] classPaths;
  
  public XmlConfigLoader(ClassLoader classLoader)
  {
    if (classLoader == null) {
      classLoader = getClass().getClassLoader();
    }
    String cn = GlobalProperties.get("ebean.classpathreader", null);
    if (cn != null)
    {
      logger.info("Using [" + cn + "] to read the searchable class path");
      this.classPathReader = ((ClassPathReader)ClassUtil.newInstance(cn, getClass()));
    }
    else
    {
      this.classPathReader = new DefaultClassPathReader();
    }
    this.classPaths = this.classPathReader.readPath(classLoader);
  }
  
  public XmlConfig load()
  {
    List<Dnode> ormXml = search("META-INF/orm.xml");
    List<Dnode> ebeanOrmXml = search("META-INF/ebean-orm.xml");
    
    return new XmlConfig(ormXml, ebeanOrmXml);
  }
  
  public List<Dnode> search(String searchFor)
  {
    ArrayList<Dnode> xmlList = new ArrayList();
    
    String charsetName = Charset.defaultCharset().name();
    for (int h = 0; h < this.classPaths.length; h++) {
      try
      {
        File classPath;
        if (URL.class.isInstance(this.classPaths[h])) {
          classPath = new File(((URL)this.classPaths[h]).getFile());
        } else {
          classPath = new File(this.classPaths[h].toString());
        }
        String path = URLDecoder.decode(classPath.getAbsolutePath(), charsetName);
        
        File classPath = new File(path);
        if (classPath.isDirectory())
        {
          checkDir(searchFor, xmlList, classPath);
        }
        else if (classPath.getName().endsWith(".jar"))
        {
          checkJar(searchFor, xmlList, classPath);
        }
        else
        {
          String msg = "Not a Jar or Directory? " + classPath.getAbsolutePath();
          logger.log(Level.SEVERE, msg);
        }
      }
      catch (UnsupportedEncodingException e)
      {
        throw new RuntimeException(e);
      }
      catch (IOException e)
      {
        throw new RuntimeException(e);
      }
    }
    return xmlList;
  }
  
  private void processInputStream(ArrayList<Dnode> xmlList, InputStream is)
    throws IOException
  {
    DnodeReader reader = new DnodeReader();
    Dnode xmlDoc = reader.parseXml(is);
    is.close();
    
    xmlList.add(xmlDoc);
  }
  
  private void checkFile(String searchFor, ArrayList<Dnode> xmlList, File dir)
    throws IOException
  {
    File f = new File(dir, searchFor);
    if (f.exists())
    {
      FileInputStream fis = new FileInputStream(f);
      BufferedInputStream is = new BufferedInputStream(fis);
      processInputStream(xmlList, is);
    }
  }
  
  private void checkDir(String searchFor, ArrayList<Dnode> xmlList, File dir)
    throws IOException
  {
    checkFile(searchFor, xmlList, dir);
    if (dir.getPath().endsWith("classes"))
    {
      File parent = dir.getParentFile();
      if ((parent != null) && (parent.getPath().endsWith("WEB-INF")))
      {
        parent = parent.getParentFile();
        if (parent != null)
        {
          File metaInf = new File(parent, "META-INF");
          if (metaInf.exists()) {
            checkFile(searchFor, xmlList, metaInf);
          }
        }
      }
    }
  }
  
  private void checkJar(String searchFor, ArrayList<Dnode> xmlList, File classPath)
    throws IOException
  {
    String fileName = classPath.getName();
    if (fileName.toLowerCase().startsWith("surefire")) {
      return;
    }
    JarFile module = null;
    try
    {
      module = new JarFile(classPath);
      ZipEntry entry = module.getEntry(searchFor);
      if (entry != null)
      {
        InputStream is = module.getInputStream(entry);
        processInputStream(xmlList, is);
      }
    }
    catch (Exception e)
    {
      logger.info("Unable to check jar file " + fileName + " for ebean-orm.xml");
    }
    finally
    {
      if (module != null) {
        module.close();
      }
    }
  }
}
