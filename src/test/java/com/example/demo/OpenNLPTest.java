package com.example.demo;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

import java.io.FileInputStream;
import java.io.InputStream;

public class OpenNLPTest {

    public static void main(String[] args) throws Exception{
        String paragraph =
                "Despite dramatic advances in cancer research setting, breast cancer remains a major health problem and represents currently a top biomedical research priority. Worldwide, breast cancer is the most common cancer affecting women, and its incidence and mortality rates are expected to increase significantly the next years. Recently the researchers' interest has been attracted by breast cancer arising in young women. Current evidence suggests that in women aged <45 years, breast cancer is unquestionably the leading cause of cancer-related deaths. This type of cancer seems to be highly heterogeneous and has potentially aggressive and complex biological features. However, management strategies, recommendations and options are not age based and the 'complex' biology of this type of cancer remains uncertain and unexplored. In this review, we summarize the latest scientific information on breast cancer arising in young women highlighting the heterogeneity and the complex nature of this type of cancer.";
        String modelPath = "C:\\Users\\Administrator\\Downloads\\opennlp-en-ud-ewt-sentence-1.0-1.9.3.bin";
        InputStream is = new FileInputStream(modelPath);
        SentenceModel model = new SentenceModel(is);
        SentenceDetectorME detector = new SentenceDetectorME(model);
        String[] sens = detector.sentDetect(paragraph);
        for(String sen : sens){
            System.out.println(sen);
        }
    }
}
