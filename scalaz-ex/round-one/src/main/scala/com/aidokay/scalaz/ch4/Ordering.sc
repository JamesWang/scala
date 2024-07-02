import com.aidokay.scalaz.ch4.ord.ANumeric
import com.aidokay.scalaz.ch4.ord.ANumeric.ops._

def signOfTheTimes[T: ANumeric](t: T): T = - t.abs * t //(t.abs * t).unary_
