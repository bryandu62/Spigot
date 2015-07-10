package org.apache.logging.log4j.core.net;

import java.util.Map;

public abstract interface Advertiser
{
  public abstract Object advertise(Map<String, String> paramMap);
  
  public abstract void unadvertise(Object paramObject);
}
