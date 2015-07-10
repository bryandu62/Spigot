package org.bukkit.craftbukkit.libs.jline.console.completer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import org.bukkit.craftbukkit.libs.jline.console.ConsoleReader;
import org.bukkit.craftbukkit.libs.jline.console.CursorBuffer;

public class CandidateListCompletionHandler
  implements CompletionHandler
{
  public boolean complete(ConsoleReader reader, List<CharSequence> candidates, int pos)
    throws IOException
  {
    CursorBuffer buf = reader.getCursorBuffer();
    if (candidates.size() == 1)
    {
      CharSequence value = (CharSequence)candidates.get(0);
      if (value.equals(buf.toString())) {
        return false;
      }
      setBuffer(reader, value, pos);
      
      return true;
    }
    if (candidates.size() > 1)
    {
      String value = getUnambiguousCompletions(candidates);
      setBuffer(reader, value, pos);
    }
    printCandidates(reader, candidates);
    
    reader.drawLine();
    
    return true;
  }
  
  public static void setBuffer(ConsoleReader reader, CharSequence value, int offset)
    throws IOException
  {
    while ((reader.getCursorBuffer().cursor > offset) && (reader.backspace())) {}
    reader.putString(value);
    reader.setCursorPosition(offset + value.length());
  }
  
  public static void printCandidates(ConsoleReader reader, Collection<CharSequence> candidates)
    throws IOException
  {
    Set<CharSequence> distinct = new HashSet(candidates);
    if (distinct.size() > reader.getAutoprintThreshold())
    {
      reader.print(Messages.DISPLAY_CANDIDATES.format(new Object[] { Integer.valueOf(candidates.size()) }));
      reader.flush();
      
      String noOpt = Messages.DISPLAY_CANDIDATES_NO.format(new Object[0]);
      String yesOpt = Messages.DISPLAY_CANDIDATES_YES.format(new Object[0]);
      char[] allowed = { yesOpt.charAt(0), noOpt.charAt(0) };
      int c;
      while ((c = reader.readCharacter(allowed)) != -1)
      {
        String tmp = new String(new char[] { (char)c });
        if (noOpt.startsWith(tmp))
        {
          reader.println();
          return;
        }
        if (yesOpt.startsWith(tmp)) {
          break;
        }
        reader.beep();
      }
    }
    if (distinct.size() != candidates.size())
    {
      Collection<CharSequence> copy = new ArrayList();
      for (CharSequence next : candidates) {
        if (!copy.contains(next)) {
          copy.add(next);
        }
      }
      candidates = copy;
    }
    reader.println();
    reader.printColumns(candidates);
  }
  
  private String getUnambiguousCompletions(List<CharSequence> candidates)
  {
    if ((candidates == null) || (candidates.isEmpty())) {
      return null;
    }
    String[] strings = (String[])candidates.toArray(new String[candidates.size()]);
    
    String first = strings[0];
    StringBuilder candidate = new StringBuilder();
    for (int i = 0; i < first.length(); i++)
    {
      if (!startsWith(first.substring(0, i + 1), strings)) {
        break;
      }
      candidate.append(first.charAt(i));
    }
    return candidate.toString();
  }
  
  private boolean startsWith(String starts, String[] candidates)
  {
    for (String candidate : candidates) {
      if (!candidate.startsWith(starts)) {
        return false;
      }
    }
    return true;
  }
  
  private static enum Messages
  {
    DISPLAY_CANDIDATES,  DISPLAY_CANDIDATES_YES,  DISPLAY_CANDIDATES_NO;
    
    private static final ResourceBundle bundle = ResourceBundle.getBundle(CandidateListCompletionHandler.class.getName(), Locale.getDefault());
    
    private Messages() {}
    
    public String format(Object... args)
    {
      if (bundle == null) {
        return "";
      }
      return String.format(bundle.getString(name()), args);
    }
  }
}
