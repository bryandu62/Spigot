package org.apache.logging.log4j.message;

import java.util.ResourceBundle;

public class LocalizedMessageFactory
  extends AbstractMessageFactory
{
  private final ResourceBundle bundle;
  private final String bundleId;
  
  public LocalizedMessageFactory(ResourceBundle bundle)
  {
    this.bundle = bundle;
    this.bundleId = null;
  }
  
  public LocalizedMessageFactory(String bundleId)
  {
    this.bundle = null;
    this.bundleId = bundleId;
  }
  
  public Message newMessage(String message, Object... params)
  {
    if (this.bundle == null) {
      return new LocalizedMessage(this.bundleId, message, params);
    }
    return new LocalizedMessage(this.bundle, message, params);
  }
}
