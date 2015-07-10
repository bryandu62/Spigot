package com.avaje.ebeaninternal.server.persist;

import com.avaje.ebeaninternal.api.BindParams;
import com.avaje.ebeaninternal.api.BindParams.Param;
import com.avaje.ebeaninternal.server.core.Message;
import com.avaje.ebeaninternal.server.type.DataBind;
import com.avaje.ebeaninternal.server.type.ScalarType;
import com.avaje.ebeaninternal.server.type.TypeManager;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.PersistenceException;

public class Binder
{
  private static final Logger logger = Logger.getLogger(Binder.class.getName());
  private final TypeManager typeManager;
  
  public Binder(TypeManager typeManager)
  {
    this.typeManager = typeManager;
  }
  
  public void bind(BindValues bindValues, DataBind dataBind, StringBuilder bindBuf)
    throws SQLException
  {
    String logPrefix = "";
    
    ArrayList<BindValues.Value> list = bindValues.values();
    for (int i = 0; i < list.size(); i++)
    {
      BindValues.Value bindValue = (BindValues.Value)list.get(i);
      if (bindValue.isComment())
      {
        if (bindBuf != null)
        {
          bindBuf.append(bindValue.getName());
          if (logPrefix.equals("")) {
            logPrefix = ", ";
          }
        }
      }
      else
      {
        Object val = bindValue.getValue();
        int dt = bindValue.getDbType();
        bindObject(dataBind, val, dt);
        if (bindBuf != null)
        {
          bindBuf.append(logPrefix);
          if (logPrefix.equals("")) {
            logPrefix = ", ";
          }
          bindBuf.append(bindValue.getName());
          bindBuf.append("=");
          if (isLob(dt)) {
            bindBuf.append("[LOB]");
          } else {
            bindBuf.append(String.valueOf(val));
          }
        }
      }
    }
  }
  
  public String bind(BindParams bindParams, DataBind dataBind)
    throws SQLException
  {
    StringBuilder bindLog = new StringBuilder();
    bind(bindParams, dataBind, bindLog);
    return bindLog.toString();
  }
  
  public void bind(BindParams bindParams, DataBind dataBind, StringBuilder bindLog)
    throws SQLException
  {
    bind(bindParams.positionedParameters(), dataBind, bindLog);
  }
  
  public void bind(List<BindParams.Param> list, DataBind dataBind, StringBuilder bindLog)
    throws SQLException
  {
    CallableStatement cstmt = null;
    if ((dataBind.getPstmt() instanceof CallableStatement)) {
      cstmt = (CallableStatement)dataBind.getPstmt();
    }
    Object value = null;
    try
    {
      for (int i = 0; i < list.size(); i++)
      {
        BindParams.Param param = (BindParams.Param)list.get(i);
        if ((param.isOutParam()) && (cstmt != null))
        {
          cstmt.registerOutParameter(dataBind.nextPos(), param.getType());
          if (param.isInParam()) {
            dataBind.decrementPos();
          }
        }
        if (param.isInParam())
        {
          value = param.getInValue();
          if (bindLog != null)
          {
            if (param.isEncryptionKey()) {
              bindLog.append("****");
            } else {
              bindLog.append(value);
            }
            bindLog.append(", ");
          }
          if (value == null) {
            bindObject(dataBind, null, param.getType());
          } else {
            bindObject(dataBind, value);
          }
        }
      }
    }
    catch (SQLException ex)
    {
      logger.warning(Message.msg("fetch.bind.error", "" + (dataBind.currentPos() - 1), value));
      throw ex;
    }
  }
  
  public void bindObject(DataBind dataBind, Object value)
    throws SQLException
  {
    if (value == null)
    {
      bindObject(dataBind, null, 1111);
    }
    else
    {
      ScalarType<?> type = this.typeManager.getScalarType(value.getClass());
      if (type == null)
      {
        String msg = "No ScalarType registered for " + value.getClass();
        throw new PersistenceException(msg);
      }
      if (!type.isJdbcNative()) {
        value = type.toJdbcType(value);
      }
      int dbType = type.getJdbcType();
      bindObject(dataBind, value, dbType);
    }
  }
  
  public void bindObject(DataBind dataBind, Object data, int dbType)
    throws SQLException
  {
    if (data == null)
    {
      dataBind.setNull(dbType);
      return;
    }
    switch (dbType)
    {
    case -1: 
      bindLongVarChar(dataBind, data);
      break;
    case -4: 
      bindLongVarBinary(dataBind, data);
      break;
    case 2005: 
      bindClob(dataBind, data);
      break;
    case 2004: 
      bindBlob(dataBind, data);
      break;
    default: 
      bindSimpleData(dataBind, dbType, data);
    }
  }
  
  private void bindSimpleData(DataBind b, int dataType, Object data)
    throws SQLException
  {
    try
    {
      switch (dataType)
      {
      case 16: 
        Boolean bo = (Boolean)data;
        b.setBoolean(bo.booleanValue());
        break;
      case -7: 
        Boolean bitBool = (Boolean)data;
        b.setBoolean(bitBool.booleanValue());
        break;
      case 12: 
        b.setString((String)data);
        break;
      case 1: 
        b.setString(data.toString());
        break;
      case -6: 
        b.setByte(((Byte)data).byteValue());
        break;
      case 5: 
        b.setShort(((Short)data).shortValue());
        break;
      case 4: 
        b.setInt(((Integer)data).intValue());
        break;
      case -5: 
        b.setLong(((Long)data).longValue());
        break;
      case 7: 
        b.setFloat(((Float)data).floatValue());
        break;
      case 6: 
        b.setDouble(((Double)data).doubleValue());
        break;
      case 8: 
        b.setDouble(((Double)data).doubleValue());
        break;
      case 2: 
        b.setBigDecimal((BigDecimal)data);
        break;
      case 3: 
        b.setBigDecimal((BigDecimal)data);
        break;
      case 92: 
        b.setTime((Time)data);
        break;
      case 91: 
        b.setDate((Date)data);
        break;
      case 93: 
        b.setTimestamp((Timestamp)data);
        break;
      case -2: 
        b.setBytes((byte[])data);
        break;
      case -3: 
        b.setBytes((byte[])data);
        break;
      case 1111: 
        b.setObject(data);
        break;
      case 2000: 
        b.setObject(data);
        break;
      default: 
        String msg = Message.msg("persist.bind.datatype", "" + dataType, "" + b.currentPos());
        throw new SQLException(msg);
      }
    }
    catch (Exception e)
    {
      String dataClass = "Data is null?";
      if (data != null) {
        dataClass = data.getClass().getName();
      }
      String m = "Error with property[" + b.currentPos() + "] dt[" + dataType + "]";
      m = m + "data[" + data + "][" + dataClass + "]";
      throw new PersistenceException(m, e);
    }
  }
  
  private void bindLongVarChar(DataBind b, Object data)
    throws SQLException
  {
    String sd = (String)data;
    b.setClob(sd);
  }
  
  private void bindLongVarBinary(DataBind b, Object data)
    throws SQLException
  {
    byte[] bytes = (byte[])data;
    b.setBlob(bytes);
  }
  
  private void bindClob(DataBind b, Object data)
    throws SQLException
  {
    String sd = (String)data;
    b.setClob(sd);
  }
  
  private void bindBlob(DataBind b, Object data)
    throws SQLException
  {
    byte[] bytes = (byte[])data;
    b.setBlob(bytes);
  }
  
  private boolean isLob(int dbType)
  {
    switch (dbType)
    {
    case 2005: 
      return true;
    case -1: 
      return true;
    case 2004: 
      return true;
    case -4: 
      return true;
    }
    return false;
  }
}
