## Setup Bloop

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

After this setup, IDE(Metals or Intellij) will use it to compile and will be much faster