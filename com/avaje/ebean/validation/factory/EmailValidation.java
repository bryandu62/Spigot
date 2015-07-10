package com.avaje.ebean.validation.factory;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidation
  implements Serializable
{
  private static final long serialVersionUID = 2664585768077565394L;
  private static final String wsp = "[ \\t]";
  private static final String fwsp = "[ \\t]*";
  private static final String dquote = "\\\"";
  private static final String noWsCtl = "\\x01-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F";
  private static final String asciiText = "[\\x01-\\x09\\x0B\\x0C\\x0E-\\x7F]";
  private static final String quotedPair = "(\\\\[\\x01-\\x09\\x0B\\x0C\\x0E-\\x7F])";
  private static final String atext = "[a-zA-Z0-9\\!\\#\\$\\%\\&\\'\\*\\+\\-\\/\\=\\?\\^\\_\\`\\{\\|\\}\\~]";
  private static final String atom = "[ \\t]*[a-zA-Z0-9\\!\\#\\$\\%\\&\\'\\*\\+\\-\\/\\=\\?\\^\\_\\`\\{\\|\\}\\~]+[ \\t]*";
  private static final String dotAtomText = "[a-zA-Z0-9\\!\\#\\$\\%\\&\\'\\*\\+\\-\\/\\=\\?\\^\\_\\`\\{\\|\\}\\~]+(\\.[a-zA-Z0-9\\!\\#\\$\\%\\&\\'\\*\\+\\-\\/\\=\\?\\^\\_\\`\\{\\|\\}\\~]+)*";
  private static final String dotAtom = "[ \\t]*([a-zA-Z0-9\\!\\#\\$\\%\\&\\'\\*\\+\\-\\/\\=\\?\\^\\_\\`\\{\\|\\}\\~]+(\\.[a-zA-Z0-9\\!\\#\\$\\%\\&\\'\\*\\+\\-\\/\\=\\?\\^\\_\\`\\{\\|\\}\\~]+)*)[ \\t]*";
  private static final String qtext = "[\\x01-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F\\x21\\x23-\\x5B\\x5D-\\x7E]";
  private static final String qcontent = "([\\x01-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F\\x21\\x23-\\x5B\\x5D-\\x7E]|(\\\\[\\x01-\\x09\\x0B\\x0C\\x0E-\\x7F]))";
  private static final String quotedString = "\\\"([ \\t]*([\\x01-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F\\x21\\x23-\\x5B\\x5D-\\x7E]|(\\\\[\\x01-\\x09\\x0B\\x0C\\x0E-\\x7F])))*[ \\t]*\\\"";
  private static final String word = "(([ \\t]*[a-zA-Z0-9\\!\\#\\$\\%\\&\\'\\*\\+\\-\\/\\=\\?\\^\\_\\`\\{\\|\\}\\~]+[ \\t]*)|(\\\"([ \\t]*([\\x01-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F\\x21\\x23-\\x5B\\x5D-\\x7E]|(\\\\[\\x01-\\x09\\x0B\\x0C\\x0E-\\x7F])))*[ \\t]*\\\"))";
  private static final String phrase = "(([ \\t]*[a-zA-Z0-9\\!\\#\\$\\%\\&\\'\\*\\+\\-\\/\\=\\?\\^\\_\\`\\{\\|\\}\\~]+[ \\t]*)|(\\\"([ \\t]*([\\x01-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F\\x21\\x23-\\x5B\\x5D-\\x7E]|(\\\\[\\x01-\\x09\\x0B\\x0C\\x0E-\\x7F])))*[ \\t]*\\\"))+";
  private static final String letter = "[a-zA-Z]";
  private static final String letDig = "[a-zA-Z0-9]";
  private static final String letDigHyp = "[a-zA-Z0-9-]";
  private static final String rfcLabel = "[a-zA-Z0-9]([a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?";
  private static final String rfc1035DomainName = "[a-zA-Z0-9]([a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(\\.[a-zA-Z0-9]([a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*\\.[a-zA-Z]{2,6}";
  private static final String dtext = "[\\x01-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F\\x21-\\x5A\\x5E-\\x7E]";
  private static final String dcontent = "[\\x01-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F\\x21-\\x5A\\x5E-\\x7E]|(\\\\[\\x01-\\x09\\x0B\\x0C\\x0E-\\x7F])";
  private static final String domainLiteral = "\\[([ \\t]*[\\x01-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F\\x21-\\x5A\\x5E-\\x7E]|(\\\\[\\x01-\\x09\\x0B\\x0C\\x0E-\\x7F])+)*[ \\t]*\\]";
  private static final String rfc2822Domain = "([ \\t]*([a-zA-Z0-9\\!\\#\\$\\%\\&\\'\\*\\+\\-\\/\\=\\?\\^\\_\\`\\{\\|\\}\\~]+(\\.[a-zA-Z0-9\\!\\#\\$\\%\\&\\'\\*\\+\\-\\/\\=\\?\\^\\_\\`\\{\\|\\}\\~]+)*)[ \\t]*|\\[([ \\t]*[\\x01-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F\\x21-\\x5A\\x5E-\\x7E]|(\\\\[\\x01-\\x09\\x0B\\x0C\\x0E-\\x7F])+)*[ \\t]*\\])";
  private static final String localPart = "(([ \\t]*([a-zA-Z0-9\\!\\#\\$\\%\\&\\'\\*\\+\\-\\/\\=\\?\\^\\_\\`\\{\\|\\}\\~]+(\\.[a-zA-Z0-9\\!\\#\\$\\%\\&\\'\\*\\+\\-\\/\\=\\?\\^\\_\\`\\{\\|\\}\\~]+)*)[ \\t]*)|(\\\"([ \\t]*([\\x01-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F\\x21\\x23-\\x5B\\x5D-\\x7E]|(\\\\[\\x01-\\x09\\x0B\\x0C\\x0E-\\x7F])))*[ \\t]*\\\"))";
  private static final EmailValidation DEFAULT_VALIDATOR = create(false, false);
  private final Pattern localPattern;
  
  public static EmailValidation create(boolean allowDomainLiterals, boolean allowQuotedIdentifiers)
  {
    String domain = allowDomainLiterals ? "([ \\t]*([a-zA-Z0-9\\!\\#\\$\\%\\&\\'\\*\\+\\-\\/\\=\\?\\^\\_\\`\\{\\|\\}\\~]+(\\.[a-zA-Z0-9\\!\\#\\$\\%\\&\\'\\*\\+\\-\\/\\=\\?\\^\\_\\`\\{\\|\\}\\~]+)*)[ \\t]*|\\[([ \\t]*[\\x01-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F\\x21-\\x5A\\x5E-\\x7E]|(\\\\[\\x01-\\x09\\x0B\\x0C\\x0E-\\x7F])+)*[ \\t]*\\])" : "[a-zA-Z0-9]([a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(\\.[a-zA-Z0-9]([a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*\\.[a-zA-Z]{2,6}";
    String addrSpec = "(([ \\t]*([a-zA-Z0-9\\!\\#\\$\\%\\&\\'\\*\\+\\-\\/\\=\\?\\^\\_\\`\\{\\|\\}\\~]+(\\.[a-zA-Z0-9\\!\\#\\$\\%\\&\\'\\*\\+\\-\\/\\=\\?\\^\\_\\`\\{\\|\\}\\~]+)*)[ \\t]*)|(\\\"([ \\t]*([\\x01-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F\\x21\\x23-\\x5B\\x5D-\\x7E]|(\\\\[\\x01-\\x09\\x0B\\x0C\\x0E-\\x7F])))*[ \\t]*\\\"))@" + domain;
    String angleAddr = "<" + addrSpec + ">";
    String nameAddr = "((([ \\t]*[a-zA-Z0-9\\!\\#\\$\\%\\&\\'\\*\\+\\-\\/\\=\\?\\^\\_\\`\\{\\|\\}\\~]+[ \\t]*)|(\\\"([ \\t]*([\\x01-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F\\x21\\x23-\\x5B\\x5D-\\x7E]|(\\\\[\\x01-\\x09\\x0B\\x0C\\x0E-\\x7F])))*[ \\t]*\\\"))+)?[ \\t]*" + angleAddr;
    String mailbox = nameAddr + "|" + addrSpec;
    String patternString = allowQuotedIdentifiers ? mailbox : addrSpec;
    
    return new EmailValidation(patternString);
  }
  
  public EmailValidation(String pattern)
  {
    this.localPattern = Pattern.compile(pattern);
  }
  
  public boolean isValid(String email)
  {
    return (email != null) && (this.localPattern.matcher(email).matches());
  }
  
  public static boolean isValidEmail(String email)
  {
    return DEFAULT_VALIDATOR.isValid(email);
  }
  
  public static void main(String[] args)
  {
    System.out.println("... test with default settings");
    test(null, "\"John Smith\" <john.smith@u.washington.edu>");
    test(null, "<john.smith@u.washington.edu>");
    test(null, "john.smith@u.washington.edu");
    
    EmailValidation allowValidator = create(true, true);
    System.out.println("... test with allow literals and domains");
    test(allowValidator, "\"John Smith\" <john.smith@u.washington.edu>");
    test(allowValidator, "<john.smith@u.washington.edu>");
    test(allowValidator, "john.smith@u.washington.edu");
  }
  
  private static void test(EmailValidation validator, String email)
  {
    if (validator == null) {
      validator = DEFAULT_VALIDATOR;
    }
    if (validator.isValid(email)) {
      System.out.println(email + " is valid");
    } else {
      System.out.println(email + " is Invalid!");
    }
  }
}
