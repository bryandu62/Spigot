package org.apache.logging.log4j.core.appender.db.jdbc;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.appender.db.AbstractDatabaseManager;
import org.apache.logging.log4j.core.appender.db.AbstractDatabaseManager.AbstractFactoryData;
import org.apache.logging.log4j.core.helpers.Closer;
import org.apache.logging.log4j.core.layout.PatternLayout;

public final class JDBCDatabaseManager
  extends AbstractDatabaseManager
{
  private static final JDBCDatabaseManagerFactory FACTORY = new JDBCDatabaseManagerFactory(null);
  private final List<Column> columns;
  private final ConnectionSource connectionSource;
  private final String sqlStatement;
  private Connection connection;
  private PreparedStatement statement;
  
  private JDBCDatabaseManager(String name, int bufferSize, ConnectionSource connectionSource, String sqlStatement, List<Column> columns)
  {
    super(name, bufferSize);
    this.connectionSource = connectionSource;
    this.sqlStatement = sqlStatement;
    this.columns = columns;
  }
  
  protected void connectInternal()
    throws SQLException
  {
    this.connection = this.connectionSource.getConnection();
    this.statement = this.connection.prepareStatement(this.sqlStatement);
  }
  
  protected void disconnectInternal()
    throws SQLException
  {
    try
    {
      Closer.close(this.statement);
    }
    finally
    {
      Closer.close(this.connection);
    }
  }
  
  protected void writeInternal(LogEvent event)
  {
    StringReader reader = null;
    try
    {
      if ((!isConnected()) || (this.connection == null) || (this.connection.isClosed())) {
        throw new AppenderLoggingException("Cannot write logging event; JDBC manager not connected to the database.");
      }
      int i = 1;
      for (Column column : this.columns) {
        if (column.isEventTimestamp)
        {
          this.statement.setTimestamp(i++, new Timestamp(event.getMillis()));
        }
        else if (column.isClob)
        {
          reader = new StringReader(column.layout.toSerializable(event));
          if (column.isUnicode) {
            this.statement.setNClob(i++, reader);
          } else {
            this.statement.setClob(i++, reader);
          }
        }
        else if (column.isUnicode)
        {
          this.statement.setNString(i++, column.layout.toSerializable(event));
        }
        else
        {
          this.statement.setString(i++, column.layout.toSerializable(event));
        }
      }
      if (this.statement.executeUpdate() == 0) {
        throw new AppenderLoggingException("No records inserted in database table for log event in JDBC manager.");
      }
    }
    catch (SQLException e)
    {
      throw new AppenderLoggingException("Failed to insert record for log event in JDBC manager: " + e.getMessage(), e);
    }
    finally
    {
      Closer.closeSilent(reader);
    }
  }
  
  public static JDBCDatabaseManager getJDBCDatabaseManager(String name, int bufferSize, ConnectionSource connectionSource, String tableName, ColumnConfig[] columnConfigs)
  {
    return (JDBCDatabaseManager)AbstractDatabaseManager.getManager(name, new FactoryData(bufferSize, connectionSource, tableName, columnConfigs), FACTORY);
  }
  
  private static final class FactoryData
    extends AbstractDatabaseManager.AbstractFactoryData
  {
    private final ColumnConfig[] columnConfigs;
    private final ConnectionSource connectionSource;
    private final String tableName;
    
    protected FactoryData(int bufferSize, ConnectionSource connectionSource, String tableName, ColumnConfig[] columnConfigs)
    {
      super();
      this.connectionSource = connectionSource;
      this.tableName = tableName;
      this.columnConfigs = columnConfigs;
    }
  }
  
  private static final class JDBCDatabaseManagerFactory
    implements ManagerFactory<JDBCDatabaseManager, JDBCDatabaseManager.FactoryData>
  {
    public JDBCDatabaseManager createManager(String name, JDBCDatabaseManager.FactoryData data)
    {
      StringBuilder columnPart = new StringBuilder();
      StringBuilder valuePart = new StringBuilder();
      List<JDBCDatabaseManager.Column> columns = new ArrayList();
      int i = 0;
      for (ColumnConfig config : JDBCDatabaseManager.FactoryData.access$500(data))
      {
        if (i++ > 0)
        {
          columnPart.append(',');
          valuePart.append(',');
        }
        columnPart.append(config.getColumnName());
        if (config.getLiteralValue() != null)
        {
          valuePart.append(config.getLiteralValue());
        }
        else
        {
          columns.add(new JDBCDatabaseManager.Column(config.getLayout(), config.isEventTimestamp(), config.isUnicode(), config.isClob(), null));
          
          valuePart.append('?');
        }
      }
      String sqlStatement = "INSERT INTO " + JDBCDatabaseManager.FactoryData.access$700(data) + " (" + columnPart + ") VALUES (" + valuePart + ")";
      
      return new JDBCDatabaseManager(name, data.getBufferSize(), JDBCDatabaseManager.FactoryData.access$800(data), sqlStatement, columns, null);
    }
  }
  
  private static final class Column
  {
    private final PatternLayout layout;
    private final boolean isEventTimestamp;
    private final boolean isUnicode;
    private final boolean isClob;
    
    private Column(PatternLayout layout, boolean isEventDate, boolean isUnicode, boolean isClob)
    {
      this.layout = layout;
      this.isEventTimestamp = isEventDate;
      this.isUnicode = isUnicode;
      this.isClob = isClob;
    }
  }
}
