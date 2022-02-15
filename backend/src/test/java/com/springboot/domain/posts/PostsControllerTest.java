package com.springboot.domain.posts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.domain.auth.jwt.JwtUtil;
import com.springboot.domain.posts.controller.PostsController;
import com.springboot.domain.posts.model.entity.Posts;
import com.springboot.domain.posts.model.dto.PostsSaveRequestDto;
import com.springboot.domain.posts.repository.PostsRepository;
//import com.springboot.domain.posts.model.dto.PostsUpdateRequestDto;
import java.util.stream.IntStream;
import javax.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostsControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private JwtUtil jwtUtil;

    private MockMvc mvc;
    private String accessToken;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();

        accessToken = jwtUtil.createAuthToken("tester");
    }

    @Test
    @Transactional
    @WithMockUser(username = "tester", roles = "USER")
    public void Posts_등록된다() throws Exception {
        //given
        String content = "content";
        String ref = "reference";
        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
            .content(content)
            .author("author")
            .ref(ref)
            .build();

        String url = "http://localhost:" + port + "/api/v1/posts";

        //when
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-AUTH_TOKEN", accessToken)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
            .andExpect(status().isOk());

        //when
//        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url,requestDto,Long.class);
//
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity.getBody()).isEqualTo(1);

        //then
        List<Posts> all = postsRepository.findAll();
//        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
        assertThat(all.get(0).getRef()).isEqualTo(ref);
    }

    @Test
    @Transactional
    @WithMockUser(roles = "USER")
    public void Posts_삭제된다() throws Exception {
        //given
        Posts saved = postsRepository.save(Posts.builder()
            .content("content")
            .author("author")
            .ref("reference")
            .build());

        Long savedId = saved.getId();

        String url = "http://localhost:" + port + "/api/v1/posts/" + savedId;

        mvc.perform(delete(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-AUTH_TOKEN", accessToken))
            .andExpect(status().isOk());

    }

    // 전체 게시물 내림차순 조회 테스트
    @Test
    @WithMockUser(username = "tester", roles = "USER")
    public void Posts_모두_조회한다() throws Exception {
        //given
        int page = 1;
        int size = 10;

        String searched_page = "/page/" + String.valueOf(page);
        String url = "http://localhost:" + port + "/api/v1/posts" + searched_page;

        //when
        mvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-AUTH_TOKEN", accessToken))
            .andExpect(status().isOk())
            .andDo(print());
    }

    // 검색 내용 포함 게시물 내림차순 조회 테스트 - content
    @Test
    @WithMockUser(username = "tester", roles = "USER")
    public void Posts_content_검색한다() throws Exception {

        //given
        int page = 1;
        int size = 10;

        // content 검색
        String type = "c";
        String keyword = "2";

        String searched_type = "/type/" + type;
        String searched_keyword = "/keyword/" + keyword;
        String searched_page = "/page/" + String.valueOf(page);
        String url = "http://localhost:" + port + "/api/v1/posts"
            + searched_type
            + searched_keyword
            + searched_page;

        //when
        mvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-AUTH_TOKEN", accessToken))
            .andExpect(status().isOk())
            .andDo(print());
    }



//    @Test
//    @WithMockUser(roles="USER")
//    public void Posts_수정된다() throws Exception {
//        //given
//        Posts savedPosts = postsRepository.save(Posts.builder()
//                .ref("reference")
//                .content("content")
//                .author("author")
//                .build());
//
//        Long updateId = savedPosts.getId();
//        String expectedRef = "reference2";
//        String expectedContent = "content2";
//
//        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
//                .ref(expectedRef)
//                .content(expectedContent)
//                .build();
//
//        String url = "http://localhost:" + port + "/api/v1/posts/" + updateId;
//
//        //when
//        mvc.perform(put(url)
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .content(new ObjectMapper().writeValueAsString(requestDto)))
//                .andExpect(status().isOk());
//
//        //then
//        List<Posts> all = postsRepository.findAll();
//        assertThat(all.get(0).getRef()).isEqualTo(expectedRef);
//        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
//    }

}
