package com.google.common.io;

import java.io.IOException;

@Deprecated
public abstract interface OutputSupplier<T>
{
  public abstract T getOutput()
    throws IOException;
}
