package chat

import akka.actor._
import akka.cluster._
import akka.remote._
import ActivatePersistenceContext._
import akka.remote.AssociationErrorEvent

object Main {
  def main(args: Array[String]): Unit = {
    val systemName = "ChatApp"
    val system1 = ActorSystem(systemName)
    
    val cluster = Cluster(system1)
    
    val listener = system1.actorOf(Props(classOf[SimpleClusterListener], cluster), "clusterStateSub")
    system1.eventStream.subscribe(listener, classOf[RemotingLifecycleEvent])
    
    val joinAddress = cluster.selfAddress
    println(joinAddress)


    transactional {
      val person = new ClusterNode(joinAddress.toString, "ANNOUNCE", 123L)
      println(person)
    }

    val testAddr = AddressFromURIString("akka.tcp://ChatApp@192.168.2.106:1233")
    val testAddr2 = AddressFromURIString("akka.tcp://ChatApp@192.168.2.106:1234")
    
    //cluster.join(joinAddress)
    // cluster.join(testAddr2)
    
    system1.actorOf(Props[MemberListener], "memberListener")
    system1.actorOf(Props[RandomUser], "Ben")
    system1.actorOf(Props[RandomUser], "Kathy")



    //Thread.sleep(5000)
    val system2 = ActorSystem(systemName)
    val c2 = Cluster(system2)
    c2.join(joinAddress)
    transactional {
      val person = new ClusterNode(c2.selfAddress.toString, "ANNOUNCE", 124L)
      println(person)
    }
    system2.actorOf(Props[RandomUser], "Skye")



   // Thread.sleep(10000)
    val system3 = ActorSystem(systemName)
    val c3 = Cluster(system3)
    c3.join(joinAddress)
    transactional {
      val person = new ClusterNode(c3.selfAddress.toString, "ANNOUNCE", 125L)
      println(person)
    }

    system3.actorOf(Props[RandomUser], "Miguel")
    system3.actorOf(Props[RandomUser], "Tyler")
    //Thread.sleep(10000)
    cluster.join(joinAddress)
  }
}

import akka.cluster.Cluster
import akka.cluster.ClusterEvent._

class SimpleClusterListener(val cluster: Cluster) extends Actor with ActorLogging {
 
  // subscribe to cluster changes, re-subscribe when restart 
  override def preStart(): Unit = {
    //#subscribe
    cluster.subscribe(self, initialStateMode = InitialStateAsEvents,
      classOf[MemberEvent], classOf[UnreachableMember], classOf[ClusterDomainEvent], classOf[ReachabilityEvent])
    //#subscribe
  }
  override def postStop(): Unit = cluster.unsubscribe(self)
 
  def receive = {
    case MemberUp(member) =>
      log.info("***Member is Up: {}", member.address)
    case UnreachableMember(member) =>
      log.info("***Member detected as unreachable: {}", member)
    case MemberRemoved(member, previousStatus) =>
      log.info("***Member is Removed: {} after {}",
        member.address, previousStatus)
    case ae: AssociationErrorEvent =>
        log.info("Trying to join proper")
        // cluster.join(cluster.selfAddress)
    case m => log.info("*** Unknown Message: " + m)
  }
}