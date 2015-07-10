package org.apache.logging.log4j.core.selector;

import java.net.URI;
import org.apache.logging.log4j.core.LoggerContext;

public abstract interface NamedContextSelector
  extends ContextSelector
{
  public abstract LoggerContext locateContext(String paramString, Object paramObject, URI paramURI);
  
  public abstract LoggerContext removeContext(String paramString);
}
