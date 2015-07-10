package org.bukkit.craftbukkit.libs.joptsimple.internal;

import java.text.BreakIterator;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class Column
{
  static final Comparator<Column> BY_HEIGHT = new Comparator()
  {
    public int compare(Column first, Column second)
    {
      if (first.height() < second.height()) {
        return -1;
      }
      return first.height() == second.height() ? 0 : 1;
    }
  };
  private final String header;
  private final List<String> data;
  private final int width;
  private int height;
  
  Column(String header, int width)
  {
    this.header = header;
    this.width = Math.max(width, header.length());
    this.data = new LinkedList();
    this.height = 0;
  }
  
  int addCells(Object cellCandidate)
  {
    int originalHeight = this.height;
    
    String source = String.valueOf(cellCandidate).trim();
    for (String eachPiece : source.split(System.getProperty("line.separator"))) {
      processNextEmbeddedLine(eachPiece);
    }
    return this.height - originalHeight;
  }
  
  private void processNextEmbeddedLine(String line)
  {
    BreakIterator words = BreakIterator.getLineInstance(Locale.US);
    words.setText(line);
    
    StringBuilder nextCell = new StringBuilder();
    
    int start = words.first();
    for (int end = words.next(); end != -1; end = words.next())
    {
      nextCell = processNextWord(line, nextCell, start, end);start = end;
    }
    if (nextCell.length() > 0) {
      addCell(nextCell.toString());
    }
  }
  
  private StringBuilder processNextWord(String source, StringBuilder nextCell, int start, int end)
  {
    StringBuilder augmented = nextCell;
    
    String word = source.substring(start, end);
    if (augmented.length() + word.length() > this.width)
    {
      addCell(augmented.toString());
      augmented = new StringBuilder("  ").append(word);
    }
    else
    {
      augmented.append(word);
    }
    return augmented;
  }
  
  void addCell(String newCell)
  {
    this.data.add(newCell);
    this.height += 1;
  }
  
  void writeHeaderOn(StringBuilder buffer, boolean appendSpace)
  {
    buffer.append(this.header).append(Strings.repeat(' ', this.width - this.header.length()));
    if (appendSpace) {
      buffer.append(' ');
    }
  }
  
  void writeSeparatorOn(StringBuilder buffer, boolean appendSpace)
  {
    buffer.append(Strings.repeat('-', this.header.length())).append(Strings.repeat(' ', this.width - this.header.length()));
    if (appendSpace) {
      buffer.append(' ');
    }
  }
  
  void writeCellOn(int index, StringBuilder buffer, boolean appendSpace)
  {
    if (index < this.data.size())
    {
      String item = (String)this.data.get(index);
      
      buffer.append(item).append(Strings.repeat(' ', this.width - item.length()));
      if (appendSpace) {
        buffer.append(' ');
      }
    }
  }
  
  int height()
  {
    return this.height;
  }
}
