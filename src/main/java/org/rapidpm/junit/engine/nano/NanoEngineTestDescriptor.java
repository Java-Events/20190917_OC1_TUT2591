package org.rapidpm.junit.engine.nano;

import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;
import org.junit.platform.engine.support.descriptor.ClassSource;

import java.lang.reflect.Method;

public class NanoEngineTestDescriptor
    extends AbstractTestDescriptor {

  private final Class<?> testClass;

  public NanoEngineTestDescriptor(Method method, Class<?> testClass, NanoEngineTestDescriptor parent) {
    super(parent.getUniqueId()
                .append("method", testClass.getName()), method.getName());
    this.testClass = testClass;
  }


  @Override
  public Type getType() {
    return Type.CONTAINER;
  }

  public NanoEngineTestDescriptor(Class<?> testClass, TestDescriptor parent) {
    super(parent.getUniqueId()
                .append("class", testClass.getName()), testClass.getSimpleName(), ClassSource.from(testClass));

    this.testClass = testClass;
    setParent(parent);
    addAllChildren();
  }

  private void addAllChildren() {

  }
}
