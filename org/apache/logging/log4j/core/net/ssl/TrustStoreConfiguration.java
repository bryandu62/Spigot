package org.apache.logging.log4j.core.net.ssl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(name="trustStore", category="Core", printObject=true)
public class TrustStoreConfiguration
  extends StoreConfiguration
{
  private KeyStore trustStore;
  private String trustStoreType;
  
  public TrustStoreConfiguration(String location, String password)
  {
    super(location, password);
    this.trustStoreType = "JKS";
    this.trustStore = null;
  }
  
  protected void load()
    throws StoreConfigurationException
  {
    KeyStore ts = null;
    InputStream in = null;
    
    LOGGER.debug("Loading truststore from file with params(location={})", new Object[] { getLocation() });
    try
    {
      if (getLocation() == null) {
        throw new IOException("The location is null");
      }
      ts = KeyStore.getInstance(this.trustStoreType);
      in = new FileInputStream(getLocation());
      ts.load(in, getPasswordAsCharArray());
      try
      {
        if (in != null) {
          in.close();
        }
      }
      catch (Exception e)
      {
        LOGGER.warn("Error closing {}", new Object[] { getLocation(), e });
      }
      this.trustStore = ts;
    }
    catch (CertificateException e)
    {
      LOGGER.error("No Provider supports a KeyStoreSpi implementation for the specified type {}", new Object[] { this.trustStoreType });
      throw new StoreConfigurationException(e);
    }
    catch (NoSuchAlgorithmException e)
    {
      LOGGER.error("The algorithm used to check the integrity of the keystore cannot be found");
      throw new StoreConfigurationException(e);
    }
    catch (KeyStoreException e)
    {
      LOGGER.error(e);
      throw new StoreConfigurationException(e);
    }
    catch (FileNotFoundException e)
    {
      LOGGER.error("The keystore file({}) is not found", new Object[] { getLocation() });
      throw new StoreConfigurationException(e);
    }
    catch (IOException e)
    {
      LOGGER.error("Something is wrong with the format of the truststore or the given password: {}", new Object[] { e.getMessage() });
      throw new StoreConfigurationException(e);
    }
    finally
    {
      try
      {
        if (in != null) {
          in.close();
        }
      }
      catch (Exception e)
      {
        LOGGER.warn("Error closing {}", new Object[] { getLocation(), e });
      }
    }
    LOGGER.debug("Truststore successfully loaded with params(location={})", new Object[] { getLocation() });
  }
  
  public KeyStore getTrustStore()
    throws StoreConfigurationException
  {
    if (this.trustStore == null) {
      load();
    }
    return this.trustStore;
  }
  
  @PluginFactory
  public static TrustStoreConfiguration createTrustStoreConfiguration(@PluginAttribute("location") String location, @PluginAttribute("password") String password)
  {
    return new TrustStoreConfiguration(location, password);
  }
}
