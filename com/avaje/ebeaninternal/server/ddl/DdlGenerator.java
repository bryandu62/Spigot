package com.avaje.ebeaninternal.server.ddl;

import com.avaje.ebean.Transaction;
import com.avaje.ebean.config.NamingConvention;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.config.dbplatform.DatabasePlatform;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintStream;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.PersistenceException;

public class DdlGenerator
{
  private static final Logger logger = Logger.getLogger(DdlGenerator.class.getName());
  private final SpiEbeanServer server;
  private final DatabasePlatform dbPlatform;
  private PrintStream out = System.out;
  private int summaryLength = 80;
  private boolean debug = true;
  private boolean generateDdl;
  private boolean runDdl;
  private String dropContent;
  private String createContent;
  private NamingConvention namingConvention;
  
  public DdlGenerator(SpiEbeanServer server, DatabasePlatform dbPlatform, ServerConfig serverConfig)
  {
    this.server = server;
    this.dbPlatform = dbPlatform;
    this.generateDdl = serverConfig.isDdlGenerate();
    this.runDdl = serverConfig.isDdlRun();
    this.namingConvention = serverConfig.getNamingConvention();
  }
  
  public void execute(boolean online)
  {
    generateDdl();
    if (online) {
      runDdl();
    }
  }
  
  public void generateDdl()
  {
    if (this.generateDdl)
    {
      writeDrop(getDropFileName());
      writeCreate(getCreateFileName());
    }
  }
  
  public void runDdl()
  {
    if (this.runDdl) {
      try
      {
        if (this.dropContent == null) {
          this.dropContent = readFile(getDropFileName());
        }
        if (this.createContent == null) {
          this.createContent = readFile(getCreateFileName());
        }
        runScript(true, this.dropContent);
        runScript(false, this.createContent);
      }
      catch (IOException e)
      {
        String msg = "Error reading drop/create script from file system";
        throw new RuntimeException(msg, e);
      }
    }
  }
  
  protected void writeDrop(String dropFile)
  {
    try
    {
      String c = generateDropDdl();
      writeFile(dropFile, c);
    }
    catch (IOException e)
    {
      String msg = "Error generating Drop DDL";
      throw new PersistenceException(msg, e);
    }
  }
  
  protected void writeCreate(String createFile)
  {
    try
    {
      String c = generateCreateDdl();
      writeFile(createFile, c);
    }
    catch (IOException e)
    {
      String msg = "Error generating Create DDL";
      throw new PersistenceException(msg, e);
    }
  }
  
  public String generateDropDdl()
  {
    DdlGenContext ctx = createContext();
    
    DropTableVisitor drop = new DropTableVisitor(ctx);
    VisitorUtil.visit(this.server, drop);
    
    DropSequenceVisitor dropSequence = new DropSequenceVisitor(ctx);
    VisitorUtil.visit(this.server, dropSequence);
    
    ctx.flush();
    this.dropContent = ctx.getContent();
    return this.dropContent;
  }
  
  public String generateCreateDdl()
  {
    DdlGenContext ctx = createContext();
    CreateTableVisitor create = new CreateTableVisitor(ctx);
    VisitorUtil.visit(this.server, create);
    
    CreateSequenceVisitor createSequence = new CreateSequenceVisitor(ctx);
    VisitorUtil.visit(this.server, createSequence);
    
    AddForeignKeysVisitor fkeys = new AddForeignKeysVisitor(ctx);
    VisitorUtil.visit(this.server, fkeys);
    
    ctx.flush();
    this.createContent = ctx.getContent();
    return this.createContent;
  }
  
  protected String getDropFileName()
  {
    return this.server.getName() + "-drop.sql";
  }
  
  protected String getCreateFileName()
  {
    return this.server.getName() + "-create.sql";
  }
  
  protected DdlGenContext createContext()
  {
    return new DdlGenContext(this.dbPlatform, this.namingConvention);
  }
  
  protected void writeFile(String fileName, String fileContent)
    throws IOException
  {
    File f = new File(fileName);
    
    FileWriter fw = new FileWriter(f);
    try
    {
      fw.write(fileContent);
      fw.flush();
    }
    finally
    {
      fw.close();
    }
  }
  
  protected String readFile(String fileName)
    throws IOException
  {
    File f = new File(fileName);
    if (!f.exists()) {
      return null;
    }
    StringBuilder buf = new StringBuilder();
    
    FileReader fr = new FileReader(f);
    LineNumberReader lr = new LineNumberReader(fr);
    try
    {
      String s = null;
      while ((s = lr.readLine()) != null) {
        buf.append(s).append("\n");
      }
    }
    finally
    {
      lr.close();
    }
    return buf.toString();
  }
  
  public void runScript(boolean expectErrors, String content)
  {
    StringReader sr = new StringReader(content);
    List<String> statements = parseStatements(sr);
    
    Transaction t = this.server.createTransaction();
    try
    {
      Connection connection = t.getConnection();
      
      this.out.println("runScript");
      this.out.flush();
      
      runStatements(expectErrors, statements, connection);
      
      this.out.println("... end of script");
      this.out.flush();
      
      t.commit();
    }
    catch (Exception e)
    {
      String msg = "Error: " + e.getMessage();
      throw new PersistenceException(msg, e);
    }
    finally
    {
      t.end();
    }
  }
  
  private void runStatements(boolean expectErrors, List<String> statements, Connection c)
  {
    for (int i = 0; i < statements.size(); i++)
    {
      String xOfy = i + 1 + " of " + statements.size();
      runStatement(expectErrors, xOfy, (String)statements.get(i), c);
    }
  }
  
  private void runStatement(boolean expectErrors, String oneOf, String stmt, Connection c)
  {
    PreparedStatement pstmt = null;
    try
    {
      stmt = stmt.trim();
      if (stmt.endsWith(";")) {
        stmt = stmt.substring(0, stmt.length() - 1);
      } else if (stmt.endsWith("/")) {
        stmt = stmt.substring(0, stmt.length() - 1);
      }
      if (this.debug)
      {
        this.out.println("executing " + oneOf + " " + getSummary(stmt));
        this.out.flush();
      }
      pstmt = c.prepareStatement(stmt);
      pstmt.execute();
      String msg;
      return;
    }
    catch (Exception e)
    {
      if (expectErrors)
      {
        this.out.println(" ... ignoring error executing " + getSummary(stmt) + "  error: " + e.getMessage());
        e.printStackTrace();
        this.out.flush();
      }
      else
      {
        msg = "Error executing stmt[" + stmt + "] error[" + e.getMessage() + "]";
        throw new RuntimeException(msg, e);
      }
    }
    finally
    {
      if (pstmt != null) {
        try
        {
          pstmt.close();
        }
        catch (SQLException e)
        {
          logger.log(Level.SEVERE, "Error closing pstmt", e);
        }
      }
    }
  }
  
  protected List<String> parseStatements(StringReader reader)
  {
    try
    {
      BufferedReader br = new BufferedReader(reader);
      
      ArrayList<String> statements = new ArrayList();
      
      StringBuilder sb = new StringBuilder();
      String s;
      while ((s = br.readLine()) != null)
      {
        s = s.trim();
        int semiPos = s.indexOf(';');
        if (semiPos == -1)
        {
          sb.append(s).append(" ");
        }
        else if (semiPos == s.length() - 1)
        {
          sb.append(s);
          statements.add(sb.toString().trim());
          sb = new StringBuilder();
        }
        else
        {
          String preSemi = s.substring(0, semiPos);
          sb.append(preSemi);
          statements.add(sb.toString().trim());
          sb = new StringBuilder();
          sb.append(s.substring(semiPos + 1));
        }
      }
      return statements;
    }
    catch (IOException e)
    {
      throw new PersistenceException(e);
    }
  }
  
  private String getSummary(String s)
  {
    if (s.length() > this.summaryLength) {
      return s.substring(0, this.summaryLength).trim() + "...";
    }
    return s;
  }
}
