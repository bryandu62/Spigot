package com.avaje.ebeaninternal.server.core;

import com.avaje.ebeaninternal.server.lib.util.Dnode;
import java.util.ArrayList;
import java.util.List;

public class XmlConfig
{
  private final List<Dnode> ebeanOrmXml;
  private final List<Dnode> ormXml;
  private final List<Dnode> allXml;
  
  public XmlConfig(List<Dnode> ormXml, List<Dnode> ebeanOrmXml)
  {
    this.ormXml = ormXml;
    this.ebeanOrmXml = ebeanOrmXml;
    this.allXml = new ArrayList(ormXml.size() + ebeanOrmXml.size());
    this.allXml.addAll(ormXml);
    this.allXml.addAll(ebeanOrmXml);
  }
  
  public List<Dnode> getEbeanOrmXml()
  {
    return this.ebeanOrmXml;
  }
  
  public List<Dnode> getOrmXml()
  {
    return this.ormXml;
  }
  
  public List<Dnode> find(List<Dnode> entityXml, String element)
  {
    ArrayList<Dnode> hits = new ArrayList();
    for (int i = 0; i < entityXml.size(); i++) {
      hits.addAll(((Dnode)entityXml.get(i)).findAll(element, 1));
    }
    return hits;
  }
  
  public List<Dnode> findEntityXml(String className)
  {
    ArrayList<Dnode> hits = new ArrayList(2);
    for (Dnode ormXml : this.allXml)
    {
      Dnode entityMappings = ormXml.find("entity-mappings");
      
      List<Dnode> entities = entityMappings.findAll("entity", "class", className, 1);
      if (entities.size() == 1) {
        hits.add(entities.get(0));
      }
    }
    return hits;
  }
}
