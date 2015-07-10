package net.minecraft.server.v1_8_R3;

public class SharedConstants
{
  public static boolean isAllowedChatCharacter(char ☃)
  {
    return (☃ != '§') && (☃ >= ' ') && (☃ != '');
  }
  
  public static final char[] allowedCharacters = { '/', '\n', '\r', '\t', '\000', '\f', '`', '?', '*', '\\', '<', '>', '|', '"', ':' };
  
  public static String a(String ☃)
  {
    StringBuilder ☃ = new StringBuilder();
    for (char ☃ : ☃.toCharArray()) {
      if (isAllowedChatCharacter(☃)) {
        ☃.append(☃);
      }
    }
    return ☃.toString();
  }
}
