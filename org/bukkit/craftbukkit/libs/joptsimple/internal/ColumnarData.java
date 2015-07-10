package org.bukkit.craftbukkit.libs.joptsimple.internal;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ColumnarData
{
  private static final String LINE_SEPARATOR = System.getProperty("line.separator");
  private static final int TOTAL_WIDTH = 80;
  private final ColumnWidthCalculator widthCalculator;
  private final List<Column> columns;
  private final String[] headers;
  
  public ColumnarData(String... headers)
  {
    this.headers = ((String[])headers.clone());
    this.widthCalculator = new ColumnWidthCalculator();
    this.columns = new LinkedList();
    
    clear();
  }
  
  public void addRow(Object... rowData)
  {
    int[] numberOfCellsAddedAt = addRowCells(rowData);
    addPaddingCells(numberOfCellsAddedAt);
  }
  
  public String format()
  {
    StringBuilder buffer = new StringBuilder();
    
    writeHeadersOn(buffer);
    writeSeparatorsOn(buffer);
    writeRowsOn(buffer);
    
    return buffer.toString();
  }
  
  public final void clear()
  {
    this.columns.clear();
    
    int desiredColumnWidth = this.widthCalculator.calculate(80, this.headers.length);
    for (String each : this.headers) {
      this.columns.add(new Column(each, desiredColumnWidth));
    }
  }
  
  private void writeHeadersOn(StringBuilder buffer)
  {
    for (Iterator<Column> iter = this.columns.iterator(); iter.hasNext();) {
      ((Column)iter.next()).writeHeaderOn(buffer, iter.hasNext());
    }
    buffer.append(LINE_SEPARATOR);
  }
  
  private void writeSeparatorsOn(StringBuilder buffer)
  {
    for (Iterator<Column> iter = this.columns.iterator(); iter.hasNext();) {
      ((Column)iter.next()).writeSeparatorOn(buffer, iter.hasNext());
    }
    buffer.append(LINE_SEPARATOR);
  }
  
  private void writeRowsOn(StringBuilder buffer)
  {
    int maxHeight = ((Column)Collections.max(this.columns, Column.BY_HEIGHT)).height();
    for (int i = 0; i < maxHeight; i++) {
      writeRowOn(buffer, i);
    }
  }
  
  private void writeRowOn(StringBuilder buffer, int rowIndex)
  {
    for (Iterator<Column> iter = this.columns.iterator(); iter.hasNext();) {
      ((Column)iter.next()).writeCellOn(rowIndex, buffer, iter.hasNext());
    }
    buffer.append(LINE_SEPARATOR);
  }
  
  private int arrayMax(int[] numbers)
  {
    int maximum = Integer.MIN_VALUE;
    for (int each : numbers) {
      maximum = Math.max(maximum, each);
    }
    return maximum;
  }
  
  private int[] addRowCells(Object... rowData)
  {
    int[] cellsAddedAt = new int[rowData.length];
    
    Iterator<Column> iter = this.columns.iterator();
    for (int i = 0; (iter.hasNext()) && (i < rowData.length); i++) {
      cellsAddedAt[i] = ((Column)iter.next()).addCells(rowData[i]);
    }
    return cellsAddedAt;
  }
  
  private void addPaddingCells(int... numberOfCellsAddedAt)
  {
    int maxHeight = arrayMax(numberOfCellsAddedAt);
    
    Iterator<Column> iter = this.columns.iterator();
    for (int i = 0; (iter.hasNext()) && (i < numberOfCellsAddedAt.length); i++) {
      addPaddingCellsForColumn((Column)iter.next(), maxHeight, numberOfCellsAddedAt[i]);
    }
  }
  
  private void addPaddingCellsForColumn(Column column, int maxHeight, int numberOfCellsAdded)
  {
    for (int i = 0; i < maxHeight - numberOfCellsAdded; i++) {
      column.addCell("");
    }
  }
}
