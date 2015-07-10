package com.sun.jna.platform.win32;

import com.sun.jna.Memory;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.ByReference;
import com.sun.jna.WString;
import com.sun.jna.win32.StdCallLibrary;

public abstract interface Sspi
  extends StdCallLibrary
{
  public static final int MAX_TOKEN_SIZE = 12288;
  public static final int SECPKG_CRED_INBOUND = 1;
  public static final int SECPKG_CRED_OUTBOUND = 2;
  public static final int SECURITY_NATIVE_DREP = 16;
  public static final int ISC_REQ_ALLOCATE_MEMORY = 256;
  public static final int ISC_REQ_CONFIDENTIALITY = 16;
  public static final int ISC_REQ_CONNECTION = 2048;
  public static final int ISC_REQ_DELEGATE = 1;
  public static final int ISC_REQ_EXTENDED_ERROR = 16384;
  public static final int ISC_REQ_INTEGRITY = 65536;
  public static final int ISC_REQ_MUTUAL_AUTH = 2;
  public static final int ISC_REQ_REPLAY_DETECT = 4;
  public static final int ISC_REQ_SEQUENCE_DETECT = 8;
  public static final int ISC_REQ_STREAM = 32768;
  public static final int SECBUFFER_VERSION = 0;
  public static final int SECBUFFER_EMPTY = 0;
  public static final int SECBUFFER_DATA = 1;
  public static final int SECBUFFER_TOKEN = 2;
  
  public static class SecHandle
    extends Structure
  {
    public Pointer dwLower;
    public Pointer dwUpper;
    
    public SecHandle()
    {
      this.dwLower = null;
      this.dwUpper = null;
    }
    
    public boolean isNull()
    {
      return (this.dwLower == null) && (this.dwUpper == null);
    }
    
    public static class ByReference
      extends Sspi.SecHandle
      implements Structure.ByReference
    {}
  }
  
  public static class PSecHandle
    extends Structure
  {
    public Sspi.SecHandle.ByReference secHandle;
    
    public PSecHandle() {}
    
    public PSecHandle(Sspi.SecHandle h)
    {
      super();
      read();
    }
    
    public static class ByReference
      extends Sspi.PSecHandle
      implements Structure.ByReference
    {}
  }
  
  public static class CredHandle
    extends Sspi.SecHandle
  {}
  
  public static class CtxtHandle
    extends Sspi.SecHandle
  {}
  
  public static class SecBuffer
    extends Structure
  {
    public NativeLong cbBuffer;
    public NativeLong BufferType;
    public Pointer pvBuffer;
    
    public static class ByReference
      extends Sspi.SecBuffer
      implements Structure.ByReference
    {
      public ByReference() {}
      
      public ByReference(int type, int size)
      {
        super(size);
      }
      
      public ByReference(int type, byte[] token)
      {
        super(token);
      }
      
      public byte[] getBytes()
      {
        return super.getBytes();
      }
    }
    
    public SecBuffer()
    {
      this.cbBuffer = new NativeLong(0L);
      this.pvBuffer = null;
      this.BufferType = new NativeLong(0L);
    }
    
    public SecBuffer(int type, int size)
    {
      this.cbBuffer = new NativeLong(size);
      this.pvBuffer = new Memory(size);
      this.BufferType = new NativeLong(type);
      allocateMemory();
    }
    
    public SecBuffer(int type, byte[] token)
    {
      this.cbBuffer = new NativeLong(token.length);
      this.pvBuffer = new Memory(token.length);
      this.pvBuffer.write(0L, token, 0, token.length);
      this.BufferType = new NativeLong(type);
      allocateMemory();
    }
    
    public byte[] getBytes()
    {
      return this.pvBuffer.getByteArray(0L, this.cbBuffer.intValue());
    }
  }
  
  public static class SecBufferDesc
    extends Structure
  {
    public NativeLong ulVersion;
    public NativeLong cBuffers;
    public Sspi.SecBuffer.ByReference[] pBuffers;
    
    public SecBufferDesc()
    {
      this.ulVersion = new NativeLong(0L);
      this.cBuffers = new NativeLong(1L);
      Sspi.SecBuffer.ByReference secBuffer = new Sspi.SecBuffer.ByReference();
      this.pBuffers = ((Sspi.SecBuffer.ByReference[])secBuffer.toArray(1));
      allocateMemory();
    }
    
    public SecBufferDesc(int type, byte[] token)
    {
      this.ulVersion = new NativeLong(0L);
      this.cBuffers = new NativeLong(1L);
      Sspi.SecBuffer.ByReference secBuffer = new Sspi.SecBuffer.ByReference(type, token);
      this.pBuffers = ((Sspi.SecBuffer.ByReference[])secBuffer.toArray(1));
      allocateMemory();
    }
    
    public SecBufferDesc(int type, int tokenSize)
    {
      this.ulVersion = new NativeLong(0L);
      this.cBuffers = new NativeLong(1L);
      Sspi.SecBuffer.ByReference secBuffer = new Sspi.SecBuffer.ByReference(type, tokenSize);
      this.pBuffers = ((Sspi.SecBuffer.ByReference[])secBuffer.toArray(1));
      allocateMemory();
    }
    
    public byte[] getBytes()
    {
      if ((this.pBuffers == null) || (this.cBuffers == null)) {
        throw new RuntimeException("pBuffers | cBuffers");
      }
      if (this.cBuffers.intValue() == 1) {
        return this.pBuffers[0].getBytes();
      }
      throw new RuntimeException("cBuffers > 1");
    }
  }
  
  public static class SECURITY_INTEGER
    extends Structure
  {
    public NativeLong dwLower;
    public NativeLong dwUpper;
    
    public SECURITY_INTEGER()
    {
      this.dwLower = new NativeLong(0L);
      this.dwUpper = new NativeLong(0L);
    }
  }
  
  public static class TimeStamp
    extends Sspi.SECURITY_INTEGER
  {}
  
  public static class PSecPkgInfo
    extends Structure
  {
    public Sspi.SecPkgInfo.ByReference pPkgInfo;
    
    public Sspi.SecPkgInfo.ByReference[] toArray(int size)
    {
      return (Sspi.SecPkgInfo.ByReference[])this.pPkgInfo.toArray(size);
    }
    
    public static class ByReference
      extends Sspi.PSecPkgInfo
      implements Structure.ByReference
    {}
  }
  
  public static class SecPkgInfo
    extends Structure
  {
    public NativeLong fCapabilities;
    public short wVersion;
    public short wRPCID;
    public NativeLong cbMaxToken;
    public WString Name;
    public WString Comment;
    
    public SecPkgInfo()
    {
      this.fCapabilities = new NativeLong(0L);
      this.wVersion = 1;
      this.wRPCID = 0;
      this.cbMaxToken = new NativeLong(0L);
    }
    
    public static class ByReference
      extends Sspi.SecPkgInfo
      implements Structure.ByReference
    {}
  }
}
