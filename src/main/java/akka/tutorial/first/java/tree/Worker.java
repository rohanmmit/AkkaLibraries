package akka.tutorial.first.java.tree;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

import java.util.ArrayList;
import java.util.HashSet;

public  class Worker extends UntypedActor {
    private int workerId;
    private HashSet<Integer> initialParents;
    private ActorRef initialChild;
    private ActorRef master;
    private ArrayList<ActorRef> secondaryChildren;
    private double [] vector;

    public Worker(int workerId, double [] vector) {
        this.workerId = workerId;
        this.vector = vector;
    }

    public boolean isLeaf(int numNodes) {
        int half = (numNodes - 1) / 2;
        if (workerId <= half - 1) {
            return false;
        }
        return true;
    }

    public void handleInitialize(Object message) {
        InitializeMessage initializeMessage = (InitializeMessage) message;
        this.master = initializeMessage.getMaster();
        ActorRef [] nodes = initializeMessage.getNodes();
        if (this.workerId != 0) {
            int initalChildIndex = (this.workerId - 1) / 2;
            initialChild = nodes[initalChildIndex];
        }
        initialParents = new HashSet<Integer>();
        if (!isLeaf(nodes.length)) {
            int base = (workerId + 1) * 2;
            initialParents.add(base);
            initialParents.add(base - 1);
        }
        secondaryChildren = new ArrayList<ActorRef>();
        for (Integer parent: initialParents) {
            ActorRef parentRef = nodes[parent];
            secondaryChildren.add(parentRef);
        }
    }

    public void averageArrays  (double [] values)  {
        for (int i = 0; i < values.length; i++) {
            double value = values[i];
            this.vector[i] = (this.vector[i] + value) / 2;
        }
    }

    public void handleConvergeWorkerMessage(Object message) {
        WorkerConvergeMessage workerMessage = (WorkerConvergeMessage) message;
        int givenId = workerMessage.getWorkerId();
        averageArrays(workerMessage.getVector());
        initialParents.remove(givenId);
        if (initialParents.isEmpty()) {
                if (this.workerId != 0) {
                    WorkerConvergeMessage convergeMessage = new WorkerConvergeMessage(this.workerId, this.vector);
                    initialChild.tell(convergeMessage);
                } else {
                    for (ActorRef child: secondaryChildren) {
                        WorkerDivergeMessage targetWorkerMessage = new WorkerDivergeMessage(this.vector);
                        child.tell(targetWorkerMessage);
                    }
                    master.tell(new CompletionMessage(this.workerId));
                }
        }
    }

    public void handleDivergeWorkerMessage(Object message) {
        WorkerDivergeMessage workerMessage = (WorkerDivergeMessage) message;
        double [] vector = workerMessage.getVector();
        for (ActorRef child: secondaryChildren) {
                WorkerDivergeMessage targetWorkerMessage = new WorkerDivergeMessage(vector);
                child.tell(targetWorkerMessage);
        }
        master.tell(new CompletionMessage(this.workerId));
        getContext().stop(getSelf());

    }


    public void handleStart(Object message) {
        if (initialParents.isEmpty()) {
            WorkerConvergeMessage newWorkerMessage = new WorkerConvergeMessage(this.workerId, this.vector);
            initialChild.tell(newWorkerMessage);
        }
    }

    public void onReceive(Object message) {
        if (message instanceof WorkerConvergeMessage) {
            handleConvergeWorkerMessage(message);
        } else if (message instanceof  WorkerDivergeMessage) {
            handleDivergeWorkerMessage(message);
        } else if (message instanceof InitializeMessage) {
            handleInitialize(message);
        } else if (message instanceof StartMessage) {
            handleStart(message);
        }
    }
}
