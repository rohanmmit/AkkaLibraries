package akka.tutorial.first.java.gossip;

import akka.actor.*;

public class Gossip {
    public static final double threshold = 0.05;
    public static void main(String[] args) {
        Gossip gossip = new Gossip();
        System.out.println("started");
        gossip.start(32767);
    }

    public double [] createVector (int length) {
        double [] values = new double [length];
        for (int i = 0; i < length; i++) {
            values[i] = Math.random();
        }
        return values;
    }

    public double [][] createVectors(int vectorLength, int numWorkers) {
        double[][] vectors = new double[numWorkers][vectorLength];
        for (int i = 0; i < numWorkers; i++) {
            vectors[i] = createVector(vectorLength);
        }
        return vectors;
    }

    public double computeMagnitude (double [] vector) {
        double magnitude = 0.0;
        for (int i = 0; i < vector.length; i++) {
            double value = vector[i];
            magnitude += value;
        }
        return magnitude;
    }



    public double [] averageVectors(double [] [] vectors, int vectorLength) {
        double [] averageVector = new double[vectorLength];
        for (int i = 0; i < vectors.length; i++) {
            double [] newVector = vectors[i];
            for (int j = 0; j <vectorLength; j++) {
                double newValue = (averageVector[j] * i + newVector[j]) / (i + 1);
                averageVector[j] = newValue;
            }
        }
        return averageVector;
    }

    public ActorRef [] constructWorkers(ActorSystem system, int numberOfWorkers, ActorRef  master, double [][] vectors, final double [] desiredValues, final double optimumThreshold) {
        ActorRef [] nodes = new ActorRef[numberOfWorkers];
        final ActorRef tempMaster = master;
        for ( int i = 0; i < numberOfWorkers; i++) {
            final int j = i;
            final double [] specificVector = vectors[j];
            String name = "worker" + j;
            final ActorRef worker = system.actorOf(new Props(new UntypedActorFactory() {
                public UntypedActor create() {
                    return new Worker(j, specificVector, tempMaster, desiredValues, optimumThreshold);
                }
            }), name);
            nodes[i] = worker;
        }
        return nodes;
    }

    public void start(final int numberOfWorkers) {
        // Create an Akka system
        ActorSystem system = ActorSystem.create("Gossip");
        int vectorLength = 100;
        double [][] vectors = createVectors(vectorLength, numberOfWorkers);
        final double [] averageVector = averageVectors(vectors, vectorLength);
        final double optimumThreshold = threshold * computeMagnitude(averageVector);
        final ActorRef master = system.actorOf(new Props(new UntypedActorFactory() {
            public UntypedActor create() {
                return new Master();
            }
        }), "master");
        ActorRef[] workers = constructWorkers(system, numberOfWorkers, master, vectors, averageVector, optimumThreshold);
        InitializationMasterMessage initializationMasterMessage = new InitializationMasterMessage(workers);
        master.tell(initializationMasterMessage);
    }
}
