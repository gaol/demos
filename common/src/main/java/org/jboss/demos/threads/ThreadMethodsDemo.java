/**
 * 
 */
package org.jboss.demos.threads;

import org.jboss.demos.Demo;

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
	
	// =======================================================
	//   Demo of illegal wait method call - no synchronized
	// =======================================================
	@Demo(name = "illegalWaitNoSync", description = "demos show the illegal wait method call. (without synchronized keyword)")
	public void demoIllegalWaitNoSync() {
		Object obj = new Object();
		try {
			obj.wait();
		}
		catch (IllegalMonitorStateException imse)
		{
			System.out.println("  Catched expected IllegalMonitorStateException: " + imse);
			System.out.println("  Stack Trace is: \n");
			imse.printStackTrace();
		}
		catch (InterruptedException e) {
			System.out.println("Single run won't cause the InterruptedException!!!");
			e.printStackTrace();
		}
	}
	
	// =======================================================
	//   Demo of illegal wait method call - synchronized on different object
	// =======================================================
	@Demo(name = "illegalWaitDiffSync", description = "demos show the illegal wait method call.(synchronized on different object)")
	public void demoIllegalWaitDiffSync() {
		Object obj = new Object();
		Object another = new Object();
		try {
			synchronized (another) {
				obj.wait();
			}
		}
		catch (IllegalMonitorStateException imse)
		{
			System.out.println("  Catched expected IllegalMonitorStateException: " + imse);
			System.out.println("  Stack Trace is:");
			imse.printStackTrace();
		}
		catch (InterruptedException e) {
			System.out.println("  Single run won't cause the InterruptedException!!!");
			e.printStackTrace();
		}
	}
	
	// =======================================================
	//   Demo of legal wait method call
	// =======================================================
	@Demo(name = "demoWait", description = "demos show the normal wait method call.")
	public void demoWait() {
		final Object obj = new Object();
		Thread t1 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				synchronized (obj) {
					try {
						System.out.println("  It waits another thread to release monitor.");
						obj.wait();
						System.out.println("  After wait() returns, going on!");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}
			}
		});
		final Thread t2 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				synchronized (obj) {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					obj.notify();
					System.out.println("  After notify, thread 1 will going on.");
				}
			}
		});
		t1.start();
		t2.start();
	}
	
	
	// =======================================================
	//   Demo of legal wait method call
	// =======================================================
	@Demo(name = "syncWithoutWait", description = "demos show normal synchronized block")
	public void syncWithoutWait() {
		final Object obj = new Object();
		Thread t1 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				synchronized (obj) {
					System.out.println("  thread 1 holds the lock.");
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("It is thread 2's turn now!");
				}
			}
		});
		final Thread t2 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				synchronized (obj) {
					System.out.println("  thread 2 holds the lock");
				}
			}
		});
		t1.start();
		try {
			Thread.sleep(500); // wait thread 1 starts.
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		t2.start();
	}
	
	// =======================================================
	//   Demo of interrupt method call
	// =======================================================
	@Demo(name = "interrupt", description = "demos show the interrupt() method call")
	public void interrupt() {
		
		final Object obj = new Object();
		
		final Thread t1 = new Thread() {
			@Override
			public void run() {
				synchronized(obj) {
					try {
						obj.wait();
						System.out.println("!!!!THIS LINE NEVER HAPPEN!!!!!");
					} catch (InterruptedException e) {
						System.out.println("!GOT YOU!");
						System.out.println("isInterrupted() afterInterruptedException catch: " + isInterrupted());
						e.printStackTrace();
					}
				}
			}
		};
		
		Thread t2 = new Thread() {
			@Override
			public void run() {
				synchronized(obj) {
					System.out.println("\ncall t1.interrupt()");
					t1.interrupt();
					System.out.println("Now, the interrupted state is: " + t1.isInterrupted());
					try {
						sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("After sleep 5 seconds, it still happen before the InterruptedException catch.");
				}
				try {
					sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("outside of the synchronized block won't happen before the InterruptedException catch.");
			}
		};
		
		t1.start();
		t2.start();
	}

}
