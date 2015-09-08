package akka.tutorial.first.java.tree;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

import java.util.HashSet;

/**
 * Created by rohanm on 5/7/15.
 */
public class Master  extends UntypedActor {
    private ActorRef [] nodes ;
    private HashSet <Integer> workerIds;
    private  long startTime ;

    public Master (ActorRef [] nodes) {
        this.nodes = nodes;
        workerIds = new HashSet<Integer>();
        for (int i = 0; i < nodes.length; i++) {
            workerIds.add(i);
        }

    }



    public void handleStartMessage(Object message) {
        for (int i = 0; i < nodes.length; i++) {
            nodes[i].tell(new StartMessage());
        }
        this.startTime = System.currentTimeMillis();
    }

    public void handleCompletionMessage (Object message) {
        CompletionMessage completionMessage = (CompletionMessage) message;
        int workerId = completionMessage.getWorkerId();
        workerIds.remove(workerId);
        if (workerIds.isEmpty()) {
            long finishedTime = System.currentTimeMillis();
            long duration = finishedTime - startTime;
            System.out.println("we are finished" + duration);

        }
    }

    public void onReceive(Object message) {
        if (message instanceof StartMessage) {
            handleStartMessage(message);
        } else if (message instanceof CompletionMessage) {
            handleCompletionMessage(message);
        }
    }
}
