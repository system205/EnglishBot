package com.system205.englishbot.repositories;

import com.system205.englishbot.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {
    Set<Word> findByUser_Id(@NonNull Long id);

    long countByUser_Id(Long id);
}
