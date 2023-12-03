package com.system205.englishbot.repositories;

import com.system205.englishbot.entity.EnglishUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnglishUserRepository extends JpaRepository<EnglishUser, Long> {

}
