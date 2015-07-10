package org.apache.logging.log4j.core.layout;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LoggingException;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.TLSSyslogFrame;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.helpers.Charsets;
import org.apache.logging.log4j.core.helpers.Integers;
import org.apache.logging.log4j.core.helpers.NetUtils;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.core.net.Facility;
import org.apache.logging.log4j.core.net.Priority;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.PatternConverter;
import org.apache.logging.log4j.core.pattern.PatternFormatter;
import org.apache.logging.log4j.core.pattern.PatternParser;
import org.apache.logging.log4j.core.pattern.ThrowablePatternConverter;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.StructuredDataId;
import org.apache.logging.log4j.message.StructuredDataMessage;

@Plugin(name="RFC5424Layout", category="Core", elementType="layout", printObject=true)
public class RFC5424Layout
  extends AbstractStringLayout
{
  private static final String LF = "\n";
  public static final int DEFAULT_ENTERPRISE_NUMBER = 18060;
  public static final String DEFAULT_ID = "Audit";
  public static final Pattern NEWLINE_PATTERN = Pattern.compile("\\r?\\n");
  public static final Pattern PARAM_VALUE_ESCAPE_PATTERN = Pattern.compile("[\\\"\\]\\\\]");
  protected static final String DEFAULT_MDCID = "mdc";
  private static final int TWO_DIGITS = 10;
  private static final int THREE_DIGITS = 100;
  private static final int MILLIS_PER_MINUTE = 60000;
  private static final int MINUTES_PER_HOUR = 60;
  private static final String COMPONENT_KEY = "RFC5424-Converter";
  private final Facility facility;
  private final String defaultId;
  private final int enterpriseNumber;
  private final boolean includeMDC;
  private final String mdcId;
  private final StructuredDataId mdcSDID;
  private final String localHostName;
  private final String appName;
  private final String messageId;
  private final String configName;
  private final String mdcPrefix;
  private final String eventPrefix;
  private final List<String> mdcExcludes;
  private final List<String> mdcIncludes;
  private final List<String> mdcRequired;
  private final ListChecker checker;
  private final ListChecker noopChecker = new NoopChecker(null);
  private final boolean includeNewLine;
  private final String escapeNewLine;
  private final boolean useTLSMessageFormat;
  private long lastTimestamp = -1L;
  private String timestamppStr;
  private final List<PatternFormatter> exceptionFormatters;
  private final Map<String, FieldFormatter> fieldFormatters;
  
  private RFC5424Layout(Configuration config, Facility facility, String id, int ein, boolean includeMDC, boolean includeNL, String escapeNL, String mdcId, String mdcPrefix, String eventPrefix, String appName, String messageId, String excludes, String includes, String required, Charset charset, String exceptionPattern, boolean useTLSMessageFormat, LoggerFields[] loggerFields)
  {
    super(charset);
    PatternParser exceptionParser = createPatternParser(config, ThrowablePatternConverter.class);
    this.exceptionFormatters = (exceptionPattern == null ? null : exceptionParser.parse(exceptionPattern, false));
    this.facility = facility;
    this.defaultId = (id == null ? "Audit" : id);
    this.enterpriseNumber = ein;
    this.includeMDC = includeMDC;
    this.includeNewLine = includeNL;
    this.escapeNewLine = (escapeNL == null ? null : Matcher.quoteReplacement(escapeNL));
    this.mdcId = mdcId;
    this.mdcSDID = new StructuredDataId(mdcId, this.enterpriseNumber, null, null);
    this.mdcPrefix = mdcPrefix;
    this.eventPrefix = eventPrefix;
    this.appName = appName;
    this.messageId = messageId;
    this.useTLSMessageFormat = useTLSMessageFormat;
    this.localHostName = NetUtils.getLocalHostname();
    ListChecker c = null;
    if (excludes != null)
    {
      String[] array = excludes.split(",");
      if (array.length > 0)
      {
        c = new ExcludeChecker(null);
        this.mdcExcludes = new ArrayList(array.length);
        for (String str : array) {
          this.mdcExcludes.add(str.trim());
        }
      }
      else
      {
        this.mdcExcludes = null;
      }
    }
    else
    {
      this.mdcExcludes = null;
    }
    if (includes != null)
    {
      String[] array = includes.split(",");
      if (array.length > 0)
      {
        c = new IncludeChecker(null);
        this.mdcIncludes = new ArrayList(array.length);
        for (String str : array) {
          this.mdcIncludes.add(str.trim());
        }
      }
      else
      {
        this.mdcIncludes = null;
      }
    }
    else
    {
      this.mdcIncludes = null;
    }
    if (required != null)
    {
      String[] array = required.split(",");
      if (array.length > 0)
      {
        this.mdcRequired = new ArrayList(array.length);
        for (String str : array) {
          this.mdcRequired.add(str.trim());
        }
      }
      else
      {
        this.mdcRequired = null;
      }
    }
    else
    {
      this.mdcRequired = null;
    }
    this.checker = (c != null ? c : this.noopChecker);
    String name = config == null ? null : config.getName();
    this.configName = ((name != null) && (name.length() > 0) ? name : null);
    this.fieldFormatters = createFieldFormatters(loggerFields, config);
  }
  
  private Map<String, FieldFormatter> createFieldFormatters(LoggerFields[] loggerFields, Configuration config)
  {
    Map<String, FieldFormatter> sdIdMap = new HashMap();
    if (loggerFields != null) {
      for (LoggerFields lField : loggerFields)
      {
        StructuredDataId key = lField.getSdId() == null ? this.mdcSDID : lField.getSdId();
        Map<String, List<PatternFormatter>> sdParams = new HashMap();
        Map<String, String> fields = lField.getMap();
        if (!fields.isEmpty())
        {
          PatternParser fieldParser = createPatternParser(config, null);
          for (Map.Entry<String, String> entry : fields.entrySet())
          {
            List<PatternFormatter> formatters = fieldParser.parse((String)entry.getValue(), false);
            sdParams.put(entry.getKey(), formatters);
          }
          FieldFormatter fieldFormatter = new FieldFormatter(sdParams, lField.getDiscardIfAllFieldsAreEmpty());
          
          sdIdMap.put(key.toString(), fieldFormatter);
        }
      }
    }
    return sdIdMap.size() > 0 ? sdIdMap : null;
  }
  
  private static PatternParser createPatternParser(Configuration config, Class<? extends PatternConverter> filterClass)
  {
    if (config == null) {
      return new PatternParser(config, "Converter", LogEventPatternConverter.class, filterClass);
    }
    PatternParser parser = (PatternParser)config.getComponent("RFC5424-Converter");
    if (parser == null)
    {
      parser = new PatternParser(config, "Converter", ThrowablePatternConverter.class);
      config.addComponent("RFC5424-Converter", parser);
      parser = (PatternParser)config.getComponent("RFC5424-Converter");
    }
    return parser;
  }
  
  public Map<String, String> getContentFormat()
  {
    Map<String, String> result = new HashMap();
    result.put("structured", "true");
    result.put("formatType", "RFC5424");
    return result;
  }
  
  public String toSerializable(LogEvent event)
  {
    StringBuilder buf = new StringBuilder();
    appendPriority(buf, event.getLevel());
    appendTimestamp(buf, event.getMillis());
    appendSpace(buf);
    appendHostName(buf);
    appendSpace(buf);
    appendAppName(buf);
    appendSpace(buf);
    appendProcessId(buf);
    appendSpace(buf);
    appendMessageId(buf, event.getMessage());
    appendSpace(buf);
    appendStructuredElements(buf, event);
    appendMessage(buf, event);
    if (this.useTLSMessageFormat) {
      return new TLSSyslogFrame(buf.toString()).toString();
    }
    return buf.toString();
  }
  
  private void appendPriority(StringBuilder buffer, Level logLevel)
  {
    buffer.append("<");
    buffer.append(Priority.getPriority(this.facility, logLevel));
    buffer.append(">1 ");
  }
  
  private void appendTimestamp(StringBuilder buffer, long milliseconds)
  {
    buffer.append(computeTimeStampString(milliseconds));
  }
  
  private void appendSpace(StringBuilder buffer)
  {
    buffer.append(" ");
  }
  
  private void appendHostName(StringBuilder buffer)
  {
    buffer.append(this.localHostName);
  }
  
  private void appendAppName(StringBuilder buffer)
  {
    if (this.appName != null) {
      buffer.append(this.appName);
    } else if (this.configName != null) {
      buffer.append(this.configName);
    } else {
      buffer.append("-");
    }
  }
  
  private void appendProcessId(StringBuilder buffer)
  {
    buffer.append(getProcId());
  }
  
  private void appendMessageId(StringBuilder buffer, Message message)
  {
    boolean isStructured = message instanceof StructuredDataMessage;
    String type = isStructured ? ((StructuredDataMessage)message).getType() : null;
    if (type != null) {
      buffer.append(type);
    } else if (this.messageId != null) {
      buffer.append(this.messageId);
    } else {
      buffer.append("-");
    }
  }
  
  private void appendMessage(StringBuilder buffer, LogEvent event)
  {
    Message message = event.getMessage();
    String text = message.getFormat();
    if ((text != null) && (text.length() > 0)) {
      buffer.append(" ").append(escapeNewlines(text, this.escapeNewLine));
    }
    if ((this.exceptionFormatters != null) && (event.getThrown() != null))
    {
      StringBuilder exception = new StringBuilder("\n");
      for (PatternFormatter formatter : this.exceptionFormatters) {
        formatter.format(event, exception);
      }
      buffer.append(escapeNewlines(exception.toString(), this.escapeNewLine));
    }
    if (this.includeNewLine) {
      buffer.append("\n");
    }
  }
  
  private void appendStructuredElements(StringBuilder buffer, LogEvent event)
  {
    Message message = event.getMessage();
    boolean isStructured = message instanceof StructuredDataMessage;
    if ((!isStructured) && (this.fieldFormatters != null) && (this.fieldFormatters.size() == 0) && (!this.includeMDC))
    {
      buffer.append("-");
      return;
    }
    Map<String, StructuredDataElement> sdElements = new HashMap();
    Map<String, String> contextMap = event.getContextMap();
    if (this.mdcRequired != null) {
      checkRequired(contextMap);
    }
    if (this.fieldFormatters != null) {
      for (Map.Entry<String, FieldFormatter> sdElement : this.fieldFormatters.entrySet())
      {
        String sdId = (String)sdElement.getKey();
        StructuredDataElement elem = ((FieldFormatter)sdElement.getValue()).format(event);
        sdElements.put(sdId, elem);
      }
    }
    if ((this.includeMDC) && (contextMap.size() > 0)) {
      if (sdElements.containsKey(this.mdcSDID.toString()))
      {
        StructuredDataElement union = (StructuredDataElement)sdElements.get(this.mdcSDID.toString());
        union.union(contextMap);
        sdElements.put(this.mdcSDID.toString(), union);
      }
      else
      {
        StructuredDataElement formattedContextMap = new StructuredDataElement(contextMap, false);
        sdElements.put(this.mdcSDID.toString(), formattedContextMap);
      }
    }
    if (isStructured)
    {
      StructuredDataMessage data = (StructuredDataMessage)message;
      Map<String, String> map = data.getData();
      StructuredDataId id = data.getId();
      if (sdElements.containsKey(id.toString()))
      {
        StructuredDataElement union = (StructuredDataElement)sdElements.get(id.toString());
        union.union(map);
        sdElements.put(id.toString(), union);
      }
      else
      {
        StructuredDataElement formattedData = new StructuredDataElement(map, false);
        sdElements.put(id.toString(), formattedData);
      }
    }
    if (sdElements.size() == 0)
    {
      buffer.append("-");
      return;
    }
    for (Map.Entry<String, StructuredDataElement> entry : sdElements.entrySet()) {
      formatStructuredElement((String)entry.getKey(), this.mdcPrefix, (StructuredDataElement)entry.getValue(), buffer, this.checker);
    }
  }
  
  private String escapeNewlines(String text, String escapeNewLine)
  {
    if (null == escapeNewLine) {
      return text;
    }
    return NEWLINE_PATTERN.matcher(text).replaceAll(escapeNewLine);
  }
  
  protected String getProcId()
  {
    return "-";
  }
  
  protected List<String> getMdcExcludes()
  {
    return this.mdcExcludes;
  }
  
  protected List<String> getMdcIncludes()
  {
    return this.mdcIncludes;
  }
  
  private String computeTimeStampString(long now)
  {
    long last;
    synchronized (this)
    {
      last = this.lastTimestamp;
      if (now == this.lastTimestamp) {
        return this.timestamppStr;
      }
    }
    StringBuilder buffer = new StringBuilder();
    Object cal = new GregorianCalendar();
    ((Calendar)cal).setTimeInMillis(now);
    buffer.append(Integer.toString(((Calendar)cal).get(1)));
    buffer.append("-");
    pad(((Calendar)cal).get(2) + 1, 10, buffer);
    buffer.append("-");
    pad(((Calendar)cal).get(5), 10, buffer);
    buffer.append("T");
    pad(((Calendar)cal).get(11), 10, buffer);
    buffer.append(":");
    pad(((Calendar)cal).get(12), 10, buffer);
    buffer.append(":");
    pad(((Calendar)cal).get(13), 10, buffer);
    
    int millis = ((Calendar)cal).get(14);
    if (millis != 0)
    {
      buffer.append('.');
      pad(millis, 100, buffer);
    }
    int tzmin = (((Calendar)cal).get(15) + ((Calendar)cal).get(16)) / 60000;
    if (tzmin == 0)
    {
      buffer.append("Z");
    }
    else
    {
      if (tzmin < 0)
      {
        tzmin = -tzmin;
        buffer.append("-");
      }
      else
      {
        buffer.append("+");
      }
      int tzhour = tzmin / 60;
      tzmin -= tzhour * 60;
      pad(tzhour, 10, buffer);
      buffer.append(":");
      pad(tzmin, 10, buffer);
    }
    synchronized (this)
    {
      if (last == this.lastTimestamp)
      {
        this.lastTimestamp = now;
        this.timestamppStr = buffer.toString();
      }
    }
    return buffer.toString();
  }
  
  private void pad(int val, int max, StringBuilder buf)
  {
    while (max > 1)
    {
      if (val < max) {
        buf.append("0");
      }
      max /= 10;
    }
    buf.append(Integer.toString(val));
  }
  
  private void formatStructuredElement(String id, String prefix, StructuredDataElement data, StringBuilder sb, ListChecker checker)
  {
    if (((id == null) && (this.defaultId == null)) || (data.discard())) {
      return;
    }
    sb.append("[");
    sb.append(id);
    if (!this.mdcSDID.toString().equals(id)) {
      appendMap(prefix, data.getFields(), sb, this.noopChecker);
    } else {
      appendMap(prefix, data.getFields(), sb, checker);
    }
    sb.append("]");
  }
  
  private String getId(StructuredDataId id)
  {
    StringBuilder sb = new StringBuilder();
    if ((id == null) || (id.getName() == null)) {
      sb.append(this.defaultId);
    } else {
      sb.append(id.getName());
    }
    int ein = id != null ? id.getEnterpriseNumber() : this.enterpriseNumber;
    if (ein < 0) {
      ein = this.enterpriseNumber;
    }
    if (ein >= 0) {
      sb.append("@").append(ein);
    }
    return sb.toString();
  }
  
  private void checkRequired(Map<String, String> map)
  {
    for (String key : this.mdcRequired)
    {
      String value = (String)map.get(key);
      if (value == null) {
        throw new LoggingException("Required key " + key + " is missing from the " + this.mdcId);
      }
    }
  }
  
  private void appendMap(String prefix, Map<String, String> map, StringBuilder sb, ListChecker checker)
  {
    SortedMap<String, String> sorted = new TreeMap(map);
    for (Map.Entry<String, String> entry : sorted.entrySet()) {
      if ((checker.check((String)entry.getKey())) && (entry.getValue() != null))
      {
        sb.append(" ");
        if (prefix != null) {
          sb.append(prefix);
        }
        sb.append(escapeNewlines(escapeSDParams((String)entry.getKey()), this.escapeNewLine)).append("=\"").append(escapeNewlines(escapeSDParams((String)entry.getValue()), this.escapeNewLine)).append("\"");
      }
    }
  }
  
  private String escapeSDParams(String value)
  {
    return PARAM_VALUE_ESCAPE_PATTERN.matcher(value).replaceAll("\\\\$0");
  }
  
  private static abstract interface ListChecker
  {
    public abstract boolean check(String paramString);
  }
  
  private class IncludeChecker
    implements RFC5424Layout.ListChecker
  {
    private IncludeChecker() {}
    
    public boolean check(String key)
    {
      return RFC5424Layout.this.mdcIncludes.contains(key);
    }
  }
  
  private class ExcludeChecker
    implements RFC5424Layout.ListChecker
  {
    private ExcludeChecker() {}
    
    public boolean check(String key)
    {
      return !RFC5424Layout.this.mdcExcludes.contains(key);
    }
  }
  
  private class NoopChecker
    implements RFC5424Layout.ListChecker
  {
    private NoopChecker() {}
    
    public boolean check(String key)
    {
      return true;
    }
  }
  
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append("facility=").append(this.facility.name());
    sb.append(" appName=").append(this.appName);
    sb.append(" defaultId=").append(this.defaultId);
    sb.append(" enterpriseNumber=").append(this.enterpriseNumber);
    sb.append(" newLine=").append(this.includeNewLine);
    sb.append(" includeMDC=").append(this.includeMDC);
    sb.append(" messageId=").append(this.messageId);
    return sb.toString();
  }
  
  @PluginFactory
  public static RFC5424Layout createLayout(@PluginAttribute("facility") String facility, @PluginAttribute("id") String id, @PluginAttribute("enterpriseNumber") String ein, @PluginAttribute("includeMDC") String includeMDC, @PluginAttribute("mdcId") String mdcId, @PluginAttribute("mdcPrefix") String mdcPrefix, @PluginAttribute("eventPrefix") String eventPrefix, @PluginAttribute("newLine") String includeNL, @PluginAttribute("newLineEscape") String escapeNL, @PluginAttribute("appName") String appName, @PluginAttribute("messageId") String msgId, @PluginAttribute("mdcExcludes") String excludes, @PluginAttribute("mdcIncludes") String includes, @PluginAttribute("mdcRequired") String required, @PluginAttribute("exceptionPattern") String exceptionPattern, @PluginAttribute("useTLSMessageFormat") String useTLSMessageFormat, @PluginElement("LoggerFields") LoggerFields[] loggerFields, @PluginConfiguration Configuration config)
  {
    Charset charset = Charsets.UTF_8;
    if ((includes != null) && (excludes != null))
    {
      LOGGER.error("mdcIncludes and mdcExcludes are mutually exclusive. Includes wil be ignored");
      includes = null;
    }
    Facility f = Facility.toFacility(facility, Facility.LOCAL0);
    int enterpriseNumber = Integers.parseInt(ein, 18060);
    boolean isMdc = Booleans.parseBoolean(includeMDC, true);
    boolean includeNewLine = Boolean.parseBoolean(includeNL);
    boolean useTlsMessageFormat = Booleans.parseBoolean(useTLSMessageFormat, false);
    if (mdcId == null) {
      mdcId = "mdc";
    }
    return new RFC5424Layout(config, f, id, enterpriseNumber, isMdc, includeNewLine, escapeNL, mdcId, mdcPrefix, eventPrefix, appName, msgId, excludes, includes, required, charset, exceptionPattern, useTlsMessageFormat, loggerFields);
  }
  
  private class FieldFormatter
  {
    private final Map<String, List<PatternFormatter>> delegateMap;
    private final boolean discardIfEmpty;
    
    public FieldFormatter(boolean fieldMap)
    {
      this.discardIfEmpty = discardIfEmpty;
      this.delegateMap = fieldMap;
    }
    
    public RFC5424Layout.StructuredDataElement format(LogEvent event)
    {
      Map<String, String> map = new HashMap();
      for (Map.Entry<String, List<PatternFormatter>> entry : this.delegateMap.entrySet())
      {
        StringBuilder buffer = new StringBuilder();
        for (PatternFormatter formatter : (List)entry.getValue()) {
          formatter.format(event, buffer);
        }
        map.put(entry.getKey(), buffer.toString());
      }
      return new RFC5424Layout.StructuredDataElement(RFC5424Layout.this, map, this.discardIfEmpty);
    }
  }
  
  private class StructuredDataElement
  {
    private final Map<String, String> fields;
    private final boolean discardIfEmpty;
    
    public StructuredDataElement(boolean fields)
    {
      this.discardIfEmpty = discardIfEmpty;
      this.fields = fields;
    }
    
    boolean discard()
    {
      if (!this.discardIfEmpty) {
        return false;
      }
      boolean foundNotEmptyValue = false;
      for (Map.Entry<String, String> entry : this.fields.entrySet()) {
        if (Strings.isNotEmpty((CharSequence)entry.getValue()))
        {
          foundNotEmptyValue = true;
          break;
        }
      }
      return !foundNotEmptyValue;
    }
    
    void union(Map<String, String> fields)
    {
      this.fields.putAll(fields);
    }
    
    Map<String, String> getFields()
    {
      return this.fields;
    }
  }
}
