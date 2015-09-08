package akka.tutorial.first.java.tree;
 
 
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;

 
 
public class Tree {
 
  public static void main(String[] args) {
    Tree tree = new Tree();
    System.out.println("started");
     tree.start(65536);

  }

  public double [] createVector (int length) {
    double [] values = new double [length];
    for (int i = 0; i < length; i++) {
      values[i] = Math.random();
    }
    return values;
  }

  public ActorRef [] constructNodes(ActorSystem system, int numberOfWorkers, int vectorSize) {
    ActorRef [] nodes = new ActorRef[numberOfWorkers];
    for ( int i = 0; i < numberOfWorkers; i++) {
      // create the master
      final int j = i;
      String name = "worker" + j;
      final double [] vector = createVector(vectorSize);
      final ActorRef worker = system.actorOf(new Props(new UntypedActorFactory() {
        public UntypedActor create() {
          return new Worker(j, vector);
        }
      }), name);
      nodes[i] = worker;
    }
    return nodes;
  }

  public void constructGraph(ActorRef [] nodes, ActorRef master) {
     for (int i = 0; i < nodes.length; i++) {
        ActorRef worker = nodes[i];
        InitializeMessage message = new InitializeMessage(nodes, master);
        worker.tell(message);
     }
  }

  public void startNodes(ActorRef [] nodes) {
    for (int i = 0; i < nodes.length; i++) {
      nodes[i].tell(new StartMessage());
    }
  }

  public void start(final int numberOfWorkers) {
    // Create an Akka system
    ActorSystem system = ActorSystem.create("Tree");
    final ActorRef [] nodes = constructNodes(system, numberOfWorkers, 100);
    final ActorRef master = system.actorOf(new Props(new UntypedActorFactory() {
      public UntypedActor create() {
        return new Master(nodes);
      }
    }), "master");
    constructGraph(nodes, master);
    master.tell(new StartMessage());
  }
}
