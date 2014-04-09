/**
 * 
 */
package org.jboss.demos.common;

import java.util.Map;

/**
 * 
 * Demos just to show the command line:
 * 'java -DsysProp1=YYYY' will set to system property to current JVM.
 * 
 * @author Lin Gao <lgao@redhat.com>
 *
 */
public class SystemPropertyDemo
{

   /**
    * @param args
    */
   public static void main(String[] args)
   {
      System.out.println("System properties:");
      boolean found = false;
      for (Map.Entry<Object, Object> entry: System.getProperties().entrySet())
      {
         if (entry.getKey().toString().equals("sysProp1"))
         {
            System.out.println("Got the system property from command line: -DsysProp1=" + entry.getValue().toString());
            found = true;
         }
      }
      if (! found){
         throw new RuntimeException("No -DsysProp1= system properties set!");
      }
   }

}
