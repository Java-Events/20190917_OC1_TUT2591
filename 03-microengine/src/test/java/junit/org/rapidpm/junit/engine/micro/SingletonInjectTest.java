package junit.org.rapidpm.junit.engine.micro;

import junit.org.rapidpm.junit.engine.micro.mocks.MyStaticMockedService;
import org.rapidpm.dependencies.core.logger.HasLogger;
import org.rapidpm.junit.engine.micro.MicroTest;
import org.rapidpm.junit.engine.micro.MicroTestClass;

import javax.inject.Inject;

@MicroTestClass
public class SingletonInjectTest
    implements HasLogger {

  @Inject private MyStaticMockedService staticService;


  @MicroTest
  void test001() {
    logger().info("test001 - " + staticService.getCreated());
  }


  @MicroTest
  void test002() {
    logger().info("test002 - " + staticService.getCreated());
  }
}
