import dal.{ExercisesRepository, TestRepository}
import entity.TestEntity
import reactivemongo.bson.{BSONDocument, BSONDocumentWriter, BSONObjectID}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

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
    val user = TestEntity(BSONObjectID.generate(), "some user", 1)

    TestRepository.insertDocument(user) onComplete{
      case Success(_) => println("inserted")
      case Failure(exception) => println(exception)
    }
  }

  def findUser(): Unit = {
    TestRepository.findDocuments(BSONDocument()) onComplete {
      case Success(r) => r foreach { d: TestEntity =>
        println(s"[${d.id}] ${d.login} - ${d.age}" )
      }
      case Failure(e) => println(e)
    }
  }

}