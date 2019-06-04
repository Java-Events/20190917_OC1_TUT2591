package org.rapidpm.junit.engine.nano;

import org.junit.platform.commons.support.ReflectionSupport;
import org.junit.platform.engine.*;
import org.junit.platform.engine.discovery.ClassSelector;
import org.junit.platform.engine.discovery.PackageSelector;
import org.junit.platform.engine.support.descriptor.EngineDescriptor;
import org.rapidpm.dependencies.core.logger.HasLogger;

import java.util.function.Predicate;

import static org.junit.platform.commons.support.AnnotationSupport.isAnnotated;
import static org.junit.platform.commons.util.ReflectionUtils.isAbstract;
import static org.junit.platform.commons.util.ReflectionUtils.isPrivate;
import static org.rapidpm.frp.matcher.Case.match;
import static org.rapidpm.frp.matcher.Case.matchCase;
import static org.rapidpm.frp.model.Result.failure;
import static org.rapidpm.frp.model.Result.success;

public class NanoEngine
    implements TestEngine, HasLogger {

  public static final String ENGINE_ID = NanoEngine.class.getSimpleName();

  @Override
  public String getId() {
    return ENGINE_ID;
  }


  private final Predicate<Class<?>> checkClass() {
    return classCandidate -> match(matchCase(
        () -> failure("this class is not a supported by this TestEngine - " + classCandidate.getSimpleName())),
                                   matchCase(() -> isAbstract(classCandidate), () -> failure(
                                       "no support for abstract classes" + classCandidate.getSimpleName())),
                                   matchCase(() -> isPrivate(classCandidate), () -> failure(
                                       "no support for private classes" + classCandidate.getSimpleName())),
                                   matchCase(() -> isAnnotated(classCandidate, NanoTest.class),
                                             () -> success(Boolean.TRUE))).ifFailed(msg -> logger().info(msg))
                                                                          .getOrElse(() -> Boolean.FALSE);
  }

  @Override
  public TestDescriptor discover(EngineDiscoveryRequest request, UniqueId engineID) {
    EngineDescriptor rootNode = new EngineDescriptor(engineID, "The NanoEngine");

    //could create your own Selector ?
    request.getSelectorsByType(PackageSelector.class)
           .forEach(selector -> {
             appendTestInPackage(selector.getPackageName(), rootNode);
           });

    request.getSelectorsByType(ClassSelector.class)
           .forEach(classSelector -> {
             appendTestInClass(classSelector.getJavaClass(), rootNode);
           });

    return rootNode;
  }

  private void appendTestInClass(Class<?> javaClass, EngineDescriptor rootNode) {
    if (checkClass().test(javaClass)) rootNode.addChild(new NanoEngineClassTestDescriptor(javaClass, rootNode));
  }

  private void appendTestInPackage(String packageName, EngineDescriptor rootNode) {

    ReflectionSupport.findAllClassesInPackage(packageName, checkClass(), name -> true)
                     .stream()
                     .map(javaClass -> new NanoEngineClassTestDescriptor(javaClass, rootNode))
                     .forEach(rootNode::addChild);
  }

  @Override
  public void execute(ExecutionRequest request) {
    TestDescriptor rootNode = request.getRootTestDescriptor();
    new NanoEngineTestExecutor().execute(request, rootNode);

  }
}
