package org.bukkit.craftbukkit.libs.joptsimple;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.bukkit.craftbukkit.libs.joptsimple.internal.Classes;
import org.bukkit.craftbukkit.libs.joptsimple.internal.ColumnarData;
import org.bukkit.craftbukkit.libs.joptsimple.internal.Strings;

class HelpFormatter
  implements OptionSpecVisitor
{
  private final ColumnarData grid;
  
  HelpFormatter()
  {
    this.grid = new ColumnarData(new String[] { "Option", "Description" });
  }
  
  String format(Map<String, AbstractOptionSpec<?>> options)
  {
    if (options.isEmpty()) {
      return "No options specified";
    }
    this.grid.clear();
    
    Comparator<AbstractOptionSpec<?>> comparator = new Comparator()
    {
      public int compare(AbstractOptionSpec<?> first, AbstractOptionSpec<?> second)
      {
        return ((String)first.options().iterator().next()).compareTo((String)second.options().iterator().next());
      }
    };
    Set<AbstractOptionSpec<?>> sorted = new TreeSet(comparator);
    sorted.addAll(options.values());
    for (AbstractOptionSpec<?> each : sorted) {
      each.accept(this);
    }
    return this.grid.format();
  }
  
  void addHelpLineFor(AbstractOptionSpec<?> spec, String additionalInfo)
  {
    this.grid.addRow(new Object[] { createOptionDisplay(spec) + additionalInfo, createDescriptionDisplay(spec) });
  }
  
  public void visit(NoArgumentOptionSpec spec)
  {
    addHelpLineFor(spec, "");
  }
  
  public void visit(RequiredArgumentOptionSpec<?> spec)
  {
    visit(spec, '<', '>');
  }
  
  public void visit(OptionalArgumentOptionSpec<?> spec)
  {
    visit(spec, '[', ']');
  }
  
  public void visit(AlternativeLongOptionSpec spec)
  {
    addHelpLineFor(spec, ' ' + Strings.surround(spec.argumentDescription(), '<', '>'));
  }
  
  private void visit(ArgumentAcceptingOptionSpec<?> spec, char begin, char end)
  {
    String argDescription = spec.argumentDescription();
    String typeIndicator = typeIndicator(spec);
    StringBuilder collector = new StringBuilder();
    if (typeIndicator.length() > 0)
    {
      collector.append(typeIndicator);
      if (argDescription.length() > 0) {
        collector.append(": ").append(argDescription);
      }
    }
    else if (argDescription.length() > 0)
    {
      collector.append(argDescription);
    }
    String helpLine = ' ' + Strings.surround(collector.toString(), begin, end);
    
    addHelpLineFor(spec, helpLine);
  }
  
  private String createOptionDisplay(AbstractOptionSpec<?> spec)
  {
    StringBuilder buffer = new StringBuilder();
    for (Iterator<String> iter = spec.options().iterator(); iter.hasNext();)
    {
      String option = (String)iter.next();
      buffer.append(option.length() > 1 ? "--" : ParserRules.HYPHEN);
      buffer.append(option);
      if (iter.hasNext()) {
        buffer.append(", ");
      }
    }
    return buffer.toString();
  }
  
  private String createDescriptionDisplay(AbstractOptionSpec<?> spec)
  {
    List<?> defaultValues = spec.defaultValues();
    if (defaultValues.isEmpty()) {
      return spec.description();
    }
    String defaultValuesDisplay = createDefaultValuesDisplay(defaultValues);
    return spec.description() + ' ' + Strings.surround(new StringBuilder().append("default: ").append(defaultValuesDisplay).toString(), '(', ')');
  }
  
  private String createDefaultValuesDisplay(List<?> defaultValues)
  {
    return defaultValues.size() == 1 ? defaultValues.get(0).toString() : defaultValues.toString();
  }
  
  private static String typeIndicator(ArgumentAcceptingOptionSpec<?> spec)
  {
    String indicator = spec.typeIndicator();
    return (indicator == null) || (String.class.getName().equals(indicator)) ? "" : Classes.shortNameOf(indicator);
  }
}
