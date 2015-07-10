package org.apache.logging.log4j.core.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Properties;
import javax.activation.DataSource;
import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LoggingException;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractManager;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.helpers.CyclicBuffer;
import org.apache.logging.log4j.core.helpers.NameUtil;
import org.apache.logging.log4j.core.helpers.NetUtils;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.util.PropertiesUtil;

public class SMTPManager
  extends AbstractManager
{
  private static final SMTPManagerFactory FACTORY = new SMTPManagerFactory(null);
  private final Session session;
  private final CyclicBuffer<LogEvent> buffer;
  private volatile MimeMessage message;
  private final FactoryData data;
  
  protected SMTPManager(String name, Session session, MimeMessage message, FactoryData data)
  {
    super(name);
    this.session = session;
    this.message = message;
    this.data = data;
    this.buffer = new CyclicBuffer(LogEvent.class, data.numElements);
  }
  
  public void add(LogEvent event)
  {
    this.buffer.add(event);
  }
  
  public static SMTPManager getSMTPManager(String to, String cc, String bcc, String from, String replyTo, String subject, String protocol, String host, int port, String username, String password, boolean isDebug, String filterName, int numElements)
  {
    if (Strings.isEmpty(protocol)) {
      protocol = "smtp";
    }
    StringBuilder sb = new StringBuilder();
    if (to != null) {
      sb.append(to);
    }
    sb.append(":");
    if (cc != null) {
      sb.append(cc);
    }
    sb.append(":");
    if (bcc != null) {
      sb.append(bcc);
    }
    sb.append(":");
    if (from != null) {
      sb.append(from);
    }
    sb.append(":");
    if (replyTo != null) {
      sb.append(replyTo);
    }
    sb.append(":");
    if (subject != null) {
      sb.append(subject);
    }
    sb.append(":");
    sb.append(protocol).append(":").append(host).append(":").append("port").append(":");
    if (username != null) {
      sb.append(username);
    }
    sb.append(":");
    if (password != null) {
      sb.append(password);
    }
    sb.append(isDebug ? ":debug:" : "::");
    sb.append(filterName);
    
    String name = "SMTP:" + NameUtil.md5(sb.toString());
    
    return (SMTPManager)getManager(name, FACTORY, new FactoryData(to, cc, bcc, from, replyTo, subject, protocol, host, port, username, password, isDebug, numElements));
  }
  
  public void sendEvents(Layout<?> layout, LogEvent appendEvent)
  {
    if (this.message == null) {
      connect();
    }
    try
    {
      LogEvent[] priorEvents = (LogEvent[])this.buffer.removeAll();
      
      byte[] rawBytes = formatContentToBytes(priorEvents, appendEvent, layout);
      
      String contentType = layout.getContentType();
      String encoding = getEncoding(rawBytes, contentType);
      byte[] encodedBytes = encodeContentToBytes(rawBytes, encoding);
      
      InternetHeaders headers = getHeaders(contentType, encoding);
      MimeMultipart mp = getMimeMultipart(encodedBytes, headers);
      
      sendMultipartMessage(this.message, mp);
    }
    catch (MessagingException e)
    {
      LOGGER.error("Error occurred while sending e-mail notification.", e);
      throw new LoggingException("Error occurred while sending email", e);
    }
    catch (IOException e)
    {
      LOGGER.error("Error occurred while sending e-mail notification.", e);
      throw new LoggingException("Error occurred while sending email", e);
    }
    catch (RuntimeException e)
    {
      LOGGER.error("Error occurred while sending e-mail notification.", e);
      throw new LoggingException("Error occurred while sending email", e);
    }
  }
  
  protected byte[] formatContentToBytes(LogEvent[] priorEvents, LogEvent appendEvent, Layout<?> layout)
    throws IOException
  {
    ByteArrayOutputStream raw = new ByteArrayOutputStream();
    writeContent(priorEvents, appendEvent, layout, raw);
    return raw.toByteArray();
  }
  
  private void writeContent(LogEvent[] priorEvents, LogEvent appendEvent, Layout<?> layout, ByteArrayOutputStream out)
    throws IOException
  {
    writeHeader(layout, out);
    writeBuffer(priorEvents, appendEvent, layout, out);
    writeFooter(layout, out);
  }
  
  protected void writeHeader(Layout<?> layout, OutputStream out)
    throws IOException
  {
    byte[] header = layout.getHeader();
    if (header != null) {
      out.write(header);
    }
  }
  
  protected void writeBuffer(LogEvent[] priorEvents, LogEvent appendEvent, Layout<?> layout, OutputStream out)
    throws IOException
  {
    for (LogEvent priorEvent : priorEvents)
    {
      byte[] bytes = layout.toByteArray(priorEvent);
      out.write(bytes);
    }
    byte[] bytes = layout.toByteArray(appendEvent);
    out.write(bytes);
  }
  
  protected void writeFooter(Layout<?> layout, OutputStream out)
    throws IOException
  {
    byte[] footer = layout.getFooter();
    if (footer != null) {
      out.write(footer);
    }
  }
  
  protected String getEncoding(byte[] rawBytes, String contentType)
  {
    DataSource dataSource = new ByteArrayDataSource(rawBytes, contentType);
    return MimeUtility.getEncoding(dataSource);
  }
  
  protected byte[] encodeContentToBytes(byte[] rawBytes, String encoding)
    throws MessagingException, IOException
  {
    ByteArrayOutputStream encoded = new ByteArrayOutputStream();
    encodeContent(rawBytes, encoding, encoded);
    return encoded.toByteArray();
  }
  
  protected void encodeContent(byte[] bytes, String encoding, ByteArrayOutputStream out)
    throws MessagingException, IOException
  {
    OutputStream encoder = MimeUtility.encode(out, encoding);
    encoder.write(bytes);
    encoder.close();
  }
  
  protected InternetHeaders getHeaders(String contentType, String encoding)
  {
    InternetHeaders headers = new InternetHeaders();
    headers.setHeader("Content-Type", contentType + "; charset=UTF-8");
    headers.setHeader("Content-Transfer-Encoding", encoding);
    return headers;
  }
  
  protected MimeMultipart getMimeMultipart(byte[] encodedBytes, InternetHeaders headers)
    throws MessagingException
  {
    MimeMultipart mp = new MimeMultipart();
    MimeBodyPart part = new MimeBodyPart(headers, encodedBytes);
    mp.addBodyPart(part);
    return mp;
  }
  
  protected void sendMultipartMessage(MimeMessage message, MimeMultipart mp)
    throws MessagingException
  {
    synchronized (message)
    {
      message.setContent(mp);
      message.setSentDate(new Date());
      Transport.send(message);
    }
  }
  
  private static class FactoryData
  {
    private final String to;
    private final String cc;
    private final String bcc;
    private final String from;
    private final String replyto;
    private final String subject;
    private final String protocol;
    private final String host;
    private final int port;
    private final String username;
    private final String password;
    private final boolean isDebug;
    private final int numElements;
    
    public FactoryData(String to, String cc, String bcc, String from, String replyTo, String subject, String protocol, String host, int port, String username, String password, boolean isDebug, int numElements)
    {
      this.to = to;
      this.cc = cc;
      this.bcc = bcc;
      this.from = from;
      this.replyto = replyTo;
      this.subject = subject;
      this.protocol = protocol;
      this.host = host;
      this.port = port;
      this.username = username;
      this.password = password;
      this.isDebug = isDebug;
      this.numElements = numElements;
    }
  }
  
  private synchronized void connect()
  {
    if (this.message != null) {
      return;
    }
    try
    {
      this.message = new MimeMessageBuilder(this.session).setFrom(this.data.from).setReplyTo(this.data.replyto).setRecipients(Message.RecipientType.TO, this.data.to).setRecipients(Message.RecipientType.CC, this.data.cc).setRecipients(Message.RecipientType.BCC, this.data.bcc).setSubject(this.data.subject).getMimeMessage();
    }
    catch (MessagingException e)
    {
      LOGGER.error("Could not set SMTPAppender message options.", e);
      this.message = null;
    }
  }
  
  private static class SMTPManagerFactory
    implements ManagerFactory<SMTPManager, SMTPManager.FactoryData>
  {
    public SMTPManager createManager(String name, SMTPManager.FactoryData data)
    {
      String prefix = "mail." + SMTPManager.FactoryData.access$800(data);
      
      Properties properties = PropertiesUtil.getSystemProperties();
      properties.put("mail.transport.protocol", SMTPManager.FactoryData.access$800(data));
      if (properties.getProperty("mail.host") == null) {
        properties.put("mail.host", NetUtils.getLocalHostname());
      }
      if (null != SMTPManager.FactoryData.access$900(data)) {
        properties.put(prefix + ".host", SMTPManager.FactoryData.access$900(data));
      }
      if (SMTPManager.FactoryData.access$1000(data) > 0) {
        properties.put(prefix + ".port", String.valueOf(SMTPManager.FactoryData.access$1000(data)));
      }
      Authenticator authenticator = buildAuthenticator(SMTPManager.FactoryData.access$1100(data), SMTPManager.FactoryData.access$1200(data));
      if (null != authenticator) {
        properties.put(prefix + ".auth", "true");
      }
      Session session = Session.getInstance(properties, authenticator);
      session.setProtocolForAddress("rfc822", SMTPManager.FactoryData.access$800(data));
      session.setDebug(SMTPManager.FactoryData.access$1300(data));
      MimeMessage message;
      try
      {
        message = new MimeMessageBuilder(session).setFrom(SMTPManager.FactoryData.access$700(data)).setReplyTo(SMTPManager.FactoryData.access$600(data)).setRecipients(Message.RecipientType.TO, SMTPManager.FactoryData.access$500(data)).setRecipients(Message.RecipientType.CC, SMTPManager.FactoryData.access$400(data)).setRecipients(Message.RecipientType.BCC, SMTPManager.FactoryData.access$300(data)).setSubject(SMTPManager.FactoryData.access$200(data)).getMimeMessage();
      }
      catch (MessagingException e)
      {
        SMTPManager.LOGGER.error("Could not set SMTPAppender message options.", e);
        message = null;
      }
      return new SMTPManager(name, session, message, data);
    }
    
    private Authenticator buildAuthenticator(final String username, final String password)
    {
      if ((null != password) && (null != username)) {
        new Authenticator()
        {
          private final PasswordAuthentication passwordAuthentication = new PasswordAuthentication(username, password);
          
          protected PasswordAuthentication getPasswordAuthentication()
          {
            return this.passwordAuthentication;
          }
        };
      }
      return null;
    }
  }
}
