package org.rapidpm.junit.engine.nano;

import org.junit.platform.commons.util.ReflectionUtils;
import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.support.descriptor.EngineDescriptor;
import org.rapidpm.dependencies.core.logger.HasLogger;

public class NanoEngineTestExecutor
    implements HasLogger {

  public void execute(ExecutionRequest request, TestDescriptor rootNode) {
    if (rootNode instanceof EngineDescriptor) executeContainer(request, rootNode);
    if (rootNode instanceof NanoEngineClassTestDescriptor) executeContainer(request, rootNode);
    if (rootNode instanceof NanoEngineMethodTestDescriptor) executeMethod(request,
                                                                          (NanoEngineMethodTestDescriptor) rootNode);
  }

  private void executeContainer(ExecutionRequest request, TestDescriptor rootNode) {
    request.getEngineExecutionListener()
           .executionStarted(rootNode);

    rootNode.getChildren()
            .forEach(c -> execute(request, c));

    request.getEngineExecutionListener()
           .executionFinished(rootNode, TestExecutionResult.successful());
  }

  private void executeMethod(ExecutionRequest request, NanoEngineMethodTestDescriptor descriptor) {
    request.getEngineExecutionListener()
           .executionStarted(descriptor);
    TestExecutionResult executionResult = executeTestMethod(descriptor);
    request.getEngineExecutionListener()
           .executionFinished(descriptor, executionResult);
  }

  private TestExecutionResult executeTestMethod(NanoEngineMethodTestDescriptor descriptor) {

    try {
      Object newInstance = ReflectionUtils.newInstance(descriptor.getTestClass());
      return invokeTestMethod(descriptor, newInstance);
    } catch (Exception e) {
      String msg = "can  not create instance of " + descriptor.getClass() + " -- " + e.getLocalizedMessage();
      logger().warning(msg);
      return TestExecutionResult.failed(new RuntimeException(msg, e));
    }
  }

  private TestExecutionResult invokeTestMethod(NanoEngineMethodTestDescriptor descriptor, Object newInstance) {
    try {
      //could check result types for more detailed return info
      ReflectionUtils.invokeMethod(descriptor.getTestMethod(), newInstance);
      return TestExecutionResult.successful();
    } catch (Exception e) {
      logger().warning(e.getLocalizedMessage());
      return TestExecutionResult.failed(e);
    }
  }


}
