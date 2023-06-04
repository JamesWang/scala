trait RationalTrait(val numerArg: Int, val denomArg: Int):
  require(denomArg != 0)
  private val g = gcd(numerArg, denomArg)
  val numer = numerArg / g
  val denom = denomArg / g

  private def gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)

  override def toString: String = s"$numer/$denom"


val x = 3
new RationalTrait(1 * x, 2 * x ){}
