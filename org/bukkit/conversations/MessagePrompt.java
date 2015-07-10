package org.bukkit.conversations;

public abstract class MessagePrompt
  implements Prompt
{
  public boolean blocksForInput(ConversationContext context)
  {
    return false;
  }
  
  public Prompt acceptInput(ConversationContext context, String input)
  {
    return getNextPrompt(context);
  }
  
  protected abstract Prompt getNextPrompt(ConversationContext paramConversationContext);
}
