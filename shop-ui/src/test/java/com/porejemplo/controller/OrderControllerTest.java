package com.porejemplo.controller;

import com.porejemplo.controller.repr.ProductRepr;
import com.porejemplo.persist.model.Order;
import com.porejemplo.persist.model.Role;
import com.porejemplo.persist.model.User;
import com.porejemplo.persist.repo.BrandRepository;
import com.porejemplo.persist.repo.CategoryRepository;
import com.porejemplo.persist.repo.RoleRepository;
import com.porejemplo.persist.repo.UserRepository;
import com.porejemplo.service.CartService;
import com.porejemplo.service.OrderService;
import com.porejemplo.service.UserService;
import com.porejemplo.service.model.LineItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.springframework.test.web.servlet.ResultActions.*;

@Import(TestConfig.class)
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

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @WithMockUser(value = "guest", password = "guest")
    @Test
    public void testCreateOrder() throws Exception {

        MockHttpSession mockHttpSession = new MockHttpSession();

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
        User user = userService.findUserByLogin(expectedUser.getLogin()).get();

        assertNotNull(user);
        assertEquals(expectedUser.getLogin(), user.getLogin());
        assertEquals(expectedUser.getPassword(), user.getPassword());
        assertEquals(1, userRepository.findAll().size());

        mvc.perform(get("/order/create"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/order"));


//        assertEquals(0, cartService.getLineItems().size());

        List<Order> orders = orderService.findAllByUserWithOrderItemsFetch(expectedUser);

        assertNotNull(orders);
        assertEquals(1, orders.size());
//        assertEquals(1, orders.get(0).getOrderItems().size());
    }
}
