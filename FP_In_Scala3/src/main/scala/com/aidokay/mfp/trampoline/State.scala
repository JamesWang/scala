package com.aidokay.mfp.trampoline

case class State[S, +A](runS: S => Trampoline[(A, S)]) {
  def flatMap[B](f: A => State[S, B]): State[S, B] =
    State[S, B](s => More(() => {
      val (a, s1) = runS(s).runT
      f(a).runS(s1)
    }))

  def map[B](f: A => B): State[S, B] =
          //ff: a: A => State[S, B], need create a State[S, B]
                            //State[S, B] = State(s => Trampoline[(B, S)]
                                //so, Done((B, S)) == Done((f(a),s))
    flatMap(a => State[S, B](s => Done((f(a), s))))

}
