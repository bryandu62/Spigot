package org.apache.commons.lang.text;

import java.text.Format;
import java.util.Locale;

public abstract interface FormatFactory
{
  public abstract Format getFormat(String paramString1, String paramString2, Locale paramLocale);
}
