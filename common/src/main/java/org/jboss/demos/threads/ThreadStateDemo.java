/**
 * 
 */
package org.jboss.demos.threads;

import java.util.concurrent.Executor;

import org.jboss.demos.Demo;

/**
 * @author lgao
 *
 */
public class ThreadStateDemo {

	@Demo(name = "threadState", description = "show the thread state change.")
	public void demoThreadState() {
		
		// one monitor
		final Object obj = new Object();
		
		final Thread t1 = new Thread("Demo-Thread-1") {
			@Override
			public void run() {
				System.out.println("After run is called, the state of the thread 1 is: " + getState().name());
				synchronized(obj) {
					System.out.println("After getting the monitor lock, the state of the thread 1 is: " + getState().name());
					try {
						obj.wait();
						System.out.println("After thead 1 wait() returns, the state of the thread 1 is: " + getState().name());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
			@Override
			public synchronized void start() {
				System.out.println("Before actuall run, the state of the thread 1 is still: " + getState().name());
				super.start();
			}
		};
		
		System.out.println("Before call Thread.start(), the state of the thread 1 is: " + t1.getState().name());
		t1.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		final Thread t2 = new Thread("Demo-Thread-2") {
			
			@Override
			public void run() {
				synchronized (obj) {
					System.out.println("After another thread get the monitor lock, the state of t1 is: " + t1.getState().name());
					
					// only notify can wake up t1
					obj.notify();
					
					try {
						System.out.println("thread 2 starts waiting for max 10 seconds");
						obj.wait(10000);
						System.out.println("tread 2 ends waiting.");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		t2.start();
		
		
		Thread t3 = new Thread("Demo-Thread-3") {
			
			@Override
			public void run() {
				try {
					t1.join(); // waiting for thread 1 terminates
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
//				t1.start();
				
				System.out.println("when thread 3 starts, thread 1 has been terminated: " + t1.getState().name());
				synchronized (obj) {
					System.out.println("Thread 3 gets the monitor lock, the state of t2 is: " + t2.getState().name());
					
					// notify t2
					obj.notify();
					
				}
			}
		};
		t3.start();
	}

}
