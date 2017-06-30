package entity

import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter, BSONObjectID}

case class TestEntity(id: BSONObjectID, login: String, age: Int)

object TestEntity {

  implicit object testEntityReader extends BSONDocumentReader[TestEntity] {
    def read(doc: BSONDocument): TestEntity = {
      TestEntity(
        doc.getAs[BSONObjectID]("_id").get,
        doc.getAs[String]("login").get,
        doc.getAs[Int]("age").get
      )
    }
  }

  implicit object testEntityWriter extends BSONDocumentWriter[TestEntity] {
    def write(entity: TestEntity): BSONDocument = {
      BSONDocument(
        "_id" -> entity.id,
        "login" -> entity.login,
        "age" -> entity.age
      )
    }
  }

}
