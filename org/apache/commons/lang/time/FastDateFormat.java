package org.apache.commons.lang.time;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.text.StrBuilder;

public class FastDateFormat
  extends Format
{
  private static final long serialVersionUID = 1L;
  public static final int FULL = 0;
  public static final int LONG = 1;
  public static final int MEDIUM = 2;
  public static final int SHORT = 3;
  private static String cDefaultPattern;
  private static final Map cInstanceCache = new HashMap(7);
  private static final Map cDateInstanceCache = new HashMap(7);
  private static final Map cTimeInstanceCache = new HashMap(7);
  private static final Map cDateTimeInstanceCache = new HashMap(7);
  private static final Map cTimeZoneDisplayCache = new HashMap(7);
  private final String mPattern;
  private final TimeZone mTimeZone;
  private final boolean mTimeZoneForced;
  private final Locale mLocale;
  private final boolean mLocaleForced;
  private transient Rule[] mRules;
  private transient int mMaxLengthEstimate;
  
  public static FastDateFormat getInstance()
  {
    return getInstance(getDefaultPattern(), null, null);
  }
  
  public static FastDateFormat getInstance(String pattern)
  {
    return getInstance(pattern, null, null);
  }
  
  public static FastDateFormat getInstance(String pattern, TimeZone timeZone)
  {
    return getInstance(pattern, timeZone, null);
  }
  
  public static FastDateFormat getInstance(String pattern, Locale locale)
  {
    return getInstance(pattern, null, locale);
  }
  
  public static synchronized FastDateFormat getInstance(String pattern, TimeZone timeZone, Locale locale)
  {
    FastDateFormat emptyFormat = new FastDateFormat(pattern, timeZone, locale);
    FastDateFormat format = (FastDateFormat)cInstanceCache.get(emptyFormat);
    if (format == null)
    {
      format = emptyFormat;
      format.init();
      cInstanceCache.put(format, format);
    }
    return format;
  }
  
  public static FastDateFormat getDateInstance(int style)
  {
    return getDateInstance(style, null, null);
  }
  
  public static FastDateFormat getDateInstance(int style, Locale locale)
  {
    return getDateInstance(style, null, locale);
  }
  
  public static FastDateFormat getDateInstance(int style, TimeZone timeZone)
  {
    return getDateInstance(style, timeZone, null);
  }
  
  public static synchronized FastDateFormat getDateInstance(int style, TimeZone timeZone, Locale locale)
  {
    Object key = new Integer(style);
    if (timeZone != null) {
      key = new Pair(key, timeZone);
    }
    if (locale == null) {
      locale = Locale.getDefault();
    }
    key = new Pair(key, locale);
    
    FastDateFormat format = (FastDateFormat)cDateInstanceCache.get(key);
    if (format == null) {
      try
      {
        SimpleDateFormat formatter = (SimpleDateFormat)DateFormat.getDateInstance(style, locale);
        String pattern = formatter.toPattern();
        format = getInstance(pattern, timeZone, locale);
        cDateInstanceCache.put(key, format);
      }
      catch (ClassCastException ex)
      {
        throw new IllegalArgumentException("No date pattern for locale: " + locale);
      }
    }
    return format;
  }
  
  public static FastDateFormat getTimeInstance(int style)
  {
    return getTimeInstance(style, null, null);
  }
  
  public static FastDateFormat getTimeInstance(int style, Locale locale)
  {
    return getTimeInstance(style, null, locale);
  }
  
  public static FastDateFormat getTimeInstance(int style, TimeZone timeZone)
  {
    return getTimeInstance(style, timeZone, null);
  }
  
  public static synchronized FastDateFormat getTimeInstance(int style, TimeZone timeZone, Locale locale)
  {
    Object key = new Integer(style);
    if (timeZone != null) {
      key = new Pair(key, timeZone);
    }
    if (locale != null) {
      key = new Pair(key, locale);
    }
    FastDateFormat format = (FastDateFormat)cTimeInstanceCache.get(key);
    if (format == null)
    {
      if (locale == null) {
        locale = Locale.getDefault();
      }
      try
      {
        SimpleDateFormat formatter = (SimpleDateFormat)DateFormat.getTimeInstance(style, locale);
        String pattern = formatter.toPattern();
        format = getInstance(pattern, timeZone, locale);
        cTimeInstanceCache.put(key, format);
      }
      catch (ClassCastException ex)
      {
        throw new IllegalArgumentException("No date pattern for locale: " + locale);
      }
    }
    return format;
  }
  
  public static FastDateFormat getDateTimeInstance(int dateStyle, int timeStyle)
  {
    return getDateTimeInstance(dateStyle, timeStyle, null, null);
  }
  
  public static FastDateFormat getDateTimeInstance(int dateStyle, int timeStyle, Locale locale)
  {
    return getDateTimeInstance(dateStyle, timeStyle, null, locale);
  }
  
  public static FastDateFormat getDateTimeInstance(int dateStyle, int timeStyle, TimeZone timeZone)
  {
    return getDateTimeInstance(dateStyle, timeStyle, timeZone, null);
  }
  
  public static synchronized FastDateFormat getDateTimeInstance(int dateStyle, int timeStyle, TimeZone timeZone, Locale locale)
  {
    Object key = new Pair(new Integer(dateStyle), new Integer(timeStyle));
    if (timeZone != null) {
      key = new Pair(key, timeZone);
    }
    if (locale == null) {
      locale = Locale.getDefault();
    }
    key = new Pair(key, locale);
    
    FastDateFormat format = (FastDateFormat)cDateTimeInstanceCache.get(key);
    if (format == null) {
      try
      {
        SimpleDateFormat formatter = (SimpleDateFormat)DateFormat.getDateTimeInstance(dateStyle, timeStyle, locale);
        
        String pattern = formatter.toPattern();
        format = getInstance(pattern, timeZone, locale);
        cDateTimeInstanceCache.put(key, format);
      }
      catch (ClassCastException ex)
      {
        throw new IllegalArgumentException("No date time pattern for locale: " + locale);
      }
    }
    return format;
  }
  
  static synchronized String getTimeZoneDisplay(TimeZone tz, boolean daylight, int style, Locale locale)
  {
    Object key = new TimeZoneDisplayKey(tz, daylight, style, locale);
    String value = (String)cTimeZoneDisplayCache.get(key);
    if (value == null)
    {
      value = tz.getDisplayName(daylight, style, locale);
      cTimeZoneDisplayCache.put(key, value);
    }
    return value;
  }
  
  private static synchronized String getDefaultPattern()
  {
    if (cDefaultPattern == null) {
      cDefaultPattern = new SimpleDateFormat().toPattern();
    }
    return cDefaultPattern;
  }
  
  protected FastDateFormat(String pattern, TimeZone timeZone, Locale locale)
  {
    if (pattern == null) {
      throw new IllegalArgumentException("The pattern must not be null");
    }
    this.mPattern = pattern;
    
    this.mTimeZoneForced = (timeZone != null);
    if (timeZone == null) {
      timeZone = TimeZone.getDefault();
    }
    this.mTimeZone = timeZone;
    
    this.mLocaleForced = (locale != null);
    if (locale == null) {
      locale = Locale.getDefault();
    }
    this.mLocale = locale;
  }
  
  protected void init()
  {
    List rulesList = parsePattern();
    this.mRules = ((Rule[])rulesList.toArray(new Rule[rulesList.size()]));
    
    int len = 0;
    int i = this.mRules.length;
    for (;;)
    {
      i--;
      if (i < 0) {
        break;
      }
      len += this.mRules[i].estimateLength();
    }
    this.mMaxLengthEstimate = len;
  }
  
  protected List parsePattern()
  {
    DateFormatSymbols symbols = new DateFormatSymbols(this.mLocale);
    List rules = new ArrayList();
    
    String[] ERAs = symbols.getEras();
    String[] months = symbols.getMonths();
    String[] shortMonths = symbols.getShortMonths();
    String[] weekdays = symbols.getWeekdays();
    String[] shortWeekdays = symbols.getShortWeekdays();
    String[] AmPmStrings = symbols.getAmPmStrings();
    
    int length = this.mPattern.length();
    int[] indexRef = new int[1];
    for (int i = 0; i < length; i++)
    {
      indexRef[0] = i;
      String token = parseToken(this.mPattern, indexRef);
      i = indexRef[0];
      
      int tokenLen = token.length();
      if (tokenLen == 0) {
        break;
      }
      char c = token.charAt(0);
      Rule rule;
      Rule rule;
      Rule rule;
      Rule rule;
      Rule rule;
      Rule rule;
      switch (c)
      {
      case 'G': 
        rule = new TextField(0, ERAs);
        break;
      case 'y': 
        if (tokenLen >= 4) {
          rule = selectNumberRule(1, tokenLen);
        } else {
          rule = TwoDigitYearField.INSTANCE;
        }
        break;
      case 'M': 
        if (tokenLen >= 4)
        {
          rule = new TextField(2, months);
        }
        else
        {
          Rule rule;
          if (tokenLen == 3)
          {
            rule = new TextField(2, shortMonths);
          }
          else
          {
            Rule rule;
            if (tokenLen == 2) {
              rule = TwoDigitMonthField.INSTANCE;
            } else {
              rule = UnpaddedMonthField.INSTANCE;
            }
          }
        }
        break;
      case 'd': 
        rule = selectNumberRule(5, tokenLen);
        break;
      case 'h': 
        rule = new TwelveHourField(selectNumberRule(10, tokenLen));
        break;
      case 'H': 
        rule = selectNumberRule(11, tokenLen);
        break;
      case 'm': 
        rule = selectNumberRule(12, tokenLen);
        break;
      case 's': 
        rule = selectNumberRule(13, tokenLen);
        break;
      case 'S': 
        rule = selectNumberRule(14, tokenLen);
        break;
      case 'E': 
        rule = new TextField(7, tokenLen < 4 ? shortWeekdays : weekdays);
        break;
      case 'D': 
        rule = selectNumberRule(6, tokenLen);
        break;
      case 'F': 
        rule = selectNumberRule(8, tokenLen);
        break;
      case 'w': 
        rule = selectNumberRule(3, tokenLen);
        break;
      case 'W': 
        rule = selectNumberRule(4, tokenLen);
        break;
      case 'a': 
        rule = new TextField(9, AmPmStrings);
        break;
      case 'k': 
        rule = new TwentyFourHourField(selectNumberRule(11, tokenLen));
        break;
      case 'K': 
        rule = selectNumberRule(10, tokenLen);
        break;
      case 'z': 
        if (tokenLen >= 4) {
          rule = new TimeZoneNameRule(this.mTimeZone, this.mTimeZoneForced, this.mLocale, 1);
        } else {
          rule = new TimeZoneNameRule(this.mTimeZone, this.mTimeZoneForced, this.mLocale, 0);
        }
        break;
      case 'Z': 
        if (tokenLen == 1) {
          rule = TimeZoneNumberRule.INSTANCE_NO_COLON;
        } else {
          rule = TimeZoneNumberRule.INSTANCE_COLON;
        }
        break;
      case '\'': 
        String sub = token.substring(1);
        if (sub.length() == 1) {
          rule = new CharacterLiteral(sub.charAt(0));
        } else {
          rule = new StringLiteral(sub);
        }
        break;
      case '(': 
      case ')': 
      case '*': 
      case '+': 
      case ',': 
      case '-': 
      case '.': 
      case '/': 
      case '0': 
      case '1': 
      case '2': 
      case '3': 
      case '4': 
      case '5': 
      case '6': 
      case '7': 
      case '8': 
      case '9': 
      case ':': 
      case ';': 
      case '<': 
      case '=': 
      case '>': 
      case '?': 
      case '@': 
      case 'A': 
      case 'B': 
      case 'C': 
      case 'I': 
      case 'J': 
      case 'L': 
      case 'N': 
      case 'O': 
      case 'P': 
      case 'Q': 
      case 'R': 
      case 'T': 
      case 'U': 
      case 'V': 
      case 'X': 
      case 'Y': 
      case '[': 
      case '\\': 
      case ']': 
      case '^': 
      case '_': 
      case '`': 
      case 'b': 
      case 'c': 
      case 'e': 
      case 'f': 
      case 'g': 
      case 'i': 
      case 'j': 
      case 'l': 
      case 'n': 
      case 'o': 
      case 'p': 
      case 'q': 
      case 'r': 
      case 't': 
      case 'u': 
      case 'v': 
      case 'x': 
      default: 
        throw new IllegalArgumentException("Illegal pattern component: " + token);
      }
      rules.add(rule);
    }
    return rules;
  }
  
  protected String parseToken(String pattern, int[] indexRef)
  {
    StrBuilder buf = new StrBuilder();
    
    int i = indexRef[0];
    int length = pattern.length();
    
    char c = pattern.charAt(i);
    if (((c >= 'A') && (c <= 'Z')) || ((c >= 'a') && (c <= 'z'))) {
      buf.append(c);
    }
    while (i + 1 < length)
    {
      char peek = pattern.charAt(i + 1);
      if (peek == c)
      {
        buf.append(c);
        i++;
        
        continue;
        
        buf.append('\'');
        
        boolean inLiteral = false;
        for (; i < length; i++)
        {
          c = pattern.charAt(i);
          if (c == '\'')
          {
            if ((i + 1 < length) && (pattern.charAt(i + 1) == '\''))
            {
              i++;
              buf.append(c);
            }
            else
            {
              inLiteral = !inLiteral;
            }
          }
          else
          {
            if ((!inLiteral) && (((c >= 'A') && (c <= 'Z')) || ((c >= 'a') && (c <= 'z'))))
            {
              i--;
              break;
            }
            buf.append(c);
          }
        }
      }
    }
    indexRef[0] = i;
    return buf.toString();
  }
  
  protected NumberRule selectNumberRule(int field, int padding)
  {
    switch (padding)
    {
    case 1: 
      return new UnpaddedNumberField(field);
    case 2: 
      return new TwoDigitNumberField(field);
    }
    return new PaddedNumberField(field, padding);
  }
  
  public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
  {
    if ((obj instanceof Date)) {
      return format((Date)obj, toAppendTo);
    }
    if ((obj instanceof Calendar)) {
      return format((Calendar)obj, toAppendTo);
    }
    if ((obj instanceof Long)) {
      return format(((Long)obj).longValue(), toAppendTo);
    }
    throw new IllegalArgumentException("Unknown class: " + (obj == null ? "<null>" : obj.getClass().getName()));
  }
  
  public String format(long millis)
  {
    return format(new Date(millis));
  }
  
  public String format(Date date)
  {
    Calendar c = new GregorianCalendar(this.mTimeZone, this.mLocale);
    c.setTime(date);
    return applyRules(c, new StringBuffer(this.mMaxLengthEstimate)).toString();
  }
  
  public String format(Calendar calendar)
  {
    return format(calendar, new StringBuffer(this.mMaxLengthEstimate)).toString();
  }
  
  public StringBuffer format(long millis, StringBuffer buf)
  {
    return format(new Date(millis), buf);
  }
  
  public StringBuffer format(Date date, StringBuffer buf)
  {
    Calendar c = new GregorianCalendar(this.mTimeZone);
    c.setTime(date);
    return applyRules(c, buf);
  }
  
  public StringBuffer format(Calendar calendar, StringBuffer buf)
  {
    if (this.mTimeZoneForced)
    {
      calendar.getTime();
      calendar = (Calendar)calendar.clone();
      calendar.setTimeZone(this.mTimeZone);
    }
    return applyRules(calendar, buf);
  }
  
  protected StringBuffer applyRules(Calendar calendar, StringBuffer buf)
  {
    Rule[] rules = this.mRules;
    int len = this.mRules.length;
    for (int i = 0; i < len; i++) {
      rules[i].appendTo(buf, calendar);
    }
    return buf;
  }
  
  public Object parseObject(String source, ParsePosition pos)
  {
    pos.setIndex(0);
    pos.setErrorIndex(0);
    return null;
  }
  
  public String getPattern()
  {
    return this.mPattern;
  }
  
  public TimeZone getTimeZone()
  {
    return this.mTimeZone;
  }
  
  public boolean getTimeZoneOverridesCalendar()
  {
    return this.mTimeZoneForced;
  }
  
  public Locale getLocale()
  {
    return this.mLocale;
  }
  
  public int getMaxLengthEstimate()
  {
    return this.mMaxLengthEstimate;
  }
  
  public boolean equals(Object obj)
  {
    if (!(obj instanceof FastDateFormat)) {
      return false;
    }
    FastDateFormat other = (FastDateFormat)obj;
    if (((this.mPattern == other.mPattern) || (this.mPattern.equals(other.mPattern))) && ((this.mTimeZone == other.mTimeZone) || (this.mTimeZone.equals(other.mTimeZone))) && ((this.mLocale == other.mLocale) || (this.mLocale.equals(other.mLocale))) && (this.mTimeZoneForced == other.mTimeZoneForced) && (this.mLocaleForced == other.mLocaleForced)) {
      return true;
    }
    return false;
  }
  
  public int hashCode()
  {
    int total = 0;
    total += this.mPattern.hashCode();
    total += this.mTimeZone.hashCode();
    total += (this.mTimeZoneForced ? 1 : 0);
    total += this.mLocale.hashCode();
    total += (this.mLocaleForced ? 1 : 0);
    return total;
  }
  
  public String toString()
  {
    return "FastDateFormat[" + this.mPattern + "]";
  }
  
  private void readObject(ObjectInputStream in)
    throws IOException, ClassNotFoundException
  {
    in.defaultReadObject();
    init();
  }
  
  private static abstract interface Rule
  {
    public abstract int estimateLength();
    
    public abstract void appendTo(StringBuffer paramStringBuffer, Calendar paramCalendar);
  }
  
  private static abstract interface NumberRule
    extends FastDateFormat.Rule
  {
    public abstract void appendTo(StringBuffer paramStringBuffer, int paramInt);
  }
  
  private static class CharacterLiteral
    implements FastDateFormat.Rule
  {
    private final char mValue;
    
    CharacterLiteral(char value)
    {
      this.mValue = value;
    }
    
    public int estimateLength()
    {
      return 1;
    }
    
    public void appendTo(StringBuffer buffer, Calendar calendar)
    {
      buffer.append(this.mValue);
    }
  }
  
  private static class StringLiteral
    implements FastDateFormat.Rule
  {
    private final String mValue;
    
    StringLiteral(String value)
    {
      this.mValue = value;
    }
    
    public int estimateLength()
    {
      return this.mValue.length();
    }
    
    public void appendTo(StringBuffer buffer, Calendar calendar)
    {
      buffer.append(this.mValue);
    }
  }
  
  private static class TextField
    implements FastDateFormat.Rule
  {
    private final int mField;
    private final String[] mValues;
    
    TextField(int field, String[] values)
    {
      this.mField = field;
      this.mValues = values;
    }
    
    public int estimateLength()
    {
      int max = 0;
      int i = this.mValues.length;
      for (;;)
      {
        i--;
        if (i < 0) {
          break;
        }
        int len = this.mValues[i].length();
        if (len > max) {
          max = len;
        }
      }
      return max;
    }
    
    public void appendTo(StringBuffer buffer, Calendar calendar)
    {
      buffer.append(this.mValues[calendar.get(this.mField)]);
    }
  }
  
  private static class UnpaddedNumberField
    implements FastDateFormat.NumberRule
  {
    private final int mField;
    
    UnpaddedNumberField(int field)
    {
      this.mField = field;
    }
    
    public int estimateLength()
    {
      return 4;
    }
    
    public void appendTo(StringBuffer buffer, Calendar calendar)
    {
      appendTo(buffer, calendar.get(this.mField));
    }
    
    public final void appendTo(StringBuffer buffer, int value)
    {
      if (value < 10)
      {
        buffer.append((char)(value + 48));
      }
      else if (value < 100)
      {
        buffer.append((char)(value / 10 + 48));
        buffer.append((char)(value % 10 + 48));
      }
      else
      {
        buffer.append(Integer.toString(value));
      }
    }
  }
  
  private static class UnpaddedMonthField
    implements FastDateFormat.NumberRule
  {
    static final UnpaddedMonthField INSTANCE = new UnpaddedMonthField();
    
    public int estimateLength()
    {
      return 2;
    }
    
    public void appendTo(StringBuffer buffer, Calendar calendar)
    {
      appendTo(buffer, calendar.get(2) + 1);
    }
    
    public final void appendTo(StringBuffer buffer, int value)
    {
      if (value < 10)
      {
        buffer.append((char)(value + 48));
      }
      else
      {
        buffer.append((char)(value / 10 + 48));
        buffer.append((char)(value % 10 + 48));
      }
    }
  }
  
  private static class PaddedNumberField
    implements FastDateFormat.NumberRule
  {
    private final int mField;
    private final int mSize;
    
    PaddedNumberField(int field, int size)
    {
      if (size < 3) {
        throw new IllegalArgumentException();
      }
      this.mField = field;
      this.mSize = size;
    }
    
    public int estimateLength()
    {
      return 4;
    }
    
    public void appendTo(StringBuffer buffer, Calendar calendar)
    {
      appendTo(buffer, calendar.get(this.mField));
    }
    
    public final void appendTo(StringBuffer buffer, int value)
    {
      if (value < 100)
      {
        int i = this.mSize;
        for (;;)
        {
          i--;
          if (i < 2) {
            break;
          }
          buffer.append('0');
        }
        buffer.append((char)(value / 10 + 48));
        buffer.append((char)(value % 10 + 48));
      }
      else
      {
        int digits;
        int digits;
        if (value < 1000)
        {
          digits = 3;
        }
        else
        {
          Validate.isTrue(value > -1, "Negative values should not be possible", value);
          digits = Integer.toString(value).length();
        }
        int i = this.mSize;
        for (;;)
        {
          i--;
          if (i < digits) {
            break;
          }
          buffer.append('0');
        }
        buffer.append(Integer.toString(value));
      }
    }
  }
  
  private static class TwoDigitNumberField
    implements FastDateFormat.NumberRule
  {
    private final int mField;
    
    TwoDigitNumberField(int field)
    {
      this.mField = field;
    }
    
    public int estimateLength()
    {
      return 2;
    }
    
    public void appendTo(StringBuffer buffer, Calendar calendar)
    {
      appendTo(buffer, calendar.get(this.mField));
    }
    
    public final void appendTo(StringBuffer buffer, int value)
    {
      if (value < 100)
      {
        buffer.append((char)(value / 10 + 48));
        buffer.append((char)(value % 10 + 48));
      }
      else
      {
        buffer.append(Integer.toString(value));
      }
    }
  }
  
  private static class TwoDigitYearField
    implements FastDateFormat.NumberRule
  {
    static final TwoDigitYearField INSTANCE = new TwoDigitYearField();
    
    public int estimateLength()
    {
      return 2;
    }
    
    public void appendTo(StringBuffer buffer, Calendar calendar)
    {
      appendTo(buffer, calendar.get(1) % 100);
    }
    
    public final void appendTo(StringBuffer buffer, int value)
    {
      buffer.append((char)(value / 10 + 48));
      buffer.append((char)(value % 10 + 48));
    }
  }
  
  private static class TwoDigitMonthField
    implements FastDateFormat.NumberRule
  {
    static final TwoDigitMonthField INSTANCE = new TwoDigitMonthField();
    
    public int estimateLength()
    {
      return 2;
    }
    
    public void appendTo(StringBuffer buffer, Calendar calendar)
    {
      appendTo(buffer, calendar.get(2) + 1);
    }
    
    public final void appendTo(StringBuffer buffer, int value)
    {
      buffer.append((char)(value / 10 + 48));
      buffer.append((char)(value % 10 + 48));
    }
  }
  
  private static class TwelveHourField
    implements FastDateFormat.NumberRule
  {
    private final FastDateFormat.NumberRule mRule;
    
    TwelveHourField(FastDateFormat.NumberRule rule)
    {
      this.mRule = rule;
    }
    
    public int estimateLength()
    {
      return this.mRule.estimateLength();
    }
    
    public void appendTo(StringBuffer buffer, Calendar calendar)
    {
      int value = calendar.get(10);
      if (value == 0) {
        value = calendar.getLeastMaximum(10) + 1;
      }
      this.mRule.appendTo(buffer, value);
    }
    
    public void appendTo(StringBuffer buffer, int value)
    {
      this.mRule.appendTo(buffer, value);
    }
  }
  
  private static class TwentyFourHourField
    implements FastDateFormat.NumberRule
  {
    private final FastDateFormat.NumberRule mRule;
    
    TwentyFourHourField(FastDateFormat.NumberRule rule)
    {
      this.mRule = rule;
    }
    
    public int estimateLength()
    {
      return this.mRule.estimateLength();
    }
    
    public void appendTo(StringBuffer buffer, Calendar calendar)
    {
      int value = calendar.get(11);
      if (value == 0) {
        value = calendar.getMaximum(11) + 1;
      }
      this.mRule.appendTo(buffer, value);
    }
    
    public void appendTo(StringBuffer buffer, int value)
    {
      this.mRule.appendTo(buffer, value);
    }
  }
  
  private static class TimeZoneNameRule
    implements FastDateFormat.Rule
  {
    private final TimeZone mTimeZone;
    private final boolean mTimeZoneForced;
    private final Locale mLocale;
    private final int mStyle;
    private final String mStandard;
    private final String mDaylight;
    
    TimeZoneNameRule(TimeZone timeZone, boolean timeZoneForced, Locale locale, int style)
    {
      this.mTimeZone = timeZone;
      this.mTimeZoneForced = timeZoneForced;
      this.mLocale = locale;
      this.mStyle = style;
      if (timeZoneForced)
      {
        this.mStandard = FastDateFormat.getTimeZoneDisplay(timeZone, false, style, locale);
        this.mDaylight = FastDateFormat.getTimeZoneDisplay(timeZone, true, style, locale);
      }
      else
      {
        this.mStandard = null;
        this.mDaylight = null;
      }
    }
    
    public int estimateLength()
    {
      if (this.mTimeZoneForced) {
        return Math.max(this.mStandard.length(), this.mDaylight.length());
      }
      if (this.mStyle == 0) {
        return 4;
      }
      return 40;
    }
    
    public void appendTo(StringBuffer buffer, Calendar calendar)
    {
      if (this.mTimeZoneForced)
      {
        if ((this.mTimeZone.useDaylightTime()) && (calendar.get(16) != 0)) {
          buffer.append(this.mDaylight);
        } else {
          buffer.append(this.mStandard);
        }
      }
      else
      {
        TimeZone timeZone = calendar.getTimeZone();
        if ((timeZone.useDaylightTime()) && (calendar.get(16) != 0)) {
          buffer.append(FastDateFormat.getTimeZoneDisplay(timeZone, true, this.mStyle, this.mLocale));
        } else {
          buffer.append(FastDateFormat.getTimeZoneDisplay(timeZone, false, this.mStyle, this.mLocale));
        }
      }
    }
  }
  
  private static class TimeZoneNumberRule
    implements FastDateFormat.Rule
  {
    static final TimeZoneNumberRule INSTANCE_COLON = new TimeZoneNumberRule(true);
    static final TimeZoneNumberRule INSTANCE_NO_COLON = new TimeZoneNumberRule(false);
    final boolean mColon;
    
    TimeZoneNumberRule(boolean colon)
    {
      this.mColon = colon;
    }
    
    public int estimateLength()
    {
      return 5;
    }
    
    public void appendTo(StringBuffer buffer, Calendar calendar)
    {
      int offset = calendar.get(15) + calendar.get(16);
      if (offset < 0)
      {
        buffer.append('-');
        offset = -offset;
      }
      else
      {
        buffer.append('+');
      }
      int hours = offset / 3600000;
      buffer.append((char)(hours / 10 + 48));
      buffer.append((char)(hours % 10 + 48));
      if (this.mColon) {
        buffer.append(':');
      }
      int minutes = offset / 60000 - 60 * hours;
      buffer.append((char)(minutes / 10 + 48));
      buffer.append((char)(minutes % 10 + 48));
    }
  }
  
  private static class TimeZoneDisplayKey
  {
    private final TimeZone mTimeZone;
    private final int mStyle;
    private final Locale mLocale;
    
    TimeZoneDisplayKey(TimeZone timeZone, boolean daylight, int style, Locale locale)
    {
      this.mTimeZone = timeZone;
      if (daylight) {
        style |= 0x80000000;
      }
      this.mStyle = style;
      this.mLocale = locale;
    }
    
    public int hashCode()
    {
      return this.mStyle * 31 + this.mLocale.hashCode();
    }
    
    public boolean equals(Object obj)
    {
      if (this == obj) {
        return true;
      }
      if ((obj instanceof TimeZoneDisplayKey))
      {
        TimeZoneDisplayKey other = (TimeZoneDisplayKey)obj;
        return (this.mTimeZone.equals(other.mTimeZone)) && (this.mStyle == other.mStyle) && (this.mLocale.equals(other.mLocale));
      }
      return false;
    }
  }
  
  private static class Pair
  {
    private final Object mObj1;
    private final Object mObj2;
    
    public Pair(Object obj1, Object obj2)
    {
      this.mObj1 = obj1;
      this.mObj2 = obj2;
    }
    
    public boolean equals(Object obj)
    {
      if (this == obj) {
        return true;
      }
      if (!(obj instanceof Pair)) {
        return false;
      }
      Pair key = (Pair)obj;
      
      return (this.mObj1 == null ? key.mObj1 == null : this.mObj1.equals(key.mObj1)) && (this.mObj2 == null ? key.mObj2 == null : this.mObj2.equals(key.mObj2));
    }
    
    public int hashCode()
    {
      return (this.mObj1 == null ? 0 : this.mObj1.hashCode()) + (this.mObj2 == null ? 0 : this.mObj2.hashCode());
    }
    
    public String toString()
    {
      return "[" + this.mObj1 + ':' + this.mObj2 + ']';
    }
  }
}
