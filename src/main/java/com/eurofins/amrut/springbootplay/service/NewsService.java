package com.eurofins.amrut.springbootplay.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.eurofins.amrut.springbootplay.entity.Article;
import com.eurofins.amrut.springbootplay.entity.Response;

@RestController
public class NewsService {
	static List<Article> articleList = null;
	static List<Article> techArticleList = null;
	static Map<String,List<Article>> cache= new HashMap<>();
	@Value("${news.api.key}")
	private  String newsApiKey;
	
	@RequestMapping(value = "/news/getLatest")
	@ResponseBody
	public List<Article> getLatestNews() {
		final String uri = "https://newsapi.org/v2/top-headlines?language=en&country=in&apiKey=d7ae5b652f4d481d8cb7ded898d9d43f";
		
		if(articleList!=null)  
			return articleList;
		else
			articleList = doRestCall(uri); 
			return articleList;
	
	}
	
	@RequestMapping("/news/source/{sourceName}")
	@ResponseBody
	public List<Article> getNewsFromSource(@PathVariable String sourceName) {
		final String uri = "https://newsapi.org/v2/top-headlines?language=en&apiKey="+newsApiKey+"&sources="+sourceName;
		return doRestCall(uri);
	}
	
	@RequestMapping(value = "/news/search/{searchText}")
	@ResponseBody
	public List<Article> getLatestNewsForSearchText(@PathVariable String searchText) {
		final String uri = "https://newsapi.org/v2/top-headlines?language=en&apiKey="+newsApiKey+"&q="+searchText;
		return doRestCall(uri);
	}
	
	@RequestMapping(value = "/news/all/search/{searchText}")
	@ResponseBody
	public List<Article> getAllNewsForSearchText(@PathVariable String searchText) {
		final String uri = "https://newsapi.org/v2/everything?language=en&apiKey="+newsApiKey+"&q="+searchText;
		return doRestCall(uri);
	}
	
	@RequestMapping(value = "/news/category/{category}")
	@ResponseBody
	public List<Article> getNewsForCategory(@PathVariable String category) {
		final String uri = "https://newsapi.org/v2/top-headlines?language=en&country=in&apiKey=d7ae5b652f4d481d8cb7ded898d9d43f&category="+category;
		if(cache.get(category)==null) {
			List<Article> articles = doRestCall(uri);
			if(articles.isEmpty()) {
				articles = getLatestNewsForSearchText(category);
			}
			if(articles.isEmpty()) {
				articles = getAllNewsForSearchText(category);
			}
			cache.put(category, articles);
		}
		return cache.get(category);
	}
	
	private List<Article> doRestCall(String uri) {
		System.out.println("Doing rest call...");
		RestTemplate restTemplate = new RestTemplate();
		Response result = restTemplate.getForObject(uri,Response.class);
		System.out.println(result);
		return result.getArticles();
		
	}
}