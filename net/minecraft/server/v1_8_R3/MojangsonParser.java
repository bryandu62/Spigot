package net.minecraft.server.v1_8_R3;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MojangsonParser
{
  private static final Logger a = ;
  private static final Pattern b = Pattern.compile("\\[[-+\\d|,\\s]+\\]");
  
  public static NBTTagCompound parse(String ☃)
    throws MojangsonParseException
  {
    ☃ = ☃.trim();
    if (!☃.startsWith("{")) {
      throw new MojangsonParseException("Invalid tag encountered, expected '{' as first char.");
    }
    if (b(☃) != 1) {
      throw new MojangsonParseException("Encountered multiple top tags, only one expected");
    }
    return (NBTTagCompound)a("tag", ☃).a();
  }
  
  static int b(String ☃)
    throws MojangsonParseException
  {
    int ☃ = 0;
    boolean ☃ = false;
    Stack<Character> ☃ = new Stack();
    
    int ☃ = 0;
    while (☃ < ☃.length())
    {
      char ☃ = ☃.charAt(☃);
      if (☃ == '"')
      {
        if (b(☃, ☃))
        {
          if (!☃) {
            throw new MojangsonParseException("Illegal use of \\\": " + ☃);
          }
        }
        else {
          ☃ = !☃;
        }
      }
      else if (!☃) {
        if ((☃ == '{') || (☃ == '['))
        {
          if (☃.isEmpty()) {
            ☃++;
          }
          ☃.push(Character.valueOf(☃));
        }
        else
        {
          if ((☃ == '}') && ((☃.isEmpty()) || (((Character)☃.pop()).charValue() != '{'))) {
            throw new MojangsonParseException("Unbalanced curly brackets {}: " + ☃);
          }
          if ((☃ == ']') && ((☃.isEmpty()) || (((Character)☃.pop()).charValue() != '['))) {
            throw new MojangsonParseException("Unbalanced square brackets []: " + ☃);
          }
        }
      }
      ☃++;
    }
    if (☃) {
      throw new MojangsonParseException("Unbalanced quotation: " + ☃);
    }
    if (!☃.isEmpty()) {
      throw new MojangsonParseException("Unbalanced brackets: " + ☃);
    }
    if ((☃ == 0) && (!☃.isEmpty())) {
      ☃ = 1;
    }
    return ☃;
  }
  
  static MojangsonTypeParser a(String... ☃)
    throws MojangsonParseException
  {
    return a(☃[0], ☃[1]);
  }
  
  static MojangsonTypeParser a(String ☃, String ☃)
    throws MojangsonParseException
  {
    ☃ = ☃.trim();
    if (☃.startsWith("{"))
    {
      ☃ = ☃.substring(1, ☃.length() - 1);
      
      MojangsonCompoundParser ☃ = new MojangsonCompoundParser(☃);
      while (☃.length() > 0)
      {
        String ☃ = b(☃, true);
        if (☃.length() > 0)
        {
          boolean ☃ = false;
          ☃.b.add(a(☃, ☃));
        }
        if (☃.length() < ☃.length() + 1) {
          break;
        }
        char ☃ = ☃.charAt(☃.length());
        if ((☃ != ',') && (☃ != '{') && (☃ != '}') && (☃ != '[') && (☃ != ']')) {
          throw new MojangsonParseException("Unexpected token '" + ☃ + "' at: " + ☃.substring(☃.length()));
        }
        ☃ = ☃.substring(☃.length() + 1);
      }
      return ☃;
    }
    if ((☃.startsWith("[")) && (!b.matcher(☃).matches()))
    {
      ☃ = ☃.substring(1, ☃.length() - 1);
      
      MojangsonListParser ☃ = new MojangsonListParser(☃);
      while (☃.length() > 0)
      {
        String ☃ = b(☃, false);
        if (☃.length() > 0)
        {
          boolean ☃ = true;
          ☃.b.add(a(☃, ☃));
        }
        if (☃.length() < ☃.length() + 1) {
          break;
        }
        char ☃ = ☃.charAt(☃.length());
        if ((☃ != ',') && (☃ != '{') && (☃ != '}') && (☃ != '[') && (☃ != ']')) {
          throw new MojangsonParseException("Unexpected token '" + ☃ + "' at: " + ☃.substring(☃.length()));
        }
        ☃ = ☃.substring(☃.length() + 1);
      }
      return ☃;
    }
    return new MojangsonPrimitiveParser(☃, ☃);
  }
  
  private static MojangsonTypeParser a(String ☃, boolean ☃)
    throws MojangsonParseException
  {
    String ☃ = c(☃, ☃);
    String ☃ = d(☃, ☃);
    return a(new String[] { ☃, ☃ });
  }
  
  private static String b(String ☃, boolean ☃)
    throws MojangsonParseException
  {
    int ☃ = a(☃, ':');
    int ☃ = a(☃, ',');
    if (☃)
    {
      if (☃ == -1) {
        throw new MojangsonParseException("Unable to locate name/value separator for string: " + ☃);
      }
      if ((☃ != -1) && (☃ < ☃)) {
        throw new MojangsonParseException("Name error at: " + ☃);
      }
    }
    else if ((☃ == -1) || (☃ > ☃))
    {
      ☃ = -1;
    }
    return a(☃, ☃);
  }
  
  private static String a(String ☃, int ☃)
    throws MojangsonParseException
  {
    Stack<Character> ☃ = new Stack();
    int ☃ = ☃ + 1;
    boolean ☃ = false;
    boolean ☃ = false;
    boolean ☃ = false;
    int ☃ = 0;
    while (☃ < ☃.length())
    {
      char ☃ = ☃.charAt(☃);
      if (☃ == '"')
      {
        if (b(☃, ☃))
        {
          if (!☃) {
            throw new MojangsonParseException("Illegal use of \\\": " + ☃);
          }
        }
        else
        {
          ☃ = !☃;
          if ((☃) && (!☃)) {
            ☃ = true;
          }
          if (!☃) {
            ☃ = ☃;
          }
        }
      }
      else if (!☃) {
        if ((☃ == '{') || (☃ == '['))
        {
          ☃.push(Character.valueOf(☃));
        }
        else
        {
          if ((☃ == '}') && ((☃.isEmpty()) || (((Character)☃.pop()).charValue() != '{'))) {
            throw new MojangsonParseException("Unbalanced curly brackets {}: " + ☃);
          }
          if ((☃ == ']') && ((☃.isEmpty()) || (((Character)☃.pop()).charValue() != '['))) {
            throw new MojangsonParseException("Unbalanced square brackets []: " + ☃);
          }
          if ((☃ == ',') && 
            (☃.isEmpty())) {
            return ☃.substring(0, ☃);
          }
        }
      }
      if (!Character.isWhitespace(☃))
      {
        if ((!☃) && (☃) && (☃ != ☃)) {
          return ☃.substring(0, ☃ + 1);
        }
        ☃ = true;
      }
      ☃++;
    }
    return ☃.substring(0, ☃);
  }
  
  private static String c(String ☃, boolean ☃)
    throws MojangsonParseException
  {
    if (☃)
    {
      ☃ = ☃.trim();
      if ((☃.startsWith("{")) || (☃.startsWith("["))) {
        return "";
      }
    }
    int ☃ = a(☃, ':');
    if (☃ == -1)
    {
      if (☃) {
        return "";
      }
      throw new MojangsonParseException("Unable to locate name/value separator for string: " + ☃);
    }
    return ☃.substring(0, ☃).trim();
  }
  
  private static String d(String ☃, boolean ☃)
    throws MojangsonParseException
  {
    if (☃)
    {
      ☃ = ☃.trim();
      if ((☃.startsWith("{")) || (☃.startsWith("["))) {
        return ☃;
      }
    }
    int ☃ = a(☃, ':');
    if (☃ == -1)
    {
      if (☃) {
        return ☃;
      }
      throw new MojangsonParseException("Unable to locate name/value separator for string: " + ☃);
    }
    return ☃.substring(☃ + 1).trim();
  }
  
  private static int a(String ☃, char ☃)
  {
    int ☃ = 0;
    boolean ☃ = true;
    while (☃ < ☃.length())
    {
      char ☃ = ☃.charAt(☃);
      if (☃ == '"')
      {
        if (!b(☃, ☃)) {
          ☃ = !☃;
        }
      }
      else if (☃)
      {
        if (☃ == ☃) {
          return ☃;
        }
        if ((☃ == '{') || (☃ == '[')) {
          return -1;
        }
      }
      ☃++;
    }
    return -1;
  }
  
  private static boolean b(String ☃, int ☃)
  {
    return (☃ > 0) && (☃.charAt(☃ - 1) == '\\') && (!b(☃, ☃ - 1));
  }
  
  static abstract class MojangsonTypeParser
  {
    protected String a;
    
    public abstract NBTBase a()
      throws MojangsonParseException;
  }
  
  static class MojangsonCompoundParser
    extends MojangsonParser.MojangsonTypeParser
  {
    protected List<MojangsonParser.MojangsonTypeParser> b = Lists.newArrayList();
    
    public MojangsonCompoundParser(String ☃)
    {
      this.a = ☃;
    }
    
    public NBTBase a()
      throws MojangsonParseException
    {
      NBTTagCompound ☃ = new NBTTagCompound();
      for (MojangsonParser.MojangsonTypeParser ☃ : this.b) {
        ☃.set(☃.a, ☃.a());
      }
      return ☃;
    }
  }
  
  static class MojangsonListParser
    extends MojangsonParser.MojangsonTypeParser
  {
    protected List<MojangsonParser.MojangsonTypeParser> b = Lists.newArrayList();
    
    public MojangsonListParser(String ☃)
    {
      this.a = ☃;
    }
    
    public NBTBase a()
      throws MojangsonParseException
    {
      NBTTagList ☃ = new NBTTagList();
      for (MojangsonParser.MojangsonTypeParser ☃ : this.b) {
        ☃.add(☃.a());
      }
      return ☃;
    }
  }
  
  static class MojangsonPrimitiveParser
    extends MojangsonParser.MojangsonTypeParser
  {
    private static final Pattern c = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+[d|D]");
    private static final Pattern d = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+[f|F]");
    private static final Pattern e = Pattern.compile("[-+]?[0-9]+[b|B]");
    private static final Pattern f = Pattern.compile("[-+]?[0-9]+[l|L]");
    private static final Pattern g = Pattern.compile("[-+]?[0-9]+[s|S]");
    private static final Pattern h = Pattern.compile("[-+]?[0-9]+");
    private static final Pattern i = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+");
    private static final Splitter j = Splitter.on(',').omitEmptyStrings();
    protected String b;
    
    public MojangsonPrimitiveParser(String ☃, String ☃)
    {
      this.a = ☃;
      this.b = ☃;
    }
    
    public NBTBase a()
      throws MojangsonParseException
    {
      try
      {
        if (c.matcher(this.b).matches()) {
          return new NBTTagDouble(Double.parseDouble(this.b.substring(0, this.b.length() - 1)));
        }
        if (d.matcher(this.b).matches()) {
          return new NBTTagFloat(Float.parseFloat(this.b.substring(0, this.b.length() - 1)));
        }
        if (e.matcher(this.b).matches()) {
          return new NBTTagByte(Byte.parseByte(this.b.substring(0, this.b.length() - 1)));
        }
        if (f.matcher(this.b).matches()) {
          return new NBTTagLong(Long.parseLong(this.b.substring(0, this.b.length() - 1)));
        }
        if (g.matcher(this.b).matches()) {
          return new NBTTagShort(Short.parseShort(this.b.substring(0, this.b.length() - 1)));
        }
        if (h.matcher(this.b).matches()) {
          return new NBTTagInt(Integer.parseInt(this.b));
        }
        if (i.matcher(this.b).matches()) {
          return new NBTTagDouble(Double.parseDouble(this.b));
        }
        if ((this.b.equalsIgnoreCase("true")) || (this.b.equalsIgnoreCase("false"))) {
          return new NBTTagByte((byte)(Boolean.parseBoolean(this.b) ? 1 : 0));
        }
      }
      catch (NumberFormatException ☃)
      {
        this.b = this.b.replaceAll("\\\\\"", "\"");
        return new NBTTagString(this.b);
      }
      if ((this.b.startsWith("[")) && (this.b.endsWith("]")))
      {
        String ☃ = this.b.substring(1, this.b.length() - 1);
        
        String[] ☃ = (String[])Iterables.toArray(j.split(☃), String.class);
        try
        {
          int[] ☃ = new int[☃.length];
          for (int ☃ = 0; ☃ < ☃.length; ☃++) {
            ☃[☃] = Integer.parseInt(☃[☃].trim());
          }
          return new NBTTagIntArray(☃);
        }
        catch (NumberFormatException ☃)
        {
          return new NBTTagString(this.b);
        }
      }
      if ((this.b.startsWith("\"")) && (this.b.endsWith("\""))) {
        this.b = this.b.substring(1, this.b.length() - 1);
      }
      this.b = this.b.replaceAll("\\\\\"", "\"");
      
      StringBuilder ☃ = new StringBuilder();
      for (int ☃ = 0; ☃ < this.b.length(); ☃++) {
        if ((☃ < this.b.length() - 1) && (this.b.charAt(☃) == '\\') && (this.b.charAt(☃ + 1) == '\\'))
        {
          ☃.append('\\');
          ☃++;
        }
        else
        {
          ☃.append(this.b.charAt(☃));
        }
      }
      return new NBTTagString(☃.toString());
    }
  }
}
