package akka.tutorial.first.java.remote;
import akka.actor.*;
import akka.tutorial.first.java.gossip.Master;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.File;
import java.util.Random;

/**
 * Created by rohanm on 4/29/15.
 */
public class Remote {

    public static void main (String [] args) {
       Remote r = new Remote();
    }

    public byte [] createRandomArray(int size) {
        byte [] values = new byte [size];
        Random r = new Random();
        r.nextBytes(values);
        return values;
    }
    public Remote() {
        ClassLoader classLoader = getClass().getClassLoader();
        File localFile = new File((classLoader).getResource("remote_application.conf").getFile());
        Config localConfig = ConfigFactory.parseFile(localFile);
        ActorSystem actorSystem = ActorSystem.create("remotesystem", localConfig);
        final ActorRef localActor = actorSystem.actorFor("akka://local@127.0.0.1:2552/user/worker");
        final byte [] values = createRandomArray(1000000);
        final ActorRef remoteActor = actorSystem.actorOf(new Props(new UntypedActorFactory() {
            public UntypedActor create() {
                return new RemoteActor(localActor,values);
            }
        }), "worker");
    }
}
