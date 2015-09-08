package akka.tutorial.first.java.tree;

// converge message is when you are trying to converge values

public  class WorkerConvergeMessage {
    private final int workerId;
    private final double [] vector;

    public WorkerConvergeMessage(int workerId, double [] vector) {
        this.workerId = workerId ;
        this.vector = vector;
    }

    public int getWorkerId() {
        return workerId;
    }

    public double [] getVector() {
        return this.vector;
    }
}
