/**
 * 
 */
package org.jboss.demos.threads;

import java.util.concurrent.atomic.AtomicInteger;

import org.jboss.demos.Demo;

/**
 * @author lgao
 *
 */
public class AtomicIntegerDemo {

	
	@Demo(name = "atomicInteger", description = "demos show usage of AtomicInteger")
	public void demoAtomic() {
		AtomicInteger atomicInt = new AtomicInteger(100);
		boolean result = atomicInt.compareAndSet(100, 101);
		System.out.println("Result of compareAndSet(100,101): " + result);
		System.out.println("After compareAndSet(100,101): " + atomicInt.get());
		
		int get = atomicInt.addAndGet(100);
		System.out.println("addAndGet(100) = " + get);
		System.out.println("After addAndGet(100) = " + atomicInt.get());
		
	}
}
