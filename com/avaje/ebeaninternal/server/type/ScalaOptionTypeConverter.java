package com.avaje.ebeaninternal.server.type;

import com.avaje.ebean.config.ScalarTypeConverter;
import scala.None.;
import scala.Option;
import scala.Some;

public class ScalaOptionTypeConverter<S>
  implements ScalarTypeConverter<Option<S>, S>
{
  public Option<S> getNullValue()
  {
    return None..MODULE$;
  }
  
  public S unwrapValue(Option<S> beanType)
  {
    if (beanType.isEmpty()) {
      return null;
    }
    return (S)beanType.get();
  }
  
  public Option<S> wrapValue(S scalarType)
  {
    if (scalarType == null) {
      return None..MODULE$;
    }
    if ((scalarType instanceof Some)) {
      return (Option)scalarType;
    }
    return new Some(scalarType);
  }
}
