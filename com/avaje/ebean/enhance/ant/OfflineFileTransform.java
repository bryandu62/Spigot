package com.avaje.ebean.enhance.ant;

import com.avaje.ebean.enhance.agent.InputStreamTransform;
import com.avaje.ebean.enhance.agent.Transformer;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.instrument.IllegalClassFormatException;

public class OfflineFileTransform
{
  final InputStreamTransform inputStreamTransform;
  final String inDir;
  final String outDir;
  private TransformationListener listener;
  
  public OfflineFileTransform(Transformer transformer, ClassLoader classLoader, String inDir, String outDir)
  {
    this.inputStreamTransform = new InputStreamTransform(transformer, classLoader);
    inDir = trimSlash(inDir);
    this.inDir = inDir;
    this.outDir = (outDir == null ? inDir : outDir);
  }
  
  public void setListener(TransformationListener v)
  {
    this.listener = v;
  }
  
  private String trimSlash(String dir)
  {
    if (dir.endsWith("/")) {
      return dir.substring(0, dir.length() - 1);
    }
    return dir;
  }
  
  public void process(String packageNames)
  {
    if (packageNames == null)
    {
      processPackage("", true);
      return;
    }
    String[] pkgs = packageNames.split(",");
    for (int i = 0; i < pkgs.length; i++)
    {
      String pkg = pkgs[i].trim().replace('.', '/');
      
      boolean recurse = false;
      if (pkg.endsWith("**"))
      {
        recurse = true;
        pkg = pkg.substring(0, pkg.length() - 2);
      }
      else if (pkg.endsWith("*"))
      {
        recurse = true;
        pkg = pkg.substring(0, pkg.length() - 1);
      }
      pkg = trimSlash(pkg);
      
      processPackage(pkg, recurse);
    }
  }
  
  private void processPackage(String dir, boolean recurse)
  {
    this.inputStreamTransform.log(1, "transform> pkg: " + dir);
    
    String dirPath = this.inDir + "/" + dir;
    File d = new File(dirPath);
    if (!d.exists())
    {
      String m = "File not found " + dirPath;
      throw new RuntimeException(m);
    }
    File[] files = d.listFiles();
    
    File file = null;
    try
    {
      for (int i = 0; i < files.length; i++)
      {
        file = files[i];
        if (file.isDirectory())
        {
          if (recurse)
          {
            String subdir = dir + "/" + file.getName();
            processPackage(subdir, recurse);
          }
        }
        else
        {
          String fileName = file.getName();
          if (fileName.endsWith(".java")) {
            System.err.println("Expecting a .class file but got " + fileName + " ... ignoring");
          } else if (fileName.endsWith(".class")) {
            transformFile(file);
          }
        }
      }
    }
    catch (Exception e)
    {
      String fileName = file == null ? "null" : file.getName();
      String m = "Error transforming file " + fileName;
      throw new RuntimeException(m, e);
    }
  }
  
  private void transformFile(File file)
    throws IOException, IllegalClassFormatException
  {
    String className = getClassName(file);
    
    byte[] result = this.inputStreamTransform.transform(className, file);
    if (result != null)
    {
      InputStreamTransform.writeBytes(result, file);
      if (this.listener != null) {
        this.listener.logEvent("Enhanced " + file);
      }
    }
    else if (this.listener != null)
    {
      this.listener.logError("Unable to enhance " + file);
    }
  }
  
  private String getClassName(File file)
  {
    String path = file.getPath();
    path = path.substring(this.inDir.length() + 1);
    path = path.substring(0, path.length() - ".class".length());
    
    return StringReplace.replace(path, "\\", "/");
  }
}
