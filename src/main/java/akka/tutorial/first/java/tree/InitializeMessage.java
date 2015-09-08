package akka.tutorial.first.java.tree;

import akka.actor.ActorRef;
import akka.tutorial.first.java.remote.VectorMessage;

public class InitializeMessage {

        private ActorRef[] nodes;
        private ActorRef master;
        public InitializeMessage(ActorRef[] nodes, ActorRef master) {
            this.nodes = nodes;
            this.master = master;
        }

        public ActorRef [] getNodes() {
            return nodes;
        }

        public ActorRef getMaster() {
            return master;
        }


}
