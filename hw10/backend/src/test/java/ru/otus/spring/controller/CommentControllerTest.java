package ru.otus.spring.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.dto.CommentDto;
import ru.otus.spring.dto.create.CommentCreateDto;
import ru.otus.spring.dto.update.CommentUpdateDto;
import ru.otus.spring.model.Comment;
import ru.otus.spring.service.CommentService;

import java.util.Collections;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.otus.spring.utils.Utils.getDbBooks;
import static ru.otus.spring.utils.Utils.getDbComments;
import static ru.otus.spring.utils.Utils.getStringJsonByFilePath;

@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Test
    void getAllByBookIdTest() throws Exception {
        Comment comment = getDbComments().get(0);
        given(commentService.findAllByBookId(anyLong())).willReturn(Collections.singletonList(CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .bookId(comment.getBook().getId())
                .build()));
        this.mockMvc
                .perform(get("/api/library/comments").param(
                        "bookId", getDbBooks().get(0).getId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(getStringJsonByFilePath(
                        "src/test/resources/json/comment/commentsByBookId.json")));
    }

    @Test
    void editTest() throws Exception {
        Comment comment = getDbComments().get(0);
        when(commentService.update(any())).thenReturn(CommentUpdateDto.builder()
                .id(comment.getId())
                .text("Comment_4")
                .bookId(comment.getBook().getId())
                .build());
        String json = getStringJsonByFilePath("src/test/resources/json/comment/comment_for_update.json");
        this.mockMvc
                .perform(put("/api/library/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }

    @Test
    void addTest() throws Exception {
        when(commentService.create(any())).thenReturn(CommentCreateDto.builder()
                .id(4L)
                .text("Comment_4")
                .bookId(1L)
                .build());
        this.mockMvc
                .perform(post("/api/library/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getStringJsonByFilePath("src/test/resources/json/comment/comment_for_creation.json")))
                .andExpect(status().isOk())
                .andExpect(content().json(getStringJsonByFilePath(
                        "src/test/resources/json/comment/created_comment.json")));
    }

    @Test
    void deleteTest() throws Exception {
        this.mockMvc
                .perform(delete("/api/library/comment?id=" + getDbComments().get(0).getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Comment with id 1 deleted")));
    }

    @Test
    void noIdEditTest() throws Exception {
        this.mockMvc
                .perform(put("/api/library/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getStringJsonByFilePath(
                                "src/test/resources/json/comment/validation/no_id_comment_for_update.json")))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"id\":\"Comment ID value should not be null\"}"));
    }

    @Test
    void noTextEditTest() throws Exception {
        this.mockMvc
                .perform(put("/api/library/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getStringJsonByFilePath(
                                "src/test/resources/json/comment/validation/no_text_comment_for_update.json")))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"text\":\"Comment text value should not be blank\"}"));
    }

    @Test
    void noBookIdEditTest() throws Exception {
        this.mockMvc
                .perform(put("/api/library/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getStringJsonByFilePath(
                                "src/test/resources/json/comment/validation/no_book_id_comment_for_update.json")))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"bookId\":\"Book ID value should not be null\"}"));
    }
}
