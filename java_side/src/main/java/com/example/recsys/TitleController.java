package com.example.recsys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
@AllArgsConstructor
@Controller

public class TitleController {
	@Autowired 
	private TitleRepository repository;
	private List<Long> favoritesList = new ArrayList<Long>();
	
	@GetMapping("/titles")
    public String getTitles(Model model) {
    	model.addAttribute("mode", "main");
    	model.addAttribute("titlesList", repository.findAll());
    	return "titles";
    }
	
    @GetMapping("/titles/{id}")
    public String getTitle(@PathVariable("id") long id, Model model) {
    	Title title = repository.findById(id)
    	.orElseThrow(() -> new IllegalArgumentException("Invalid title Id:" + id));
    	model.addAttribute("title", title);
    	return "title";
    }
    
    @GetMapping("/favorites")
    public String getFavorites(Model model) {
    	model.addAttribute("titlesList", repository.findAllByIdIn(favoritesList));
    	return "titles";
    }
    
    @PostMapping(value = "/save/favorites/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody void addFavorites(@PathVariable long id) {
    	
    	if(favoritesList.contains(id))
    		favoritesList.remove(id);
    	else
    		favoritesList.add(id);	
    }
    
    @GetMapping("/recommendations")
    public String getRecommendations(Model model) throws JSONException, JsonMappingException, JsonProcessingException {
    	
    	String BASE_URL = "http://127.0.0.1:5000/get_recommendations";
    	RestTemplate restTemplate = new RestTemplate();
    	JSONObject jsonObject=new JSONObject();
    	jsonObject.put("favorites", favoritesList);
    	HttpHeaders headers=new HttpHeaders();
    	headers.add("Accept", MediaType.APPLICATION_JSON.toString());
    	headers.add("Content-Type", MediaType.APPLICATION_JSON.toString());
    	            
    	HttpEntity<String> httpEntity = new HttpEntity<>(jsonObject.toString(), headers);
    	String json = restTemplate.exchange(BASE_URL, HttpMethod.POST, httpEntity, String.class).getBody();
    	ObjectMapper objectMapper = new ObjectMapper();
    	HashMap<String, List<Long>> map = objectMapper.readValue(json, new TypeReference<HashMap<String,List<Long>>>() {});
    	model.addAttribute("titlesList",repository.findAllByIdIn(map.get("recommendations")));
        return "titles";
    }
    
}
