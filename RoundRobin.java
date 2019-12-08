import java.util.*;
import java.io.*;

public class RoundRobin {
    //processQueue contains processes that havent completed
    //finishedQueue contains process that have completed
    Queue processQueue, finishedQueue;
    //Average turnaround time, time Quantum
    int avgTat, timeQuantum;
    public RoundRobin(ArrayList<process> processes, int timeQuantum) {
        processQueue = new LinkedList<process>();
        finishedQueue = new LinkedList<process>();
        fillQueue(processes);
        //Default avgTat
        avgTat = -1;
        this.timeQuantum = timeQuantum;
    }

    //Run simulation
    public void run(BufferedWriter bw) throws IOException {
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
                //Initiate context switch
                //tmieQuantum - 1, because time starts at 0
                if(time % (timeQuantum - 1) == 0 && time != 0 && processQueue.peek() != null) {
                    contextSwitch = true;
                    //Remove process currently being worked on and add to end of queue
                    processQueue.add((process)processQueue.remove());
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

    //Print processes in finished queue
    public void print() {
        for(Object p : finishedQueue) {
            ((process)p).print();
            System.out.println();
        }
    }

    //getters
    public int getavgTat() {return avgTat;}

    //Helper functions
    //Copy given processes into processQueue
    private void fillQueue(ArrayList<process> processes) {
        for(process p : processes) {
            processQueue.add(new process(p.getpid(), p.getburstTime(), p.getpriority()));
        }
    }
}