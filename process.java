public class process {
    int pid, burstTime, priority, completionTime;

    //Constructors
    //Default priority = 1
    public process(int pid, int burstTime) {
        this.pid = pid;
        this.burstTime = burstTime;
        priority = 1;
        //Default completion time
        completionTime = -1;
    }

    //Given a priority
    public process(int pid, int burstTime, int priority) {
        this(pid, burstTime);
        this.priority = priority;
    }

    //Getters
    public int getpid() {return pid;}
    public int getburstTime() {return burstTime;}
    public int getpriority() {return priority;}
    public int getcompletionTime() {return completionTime;}

    //Setters
    public void setburstTime(int burstTime) {this.burstTime = burstTime;}
    public void setcompletionTime(int completionTime) {this.completionTime = completionTime;}

    //Print process
    public void print() {
        System.out.printf("pid: %-2d burstTime: %-4d priority: %-3d completionTime: %-4d waitingTime: %-4d", pid, burstTime, priority, completionTime, completionTime - burstTime);
    }
}