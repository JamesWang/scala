
import shapeless.LabelledGeneric
import sp1.Models.{Circle, Rectangle, Shape}

LabelledGeneric[Shape].to(Circle(1.0))

val d = LabelledGeneric[Shape].to(Circle(1.0))
println(d)