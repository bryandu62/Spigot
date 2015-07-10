package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.Iterator;

public abstract class IterateBlock
{
  DatabaseMetaData.IteratorWithCleanup iteratorWithCleanup;
  Iterator javaIterator;
  boolean stopIterating = false;
  
  IterateBlock(DatabaseMetaData.IteratorWithCleanup i)
  {
    this.iteratorWithCleanup = i;
    this.javaIterator = null;
  }
  
  IterateBlock(Iterator i)
  {
    this.javaIterator = i;
    this.iteratorWithCleanup = null;
  }
  
  public void doForAll()
    throws SQLException
  {
    if (this.iteratorWithCleanup != null) {
      try
      {
        while (this.iteratorWithCleanup.hasNext())
        {
          forEach(this.iteratorWithCleanup.next());
          if (this.stopIterating) {
            break;
          }
        }
      }
      finally
      {
        this.iteratorWithCleanup.close();
      }
    } else {
      while (this.javaIterator.hasNext())
      {
        forEach(this.javaIterator.next());
        if (this.stopIterating) {
          break;
        }
      }
    }
  }
  
  abstract void forEach(Object paramObject)
    throws SQLException;
  
  public final boolean fullIteration()
  {
    return !this.stopIterating;
  }
}
