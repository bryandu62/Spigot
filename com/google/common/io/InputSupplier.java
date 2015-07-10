package com.google.common.io;

import java.io.IOException;

@Deprecated
public abstract interface InputSupplier<T>
{
  public abstract T getInput()
    throws IOException;
}
