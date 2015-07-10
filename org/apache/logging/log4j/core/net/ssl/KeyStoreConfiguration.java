package org.apache.logging.log4j.core.net.ssl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(name="keyStore", category="Core", printObject=true)
public class KeyStoreConfiguration
  extends StoreConfiguration
{
  private KeyStore keyStore;
  private String keyStoreType;
  
  public KeyStoreConfiguration(String location, String password)
  {
    super(location, password);
    this.keyStoreType = "JKS";
    this.keyStore = null;
  }
  
  protected void load()
    throws StoreConfigurationException
  {
    FileInputStream fin = null;
    
    LOGGER.debug("Loading keystore from file with params(location={})", new Object[] { getLocation() });
    try
    {
      if (getLocation() == null) {
        throw new IOException("The location is null");
      }
      fin = new FileInputStream(getLocation());
      KeyStore ks = KeyStore.getInstance(this.keyStoreType);
      ks.load(fin, getPasswordAsCharArray());
      this.keyStore = ks;
      try
      {
        if (fin != null) {
          fin.close();
        }
      }
      catch (IOException e) {}
    }
    catch (CertificateException e)
    {
      LOGGER.error("No Provider supports a KeyStoreSpi implementation for the specified type {}", new Object[] { this.keyStoreType });
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
      LOGGER.error("Something is wrong with the format of the keystore or the given password");
      throw new StoreConfigurationException(e);
    }
    finally
    {
      try
      {
        if (fin != null) {
          fin.close();
        }
      }
      catch (IOException e) {}
    }
    tmp223_220[0] = getLocation();LOGGER.debug("Keystore successfully loaded with params(location={})", tmp223_220);
  }
  
  public KeyStore getKeyStore()
    throws StoreConfigurationException
  {
    if (this.keyStore == null) {
      load();
    }
    return this.keyStore;
  }
  
  @PluginFactory
  public static KeyStoreConfiguration createKeyStoreConfiguration(@PluginAttribute("location") String location, @PluginAttribute("password") String password)
  {
    return new KeyStoreConfiguration(location, password);
  }
}
