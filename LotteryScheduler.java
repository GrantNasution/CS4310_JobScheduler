import java.lang.System.*;
import java.io.*;
import java.util.*;

public class LotteryScheduler {
    //List of processes
    ArrayList<process> processes;
    //Queue of finished processes
    Queue finishedQueue;
    //Number of tickets
    int prioritySum;
    //Cycles till context switch
    int timeQuantum;
    //Average turnaround time
    int avgTat;
    
    public LotteryScheduler(ArrayList<process> processes, int timeQuantum) {
        this.processes = new ArrayList<process>();
        finishedQueue = new LinkedList<process>();
        prioritySum = 0;
        avgTat = -1;
        this.timeQuantum = timeQuantum;
        fillProcesses(processes);
        //Sort by high to low priority
        Collections.sort(this.processes, new PriorityComparator());
    }

    //run simulation
    public void run(BufferedWriter bw) throws IOException {
                //Logical representation of time
                int time = 0;
                //If OS is in context switch
                Boolean contextSwitch = false;
                //Time for context switch
                int csTime = 3, csCount = 0;
                //Summation of turn around times
                int tatSum = 0;
                //Process index currently being worked on
                int index = 0;
                //Decides which process to pick
                Random rand = new Random();
                int roll = 0;
                //If process is running or not
                Boolean processRunning = false;
                //String to write to file
                String writeToFile = "";
                while(!processes.isEmpty()) {
                    //Write CPUTime
                    writeToFile += String.format("%5d, ", time);
                    //If in context switch decrement current process bursttime
                    if(!contextSwitch) {
                        //If no process running pick one
                        if(!processRunning) {
                            roll = rand.nextInt(prioritySum);
                            for(int i = 0; i < processes.size(); ++i) {
                                //If roll is less than priority then select that process to run
                                if(roll < processes.get(i).getpriority()) {
                                    index = i;
                                    break;
                                }
                                //Else subtract current process priority from roll
                                else {
                                    roll -= processes.get(i).getpriority();
                                }
                            }
                            contextSwitch = false;
                            processRunning = true;
                        }
                            //Write pid
                            writeToFile += String.format("%2d, ", processes.get(index).getpid());
                            //Write startBt
                            writeToFile += String.format("%5d, ", processes.get(index).getburstTime());
                            //Get burstTime of current running process and decrement by 1
                            int burstTime = ((process)processes.get(index)).getburstTime() - 1;
                            //Write endBt
                            writeToFile += String.format("%5d, ", burstTime);
                            //Set new burstTime
                            processes.get(index).setburstTime(burstTime);
                            //If burstTime is 0 remove process and add to finishedQueue
                            if(burstTime == 0) {
                                process p = processes.get(index);
                                processes.remove(index);
                                p.setcompletionTime(time);
                                tatSum += time;
                                finishedQueue.add(p);
                                prioritySum -= p.getpriority();
                                //Set flags
                                contextSwitch = true;
                                processRunning = false;
                                //Write completionTime
                                writeToFile += String.format("%5d, ", time);
                            }
                            else {
                                //Write completionTime
                                writeToFile += String.format("%5d, ", 0);
                            }
                            //Initiate context switch
                            //timeQuantum - 1, because time starts at 0
                            if(time % (timeQuantum - 1) == 0 && time != 0 && index < processes.size() && processes.get(index) != null) {
                                //If Last process dont initiate context switch
                                if(processes.size() != 1) {
                                    contextSwitch = true;
                                    processRunning = false;
                                }
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
                            processRunning = false;
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

    //Getters
    public int getavgTat() {return avgTat;}

    //Helper functions
    //Copy processes into LotteryScheduler's processes
    private void fillProcesses(ArrayList<process> processes) {
        for(process p : processes) {
            this.processes.add(new process(p.getpid(), p.getburstTime(), p.getpriority()));
            prioritySum += p.getpriority();
        }
    }

    //Print processes in finished queue
    public void print() {
        for(Object p : finishedQueue) {
            ((process)p).print();
            System.out.println();
        }
    }
}

class PriorityComparator implements Comparator<process> {
    //Overriding compare() method of Comparator  
    //Sort from high to low priority
    public int compare(process p1, process p2) { 
        if (p1.getpriority() > p2.getpriority()) 
            return 1; 
        else
            return -1; 
    } 
}