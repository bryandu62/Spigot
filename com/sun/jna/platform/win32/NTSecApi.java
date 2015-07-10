package com.sun.jna.platform.win32;

import com.sun.jna.Memory;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.ByReference;
import com.sun.jna.Union;
import com.sun.jna.win32.StdCallLibrary;

public abstract interface NTSecApi
  extends StdCallLibrary
{
  public static final int ForestTrustTopLevelName = 0;
  public static final int ForestTrustTopLevelNameEx = 1;
  public static final int ForestTrustDomainInfo = 2;
  
  public static class LSA_UNICODE_STRING
    extends Structure
  {
    public short Length;
    public short MaximumLength;
    public Pointer Buffer;
    
    public String getString()
    {
      byte[] data = this.Buffer.getByteArray(0L, this.Length);
      if ((data.length < 2) || (data[(data.length - 1)] != 0))
      {
        Memory newdata = new Memory(data.length + 2);
        newdata.write(0L, data, 0, data.length);
        return newdata.getString(0L, true);
      }
      return this.Buffer.getString(0L, true);
    }
    
    public static class ByReference
      extends NTSecApi.LSA_UNICODE_STRING
      implements Structure.ByReference
    {}
  }
  
  public static class PLSA_UNICODE_STRING
  {
    public NTSecApi.LSA_UNICODE_STRING.ByReference s;
    
    public static class ByReference
      extends NTSecApi.PLSA_UNICODE_STRING
      implements Structure.ByReference
    {}
  }
  
  public static class LSA_FOREST_TRUST_DOMAIN_INFO
    extends Structure
  {
    public WinNT.PSID.ByReference Sid;
    public NTSecApi.LSA_UNICODE_STRING DnsName;
    public NTSecApi.LSA_UNICODE_STRING NetbiosName;
  }
  
  public static class LSA_FOREST_TRUST_BINARY_DATA
    extends Structure
  {
    public NativeLong Length;
    public Pointer Buffer;
  }
  
  public static class LSA_FOREST_TRUST_RECORD
    extends Structure
  {
    public NativeLong Flags;
    public int ForestTrustType;
    public WinNT.LARGE_INTEGER Time;
    public UNION u;
    
    public void read()
    {
      super.read();
      switch (this.ForestTrustType)
      {
      case 0: 
      case 1: 
        this.u.setType(NTSecApi.LSA_UNICODE_STRING.class);
        break;
      case 2: 
        this.u.setType(NTSecApi.LSA_FOREST_TRUST_DOMAIN_INFO.class);
        break;
      default: 
        this.u.setType(NTSecApi.LSA_FOREST_TRUST_BINARY_DATA.class);
      }
      this.u.read();
    }
    
    public static class UNION
      extends Union
    {
      public NTSecApi.LSA_UNICODE_STRING TopLevelName;
      public NTSecApi.LSA_FOREST_TRUST_DOMAIN_INFO DomainInfo;
      public NTSecApi.LSA_FOREST_TRUST_BINARY_DATA Data;
      
      public static class ByReference
        extends NTSecApi.LSA_FOREST_TRUST_RECORD.UNION
        implements Structure.ByReference
      {}
    }
    
    public static class ByReference
      extends NTSecApi.LSA_FOREST_TRUST_RECORD
      implements Structure.ByReference
    {}
  }
  
  public static class PLSA_FOREST_TRUST_RECORD
    extends Structure
  {
    public NTSecApi.LSA_FOREST_TRUST_RECORD.ByReference tr;
    
    public static class ByReference
      extends NTSecApi.PLSA_FOREST_TRUST_RECORD
      implements Structure.ByReference
    {}
  }
  
  public static class LSA_FOREST_TRUST_INFORMATION
    extends Structure
  {
    public NativeLong RecordCount;
    public NTSecApi.PLSA_FOREST_TRUST_RECORD.ByReference Entries;
    
    public NTSecApi.PLSA_FOREST_TRUST_RECORD[] getEntries()
    {
      return (NTSecApi.PLSA_FOREST_TRUST_RECORD[])this.Entries.toArray(this.RecordCount.intValue());
    }
    
    public static class ByReference
      extends NTSecApi.LSA_FOREST_TRUST_INFORMATION
      implements Structure.ByReference
    {}
  }
  
  public static class PLSA_FOREST_TRUST_INFORMATION
    extends Structure
  {
    public NTSecApi.LSA_FOREST_TRUST_INFORMATION.ByReference fti;
    
    public static class ByReference
      extends NTSecApi.PLSA_FOREST_TRUST_INFORMATION
      implements Structure.ByReference
    {}
  }
}
