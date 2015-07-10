package io.netty.channel;

import io.netty.util.ReferenceCounted;
import java.net.SocketAddress;

public abstract interface AddressedEnvelope<M, A extends SocketAddress>
  extends ReferenceCounted
{
  public abstract M content();
  
  public abstract A sender();
  
  public abstract A recipient();
}
