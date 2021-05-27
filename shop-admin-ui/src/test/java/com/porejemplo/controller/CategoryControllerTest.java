package com.porejemplo.controller;

import com.porejemplo.persist.model.Category;
import com.porejemplo.persist.repo.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
public class CategoryControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    public void init() {
        categoryRepository.deleteAllInBatch();
    }

    @WithMockUser(value = "admin", password = "admin1", roles = {"ADMIN"})
    @Test
    public void testCategoryUpdate() throws Exception {
        Category initialCategory = new Category("Initial category name");
        categoryRepository.save(initialCategory);

        Optional<Category> opt = categoryRepository.findOne(Example.of(new Category("Initial category name")));
        assertTrue(opt.isPresent());
        assertEquals("Initial category name", opt.get().getName());

        mvc.perform(post("/category/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", opt.get().getId().toString())
                .param("name", "Updated category name")
                .with(csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/category"));

        Optional<Category> optUpdated = categoryRepository.findOne(Example.of(new Category("Updated category name")));
        assertTrue(optUpdated.isPresent());
        assertEquals(opt.get().getId(), optUpdated.get().getId());
        assertEquals("Updated category name", optUpdated.get().getName());
    }


}
