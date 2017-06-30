package dal

import entity.TestEntity

import scala.concurrent.ExecutionContext.Implicits.global
import reactivemongo.bson.{BSONDocument}

import scala.concurrent.Future

object TestRepository extends Repository[TestEntity] {
  override protected val collectionName: String = "testCollection"

  override def findDocuments(query: BSONDocument): Future[List[TestEntity]] =
    getCollection.flatMap(collection => {
      collection.find(query).cursor[TestEntity]().collect[List]()
    })

  override def insertDocument(entity: TestEntity): Future[Unit] =
    getCollection.map(_.insert(entity))
}
