package dal

import scala.concurrent.ExecutionContext.Implicits.global
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONDocument}

import scala.concurrent.Future

/**
  * Created by Oleg_Gelya on 6/30/2017.
  */
trait Repository[T] {
  //import scala.concurrent.ExecutionContext.Implicits.global

  private val driver = new reactivemongo.api.MongoDriver
  private val connection = driver.connection(List("localhost"))

  private val databaseName = "FitnessDiary"
  protected val collectionName: String

  protected def getCollection: Future[BSONCollection] =
    connection.database(databaseName).map(_.collection(collectionName))

  def getCollectionNames: Future[List[String]] =
    connection.database(databaseName).flatMap(_.collectionNames)

  def findDocuments(query: BSONDocument): Future[List[T]]
  def insertDocument(doc: T): Future[Unit]
}
