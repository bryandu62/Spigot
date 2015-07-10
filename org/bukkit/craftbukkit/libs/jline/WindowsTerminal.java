package org.bukkit.craftbukkit.libs.jline;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import org.bukkit.craftbukkit.libs.jline.internal.Configuration;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.fusesource.jansi.internal.Kernel32;
import org.fusesource.jansi.internal.Kernel32.INPUT_RECORD;
import org.fusesource.jansi.internal.Kernel32.KEY_EVENT_RECORD;
import org.fusesource.jansi.internal.WindowsSupport;

public class WindowsTerminal
  extends TerminalSupport
{
  public static final String DIRECT_CONSOLE = WindowsTerminal.class.getName() + ".directConsole";
  public static final String ANSI = WindowsTerminal.class.getName() + ".ansi";
  private boolean directConsole;
  private int originalMode;
  
  public WindowsTerminal()
    throws Exception
  {
    super(true);
  }
  
  public void init()
    throws Exception
  {
    super.init();
    
    setAnsiSupported(Configuration.getBoolean(ANSI, true));
    
    setDirectConsole(Configuration.getBoolean(DIRECT_CONSOLE, true));
    
    this.originalMode = getConsoleMode();
    setConsoleMode(this.originalMode & (ConsoleMode.ENABLE_ECHO_INPUT.code ^ 0xFFFFFFFF));
    setEchoEnabled(false);
  }
  
  public void restore()
    throws Exception
  {
    setConsoleMode(this.originalMode);
    super.restore();
  }
  
  public int getWidth()
  {
    int w = getWindowsTerminalWidth();
    return w < 1 ? 80 : w;
  }
  
  public int getHeight()
  {
    int h = getWindowsTerminalHeight();
    return h < 1 ? 24 : h;
  }
  
  public void setEchoEnabled(boolean enabled)
  {
    if (enabled) {
      setConsoleMode(getConsoleMode() | ConsoleMode.ENABLE_ECHO_INPUT.code | ConsoleMode.ENABLE_LINE_INPUT.code | ConsoleMode.ENABLE_PROCESSED_INPUT.code | ConsoleMode.ENABLE_WINDOW_INPUT.code);
    } else {
      setConsoleMode(getConsoleMode() & ((ConsoleMode.ENABLE_LINE_INPUT.code | ConsoleMode.ENABLE_ECHO_INPUT.code | ConsoleMode.ENABLE_PROCESSED_INPUT.code | ConsoleMode.ENABLE_WINDOW_INPUT.code) ^ 0xFFFFFFFF));
    }
    super.setEchoEnabled(enabled);
  }
  
  public void setDirectConsole(boolean flag)
  {
    this.directConsole = flag;
    Log.debug(new Object[] { "Direct console: ", Boolean.valueOf(flag) });
  }
  
  public Boolean getDirectConsole()
  {
    return Boolean.valueOf(this.directConsole);
  }
  
  public InputStream wrapInIfNeeded(InputStream in)
    throws IOException
  {
    if ((this.directConsole) && (isSystemIn(in))) {
      new InputStream()
      {
        private byte[] buf = null;
        int bufIdx = 0;
        
        public int read()
          throws IOException
        {
          while ((this.buf == null) || (this.bufIdx == this.buf.length))
          {
            this.buf = WindowsTerminal.this.readConsoleInput();
            this.bufIdx = 0;
          }
          int c = this.buf[this.bufIdx] & 0xFF;
          this.bufIdx += 1;
          return c;
        }
      };
    }
    return super.wrapInIfNeeded(in);
  }
  
  protected boolean isSystemIn(InputStream in)
    throws IOException
  {
    if (in == null) {
      return false;
    }
    if (in == System.in) {
      return true;
    }
    if (((in instanceof FileInputStream)) && (((FileInputStream)in).getFD() == FileDescriptor.in)) {
      return true;
    }
    return false;
  }
  
  public String getOutputEncoding()
  {
    int codepage = getConsoleOutputCodepage();
    
    String charsetMS = "ms" + codepage;
    if (Charset.isSupported(charsetMS)) {
      return charsetMS;
    }
    String charsetCP = "cp" + codepage;
    if (Charset.isSupported(charsetCP)) {
      return charsetCP;
    }
    Log.debug(new Object[] { "can't figure out the Java Charset of this code page (" + codepage + ")..." });
    return super.getOutputEncoding();
  }
  
  private int getConsoleMode()
  {
    return WindowsSupport.getConsoleMode();
  }
  
  private void setConsoleMode(int mode)
  {
    WindowsSupport.setConsoleMode(mode);
  }
  
  private byte[] readConsoleInput()
  {
    Kernel32.INPUT_RECORD[] events = null;
    try
    {
      events = WindowsSupport.readConsoleInput(1);
    }
    catch (IOException e)
    {
      Log.debug(new Object[] { "read Windows console input error: ", e });
    }
    if (events == null) {
      return new byte[0];
    }
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < events.length; i++)
    {
      Kernel32.KEY_EVENT_RECORD keyEvent = events[i].keyEvent;
      if (keyEvent.keyDown)
      {
        if (keyEvent.uchar > 0)
        {
          int altState = Kernel32.KEY_EVENT_RECORD.LEFT_ALT_PRESSED | Kernel32.KEY_EVENT_RECORD.RIGHT_ALT_PRESSED;
          if (((keyEvent.uchar >= '@') && (keyEvent.uchar <= '_')) || ((keyEvent.uchar >= 'a') && (keyEvent.uchar <= 'z') && ((keyEvent.controlKeyState & altState) != 0))) {
            sb.append('\033');
          }
          sb.append(keyEvent.uchar);
        }
        else
        {
          String escapeSequence = null;
          switch (keyEvent.keyCode)
          {
          case 33: 
            escapeSequence = "\033[5~";
            break;
          case 34: 
            escapeSequence = "\033[6~";
            break;
          case 35: 
            escapeSequence = "\033[4~";
            break;
          case 36: 
            escapeSequence = "\033[1~";
            break;
          case 37: 
            escapeSequence = "\033[D";
            break;
          case 38: 
            escapeSequence = "\033[A";
            break;
          case 39: 
            escapeSequence = "\033[C";
            break;
          case 40: 
            escapeSequence = "\033[B";
            break;
          case 45: 
            escapeSequence = "\033[2~";
            break;
          case 46: 
            escapeSequence = "\033[3~";
            break;
          }
          if (escapeSequence != null) {
            for (int k = 0; k < keyEvent.repeatCount; k++) {
              sb.append(escapeSequence);
            }
          }
        }
      }
      else if ((keyEvent.keyCode == 18) && (keyEvent.uchar > 0)) {
        sb.append(keyEvent.uchar);
      }
    }
    return sb.toString().getBytes();
  }
  
  private int getConsoleOutputCodepage()
  {
    return Kernel32.GetConsoleOutputCP();
  }
  
  private int getWindowsTerminalWidth()
  {
    return WindowsSupport.getWindowsTerminalWidth();
  }
  
  private int getWindowsTerminalHeight()
  {
    return WindowsSupport.getWindowsTerminalHeight();
  }
  
  public static enum ConsoleMode
  {
    ENABLE_LINE_INPUT(2),  ENABLE_ECHO_INPUT(4),  ENABLE_PROCESSED_INPUT(1),  ENABLE_WINDOW_INPUT(8),  ENABLE_MOUSE_INPUT(16),  ENABLE_PROCESSED_OUTPUT(1),  ENABLE_WRAP_AT_EOL_OUTPUT(2);
    
    public final int code;
    
    private ConsoleMode(int code)
    {
      this.code = code;
    }
  }
}
