/**
 * 
 */
package org.jboss.demos.threads;

import org.jboss.demos.Demo;

/**
 * @author lgao
 *
 */
public class ThreadDeadLockDemo {

	
	// =======================================================
	//   Demo of dead lock
	// =======================================================
	@Demo(name = "deadLock", description = "demos show what is dead lock.(wait for each other)")
	public void deadLock() {
		
		final Object obj = new Object();
		final Object another = new Object();
		
		final Thread t1 = new Thread("Demo-Thread-1") {
			@Override
			public void run() {
				System.out.println("Thread 1 locks obj:");
				synchronized(obj) {
					System.out.println("Thread 1 locks another:");
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					synchronized (another) {
						System.out.println("thread 1: some time it dead locks");
					}
				}
			}
		};
		
		Thread t2 = new Thread("Demo-Thread-2") {
			@Override
			public void run() {
				System.out.println("Thread 2 locks another:");
				synchronized(another) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("Thread 2 locks obj:");
					t1.interrupt();
					synchronized (obj) {
						System.out.println("thread 2: some time it dead locks");
					}
				}
			}
		};
		
		t1.start();
		t2.start();
	}
	
	// =======================================================
	//   Demo of dead lock
	// =======================================================
	@Demo(name = "waitForever", description = "demos show the dead lock type - wait forever.")
	public void waitForever() {
		
		final Object obj = new Object();
		
		final Thread t1 = new Thread("Demo-Thread-1") {
			@Override
			public void run() {
				System.out.println("Thread 1 locks obj:");
				synchronized(obj) {
					try {
						System.out.println("thread 1 waits lock obj without notify()");
						obj.wait();
						System.out.println("NOT HAPPEN EVER!!!");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		
		Thread t2 = new Thread("Demo-Thread-2") {
			@Override
			public void run() {
				System.out.println("Thread 2 locks obj:");
				synchronized(obj) {
					try {
						System.out.println("thread 2 waits lock obj without notify()");
						obj.wait();
						System.out.println("NOT HAPPEN EVER!!!");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		
		t1.start();
		t2.start();
	}
}
