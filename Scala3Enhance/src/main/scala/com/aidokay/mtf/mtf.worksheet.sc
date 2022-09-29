import cats.data.{EitherT, ReaderT}
import cats.instances.all.catsStdInstancesForFuture
import scala.concurrent.Future
import scala.language.reflectiveCalls
import scala.concurrent.ExecutionContext.global
import cats.Functor.*

type Response
type Post
type Token
type Request <: {def token: Token}


def allPosts(): EitherT[Future, String, List[Post]] = ???
def respond[A](a: A): Response = ???
def authenticate(token: Token): EitherT[Future,String, Boolean] = ???

def handle(request: Request): EitherT[Future, String, Response] =
  for {
    authenticated  <- authenticate(request.token)
      .ensure("You are not authorized to perform this action")(identity)
    posts          <-  if (authenticated) allPosts()
    response = respond(posts)
  } yield response


