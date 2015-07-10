package org.bukkit.conversations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ConversationFactory
{
  protected Plugin plugin;
  protected boolean isModal;
  protected boolean localEchoEnabled;
  protected ConversationPrefix prefix;
  protected Prompt firstPrompt;
  protected Map<Object, Object> initialSessionData;
  protected String playerOnlyMessage;
  protected List<ConversationCanceller> cancellers;
  protected List<ConversationAbandonedListener> abandonedListeners;
  
  public ConversationFactory(Plugin plugin)
  {
    this.plugin = plugin;
    this.isModal = true;
    this.localEchoEnabled = true;
    this.prefix = new NullConversationPrefix();
    this.firstPrompt = Prompt.END_OF_CONVERSATION;
    this.initialSessionData = new HashMap();
    this.playerOnlyMessage = null;
    this.cancellers = new ArrayList();
    this.abandonedListeners = new ArrayList();
  }
  
  public ConversationFactory withModality(boolean modal)
  {
    this.isModal = modal;
    return this;
  }
  
  public ConversationFactory withLocalEcho(boolean localEchoEnabled)
  {
    this.localEchoEnabled = localEchoEnabled;
    return this;
  }
  
  public ConversationFactory withPrefix(ConversationPrefix prefix)
  {
    this.prefix = prefix;
    return this;
  }
  
  public ConversationFactory withTimeout(int timeoutSeconds)
  {
    return withConversationCanceller(new InactivityConversationCanceller(this.plugin, timeoutSeconds));
  }
  
  public ConversationFactory withFirstPrompt(Prompt firstPrompt)
  {
    this.firstPrompt = firstPrompt;
    return this;
  }
  
  public ConversationFactory withInitialSessionData(Map<Object, Object> initialSessionData)
  {
    this.initialSessionData = initialSessionData;
    return this;
  }
  
  public ConversationFactory withEscapeSequence(String escapeSequence)
  {
    return withConversationCanceller(new ExactMatchConversationCanceller(escapeSequence));
  }
  
  public ConversationFactory withConversationCanceller(ConversationCanceller canceller)
  {
    this.cancellers.add(canceller);
    return this;
  }
  
  public ConversationFactory thatExcludesNonPlayersWithMessage(String playerOnlyMessage)
  {
    this.playerOnlyMessage = playerOnlyMessage;
    return this;
  }
  
  public ConversationFactory addConversationAbandonedListener(ConversationAbandonedListener listener)
  {
    this.abandonedListeners.add(listener);
    return this;
  }
  
  public Conversation buildConversation(Conversable forWhom)
  {
    if ((this.playerOnlyMessage != null) && (!(forWhom instanceof Player))) {
      return new Conversation(this.plugin, forWhom, new NotPlayerMessagePrompt(null));
    }
    Map<Object, Object> copiedInitialSessionData = new HashMap();
    copiedInitialSessionData.putAll(this.initialSessionData);
    
    Conversation conversation = new Conversation(this.plugin, forWhom, this.firstPrompt, copiedInitialSessionData);
    conversation.setModal(this.isModal);
    conversation.setLocalEchoEnabled(this.localEchoEnabled);
    conversation.setPrefix(this.prefix);
    for (ConversationCanceller canceller : this.cancellers) {
      conversation.addConversationCanceller(canceller.clone());
    }
    for (ConversationAbandonedListener listener : this.abandonedListeners) {
      conversation.addConversationAbandonedListener(listener);
    }
    return conversation;
  }
  
  private class NotPlayerMessagePrompt
    extends MessagePrompt
  {
    private NotPlayerMessagePrompt() {}
    
    public String getPromptText(ConversationContext context)
    {
      return ConversationFactory.this.playerOnlyMessage;
    }
    
    protected Prompt getNextPrompt(ConversationContext context)
    {
      return Prompt.END_OF_CONVERSATION;
    }
  }
}
