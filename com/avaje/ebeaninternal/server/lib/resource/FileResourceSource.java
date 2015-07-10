package com.avaje.ebeaninternal.server.lib.resource;

import java.io.File;

public class FileResourceSource
  extends AbstractResourceSource
  implements ResourceSource
{
  String directory;
  String baseDir;
  
  public FileResourceSource(String directory)
  {
    this.directory = directory;
    this.baseDir = (directory + File.separator);
  }
  
  public FileResourceSource(File dir)
  {
    this(dir.getPath());
  }
  
  public String getRealPath()
  {
    return this.directory;
  }
  
  public ResourceContent getContent(String entry)
  {
    String fullPath = this.baseDir + entry;
    
    File f = new File(fullPath);
    if (f.exists())
    {
      FileResourceContent content = new FileResourceContent(f, entry);
      return content;
    }
    return null;
  }
}
