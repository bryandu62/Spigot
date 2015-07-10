package org.bukkit.conversations;

public abstract interface ConversationCanceller
  extends Cloneable
{
  public abstract void setConversation(Conversation paramConversation);
  
  public abstract boolean cancelBasedOnInput(ConversationContext paramConversationContext, String paramString);
  
  public abstract ConversationCanceller clone();
}
