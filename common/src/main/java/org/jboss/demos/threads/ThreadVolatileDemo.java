/**
 * 
 */
package org.jboss.demos.threads;

import org.jboss.demos.Demo;

/**
 * @author lgao
 *
 */
public class ThreadVolatileDemo {

	private int v = 0;
	
	private volatile int vv = 0;
	
//	@Demo(name = "nonVolatile", description = "shows the non-volatile keyword")
	public void nonVolatileDemo() {
		
		final Object obj = new Object();
		
		Thread t1 = new Thread("Demo-Thread-SetValue") {
			@Override
			public void run() {
				int loop = 10;
				for(int i = 0;i<=loop;i++) {
					synchronized(obj) {
						System.out.println("Set V = " + i);
						v = i;
						
						obj.notify();
						
						try {
							obj.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		};
		t1.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Thread t2 = new Thread("Demo-Thread-ReadValue") {
			@Override
			public void run() {
				int loop = 10;
				for(int i = 0;i<=loop;i++) {
					synchronized(obj) {
						System.out.println("Red V: " + v);
						
						obj.notify();
						if (i != loop) { // not the last one
							try {
								obj.wait();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		};
		t2.start();
		
	}
	
//	@Demo(name = "volatile", description = "shows the volatile keyword")
	public void volatileDemo() {
		
		Thread t1 = new Thread("Demo-Thread-SetValue") {
			@Override
			public void run() {
				int loop = 10;
				for(int i = 0;i<=loop;i++) {
					int newV = i;
					System.out.println("Set VV = " + newV);
					vv = newV;
//					try {
//						Thread.sleep(1000);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
				}
			}
		};
		t1.start();
		
		Thread t2 = new Thread("Demo-Thread-ReadValue") {
			@Override
			public void run() {
				int loop = 10;
				for(int i = 0;i<=loop;i++) {
					System.out.println("Red VV: " + vv);
//					try {
//						Thread.sleep(1000);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
				}
			}
		};
		t2.start();
		
	}
}
