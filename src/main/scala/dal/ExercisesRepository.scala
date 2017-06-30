package dal

import reactivemongo.bson.BSONDocument
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object ExercisesRepository extends Repository[BSONDocument] {
  override protected val collectionName: String = "exercises"

  override def findDocuments(query: BSONDocument): Future[List[BSONDocument]] = ???

  override def insertDocument(doc: BSONDocument): Future[Unit] = ???
}
