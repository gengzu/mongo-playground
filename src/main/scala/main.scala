
import reactivemongo.api.collections.bson.BSONCollection

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

import reactivemongo.bson.{
  BSONDocument, BSONDocumentWriter, BSONDocumentReader
}

object main {
  def main(args: Array[String]): Unit = {

    println("collections:")
    println("============")

    ExercisesRepository.getCollectionNames map(_.foreach(println))

    ExercisesRepository.getCollectionNames onComplete{
      case Success(list) => list foreach println
      case Failure(exception) => println(exception)
    }

    // ===========================
    //insertUser()
    findUser()
  }

  def insertUser(): Unit = {
    val testDocument = BSONDocument(
      "login" -> "gengzu",
      "age" -> 33
    )

    TestRepository.insertDocument(testDocument) onComplete{
      case Success(_) => println("inserted")
      case Failure(exception) => println(exception)
    }
  }

  def findUser(): Unit = {
    TestRepository.findDocuments(BSONDocument()) onComplete {
      case Success(r) => r foreach { d: BSONDocument =>
        println(d.getAs[String]("login").get)
        println(d.getAs[Int]("age").get)
      }
      case Failure(e) => println(e)
    }
  }

}

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

  def insertDocument(doc: BSONDocument): Future[Unit] = {
    getCollection.map(_.insert(doc))
  }

  def findDocuments(query: BSONDocument): Future[List[T]]
}

object ExercisesRepository extends Repository[BSONDocument] {
  override protected val collectionName: String = "exercises"

  override def findDocuments(query: BSONDocument): Future[List[BSONDocument]] = ???
}

object TestRepository extends Repository[BSONDocument] {
  override protected val collectionName: String = "testCollection"

  override def findDocuments(query: BSONDocument): Future[List[BSONDocument]] =
    getCollection.flatMap(_.find(query).cursor().collect[List]())
}