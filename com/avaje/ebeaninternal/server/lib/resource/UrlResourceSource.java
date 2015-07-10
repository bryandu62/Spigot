package com.avaje.ebeaninternal.server.lib.resource;

import com.avaje.ebeaninternal.server.lib.util.GeneralException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.servlet.ServletContext;

public class UrlResourceSource
  extends AbstractResourceSource
  implements ResourceSource
{
  ServletContext sc;
  String basePath;
  String realPath;
  
  public UrlResourceSource(ServletContext sc, String basePath)
  {
    this.sc = sc;
    if (basePath == null) {
      this.basePath = "/";
    } else {
      this.basePath = ("/" + basePath + "/");
    }
    this.realPath = sc.getRealPath(basePath);
  }
  
  public String getRealPath()
  {
    return this.realPath;
  }
  
  public ResourceContent getContent(String entry)
  {
    try
    {
      URL url = this.sc.getResource(this.basePath + entry);
      if (url != null) {
        return new UrlResourceContent(url, entry);
      }
      return null;
    }
    catch (MalformedURLException ex)
    {
      throw new GeneralException(ex);
    }
  }
}
