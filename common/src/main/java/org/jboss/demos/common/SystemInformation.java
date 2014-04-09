/**
 * 
 */
package org.jboss.demos.common;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;

/**
 * Class used to get system information.
 * 
 * @author Lin Gao <lgao@redhat.com>
 *
 */
public class SystemInformation
{
   static
   {
      try
      {
         Sigar.load();
      }
      catch (SigarException e)
      {
         throw new IllegalStateException("Can not load Sigar native library.", e);
      }
   }
   
   private static final int KB = 1024;
   
   private static final int MB = 1024 * 1024;
   
   private static final int GB = 1024 * 1024 * 1024;
   
   
   private final SigarProxy sigar;
   
   public SystemInformation()
   {
      this.sigar = new Sigar();
   }
   
   private void printMemInformation()
   {
      try
      {
         Mem mem = this.sigar.getMem();
         
         if (mem != null)
         {
            StringBuilder sb = new StringBuilder();
            long total = mem.getTotal();
            long used = mem.getActualUsed();
            long free = mem.getActualFree();
            sb.append("\n");
            sb.append("Memory Information: \n");
            sb.append("\tTotal: " + toByteString(total));
            sb.append("\n\tUsed:  " + toByteString(used));
            sb.append("\n\tFree:  " + toByteString(free));
            
            print(sb.toString());
         }
      }
      catch (SigarException e)
      {
         throw new RuntimeException("Can't get Memory information", e);
      }
   }
   
   private void printProcessUsage()
   {
      try
      {
         long pid = this.sigar.getPid();
         long freeMem = Runtime.getRuntime().freeMemory();
         long totalMem = Runtime.getRuntime().totalMemory();
         long usedMem = totalMem - freeMem;
         
         final OperatingSystemMXBean myOsBean= 
               ManagementFactory.getOperatingSystemMXBean();
         
         
         double load = myOsBean.getSystemLoadAverage();
   
         StringBuilder sb = new StringBuilder();
         sb.append("\n");
         sb.append("Process resource usage information:\n");
         sb.append("\t CPU: ");
//         sb.append("[Per: " + load + "%]");
         sb.append(this.sigar.getProcCpu(pid));
         sb.append("\n\t Mem: ");
         sb.append(this.sigar.getProcMem(pid));
//         sb.append("[Used: " + toByteString(usedMem) + ", Per: " + (((double)usedMem / (double)totalMem) * 100) + "%]");
         print(sb.toString());
         
      }
      catch (Exception e)
      {
         throw new RuntimeException("Can't get CPU usage information of this process.", e);
      }
   }
   
   private void printCPUInformation()
   {
      try
      {
         CpuInfo[] cpuInfos = this.sigar.getCpuInfoList();
         if (cpuInfos != null)
         {
            StringBuilder sb = new StringBuilder();
            int cpuCount = cpuInfos.length;
            sb.append("\n");
            sb.append("CPU count: " + cpuCount);
            sb.append("\n");
            for (CpuInfo cpuInfo: cpuInfos)
            {
               String vendor = cpuInfo.getVendor();
               String model = cpuInfo.getModel();
               sb.append("\t[Vendor: " + vendor);
               sb.append(", Model: " + model);
               sb.append("]\n");
            }
            print(sb.toString());
         }
      }
      catch (SigarException e)
      {
         throw new RuntimeException("Can't get CPU information", e);
      }
   }

   private String toByteString(long size)
   {
      if (size >= GB)
      {
         double sD = ((double)size) / ((double)GB);
         return String.valueOf(sD) + " GB";
      }
      if (size >= MB)
      {
         double sD = ((double)size) / ((double)MB);
         return String.valueOf(sD) + " MB";
      }
      if (size >= KB)
      {
         double sD = ((double)size) / ((double)KB);
         return String.valueOf(sD) + " KB";
      }
      return "" + size;
   }

   private void print(String string)
   {
      System.out.println(string);
   }
   
   public void printBasicInformation()
   {
      printCPUInformation();
      printMemInformation();
   }
   
   public void printUsageInformation()
   {
      printProcessUsage();
   }
   
   public static void main(String[] args)
   {
      SystemInformation info = new SystemInformation();
      
      info.printBasicInformation();
      info.printUsageInformation();
   }
   
}
