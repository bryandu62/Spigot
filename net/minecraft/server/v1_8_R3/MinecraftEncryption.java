package net.minecraft.server.v1_8_R3;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MinecraftEncryption
{
  private static final Logger a = ;
  
  public static KeyPair b()
  {
    try
    {
      KeyPairGenerator ☃ = KeyPairGenerator.getInstance("RSA");
      ☃.initialize(1024);
      
      return ☃.generateKeyPair();
    }
    catch (NoSuchAlgorithmException ☃)
    {
      ☃.printStackTrace();
      
      a.error("Key pair generation failed!");
    }
    return null;
  }
  
  public static byte[] a(String ☃, PublicKey ☃, SecretKey ☃)
  {
    try
    {
      return a("SHA-1", new byte[][] { ☃.getBytes("ISO_8859_1"), ☃.getEncoded(), ☃.getEncoded() });
    }
    catch (UnsupportedEncodingException ☃)
    {
      ☃.printStackTrace();
    }
    return null;
  }
  
  private static byte[] a(String ☃, byte[]... ☃)
  {
    try
    {
      MessageDigest ☃ = MessageDigest.getInstance(☃);
      for (byte[] ☃ : ☃) {
        ☃.update(☃);
      }
      return ☃.digest();
    }
    catch (NoSuchAlgorithmException ☃)
    {
      ☃.printStackTrace();
    }
    return null;
  }
  
  public static PublicKey a(byte[] ☃)
  {
    try
    {
      EncodedKeySpec ☃ = new X509EncodedKeySpec(☃);
      KeyFactory ☃ = KeyFactory.getInstance("RSA");
      return ☃.generatePublic(☃);
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {}catch (InvalidKeySpecException localInvalidKeySpecException) {}
    a.error("Public key reconstitute failed!");
    return null;
  }
  
  public static SecretKey a(PrivateKey ☃, byte[] ☃)
  {
    return new SecretKeySpec(b(☃, ☃), "AES");
  }
  
  public static byte[] b(Key ☃, byte[] ☃)
  {
    return a(2, ☃, ☃);
  }
  
  private static byte[] a(int ☃, Key ☃, byte[] ☃)
  {
    try
    {
      return a(☃, ☃.getAlgorithm(), ☃).doFinal(☃);
    }
    catch (IllegalBlockSizeException ☃)
    {
      ☃.printStackTrace();
    }
    catch (BadPaddingException ☃)
    {
      ☃.printStackTrace();
    }
    a.error("Cipher data failed!");
    return null;
  }
  
  private static Cipher a(int ☃, String ☃, Key ☃)
  {
    try
    {
      Cipher ☃ = Cipher.getInstance(☃);
      ☃.init(☃, ☃);
      return ☃;
    }
    catch (InvalidKeyException ☃)
    {
      ☃.printStackTrace();
    }
    catch (NoSuchAlgorithmException ☃)
    {
      ☃.printStackTrace();
    }
    catch (NoSuchPaddingException ☃)
    {
      ☃.printStackTrace();
    }
    a.error("Cipher creation failed!");
    return null;
  }
  
  public static Cipher a(int ☃, Key ☃)
  {
    try
    {
      Cipher ☃ = Cipher.getInstance("AES/CFB8/NoPadding");
      ☃.init(☃, ☃, new IvParameterSpec(☃.getEncoded()));
      return ☃;
    }
    catch (GeneralSecurityException ☃)
    {
      throw new RuntimeException(☃);
    }
  }
}
