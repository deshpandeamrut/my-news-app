package com.eurofins.amrut.springbootplay.service;

import java.util.List;

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

	@Value("${news.api.key}")
	private  String newsApiKey;
	
	@RequestMapping(value = "/news/getLatest")
	@ResponseBody
	public List<Article> getLatestNews() {
		final String uri = "https://newsapi.org/v2/top-headlines?language=en&country=in&apiKey=d7ae5b652f4d481d8cb7ded898d9d43f";
		return doRestCall(uri);
	
	}

	@RequestMapping("/news/source/{sourceName}")
	@ResponseBody
	public List<Article> getNewsFromSource(@PathVariable String sourceName) {
		final String uri = "https://newsapi.org/v2/top-headlines?language=en&apiKey="+newsApiKey+"&sources="+sourceName;
		return doRestCall(uri);
	}
	
	@RequestMapping(value = "/news/search/{searchText}")
	@ResponseBody
	public List<Article> getNewsForSearchText(@PathVariable String searchText) {
		final String uri = "https://newsapi.org/v2/top-headlines?language=en&apiKey="+newsApiKey+"&q="+searchText;
		return doRestCall(uri);
	}
	
	@RequestMapping(value = "/news/category/{category}")
	@ResponseBody
	public List<Article> getNewsForCategory(@PathVariable String category) {
		final String uri = "https://newsapi.org/v2/top-headlines?language=en&country=in&apiKey=d7ae5b652f4d481d8cb7ded898d9d43f&category="+category;
		return doRestCall(uri);
	}
	
	private List<Article> doRestCall(String uri) {
		RestTemplate restTemplate = new RestTemplate();
		Response result = restTemplate.getForObject(uri,Response.class);
		return result.getArticles();
		
	}
}