package com.example.recsys;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TitleRepository extends CrudRepository<Title, Long> {

	List<Title> findAllByIdIn(List<Long> idList);
}