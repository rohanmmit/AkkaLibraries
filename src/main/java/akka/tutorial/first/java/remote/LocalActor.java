package akka.tutorial.first.java.remote;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.UntypedActor;

/**
 * Created by rohanm on 4/29/15.
 */
public class LocalActor extends UntypedActor {
        @Override
        public void onReceive(Object message) throws Exception {
            VectorMessage vectorMessage = (VectorMessage) message;
            //System.out.println("local actor received a a message" + vectorMessage.getByteArray().length);
            getSender().tell(vectorMessage,getSelf());
        }

        public LocalActor() {
            System.out.println("initializing local actor");

        }


}

