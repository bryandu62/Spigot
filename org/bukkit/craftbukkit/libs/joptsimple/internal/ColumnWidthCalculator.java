package org.bukkit.craftbukkit.libs.joptsimple.internal;

class ColumnWidthCalculator
{
  int calculate(int totalWidth, int numberOfColumns)
  {
    if (numberOfColumns == 1) {
      return totalWidth;
    }
    int remainder = totalWidth % numberOfColumns;
    if (remainder == numberOfColumns - 1) {
      return totalWidth / numberOfColumns;
    }
    return totalWidth / numberOfColumns - 1;
  }
}
