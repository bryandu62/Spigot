package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Beta
public final class CharStreams
{
  private static final int BUF_SIZE = 2048;
  
  @Deprecated
  public static InputSupplier<StringReader> newReaderSupplier(String value)
  {
    return asInputSupplier(CharSource.wrap(value));
  }
  
  @Deprecated
  public static InputSupplier<InputStreamReader> newReaderSupplier(InputSupplier<? extends InputStream> in, Charset charset)
  {
    return asInputSupplier(ByteStreams.asByteSource(in).asCharSource(charset));
  }
  
  @Deprecated
  public static OutputSupplier<OutputStreamWriter> newWriterSupplier(OutputSupplier<? extends OutputStream> out, Charset charset)
  {
    return asOutputSupplier(ByteStreams.asByteSink(out).asCharSink(charset));
  }
  
  @Deprecated
  public static <W extends Appendable,  extends Closeable> void write(CharSequence from, OutputSupplier<W> to)
    throws IOException
  {
    asCharSink(to).write(from);
  }
  
  @Deprecated
  public static <R extends Readable,  extends Closeable, W extends Appendable,  extends Closeable> long copy(InputSupplier<R> from, OutputSupplier<W> to)
    throws IOException
  {
    return asCharSource(from).copyTo(asCharSink(to));
  }
  
  @Deprecated
  public static <R extends Readable,  extends Closeable> long copy(InputSupplier<R> from, Appendable to)
    throws IOException
  {
    return asCharSource(from).copyTo(to);
  }
  
  public static long copy(Readable from, Appendable to)
    throws IOException
  {
    Preconditions.checkNotNull(from);
    Preconditions.checkNotNull(to);
    CharBuffer buf = CharBuffer.allocate(2048);
    long total = 0L;
    while (from.read(buf) != -1)
    {
      buf.flip();
      to.append(buf);
      total += buf.remaining();
      buf.clear();
    }
    return total;
  }
  
  public static String toString(Readable r)
    throws IOException
  {
    return toStringBuilder(r).toString();
  }
  
  @Deprecated
  public static <R extends Readable,  extends Closeable> String toString(InputSupplier<R> supplier)
    throws IOException
  {
    return asCharSource(supplier).read();
  }
  
  private static StringBuilder toStringBuilder(Readable r)
    throws IOException
  {
    StringBuilder sb = new StringBuilder();
    copy(r, sb);
    return sb;
  }
  
  @Deprecated
  public static <R extends Readable,  extends Closeable> String readFirstLine(InputSupplier<R> supplier)
    throws IOException
  {
    return asCharSource(supplier).readFirstLine();
  }
  
  @Deprecated
  public static <R extends Readable,  extends Closeable> List<String> readLines(InputSupplier<R> supplier)
    throws IOException
  {
    Closer closer = Closer.create();
    try
    {
      R r = (Readable)closer.register((Closeable)supplier.getInput());
      return readLines(r);
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
  
  public static List<String> readLines(Readable r)
    throws IOException
  {
    List<String> result = new ArrayList();
    LineReader lineReader = new LineReader(r);
    String line;
    while ((line = lineReader.readLine()) != null) {
      result.add(line);
    }
    return result;
  }
  
  public static <T> T readLines(Readable readable, LineProcessor<T> processor)
    throws IOException
  {
    Preconditions.checkNotNull(readable);
    Preconditions.checkNotNull(processor);
    
    LineReader lineReader = new LineReader(readable);
    String line;
    while ((line = lineReader.readLine()) != null) {
      if (!processor.processLine(line)) {
        break;
      }
    }
    return (T)processor.getResult();
  }
  
  @Deprecated
  public static <R extends Readable,  extends Closeable, T> T readLines(InputSupplier<R> supplier, LineProcessor<T> callback)
    throws IOException
  {
    Preconditions.checkNotNull(supplier);
    Preconditions.checkNotNull(callback);
    
    Closer closer = Closer.create();
    try
    {
      R r = (Readable)closer.register((Closeable)supplier.getInput());
      return (T)readLines(r, callback);
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
  
  @Deprecated
  public static InputSupplier<Reader> join(Iterable<? extends InputSupplier<? extends Reader>> suppliers)
  {
    Preconditions.checkNotNull(suppliers);
    Iterable<CharSource> sources = Iterables.transform(suppliers, new Function()
    {
      public CharSource apply(InputSupplier<? extends Reader> input)
      {
        return CharStreams.asCharSource(input);
      }
    });
    return asInputSupplier(CharSource.concat(sources));
  }
  
  @Deprecated
  public static InputSupplier<Reader> join(InputSupplier<? extends Reader>... suppliers)
  {
    return join(Arrays.asList(suppliers));
  }
  
  public static void skipFully(Reader reader, long n)
    throws IOException
  {
    Preconditions.checkNotNull(reader);
    while (n > 0L)
    {
      long amt = reader.skip(n);
      if (amt == 0L)
      {
        if (reader.read() == -1) {
          throw new EOFException();
        }
        n -= 1L;
      }
      else
      {
        n -= amt;
      }
    }
  }
  
  public static Writer nullWriter()
  {
    return NullWriter.INSTANCE;
  }
  
  private static final class NullWriter
    extends Writer
  {
    private static final NullWriter INSTANCE = new NullWriter();
    
    public void write(int c) {}
    
    public void write(char[] cbuf)
    {
      Preconditions.checkNotNull(cbuf);
    }
    
    public void write(char[] cbuf, int off, int len)
    {
      Preconditions.checkPositionIndexes(off, off + len, cbuf.length);
    }
    
    public void write(String str)
    {
      Preconditions.checkNotNull(str);
    }
    
    public void write(String str, int off, int len)
    {
      Preconditions.checkPositionIndexes(off, off + len, str.length());
    }
    
    public Writer append(CharSequence csq)
    {
      Preconditions.checkNotNull(csq);
      return this;
    }
    
    public Writer append(CharSequence csq, int start, int end)
    {
      Preconditions.checkPositionIndexes(start, end, csq.length());
      return this;
    }
    
    public Writer append(char c)
    {
      return this;
    }
    
    public void flush() {}
    
    public void close() {}
    
    public String toString()
    {
      return "CharStreams.nullWriter()";
    }
  }
  
  public static Writer asWriter(Appendable target)
  {
    if ((target instanceof Writer)) {
      return (Writer)target;
    }
    return new AppendableWriter(target);
  }
  
  static Reader asReader(Readable readable)
  {
    Preconditions.checkNotNull(readable);
    if ((readable instanceof Reader)) {
      return (Reader)readable;
    }
    new Reader()
    {
      public int read(char[] cbuf, int off, int len)
        throws IOException
      {
        return read(CharBuffer.wrap(cbuf, off, len));
      }
      
      public int read(CharBuffer target)
        throws IOException
      {
        return this.val$readable.read(target);
      }
      
      public void close()
        throws IOException
      {
        if ((this.val$readable instanceof Closeable)) {
          ((Closeable)this.val$readable).close();
        }
      }
    };
  }
  
  @Deprecated
  public static CharSource asCharSource(InputSupplier<? extends Readable> supplier)
  {
    Preconditions.checkNotNull(supplier);
    new CharSource()
    {
      public Reader openStream()
        throws IOException
      {
        return CharStreams.asReader((Readable)this.val$supplier.getInput());
      }
      
      public String toString()
      {
        return "CharStreams.asCharSource(" + this.val$supplier + ")";
      }
    };
  }
  
  @Deprecated
  public static CharSink asCharSink(OutputSupplier<? extends Appendable> supplier)
  {
    Preconditions.checkNotNull(supplier);
    new CharSink()
    {
      public Writer openStream()
        throws IOException
      {
        return CharStreams.asWriter((Appendable)this.val$supplier.getOutput());
      }
      
      public String toString()
      {
        return "CharStreams.asCharSink(" + this.val$supplier + ")";
      }
    };
  }
  
  static <R extends Reader> InputSupplier<R> asInputSupplier(CharSource source)
  {
    return (InputSupplier)Preconditions.checkNotNull(source);
  }
  
  static <W extends Writer> OutputSupplier<W> asOutputSupplier(CharSink sink)
  {
    return (OutputSupplier)Preconditions.checkNotNull(sink);
  }
}
