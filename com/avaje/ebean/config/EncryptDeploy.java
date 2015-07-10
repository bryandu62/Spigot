package com.avaje.ebean.config;

public class EncryptDeploy
{
  public static final EncryptDeploy NO_ENCRYPT = new EncryptDeploy(Mode.MODE_NO_ENCRYPT, true, 0);
  public static final EncryptDeploy ANNOTATION = new EncryptDeploy(Mode.MODE_ANNOTATION, true, 0);
  public static final EncryptDeploy ENCRYPT_DB = new EncryptDeploy(Mode.MODE_ENCRYPT, true, 0);
  public static final EncryptDeploy ENCRYPT_CLIENT = new EncryptDeploy(Mode.MODE_ENCRYPT, false, 0);
  private final Mode mode;
  private final boolean dbEncrypt;
  private final int dbLength;
  
  public static enum Mode
  {
    MODE_ENCRYPT,  MODE_NO_ENCRYPT,  MODE_ANNOTATION;
    
    private Mode() {}
  }
  
  public EncryptDeploy(Mode mode, boolean dbEncrypt, int dbLength)
  {
    this.mode = mode;
    this.dbEncrypt = dbEncrypt;
    this.dbLength = dbLength;
  }
  
  public Mode getMode()
  {
    return this.mode;
  }
  
  public boolean isDbEncrypt()
  {
    return this.dbEncrypt;
  }
  
  public int getDbLength()
  {
    return this.dbLength;
  }
}
