package org.bukkit.conversations;

import org.bukkit.ChatColor;

public abstract class ValidatingPrompt
  implements Prompt
{
  public Prompt acceptInput(ConversationContext context, String input)
  {
    if (isInputValid(context, input)) {
      return acceptValidatedInput(context, input);
    }
    String failPrompt = getFailedValidationText(context, input);
    if (failPrompt != null) {
      context.getForWhom().sendRawMessage(ChatColor.RED + failPrompt);
    }
    return this;
  }
  
  public boolean blocksForInput(ConversationContext context)
  {
    return true;
  }
  
  protected abstract boolean isInputValid(ConversationContext paramConversationContext, String paramString);
  
  protected abstract Prompt acceptValidatedInput(ConversationContext paramConversationContext, String paramString);
  
  protected String getFailedValidationText(ConversationContext context, String invalidInput)
  {
    return null;
  }
}
