import java.lang.System.*;
import java.io.*;
import java.util.*;


public class CPUScheduler {
    public static void main(String args[]) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(args[0])));
            ArrayList<process> processes = readProcesses(br);

            //Running the schedulers
            FCFS fcfs = new FCFS(processes);
            fcfs.run(new BufferedWriter(new FileWriter("fcfs_" + args[0] + "_GrantNasution.csv")));
            System.out.println("FCFS");
            fcfs.print();

            SJF sjf = new SJF(processes);
            sjf.run(new BufferedWriter(new FileWriter("sjf_" + args[0] + "_GrantNasution.csv")));
            System.out.println("SJF");
            sjf.print();

            RoundRobin roundrobin20 = new RoundRobin(processes, 20);
            roundrobin20.run(new BufferedWriter(new FileWriter("roundrobin20_" + args[0] + "_GrantNasution.csv")));
            System.out.println("roundrobin20");
            roundrobin20.print();

            RoundRobin roundrobin40 = new RoundRobin(processes, 40);
            roundrobin40.run(new BufferedWriter(new FileWriter("roundrobin40_" + args[0] + "_GrantNasution.csv")));
            System.out.println("roundrobin40");
            roundrobin40.print();

            LotteryScheduler ls = new LotteryScheduler(processes, 40);
            ls.run(new BufferedWriter(new FileWriter("LotteryScheduler_" + args[0] + "_GrantNasution.csv")));
            System.out.println("Lottery Scheduler");
            ls.print();

            BufferedWriter bw = new BufferedWriter(new FileWriter("AvgTat_" + args[0] + ".csv"));
            bw.write(String.format("%4d, %4d, %4d, %4d, %4d", fcfs.getavgTat(), sjf.getavgTat(), roundrobin20.getavgTat(), roundrobin40.getavgTat(), ls.getavgTat()));
            bw.close();
        }
        catch(Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    //Read processes from file and add to arraylist
    public static ArrayList<process> readProcesses(BufferedReader br) throws IOException{
        int pid, burstTime, priority;
        ArrayList<process> result = new ArrayList<process>();
        while(br.ready()) {
            pid = Integer.valueOf(br.readLine());
            burstTime = Integer.valueOf(br.readLine());
            priority = Integer.valueOf(br.readLine()); 
            result.add(new process(pid, burstTime, priority));
        }
        return result;
    }
}