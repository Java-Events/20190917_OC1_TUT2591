package org.rapidpm.junit.engine.nano;

import org.junit.platform.commons.util.ReflectionUtils;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;
import org.junit.platform.engine.support.descriptor.ClassSource;
import org.rapidpm.dependencies.core.logger.HasLogger;

import java.lang.reflect.Method;
import java.util.function.Predicate;

public class NanoEngineClassTestDescriptor
    extends AbstractTestDescriptor
    implements HasLogger {

  private final Class<?> testClass;

  public NanoEngineClassTestDescriptor(Class<?> testClass, TestDescriptor parent) {
    super(parent.getUniqueId()
                .append("class", testClass.getSimpleName()), testClass.getSimpleName(), ClassSource.from(testClass));

    this.testClass = testClass;

    setParent(parent);
    addChildren();
  }

  private void addChildren() {

    Predicate<Method> isTestMethod = method -> {
      if (ReflectionUtils.isStatic(method)) return false;
      if (ReflectionUtils.isPrivate(method)) return false;
      if (ReflectionUtils.isAbstract(method)) return false;
      if (method.getParameterCount() > 0) return false;
      return method.getReturnType()
                   .equals(void.class);
    };

    ReflectionUtils.findMethods(testClass, isTestMethod)
                   .stream()
                   .map(method -> new NanoEngineMethodTestDescriptor(method, testClass, this))
                   .forEach(this::addChild);

  }


  @Override
  public Type getType() {
    return Type.CONTAINER;
  }
}
