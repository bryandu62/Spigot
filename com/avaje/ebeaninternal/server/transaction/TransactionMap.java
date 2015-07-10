package com.avaje.ebeaninternal.server.transaction;

import com.avaje.ebeaninternal.api.SpiTransaction;
import java.util.HashMap;
import javax.persistence.PersistenceException;

public class TransactionMap
{
  private HashMap<String, State> map;
  
  public TransactionMap()
  {
    this.map = new HashMap();
  }
  
  public String toString()
  {
    return this.map.toString();
  }
  
  public boolean isEmpty()
  {
    return this.map.isEmpty();
  }
  
  public State getState(String serverName)
  {
    return (State)this.map.get(serverName);
  }
  
  public State getStateWithCreate(String serverName)
  {
    State state = (State)this.map.get(serverName);
    if (state == null)
    {
      state = new State();
      this.map.put(serverName, state);
    }
    return state;
  }
  
  public State removeState(String serverName)
  {
    return (State)this.map.remove(serverName);
  }
  
  public static class State
  {
    SpiTransaction transaction;
    
    public String toString()
    {
      return "txn[" + this.transaction + "]";
    }
    
    public SpiTransaction get()
    {
      return this.transaction;
    }
    
    public void set(SpiTransaction trans)
    {
      if ((this.transaction != null) && (this.transaction.isActive()))
      {
        String m = "The existing transaction is still active?";
        throw new PersistenceException(m);
      }
      this.transaction = trans;
    }
    
    public void commit()
    {
      this.transaction.commit();
      this.transaction = null;
    }
    
    public void rollback()
    {
      this.transaction.rollback();
      this.transaction = null;
    }
    
    public void end()
    {
      if (this.transaction != null)
      {
        this.transaction.end();
        this.transaction = null;
      }
    }
    
    public void replace(SpiTransaction trans)
    {
      this.transaction = trans;
    }
  }
}
