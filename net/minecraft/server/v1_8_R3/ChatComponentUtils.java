package net.minecraft.server.v1_8_R3;

import java.util.List;

public class ChatComponentUtils
{
  public static IChatBaseComponent filterForDisplay(ICommandListener ☃, IChatBaseComponent ☃, Entity ☃)
    throws CommandException
  {
    IChatBaseComponent ☃ = null;
    if ((☃ instanceof ChatComponentScore))
    {
      ChatComponentScore ☃ = (ChatComponentScore)☃;
      String ☃ = ☃.g();
      if (PlayerSelector.isPattern(☃))
      {
        List<Entity> ☃ = PlayerSelector.getPlayers(☃, ☃, Entity.class);
        if (☃.size() == 1) {
          ☃ = ((Entity)☃.get(0)).getName();
        } else {
          throw new ExceptionEntityNotFound();
        }
      }
      ☃ = (☃ != null) && (☃.equals("*")) ? new ChatComponentScore(☃.getName(), ☃.h()) : new ChatComponentScore(☃, ☃.h());
      
      ((ChatComponentScore)☃).b(☃.getText());
    }
    else if ((☃ instanceof ChatComponentSelector))
    {
      String ☃ = ((ChatComponentSelector)☃).g();
      ☃ = PlayerSelector.getPlayerNames(☃, ☃);
      if (☃ == null) {
        ☃ = new ChatComponentText("");
      }
    }
    else if ((☃ instanceof ChatComponentText))
    {
      ☃ = new ChatComponentText(((ChatComponentText)☃).g());
    }
    else if ((☃ instanceof ChatMessage))
    {
      Object[] ☃ = ((ChatMessage)☃).j();
      for (int ☃ = 0; ☃ < ☃.length; ☃++)
      {
        Object ☃ = ☃[☃];
        if ((☃ instanceof IChatBaseComponent)) {
          ☃[☃] = filterForDisplay(☃, (IChatBaseComponent)☃, ☃);
        }
      }
      ☃ = new ChatMessage(((ChatMessage)☃).i(), ☃);
    }
    else
    {
      return ☃;
    }
    ChatModifier ☃ = ☃.getChatModifier();
    if (☃ != null) {
      ☃.setChatModifier(☃.clone());
    }
    for (IChatBaseComponent ☃ : ☃.a()) {
      ☃.addSibling(filterForDisplay(☃, ☃, ☃));
    }
    return ☃;
  }
}
