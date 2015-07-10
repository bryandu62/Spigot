package com.avaje.ebeaninternal.server.lib.resource;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DirectoryFinder
{
  private static final Logger logger = Logger.getLogger(DirectoryFinder.class.getName());
  
  public static File find(File startDir, String match, int maxDepth)
  {
    String matchSub = null;
    int slashPos = match.indexOf('/');
    if (slashPos > -1)
    {
      matchSub = match.substring(slashPos + 1);
      match = match.substring(0, slashPos);
    }
    File found = find(startDir, match, matchSub, 0, maxDepth);
    if ((found != null) && (matchSub != null)) {
      return new File(found, matchSub);
    }
    return found;
  }
  
  private static File find(File dir, String match, String matchSub, int depth, int maxDepth)
  {
    if (dir == null)
    {
      String curDir = System.getProperty("user.dir");
      dir = new File(curDir);
    }
    if (dir.exists())
    {
      File[] list = dir.listFiles();
      if (list != null)
      {
        for (int i = 0; i < list.length; i++) {
          if (isMatch(list[i], match, matchSub)) {
            return list[i];
          }
        }
        if (depth < maxDepth) {
          for (int i = 0; i < list.length; i++) {
            if (list[i].isDirectory())
            {
              File found = find(list[i], match, matchSub, depth + 1, maxDepth);
              if (found != null) {
                return found;
              }
            }
          }
        }
      }
    }
    return null;
  }
  
  private static boolean isMatch(File f, String match, String matchSub)
  {
    if (f == null) {
      return false;
    }
    if (!f.isDirectory()) {
      return false;
    }
    if (!f.getName().equalsIgnoreCase(match)) {
      return false;
    }
    if (matchSub == null) {
      return true;
    }
    File sub = new File(f, matchSub);
    if (logger.isLoggable(Level.FINEST)) {
      logger.finest("search; " + f.getPath());
    }
    return sub.exists();
  }
}
