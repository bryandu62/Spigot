package org.bukkit.conversations;

import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

public class InactivityConversationCanceller
  implements ConversationCanceller
{
  protected Plugin plugin;
  protected int timeoutSeconds;
  protected Conversation conversation;
  private int taskId = -1;
  
  public InactivityConversationCanceller(Plugin plugin, int timeoutSeconds)
  {
    this.plugin = plugin;
    this.timeoutSeconds = timeoutSeconds;
  }
  
  public void setConversation(Conversation conversation)
  {
    this.conversation = conversation;
    startTimer();
  }
  
  public boolean cancelBasedOnInput(ConversationContext context, String input)
  {
    stopTimer();
    startTimer();
    return false;
  }
  
  public ConversationCanceller clone()
  {
    return new InactivityConversationCanceller(this.plugin, this.timeoutSeconds);
  }
  
  private void startTimer()
  {
    this.taskId = this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable()
    {
      public void run()
      {
        if (InactivityConversationCanceller.this.conversation.getState() == Conversation.ConversationState.UNSTARTED)
        {
          InactivityConversationCanceller.this.startTimer();
        }
        else if (InactivityConversationCanceller.this.conversation.getState() == Conversation.ConversationState.STARTED)
        {
          InactivityConversationCanceller.this.cancelling(InactivityConversationCanceller.this.conversation);
          InactivityConversationCanceller.this.conversation.abandon(new ConversationAbandonedEvent(InactivityConversationCanceller.this.conversation, InactivityConversationCanceller.this));
        }
      }
    }, this.timeoutSeconds * 20);
  }
  
  private void stopTimer()
  {
    if (this.taskId != -1)
    {
      this.plugin.getServer().getScheduler().cancelTask(this.taskId);
      this.taskId = -1;
    }
  }
  
  protected void cancelling(Conversation conversation) {}
}
