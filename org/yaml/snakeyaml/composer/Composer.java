package org.yaml.snakeyaml.composer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.yaml.snakeyaml.events.AliasEvent;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.Event.ID;
import org.yaml.snakeyaml.events.ImplicitTuple;
import org.yaml.snakeyaml.events.MappingStartEvent;
import org.yaml.snakeyaml.events.NodeEvent;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.events.SequenceStartEvent;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.parser.Parser;
import org.yaml.snakeyaml.resolver.Resolver;

public class Composer
{
  private final Parser parser;
  private final Resolver resolver;
  private final Map<String, Node> anchors;
  private final Set<Node> recursiveNodes;
  
  public Composer(Parser parser, Resolver resolver)
  {
    this.parser = parser;
    this.resolver = resolver;
    this.anchors = new HashMap();
    this.recursiveNodes = new HashSet();
  }
  
  public boolean checkNode()
  {
    if (this.parser.checkEvent(Event.ID.StreamStart)) {
      this.parser.getEvent();
    }
    return !this.parser.checkEvent(Event.ID.StreamEnd);
  }
  
  public Node getNode()
  {
    if (!this.parser.checkEvent(Event.ID.StreamEnd)) {
      return composeDocument();
    }
    return null;
  }
  
  public Node getSingleNode()
  {
    this.parser.getEvent();
    
    Node document = null;
    if (!this.parser.checkEvent(Event.ID.StreamEnd)) {
      document = composeDocument();
    }
    if (!this.parser.checkEvent(Event.ID.StreamEnd))
    {
      Event event = this.parser.getEvent();
      throw new ComposerException("expected a single document in the stream", document.getStartMark(), "but found another document", event.getStartMark());
    }
    this.parser.getEvent();
    return document;
  }
  
  private Node composeDocument()
  {
    this.parser.getEvent();
    
    Node node = composeNode(null);
    
    this.parser.getEvent();
    this.anchors.clear();
    this.recursiveNodes.clear();
    return node;
  }
  
  private Node composeNode(Node parent)
  {
    this.recursiveNodes.add(parent);
    if (this.parser.checkEvent(Event.ID.Alias))
    {
      AliasEvent event = (AliasEvent)this.parser.getEvent();
      String anchor = event.getAnchor();
      if (!this.anchors.containsKey(anchor)) {
        throw new ComposerException(null, null, "found undefined alias " + anchor, event.getStartMark());
      }
      Node result = (Node)this.anchors.get(anchor);
      if (this.recursiveNodes.remove(result)) {
        result.setTwoStepsConstruction(true);
      }
      return result;
    }
    NodeEvent event = (NodeEvent)this.parser.peekEvent();
    String anchor = null;
    anchor = event.getAnchor();
    
    Node node = null;
    if (this.parser.checkEvent(Event.ID.Scalar)) {
      node = composeScalarNode(anchor);
    } else if (this.parser.checkEvent(Event.ID.SequenceStart)) {
      node = composeSequenceNode(anchor);
    } else {
      node = composeMappingNode(anchor);
    }
    this.recursiveNodes.remove(parent);
    return node;
  }
  
  private Node composeScalarNode(String anchor)
  {
    ScalarEvent ev = (ScalarEvent)this.parser.getEvent();
    String tag = ev.getTag();
    boolean resolved = false;
    Tag nodeTag;
    if ((tag == null) || (tag.equals("!")))
    {
      Tag nodeTag = this.resolver.resolve(NodeId.scalar, ev.getValue(), ev.getImplicit().canOmitTagInPlainScalar());
      
      resolved = true;
    }
    else
    {
      nodeTag = new Tag(tag);
    }
    Node node = new ScalarNode(nodeTag, resolved, ev.getValue(), ev.getStartMark(), ev.getEndMark(), ev.getStyle());
    if (anchor != null) {
      this.anchors.put(anchor, node);
    }
    return node;
  }
  
  private Node composeSequenceNode(String anchor)
  {
    SequenceStartEvent startEvent = (SequenceStartEvent)this.parser.getEvent();
    String tag = startEvent.getTag();
    
    boolean resolved = false;
    Tag nodeTag;
    if ((tag == null) || (tag.equals("!")))
    {
      Tag nodeTag = this.resolver.resolve(NodeId.sequence, null, startEvent.getImplicit());
      resolved = true;
    }
    else
    {
      nodeTag = new Tag(tag);
    }
    ArrayList<Node> children = new ArrayList();
    SequenceNode node = new SequenceNode(nodeTag, resolved, children, startEvent.getStartMark(), null, startEvent.getFlowStyle());
    if (anchor != null) {
      this.anchors.put(anchor, node);
    }
    while (!this.parser.checkEvent(Event.ID.SequenceEnd)) {
      children.add(composeNode(node));
    }
    Event endEvent = this.parser.getEvent();
    node.setEndMark(endEvent.getEndMark());
    return node;
  }
  
  private Node composeMappingNode(String anchor)
  {
    MappingStartEvent startEvent = (MappingStartEvent)this.parser.getEvent();
    String tag = startEvent.getTag();
    
    boolean resolved = false;
    Tag nodeTag;
    if ((tag == null) || (tag.equals("!")))
    {
      Tag nodeTag = this.resolver.resolve(NodeId.mapping, null, startEvent.getImplicit());
      resolved = true;
    }
    else
    {
      nodeTag = new Tag(tag);
    }
    List<NodeTuple> children = new ArrayList();
    MappingNode node = new MappingNode(nodeTag, resolved, children, startEvent.getStartMark(), null, startEvent.getFlowStyle());
    if (anchor != null) {
      this.anchors.put(anchor, node);
    }
    while (!this.parser.checkEvent(Event.ID.MappingEnd))
    {
      Node itemKey = composeNode(node);
      if (itemKey.getTag().equals(Tag.MERGE)) {
        node.setMerged(true);
      }
      Node itemValue = composeNode(node);
      children.add(new NodeTuple(itemKey, itemValue));
    }
    Event endEvent = this.parser.getEvent();
    node.setEndMark(endEvent.getEndMark());
    return node;
  }
}
