package com.avaje.ebeaninternal.server.cluster;

import java.util.ArrayList;
import java.util.List;

public class BinaryMessageList
{
  ArrayList<BinaryMessage> list = new ArrayList();
  
  public void add(BinaryMessage msg)
  {
    this.list.add(msg);
  }
  
  public List<BinaryMessage> getList()
  {
    return this.list;
  }
}
