package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Ticker;
import java.util.concurrent.TimeUnit;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
@Beta
public abstract class RateLimiter
{
  private final SleepingTicker ticker;
  private final long offsetNanos;
  double storedPermits;
  double maxPermits;
  volatile double stableIntervalMicros;
  
  public static RateLimiter create(double permitsPerSecond)
  {
    return create(SleepingTicker.SYSTEM_TICKER, permitsPerSecond);
  }
  
  @VisibleForTesting
  static RateLimiter create(SleepingTicker ticker, double permitsPerSecond)
  {
    RateLimiter rateLimiter = new Bursty(ticker, 1.0D);
    rateLimiter.setRate(permitsPerSecond);
    return rateLimiter;
  }
  
  public static RateLimiter create(double permitsPerSecond, long warmupPeriod, TimeUnit unit)
  {
    return create(SleepingTicker.SYSTEM_TICKER, permitsPerSecond, warmupPeriod, unit);
  }
  
  @VisibleForTesting
  static RateLimiter create(SleepingTicker ticker, double permitsPerSecond, long warmupPeriod, TimeUnit unit)
  {
    RateLimiter rateLimiter = new WarmingUp(ticker, warmupPeriod, unit);
    rateLimiter.setRate(permitsPerSecond);
    return rateLimiter;
  }
  
  @VisibleForTesting
  static RateLimiter createWithCapacity(SleepingTicker ticker, double permitsPerSecond, long maxBurstBuildup, TimeUnit unit)
  {
    double maxBurstSeconds = unit.toNanos(maxBurstBuildup) / 1.0E9D;
    Bursty rateLimiter = new Bursty(ticker, maxBurstSeconds);
    rateLimiter.setRate(permitsPerSecond);
    return rateLimiter;
  }
  
  private final Object mutex = new Object();
  private long nextFreeTicketMicros = 0L;
  
  private RateLimiter(SleepingTicker ticker)
  {
    this.ticker = ticker;
    this.offsetNanos = ticker.read();
  }
  
  public final void setRate(double permitsPerSecond)
  {
    Preconditions.checkArgument((permitsPerSecond > 0.0D) && (!Double.isNaN(permitsPerSecond)), "rate must be positive");
    synchronized (this.mutex)
    {
      resync(readSafeMicros());
      double stableIntervalMicros = TimeUnit.SECONDS.toMicros(1L) / permitsPerSecond;
      this.stableIntervalMicros = stableIntervalMicros;
      doSetRate(permitsPerSecond, stableIntervalMicros);
    }
  }
  
  abstract void doSetRate(double paramDouble1, double paramDouble2);
  
  public final double getRate()
  {
    return TimeUnit.SECONDS.toMicros(1L) / this.stableIntervalMicros;
  }
  
  public double acquire()
  {
    return acquire(1);
  }
  
  public double acquire(int permits)
  {
    long microsToWait = reserve(permits);
    this.ticker.sleepMicrosUninterruptibly(microsToWait);
    return 1.0D * microsToWait / TimeUnit.SECONDS.toMicros(1L);
  }
  
  long reserve()
  {
    return reserve(1);
  }
  
  /* Error */
  long reserve(int permits)
  {
    // Byte code:
    //   0: iload_1
    //   1: invokestatic 144	com/google/common/util/concurrent/RateLimiter:checkPermits	(I)V
    //   4: aload_0
    //   5: getfield 78	com/google/common/util/concurrent/RateLimiter:mutex	Ljava/lang/Object;
    //   8: dup
    //   9: astore_2
    //   10: monitorenter
    //   11: aload_0
    //   12: iload_1
    //   13: i2d
    //   14: aload_0
    //   15: invokespecial 106	com/google/common/util/concurrent/RateLimiter:readSafeMicros	()J
    //   18: invokespecial 148	com/google/common/util/concurrent/RateLimiter:reserveNextTicket	(DJ)J
    //   21: aload_2
    //   22: monitorexit
    //   23: lreturn
    //   24: astore_3
    //   25: aload_2
    //   26: monitorexit
    //   27: aload_3
    //   28: athrow
    // Line number table:
    //   Java source line #424	-> byte code offset #0
    //   Java source line #425	-> byte code offset #4
    //   Java source line #426	-> byte code offset #11
    //   Java source line #427	-> byte code offset #24
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	29	0	this	RateLimiter
    //   0	29	1	permits	int
    //   9	17	2	Ljava/lang/Object;	Object
    //   24	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   11	23	24	finally
    //   24	27	24	finally
  }
  
  public boolean tryAcquire(long timeout, TimeUnit unit)
  {
    return tryAcquire(1, timeout, unit);
  }
  
  public boolean tryAcquire(int permits)
  {
    return tryAcquire(permits, 0L, TimeUnit.MICROSECONDS);
  }
  
  public boolean tryAcquire()
  {
    return tryAcquire(1, 0L, TimeUnit.MICROSECONDS);
  }
  
  public boolean tryAcquire(int permits, long timeout, TimeUnit unit)
  {
    long timeoutMicros = unit.toMicros(timeout);
    checkPermits(permits);
    long microsToWait;
    synchronized (this.mutex)
    {
      long nowMicros = readSafeMicros();
      if (this.nextFreeTicketMicros > nowMicros + timeoutMicros) {
        return false;
      }
      microsToWait = reserveNextTicket(permits, nowMicros);
    }
    this.ticker.sleepMicrosUninterruptibly(microsToWait);
    return true;
  }
  
  private static void checkPermits(int permits)
  {
    Preconditions.checkArgument(permits > 0, "Requested permits must be positive");
  }
  
  private long reserveNextTicket(double requiredPermits, long nowMicros)
  {
    resync(nowMicros);
    long microsToNextFreeTicket = Math.max(0L, this.nextFreeTicketMicros - nowMicros);
    double storedPermitsToSpend = Math.min(requiredPermits, this.storedPermits);
    double freshPermits = requiredPermits - storedPermitsToSpend;
    
    long waitMicros = storedPermitsToWaitTime(this.storedPermits, storedPermitsToSpend) + (freshPermits * this.stableIntervalMicros);
    
    this.nextFreeTicketMicros += waitMicros;
    this.storedPermits -= storedPermitsToSpend;
    return microsToNextFreeTicket;
  }
  
  abstract long storedPermitsToWaitTime(double paramDouble1, double paramDouble2);
  
  private void resync(long nowMicros)
  {
    if (nowMicros > this.nextFreeTicketMicros)
    {
      this.storedPermits = Math.min(this.maxPermits, this.storedPermits + (nowMicros - this.nextFreeTicketMicros) / this.stableIntervalMicros);
      
      this.nextFreeTicketMicros = nowMicros;
    }
  }
  
  private long readSafeMicros()
  {
    return TimeUnit.NANOSECONDS.toMicros(this.ticker.read() - this.offsetNanos);
  }
  
  public String toString()
  {
    return String.format("RateLimiter[stableRate=%3.1fqps]", new Object[] { Double.valueOf(1000000.0D / this.stableIntervalMicros) });
  }
  
  private static class WarmingUp
    extends RateLimiter
  {
    final long warmupPeriodMicros;
    private double slope;
    private double halfPermits;
    
    WarmingUp(RateLimiter.SleepingTicker ticker, long warmupPeriod, TimeUnit timeUnit)
    {
      super(null);
      this.warmupPeriodMicros = timeUnit.toMicros(warmupPeriod);
    }
    
    void doSetRate(double permitsPerSecond, double stableIntervalMicros)
    {
      double oldMaxPermits = this.maxPermits;
      this.maxPermits = (this.warmupPeriodMicros / stableIntervalMicros);
      this.halfPermits = (this.maxPermits / 2.0D);
      
      double coldIntervalMicros = stableIntervalMicros * 3.0D;
      this.slope = ((coldIntervalMicros - stableIntervalMicros) / this.halfPermits);
      if (oldMaxPermits == Double.POSITIVE_INFINITY) {
        this.storedPermits = 0.0D;
      } else {
        this.storedPermits = (oldMaxPermits == 0.0D ? this.maxPermits : this.storedPermits * this.maxPermits / oldMaxPermits);
      }
    }
    
    long storedPermitsToWaitTime(double storedPermits, double permitsToTake)
    {
      double availablePermitsAboveHalf = storedPermits - this.halfPermits;
      long micros = 0L;
      if (availablePermitsAboveHalf > 0.0D)
      {
        double permitsAboveHalfToTake = Math.min(availablePermitsAboveHalf, permitsToTake);
        micros = (permitsAboveHalfToTake * (permitsToTime(availablePermitsAboveHalf) + permitsToTime(availablePermitsAboveHalf - permitsAboveHalfToTake)) / 2.0D);
        
        permitsToTake -= permitsAboveHalfToTake;
      }
      micros = (micros + this.stableIntervalMicros * permitsToTake);
      return micros;
    }
    
    private double permitsToTime(double permits)
    {
      return this.stableIntervalMicros + permits * this.slope;
    }
  }
  
  private static class Bursty
    extends RateLimiter
  {
    final double maxBurstSeconds;
    
    Bursty(RateLimiter.SleepingTicker ticker, double maxBurstSeconds)
    {
      super(null);
      this.maxBurstSeconds = maxBurstSeconds;
    }
    
    void doSetRate(double permitsPerSecond, double stableIntervalMicros)
    {
      double oldMaxPermits = this.maxPermits;
      this.maxPermits = (this.maxBurstSeconds * permitsPerSecond);
      this.storedPermits = (oldMaxPermits == 0.0D ? 0.0D : this.storedPermits * this.maxPermits / oldMaxPermits);
    }
    
    long storedPermitsToWaitTime(double storedPermits, double permitsToTake)
    {
      return 0L;
    }
  }
  
  @VisibleForTesting
  static abstract class SleepingTicker
    extends Ticker
  {
    static final SleepingTicker SYSTEM_TICKER = new SleepingTicker()
    {
      public long read()
      {
        return systemTicker().read();
      }
      
      public void sleepMicrosUninterruptibly(long micros)
      {
        if (micros > 0L) {
          Uninterruptibles.sleepUninterruptibly(micros, TimeUnit.MICROSECONDS);
        }
      }
    };
    
    abstract void sleepMicrosUninterruptibly(long paramLong);
  }
}
