package akka.tutorial.first.java.gossip;

import akka.actor.ActorRef;

public class ForwardMessage {

    public ActorRef target;

    public ForwardMessage(ActorRef target) {
        this.target = target;
    }



    public ActorRef getTarget() {
        return target;
    }
}
