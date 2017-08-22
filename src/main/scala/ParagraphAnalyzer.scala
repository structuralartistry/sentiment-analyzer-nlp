package com.shekhargulati.sentiment_analyzer

import java.util.Properties
import com.shekhargulati.sentiment_analyzer.Sentiment.Sentiment
import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations
import edu.stanford.nlp.pipeline.{Annotation, StanfordCoreNLP}
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations
import scala.collection.convert.wrapAll._
import play.api.libs.json._
import scala.collection.mutable.ListBuffer

object SentimentAnalyzer {

  val props = new Properties()
  props.setProperty("annotators", "tokenize, ssplit, parse, sentiment")
  val pipeline: StanfordCoreNLP = new StanfordCoreNLP(props)

  def processJson(input: play.api.libs.json.JsValue): play.api.libs.json.JsObject = {
    val paragraph = (input \ "paragraph").as[String]

    val sentiment = mainSentiment(paragraph)
    val sentimentString = sentiment match {
      case Sentiment.NEGATIVE => "negative"
      case Sentiment.POSITIVE => "positive"
      case Sentiment.NEUTRAL => "neutral"
    }

    var timeDuration = extractTimeDuration(paragraph)

    val gender = extractGender(paragraph)

    Json.obj("sentiment" -> sentimentString, "timeDuration" -> timeDuration, "gender" -> gender)
  }

  def mainSentiment(input: String): Sentiment = Option(input) match {
    case Some(text) if !text.isEmpty => extractSentiment(text)
    case _ => throw new IllegalArgumentException("input can't be null or empty")
  }

  def sentiment(input: String): List[(String, Sentiment)] = Option(input) match {
    case Some(text) if !text.isEmpty => extractSentiments(text)
    case _ => throw new IllegalArgumentException("input can't be null or empty")
  }

  private def extractSentiment(text: String): Sentiment = {
    val (_, sentiment) = extractSentiments(text)
      .maxBy { case (sentence, _) => sentence.length }
    sentiment
  }

  def extractSentiments(text: String): List[(String, Sentiment)] = {
    val annotation: Annotation = pipeline.process(text)
    val sentences = annotation.get(classOf[CoreAnnotations.SentencesAnnotation])
    sentences
      .map(sentence => (sentence, sentence.get(classOf[SentimentCoreAnnotations.SentimentAnnotatedTree])))
      .map { case (sentence, tree) => (sentence.toString, Sentiment.toSentiment(RNNCoreAnnotations.getPredictedClass(tree))) }
      .toList
  }

  // this is very loose and will not account for both genders appearing in a paragraph
  // it only deals with the first instance
  def extractGender(text: String): String = {
    var gender = "unknown"

    // this is not DRY... would refactor later

    val male_pattern = "\\bhe\\b".r
    val male_result = male_pattern.findFirstIn(text) 
    male_result match {
      case Some(s) => gender = "male"
      case None =>
    }

    val female_pattern = "\\bshe\\b".r
    val female_result = female_pattern.findFirstIn(text) 
    female_result match {
      case Some(s) => gender = "female"
      case None =>
    }
  
    gender
  }

  def extractTimeDuration(text: String): String = {
    var calculatedDuration : Long = -1

    val pattern = "\\d{2}/\\d{2}/\\d{4}".r
    val result = pattern.findAllIn(text) 
    val dates = new ListBuffer[Long]()
    val format = new java.text.SimpleDateFormat("MM/dd/yyyy")
    result foreach { date =>
        dates += format.parse(date).getTime()
    }
    if(dates.length>=2) {
      val orderedDates = dates.sorted
      val day_in_ms = 1000 * 60 * 60 * 24;
      calculatedDuration = ((orderedDates.last-orderedDates.head)/day_in_ms) + 1
    }

    calculatedDuration.toString
  }

}

object Sentiment extends Enumeration {
  type Sentiment = Value
  val POSITIVE, NEGATIVE, NEUTRAL = Value

  def toSentiment(sentiment: Int): Sentiment = sentiment match {
    case x if x == 0 || x == 1 => Sentiment.NEGATIVE
    case 2 => Sentiment.NEUTRAL
    case x if x == 3 || x == 4 => Sentiment.POSITIVE
  }
}
