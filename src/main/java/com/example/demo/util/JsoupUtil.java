package com.example.demo.util;

import com.example.demo.pojo.Article;
import com.example.demo.pojo.EfetchParam;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class JsoupUtil {

    public static EfetchParam parseForEfetchParam(String html){
        Document document = Jsoup.parse(html);
        String webEnv = document.select("WebEnv").first().text();
        String key = document.select("QueryKey").first().text();
        int total = Integer.parseInt(document.select("Count").first().text());
        return EfetchParam.builder().key(key).total(total).webEnv(webEnv).build();
    }

    public static List<Article> parseForArticles(String html){
        List<Article> list = new ArrayList<>();

        Document document = Jsoup.parse(html);
        Elements articles = document.select("PubmedArticle");
        for(Element article:articles){
            String pmid = null;
            String articleTitle = null;
            String abstractText = null;
            try {
                pmid = article.select("PMID").first().text();
                articleTitle = article.select("ArticleTitle").first().text();
                abstractText = article.select("AbstractText").first().text();

            } catch (Exception e) {
            }
            if(pmid!=null)
            list.add(Article.builder().articleTitle(articleTitle).pmid(pmid).abstractText(abstractText).build());
        }
        return list;
    }
}
