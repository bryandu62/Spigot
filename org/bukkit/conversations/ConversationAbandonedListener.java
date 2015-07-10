package org.bukkit.conversations;

import java.util.EventListener;

public abstract interface ConversationAbandonedListener
  extends EventListener
{
  public abstract void conversationAbandoned(ConversationAbandonedEvent paramConversationAbandonedEvent);
}
