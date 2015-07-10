package org.yaml.snakeyaml.emitter;

public final class ScalarAnalysis
{
  public String scalar;
  public boolean empty;
  public boolean multiline;
  public boolean allowFlowPlain;
  public boolean allowBlockPlain;
  public boolean allowSingleQuoted;
  public boolean allowBlock;
  
  public ScalarAnalysis(String scalar, boolean empty, boolean multiline, boolean allowFlowPlain, boolean allowBlockPlain, boolean allowSingleQuoted, boolean allowBlock)
  {
    this.scalar = scalar;
    this.empty = empty;
    this.multiline = multiline;
    this.allowFlowPlain = allowFlowPlain;
    this.allowBlockPlain = allowBlockPlain;
    this.allowSingleQuoted = allowSingleQuoted;
    this.allowBlock = allowBlock;
  }
}
