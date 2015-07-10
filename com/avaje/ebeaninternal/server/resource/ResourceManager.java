package com.avaje.ebeaninternal.server.resource;

import com.avaje.ebeaninternal.server.lib.resource.ResourceSource;
import java.io.File;

public class ResourceManager
{
  final ResourceSource resourceSource;
  final File autofetchDir;
  
  public ResourceManager(ResourceSource resourceSource, File autofetchDir)
  {
    this.resourceSource = resourceSource;
    this.autofetchDir = autofetchDir;
  }
  
  public ResourceSource getResourceSource()
  {
    return this.resourceSource;
  }
  
  public File getAutofetchDirectory()
  {
    return this.autofetchDir;
  }
}
