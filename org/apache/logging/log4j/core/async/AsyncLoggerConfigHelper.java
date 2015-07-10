package org.apache.logging.log4j.core.async;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.Sequence;
import com.lmax.disruptor.SequenceReportingEventHandler;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.Util;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.status.StatusLogger;

class AsyncLoggerConfigHelper
{
  private static final int MAX_DRAIN_ATTEMPTS_BEFORE_SHUTDOWN = 20;
  private static final int HALF_A_SECOND = 500;
  private static final int RINGBUFFER_MIN_SIZE = 128;
  private static final int RINGBUFFER_DEFAULT_SIZE = 262144;
  private static final Logger LOGGER = ;
  private static ThreadFactory threadFactory = new DaemonThreadFactory("AsyncLoggerConfig-");
  private static volatile Disruptor<Log4jEventWrapper> disruptor;
  private static ExecutorService executor;
  private static volatile int count = 0;
  private static final EventFactory<Log4jEventWrapper> FACTORY = new EventFactory()
  {
    public AsyncLoggerConfigHelper.Log4jEventWrapper newInstance()
    {
      return new AsyncLoggerConfigHelper.Log4jEventWrapper(null);
    }
  };
  private final EventTranslator<Log4jEventWrapper> translator = new EventTranslator()
  {
    public void translateTo(AsyncLoggerConfigHelper.Log4jEventWrapper event, long sequence)
    {
      event.event = ((LogEvent)AsyncLoggerConfigHelper.this.currentLogEvent.get());
      event.loggerConfig = AsyncLoggerConfigHelper.this.asyncLoggerConfig;
    }
  };
  private final ThreadLocal<LogEvent> currentLogEvent = new ThreadLocal();
  private final AsyncLoggerConfig asyncLoggerConfig;
  
  public AsyncLoggerConfigHelper(AsyncLoggerConfig asyncLoggerConfig)
  {
    this.asyncLoggerConfig = asyncLoggerConfig;
    claim();
  }
  
  private static synchronized void initDisruptor()
  {
    if (disruptor != null)
    {
      LOGGER.trace("AsyncLoggerConfigHelper not starting new disruptor, using existing object. Ref count is {}.", new Object[] { Integer.valueOf(count) });
      return;
    }
    LOGGER.trace("AsyncLoggerConfigHelper creating new disruptor. Ref count is {}.", new Object[] { Integer.valueOf(count) });
    int ringBufferSize = calculateRingBufferSize();
    WaitStrategy waitStrategy = createWaitStrategy();
    executor = Executors.newSingleThreadExecutor(threadFactory);
    disruptor = new Disruptor(FACTORY, ringBufferSize, executor, ProducerType.MULTI, waitStrategy);
    
    EventHandler<Log4jEventWrapper>[] handlers = { new Log4jEventWrapperHandler(null) };
    
    ExceptionHandler errorHandler = getExceptionHandler();
    disruptor.handleExceptionsWith(errorHandler);
    disruptor.handleEventsWith(handlers);
    
    LOGGER.debug("Starting AsyncLoggerConfig disruptor with ringbuffer size={}, waitStrategy={}, exceptionHandler={}...", new Object[] { Integer.valueOf(disruptor.getRingBuffer().getBufferSize()), waitStrategy.getClass().getSimpleName(), errorHandler });
    
    disruptor.start();
  }
  
  private static WaitStrategy createWaitStrategy()
  {
    String strategy = System.getProperty("AsyncLoggerConfig.WaitStrategy");
    
    LOGGER.debug("property AsyncLoggerConfig.WaitStrategy={}", new Object[] { strategy });
    if ("Sleep".equals(strategy)) {
      return new SleepingWaitStrategy();
    }
    if ("Yield".equals(strategy)) {
      return new YieldingWaitStrategy();
    }
    if ("Block".equals(strategy)) {
      return new BlockingWaitStrategy();
    }
    return new SleepingWaitStrategy();
  }
  
  private static int calculateRingBufferSize()
  {
    int ringBufferSize = 262144;
    String userPreferredRBSize = System.getProperty("AsyncLoggerConfig.RingBufferSize", String.valueOf(ringBufferSize));
    try
    {
      int size = Integer.parseInt(userPreferredRBSize);
      if (size < 128)
      {
        size = 128;
        LOGGER.warn("Invalid RingBufferSize {}, using minimum size {}.", new Object[] { userPreferredRBSize, Integer.valueOf(128) });
      }
      ringBufferSize = size;
    }
    catch (Exception ex)
    {
      LOGGER.warn("Invalid RingBufferSize {}, using default size {}.", new Object[] { userPreferredRBSize, Integer.valueOf(ringBufferSize) });
    }
    return Util.ceilingNextPowerOfTwo(ringBufferSize);
  }
  
  private static ExceptionHandler getExceptionHandler()
  {
    String cls = System.getProperty("AsyncLoggerConfig.ExceptionHandler");
    if (cls == null) {
      return null;
    }
    try
    {
      Class<? extends ExceptionHandler> klass = Class.forName(cls);
      
      return (ExceptionHandler)klass.newInstance();
    }
    catch (Exception ignored)
    {
      LOGGER.debug("AsyncLoggerConfig.ExceptionHandler not set: error creating " + cls + ": ", ignored);
    }
    return null;
  }
  
  private static class Log4jEventWrapper
  {
    private AsyncLoggerConfig loggerConfig;
    private LogEvent event;
    
    public void clear()
    {
      this.loggerConfig = null;
      this.event = null;
    }
  }
  
  private static class Log4jEventWrapperHandler
    implements SequenceReportingEventHandler<AsyncLoggerConfigHelper.Log4jEventWrapper>
  {
    private static final int NOTIFY_PROGRESS_THRESHOLD = 50;
    private Sequence sequenceCallback;
    private int counter;
    
    public void setSequenceCallback(Sequence sequenceCallback)
    {
      this.sequenceCallback = sequenceCallback;
    }
    
    public void onEvent(AsyncLoggerConfigHelper.Log4jEventWrapper event, long sequence, boolean endOfBatch)
      throws Exception
    {
      AsyncLoggerConfigHelper.Log4jEventWrapper.access$100(event).setEndOfBatch(endOfBatch);
      AsyncLoggerConfigHelper.Log4jEventWrapper.access$300(event).asyncCallAppenders(AsyncLoggerConfigHelper.Log4jEventWrapper.access$100(event));
      event.clear();
      if (++this.counter > 50)
      {
        this.sequenceCallback.set(sequence);
        this.counter = 0;
      }
    }
  }
  
  static synchronized void claim()
  {
    count += 1;
    initDisruptor();
  }
  
  static synchronized void release()
  {
    if (--count > 0)
    {
      LOGGER.trace("AsyncLoggerConfigHelper: not shutting down disruptor: ref count is {}.", new Object[] { Integer.valueOf(count) });
      return;
    }
    Disruptor<Log4jEventWrapper> temp = disruptor;
    if (temp == null)
    {
      LOGGER.trace("AsyncLoggerConfigHelper: disruptor already shut down: ref count is {}.", new Object[] { Integer.valueOf(count) });
      return;
    }
    LOGGER.trace("AsyncLoggerConfigHelper: shutting down disruptor: ref count is {}.", new Object[] { Integer.valueOf(count) });
    
    disruptor = null;
    temp.shutdown();
    
    RingBuffer<Log4jEventWrapper> ringBuffer = temp.getRingBuffer();
    for (int i = 0; i < 20; i++)
    {
      if (ringBuffer.hasAvailableCapacity(ringBuffer.getBufferSize())) {
        break;
      }
      try
      {
        Thread.sleep(500L);
      }
      catch (InterruptedException e) {}
    }
    executor.shutdown();
    executor = null;
  }
  
  public void callAppendersFromAnotherThread(LogEvent event)
  {
    this.currentLogEvent.set(event);
    disruptor.publishEvent(this.translator);
  }
}
