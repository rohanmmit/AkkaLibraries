package akka.tutorial.first.java.remote;

import akka.actor.*;

public class RemoteActor extends UntypedActor {

    @Override
    public void onReceive(Object message) throws Exception {
        VectorMessage vectorMessage =  (VectorMessage) message;
        //System.out.println("remote actor received" + this.getSender());
        long difference = System.currentTimeMillis() - pastTime;
        if(difference > 60000) {
            System.out.println("the counter is" + counter);
        } else if (difference > 30000) {
            counter +=1;
        }
        getSender().tell(vectorMessage, getSelf());

    }
    ActorRef target;
    byte [] values;
    long pastTime;
    int counter = 0;
    public RemoteActor(ActorRef target, byte [] values)
    {
        this.target = target;
        this.values = values;
        pastTime =  System.currentTimeMillis();
        target.tell(new VectorMessage(values), getSelf());
        System.out.println("remote actor initialized");
    }




}
