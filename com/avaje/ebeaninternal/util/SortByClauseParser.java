package com.avaje.ebeaninternal.util;

public final class SortByClauseParser
{
  private final String rawSortBy;
  
  public static SortByClause parse(String rawSortByClause)
  {
    return new SortByClauseParser(rawSortByClause).parse();
  }
  
  private SortByClauseParser(String rawSortByClause)
  {
    this.rawSortBy = rawSortByClause.trim();
  }
  
  private SortByClause parse()
  {
    SortByClause sortBy = new SortByClause();
    
    String[] sections = this.rawSortBy.split(",");
    for (int i = 0; i < sections.length; i++)
    {
      SortByClause.Property p = parseSection(sections[i].trim());
      if (p == null) {
        break;
      }
      sortBy.add(p);
    }
    return sortBy;
  }
  
  private SortByClause.Property parseSection(String section)
  {
    if (section.length() == 0) {
      return null;
    }
    String[] words = section.split(" ");
    if ((words.length < 1) || (words.length > 3)) {
      throw new RuntimeException("Expecting 1 to 3 words in [" + section + "] but got [" + words.length + "]");
    }
    Boolean nullsHigh = null;
    boolean ascending = true;
    String propName = words[0];
    if (words.length > 1) {
      if (words[1].startsWith("nulls")) {
        nullsHigh = isNullsHigh(words[1]);
      } else {
        ascending = isAscending(words[1]);
      }
    }
    if (words.length > 2) {
      if (words[2].startsWith("nulls")) {
        nullsHigh = isNullsHigh(words[2]);
      } else {
        ascending = isAscending(words[2]);
      }
    }
    return new SortByClause.Property(propName, ascending, nullsHigh);
  }
  
  private Boolean isNullsHigh(String word)
  {
    if ("nullshigh".equalsIgnoreCase(word)) {
      return Boolean.TRUE;
    }
    if ("nullslow".equalsIgnoreCase(word)) {
      return Boolean.FALSE;
    }
    String m = "Expecting nullsHigh or nullsLow but got [" + word + "] in [" + this.rawSortBy + "]";
    throw new RuntimeException(m);
  }
  
  private boolean isAscending(String word)
  {
    if ("asc".equalsIgnoreCase(word)) {
      return true;
    }
    if ("desc".equalsIgnoreCase(word)) {
      return false;
    }
    String m = "Expection ASC or DESC but got [" + word + "] in [" + this.rawSortBy + "]";
    throw new RuntimeException(m);
  }
}
