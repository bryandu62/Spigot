package org.bukkit.inventory.meta;

import java.util.List;

public abstract interface BookMeta
  extends ItemMeta
{
  public abstract boolean hasTitle();
  
  public abstract String getTitle();
  
  public abstract boolean setTitle(String paramString);
  
  public abstract boolean hasAuthor();
  
  public abstract String getAuthor();
  
  public abstract void setAuthor(String paramString);
  
  public abstract boolean hasPages();
  
  public abstract String getPage(int paramInt);
  
  public abstract void setPage(int paramInt, String paramString);
  
  public abstract List<String> getPages();
  
  public abstract void setPages(List<String> paramList);
  
  public abstract void setPages(String... paramVarArgs);
  
  public abstract void addPage(String... paramVarArgs);
  
  public abstract int getPageCount();
  
  public abstract BookMeta clone();
}
