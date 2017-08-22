/**
Properties props = new Properties();
props.setProperty("annotators", "tokenize,ssplit,pos,parse,gender");

StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

Annotation document = new Annotation("Annie goes to school");

pipeline.annotate(document);

for (CoreMap sentence : document.get(CoreAnnotations.SentencesAnnotation.class)) {
  for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
    System.out.print(token.value());
    System.out.print(", Gender: ");
    System.out.println(token.get(MachineReadingAnnotations.GenderAnnotation.class));
  }
}
*/
