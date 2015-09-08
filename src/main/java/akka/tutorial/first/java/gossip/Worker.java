package akka.tutorial.first.java.gossip;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public  class Worker extends UntypedActor {
    double [] values;
    double [] desiredValues;
    private boolean isFinished;
    int workerId;
    ActorRef master;
    private double threshold;



    public Worker(int workerId, double [] values, ActorRef master, double [] desiredValues, double threshold) {
        this.values = values;
        this.workerId = workerId;
        this.master = master;
        isFinished = false;
        this.threshold = threshold;
        this.desiredValues = desiredValues;
    }

    public void balanceValues(double [] otherValues) {
        for (int i = 0; i < values.length; i++) {
            values[i] = (otherValues[i] + values[i]) / 2;
        }
    }

    public void handleForwardMessage(Object message) {
        ForwardMessage forwardMessage = (ForwardMessage) message;
        ActorRef target = forwardMessage.getTarget();
                VectorWorkerMessage vectorWorkerMessage = new VectorWorkerMessage(values);
        target.tell(vectorWorkerMessage, getSelf());
    }

    public void updateMaster() {
        updateIsFinished();
        VectorMasterMessage message = new VectorMasterMessage(workerId, isFinished);
        master.tell(message);
    }
    public void handleAckMessage(Object message) {
        AckMessage ackMessage = (AckMessage) message;
        double [] newValues = ackMessage.getValues();
        balanceValues(newValues);
        updateMaster();
    }

    public void handleVectorMessage(Object message) {
        VectorWorkerMessage vectorWorkerMessage = (VectorWorkerMessage) message;
        double [] newValues = vectorWorkerMessage.getValues();
        AckMessage ackMessage = new AckMessage(values);
        getSender().tell(ackMessage);
        balanceValues(newValues);
        updateMaster();
    }

    public double computeDistance(double [] vector1, double [] vector2)  {
        double sum = 0;
        for (int i = 0; i < vector1.length; i++) {
            double difference = Math.abs(vector1[i] - vector2[i]);
            sum += difference;
        }
        return sum;
    }

    public void updateIsFinished() {
        if  (computeDistance(this.values, this.desiredValues) < threshold) {
            this.isFinished = true;
        } else {
            this.isFinished = false;
        }
    }


    public void onReceive(Object message) {
        if (message instanceof ForwardMessage) {
            handleForwardMessage(message);
        } else if (message instanceof AckMessage) {
            handleAckMessage(message);
        } else if (message instanceof VectorWorkerMessage) {
            handleVectorMessage(message);
        }
    }
}