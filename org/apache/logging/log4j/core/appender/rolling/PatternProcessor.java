package org.apache.logging.log4j.core.appender.rolling;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.core.pattern.ArrayPatternConverter;
import org.apache.logging.log4j.core.pattern.DatePatternConverter;
import org.apache.logging.log4j.core.pattern.FormattingInfo;
import org.apache.logging.log4j.core.pattern.PatternConverter;
import org.apache.logging.log4j.core.pattern.PatternParser;

public class PatternProcessor
{
  private static final String KEY = "FileConverter";
  private static final char YEAR_CHAR = 'y';
  private static final char MONTH_CHAR = 'M';
  private static final char[] WEEK_CHARS = { 'w', 'W' };
  private static final char[] DAY_CHARS = { 'D', 'd', 'F', 'E' };
  private static final char[] HOUR_CHARS = { 'H', 'K', 'h', 'k' };
  private static final char MINUTE_CHAR = 'm';
  private static final char SECOND_CHAR = 's';
  private static final char MILLIS_CHAR = 'S';
  private final ArrayPatternConverter[] patternConverters;
  private final FormattingInfo[] patternFields;
  private long prevFileTime = 0L;
  private long nextFileTime = 0L;
  private RolloverFrequency frequency = null;
  
  public PatternProcessor(String pattern)
  {
    PatternParser parser = createPatternParser();
    List<PatternConverter> converters = new ArrayList();
    List<FormattingInfo> fields = new ArrayList();
    parser.parse(pattern, converters, fields);
    FormattingInfo[] infoArray = new FormattingInfo[fields.size()];
    this.patternFields = ((FormattingInfo[])fields.toArray(infoArray));
    ArrayPatternConverter[] converterArray = new ArrayPatternConverter[converters.size()];
    this.patternConverters = ((ArrayPatternConverter[])converters.toArray(converterArray));
    for (ArrayPatternConverter converter : this.patternConverters) {
      if ((converter instanceof DatePatternConverter))
      {
        DatePatternConverter dateConverter = (DatePatternConverter)converter;
        this.frequency = calculateFrequency(dateConverter.getPattern());
      }
    }
  }
  
  public long getNextTime(long current, int increment, boolean modulus)
  {
    this.prevFileTime = this.nextFileTime;
    if (this.frequency == null) {
      throw new IllegalStateException("Pattern does not contain a date");
    }
    Calendar currentCal = Calendar.getInstance();
    currentCal.setTimeInMillis(current);
    Calendar cal = Calendar.getInstance();
    cal.set(currentCal.get(1), 0, 1, 0, 0, 0);
    cal.set(14, 0);
    if (this.frequency == RolloverFrequency.ANNUALLY)
    {
      increment(cal, 1, increment, modulus);
      long nextTime = cal.getTimeInMillis();
      cal.add(1, -1);
      this.nextFileTime = cal.getTimeInMillis();
      return nextTime;
    }
    if (this.frequency == RolloverFrequency.MONTHLY)
    {
      increment(cal, 2, increment, modulus);
      long nextTime = cal.getTimeInMillis();
      cal.add(2, -1);
      this.nextFileTime = cal.getTimeInMillis();
      return nextTime;
    }
    if (this.frequency == RolloverFrequency.WEEKLY)
    {
      increment(cal, 3, increment, modulus);
      long nextTime = cal.getTimeInMillis();
      cal.add(3, -1);
      this.nextFileTime = cal.getTimeInMillis();
      return nextTime;
    }
    cal.set(6, currentCal.get(6));
    if (this.frequency == RolloverFrequency.DAILY)
    {
      increment(cal, 6, increment, modulus);
      long nextTime = cal.getTimeInMillis();
      cal.add(6, -1);
      this.nextFileTime = cal.getTimeInMillis();
      return nextTime;
    }
    cal.set(10, currentCal.get(10));
    if (this.frequency == RolloverFrequency.HOURLY)
    {
      increment(cal, 10, increment, modulus);
      long nextTime = cal.getTimeInMillis();
      cal.add(10, -1);
      this.nextFileTime = cal.getTimeInMillis();
      return nextTime;
    }
    cal.set(12, currentCal.get(12));
    if (this.frequency == RolloverFrequency.EVERY_MINUTE)
    {
      increment(cal, 12, increment, modulus);
      long nextTime = cal.getTimeInMillis();
      cal.add(12, -1);
      this.nextFileTime = cal.getTimeInMillis();
      return nextTime;
    }
    cal.set(13, currentCal.get(13));
    if (this.frequency == RolloverFrequency.EVERY_SECOND)
    {
      increment(cal, 13, increment, modulus);
      long nextTime = cal.getTimeInMillis();
      cal.add(13, -1);
      this.nextFileTime = cal.getTimeInMillis();
      return nextTime;
    }
    increment(cal, 14, increment, modulus);
    long nextTime = cal.getTimeInMillis();
    cal.add(14, -1);
    this.nextFileTime = cal.getTimeInMillis();
    return nextTime;
  }
  
  private void increment(Calendar cal, int type, int increment, boolean modulate)
  {
    int interval = modulate ? increment - cal.get(type) % increment : increment;
    cal.add(type, interval);
  }
  
  public final void formatFileName(StringBuilder buf, Object obj)
  {
    long time = this.prevFileTime == 0L ? System.currentTimeMillis() : this.prevFileTime;
    formatFileName(buf, new Object[] { new Date(time), obj });
  }
  
  public final void formatFileName(StrSubstitutor subst, StringBuilder buf, Object obj)
  {
    long time = this.prevFileTime == 0L ? System.currentTimeMillis() : this.prevFileTime;
    formatFileName(buf, new Object[] { new Date(time), obj });
    LogEvent event = new Log4jLogEvent(time);
    String fileName = subst.replace(event, buf);
    buf.setLength(0);
    buf.append(fileName);
  }
  
  protected final void formatFileName(StringBuilder buf, Object... objects)
  {
    for (int i = 0; i < this.patternConverters.length; i++)
    {
      int fieldStart = buf.length();
      this.patternConverters[i].format(buf, objects);
      if (this.patternFields[i] != null) {
        this.patternFields[i].format(fieldStart, buf);
      }
    }
  }
  
  private RolloverFrequency calculateFrequency(String pattern)
  {
    if (patternContains(pattern, 'S')) {
      return RolloverFrequency.EVERY_MILLISECOND;
    }
    if (patternContains(pattern, 's')) {
      return RolloverFrequency.EVERY_SECOND;
    }
    if (patternContains(pattern, 'm')) {
      return RolloverFrequency.EVERY_MINUTE;
    }
    if (patternContains(pattern, HOUR_CHARS)) {
      return RolloverFrequency.HOURLY;
    }
    if (patternContains(pattern, DAY_CHARS)) {
      return RolloverFrequency.DAILY;
    }
    if (patternContains(pattern, WEEK_CHARS)) {
      return RolloverFrequency.WEEKLY;
    }
    if (patternContains(pattern, 'M')) {
      return RolloverFrequency.MONTHLY;
    }
    if (patternContains(pattern, 'y')) {
      return RolloverFrequency.ANNUALLY;
    }
    return null;
  }
  
  private PatternParser createPatternParser()
  {
    return new PatternParser(null, "FileConverter", null);
  }
  
  private boolean patternContains(String pattern, char... chars)
  {
    for (char character : chars) {
      if (patternContains(pattern, character)) {
        return true;
      }
    }
    return false;
  }
  
  private boolean patternContains(String pattern, char character)
  {
    return pattern.indexOf(character) >= 0;
  }
}
