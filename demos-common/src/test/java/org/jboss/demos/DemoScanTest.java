/**
 * 
 */
package org.jboss.demos;

import java.lang.reflect.Method;
import java.util.List;

import org.jboss.demos.run.Demos;
import org.jboss.demos.run.Demos.Demo;
import org.jboss.demos.threads.ThreadMethodsDemo;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author lgao
 *
 */
public class DemoScanTest {

	@Test
	public void testDemoAnnotationScan() {
		Demos demos = Demos.scanCurrentJar();
		List<org.jboss.demos.run.Demos.Demo> demoList = demos.getDemos();
		
		Assert.assertNotNull(demoList);
		Assert.assertTrue(demoList.size() > 0);
		
		Demo waitDemo = demos.getDemo("waitMethod");
		Assert.assertNotNull(waitDemo);
		Assert.assertEquals("waitMethod", waitDemo.getName());
		Assert.assertEquals("demos show the wait method call.", waitDemo.getDescription());
		Method method = waitDemo.getMethod();
		
		Assert.assertNotNull(method);
		Assert.assertEquals("waitMethod", method.getName());
		Assert.assertEquals(ThreadMethodsDemo.class, method.getDeclaringClass());
	}
}
