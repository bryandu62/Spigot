package com.sun.jna.platform.win32;

import com.sun.jna.platform.FileMonitor;
import com.sun.jna.platform.FileMonitor.FileEvent;
import com.sun.jna.ptr.IntByReference;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class W32FileMonitor
  extends FileMonitor
{
  private static final int BUFFER_SIZE = 4096;
  private Thread watcher;
  private WinNT.HANDLE port;
  
  private class FileInfo
  {
    public final File file;
    public final WinNT.HANDLE handle;
    public final int notifyMask;
    public final boolean recursive;
    public final WinNT.FILE_NOTIFY_INFORMATION info = new WinNT.FILE_NOTIFY_INFORMATION(4096);
    public final IntByReference infoLength = new IntByReference();
    public final WinBase.OVERLAPPED overlapped = new WinBase.OVERLAPPED();
    
    public FileInfo(File f, WinNT.HANDLE h, int mask, boolean recurse)
    {
      this.file = f;
      this.handle = h;
      this.notifyMask = mask;
      this.recursive = recurse;
    }
  }
  
  private final Map<File, FileInfo> fileMap = new HashMap();
  private final Map<WinNT.HANDLE, FileInfo> handleMap = new HashMap();
  private boolean disposing = false;
  private static int watcherThreadID;
  
  private void handleChanges(FileInfo finfo)
    throws IOException
  {
    Kernel32 klib = Kernel32.INSTANCE;
    WinNT.FILE_NOTIFY_INFORMATION fni = finfo.info;
    
    fni.read();
    do
    {
      FileMonitor.FileEvent event = null;
      File file = new File(finfo.file, fni.getFilename());
      switch (fni.Action)
      {
      case 0: 
        break;
      case 3: 
        event = new FileMonitor.FileEvent(this, file, 4);
        break;
      case 1: 
        event = new FileMonitor.FileEvent(this, file, 1);
        break;
      case 2: 
        event = new FileMonitor.FileEvent(this, file, 2);
        break;
      case 4: 
        event = new FileMonitor.FileEvent(this, file, 16);
        break;
      case 5: 
        event = new FileMonitor.FileEvent(this, file, 32);
        break;
      default: 
        System.err.println("Unrecognized file action '" + fni.Action + "'");
      }
      if (event != null) {
        notify(event);
      }
      fni = fni.next();
    } while (fni != null);
    if (!finfo.file.exists())
    {
      unwatch(finfo.file);
      return;
    }
    if (!klib.ReadDirectoryChangesW(finfo.handle, finfo.info, finfo.info.size(), finfo.recursive, finfo.notifyMask, finfo.infoLength, finfo.overlapped, null)) {
      if (!this.disposing)
      {
        int err = klib.GetLastError();
        throw new IOException("ReadDirectoryChangesW failed on " + finfo.file + ": '" + Kernel32Util.formatMessageFromLastErrorCode(err) + "' (" + err + ")");
      }
    }
  }
  
  /* Error */
  private FileInfo waitForChange()
  {
    // Byte code:
    //   0: getstatic 80	com/sun/jna/platform/win32/Kernel32:INSTANCE	Lcom/sun/jna/platform/win32/Kernel32;
    //   3: astore_1
    //   4: new 208	com/sun/jna/ptr/IntByReference
    //   7: dup
    //   8: invokespecial 209	com/sun/jna/ptr/IntByReference:<init>	()V
    //   11: astore_2
    //   12: new 21	com/sun/jna/platform/win32/BaseTSD$ULONG_PTRByReference
    //   15: dup
    //   16: invokespecial 210	com/sun/jna/platform/win32/BaseTSD$ULONG_PTRByReference:<init>	()V
    //   19: astore_3
    //   20: new 212	com/sun/jna/ptr/PointerByReference
    //   23: dup
    //   24: invokespecial 213	com/sun/jna/ptr/PointerByReference:<init>	()V
    //   27: astore 4
    //   29: aload_1
    //   30: aload_0
    //   31: getfield 215	com/sun/jna/platform/win32/W32FileMonitor:port	Lcom/sun/jna/platform/win32/WinNT$HANDLE;
    //   34: aload_2
    //   35: aload_3
    //   36: aload 4
    //   38: iconst_m1
    //   39: invokeinterface 219 6 0
    //   44: pop
    //   45: aload_0
    //   46: dup
    //   47: astore 5
    //   49: monitorenter
    //   50: aload_0
    //   51: getfield 66	com/sun/jna/platform/win32/W32FileMonitor:handleMap	Ljava/util/Map;
    //   54: aload_3
    //   55: invokevirtual 223	com/sun/jna/platform/win32/BaseTSD$ULONG_PTRByReference:getValue	()Lcom/sun/jna/platform/win32/BaseTSD$ULONG_PTR;
    //   58: invokeinterface 229 2 0
    //   63: checkcast 7	com/sun/jna/platform/win32/W32FileMonitor$FileInfo
    //   66: aload 5
    //   68: monitorexit
    //   69: areturn
    //   70: astore 6
    //   72: aload 5
    //   74: monitorexit
    //   75: aload 6
    //   77: athrow
    // Line number table:
    //   Java source line #113	-> byte code offset #0
    //   Java source line #114	-> byte code offset #4
    //   Java source line #115	-> byte code offset #12
    //   Java source line #116	-> byte code offset #20
    //   Java source line #117	-> byte code offset #29
    //   Java source line #119	-> byte code offset #45
    //   Java source line #120	-> byte code offset #50
    //   Java source line #121	-> byte code offset #70
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	78	0	this	W32FileMonitor
    //   3	27	1	klib	Kernel32
    //   11	24	2	rcount	IntByReference
    //   19	36	3	rkey	BaseTSD.ULONG_PTRByReference
    //   27	10	4	roverlap	com.sun.jna.ptr.PointerByReference
    //   47	26	5	Ljava/lang/Object;	Object
    //   70	6	6	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   50	69	70	finally
    //   70	75	70	finally
  }
  
  private int convertMask(int mask)
  {
    int result = 0;
    if ((mask & 0x1) != 0) {
      result |= 0x40;
    }
    if ((mask & 0x2) != 0) {
      result |= 0x3;
    }
    if ((mask & 0x4) != 0) {
      result |= 0x10;
    }
    if ((mask & 0x30) != 0) {
      result |= 0x3;
    }
    if ((mask & 0x40) != 0) {
      result |= 0x8;
    }
    if ((mask & 0x8) != 0) {
      result |= 0x20;
    }
    if ((mask & 0x80) != 0) {
      result |= 0x4;
    }
    if ((mask & 0x100) != 0) {
      result |= 0x100;
    }
    return result;
  }
  
  protected synchronized void watch(File file, int eventMask, boolean recursive)
    throws IOException
  {
    File dir = file;
    if (!dir.isDirectory())
    {
      recursive = false;
      dir = file.getParentFile();
    }
    while ((dir != null) && (!dir.exists()))
    {
      recursive = true;
      dir = dir.getParentFile();
    }
    if (dir == null) {
      throw new FileNotFoundException("No ancestor found for " + file);
    }
    Kernel32 klib = Kernel32.INSTANCE;
    int mask = 7;
    
    int flags = 1107296256;
    
    WinNT.HANDLE handle = klib.CreateFile(file.getAbsolutePath(), 1, mask, null, 3, flags, null);
    if (WinBase.INVALID_HANDLE_VALUE.equals(handle)) {
      throw new IOException("Unable to open " + file + " (" + klib.GetLastError() + ")");
    }
    int notifyMask = convertMask(eventMask);
    FileInfo finfo = new FileInfo(file, handle, notifyMask, recursive);
    this.fileMap.put(file, finfo);
    this.handleMap.put(handle, finfo);
    
    this.port = klib.CreateIoCompletionPort(handle, this.port, handle.getPointer(), 0);
    if (WinBase.INVALID_HANDLE_VALUE.equals(this.port)) {
      throw new IOException("Unable to create/use I/O Completion port for " + file + " (" + klib.GetLastError() + ")");
    }
    if (!klib.ReadDirectoryChangesW(handle, finfo.info, finfo.info.size(), recursive, notifyMask, finfo.infoLength, finfo.overlapped, null))
    {
      int err = klib.GetLastError();
      throw new IOException("ReadDirectoryChangesW failed on " + finfo.file + ", handle " + handle + ": '" + Kernel32Util.formatMessageFromLastErrorCode(err) + "' (" + err + ")");
    }
    if (this.watcher == null)
    {
      this.watcher = new Thread("W32 File Monitor-" + watcherThreadID++)
      {
        public void run()
        {
          for (;;)
          {
            W32FileMonitor.FileInfo finfo = W32FileMonitor.this.waitForChange();
            if (finfo == null) {
              synchronized (W32FileMonitor.this)
              {
                if (W32FileMonitor.this.fileMap.isEmpty())
                {
                  W32FileMonitor.this.watcher = null;
                  break;
                }
              }
            } else {
              try
              {
                W32FileMonitor.this.handleChanges(finfo);
              }
              catch (IOException e)
              {
                e.printStackTrace();
              }
            }
          }
        }
      };
      this.watcher.setDaemon(true);
      this.watcher.start();
    }
  }
  
  protected synchronized void unwatch(File file)
  {
    FileInfo finfo = (FileInfo)this.fileMap.remove(file);
    if (finfo != null)
    {
      this.handleMap.remove(finfo.handle);
      Kernel32 klib = Kernel32.INSTANCE;
      
      klib.CloseHandle(finfo.handle);
    }
  }
  
  public synchronized void dispose()
  {
    this.disposing = true;
    
    int i = 0;
    for (Object[] keys = this.fileMap.keySet().toArray(); !this.fileMap.isEmpty();) {
      unwatch((File)keys[(i++)]);
    }
    Kernel32 klib = Kernel32.INSTANCE;
    klib.PostQueuedCompletionStatus(this.port, 0, null, null);
    klib.CloseHandle(this.port);
    this.port = null;
    this.watcher = null;
  }
}
