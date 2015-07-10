package org.apache.logging.log4j.core.helpers;

import javax.crypto.SecretKey;

public abstract interface SecretKeyProvider
{
  public abstract SecretKey getSecretKey();
}
