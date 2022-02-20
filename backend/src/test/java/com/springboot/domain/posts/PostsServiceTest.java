package com.springboot.domain.posts;

import com.springboot.domain.posts.model.dto.PageRequestDto;
import com.springboot.domain.posts.model.dto.PageResultDto;
import com.springboot.domain.posts.model.dto.PostsDto;
import com.springboot.domain.posts.model.dto.PostsListResponseDto;
import com.springboot.domain.posts.model.dto.PostsSaveRequestDto;
import com.springboot.domain.posts.model.entity.Posts;
import com.springboot.domain.posts.service.PostsService;
import javax.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PostsServiceTest {

    @Autowired
    private PostsService service;

    // 0219 변경 예정. author -> member
    @DisplayName("[Service] Posts 등록 테스트")
    @Test
    public void testRegister() {

        PostsDto dto = PostsDto.builder()
            .ref("Save Test ref.")
            .content("Save Test content...")
            .authorId(303L) //현재 데이터베이스에 존재하는 회원 정보
//            .authorEmail("Test author email")
//            .authorNickname("Test author Nickname")
            .build();

        Long id = service.save(dto);

    }
//
//    // 목록 조회 테스트
//    @Test
//    public void testList() {
//
////        PageRequestDto pageRequestDTO = PageRequestDto.builder()
////            .page(1)
////            .size(10)
////            .build();
//
//        PageRequestDto pageRequestDTO = new PageRequestDto();
//
//        PageResultDto<PostsListResponseDto, Posts> resultDTO = service.getList(pageRequestDTO);
//
//        System.out.println("PREV: " + resultDTO.isPrev());
//        System.out.println("NEXT: " + resultDTO.isNext());
//        System.out.println("TOTAL: " + resultDTO.getTotalPage());
//
//        System.out.println("-------------------------------------");
//        for (PostsListResponseDto postsListResponseDto : resultDTO.getDtoList()) {
//            System.out.println(postsListResponseDto);
//        }
//
//        System.out.println("========================================");
//        resultDTO.getPageList().forEach(i -> System.out.println(i));
//    }

    @DisplayName("[Service] 게시물 목록 조회 ( 게시물 + 작성자 ) 및 페이징 처리")
    @Test
    public void testList() {

        //1페이지 10개
        PageRequestDto pageRequestDTO = new PageRequestDto();

        PageResultDto<PostsDto, Object[]> result = service.getList(pageRequestDTO);

        for (PostsDto postsDTO : result.getDtoList()) {
            System.out.println(postsDTO);
        }

    }

//
//    // 조건부 목록 조회 테스트
//    @Test
//    public void testSearch() {
//
//        PageRequestDto pageRequestDTO = PageRequestDto.builder()
//            .page(1)
//            .size(10)
//            .type("c")   //검색 조건 t : topic, c : content, a : author
//            .keyword("2")  // 검색 키워드
//            .build();
//
//        PageResultDto<PostsListResponseDto, Posts> resultDTO = service.getList(pageRequestDTO);
//
//        System.out.println("PREV: " + resultDTO.isPrev());
//        System.out.println("NEXT: " + resultDTO.isNext());
//        System.out.println("TOTAL: " + resultDTO.getTotalPage());
//
//        System.out.println("-------------------------------------");
//        for (PostsListResponseDto postsListResponseDto : resultDTO.getDtoList()) {
//            System.out.println(postsListResponseDto);
//        }
//
//        System.out.println("========================================");
//        resultDTO.getPageList().forEach(i -> System.out.println(i));
//    }

}
