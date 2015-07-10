package com.avaje.ebeaninternal.server.query;

import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class LoadedPropertiesCache
{
  static ConcurrentHashMap<Integer, Set<String>> cache = new ConcurrentHashMap(250, 0.75F, 16);
  
  public static Set<String> get(int partialHash, Set<String> partialProps, BeanDescriptor<?> desc)
  {
    int manyHash = desc.getNamesOfManyPropsHash();
    int totalHash = 37 * partialHash + manyHash;
    
    Integer key = Integer.valueOf(totalHash);
    
    Set<String> includedProps = (Set)cache.get(key);
    if (includedProps == null)
    {
      LinkedHashSet<String> mergeNames = new LinkedHashSet();
      mergeNames.addAll(partialProps);
      if (manyHash != 0) {
        mergeNames.addAll(desc.getNamesOfManyProps());
      }
      includedProps = Collections.unmodifiableSet(mergeNames);
      cache.put(key, includedProps);
    }
    return includedProps;
  }
}
