package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.base.Ascii;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

public abstract class CharSource
  implements InputSupplier<Reader>
{
  public abstract Reader openStream()
    throws IOException;
  
  @Deprecated
  public final Reader getInput()
    throws IOException
  {
    return openStream();
  }
  
  public BufferedReader openBufferedStream()
    throws IOException
  {
    Reader reader = openStream();
    return (reader instanceof BufferedReader) ? (BufferedReader)reader : new BufferedReader(reader);
  }
  
  public long copyTo(Appendable appendable)
    throws IOException
  {
    Preconditions.checkNotNull(appendable);
    
    Closer closer = Closer.create();
    try
    {
      Reader reader = (Reader)closer.register(openStream());
      return CharStreams.copy(reader, appendable);
    }
    catch (Throwable e)
    {
      throw closer.rethrow(e);
    }
    finally
    {
      closer.close();
    }
  }
  
  public long copyTo(CharSink sink)
    throws IOException
  {
    Preconditions.checkNotNull(sink);
    
    Closer closer = Closer.create();
    try
    {
      Reader reader = (Reader)closer.register(openStream());
      Writer writer = (Writer)closer.register(sink.openStream());
      return CharStreams.copy(reader, writer);
    }
    catch (Throwable e)
    {
      throw closer.rethrow(e);
    }
    finally
    {
      closer.close();
    }
  }
  
  public String read()
    throws IOException
  {
    Closer closer = Closer.create();
    try
    {
      Reader reader = (Reader)closer.register(openStream());
      return CharStreams.toString(reader);
    }
    catch (Throwable e)
    {
      throw closer.rethrow(e);
    }
    finally
    {
      closer.close();
    }
  }
  
  @Nullable
  public String readFirstLine()
    throws IOException
  {
    Closer closer = Closer.create();
    try
    {
      BufferedReader reader = (BufferedReader)closer.register(openBufferedStream());
      return reader.readLine();
    }
    catch (Throwable e)
    {
      throw closer.rethrow(e);
    }
    finally
    {
      closer.close();
    }
  }
  
  public ImmutableList<String> readLines()
    throws IOException
  {
    Closer closer = Closer.create();
    try
    {
      BufferedReader reader = (BufferedReader)closer.register(openBufferedStream());
      List<String> result = Lists.newArrayList();
      String line;
      while ((line = reader.readLine()) != null) {
        result.add(line);
      }
      return ImmutableList.copyOf(result);
    }
    catch (Throwable e)
    {
      throw closer.rethrow(e);
    }
    finally
    {
      closer.close();
    }
  }
  
  @Beta
  public <T> T readLines(LineProcessor<T> processor)
    throws IOException
  {
    Preconditions.checkNotNull(processor);
    
    Closer closer = Closer.create();
    try
    {
      Reader reader = (Reader)closer.register(openStream());
      return (T)CharStreams.readLines(reader, processor);
    }
    catch (Throwable e)
    {
      throw closer.rethrow(e);
    }
    finally
    {
      closer.close();
    }
  }
  
  public boolean isEmpty()
    throws IOException
  {
    Closer closer = Closer.create();
    try
    {
      Reader reader = (Reader)closer.register(openStream());
      return reader.read() == -1;
    }
    catch (Throwable e)
    {
      throw closer.rethrow(e);
    }
    finally
    {
      closer.close();
    }
  }
  
  public static CharSource concat(Iterable<? extends CharSource> sources)
  {
    return new ConcatenatedCharSource(sources);
  }
  
  public static CharSource concat(Iterator<? extends CharSource> sources)
  {
    return concat(ImmutableList.copyOf(sources));
  }
  
  public static CharSource concat(CharSource... sources)
  {
    return concat(ImmutableList.copyOf(sources));
  }
  
  public static CharSource wrap(CharSequence charSequence)
  {
    return new CharSequenceCharSource(charSequence);
  }
  
  public static CharSource empty()
  {
    return EmptyCharSource.INSTANCE;
  }
  
  private static class CharSequenceCharSource
    extends CharSource
  {
    private static final Splitter LINE_SPLITTER = Splitter.on(Pattern.compile("\r\n|\n|\r"));
    private final CharSequence seq;
    
    protected CharSequenceCharSource(CharSequence seq)
    {
      this.seq = ((CharSequence)Preconditions.checkNotNull(seq));
    }
    
    public Reader openStream()
    {
      return new CharSequenceReader(this.seq);
    }
    
    public String read()
    {
      return this.seq.toString();
    }
    
    public boolean isEmpty()
    {
      return this.seq.length() == 0;
    }
    
    private Iterable<String> lines()
    {
      new Iterable()
      {
        public Iterator<String> iterator()
        {
          new AbstractIterator()
          {
            Iterator<String> lines = CharSource.CharSequenceCharSource.LINE_SPLITTER.split(CharSource.CharSequenceCharSource.this.seq).iterator();
            
            protected String computeNext()
            {
              if (this.lines.hasNext())
              {
                String next = (String)this.lines.next();
                if ((this.lines.hasNext()) || (!next.isEmpty())) {
                  return next;
                }
              }
              return (String)endOfData();
            }
          };
        }
      };
    }
    
    public String readFirstLine()
    {
      Iterator<String> lines = lines().iterator();
      return lines.hasNext() ? (String)lines.next() : null;
    }
    
    public ImmutableList<String> readLines()
    {
      return ImmutableList.copyOf(lines());
    }
    
    public <T> T readLines(LineProcessor<T> processor)
      throws IOException
    {
      for (String line : lines()) {
        if (!processor.processLine(line)) {
          break;
        }
      }
      return (T)processor.getResult();
    }
    
    public String toString()
    {
      return "CharSource.wrap(" + Ascii.truncate(this.seq, 30, "...") + ")";
    }
  }
  
  private static final class EmptyCharSource
    extends CharSource.CharSequenceCharSource
  {
    private static final EmptyCharSource INSTANCE = new EmptyCharSource();
    
    private EmptyCharSource()
    {
      super();
    }
    
    public String toString()
    {
      return "CharSource.empty()";
    }
  }
  
  private static final class ConcatenatedCharSource
    extends CharSource
  {
    private final Iterable<? extends CharSource> sources;
    
    ConcatenatedCharSource(Iterable<? extends CharSource> sources)
    {
      this.sources = ((Iterable)Preconditions.checkNotNull(sources));
    }
    
    public Reader openStream()
      throws IOException
    {
      return new MultiReader(this.sources.iterator());
    }
    
    public boolean isEmpty()
      throws IOException
    {
      for (CharSource source : this.sources) {
        if (!source.isEmpty()) {
          return false;
        }
      }
      return true;
    }
    
    public String toString()
    {
      return "CharSource.concat(" + this.sources + ")";
    }
  }
}
