package net.minecraft.server.v1_8_R3;

import com.google.common.base.Charsets;

public class StatusChallengeUtils
{
  public static final char[] a = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
  
  public static String a(byte[] ☃, int ☃, int ☃)
  {
    int ☃ = ☃ - 1;
    int ☃ = ☃ > ☃ ? ☃ : ☃;
    while ((0 != ☃[☃]) && (☃ < ☃)) {
      ☃++;
    }
    return new String(☃, ☃, ☃ - ☃, Charsets.UTF_8);
  }
  
  public static int b(byte[] ☃, int ☃)
  {
    return b(☃, ☃, ☃.length);
  }
  
  public static int b(byte[] ☃, int ☃, int ☃)
  {
    if (0 > ☃ - ☃ - 4) {
      return 0;
    }
    return ☃[(☃ + 3)] << 24 | (☃[(☃ + 2)] & 0xFF) << 16 | (☃[(☃ + 1)] & 0xFF) << 8 | ☃[☃] & 0xFF;
  }
  
  public static int c(byte[] ☃, int ☃, int ☃)
  {
    if (0 > ☃ - ☃ - 4) {
      return 0;
    }
    return ☃[☃] << 24 | (☃[(☃ + 1)] & 0xFF) << 16 | (☃[(☃ + 2)] & 0xFF) << 8 | ☃[(☃ + 3)] & 0xFF;
  }
  
  public static String a(byte ☃)
  {
    return "" + a[((☃ & 0xF0) >>> 4)] + a[(☃ & 0xF)];
  }
}
