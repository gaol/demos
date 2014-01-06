package org.jboss.demos.threads;

import java.util.LinkedList;
import java.util.List;

import org.jboss.demos.common.SystemInformation;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

/**
 * Demonstration on why a new Thread consume resources
 */
public class ThreadsUsageDemo
{

   private static final long DEFAULT_THREADS = 1000L;

   private static final int DEFAULT_CHECKPOINT = 50;
   
   private static final String THREAD_NAME_PREFIX = "JBoss-Demoes-Threads-";

   @Option(name = "total", usage = "Specify how many threads will be created in total for test. Default to 50000 threads.")
   private long total;

   @Option(name = "checkpoint", usage = "Specify the checkpoint when check the system resources usage. Default to 500 threads.")
   private int checkpoint;
   
   private SystemInformation sysInfo;

   private List<Thread> threadHolders = new LinkedList<>();
   
   public ThreadsUsageDemo()
   {
      sysInfo = new SystemInformation();
   }
   
   public static void main(String[] args)
   {
      ThreadsUsageDemo demo = new ThreadsUsageDemo();
      CmdLineParser parser = new CmdLineParser(demo);
      try
      {
         if (args != null && args.length > 1)
         {
            parser.parseArgument(args);
         }
      }
      catch (CmdLineException e)
      {
         e.printStackTrace();
         parser.printUsage(System.out);
         return;
      }
      demo.run();
   }

   private void run()
   {
      if (total == 0)
      {
         total = DEFAULT_THREADS;
      }
      if (checkpoint == 0)
      {
         checkpoint = DEFAULT_CHECKPOINT;
      }
      
      System.out.println("Total threads: " + total);
      System.out.println("CheckPoints: " + checkpoint);
      
      sysInfo.printBasicInformation();
      
      for(long i = 0; i < total; i ++)
      {
         Thread t = new Thread(new Runnable()
         {
            public void run()
            {
               try
               {
                  // serves request for 1 second.
                  Thread.sleep(1000);
                  
                  // collect 1MB data
                  String s = "";
                  for (long j=0;j<1024 * 1024; j ++)
                  {
                     s = s + "j-";
                  }
               }
               catch (InterruptedException e)
               {
               }
            }
         }, THREAD_NAME_PREFIX + i);
         
         t.start();
         
         threadHolders.add(t);
         if (i > 0 && i % checkpoint == 0)
         {
            System.out.println("\nResource Usage after " + i + " threads:");
            sysInfo.printUsageInformation();
         }
      }
      
      System.out.println("\nResource Usage after " + total + " threads:");
      sysInfo.printUsageInformation();
      threadHolders.clear();
   }
}
