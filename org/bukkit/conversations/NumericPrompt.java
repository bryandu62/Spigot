package org.bukkit.conversations;

import org.apache.commons.lang.math.NumberUtils;

public abstract class NumericPrompt
  extends ValidatingPrompt
{
  protected boolean isInputValid(ConversationContext context, String input)
  {
    return (NumberUtils.isNumber(input)) && (isNumberValid(context, NumberUtils.createNumber(input)));
  }
  
  protected boolean isNumberValid(ConversationContext context, Number input)
  {
    return true;
  }
  
  protected Prompt acceptValidatedInput(ConversationContext context, String input)
  {
    try
    {
      return acceptValidatedInput(context, NumberUtils.createNumber(input));
    }
    catch (NumberFormatException localNumberFormatException) {}
    return acceptValidatedInput(context, NumberUtils.INTEGER_ZERO);
  }
  
  protected abstract Prompt acceptValidatedInput(ConversationContext paramConversationContext, Number paramNumber);
  
  protected String getFailedValidationText(ConversationContext context, String invalidInput)
  {
    if (NumberUtils.isNumber(invalidInput)) {
      return getFailedValidationText(context, NumberUtils.createNumber(invalidInput));
    }
    return getInputNotNumericText(context, invalidInput);
  }
  
  protected String getInputNotNumericText(ConversationContext context, String invalidInput)
  {
    return null;
  }
  
  protected String getFailedValidationText(ConversationContext context, Number invalidInput)
  {
    return null;
  }
}
