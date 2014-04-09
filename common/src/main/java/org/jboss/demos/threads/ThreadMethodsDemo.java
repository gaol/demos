/**
 * 
 */
package org.jboss.demos.threads;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.jboss.demos.Demo;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

/**
 * @author lgao
 * 
 * <p>
 *         A demo on the usage of thread methods like:
 * 
 *         <ul>
 *         <li>Object.wait()
 *         <li>Object.notify()
 *         <li>Object.notifyAll()
 *         <li>Thread.interrupt()
 *         <li>Thread.join()
 *         </ul>
 */
public class ThreadMethodsDemo {
	
	@Option(name = "method", usage = "Specify which method do you want to execute.")
	private String method = "usage";

	public static void main(String[] args) throws Exception {
		ThreadMethodsDemo demo = new ThreadMethodsDemo();
		CmdLineParser parser = new CmdLineParser(demo);
		try {
			if (args != null) {
				parser.parseArgument(args);
			}
		} catch (CmdLineException e) {
			parser.printUsage(System.out);
			return;
		}
		Method mtd = demo.getClass().getMethod(demo.method);
		if (mtd == null)
		{
			throw new IllegalArgumentException("Can't find method: " + demo.method);
		}
		System.out.println("Start to invoke method: " + demo.method);
		System.out.println(" ====================== ");
		mtd.invoke(demo);
		System.out.println();
		System.out.println("End of invoke method: " + demo.method);
		System.out.println(" ====================== ");
	}
	
	public void usage()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Available methods are: \n");
		for (Method m: this.getClass().getDeclaredMethods())
		{
			if (m.getModifiers() == Modifier.PUBLIC)
			{
				sb.append("\n\t" + m.getName());
			}
		}
		System.out.println(sb);
	}
	
	@Demo(name = "waitMethod", description = "demos show the wait method call.")
	public void waitMethod()
	{
		Object obj = new Object();
		try {
			obj.wait();
		}
		catch (IllegalMonitorStateException imse)
		{
			System.out.println("Catched expected IllegalMonitorStateException: " + imse.getMessage() + "\n");
			System.out.println("\t Because there is no sychronized block before Object.wait() is called.");
			System.out.println("\nStack Trace is: \n");
			imse.printStackTrace();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Demo(name = "test", description = "test demo")
	public void test()
	{
		System.out.println("\tHello, it works!");
	}

}
