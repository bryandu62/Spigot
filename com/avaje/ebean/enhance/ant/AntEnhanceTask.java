package com.avaje.ebean.enhance.ant;

import com.avaje.ebean.enhance.agent.Transformer;
import java.io.File;
import java.io.PrintStream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class AntEnhanceTask
  extends Task
{
  String classpath;
  String classSource;
  String classDestination;
  String transformArgs;
  String packages;
  
  public void execute()
    throws BuildException
  {
    File f = new File("");
    System.out.println("Current Directory: " + f.getAbsolutePath());
    
    StringBuilder extraClassPath = new StringBuilder();
    extraClassPath.append(this.classSource);
    if (this.classpath != null)
    {
      if (!extraClassPath.toString().endsWith(";")) {
        extraClassPath.append(";");
      }
      extraClassPath.append(this.classpath);
    }
    Transformer t = new Transformer(extraClassPath.toString(), this.transformArgs);
    
    ClassLoader cl = AntEnhanceTask.class.getClassLoader();
    OfflineFileTransform ft = new OfflineFileTransform(t, cl, this.classSource, this.classDestination);
    
    ft.process(this.packages);
  }
  
  public String getClasspath()
  {
    return this.classpath;
  }
  
  public void setClasspath(String classpath)
  {
    this.classpath = classpath;
  }
  
  public void setClassSource(String source)
  {
    this.classSource = source;
  }
  
  public void setClassDestination(String destination)
  {
    this.classDestination = destination;
  }
  
  public void setTransformArgs(String transformArgs)
  {
    this.transformArgs = transformArgs;
  }
  
  public void setPackages(String packages)
  {
    this.packages = packages;
  }
}
