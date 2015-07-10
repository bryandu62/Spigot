package org.bukkit.conversations;

public abstract interface Prompt
  extends Cloneable
{
  public static final Prompt END_OF_CONVERSATION = null;
  
  public abstract String getPromptText(ConversationContext paramConversationContext);
  
  public abstract boolean blocksForInput(ConversationContext paramConversationContext);
  
  public abstract Prompt acceptInput(ConversationContext paramConversationContext, String paramString);
}
