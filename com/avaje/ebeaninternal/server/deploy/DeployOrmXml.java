package com.avaje.ebeaninternal.server.deploy;

import com.avaje.ebeaninternal.server.lib.resource.ResourceContent;
import com.avaje.ebeaninternal.server.lib.resource.ResourceSource;
import com.avaje.ebeaninternal.server.lib.util.Dnode;
import com.avaje.ebeaninternal.server.lib.util.DnodeReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeployOrmXml
{
  private static final Logger logger = Logger.getLogger(DeployOrmXml.class.getName());
  private final HashMap<String, DNativeQuery> nativeQueryCache;
  private final ArrayList<Dnode> ormXmlList;
  private final ResourceSource resSource;
  
  public DeployOrmXml(ResourceSource resSource)
  {
    this.resSource = resSource;
    this.nativeQueryCache = new HashMap();
    this.ormXmlList = findAllOrmXml();
    
    initialiseNativeQueries();
  }
  
  private void initialiseNativeQueries()
  {
    for (Dnode ormXml : this.ormXmlList) {
      initialiseNativeQueries(ormXml);
    }
  }
  
  private void initialiseNativeQueries(Dnode ormXml)
  {
    Dnode entityMappings = ormXml.find("entity-mappings");
    if (entityMappings != null)
    {
      List<Dnode> nq = entityMappings.findAll("named-native-query", 1);
      for (int i = 0; i < nq.size(); i++)
      {
        Dnode nqNode = (Dnode)nq.get(i);
        Dnode nqQueryNode = nqNode.find("query");
        if (nqQueryNode != null)
        {
          String queryContent = nqQueryNode.getNodeContent();
          String queryName = nqNode.getAttribute("name");
          if ((queryName != null) && (queryContent != null))
          {
            DNativeQuery query = new DNativeQuery(queryContent);
            this.nativeQueryCache.put(queryName, query);
          }
        }
      }
    }
  }
  
  public DNativeQuery getNativeQuery(String name)
  {
    return (DNativeQuery)this.nativeQueryCache.get(name);
  }
  
  private ArrayList<Dnode> findAllOrmXml()
  {
    ArrayList<Dnode> ormXmlList = new ArrayList();
    
    String defaultFile = "orm.xml";
    readOrmXml(defaultFile, ormXmlList);
    if (!ormXmlList.isEmpty())
    {
      StringBuilder sb = new StringBuilder();
      for (Dnode ox : ormXmlList) {
        sb.append(", ").append(ox.getAttribute("ebean.filename"));
      }
      String loadedFiles = sb.toString().substring(2);
      logger.info("Deployment xml [" + loadedFiles + "]  loaded.");
    }
    return ormXmlList;
  }
  
  private boolean readOrmXml(String ormXmlName, ArrayList<Dnode> ormXmlList)
  {
    try
    {
      Dnode ormXml = null;
      ResourceContent content = this.resSource.getContent(ormXmlName);
      if (content != null) {
        ormXml = readOrmXml(content.getInputStream());
      } else {
        ormXml = readOrmXmlFromClasspath(ormXmlName);
      }
      if (ormXml != null)
      {
        ormXml.setAttribute("ebean.filename", ormXmlName);
        ormXmlList.add(ormXml);
        return true;
      }
      return false;
    }
    catch (IOException e)
    {
      logger.log(Level.SEVERE, "error reading orm xml deployment " + ormXmlName, e);
    }
    return false;
  }
  
  private Dnode readOrmXmlFromClasspath(String ormXmlName)
    throws IOException
  {
    InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(ormXmlName);
    if (is == null) {
      return null;
    }
    return readOrmXml(is);
  }
  
  private Dnode readOrmXml(InputStream in)
    throws IOException
  {
    DnodeReader reader = new DnodeReader();
    Dnode ormXml = reader.parseXml(in);
    in.close();
    return ormXml;
  }
  
  public Dnode findEntityDeploymentXml(String className)
  {
    for (Dnode ormXml : this.ormXmlList)
    {
      Dnode entityMappings = ormXml.find("entity-mappings");
      
      List<Dnode> entities = entityMappings.findAll("entity", "class", className, 1);
      if (entities.size() == 1) {
        return (Dnode)entities.get(0);
      }
    }
    return null;
  }
}
