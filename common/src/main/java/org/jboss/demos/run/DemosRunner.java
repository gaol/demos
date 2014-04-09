/**
 * 
 */
package org.jboss.demos.run;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.jboss.demos.run.Demos.Demo;

/**
 * @author lgao
 *
 */
public class DemosRunner {

	private static DemosRunner singleRuner;
	
	public static DemosRunner getSingleRunner () {
		if (singleRuner == null) {
			singleRuner = new DemosRunner(Demos.scanCurrentJar());
		}
		return singleRuner;
	}
	
	private final Demos demos;
	
	public DemosRunner(Demos demos) {
		this.demos = demos;
	}
	
	public Demos getDemos() {
		return this.demos;
	}
	
	public void run(String demoName) throws InstantiationException, IllegalAccessException, 
	  IllegalArgumentException, InvocationTargetException {
		Demo demo = demos.getDemo(demoName);
		if (demo == null) {
			throw new IllegalArgumentException("Demo: " + demoName + " does not exist!");
		}
		Method method = demo.getMethod();
		Class<?> cls = method.getDeclaringClass();
		Object obj = cls.newInstance(); //TODO check constructor.
		Class<?> paramsTypes[] = method.getParameterTypes();
		Object params[] = new Object[paramsTypes.length];
		int i = 0;
		for (Class<?> pType: paramsTypes) {
			Object param = pType.newInstance(); //TODO check constructor.
			params[i] = param;
			i ++;
		}
		method.invoke(obj, params);
	}
}
