package com.avaje.ebean.enhance.agent;

public class MethodMeta
{
  final int access;
  final String name;
  final String desc;
  final AnnotationInfo annotationInfo;
  
  public MethodMeta(AnnotationInfo classAnnotationInfo, int access, String name, String desc)
  {
    this.annotationInfo = new AnnotationInfo(classAnnotationInfo);
    this.access = access;
    this.name = name;
    this.desc = desc;
  }
  
  public String toString()
  {
    return this.name + " " + this.desc;
  }
  
  public boolean isMatch(String methodName, String methodDesc)
  {
    if ((this.name.equals(methodName)) && (this.desc.equals(methodDesc))) {
      return true;
    }
    return false;
  }
  
  public AnnotationInfo getAnnotationInfo()
  {
    return this.annotationInfo;
  }
}
