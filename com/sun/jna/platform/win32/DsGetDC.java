package com.sun.jna.platform.win32;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.ByReference;
import com.sun.jna.WString;
import com.sun.jna.win32.StdCallLibrary;

public abstract interface DsGetDC
  extends StdCallLibrary
{
  public static final int DS_DOMAIN_IN_FOREST = 1;
  public static final int DS_DOMAIN_DIRECT_OUTBOUND = 2;
  public static final int DS_DOMAIN_TREE_ROOT = 4;
  public static final int DS_DOMAIN_PRIMARY = 8;
  public static final int DS_DOMAIN_NATIVE_MODE = 16;
  public static final int DS_DOMAIN_DIRECT_INBOUND = 32;
  public static final int DS_DOMAIN_VALID_FLAGS = 63;
  
  public static class DOMAIN_CONTROLLER_INFO
    extends Structure
  {
    public WString DomainControllerName;
    public WString DomainControllerAddress;
    public int DomainControllerAddressType;
    public Guid.GUID DomainGuid;
    public WString DomainName;
    public WString DnsForestName;
    public int Flags;
    public WString DcSiteName;
    public WString ClientSiteName;
    
    public DOMAIN_CONTROLLER_INFO() {}
    
    public DOMAIN_CONTROLLER_INFO(Pointer memory)
    {
      super();
      read();
    }
    
    public static class ByReference
      extends DsGetDC.DOMAIN_CONTROLLER_INFO
      implements Structure.ByReference
    {}
  }
  
  public static class PDOMAIN_CONTROLLER_INFO
    extends Structure
  {
    public DsGetDC.DOMAIN_CONTROLLER_INFO.ByReference dci;
    
    public static class ByReference
      extends DsGetDC.PDOMAIN_CONTROLLER_INFO
      implements Structure.ByReference
    {}
  }
  
  public static class DS_DOMAIN_TRUSTS
    extends Structure
  {
    public WString NetbiosDomainName;
    public WString DnsDomainName;
    public NativeLong Flags;
    public NativeLong ParentIndex;
    public NativeLong TrustType;
    public NativeLong TrustAttributes;
    public WinNT.PSID.ByReference DomainSid;
    public Guid.GUID DomainGuid;
    
    public static class ByReference
      extends DsGetDC.DS_DOMAIN_TRUSTS
      implements Structure.ByReference
    {}
  }
  
  public static class PDS_DOMAIN_TRUSTS
    extends Structure
  {
    public DsGetDC.DS_DOMAIN_TRUSTS.ByReference t;
    
    public DsGetDC.DS_DOMAIN_TRUSTS[] getTrusts(int count)
    {
      return (DsGetDC.DS_DOMAIN_TRUSTS[])this.t.toArray(count);
    }
    
    public static class ByReference
      extends DsGetDC.PDS_DOMAIN_TRUSTS
      implements Structure.ByReference
    {}
  }
}
