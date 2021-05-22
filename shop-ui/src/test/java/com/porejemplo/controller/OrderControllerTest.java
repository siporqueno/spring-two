package com.porejemplo.controller;

import com.porejemplo.controller.repr.ProductRepr;
import com.porejemplo.persist.model.Role;
import com.porejemplo.persist.model.User;
import com.porejemplo.persist.repo.RoleRepository;
import com.porejemplo.persist.repo.UserRepository;
import com.porejemplo.service.CartService;
import com.porejemplo.service.model.LineItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
public class OrderControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    
    @WithMockUser(value = "guest", password = "guest")
    @Test
    public void testCreateOrder() throws Exception {
        ProductRepr expectedProduct = new ProductRepr();
        expectedProduct.setId(1L);
        expectedProduct.setPrice(new BigDecimal(123));
        expectedProduct.setTitle("Product title");

        cartService.addProductQty(expectedProduct, "color", "material", "M", 1);

        List<LineItem> lineItems = cartService.getLineItems();
        assertNotNull(lineItems);
        assertEquals(1, lineItems.size());

        Role expectedRole = new Role("GUEST");
        roleRepository.save(expectedRole);
        Set<Role> expectedRoles = new HashSet<>();
        expectedRoles.add(expectedRole);
        User expectedUser = new User(1L, "guest", "guest", expectedRoles);

        userRepository.save(expectedUser);
        User user = userRepository.findAll().get(0);

        assertNotNull(user);
        assertEquals(expectedUser.getLogin(), user.getLogin());
        assertEquals(expectedUser.getPassword(), user.getPassword());

        mvc.perform(get("/order/create"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/order"));
    }
}
