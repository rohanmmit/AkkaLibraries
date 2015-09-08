package akka.tutorial.first.java.gossip;


public class AckMessage {

    public double [] values;

    public AckMessage(double[] values) {
        this.values = values;
    }

    public double [] getValues() {
        return values;
    }
}
