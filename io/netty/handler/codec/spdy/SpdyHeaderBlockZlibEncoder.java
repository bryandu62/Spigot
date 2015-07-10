package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import java.util.zip.Deflater;

class SpdyHeaderBlockZlibEncoder
  extends SpdyHeaderBlockRawEncoder
{
  private final Deflater compressor;
  private boolean finished;
  
  SpdyHeaderBlockZlibEncoder(SpdyVersion spdyVersion, int compressionLevel)
  {
    super(spdyVersion);
    if ((compressionLevel < 0) || (compressionLevel > 9)) {
      throw new IllegalArgumentException("compressionLevel: " + compressionLevel + " (expected: 0-9)");
    }
    this.compressor = new Deflater(compressionLevel);
    this.compressor.setDictionary(SpdyCodecUtil.SPDY_DICT);
  }
  
  private int setInput(ByteBuf decompressed)
  {
    int len = decompressed.readableBytes();
    if (decompressed.hasArray())
    {
      this.compressor.setInput(decompressed.array(), decompressed.arrayOffset() + decompressed.readerIndex(), len);
    }
    else
    {
      byte[] in = new byte[len];
      decompressed.getBytes(decompressed.readerIndex(), in);
      this.compressor.setInput(in, 0, in.length);
    }
    return len;
  }
  
  private void encode(ByteBuf compressed)
  {
    while (compressInto(compressed)) {
      compressed.ensureWritable(compressed.capacity() << 1);
    }
  }
  
  private boolean compressInto(ByteBuf compressed)
  {
    byte[] out = compressed.array();
    int off = compressed.arrayOffset() + compressed.writerIndex();
    int toWrite = compressed.writableBytes();
    int numBytes = this.compressor.deflate(out, off, toWrite, 2);
    compressed.writerIndex(compressed.writerIndex() + numBytes);
    return numBytes == toWrite;
  }
  
  public ByteBuf encode(SpdyHeadersFrame frame)
    throws Exception
  {
    if (frame == null) {
      throw new IllegalArgumentException("frame");
    }
    if (this.finished) {
      return Unpooled.EMPTY_BUFFER;
    }
    ByteBuf decompressed = super.encode(frame);
    if (decompressed.readableBytes() == 0) {
      return Unpooled.EMPTY_BUFFER;
    }
    ByteBuf compressed = decompressed.alloc().heapBuffer(decompressed.readableBytes());
    int len = setInput(decompressed);
    encode(compressed);
    decompressed.skipBytes(len);
    
    return compressed;
  }
  
  public void end()
  {
    if (this.finished) {
      return;
    }
    this.finished = true;
    this.compressor.end();
    super.end();
  }
}
