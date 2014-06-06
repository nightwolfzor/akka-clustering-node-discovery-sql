package chat

import net.fwbrasil.activate.entity.EntityWithGeneratedID
import net.fwbrasil.activate.entity.id.SequencedIdGenerator
import net.fwbrasil.activate.sequence.LongSequenceEntity

import ActivatePersistenceContext._
/**
 * Created by Nathan on 6/06/14.
 */
class ClusterNode (val address: String, var state: String, var lastReport: Long) extends EntityWithGeneratedID[Long] {

}

class MyEntityIdGenerator
   extends SequencedIdGenerator[ClusterNode](LongSequenceEntity(sequenceName = "myEntitySequence", step = 1))

class CreateSchemaMigration extends Migration {

  def timestamp = 123l

  def up {

    table[ClusterNode]
       .createTable(
         _.column[String]("address"),
         _.column[String]("state"),
         _.column[Long]("lastReport")
       )

    table("LongSequenceEntity")
       .createTable(
//         _.column[String]("id"),
         _.column[String]("name"),
         _.column[Long]("value"),
         _.column[Int]("step")
       )
  }

}