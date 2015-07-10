package net.minecraft.server.v1_8_R3;

public class ChatComponentSelector
  extends ChatBaseComponent
{
  private final String b;
  
  public ChatComponentSelector(String ☃)
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
  
  public ChatComponentSelector h()
  {
    ChatComponentSelector ☃ = new ChatComponentSelector(this.b);
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
    if ((☃ instanceof ChatComponentSelector))
    {
      ChatComponentSelector ☃ = (ChatComponentSelector)☃;
      return (this.b.equals(☃.b)) && (super.equals(☃));
    }
    return false;
  }
  
  public String toString()
  {
    return "SelectorComponent{pattern='" + this.b + '\'' + ", siblings=" + this.a + ", style=" + getChatModifier() + '}';
  }
}
