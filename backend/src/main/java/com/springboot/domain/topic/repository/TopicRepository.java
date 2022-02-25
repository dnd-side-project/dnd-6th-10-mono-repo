package com.springboot.domain.topic.repository;

import com.springboot.domain.posts.model.entity.Posts;
import com.springboot.domain.reply.model.entity.Reply;
import com.springboot.domain.topic.model.dto.TopicDto;
import com.springboot.domain.topic.model.entity.Topic;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.HashMap;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TopicRepository extends JpaRepository<Topic, Long> {

    List<Topic> findAllByOrderByIdDesc();

    List<Topic> findByNameContaining(String keyword);

    @Query(value = "SELECT p.id FROM Posts p INNER JOIN category c ON p.id = c.posts_id and c.topic_id = :topicId order by p.id desc limit 4", nativeQuery = true)
//    @Query(value="SELECT p FROM Posts p INNER JOIN category c ON p.id = c.posts_id and c.topic_id = :topicId order by p.id desc limit 2",nativeQuery = true)
//    @Query(value="SELECT * FROM Posts p INNER JOIN category c ON p.id = c.posts_id and c.topic_id = :topicId order by p.id desc limit 2")
//    @Query(value="SELECT p FROM Posts p INNER JOIN category c ON p.id = c.posts_id and c.topic_id = :topicId order by p.id desc limit 2")
//    List<Posts> getPostsByTopicOrderByPostsIdLimit4(@Param("topicId") Long topicId);
//    List<Posts> findTop4ByTopicIdPostsByTopicOrderByPostsId(@Param("topicId") Long topicId);
//    List<Object> getPostsByTopicOrderByPostsIdLimit4(@Param("topicId") Long topicId);
//    List<HashMap<String,Object>> getPostsByTopicOrderByPostsIdLimit4(@Param("topicId") Long topicId);
    List<Long> getPostsIdByTopicOrderByPostsIdLimit4(@Param("topicId") Long topicId);
//    Object getPostsByTopicOrderByPostsIdLimit4(@Param("id") Long id);

    @Query(value = "SELECT * FROM Topic t INNER JOIN category c ON t.id = c.topic_id and c.posts_id = :postsId", nativeQuery = true)
    List<Topic> getTopicByPostsId(@Param("postsId") Long postsId);
}
