package org.bukkit.conversations;

public abstract interface Conversable
{
  public abstract boolean isConversing();
  
  public abstract void acceptConversationInput(String paramString);
  
  public abstract boolean beginConversation(Conversation paramConversation);
  
  public abstract void abandonConversation(Conversation paramConversation);
  
  public abstract void abandonConversation(Conversation paramConversation, ConversationAbandonedEvent paramConversationAbandonedEvent);
  
  public abstract void sendRawMessage(String paramString);
}
