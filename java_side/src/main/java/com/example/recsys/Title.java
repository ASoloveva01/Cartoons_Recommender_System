package com.example.recsys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="titles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Title {
	@Id
	@Column(name="Номер")
	@GeneratedValue
	
	private Long id;
	@Column(name="Название")
	private String name;
	@Column(name="Жанры")
	private String genres;
	@Column(name="Описание")
	private String description;
	@Column(name="Рейтинг")
	private String rating;
	@Column(name="Изображение")
	private String picture;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGenres() {
		return genres;
	}
	public void setGenres(String genres) {
		this.genres = genres;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	
}
