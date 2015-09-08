package akka.tutorial.first.java.gossip;


public class VectorMasterMessage {
    private int workerId;
    private boolean isFinished;


    public VectorMasterMessage(int workerId, boolean isFinished) {
        this.workerId = workerId;
        this.isFinished = isFinished;
    }

    public int getWorkerId() {
        return workerId;
    }

    public boolean getIsFinished() {
        return isFinished;
    }


}
