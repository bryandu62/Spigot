package org.yaml.snakeyaml.scanner;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.reader.StreamReader;
import org.yaml.snakeyaml.tokens.AliasToken;
import org.yaml.snakeyaml.tokens.AnchorToken;
import org.yaml.snakeyaml.tokens.BlockEndToken;
import org.yaml.snakeyaml.tokens.BlockEntryToken;
import org.yaml.snakeyaml.tokens.BlockMappingStartToken;
import org.yaml.snakeyaml.tokens.BlockSequenceStartToken;
import org.yaml.snakeyaml.tokens.DirectiveToken;
import org.yaml.snakeyaml.tokens.DocumentEndToken;
import org.yaml.snakeyaml.tokens.DocumentStartToken;
import org.yaml.snakeyaml.tokens.FlowEntryToken;
import org.yaml.snakeyaml.tokens.FlowMappingEndToken;
import org.yaml.snakeyaml.tokens.FlowMappingStartToken;
import org.yaml.snakeyaml.tokens.FlowSequenceEndToken;
import org.yaml.snakeyaml.tokens.FlowSequenceStartToken;
import org.yaml.snakeyaml.tokens.KeyToken;
import org.yaml.snakeyaml.tokens.ScalarToken;
import org.yaml.snakeyaml.tokens.StreamEndToken;
import org.yaml.snakeyaml.tokens.StreamStartToken;
import org.yaml.snakeyaml.tokens.TagToken;
import org.yaml.snakeyaml.tokens.TagTuple;
import org.yaml.snakeyaml.tokens.Token;
import org.yaml.snakeyaml.tokens.Token.ID;
import org.yaml.snakeyaml.tokens.ValueToken;
import org.yaml.snakeyaml.util.ArrayStack;
import org.yaml.snakeyaml.util.UriEncoder;

public final class ScannerImpl
  implements Scanner
{
  private static final Pattern NOT_HEXA = Pattern.compile("[^0-9A-Fa-f]");
  public static final Map<Character, String> ESCAPE_REPLACEMENTS = new HashMap();
  public static final Map<Character, Integer> ESCAPE_CODES = new HashMap();
  private final StreamReader reader;
  
  static
  {
    ESCAPE_REPLACEMENTS.put(Character.valueOf('0'), "\000");
    
    ESCAPE_REPLACEMENTS.put(Character.valueOf('a'), "\007");
    
    ESCAPE_REPLACEMENTS.put(Character.valueOf('b'), "\b");
    
    ESCAPE_REPLACEMENTS.put(Character.valueOf('t'), "\t");
    
    ESCAPE_REPLACEMENTS.put(Character.valueOf('n'), "\n");
    
    ESCAPE_REPLACEMENTS.put(Character.valueOf('v'), "\013");
    
    ESCAPE_REPLACEMENTS.put(Character.valueOf('f'), "\f");
    
    ESCAPE_REPLACEMENTS.put(Character.valueOf('r'), "\r");
    
    ESCAPE_REPLACEMENTS.put(Character.valueOf('e'), "\033");
    
    ESCAPE_REPLACEMENTS.put(Character.valueOf(' '), " ");
    
    ESCAPE_REPLACEMENTS.put(Character.valueOf('"'), "\"");
    
    ESCAPE_REPLACEMENTS.put(Character.valueOf('\\'), "\\");
    
    ESCAPE_REPLACEMENTS.put(Character.valueOf('N'), "");
    
    ESCAPE_REPLACEMENTS.put(Character.valueOf('_'), " ");
    
    ESCAPE_REPLACEMENTS.put(Character.valueOf('L'), " ");
    
    ESCAPE_REPLACEMENTS.put(Character.valueOf('P'), " ");
    
    ESCAPE_CODES.put(Character.valueOf('x'), Integer.valueOf(2));
    
    ESCAPE_CODES.put(Character.valueOf('u'), Integer.valueOf(4));
    
    ESCAPE_CODES.put(Character.valueOf('U'), Integer.valueOf(8));
  }
  
  private boolean done = false;
  private int flowLevel = 0;
  private List<Token> tokens;
  private int tokensTaken = 0;
  private int indent = -1;
  private ArrayStack<Integer> indents;
  private boolean allowSimpleKey = true;
  private Map<Integer, SimpleKey> possibleSimpleKeys;
  
  public ScannerImpl(StreamReader reader)
  {
    this.reader = reader;
    this.tokens = new ArrayList(100);
    this.indents = new ArrayStack(10);
    
    this.possibleSimpleKeys = new LinkedHashMap();
    fetchStreamStart();
  }
  
  public boolean checkToken(Token.ID... choices)
  {
    while (needMoreTokens()) {
      fetchMoreTokens();
    }
    if (!this.tokens.isEmpty())
    {
      if (choices.length == 0) {
        return true;
      }
      Token.ID first = ((Token)this.tokens.get(0)).getTokenId();
      for (int i = 0; i < choices.length; i++) {
        if (first == choices[i]) {
          return true;
        }
      }
    }
    return false;
  }
  
  public Token peekToken()
  {
    while (needMoreTokens()) {
      fetchMoreTokens();
    }
    return (Token)this.tokens.get(0);
  }
  
  public Token getToken()
  {
    if (!this.tokens.isEmpty())
    {
      this.tokensTaken += 1;
      return (Token)this.tokens.remove(0);
    }
    return null;
  }
  
  private boolean needMoreTokens()
  {
    if (this.done) {
      return false;
    }
    if (this.tokens.isEmpty()) {
      return true;
    }
    stalePossibleSimpleKeys();
    return nextPossibleSimpleKey() == this.tokensTaken;
  }
  
  private void fetchMoreTokens()
  {
    scanToNextToken();
    
    stalePossibleSimpleKeys();
    
    unwindIndent(this.reader.getColumn());
    
    char ch = this.reader.peek();
    switch (ch)
    {
    case '\000': 
      fetchStreamEnd();
      return;
    case '%': 
      if (checkDirective())
      {
        fetchDirective(); return;
      }
      break;
    case '-': 
      if (checkDocumentStart())
      {
        fetchDocumentStart();
        return;
      }
      if (checkBlockEntry())
      {
        fetchBlockEntry(); return;
      }
      break;
    case '.': 
      if (checkDocumentEnd())
      {
        fetchDocumentEnd(); return;
      }
      break;
    case '[': 
      fetchFlowSequenceStart();
      return;
    case '{': 
      fetchFlowMappingStart();
      return;
    case ']': 
      fetchFlowSequenceEnd();
      return;
    case '}': 
      fetchFlowMappingEnd();
      return;
    case ',': 
      fetchFlowEntry();
      return;
    case '?': 
      if (checkKey())
      {
        fetchKey(); return;
      }
      break;
    case ':': 
      if (checkValue())
      {
        fetchValue(); return;
      }
      break;
    case '*': 
      fetchAlias();
      return;
    case '&': 
      fetchAnchor();
      return;
    case '!': 
      fetchTag();
      return;
    case '|': 
      if (this.flowLevel == 0)
      {
        fetchLiteral(); return;
      }
      break;
    case '>': 
      if (this.flowLevel == 0)
      {
        fetchFolded(); return;
      }
      break;
    case '\'': 
      fetchSingle();
      return;
    case '"': 
      fetchDouble();
      return;
    }
    if (checkPlain())
    {
      fetchPlain();
      return;
    }
    String chRepresentation = String.valueOf(ch);
    for (Character s : ESCAPE_REPLACEMENTS.keySet())
    {
      String v = (String)ESCAPE_REPLACEMENTS.get(s);
      if (v.equals(chRepresentation))
      {
        chRepresentation = "\\" + s;
        break;
      }
    }
    if (ch == '\t') {
      chRepresentation = chRepresentation + "(TAB)";
    }
    String text = String.format("found character %s '%s' that cannot start any token. (Do not use %s for indentation)", new Object[] { Character.valueOf(ch), chRepresentation, chRepresentation });
    
    throw new ScannerException("while scanning for the next token", null, text, this.reader.getMark());
  }
  
  private int nextPossibleSimpleKey()
  {
    if (!this.possibleSimpleKeys.isEmpty()) {
      return ((SimpleKey)this.possibleSimpleKeys.values().iterator().next()).getTokenNumber();
    }
    return -1;
  }
  
  private void stalePossibleSimpleKeys()
  {
    if (!this.possibleSimpleKeys.isEmpty())
    {
      Iterator<SimpleKey> iterator = this.possibleSimpleKeys.values().iterator();
      while (iterator.hasNext())
      {
        SimpleKey key = (SimpleKey)iterator.next();
        if ((key.getLine() != this.reader.getLine()) || (this.reader.getIndex() - key.getIndex() > 1024))
        {
          if (key.isRequired()) {
            throw new ScannerException("while scanning a simple key", key.getMark(), "could not find expected ':'", this.reader.getMark());
          }
          iterator.remove();
        }
      }
    }
  }
  
  private void savePossibleSimpleKey()
  {
    boolean required = (this.flowLevel == 0) && (this.indent == this.reader.getColumn());
    if ((!this.allowSimpleKey) && (required)) {
      throw new YAMLException("A simple key is required only if it is the first token in the current line");
    }
    if (this.allowSimpleKey)
    {
      removePossibleSimpleKey();
      int tokenNumber = this.tokensTaken + this.tokens.size();
      SimpleKey key = new SimpleKey(tokenNumber, required, this.reader.getIndex(), this.reader.getLine(), this.reader.getColumn(), this.reader.getMark());
      
      this.possibleSimpleKeys.put(Integer.valueOf(this.flowLevel), key);
    }
  }
  
  private void removePossibleSimpleKey()
  {
    SimpleKey key = (SimpleKey)this.possibleSimpleKeys.remove(Integer.valueOf(this.flowLevel));
    if ((key != null) && (key.isRequired())) {
      throw new ScannerException("while scanning a simple key", key.getMark(), "could not find expected ':'", this.reader.getMark());
    }
  }
  
  private void unwindIndent(int col)
  {
    if (this.flowLevel != 0) {
      return;
    }
    while (this.indent > col)
    {
      Mark mark = this.reader.getMark();
      this.indent = ((Integer)this.indents.pop()).intValue();
      this.tokens.add(new BlockEndToken(mark, mark));
    }
  }
  
  private boolean addIndent(int column)
  {
    if (this.indent < column)
    {
      this.indents.push(Integer.valueOf(this.indent));
      this.indent = column;
      return true;
    }
    return false;
  }
  
  private void fetchStreamStart()
  {
    Mark mark = this.reader.getMark();
    
    Token token = new StreamStartToken(mark, mark);
    this.tokens.add(token);
  }
  
  private void fetchStreamEnd()
  {
    unwindIndent(-1);
    
    removePossibleSimpleKey();
    this.allowSimpleKey = false;
    this.possibleSimpleKeys.clear();
    
    Mark mark = this.reader.getMark();
    
    Token token = new StreamEndToken(mark, mark);
    this.tokens.add(token);
    
    this.done = true;
  }
  
  private void fetchDirective()
  {
    unwindIndent(-1);
    
    removePossibleSimpleKey();
    this.allowSimpleKey = false;
    
    Token tok = scanDirective();
    this.tokens.add(tok);
  }
  
  private void fetchDocumentStart()
  {
    fetchDocumentIndicator(true);
  }
  
  private void fetchDocumentEnd()
  {
    fetchDocumentIndicator(false);
  }
  
  private void fetchDocumentIndicator(boolean isDocumentStart)
  {
    unwindIndent(-1);
    
    removePossibleSimpleKey();
    this.allowSimpleKey = false;
    
    Mark startMark = this.reader.getMark();
    this.reader.forward(3);
    Mark endMark = this.reader.getMark();
    Token token;
    Token token;
    if (isDocumentStart) {
      token = new DocumentStartToken(startMark, endMark);
    } else {
      token = new DocumentEndToken(startMark, endMark);
    }
    this.tokens.add(token);
  }
  
  private void fetchFlowSequenceStart()
  {
    fetchFlowCollectionStart(false);
  }
  
  private void fetchFlowMappingStart()
  {
    fetchFlowCollectionStart(true);
  }
  
  private void fetchFlowCollectionStart(boolean isMappingStart)
  {
    savePossibleSimpleKey();
    
    this.flowLevel += 1;
    
    this.allowSimpleKey = true;
    
    Mark startMark = this.reader.getMark();
    this.reader.forward(1);
    Mark endMark = this.reader.getMark();
    Token token;
    Token token;
    if (isMappingStart) {
      token = new FlowMappingStartToken(startMark, endMark);
    } else {
      token = new FlowSequenceStartToken(startMark, endMark);
    }
    this.tokens.add(token);
  }
  
  private void fetchFlowSequenceEnd()
  {
    fetchFlowCollectionEnd(false);
  }
  
  private void fetchFlowMappingEnd()
  {
    fetchFlowCollectionEnd(true);
  }
  
  private void fetchFlowCollectionEnd(boolean isMappingEnd)
  {
    removePossibleSimpleKey();
    
    this.flowLevel -= 1;
    
    this.allowSimpleKey = false;
    
    Mark startMark = this.reader.getMark();
    this.reader.forward();
    Mark endMark = this.reader.getMark();
    Token token;
    Token token;
    if (isMappingEnd) {
      token = new FlowMappingEndToken(startMark, endMark);
    } else {
      token = new FlowSequenceEndToken(startMark, endMark);
    }
    this.tokens.add(token);
  }
  
  private void fetchFlowEntry()
  {
    this.allowSimpleKey = true;
    
    removePossibleSimpleKey();
    
    Mark startMark = this.reader.getMark();
    this.reader.forward();
    Mark endMark = this.reader.getMark();
    Token token = new FlowEntryToken(startMark, endMark);
    this.tokens.add(token);
  }
  
  private void fetchBlockEntry()
  {
    if (this.flowLevel == 0)
    {
      if (!this.allowSimpleKey) {
        throw new ScannerException(null, null, "sequence entries are not allowed here", this.reader.getMark());
      }
      if (addIndent(this.reader.getColumn()))
      {
        Mark mark = this.reader.getMark();
        this.tokens.add(new BlockSequenceStartToken(mark, mark));
      }
    }
    this.allowSimpleKey = true;
    
    removePossibleSimpleKey();
    
    Mark startMark = this.reader.getMark();
    this.reader.forward();
    Mark endMark = this.reader.getMark();
    Token token = new BlockEntryToken(startMark, endMark);
    this.tokens.add(token);
  }
  
  private void fetchKey()
  {
    if (this.flowLevel == 0)
    {
      if (!this.allowSimpleKey) {
        throw new ScannerException(null, null, "mapping keys are not allowed here", this.reader.getMark());
      }
      if (addIndent(this.reader.getColumn()))
      {
        Mark mark = this.reader.getMark();
        this.tokens.add(new BlockMappingStartToken(mark, mark));
      }
    }
    this.allowSimpleKey = (this.flowLevel == 0);
    
    removePossibleSimpleKey();
    
    Mark startMark = this.reader.getMark();
    this.reader.forward();
    Mark endMark = this.reader.getMark();
    Token token = new KeyToken(startMark, endMark);
    this.tokens.add(token);
  }
  
  private void fetchValue()
  {
    SimpleKey key = (SimpleKey)this.possibleSimpleKeys.remove(Integer.valueOf(this.flowLevel));
    if (key != null)
    {
      this.tokens.add(key.getTokenNumber() - this.tokensTaken, new KeyToken(key.getMark(), key.getMark()));
      if ((this.flowLevel == 0) && 
        (addIndent(key.getColumn()))) {
        this.tokens.add(key.getTokenNumber() - this.tokensTaken, new BlockMappingStartToken(key.getMark(), key.getMark()));
      }
      this.allowSimpleKey = false;
    }
    else
    {
      if (this.flowLevel == 0) {
        if (!this.allowSimpleKey) {
          throw new ScannerException(null, null, "mapping values are not allowed here", this.reader.getMark());
        }
      }
      if ((this.flowLevel == 0) && 
        (addIndent(this.reader.getColumn())))
      {
        Mark mark = this.reader.getMark();
        this.tokens.add(new BlockMappingStartToken(mark, mark));
      }
      this.allowSimpleKey = (this.flowLevel == 0);
      
      removePossibleSimpleKey();
    }
    Mark startMark = this.reader.getMark();
    this.reader.forward();
    Mark endMark = this.reader.getMark();
    Token token = new ValueToken(startMark, endMark);
    this.tokens.add(token);
  }
  
  private void fetchAlias()
  {
    savePossibleSimpleKey();
    
    this.allowSimpleKey = false;
    
    Token tok = scanAnchor(false);
    this.tokens.add(tok);
  }
  
  private void fetchAnchor()
  {
    savePossibleSimpleKey();
    
    this.allowSimpleKey = false;
    
    Token tok = scanAnchor(true);
    this.tokens.add(tok);
  }
  
  private void fetchTag()
  {
    savePossibleSimpleKey();
    
    this.allowSimpleKey = false;
    
    Token tok = scanTag();
    this.tokens.add(tok);
  }
  
  private void fetchLiteral()
  {
    fetchBlockScalar('|');
  }
  
  private void fetchFolded()
  {
    fetchBlockScalar('>');
  }
  
  private void fetchBlockScalar(char style)
  {
    this.allowSimpleKey = true;
    
    removePossibleSimpleKey();
    
    Token tok = scanBlockScalar(style);
    this.tokens.add(tok);
  }
  
  private void fetchSingle()
  {
    fetchFlowScalar('\'');
  }
  
  private void fetchDouble()
  {
    fetchFlowScalar('"');
  }
  
  private void fetchFlowScalar(char style)
  {
    savePossibleSimpleKey();
    
    this.allowSimpleKey = false;
    
    Token tok = scanFlowScalar(style);
    this.tokens.add(tok);
  }
  
  private void fetchPlain()
  {
    savePossibleSimpleKey();
    
    this.allowSimpleKey = false;
    
    Token tok = scanPlain();
    this.tokens.add(tok);
  }
  
  private boolean checkDirective()
  {
    return this.reader.getColumn() == 0;
  }
  
  private boolean checkDocumentStart()
  {
    if ((this.reader.getColumn() == 0) && 
      ("---".equals(this.reader.prefix(3))) && (Constant.NULL_BL_T_LINEBR.has(this.reader.peek(3)))) {
      return true;
    }
    return false;
  }
  
  private boolean checkDocumentEnd()
  {
    if ((this.reader.getColumn() == 0) && 
      ("...".equals(this.reader.prefix(3))) && (Constant.NULL_BL_T_LINEBR.has(this.reader.peek(3)))) {
      return true;
    }
    return false;
  }
  
  private boolean checkBlockEntry()
  {
    return Constant.NULL_BL_T_LINEBR.has(this.reader.peek(1));
  }
  
  private boolean checkKey()
  {
    if (this.flowLevel != 0) {
      return true;
    }
    return Constant.NULL_BL_T_LINEBR.has(this.reader.peek(1));
  }
  
  private boolean checkValue()
  {
    if (this.flowLevel != 0) {
      return true;
    }
    return Constant.NULL_BL_T_LINEBR.has(this.reader.peek(1));
  }
  
  private boolean checkPlain()
  {
    char ch = this.reader.peek();
    
    return (Constant.NULL_BL_T_LINEBR.hasNo(ch, "-?:,[]{}#&*!|>'\"%@`")) || ((Constant.NULL_BL_T_LINEBR.hasNo(this.reader.peek(1))) && ((ch == '-') || ((this.flowLevel == 0) && ("?:".indexOf(ch) != -1))));
  }
  
  private void scanToNextToken()
  {
    if ((this.reader.getIndex() == 0) && (this.reader.peek() == 65279)) {
      this.reader.forward();
    }
    boolean found = false;
    while (!found)
    {
      int ff = 0;
      while (this.reader.peek(ff) == ' ') {
        ff++;
      }
      if (ff > 0) {
        this.reader.forward(ff);
      }
      if (this.reader.peek() == '#')
      {
        ff = 0;
        while (Constant.NULL_OR_LINEBR.hasNo(this.reader.peek(ff))) {
          ff++;
        }
        if (ff > 0) {
          this.reader.forward(ff);
        }
      }
      if (scanLineBreak().length() != 0)
      {
        if (this.flowLevel == 0) {
          this.allowSimpleKey = true;
        }
      }
      else {
        found = true;
      }
    }
  }
  
  private Token scanDirective()
  {
    Mark startMark = this.reader.getMark();
    
    this.reader.forward();
    String name = scanDirectiveName(startMark);
    List<?> value = null;
    Mark endMark;
    Mark endMark;
    if ("YAML".equals(name))
    {
      value = scanYamlDirectiveValue(startMark);
      endMark = this.reader.getMark();
    }
    else
    {
      Mark endMark;
      if ("TAG".equals(name))
      {
        value = scanTagDirectiveValue(startMark);
        endMark = this.reader.getMark();
      }
      else
      {
        endMark = this.reader.getMark();
        int ff = 0;
        while (Constant.NULL_OR_LINEBR.hasNo(this.reader.peek(ff))) {
          ff++;
        }
        if (ff > 0) {
          this.reader.forward(ff);
        }
      }
    }
    scanDirectiveIgnoredLine(startMark);
    return new DirectiveToken(name, value, startMark, endMark);
  }
  
  private String scanDirectiveName(Mark startMark)
  {
    int length = 0;
    
    char ch = this.reader.peek(length);
    while (Constant.ALPHA.has(ch))
    {
      length++;
      ch = this.reader.peek(length);
    }
    if (length == 0) {
      throw new ScannerException("while scanning a directive", startMark, "expected alphabetic or numeric character, but found " + ch + "(" + ch + ")", this.reader.getMark());
    }
    String value = this.reader.prefixForward(length);
    ch = this.reader.peek();
    if (Constant.NULL_BL_LINEBR.hasNo(ch)) {
      throw new ScannerException("while scanning a directive", startMark, "expected alphabetic or numeric character, but found " + ch + "(" + ch + ")", this.reader.getMark());
    }
    return value;
  }
  
  private List<Integer> scanYamlDirectiveValue(Mark startMark)
  {
    while (this.reader.peek() == ' ') {
      this.reader.forward();
    }
    Integer major = scanYamlDirectiveNumber(startMark);
    if (this.reader.peek() != '.') {
      throw new ScannerException("while scanning a directive", startMark, "expected a digit or '.', but found " + this.reader.peek() + "(" + this.reader.peek() + ")", this.reader.getMark());
    }
    this.reader.forward();
    Integer minor = scanYamlDirectiveNumber(startMark);
    if (Constant.NULL_BL_LINEBR.hasNo(this.reader.peek())) {
      throw new ScannerException("while scanning a directive", startMark, "expected a digit or ' ', but found " + this.reader.peek() + "(" + this.reader.peek() + ")", this.reader.getMark());
    }
    List<Integer> result = new ArrayList(2);
    result.add(major);
    result.add(minor);
    return result;
  }
  
  private Integer scanYamlDirectiveNumber(Mark startMark)
  {
    char ch = this.reader.peek();
    if (!Character.isDigit(ch)) {
      throw new ScannerException("while scanning a directive", startMark, "expected a digit, but found " + ch + "(" + ch + ")", this.reader.getMark());
    }
    int length = 0;
    while (Character.isDigit(this.reader.peek(length))) {
      length++;
    }
    Integer value = Integer.valueOf(Integer.parseInt(this.reader.prefixForward(length)));
    return value;
  }
  
  private List<String> scanTagDirectiveValue(Mark startMark)
  {
    while (this.reader.peek() == ' ') {
      this.reader.forward();
    }
    String handle = scanTagDirectiveHandle(startMark);
    while (this.reader.peek() == ' ') {
      this.reader.forward();
    }
    String prefix = scanTagDirectivePrefix(startMark);
    List<String> result = new ArrayList(2);
    result.add(handle);
    result.add(prefix);
    return result;
  }
  
  private String scanTagDirectiveHandle(Mark startMark)
  {
    String value = scanTagHandle("directive", startMark);
    char ch = this.reader.peek();
    if (ch != ' ') {
      throw new ScannerException("while scanning a directive", startMark, "expected ' ', but found " + this.reader.peek() + "(" + ch + ")", this.reader.getMark());
    }
    return value;
  }
  
  private String scanTagDirectivePrefix(Mark startMark)
  {
    String value = scanTagUri("directive", startMark);
    if (Constant.NULL_BL_LINEBR.hasNo(this.reader.peek())) {
      throw new ScannerException("while scanning a directive", startMark, "expected ' ', but found " + this.reader.peek() + "(" + this.reader.peek() + ")", this.reader.getMark());
    }
    return value;
  }
  
  private String scanDirectiveIgnoredLine(Mark startMark)
  {
    int ff = 0;
    while (this.reader.peek(ff) == ' ') {
      ff++;
    }
    if (ff > 0) {
      this.reader.forward(ff);
    }
    if (this.reader.peek() == '#')
    {
      ff = 0;
      while (Constant.NULL_OR_LINEBR.hasNo(this.reader.peek(ff))) {
        ff++;
      }
      this.reader.forward(ff);
    }
    char ch = this.reader.peek();
    String lineBreak = scanLineBreak();
    if ((lineBreak.length() == 0) && (ch != 0)) {
      throw new ScannerException("while scanning a directive", startMark, "expected a comment or a line break, but found " + ch + "(" + ch + ")", this.reader.getMark());
    }
    return lineBreak;
  }
  
  private Token scanAnchor(boolean isAnchor)
  {
    Mark startMark = this.reader.getMark();
    char indicator = this.reader.peek();
    String name = indicator == '*' ? "alias" : "anchor";
    this.reader.forward();
    int length = 0;
    char ch = this.reader.peek(length);
    while (Constant.ALPHA.has(ch))
    {
      length++;
      ch = this.reader.peek(length);
    }
    if (length == 0) {
      throw new ScannerException("while scanning an " + name, startMark, "expected alphabetic or numeric character, but found but found " + ch, this.reader.getMark());
    }
    String value = this.reader.prefixForward(length);
    ch = this.reader.peek();
    if (Constant.NULL_BL_T_LINEBR.hasNo(ch, "?:,]}%@`")) {
      throw new ScannerException("while scanning an " + name, startMark, "expected alphabetic or numeric character, but found " + ch + "(" + this.reader.peek() + ")", this.reader.getMark());
    }
    Mark endMark = this.reader.getMark();
    Token tok;
    Token tok;
    if (isAnchor) {
      tok = new AnchorToken(value, startMark, endMark);
    } else {
      tok = new AliasToken(value, startMark, endMark);
    }
    return tok;
  }
  
  private Token scanTag()
  {
    Mark startMark = this.reader.getMark();
    
    char ch = this.reader.peek(1);
    String handle = null;
    String suffix = null;
    if (ch == '<')
    {
      this.reader.forward(2);
      suffix = scanTagUri("tag", startMark);
      if (this.reader.peek() != '>') {
        throw new ScannerException("while scanning a tag", startMark, "expected '>', but found '" + this.reader.peek() + "' (" + this.reader.peek() + ")", this.reader.getMark());
      }
      this.reader.forward();
    }
    else if (Constant.NULL_BL_T_LINEBR.has(ch))
    {
      suffix = "!";
      this.reader.forward();
    }
    else
    {
      int length = 1;
      boolean useHandle = false;
      while (Constant.NULL_BL_LINEBR.hasNo(ch))
      {
        if (ch == '!')
        {
          useHandle = true;
          break;
        }
        length++;
        ch = this.reader.peek(length);
      }
      handle = "!";
      if (useHandle)
      {
        handle = scanTagHandle("tag", startMark);
      }
      else
      {
        handle = "!";
        this.reader.forward();
      }
      suffix = scanTagUri("tag", startMark);
    }
    ch = this.reader.peek();
    if (Constant.NULL_BL_LINEBR.hasNo(ch)) {
      throw new ScannerException("while scanning a tag", startMark, "expected ' ', but found '" + ch + "' (" + ch + ")", this.reader.getMark());
    }
    TagTuple value = new TagTuple(handle, suffix);
    Mark endMark = this.reader.getMark();
    return new TagToken(value, startMark, endMark);
  }
  
  private Token scanBlockScalar(char style)
  {
    boolean folded;
    boolean folded;
    if (style == '>') {
      folded = true;
    } else {
      folded = false;
    }
    StringBuilder chunks = new StringBuilder();
    Mark startMark = this.reader.getMark();
    
    this.reader.forward();
    Chomping chompi = scanBlockScalarIndicators(startMark);
    int increment = chompi.getIncrement();
    scanBlockScalarIgnoredLine(startMark);
    
    int minIndent = this.indent + 1;
    if (minIndent < 1) {
      minIndent = 1;
    }
    String breaks = null;
    int maxIndent = 0;
    int indent = 0;
    Mark endMark;
    if (increment == -1)
    {
      Object[] brme = scanBlockScalarIndentation();
      breaks = (String)brme[0];
      maxIndent = ((Integer)brme[1]).intValue();
      Mark endMark = (Mark)brme[2];
      indent = Math.max(minIndent, maxIndent);
    }
    else
    {
      indent = minIndent + increment - 1;
      Object[] brme = scanBlockScalarBreaks(indent);
      breaks = (String)brme[0];
      endMark = (Mark)brme[1];
    }
    String lineBreak = "";
    while ((this.reader.getColumn() == indent) && (this.reader.peek() != 0))
    {
      chunks.append(breaks);
      boolean leadingNonSpace = " \t".indexOf(this.reader.peek()) == -1;
      int length = 0;
      while (Constant.NULL_OR_LINEBR.hasNo(this.reader.peek(length))) {
        length++;
      }
      chunks.append(this.reader.prefixForward(length));
      lineBreak = scanLineBreak();
      Object[] brme = scanBlockScalarBreaks(indent);
      breaks = (String)brme[0];
      endMark = (Mark)brme[1];
      if ((this.reader.getColumn() != indent) || (this.reader.peek() == 0)) {
        break;
      }
      if ((folded) && ("\n".equals(lineBreak)) && (leadingNonSpace) && (" \t".indexOf(this.reader.peek()) == -1))
      {
        if (breaks.length() == 0) {
          chunks.append(" ");
        }
      }
      else {
        chunks.append(lineBreak);
      }
    }
    if (chompi.chompTailIsNotFalse()) {
      chunks.append(lineBreak);
    }
    if (chompi.chompTailIsTrue()) {
      chunks.append(breaks);
    }
    return new ScalarToken(chunks.toString(), false, startMark, endMark, style);
  }
  
  private Chomping scanBlockScalarIndicators(Mark startMark)
  {
    Boolean chomping = null;
    int increment = -1;
    char ch = this.reader.peek();
    if ((ch == '-') || (ch == '+'))
    {
      if (ch == '+') {
        chomping = Boolean.TRUE;
      } else {
        chomping = Boolean.FALSE;
      }
      this.reader.forward();
      ch = this.reader.peek();
      if (Character.isDigit(ch))
      {
        increment = Integer.parseInt(String.valueOf(ch));
        if (increment == 0) {
          throw new ScannerException("while scanning a block scalar", startMark, "expected indentation indicator in the range 1-9, but found 0", this.reader.getMark());
        }
        this.reader.forward();
      }
    }
    else if (Character.isDigit(ch))
    {
      increment = Integer.parseInt(String.valueOf(ch));
      if (increment == 0) {
        throw new ScannerException("while scanning a block scalar", startMark, "expected indentation indicator in the range 1-9, but found 0", this.reader.getMark());
      }
      this.reader.forward();
      ch = this.reader.peek();
      if ((ch == '-') || (ch == '+'))
      {
        if (ch == '+') {
          chomping = Boolean.TRUE;
        } else {
          chomping = Boolean.FALSE;
        }
        this.reader.forward();
      }
    }
    ch = this.reader.peek();
    if (Constant.NULL_BL_LINEBR.hasNo(ch)) {
      throw new ScannerException("while scanning a block scalar", startMark, "expected chomping or indentation indicators, but found " + ch, this.reader.getMark());
    }
    return new Chomping(chomping, increment);
  }
  
  private String scanBlockScalarIgnoredLine(Mark startMark)
  {
    int ff = 0;
    while (this.reader.peek(ff) == ' ') {
      ff++;
    }
    if (ff > 0) {
      this.reader.forward(ff);
    }
    if (this.reader.peek() == '#')
    {
      ff = 0;
      while (Constant.NULL_OR_LINEBR.hasNo(this.reader.peek(ff))) {
        ff++;
      }
      if (ff > 0) {
        this.reader.forward(ff);
      }
    }
    char ch = this.reader.peek();
    String lineBreak = scanLineBreak();
    if ((lineBreak.length() == 0) && (ch != 0)) {
      throw new ScannerException("while scanning a block scalar", startMark, "expected a comment or a line break, but found " + ch, this.reader.getMark());
    }
    return lineBreak;
  }
  
  private Object[] scanBlockScalarIndentation()
  {
    StringBuilder chunks = new StringBuilder();
    int maxIndent = 0;
    Mark endMark = this.reader.getMark();
    while (Constant.LINEBR.has(this.reader.peek(), " \r")) {
      if (this.reader.peek() != ' ')
      {
        chunks.append(scanLineBreak());
        endMark = this.reader.getMark();
      }
      else
      {
        this.reader.forward();
        if (this.reader.getColumn() > maxIndent) {
          maxIndent = this.reader.getColumn();
        }
      }
    }
    return new Object[] { chunks.toString(), Integer.valueOf(maxIndent), endMark };
  }
  
  private Object[] scanBlockScalarBreaks(int indent)
  {
    StringBuilder chunks = new StringBuilder();
    Mark endMark = this.reader.getMark();
    int ff = 0;
    int col = this.reader.getColumn();
    while ((col < indent) && (this.reader.peek(ff) == ' '))
    {
      ff++;
      col++;
    }
    if (ff > 0) {
      this.reader.forward(ff);
    }
    String lineBreak = null;
    while ((lineBreak = scanLineBreak()).length() != 0)
    {
      chunks.append(lineBreak);
      endMark = this.reader.getMark();
      
      ff = 0;
      col = this.reader.getColumn();
      while ((col < indent) && (this.reader.peek(ff) == ' '))
      {
        ff++;
        col++;
      }
      if (ff > 0) {
        this.reader.forward(ff);
      }
    }
    return new Object[] { chunks.toString(), endMark };
  }
  
  private Token scanFlowScalar(char style)
  {
    boolean _double;
    boolean _double;
    if (style == '"') {
      _double = true;
    } else {
      _double = false;
    }
    StringBuilder chunks = new StringBuilder();
    Mark startMark = this.reader.getMark();
    char quote = this.reader.peek();
    this.reader.forward();
    chunks.append(scanFlowScalarNonSpaces(_double, startMark));
    while (this.reader.peek() != quote)
    {
      chunks.append(scanFlowScalarSpaces(startMark));
      chunks.append(scanFlowScalarNonSpaces(_double, startMark));
    }
    this.reader.forward();
    Mark endMark = this.reader.getMark();
    return new ScalarToken(chunks.toString(), false, startMark, endMark, style);
  }
  
  private String scanFlowScalarNonSpaces(boolean doubleQuoted, Mark startMark)
  {
    StringBuilder chunks = new StringBuilder();
    for (;;)
    {
      int length = 0;
      while (Constant.NULL_BL_T_LINEBR.hasNo(this.reader.peek(length), "'\"\\")) {
        length++;
      }
      if (length != 0) {
        chunks.append(this.reader.prefixForward(length));
      }
      char ch = this.reader.peek();
      if ((!doubleQuoted) && (ch == '\'') && (this.reader.peek(1) == '\''))
      {
        chunks.append("'");
        this.reader.forward(2);
      }
      else if (((doubleQuoted) && (ch == '\'')) || ((!doubleQuoted) && ("\"\\".indexOf(ch) != -1)))
      {
        chunks.append(ch);
        this.reader.forward();
      }
      else if ((doubleQuoted) && (ch == '\\'))
      {
        this.reader.forward();
        ch = this.reader.peek();
        if (ESCAPE_REPLACEMENTS.containsKey(Character.valueOf(ch)))
        {
          chunks.append((String)ESCAPE_REPLACEMENTS.get(Character.valueOf(ch)));
          this.reader.forward();
        }
        else if (ESCAPE_CODES.containsKey(Character.valueOf(ch)))
        {
          length = ((Integer)ESCAPE_CODES.get(Character.valueOf(ch))).intValue();
          this.reader.forward();
          String hex = this.reader.prefix(length);
          if (NOT_HEXA.matcher(hex).find()) {
            throw new ScannerException("while scanning a double-quoted scalar", startMark, "expected escape sequence of " + length + " hexadecimal numbers, but found: " + hex, this.reader.getMark());
          }
          int decimal = Integer.parseInt(hex, 16);
          String unicode = new String(Character.toChars(decimal));
          chunks.append(unicode);
          this.reader.forward(length);
        }
        else if (scanLineBreak().length() != 0)
        {
          chunks.append(scanFlowScalarBreaks(startMark));
        }
        else
        {
          throw new ScannerException("while scanning a double-quoted scalar", startMark, "found unknown escape character " + ch + "(" + ch + ")", this.reader.getMark());
        }
      }
      else
      {
        return chunks.toString();
      }
    }
  }
  
  private String scanFlowScalarSpaces(Mark startMark)
  {
    StringBuilder chunks = new StringBuilder();
    int length = 0;
    while (" \t".indexOf(this.reader.peek(length)) != -1) {
      length++;
    }
    String whitespaces = this.reader.prefixForward(length);
    char ch = this.reader.peek();
    if (ch == 0) {
      throw new ScannerException("while scanning a quoted scalar", startMark, "found unexpected end of stream", this.reader.getMark());
    }
    String lineBreak = scanLineBreak();
    if (lineBreak.length() != 0)
    {
      String breaks = scanFlowScalarBreaks(startMark);
      if (!"\n".equals(lineBreak)) {
        chunks.append(lineBreak);
      } else if (breaks.length() == 0) {
        chunks.append(" ");
      }
      chunks.append(breaks);
    }
    else
    {
      chunks.append(whitespaces);
    }
    return chunks.toString();
  }
  
  private String scanFlowScalarBreaks(Mark startMark)
  {
    StringBuilder chunks = new StringBuilder();
    for (;;)
    {
      String prefix = this.reader.prefix(3);
      if ((("---".equals(prefix)) || ("...".equals(prefix))) && (Constant.NULL_BL_T_LINEBR.has(this.reader.peek(3)))) {
        throw new ScannerException("while scanning a quoted scalar", startMark, "found unexpected document separator", this.reader.getMark());
      }
      while (" \t".indexOf(this.reader.peek()) != -1) {
        this.reader.forward();
      }
      String lineBreak = scanLineBreak();
      if (lineBreak.length() != 0) {
        chunks.append(lineBreak);
      } else {
        return chunks.toString();
      }
    }
  }
  
  private Token scanPlain()
  {
    StringBuilder chunks = new StringBuilder();
    Mark startMark = this.reader.getMark();
    Mark endMark = startMark;
    int indent = this.indent + 1;
    String spaces = "";
    for (;;)
    {
      int length = 0;
      if (this.reader.peek() == '#') {
        break;
      }
      char ch;
      for (;;)
      {
        ch = this.reader.peek(length);
        if ((Constant.NULL_BL_T_LINEBR.has(ch)) || ((this.flowLevel == 0) && (ch == ':') && (Constant.NULL_BL_T_LINEBR.has(this.reader.peek(length + 1)))) || ((this.flowLevel != 0) && (",:?[]{}".indexOf(ch) != -1))) {
          break;
        }
        length++;
      }
      if ((this.flowLevel != 0) && (ch == ':') && (Constant.NULL_BL_T_LINEBR.hasNo(this.reader.peek(length + 1), ",[]{}")))
      {
        this.reader.forward(length);
        throw new ScannerException("while scanning a plain scalar", startMark, "found unexpected ':'", this.reader.getMark(), "Please check http://pyyaml.org/wiki/YAMLColonInFlowContext for details.");
      }
      if (length == 0) {
        break;
      }
      this.allowSimpleKey = false;
      chunks.append(spaces);
      chunks.append(this.reader.prefixForward(length));
      endMark = this.reader.getMark();
      spaces = scanPlainSpaces();
      if ((spaces.length() == 0) || (this.reader.peek() == '#') || ((this.flowLevel == 0) && (this.reader.getColumn() < indent))) {
        break;
      }
    }
    return new ScalarToken(chunks.toString(), startMark, endMark, true);
  }
  
  private String scanPlainSpaces()
  {
    int length = 0;
    while ((this.reader.peek(length) == ' ') || (this.reader.peek(length) == '\t')) {
      length++;
    }
    String whitespaces = this.reader.prefixForward(length);
    String lineBreak = scanLineBreak();
    if (lineBreak.length() != 0)
    {
      this.allowSimpleKey = true;
      String prefix = this.reader.prefix(3);
      if (("---".equals(prefix)) || (("...".equals(prefix)) && (Constant.NULL_BL_T_LINEBR.has(this.reader.peek(3))))) {
        return "";
      }
      StringBuilder breaks = new StringBuilder();
      for (;;)
      {
        if (this.reader.peek() == ' ')
        {
          this.reader.forward();
        }
        else
        {
          String lb = scanLineBreak();
          if (lb.length() == 0) {
            break;
          }
          breaks.append(lb);
          prefix = this.reader.prefix(3);
          if (("---".equals(prefix)) || (("...".equals(prefix)) && (Constant.NULL_BL_T_LINEBR.has(this.reader.peek(3))))) {
            return "";
          }
        }
      }
      if (!"\n".equals(lineBreak)) {
        return lineBreak + breaks;
      }
      if (breaks.length() == 0) {
        return " ";
      }
      return breaks.toString();
    }
    return whitespaces;
  }
  
  private String scanTagHandle(String name, Mark startMark)
  {
    char ch = this.reader.peek();
    if (ch != '!') {
      throw new ScannerException("while scanning a " + name, startMark, "expected '!', but found " + ch + "(" + ch + ")", this.reader.getMark());
    }
    int length = 1;
    ch = this.reader.peek(length);
    if (ch != ' ')
    {
      while (Constant.ALPHA.has(ch))
      {
        length++;
        ch = this.reader.peek(length);
      }
      if (ch != '!')
      {
        this.reader.forward(length);
        throw new ScannerException("while scanning a " + name, startMark, "expected '!', but found " + ch + "(" + ch + ")", this.reader.getMark());
      }
      length++;
    }
    String value = this.reader.prefixForward(length);
    return value;
  }
  
  private String scanTagUri(String name, Mark startMark)
  {
    StringBuilder chunks = new StringBuilder();
    
    int length = 0;
    char ch = this.reader.peek(length);
    while (Constant.URI_CHARS.has(ch))
    {
      if (ch == '%')
      {
        chunks.append(this.reader.prefixForward(length));
        length = 0;
        chunks.append(scanUriEscapes(name, startMark));
      }
      else
      {
        length++;
      }
      ch = this.reader.peek(length);
    }
    if (length != 0)
    {
      chunks.append(this.reader.prefixForward(length));
      length = 0;
    }
    if (chunks.length() == 0) {
      throw new ScannerException("while scanning a " + name, startMark, "expected URI, but found " + ch + "(" + ch + ")", this.reader.getMark());
    }
    return chunks.toString();
  }
  
  private String scanUriEscapes(String name, Mark startMark)
  {
    int length = 1;
    while (this.reader.peek(length * 3) == '%') {
      length++;
    }
    Mark beginningMark = this.reader.getMark();
    ByteBuffer buff = ByteBuffer.allocate(length);
    while (this.reader.peek() == '%')
    {
      this.reader.forward();
      try
      {
        byte code = (byte)Integer.parseInt(this.reader.prefix(2), 16);
        buff.put(code);
      }
      catch (NumberFormatException nfe)
      {
        throw new ScannerException("while scanning a " + name, startMark, "expected URI escape sequence of 2 hexadecimal numbers, but found " + this.reader.peek() + "(" + this.reader.peek() + ") and " + this.reader.peek(1) + "(" + this.reader.peek(1) + ")", this.reader.getMark());
      }
      this.reader.forward(2);
    }
    buff.flip();
    try
    {
      return UriEncoder.decode(buff);
    }
    catch (CharacterCodingException e)
    {
      throw new ScannerException("while scanning a " + name, startMark, "expected URI in UTF-8: " + e.getMessage(), beginningMark);
    }
  }
  
  private String scanLineBreak()
  {
    char ch = this.reader.peek();
    if ((ch == '\r') || (ch == '\n') || (ch == ''))
    {
      if ((ch == '\r') && ('\n' == this.reader.peek(1))) {
        this.reader.forward(2);
      } else {
        this.reader.forward();
      }
      return "\n";
    }
    if ((ch == ' ') || (ch == ' '))
    {
      this.reader.forward();
      return String.valueOf(ch);
    }
    return "";
  }
  
  private static class Chomping
  {
    private final Boolean value;
    private final int increment;
    
    public Chomping(Boolean value, int increment)
    {
      this.value = value;
      this.increment = increment;
    }
    
    public boolean chompTailIsNotFalse()
    {
      return (this.value == null) || (this.value.booleanValue());
    }
    
    public boolean chompTailIsTrue()
    {
      return (this.value != null) && (this.value.booleanValue());
    }
    
    public int getIncrement()
    {
      return this.increment;
    }
  }
}
