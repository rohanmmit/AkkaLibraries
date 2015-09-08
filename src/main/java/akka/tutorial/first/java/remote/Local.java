package akka.tutorial.first.java.remote;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.File;

/**
 * Created by rohanm on 4/29/15.
 */
public class Local {

    public static void main(String [] args) {
        Local l = new Local();
    }

    public Local() {
        ClassLoader classLoader = getClass().getClassLoader();
        File localFile = new File((classLoader).getResource("local-configuration.conf").getFile());
        Config localConfig = ConfigFactory.parseFile(localFile);
        ActorSystem localSystem = ActorSystem.create("local", localConfig);
        final ActorRef localActor = localSystem.actorOf(new Props(LocalActor.class), "worker");
    }
}
