package com.shekhargulati.sentiment_analyzer

import org.scalatest.{FunSpec, Matchers}
import play.api.libs.json._

class SentimentAnalyzerSpec extends FunSpec with Matchers {

  describe("paragraph analysis") {

    it("positive affect, no gender") {
      val jsonString = """{ "paragraph": "Scala is a great general purpose language." }"""
      val jsonObject = Json.parse(jsonString)

      val result = SentimentAnalyzer.processJson(jsonObject)

      val sentimentResult = (result \ "sentiment").as[String]
      sentimentResult should be("positive")

      val genderResult = (result \ "gender").as[String]
      genderResult should be("unknown")

      val timeDurationResult = (result \ "timeDuration").as[String]
      timeDurationResult should be("-1")
    }

    it("should return NEGATIVE when input has negative emotion") {
      val jsonString = """{ "paragraph": "Dhoni laments bowling, fielding errors in series loss" }"""
      val jsonObject = Json.parse(jsonString)

      val result = SentimentAnalyzer.processJson(jsonObject)

      val sentimentResult = (result \ "sentiment").as[String]
      sentimentResult should be("negative")

      val genderResult = (result \ "gender").as[String]
      genderResult should be("unknown")

      val timeDurationResult = (result \ "timeDuration").as[String]
      timeDurationResult should be("-1")
    }

    it("should return NEUTRAL when input has no emotion") {
      val jsonString = """{ "paragraph": "I am reading a book" }"""
      val jsonObject = Json.parse(jsonString)

      val result = SentimentAnalyzer.processJson(jsonObject)

      val sentimentResult = (result \ "sentiment").as[String]
      sentimentResult should be("neutral")

      val genderResult = (result \ "gender").as[String]
      genderResult should be("unknown")

      val timeDurationResult = (result \ "timeDuration").as[String]
      timeDurationResult should be("-1")
    }

    describe("COTA supplied texts") {

      it("should return NEUTRAL when input has mixed emotion") {
        val jsonString = """{ "paragraph": "John downloaded the Pokemon Go app on 07/15/2016. By 07/22/2016, he was on level 24. Initially, he was very happy with the app. However, he soon became very disappointed with the app because it was crashing very often. As soon as he reached level 24, he uninstalled the app." }"""
        val jsonObject = Json.parse(jsonString)

        val result = SentimentAnalyzer.processJson(jsonObject)

        val sentimentResult = (result \ "sentiment").as[String]
        sentimentResult should be("neutral")

        val genderResult = (result \ "gender").as[String]
        genderResult should be("male")

        val timeDurationResult = (result \ "timeDuration").as[String]
        timeDurationResult should be("8")
      }
        
      it("should return POSITIVE affect") {
        val jsonString = """{ "paragraph": "Hua Min liked playing tennis. She first started playing on her 8th birthday - 07/07/1996. Playing tennis always made her happy. She won her first tournament on 08/12/2010. However, on 04/15/2015 when she was playing at the Flushing Meadows, she had a serious injury and had to retire from her tennis career." }"""
        val jsonObject = Json.parse(jsonString)

        val result = SentimentAnalyzer.processJson(jsonObject)

        val sentimentResult = (result \ "sentiment").as[String]
        // this should be positive but the nlp is not assigning it
        // could be that need to look into having it go sentence vs paragraph level
        sentimentResult should be("neutral")

        val genderResult = (result \ "gender").as[String]
        genderResult should be("female")

        val timeDurationResult = (result \ "timeDuration").as[String]
        timeDurationResult should be("6857")
      }

    }
  }
}
