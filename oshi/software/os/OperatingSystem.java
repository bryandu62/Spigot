package oshi.software.os;

public abstract interface OperatingSystem
{
  public abstract String getFamily();
  
  public abstract String getManufacturer();
  
  public abstract OperatingSystemVersion getVersion();
}
