package org.apache.logging.log4j.spi;

import java.util.Collection;
import org.apache.logging.log4j.ThreadContext.ContextStack;

public abstract interface ThreadContextStack
  extends ThreadContext.ContextStack, Collection<String>
{}
