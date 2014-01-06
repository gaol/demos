/**
 * 
 */
package org.jboss.demos.socket;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author Lin Gao <lgao@redhat.com>
 *
 */
public class SocketClientDemo
{

   /**
    * @param args
    */
   public static void main(String[] args) throws Exception
   {
      final String host = System.getProperty("host", "127.0.0.1");
      final Integer port = Integer.getInteger("port", 10290);
      
      Integer count = Integer.getInteger("count", 100);
      
      for (int i = 0; i < count; i ++)
      {
         Thread t = new Thread(){
            
            @Override
            public void run()
            {
               try
               {
                  System.out.println("Connecting");
                  Socket client = new Socket();
                  client.connect(new InetSocketAddress(host, port));
                  OutputStream out = client.getOutputStream();
                  PrintWriter writer = new PrintWriter(out);
                  writer.write("Hello, Bye!\n");
                  try
                  {
                     Thread.sleep(5000);
                  }
                  catch (InterruptedException e)
                  {
                     e.printStackTrace();
                  }
                  writer.close();
                  client.close();
                  System.out.println("Closed.");
               }
               catch (IOException e)
               {
                  e.printStackTrace();
               }
               
            }
         };
         t.start();
      }
      System.out.println("Total clients: " + count);
   }

}
