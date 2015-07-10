package com.avaje.ebeaninternal.server.ldap;

import com.avaje.ebeaninternal.api.SpiQuery;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapName;

public class LdapOrmQueryExecute<T>
{
  private static final Logger logger = Logger.getLogger(LdapOrmQueryExecute.class.getName());
  private final SpiQuery<?> query;
  private final BeanDescriptor<T> beanDescriptor;
  private final DirContext dc;
  private final LdapBeanBuilder<T> beanBuilder;
  private final String filterExpr;
  private final Object[] filterValues;
  private final String[] selectProps;
  
  public LdapOrmQueryExecute(LdapOrmQueryRequest<T> request, boolean defaultVanillaMode, DirContext dc)
  {
    this.query = request.getQuery();
    this.beanDescriptor = request.getBeanDescriptor();
    this.dc = dc;
    
    boolean vanillaMode = this.query.isVanillaMode(defaultVanillaMode);
    this.beanBuilder = new LdapBeanBuilder(this.beanDescriptor, vanillaMode);
    
    LdapQueryDeployHelper deployHelper = new LdapQueryDeployHelper(request);
    this.selectProps = deployHelper.getSelectedProperties();
    this.filterExpr = deployHelper.getFilterExpr();
    this.filterValues = deployHelper.getFilterValues();
  }
  
  public T findId()
  {
    Object id = this.query.getId();
    try
    {
      LdapName dn = this.beanDescriptor.createLdapNameById(id);
      
      String[] findAttrs = this.selectProps;
      if (findAttrs == null) {
        findAttrs = this.beanDescriptor.getDefaultSelectDbArray();
      }
      String debugQuery = "Name:" + dn + " attrs:" + Arrays.toString(findAttrs);
      
      Attributes attrs = this.dc.getAttributes(dn, findAttrs);
      
      T bean = this.beanBuilder.readAttributes(attrs);
      
      this.query.setGeneratedSql(debugQuery);
      return bean;
    }
    catch (NamingException e)
    {
      throw new LdapPersistenceException(e);
    }
  }
  
  public List<T> findList()
  {
    SearchControls sc = new SearchControls();
    sc.setSearchScope(1);
    
    List<T> list = new ArrayList();
    try
    {
      LdapName dn = this.beanDescriptor.createLdapName(null);
      
      String debugQuery = "Name:" + dn;
      if (this.selectProps != null)
      {
        sc.setReturningAttributes(this.selectProps);
        debugQuery = debugQuery + " select:" + Arrays.toString(this.selectProps);
      }
      if (logger.isLoggable(Level.INFO)) {
        logger.info("Ldap Query  Name:" + dn + " filterExpr:" + this.filterExpr);
      }
      debugQuery = debugQuery + " filterExpr:" + this.filterExpr;
      NamingEnumeration<SearchResult> result;
      NamingEnumeration<SearchResult> result;
      if ((this.filterValues == null) || (this.filterValues.length == 0))
      {
        result = this.dc.search(dn, this.filterExpr, sc);
      }
      else
      {
        debugQuery = debugQuery + " filterValues:" + Arrays.toString(this.filterValues);
        result = this.dc.search(dn, this.filterExpr, this.filterValues, sc);
      }
      this.query.setGeneratedSql(debugQuery);
      if (result != null) {
        while (result.hasMoreElements())
        {
          SearchResult row = (SearchResult)result.nextElement();
          T bean = this.beanBuilder.readAttributes(row.getAttributes());
          list.add(bean);
        }
      }
      return list;
    }
    catch (NamingException e)
    {
      throw new LdapPersistenceException(e);
    }
  }
}
