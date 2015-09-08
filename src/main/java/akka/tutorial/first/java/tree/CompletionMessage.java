package akka.tutorial.first.java.tree;

/**
 * Created by rohanm on 5/7/15.
 */
public class CompletionMessage {
    private int workerId;

    public CompletionMessage(int workerId) {
        this.workerId = workerId;
    }

    public int getWorkerId () {
        return this.workerId;
    }
}
