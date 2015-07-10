package org.apache.logging.log4j.core.appender.db.jpa.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import javax.persistence.PersistenceException;
import org.apache.logging.log4j.ThreadContext.ContextStack;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.spi.DefaultThreadContextStack;

@Converter(autoApply=false)
public class ContextStackJsonAttributeConverter
  implements AttributeConverter<ThreadContext.ContextStack, String>
{
  public String convertToDatabaseColumn(ThreadContext.ContextStack contextStack)
  {
    if (contextStack == null) {
      return null;
    }
    try
    {
      return ContextMapJsonAttributeConverter.OBJECT_MAPPER.writeValueAsString(contextStack.asList());
    }
    catch (IOException e)
    {
      throw new PersistenceException("Failed to convert stack list to JSON string.", e);
    }
  }
  
  public ThreadContext.ContextStack convertToEntityAttribute(String s)
  {
    if (Strings.isEmpty(s)) {
      return null;
    }
    List<String> list;
    try
    {
      list = (List)ContextMapJsonAttributeConverter.OBJECT_MAPPER.readValue(s, new TypeReference() {});
    }
    catch (IOException e)
    {
      throw new PersistenceException("Failed to convert JSON string to list for stack.", e);
    }
    DefaultThreadContextStack result = new DefaultThreadContextStack(true);
    result.addAll(list);
    return result;
  }
}
