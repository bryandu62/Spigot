package com.sun.jna.platform.mac;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.platform.FileUtils;
import com.sun.jna.ptr.PointerByReference;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MacFileUtils
  extends FileUtils
{
  public boolean hasTrash()
  {
    return true;
  }
  
  public static abstract interface FileManager
    extends Library
  {
    public static final int kFSFileOperationDefaultOptions = 0;
    public static final int kFSFileOperationsOverwrite = 1;
    public static final int kFSFileOperationsSkipSourcePermissionErrors = 2;
    public static final int kFSFileOperationsDoNotMoveAcrossVolumes = 4;
    public static final int kFSFileOperationsSkipPreflight = 8;
    public static final FileManager INSTANCE = (FileManager)Native.loadLibrary("CoreServices", FileManager.class);
    
    public abstract int FSPathMoveObjectToTrashSync(String paramString, PointerByReference paramPointerByReference, int paramInt);
  }
  
  public void moveToTrash(File[] files)
    throws IOException
  {
    File home = new File(System.getProperty("user.home"));
    File trash = new File(home, ".Trash");
    if (!trash.exists()) {
      throw new IOException("The Trash was not found in its expected location (" + trash + ")");
    }
    List<File> failed = new ArrayList();
    for (int i = 0; i < files.length; i++)
    {
      File src = files[i];
      if (FileManager.INSTANCE.FSPathMoveObjectToTrashSync(src.getAbsolutePath(), null, 0) != 0) {
        failed.add(src);
      }
    }
    if (failed.size() > 0) {
      throw new IOException("The following files could not be trashed: " + failed);
    }
  }
}
