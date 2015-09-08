package akka.tutorial.first.java.gossip;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public  class Master extends UntypedActor {
    ArrayList<Integer> availableNodes;
    HashSet<Integer> finishedNodes;
    ActorRef [] workers;
    HashMap<Integer, Integer> matchedIds;
    long currentTime;
    boolean flag;
    public Master () {
        availableNodes = new ArrayList<Integer>();
        finishedNodes = new HashSet<Integer>();
        matchedIds = new HashMap<Integer, Integer>();
        flag = true;
    }

    public void handleInitializationMessage(Object message) {
        InitializationMasterMessage initializationMasterMessage = (InitializationMasterMessage) message;
        workers = initializationMasterMessage.getNodes();
        for (int i = 0; i < workers.length; i++) {
            availableNodes.add(i);
            matchedIds.put(i,-1);
        }
    }

    public void notifyMatches(int index1, int index2) {
        ActorRef worker1 = workers[index1];
        ActorRef worker2 = workers[index2];
        ForwardMessage forwardMessage = new ForwardMessage(worker2);
        matchedIds.put(index1, index2);
        matchedIds.put(index2, index1);
        worker1.tell(forwardMessage);
    }

    public void matchWorkers() {
        int i = 0;
        while (i < availableNodes.size() - 1) {
            int workerId = availableNodes.get(i);
            int previousMatchedId = matchedIds.get(workerId);
            int j = i + 1;
            while ( j < availableNodes.size()) {
                int possibleWorkerId = availableNodes.get(j);
                if (possibleWorkerId != previousMatchedId) {
                    notifyMatches(workerId,possibleWorkerId);
                    availableNodes.remove(j);
                    availableNodes.remove(i);
                    i = i - 1;
                    break;
                }
                j++;
            }
            i = i + 1;
        }
    }

    public void handleVectorMessage(Object message) {
        VectorMasterMessage vectorMasterMessage = (VectorMasterMessage) message;
        int workerId = vectorMasterMessage.getWorkerId();
        availableNodes.add(workerId);
        finishedNodes.add(workerId);
        if (! vectorMasterMessage.getIsFinished()) {
            finishedNodes.remove(workerId);
        }
        matchWorkers() ;
        if (finishedNodes.size() == workers.length) {
            long finishedTime = System.currentTimeMillis() - currentTime;
            if (this.flag) {
                System.out.println("we are done" + finishedTime);
                this.flag = false;
                getContext().system().shutdown();
            }
        }
    }

    public void onReceive(Object message) {
        if (message instanceof InitializationMasterMessage) {
            handleInitializationMessage(message);
            currentTime = System.currentTimeMillis();
            matchWorkers();
        } else if (message instanceof VectorMasterMessage) {
            handleVectorMessage(message);
        }
    }
}
