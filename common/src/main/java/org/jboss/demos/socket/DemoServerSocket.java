/**
 * 
 */
package org.jboss.demos.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Lin Gao <lgao@redhat.com>
 *
 */
public class DemoServerSocket
{
   
   private ServerSocket server;

   public DemoServerSocket() throws IOException
   {
      super();
   }
   
   private void startSocketServer()
   {
      String host = System.getProperty("host", "localhost");
      Integer port = Integer.getInteger("port", 10290);
      bindAndStart(host, port);
   }
   
   
   private void bindAndStart(final String host, final int port)
   {
      Thread serverThread = new Thread(new Runnable()
      {
         
         @Override
         public void run()
         {
            try
            {
               server = new ServerSocket();
               InetSocketAddress endPoint = new InetSocketAddress(host, port);
               System.out.println("Tring to start server at: " + host + ":" + port);
               server.bind(endPoint);
               System.out.println("Server started!");
               while (true)
               {
                  Socket client = server.accept();
                  ClientThread clientThread = new ClientThread(client);
                  clientThread.start();
               }
            }
            catch (IOException e)
            {
               e.printStackTrace();
               System.exit(1);
            }
         }
      });
      serverThread.setDaemon(false);
      serverThread.start();
   }


   private class ClientThread extends Thread
   {
      private Socket client;
      
      private ClientThread(Socket client)
      {
         super();
         this.client = client;
         this.setDaemon(true);
      }
      
      @Override
      public void run()
      {
         int localPort = client.getLocalPort();
         int remotePort = client.getPort();
         String localHost = client.getLocalAddress().getHostAddress();
         String remoteHost = client.getInetAddress().getHostAddress();
         System.out.println("4 elements are: \tlocal:" + localHost + ":" + localPort + " == remote:" + remoteHost + ":" + remotePort);
         try
         {
            InputStream input = client.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line = reader.readLine();
            if ("Hello, Bye!".equals(line))
            {
               System.out.println("Got it. Bye!");
            }
            try
            {
               Thread.sleep(5000);
            }
            catch (InterruptedException e)
            {
               e.printStackTrace();
            }
            reader.close();
            client.close();
         }
         catch (IOException e)
         {
            e.printStackTrace();
         }
      }
   }
   

   /**
    * @param args
    */
   public static void main(String[] args) throws Exception
   {
      new DemoServerSocket().startSocketServer();
   }

}
