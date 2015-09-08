package akka.tutorial.first.java.tree;

// diverge message is once you have converged and you are trying to tell your others about your value
public class WorkerDivergeMessage {
    double [] vector;
    public WorkerDivergeMessage( double [] vector) {
        this.vector = vector;
    }

    public double [] getVector() {
        return this.vector;
    }
}
