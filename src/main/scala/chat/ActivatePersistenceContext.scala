package chat

import net.fwbrasil.activate.ActivateContext
import com.github.mauricio.async.db.Configuration
import com.github.mauricio.async.db.mysql.pool.MySQLConnectionFactory
import net.fwbrasil.activate.storage.relational.async.AsyncMySQLStorage
import com.github.mauricio.async.db.pool.PoolConfiguration


/**
 * Created by Nathan on 20/04/14.
 */
object ActivatePersistenceContext extends ActivateContext {


  override protected def entitiesPackages: List[String] = List("chat")

  val storage = new AsyncMySQLStorage {
    def configuration =
      new Configuration(
        username = "sai-app",
        host = "sai-ucn1.home.com",
        port = 3306,
        password = Some("Power@240app"),
        database = Some("akka-cluster"))
    lazy val objectFactory = new MySQLConnectionFactory(configuration)

    override def poolConfiguration: PoolConfiguration = new PoolConfiguration(20, 4, 50)
  }
}