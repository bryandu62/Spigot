package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

public abstract interface Oleaut32
  extends StdCallLibrary
{
  public static final Oleaut32 INSTANCE = (Oleaut32)Native.loadLibrary("Oleaut32", Oleaut32.class, W32APIOptions.UNICODE_OPTIONS);
  
  public abstract Pointer SysAllocString(String paramString);
  
  public abstract void SysFreeString(Pointer paramPointer);
}
