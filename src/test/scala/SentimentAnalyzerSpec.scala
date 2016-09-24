package com.shekhargulati.sentiment_analyzer

import org.scalatest.{FunSpec, Matchers}

class SentimentAnalyzerSpec extends FunSpec with Matchers {

  describe("sentiment analyzer") {

    it("should return POSITIVE when input has positive emotion") {
      val input = "Scala is a great general purpose language."
      val sentiment = SentimentAnalyzer.mainSentiment(input)
      sentiment should be(Sentiment.POSITIVE)
    }

    it("should return NEGATIVE when input has negative emotion") {
      val input = "Dhoni laments bowling, fielding errors in series loss"
      val sentiment = SentimentAnalyzer.mainSentiment(input)
      sentiment should be(Sentiment.NEGATIVE)
    }

    it("should return NEUTRAL when input has no emotion") {
      val input = "I am reading a book"
      val sentiment = SentimentAnalyzer.mainSentiment(input)
      sentiment should be(Sentiment.NEUTRAL)
    }

    describe("COTA supplied texts") {

      it("should return NEUTRAL when input has mixed emotion") {
        val input = "John downloaded the Pokemon Go app on 07/15/2016. By 07/22/2016, he was on level 24. Initially, he was very happy with the app. However, he soon became very disappointed with the app because it was crashing very often. As soon as he reached level 24, he uninstalled the app."
        val sentiment = SentimentAnalyzer.mainSentiment(input)
        sentiment should be(Sentiment.NEUTRAL)
      }
        

/** this might be failing as it has multiple sentences and need to break it down into single sentences for sentiment */
      it("should return POSITIVE affect") {
        val input = "Hua Min liked playing tennis. She first started playing on her 8th birthday - 07/07/1996. Playing tennis always made her happy. She won her first tournament on 08/12/2010. However, on 04/15/2015 when she was playing at the Flushing Meadows, she had a serious injury and had to retire from her tennis career." 
        val sentiment = SentimentAnalyzer.extractSentiments(input)
        sentiment should be(Sentiment.POSITIVE)
      }

    }
  }
}
