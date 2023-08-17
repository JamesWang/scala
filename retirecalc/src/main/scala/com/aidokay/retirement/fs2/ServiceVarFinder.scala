package com.aidokay.retirement.fs2

import org.yaml.snakeyaml.Yaml

import java.io.{File, FileInputStream}
import scala.util.Using
import scala.jdk.CollectionConverters.*

object ServiceVarFinder {

  type SMap = Map[String, Any]
  type JMap = java.util.Map[String, Any]

  case class ServiceYamlLoader(filename: String) {
    private val loadedMap: Map[String, Set[String]] = groupByService()

    private def loadYmlAsMap(): SMap =
      Using.resource(new FileInputStream(new File(filename))){ rs =>
        new Yaml().load(rs).asInstanceOf[JMap].asScala
      }.toMap

    def groupByService(): Map[String, Set[String]] =
      loadYmlAsMap()("microservices").asInstanceOf[JMap].asScala.toMap
        .map{ kv =>
          kv._1 -> {
            val v = kv._2.asInstanceOf[JMap].asScala.toMap
            if (v.contains("configMaps")) {
              v("configMaps")
                .asInstanceOf[JMap].asScala("tm-config")
                .asInstanceOf[JMap].asScala
                .keySet.toSet
            } else Set()
          }
        }
    def gertConfig(service: String): Option[Set[String]] = loadedMap.get(service)
    def getAll: Map[String, Set[String]] = loadedMap
  }

}
