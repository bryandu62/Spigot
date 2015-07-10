package net.minecraft.server.v1_8_R3;

public class ChatMessageException
  extends IllegalArgumentException
{
  public ChatMessageException(ChatMessage ☃, String ☃)
  {
    super(String.format("Error parsing: %s: %s", new Object[] { ☃, ☃ }));
  }
  
  public ChatMessageException(ChatMessage ☃, int ☃)
  {
    super(String.format("Invalid index %d requested for %s", new Object[] { Integer.valueOf(☃), ☃ }));
  }
  
  public ChatMessageException(ChatMessage ☃, Throwable ☃)
  {
    super(String.format("Error while parsing: %s", new Object[] { ☃ }), ☃);
  }
}
