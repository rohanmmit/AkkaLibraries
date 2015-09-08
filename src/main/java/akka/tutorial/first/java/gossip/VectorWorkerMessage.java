package akka.tutorial.first.java.gossip;

public class VectorWorkerMessage {
    public double [] values;

    public VectorWorkerMessage(double[] values) {
        this.values = values;
    }

    public double [] getValues() {
        return values;
    }
}
