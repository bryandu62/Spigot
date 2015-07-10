package org.bukkit.craftbukkit.libs.jline.console.completer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.craftbukkit.libs.jline.internal.Preconditions;

public class ArgumentCompleter
  implements Completer
{
  private final ArgumentDelimiter delimiter;
  private final List<Completer> completers = new ArrayList();
  private boolean strict = true;
  
  public ArgumentCompleter(ArgumentDelimiter delimiter, Collection<Completer> completers)
  {
    this.delimiter = ((ArgumentDelimiter)Preconditions.checkNotNull(delimiter));
    Preconditions.checkNotNull(completers);
    this.completers.addAll(completers);
  }
  
  public ArgumentCompleter(ArgumentDelimiter delimiter, Completer... completers)
  {
    this(delimiter, Arrays.asList(completers));
  }
  
  public ArgumentCompleter(Completer... completers)
  {
    this(new WhitespaceArgumentDelimiter(), completers);
  }
  
  public ArgumentCompleter(List<Completer> completers)
  {
    this(new WhitespaceArgumentDelimiter(), completers);
  }
  
  public void setStrict(boolean strict)
  {
    this.strict = strict;
  }
  
  public boolean isStrict()
  {
    return this.strict;
  }
  
  public ArgumentDelimiter getDelimiter()
  {
    return this.delimiter;
  }
  
  public List<Completer> getCompleters()
  {
    return this.completers;
  }
  
  public int complete(String buffer, int cursor, List<CharSequence> candidates)
  {
    Preconditions.checkNotNull(candidates);
    
    ArgumentDelimiter delim = getDelimiter();
    ArgumentList list = delim.delimit(buffer, cursor);
    int argpos = list.getArgumentPosition();
    int argIndex = list.getCursorArgumentIndex();
    if (argIndex < 0) {
      return -1;
    }
    List<Completer> completers = getCompleters();
    Completer completer;
    Completer completer;
    if (argIndex >= completers.size()) {
      completer = (Completer)completers.get(completers.size() - 1);
    } else {
      completer = (Completer)completers.get(argIndex);
    }
    for (int i = 0; (isStrict()) && (i < argIndex); i++)
    {
      Completer sub = (Completer)completers.get(i >= completers.size() ? completers.size() - 1 : i);
      String[] args = list.getArguments();
      String arg = (args == null) || (i >= args.length) ? "" : args[i];
      
      List<CharSequence> subCandidates = new LinkedList();
      if (sub.complete(arg, arg.length(), subCandidates) == -1) {
        return -1;
      }
      if (subCandidates.size() == 0) {
        return -1;
      }
    }
    int ret = completer.complete(list.getCursorArgument(), argpos, candidates);
    if (ret == -1) {
      return -1;
    }
    int pos = ret + list.getBufferPosition() - argpos;
    if ((cursor != buffer.length()) && (delim.isDelimiter(buffer, cursor))) {
      for (int i = 0; i < candidates.size(); i++)
      {
        CharSequence val = (CharSequence)candidates.get(i);
        while ((val.length() > 0) && (delim.isDelimiter(val, val.length() - 1))) {
          val = val.subSequence(0, val.length() - 1);
        }
        candidates.set(i, val);
      }
    }
    Log.trace(new Object[] { "Completing ", buffer, " (pos=", Integer.valueOf(cursor), ") with: ", candidates, ": offset=", Integer.valueOf(pos) });
    
    return pos;
  }
  
  public static abstract interface ArgumentDelimiter
  {
    public abstract ArgumentCompleter.ArgumentList delimit(CharSequence paramCharSequence, int paramInt);
    
    public abstract boolean isDelimiter(CharSequence paramCharSequence, int paramInt);
  }
  
  public static abstract class AbstractArgumentDelimiter
    implements ArgumentCompleter.ArgumentDelimiter
  {
    private char[] quoteChars = { '\'', '"' };
    private char[] escapeChars = { '\\' };
    
    public void setQuoteChars(char[] chars)
    {
      this.quoteChars = chars;
    }
    
    public char[] getQuoteChars()
    {
      return this.quoteChars;
    }
    
    public void setEscapeChars(char[] chars)
    {
      this.escapeChars = chars;
    }
    
    public char[] getEscapeChars()
    {
      return this.escapeChars;
    }
    
    public ArgumentCompleter.ArgumentList delimit(CharSequence buffer, int cursor)
    {
      List<String> args = new LinkedList();
      StringBuilder arg = new StringBuilder();
      int argpos = -1;
      int bindex = -1;
      int quoteStart = -1;
      for (int i = 0; (buffer != null) && (i < buffer.length()); i++)
      {
        if (i == cursor)
        {
          bindex = args.size();
          
          argpos = arg.length();
        }
        if ((quoteStart < 0) && (isQuoteChar(buffer, i))) {
          quoteStart = i;
        } else if (quoteStart >= 0)
        {
          if ((buffer.charAt(quoteStart) == buffer.charAt(i)) && (!isEscaped(buffer, i)))
          {
            args.add(arg.toString());
            arg.setLength(0);
            quoteStart = -1;
          }
          else if (!isEscapeChar(buffer, i))
          {
            arg.append(buffer.charAt(i));
          }
        }
        else if (isDelimiter(buffer, i))
        {
          if (arg.length() > 0)
          {
            args.add(arg.toString());
            arg.setLength(0);
          }
        }
        else if (!isEscapeChar(buffer, i)) {
          arg.append(buffer.charAt(i));
        }
      }
      if (cursor == buffer.length())
      {
        bindex = args.size();
        
        argpos = arg.length();
      }
      if (arg.length() > 0) {
        args.add(arg.toString());
      }
      return new ArgumentCompleter.ArgumentList((String[])args.toArray(new String[args.size()]), bindex, argpos, cursor);
    }
    
    public boolean isDelimiter(CharSequence buffer, int pos)
    {
      return (!isQuoted(buffer, pos)) && (!isEscaped(buffer, pos)) && (isDelimiterChar(buffer, pos));
    }
    
    public boolean isQuoted(CharSequence buffer, int pos)
    {
      return false;
    }
    
    public boolean isQuoteChar(CharSequence buffer, int pos)
    {
      if (pos < 0) {
        return false;
      }
      for (int i = 0; (this.quoteChars != null) && (i < this.quoteChars.length); i++) {
        if (buffer.charAt(pos) == this.quoteChars[i]) {
          return !isEscaped(buffer, pos);
        }
      }
      return false;
    }
    
    public boolean isEscapeChar(CharSequence buffer, int pos)
    {
      if (pos < 0) {
        return false;
      }
      for (int i = 0; (this.escapeChars != null) && (i < this.escapeChars.length); i++) {
        if (buffer.charAt(pos) == this.escapeChars[i]) {
          return !isEscaped(buffer, pos);
        }
      }
      return false;
    }
    
    public boolean isEscaped(CharSequence buffer, int pos)
    {
      if (pos <= 0) {
        return false;
      }
      return isEscapeChar(buffer, pos - 1);
    }
    
    public abstract boolean isDelimiterChar(CharSequence paramCharSequence, int paramInt);
  }
  
  public static class WhitespaceArgumentDelimiter
    extends ArgumentCompleter.AbstractArgumentDelimiter
  {
    public boolean isDelimiterChar(CharSequence buffer, int pos)
    {
      return Character.isWhitespace(buffer.charAt(pos));
    }
  }
  
  public static class ArgumentList
  {
    private String[] arguments;
    private int cursorArgumentIndex;
    private int argumentPosition;
    private int bufferPosition;
    
    public ArgumentList(String[] arguments, int cursorArgumentIndex, int argumentPosition, int bufferPosition)
    {
      this.arguments = ((String[])Preconditions.checkNotNull(arguments));
      this.cursorArgumentIndex = cursorArgumentIndex;
      this.argumentPosition = argumentPosition;
      this.bufferPosition = bufferPosition;
    }
    
    public void setCursorArgumentIndex(int i)
    {
      this.cursorArgumentIndex = i;
    }
    
    public int getCursorArgumentIndex()
    {
      return this.cursorArgumentIndex;
    }
    
    public String getCursorArgument()
    {
      if ((this.cursorArgumentIndex < 0) || (this.cursorArgumentIndex >= this.arguments.length)) {
        return null;
      }
      return this.arguments[this.cursorArgumentIndex];
    }
    
    public void setArgumentPosition(int pos)
    {
      this.argumentPosition = pos;
    }
    
    public int getArgumentPosition()
    {
      return this.argumentPosition;
    }
    
    public void setArguments(String[] arguments)
    {
      this.arguments = arguments;
    }
    
    public String[] getArguments()
    {
      return this.arguments;
    }
    
    public void setBufferPosition(int pos)
    {
      this.bufferPosition = pos;
    }
    
    public int getBufferPosition()
    {
      return this.bufferPosition;
    }
  }
}
