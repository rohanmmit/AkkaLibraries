package akka.tutorial.first.java.remote;

/**
 * Created by rohanm on 4/29/15.
 */
public class VectorMessage implements  java.io.Serializable {
    byte [] vector;
    public VectorMessage (byte [] vector) {
        this.vector = vector;
    }

    public byte [] getByteArray() {
        return vector;
    }
}
