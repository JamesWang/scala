### Algo in Scala

FindMaxDiff from a List with constraint:
- Bigger number should have bigger index in the List

iterate from the second last element of the List, with the last one as the initial diff, keeps track
of the max number, whenever find a number before it is bigger, then make it as the max, and no other
changes, keep iteration, if the number is not bigger thant max, then calculate the new diff using max
and the number, compare the new diff with prev-diff, keep the bigger one, and keep iteration with the
remaining numbers in the List

### Setup Bloop

- Follow steps here:
  https://scalacenter.github.io/bloop/setup#sbt
  - add this line(version could be diff) to ~/project/plugins.sbt ( if not exist, create plugins.sbt) 

            addSbtPlugin("ch.epfl.scala" % "sbt-bloop" % "1.5.4")

    - run
      
            sbt bloopInstall
            
            ths command will create two files:
              -  .bloop/root.json
              -  .bloop/root-test.json

    - run 
  
            bloop projects

            it will show root and root-test
  
After this setup,compile will be much faster
