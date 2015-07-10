package com.avaje.ebeaninternal.server.resource;

import com.avaje.ebean.config.AutofetchConfig;
import com.avaje.ebean.config.GlobalProperties;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebeaninternal.server.lib.resource.DirectoryFinder;
import com.avaje.ebeaninternal.server.lib.resource.FileResourceSource;
import com.avaje.ebeaninternal.server.lib.resource.ResourceSource;
import com.avaje.ebeaninternal.server.lib.resource.UrlResourceSource;
import com.avaje.ebeaninternal.server.lib.util.NotFoundException;
import java.io.File;
import java.util.logging.Logger;
import javax.servlet.ServletContext;

public class ResourceManagerFactory
{
  private static final Logger logger = Logger.getLogger(ResourceManagerFactory.class.getName());
  
  public static ResourceManager createResourceManager(ServerConfig serverConfig)
  {
    ResourceSource resourceSource = createResourceSource(serverConfig);
    File autofetchDir = getAutofetchDir(serverConfig, resourceSource);
    
    return new ResourceManager(resourceSource, autofetchDir);
  }
  
  protected static File getAutofetchDir(ServerConfig serverConfig, ResourceSource resourceSource)
  {
    String dir = null;
    if (serverConfig.getAutofetchConfig() != null) {
      dir = serverConfig.getAutofetchConfig().getLogDirectoryWithEval();
    }
    if (dir != null) {
      return new File(dir);
    }
    String realPath = resourceSource.getRealPath();
    if (realPath != null) {
      return new File(realPath);
    }
    throw new RuntimeException("No autofetch directory set?");
  }
  
  protected static ResourceSource createResourceSource(ServerConfig serverConfig)
  {
    String defaultDir = serverConfig.getResourceDirectory();
    
    ServletContext sc = GlobalProperties.getServletContext();
    if (sc != null)
    {
      if (defaultDir == null) {
        defaultDir = "WEB-INF/ebean";
      }
      return new UrlResourceSource(sc, defaultDir);
    }
    return createFileSource(defaultDir);
  }
  
  private static ResourceSource createFileSource(String fileDir)
  {
    if (fileDir != null)
    {
      File dir = new File(fileDir);
      if (dir.exists())
      {
        logger.info("ResourceManager initialised: type[file] [" + fileDir + "]");
        return new FileResourceSource(fileDir);
      }
      String msg = "ResourceManager could not find directory [" + fileDir + "]";
      throw new NotFoundException(msg);
    }
    File guessDir = DirectoryFinder.find(null, "WEB-INF", 3);
    if (guessDir != null)
    {
      logger.info("ResourceManager initialised: type[file] [" + guessDir.getPath() + "]");
      return new FileResourceSource(guessDir.getPath());
    }
    File workingDir = new File(".");
    return new FileResourceSource(workingDir);
  }
}
