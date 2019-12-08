import java.util.*;
import java.io.*;

public class FCFS {
    //processQueue contains processes that havent completed
    //finishedQueue contains process that have completed
    Queue processQueue, finishedQueue;
    //Average turnaround time
    int avgTat;
    public FCFS(ArrayList<process> processes) {
        processQueue = new LinkedList<process>();
        finishedQueue = new LinkedList<process>();
        fillQueue(processes);
        //Defaul avgTat
        avgTat = -1;
    }

    //Print processes in finished queue
    public void print() {
        for(Object p : finishedQueue) {
            ((process)p).print();
            System.out.println();
        }
    }

    //Helper functions
    //Copy given processes into processQueue
    private void fillQueue(ArrayList<process> processes) {
        for(process p : processes) {
            processQueue.add(new process(p.getpid(), p.getburstTime(), p.getpriority()));
        }
    }

    //Runs simulation
    public void run(BufferedWriter bw)  throws IOException {
        //Logical representation of time
        int time = 0;
        //If OS is in context switch
        Boolean contextSwitch = false;
        //Time for context switch
        int csTime = 3, csCount = 0;
        //Summation of turn around times
        int tatSum = 0;
        //String to write to file
        String writeToFile = "";
        //Run until all processes are complete
        while(processQueue.peek() != null) {
            //Write CPUTime
            writeToFile += String.format("%5d, ", time);
            //If in context switch decrement current process bursttime
            if(!contextSwitch) {
                //Write pid
                writeToFile += String.format("%2d, ", ((process)processQueue.peek()).getpid());
                //Write startBt
                writeToFile += String.format("%5d, ", ((process)processQueue.peek()).getburstTime());
                //Get burstTime of current running process and decrement by 1
                int burstTime = ((process)processQueue.peek()).getburstTime() - 1;
                //Write endBt
                writeToFile += String.format("%5d, ", burstTime);
                //Set new burstTime
                ((process)processQueue.peek()).setburstTime(burstTime);
                //If burstTime is 0 remove process and add to finishedQueue
                if(burstTime == 0) {
                    process p = (process) processQueue.remove();
                    p.setcompletionTime(time);
                    tatSum += time;
                    finishedQueue.add(p);
                    contextSwitch = true;
                    //Write completionTime
                    writeToFile += String.format("%5d, ", time);
                }
                else {
                    //Write completionTime
                    writeToFile += String.format("%5d, ", 0);
                }
            }
            //In context switch
            else {
                //Inc time in cs
                ++csCount;
                //Reset csCount and end cs
                if(csCount == csTime) {
                    csCount = 0;
                    contextSwitch = false;
                }
            }
            bw.write(writeToFile + "\n");
            //reset string to write to file
            writeToFile = new String();
            //Increment time
            ++time;
        }
        //Compute avg turn around time
        avgTat = tatSum / finishedQueue.size();
        //Close writer
        bw.close();
    }

    
    //getters
    public int getavgTat() {return avgTat;}
}