package net.minecraft.server.v1_8_R3;

public class ChatComponentText
  extends ChatBaseComponent
{
  private final String b;
  
  public ChatComponentText(String ☃)
  {
    this.b = ☃;
  }
  
  public String g()
  {
    return this.b;
  }
  
  public String getText()
  {
    return this.b;
  }
  
  public ChatComponentText h()
  {
    ChatComponentText ☃ = new ChatComponentText(this.b);
    ☃.setChatModifier(getChatModifier().clone());
    for (IChatBaseComponent ☃ : a()) {
      ☃.addSibling(☃.f());
    }
    return ☃;
  }
  
  public boolean equals(Object ☃)
  {
    if (this == ☃) {
      return true;
    }
    if ((☃ instanceof ChatComponentText))
    {
      ChatComponentText ☃ = (ChatComponentText)☃;
      return (this.b.equals(☃.g())) && (super.equals(☃));
    }
    return false;
  }
  
  public String toString()
  {
    return "TextComponent{text='" + this.b + '\'' + ", siblings=" + this.a + ", style=" + getChatModifier() + '}';
  }
}
