package com.avaje.ebean.config;

public abstract interface Encryptor
{
  public abstract byte[] encrypt(byte[] paramArrayOfByte, EncryptKey paramEncryptKey);
  
  public abstract byte[] decrypt(byte[] paramArrayOfByte, EncryptKey paramEncryptKey);
  
  public abstract byte[] encryptString(String paramString, EncryptKey paramEncryptKey);
  
  public abstract String decryptString(byte[] paramArrayOfByte, EncryptKey paramEncryptKey);
}
