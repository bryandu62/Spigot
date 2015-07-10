package com.avaje.ebeaninternal.api;

import com.avaje.ebean.event.BulkTableEvent;
import com.avaje.ebeaninternal.server.cluster.BinaryMessage;
import com.avaje.ebeaninternal.server.cluster.BinaryMessageList;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class TransactionEventTable
  implements Serializable
{
  private static final long serialVersionUID = 2236555729767483264L;
  private final Map<String, TableIUD> map;
  
  public TransactionEventTable()
  {
    this.map = new HashMap();
  }
  
  public String toString()
  {
    return "TransactionEventTable " + this.map.values();
  }
  
  public void writeBinaryMessage(BinaryMessageList msgList)
    throws IOException
  {
    for (TableIUD tableIud : this.map.values()) {
      tableIud.writeBinaryMessage(msgList);
    }
  }
  
  public void readBinaryMessage(DataInput dataInput)
    throws IOException
  {
    TableIUD tableIud = TableIUD.readBinaryMessage(dataInput);
    this.map.put(tableIud.getTableName(), tableIud);
  }
  
  public void add(TransactionEventTable table)
  {
    for (TableIUD iud : table.values()) {
      add(iud);
    }
  }
  
  public void add(String table, boolean insert, boolean update, boolean delete)
  {
    table = table.toUpperCase();
    
    add(new TableIUD(table, insert, update, delete, null));
  }
  
  public void add(TableIUD newTableIUD)
  {
    TableIUD existingTableIUD = (TableIUD)this.map.put(newTableIUD.getTableName(), newTableIUD);
    if (existingTableIUD != null) {
      newTableIUD.add(existingTableIUD);
    }
  }
  
  public boolean isEmpty()
  {
    return this.map.isEmpty();
  }
  
  public Collection<TableIUD> values()
  {
    return this.map.values();
  }
  
  public static class TableIUD
    implements Serializable, BulkTableEvent
  {
    private static final long serialVersionUID = -1958317571064162089L;
    private String table;
    private boolean insert;
    private boolean update;
    private boolean delete;
    
    private TableIUD(String table, boolean insert, boolean update, boolean delete)
    {
      this.table = table;
      this.insert = insert;
      this.update = update;
      this.delete = delete;
    }
    
    public static TableIUD readBinaryMessage(DataInput dataInput)
      throws IOException
    {
      String table = dataInput.readUTF();
      boolean insert = dataInput.readBoolean();
      boolean update = dataInput.readBoolean();
      boolean delete = dataInput.readBoolean();
      
      return new TableIUD(table, insert, update, delete);
    }
    
    public void writeBinaryMessage(BinaryMessageList msgList)
      throws IOException
    {
      BinaryMessage msg = new BinaryMessage(this.table.length() + 10);
      DataOutputStream os = msg.getOs();
      os.writeInt(2);
      os.writeUTF(this.table);
      os.writeBoolean(this.insert);
      os.writeBoolean(this.update);
      os.writeBoolean(this.delete);
      
      msgList.add(msg);
    }
    
    public String toString()
    {
      return "TableIUD " + this.table + " i:" + this.insert + " u:" + this.update + " d:" + this.delete;
    }
    
    private void add(TableIUD other)
    {
      if (other.insert) {
        this.insert = true;
      }
      if (other.update) {
        this.update = true;
      }
      if (other.delete) {
        this.delete = true;
      }
    }
    
    public String getTableName()
    {
      return this.table;
    }
    
    public boolean isInsert()
    {
      return this.insert;
    }
    
    public boolean isUpdate()
    {
      return this.update;
    }
    
    public boolean isDelete()
    {
      return this.delete;
    }
    
    public boolean isUpdateOrDelete()
    {
      return (this.update) || (this.delete);
    }
  }
}
