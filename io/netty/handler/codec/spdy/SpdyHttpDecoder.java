package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class SpdyHttpDecoder
  extends MessageToMessageDecoder<SpdyFrame>
{
  private final boolean validateHeaders;
  private final int spdyVersion;
  private final int maxContentLength;
  private final Map<Integer, FullHttpMessage> messageMap;
  
  public SpdyHttpDecoder(SpdyVersion version, int maxContentLength)
  {
    this(version, maxContentLength, new HashMap(), true);
  }
  
  public SpdyHttpDecoder(SpdyVersion version, int maxContentLength, boolean validateHeaders)
  {
    this(version, maxContentLength, new HashMap(), validateHeaders);
  }
  
  protected SpdyHttpDecoder(SpdyVersion version, int maxContentLength, Map<Integer, FullHttpMessage> messageMap)
  {
    this(version, maxContentLength, messageMap, true);
  }
  
  protected SpdyHttpDecoder(SpdyVersion version, int maxContentLength, Map<Integer, FullHttpMessage> messageMap, boolean validateHeaders)
  {
    if (version == null) {
      throw new NullPointerException("version");
    }
    if (maxContentLength <= 0) {
      throw new IllegalArgumentException("maxContentLength must be a positive integer: " + maxContentLength);
    }
    this.spdyVersion = version.getVersion();
    this.maxContentLength = maxContentLength;
    this.messageMap = messageMap;
    this.validateHeaders = validateHeaders;
  }
  
  protected FullHttpMessage putMessage(int streamId, FullHttpMessage message)
  {
    return (FullHttpMessage)this.messageMap.put(Integer.valueOf(streamId), message);
  }
  
  protected FullHttpMessage getMessage(int streamId)
  {
    return (FullHttpMessage)this.messageMap.get(Integer.valueOf(streamId));
  }
  
  protected FullHttpMessage removeMessage(int streamId)
  {
    return (FullHttpMessage)this.messageMap.remove(Integer.valueOf(streamId));
  }
  
  protected void decode(ChannelHandlerContext ctx, SpdyFrame msg, List<Object> out)
    throws Exception
  {
    if ((msg instanceof SpdySynStreamFrame))
    {
      SpdySynStreamFrame spdySynStreamFrame = (SpdySynStreamFrame)msg;
      int streamId = spdySynStreamFrame.streamId();
      if (SpdyCodecUtil.isServerId(streamId))
      {
        int associatedToStreamId = spdySynStreamFrame.associatedStreamId();
        if (associatedToStreamId == 0)
        {
          SpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.INVALID_STREAM);
          
          ctx.writeAndFlush(spdyRstStreamFrame);
          return;
        }
        String URL = SpdyHeaders.getUrl(this.spdyVersion, spdySynStreamFrame);
        if (URL == null)
        {
          SpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.PROTOCOL_ERROR);
          
          ctx.writeAndFlush(spdyRstStreamFrame);
          return;
        }
        if (spdySynStreamFrame.isTruncated())
        {
          SpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.INTERNAL_ERROR);
          
          ctx.writeAndFlush(spdyRstStreamFrame);
          return;
        }
        try
        {
          FullHttpResponse httpResponseWithEntity = createHttpResponse(ctx, this.spdyVersion, spdySynStreamFrame, this.validateHeaders);
          
          SpdyHttpHeaders.setStreamId(httpResponseWithEntity, streamId);
          SpdyHttpHeaders.setAssociatedToStreamId(httpResponseWithEntity, associatedToStreamId);
          SpdyHttpHeaders.setPriority(httpResponseWithEntity, spdySynStreamFrame.priority());
          SpdyHttpHeaders.setUrl(httpResponseWithEntity, URL);
          if (spdySynStreamFrame.isLast())
          {
            HttpHeaders.setContentLength(httpResponseWithEntity, 0L);
            out.add(httpResponseWithEntity);
          }
          else
          {
            putMessage(streamId, httpResponseWithEntity);
          }
        }
        catch (Exception e)
        {
          SpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.PROTOCOL_ERROR);
          
          ctx.writeAndFlush(spdyRstStreamFrame);
        }
      }
      else
      {
        if (spdySynStreamFrame.isTruncated())
        {
          SpdySynReplyFrame spdySynReplyFrame = new DefaultSpdySynReplyFrame(streamId);
          spdySynReplyFrame.setLast(true);
          SpdyHeaders.setStatus(this.spdyVersion, spdySynReplyFrame, HttpResponseStatus.REQUEST_HEADER_FIELDS_TOO_LARGE);
          
          SpdyHeaders.setVersion(this.spdyVersion, spdySynReplyFrame, HttpVersion.HTTP_1_0);
          ctx.writeAndFlush(spdySynReplyFrame);
          return;
        }
        try
        {
          FullHttpRequest httpRequestWithEntity = createHttpRequest(this.spdyVersion, spdySynStreamFrame);
          
          SpdyHttpHeaders.setStreamId(httpRequestWithEntity, streamId);
          if (spdySynStreamFrame.isLast()) {
            out.add(httpRequestWithEntity);
          } else {
            putMessage(streamId, httpRequestWithEntity);
          }
        }
        catch (Exception e)
        {
          SpdySynReplyFrame spdySynReplyFrame = new DefaultSpdySynReplyFrame(streamId);
          spdySynReplyFrame.setLast(true);
          SpdyHeaders.setStatus(this.spdyVersion, spdySynReplyFrame, HttpResponseStatus.BAD_REQUEST);
          SpdyHeaders.setVersion(this.spdyVersion, spdySynReplyFrame, HttpVersion.HTTP_1_0);
          ctx.writeAndFlush(spdySynReplyFrame);
        }
      }
    }
    else if ((msg instanceof SpdySynReplyFrame))
    {
      SpdySynReplyFrame spdySynReplyFrame = (SpdySynReplyFrame)msg;
      int streamId = spdySynReplyFrame.streamId();
      if (spdySynReplyFrame.isTruncated())
      {
        SpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.INTERNAL_ERROR);
        
        ctx.writeAndFlush(spdyRstStreamFrame);
        return;
      }
      try
      {
        FullHttpResponse httpResponseWithEntity = createHttpResponse(ctx, this.spdyVersion, spdySynReplyFrame, this.validateHeaders);
        
        SpdyHttpHeaders.setStreamId(httpResponseWithEntity, streamId);
        if (spdySynReplyFrame.isLast())
        {
          HttpHeaders.setContentLength(httpResponseWithEntity, 0L);
          out.add(httpResponseWithEntity);
        }
        else
        {
          putMessage(streamId, httpResponseWithEntity);
        }
      }
      catch (Exception e)
      {
        SpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, SpdyStreamStatus.PROTOCOL_ERROR);
        
        ctx.writeAndFlush(spdyRstStreamFrame);
      }
    }
    else if ((msg instanceof SpdyHeadersFrame))
    {
      SpdyHeadersFrame spdyHeadersFrame = (SpdyHeadersFrame)msg;
      int streamId = spdyHeadersFrame.streamId();
      FullHttpMessage fullHttpMessage = getMessage(streamId);
      if (fullHttpMessage == null) {
        return;
      }
      if (!spdyHeadersFrame.isTruncated()) {
        for (Map.Entry<String, String> e : spdyHeadersFrame.headers()) {
          fullHttpMessage.headers().add((String)e.getKey(), e.getValue());
        }
      }
      if (spdyHeadersFrame.isLast())
      {
        HttpHeaders.setContentLength(fullHttpMessage, fullHttpMessage.content().readableBytes());
        removeMessage(streamId);
        out.add(fullHttpMessage);
      }
    }
    else if ((msg instanceof SpdyDataFrame))
    {
      SpdyDataFrame spdyDataFrame = (SpdyDataFrame)msg;
      int streamId = spdyDataFrame.streamId();
      FullHttpMessage fullHttpMessage = getMessage(streamId);
      if (fullHttpMessage == null) {
        return;
      }
      ByteBuf content = fullHttpMessage.content();
      if (content.readableBytes() > this.maxContentLength - spdyDataFrame.content().readableBytes())
      {
        removeMessage(streamId);
        throw new TooLongFrameException("HTTP content length exceeded " + this.maxContentLength + " bytes.");
      }
      ByteBuf spdyDataFrameData = spdyDataFrame.content();
      int spdyDataFrameDataLen = spdyDataFrameData.readableBytes();
      content.writeBytes(spdyDataFrameData, spdyDataFrameData.readerIndex(), spdyDataFrameDataLen);
      if (spdyDataFrame.isLast())
      {
        HttpHeaders.setContentLength(fullHttpMessage, content.readableBytes());
        removeMessage(streamId);
        out.add(fullHttpMessage);
      }
    }
    else if ((msg instanceof SpdyRstStreamFrame))
    {
      SpdyRstStreamFrame spdyRstStreamFrame = (SpdyRstStreamFrame)msg;
      int streamId = spdyRstStreamFrame.streamId();
      removeMessage(streamId);
    }
  }
  
  private static FullHttpRequest createHttpRequest(int spdyVersion, SpdyHeadersFrame requestFrame)
    throws Exception
  {
    SpdyHeaders headers = requestFrame.headers();
    HttpMethod method = SpdyHeaders.getMethod(spdyVersion, requestFrame);
    String url = SpdyHeaders.getUrl(spdyVersion, requestFrame);
    HttpVersion httpVersion = SpdyHeaders.getVersion(spdyVersion, requestFrame);
    SpdyHeaders.removeMethod(spdyVersion, requestFrame);
    SpdyHeaders.removeUrl(spdyVersion, requestFrame);
    SpdyHeaders.removeVersion(spdyVersion, requestFrame);
    
    FullHttpRequest req = new DefaultFullHttpRequest(httpVersion, method, url);
    
    SpdyHeaders.removeScheme(spdyVersion, requestFrame);
    
    String host = headers.get(":host");
    headers.remove(":host");
    req.headers().set("Host", host);
    for (Map.Entry<String, String> e : requestFrame.headers()) {
      req.headers().add((String)e.getKey(), e.getValue());
    }
    HttpHeaders.setKeepAlive(req, true);
    
    req.headers().remove("Transfer-Encoding");
    
    return req;
  }
  
  private static FullHttpResponse createHttpResponse(ChannelHandlerContext ctx, int spdyVersion, SpdyHeadersFrame responseFrame, boolean validateHeaders)
    throws Exception
  {
    HttpResponseStatus status = SpdyHeaders.getStatus(spdyVersion, responseFrame);
    HttpVersion version = SpdyHeaders.getVersion(spdyVersion, responseFrame);
    SpdyHeaders.removeStatus(spdyVersion, responseFrame);
    SpdyHeaders.removeVersion(spdyVersion, responseFrame);
    
    FullHttpResponse res = new DefaultFullHttpResponse(version, status, ctx.alloc().buffer(), validateHeaders);
    for (Map.Entry<String, String> e : responseFrame.headers()) {
      res.headers().add((String)e.getKey(), e.getValue());
    }
    HttpHeaders.setKeepAlive(res, true);
    
    res.headers().remove("Transfer-Encoding");
    res.headers().remove("Trailer");
    
    return res;
  }
}
