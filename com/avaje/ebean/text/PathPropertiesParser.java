package com.avaje.ebean.text;

class PathPropertiesParser
{
  private final PathProperties pathProps;
  private final String source;
  private final char[] chars;
  private final int eof;
  private int pos;
  private int startPos;
  private PathProperties.Props currentPathProps;
  
  static PathProperties parse(String source)
  {
    return new PathPropertiesParser(source).pathProps;
  }
  
  private PathPropertiesParser(String src)
  {
    if (src.startsWith(":")) {
      src = src.substring(1);
    }
    this.pathProps = new PathProperties();
    this.source = src;
    this.chars = src.toCharArray();
    this.eof = this.chars.length;
    if (this.eof > 0)
    {
      this.currentPathProps = this.pathProps.getRootProperties();
      parse();
    }
  }
  
  private String getPath()
  {
    do
    {
      char c1 = this.chars[(this.pos++)];
      switch (c1)
      {
      case '(': 
        return currentWord();
      }
    } while (this.pos < this.eof);
    throw new RuntimeException("Hit EOF while reading sectionTitle from " + this.startPos);
  }
  
  private void parse()
  {
    do
    {
      String path = getPath();
      pushPath(path);
      parseSection();
    } while (this.pos < this.eof);
  }
  
  private void parseSection()
  {
    do
    {
      char c1 = this.chars[(this.pos++)];
      switch (c1)
      {
      case '(': 
        addSubpath();
        break;
      case ',': 
        addCurrentProperty();
        break;
      case ':': 
        this.startPos = this.pos;
        return;
      case ')': 
        addCurrentProperty();
        popSubpath();
      }
    } while (this.pos < this.eof);
  }
  
  private void addSubpath()
  {
    pushPath(currentWord());
  }
  
  private void addCurrentProperty()
  {
    String w = currentWord();
    if (w.length() > 0) {
      this.currentPathProps.addProperty(w);
    }
  }
  
  private String currentWord()
  {
    if (this.startPos == this.pos) {
      return "";
    }
    String currentWord = this.source.substring(this.startPos, this.pos - 1);
    this.startPos = this.pos;
    return currentWord;
  }
  
  private void pushPath(String title)
  {
    if (!"".equals(title)) {
      this.currentPathProps = this.currentPathProps.addChild(title);
    }
  }
  
  private void popSubpath()
  {
    this.currentPathProps = this.currentPathProps.getParent();
  }
}
