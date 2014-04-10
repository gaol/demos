/**
 * 
 */
package org.jboss.demos.threads;

import org.jboss.demos.Demo;

/**
 * @author lgao
 *
 */
public class ThreadStackDemo {

	// =======================================================
	//   Demo of stack overflow error
	// =======================================================
	@Demo(name = "stackOverflow", description = "demos show the stack overflow error")
	public void stackOverflow() {
		
		final Thread t1 = new Thread("Demo-Thread-1") {
			@Override
			public void run() {
				try
				{
					 // sometimes -Xss512k will work
					// java -Xss512k -jar target/common-cli.jar
					recursive(5000L);
					System.out.println("Works fine!!");
				} catch (StackOverflowError e) {
					System.err.println("!!!Got StackOverflowError!!!");
					System.err.println("Stack size: " + e.getStackTrace().length);
				}
				
			}
		};
		t1.start();
	}
	
	@Demo(name = "stackOverflow-Loop", description = "demos show that within a loop, there won't StackOverFlowError")
	public void stackOverflowLoop() {
		
		final Thread t1 = new Thread("Demo-Thread-1") {
			@Override
			public void run() {
				loop(100000000L);
			}
		};
		t1.start();
	}
	
	private void recursive(long n) {
		if (n > 0) {
			recursive(n - 1);
		}
	}
	
	private void loop(long n) {
		while (n >= 0) {
			n --;
		}
	}
}
