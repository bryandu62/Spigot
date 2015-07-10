package org.bukkit.craftbukkit.libs.jline.console;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bukkit.craftbukkit.libs.jline.internal.Log;

public class ConsoleKeys
{
  private KeyMap keys;
  private Map<String, KeyMap> keyMaps;
  private Map<String, String> variables = new HashMap();
  
  public ConsoleKeys(String appName, URL inputrcUrl)
  {
    this.keyMaps = KeyMap.keyMaps();
    loadKeys(appName, inputrcUrl);
  }
  
  protected boolean isViEditMode()
  {
    return this.keys.isViKeyMap();
  }
  
  protected boolean setKeyMap(String name)
  {
    KeyMap map = (KeyMap)this.keyMaps.get(name);
    if (map == null) {
      return false;
    }
    this.keys = map;
    return true;
  }
  
  protected Map<String, KeyMap> getKeyMaps()
  {
    return this.keyMaps;
  }
  
  protected KeyMap getKeys()
  {
    return this.keys;
  }
  
  protected void setKeys(KeyMap keys)
  {
    this.keys = keys;
  }
  
  protected boolean getViEditMode()
  {
    return this.keys.isViKeyMap();
  }
  
  protected void loadKeys(String appName, URL inputrcUrl)
  {
    this.keys = ((KeyMap)this.keyMaps.get("emacs"));
    try
    {
      InputStream input = inputrcUrl.openStream();
      try
      {
        loadKeys(input, appName);
        Log.debug(new Object[] { "Loaded user configuration: ", inputrcUrl });
      }
      finally
      {
        try
        {
          input.close();
        }
        catch (IOException e) {}
      }
      File file;
      return;
    }
    catch (IOException e)
    {
      if (inputrcUrl.getProtocol().equals("file"))
      {
        file = new File(inputrcUrl.getPath());
        if (file.exists()) {
          Log.warn(new Object[] { "Unable to read user configuration: ", inputrcUrl, e });
        }
      }
      else
      {
        Log.warn(new Object[] { "Unable to read user configuration: ", inputrcUrl, e });
      }
    }
  }
  
  private void loadKeys(InputStream input, String appName)
    throws IOException
  {
    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
    
    boolean parsing = true;
    List<Boolean> ifsStack = new ArrayList();
    String line;
    while ((line = reader.readLine()) != null) {
      try
      {
        line = line.trim();
        if ((line.length() != 0) && 
        
          (line.charAt(0) != '#'))
        {
          int i = 0;
          if (line.charAt(i) == '$')
          {
            for (i++; (i < line.length()) && ((line.charAt(i) == ' ') || (line.charAt(i) == '\t')); i++) {}
            int s = i;
            while ((i < line.length()) && (line.charAt(i) != ' ') && (line.charAt(i) != '\t')) {
              i++;
            }
            String cmd = line.substring(s, i);
            while ((i < line.length()) && ((line.charAt(i) == ' ') || (line.charAt(i) == '\t'))) {
              i++;
            }
            s = i;
            while ((i < line.length()) && (line.charAt(i) != ' ') && (line.charAt(i) != '\t')) {
              i++;
            }
            String args = line.substring(s, i);
            if ("if".equalsIgnoreCase(cmd))
            {
              ifsStack.add(Boolean.valueOf(parsing));
              if (!parsing) {
                continue;
              }
              if (!args.startsWith("term=")) {
                if (args.startsWith("mode="))
                {
                  if (args.equalsIgnoreCase("mode=vi")) {
                    parsing = isViEditMode();
                  } else if (args.equals("mode=emacs")) {
                    parsing = !isViEditMode();
                  } else {
                    parsing = false;
                  }
                }
                else {
                  parsing = args.equalsIgnoreCase(appName);
                }
              }
            }
            else if ("else".equalsIgnoreCase(cmd))
            {
              if (ifsStack.isEmpty()) {
                throw new IllegalArgumentException("$else found without matching $if");
              }
              boolean invert = true;
              for (Iterator i$ = ifsStack.iterator(); i$.hasNext();)
              {
                boolean b = ((Boolean)i$.next()).booleanValue();
                if (!b)
                {
                  invert = false;
                  break;
                }
              }
              if (invert) {
                parsing = !parsing;
              }
            }
            else if ("endif".equalsIgnoreCase(cmd))
            {
              if (ifsStack.isEmpty()) {
                throw new IllegalArgumentException("endif found without matching $if");
              }
              parsing = ((Boolean)ifsStack.remove(ifsStack.size() - 1)).booleanValue();
            }
            else if (!"include".equalsIgnoreCase(cmd)) {}
          }
          else if (parsing)
          {
            String keySeq = "";
            if (line.charAt(i++) == '"')
            {
              boolean esc = false;
              for (;; i++)
              {
                if (i >= line.length()) {
                  throw new IllegalArgumentException("Missing closing quote on line '" + line + "'");
                }
                if (esc) {
                  esc = false;
                } else if (line.charAt(i) == '\\') {
                  esc = true;
                } else {
                  if (line.charAt(i) == '"') {
                    break;
                  }
                }
              }
            }
            while ((i < line.length()) && (line.charAt(i) != ':') && (line.charAt(i) != ' ') && (line.charAt(i) != '\t')) {
              i++;
            }
            keySeq = line.substring(0, i);
            boolean equivalency = (i + 1 < line.length()) && (line.charAt(i) == ':') && (line.charAt(i + 1) == '=');
            i++;
            if (equivalency) {
              i++;
            }
            if (keySeq.equalsIgnoreCase("set"))
            {
              while ((i < line.length()) && ((line.charAt(i) == ' ') || (line.charAt(i) == '\t'))) {
                i++;
              }
              int s = i;
              while ((i < line.length()) && (line.charAt(i) != ' ') && (line.charAt(i) != '\t')) {
                i++;
              }
              String key = line.substring(s, i);
              while ((i < line.length()) && ((line.charAt(i) == ' ') || (line.charAt(i) == '\t'))) {
                i++;
              }
              s = i;
              while ((i < line.length()) && (line.charAt(i) != ' ') && (line.charAt(i) != '\t')) {
                i++;
              }
              String val = line.substring(s, i);
              setVar(key, val);
            }
            else
            {
              while ((i < line.length()) && ((line.charAt(i) == ' ') || (line.charAt(i) == '\t'))) {
                i++;
              }
              int start = i;
              if ((i < line.length()) && ((line.charAt(i) == '\'') || (line.charAt(i) == '"')))
              {
                char delim = line.charAt(i++);
                boolean esc = false;
                for (; i < line.length(); i++) {
                  if (esc) {
                    esc = false;
                  } else if (line.charAt(i) == '\\') {
                    esc = true;
                  } else {
                    if (line.charAt(i) == delim) {
                      break;
                    }
                  }
                }
              }
              while ((i < line.length()) && (line.charAt(i) != ' ') && (line.charAt(i) != '\t')) {
                i++;
              }
              String val = line.substring(Math.min(start, line.length()), Math.min(i, line.length()));
              if (keySeq.charAt(0) == '"')
              {
                keySeq = translateQuoted(keySeq);
              }
              else
              {
                String keyName = keySeq.lastIndexOf('-') > 0 ? keySeq.substring(keySeq.lastIndexOf('-') + 1) : keySeq;
                char key = getKeyFromName(keyName);
                keyName = keySeq.toLowerCase();
                keySeq = "";
                if ((keyName.contains("meta-")) || (keyName.contains("m-"))) {
                  keySeq = keySeq + "\033";
                }
                if ((keyName.contains("control-")) || (keyName.contains("c-")) || (keyName.contains("ctrl-"))) {
                  key = (char)(Character.toUpperCase(key) & 0x1F);
                }
                keySeq = keySeq + key;
              }
              if ((val.length() > 0) && ((val.charAt(0) == '\'') || (val.charAt(0) == '"')))
              {
                this.keys.bind(keySeq, translateQuoted(val));
              }
              else
              {
                String operationName = val.replace('-', '_').toUpperCase();
                try
                {
                  this.keys.bind(keySeq, Operation.valueOf(operationName));
                }
                catch (IllegalArgumentException e)
                {
                  Log.info(new Object[] { "Unable to bind key for unsupported operation: ", val });
                }
              }
            }
          }
        }
      }
      catch (IllegalArgumentException e)
      {
        Log.warn(new Object[] { "Unable to parse user configuration: ", e });
      }
    }
  }
  
  private String translateQuoted(String keySeq)
  {
    String str = keySeq.substring(1, keySeq.length() - 1);
    keySeq = "";
    for (int i = 0; i < str.length(); i++)
    {
      char c = str.charAt(i);
      if (c == '\\')
      {
        boolean ctrl = (str.regionMatches(i, "\\C-", 0, 3)) || (str.regionMatches(i, "\\M-\\C-", 0, 6));
        boolean meta = (str.regionMatches(i, "\\M-", 0, 3)) || (str.regionMatches(i, "\\C-\\M-", 0, 6));
        i += (meta ? 3 : 0) + (ctrl ? 3 : 0) + ((!meta) && (!ctrl) ? 1 : 0);
        if (i >= str.length()) {
          break;
        }
        c = str.charAt(i);
        if (meta) {
          keySeq = keySeq + "\033";
        }
        if (ctrl) {
          c = c == '?' ? '' : (char)(Character.toUpperCase(c) & 0x1F);
        }
        if ((!meta) && (!ctrl)) {
          switch (c)
          {
          case 'a': 
            c = '\007'; break;
          case 'b': 
            c = '\b'; break;
          case 'd': 
            c = ''; break;
          case 'e': 
            c = '\033'; break;
          case 'f': 
            c = '\f'; break;
          case 'n': 
            c = '\n'; break;
          case 'r': 
            c = '\r'; break;
          case 't': 
            c = '\t'; break;
          case 'v': 
            c = '\013'; break;
          case '\\': 
            c = '\\'; break;
          case '0': 
          case '1': 
          case '2': 
          case '3': 
          case '4': 
          case '5': 
          case '6': 
          case '7': 
            c = '\000';
            for (int j = 0; j < 3; i++)
            {
              if (i >= str.length()) {
                break;
              }
              int k = Character.digit(str.charAt(i), 8);
              if (k < 0) {
                break;
              }
              c = (char)(c * '\b' + k);j++;
            }
            c = (char)(c & 0xFF);
            break;
          case 'x': 
            i++;
            c = '\000';
            for (int j = 0; j < 2; i++)
            {
              if (i >= str.length()) {
                break;
              }
              int k = Character.digit(str.charAt(i), 16);
              if (k < 0) {
                break;
              }
              c = (char)(c * '\020' + k);j++;
            }
            c = (char)(c & 0xFF);
            break;
          case 'u': 
            i++;
            c = '\000';
            for (int j = 0; j < 4; i++)
            {
              if (i >= str.length()) {
                break;
              }
              int k = Character.digit(str.charAt(i), 16);
              if (k < 0) {
                break;
              }
              c = (char)(c * '\020' + k);j++;
            }
          }
        }
        keySeq = keySeq + c;
      }
      else
      {
        keySeq = keySeq + c;
      }
    }
    return keySeq;
  }
  
  private char getKeyFromName(String name)
  {
    if (("DEL".equalsIgnoreCase(name)) || ("Rubout".equalsIgnoreCase(name))) {
      return '';
    }
    if (("ESC".equalsIgnoreCase(name)) || ("Escape".equalsIgnoreCase(name))) {
      return '\033';
    }
    if (("LFD".equalsIgnoreCase(name)) || ("NewLine".equalsIgnoreCase(name))) {
      return '\n';
    }
    if (("RET".equalsIgnoreCase(name)) || ("Return".equalsIgnoreCase(name))) {
      return '\r';
    }
    if (("SPC".equalsIgnoreCase(name)) || ("Space".equalsIgnoreCase(name))) {
      return ' ';
    }
    if ("Tab".equalsIgnoreCase(name)) {
      return '\t';
    }
    return name.charAt(0);
  }
  
  private void setVar(String key, String val)
  {
    if ("keymap".equalsIgnoreCase(key))
    {
      if (this.keyMaps.containsKey(val)) {
        this.keys = ((KeyMap)this.keyMaps.get(val));
      }
    }
    else if ("editing-mode".equals(key))
    {
      if ("vi".equalsIgnoreCase(val)) {
        this.keys = ((KeyMap)this.keyMaps.get("vi-insert"));
      } else if ("emacs".equalsIgnoreCase(key)) {
        this.keys = ((KeyMap)this.keyMaps.get("emacs"));
      }
    }
    else if ("blink-matching-paren".equals(key)) {
      if ("on".equalsIgnoreCase(val)) {
        this.keys.setBlinkMatchingParen(true);
      } else if ("off".equalsIgnoreCase(val)) {
        this.keys.setBlinkMatchingParen(false);
      }
    }
    this.variables.put(key, val);
  }
  
  public String getVariable(String var)
  {
    return (String)this.variables.get(var);
  }
}
