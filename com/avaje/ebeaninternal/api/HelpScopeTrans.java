package com.avaje.ebeaninternal.api;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.TxScope;

public class HelpScopeTrans
{
  public static ScopeTrans createScopeTrans(TxScope txScope)
  {
    EbeanServer server = Ebean.getServer(txScope.getServerName());
    SpiEbeanServer iserver = (SpiEbeanServer)server;
    return iserver.createScopeTrans(txScope);
  }
  
  public static void onExitScopeTrans(Object returnOrThrowable, int opCode, ScopeTrans scopeTrans)
  {
    scopeTrans.onExit(returnOrThrowable, opCode);
  }
}
