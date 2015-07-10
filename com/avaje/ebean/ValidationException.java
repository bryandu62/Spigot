package com.avaje.ebean;

import java.util.Arrays;
import javax.persistence.PersistenceException;

public class ValidationException
  extends PersistenceException
{
  private static final long serialVersionUID = -6185355529565362494L;
  final InvalidValue invalid;
  
  public ValidationException(InvalidValue invalid)
  {
    super("validation failed for: " + invalid.getBeanType());
    this.invalid = invalid;
  }
  
  public InvalidValue getInvalid()
  {
    return this.invalid;
  }
  
  public InvalidValue[] getErrors()
  {
    return this.invalid.getErrors();
  }
  
  public String toString()
  {
    return super.toString() + ": " + Arrays.toString(getErrors());
  }
}
