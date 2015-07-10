package com.avaje.ebeaninternal.server.cluster.mcast;

import com.avaje.ebeaninternal.server.cluster.BinaryMessageList;
import java.io.IOException;

public abstract interface Message
{
  public abstract void writeBinaryMessage(BinaryMessageList paramBinaryMessageList)
    throws IOException;
  
  public abstract boolean isControlMessage();
  
  public abstract String getToHostPort();
}
