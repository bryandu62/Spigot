package com.avaje.ebeaninternal.server.text.json;

import com.avaje.ebeaninternal.server.util.ArrayStack;

public class PathStack
  extends ArrayStack<String>
{
  public String peekFullPath(String key)
  {
    String prefix = (String)peekWithNull();
    if (prefix != null) {
      return prefix + "." + key;
    }
    return key;
  }
  
  public void pushPathKey(String key)
  {
    String prefix = (String)peekWithNull();
    if (prefix != null) {
      key = prefix + "." + key;
    }
    push(key);
  }
}
