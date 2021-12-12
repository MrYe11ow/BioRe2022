package com.example.demo;

import com.example.demo.mapper.ArticleMapper;
import com.example.demo.pojo.Article;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	private ArticleMapper articleMapper;

	@Test
	void contextLoads() {
		List<Article> list = new ArrayList<>();
		Article article = new Article();
		article.setPmid("1");
		article.setAbstractText("asf");
		article.setArticleTitle("dsaf");
		Article article2 = new Article();
		article2.setPmid("2");
		article2.setAbstractText("asf");
		article2.setArticleTitle("dsaf");
		list.add(article);
		list.add(article2);
		articleMapper.batchInsert(list);
	}

	public static void main(String[] args) {
		Double a = 0.2;
		a++;
		System.out.println(a);
	}

}
