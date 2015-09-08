package akka.tutorial.first.java.gossip;


import akka.actor.ActorRef;

public class InitializationMasterMessage {

    private ActorRef[] nodes;

    public InitializationMasterMessage(ActorRef[] nodes) {
        this.nodes = nodes;
    }

    public ActorRef [] getNodes() {
        return nodes;
    }

}
