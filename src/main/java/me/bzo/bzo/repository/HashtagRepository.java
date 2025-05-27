package me.bzo.bzo.repository;

import me.bzo.bzo.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
   Optional<Hashtag> findByName(String name); // 해시태그 검색
}
