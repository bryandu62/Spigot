package com.avaje.ebeaninternal.server.lib.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class FileResourceContent
  implements ResourceContent
{
  File file;
  String entryName;
  
  public FileResourceContent(File file, String entryName)
  {
    this.file = file;
    this.entryName = entryName;
  }
  
  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    sb.append("[").append(getName());
    sb.append("] size[").append(size());
    sb.append("] lastModified[").append(new Date(lastModified()));
    sb.append("]");
    return sb.toString();
  }
  
  public String getName()
  {
    return this.entryName;
  }
  
  public long lastModified()
  {
    return this.file.lastModified();
  }
  
  public long size()
  {
    return this.file.length();
  }
  
  public InputStream getInputStream()
    throws IOException
  {
    FileInputStream is = new FileInputStream(this.file);
    return is;
  }
}
