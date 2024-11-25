package com.ing_software.tp.serviceTests;

import com.ing_software.tp.dto.OrderCreateResponse;
import com.ing_software.tp.dto.OrderRequest;
import com.ing_software.tp.dto.OrderResponse;
import com.ing_software.tp.dto.ProductRequest;
import com.ing_software.tp.exceptions.RulesNotSatisfiedException;
import com.ing_software.tp.model.*;
import com.ing_software.tp.model.rules.MaxAttributeCount;
import com.ing_software.tp.repository.OrderProductRepository;
import com.ing_software.tp.repository.OrderRepository;
import com.ing_software.tp.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

        @Mock private ProductService productService;
        @Mock private OrderRepository orderRepository;
        @Mock private JwtService jwtService;
        @Mock private UserService userService;
        @Mock private EmailSenderService emailSenderService;
        @Mock private RuleService ruleService;
        @Mock private OrderProductRepository orderProductRepository;
        @Mock private Clock clock;

        @InjectMocks
        private OrderServiceImpl orderService;

        private final Clock fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());

        @BeforeEach
        void setUp() {
            orderService = new OrderServiceImpl(
                    productService,
                    orderRepository,
                    jwtService,
                    userService,
                    emailSenderService,
                    ruleService,
                    orderProductRepository,
                    fixedClock
            );
        }

    @Test
    void testCreateOrder_ValidOrder_ShouldSaveOrder() {
        String authorizationHeader = "Bearer token";
        String username = "testUser";
        User user = new User();
        user.setUsername(username);

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setProducts(List.of(new ProductRequest(1L, "Product1", 2)));

        Product product = new Product();
        product.setId(1L);
        product.setAttributes(Map.of("color", "red"));

        when(jwtService.validateAuthorization(authorizationHeader)).thenReturn(username);
        when(userService.findByUsername(username)).thenReturn(user);
        when(productService.validateStock(1L, 2)).thenReturn(true);
        when(productService.findProductById(1L)).thenReturn(Optional.of(product));
        when(orderProductRepository.save(any(OrderProduct.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());
        when(ruleService.getAllRules()).thenReturn(Collections.emptyList());

        OrderCreateResponse response = orderService.createOrder(orderRequest, authorizationHeader);

        assertNotNull(response);
        assertEquals(username, response.getUsername());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void testCreateOrder_InvalidOrder_NotEnoughStock_ShouldThrowException() {
        String authorizationHeader = "Bearer token";
        String username = "testUser";
        User user = new User();
        user.setUsername(username);

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setProducts(List.of(new ProductRequest(1L, "Product1", 2)));

        Product product = new Product();
        product.setId(1L);
        product.setAttributes(Map.of("color", "red"));

        when(jwtService.validateAuthorization(authorizationHeader)).thenReturn(username);
        when(userService.findByUsername(username)).thenReturn(user);
        when(productService.validateStock(1L, 2)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> orderService.createOrder(orderRequest, authorizationHeader));
    }

    @Test
    void testCreateOrder_InvalidOrder_DoesntSatisfyRules_ShouldThrowException() {
        String authorizationHeader = "Bearer token";
        String username = "testUser";
        User user = new User();
        user.setUsername(username);

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setProducts(List.of(new ProductRequest(1L, "Product1", 2)));

        Product product = new Product();
        product.setId(1L);
        product.setAttributes(Map.of("color", "red"));

        when(jwtService.validateAuthorization(authorizationHeader)).thenReturn(username);
        when(userService.findByUsername(username)).thenReturn(user);
        when(productService.validateStock(1L, 2)).thenReturn(true);
        when(productService.findProductById(product.getId())).thenReturn(Optional.of(product));
        when(ruleService.getAllRules()).thenReturn(List.of(new MaxAttributeCount("color","red", "0")));
        when(orderProductRepository.save(any(OrderProduct.class))).thenReturn(new OrderProduct(null, product.getId(),
                product.getProduct_name(), 2, product.getAttributes()));

        assertThrows(RulesNotSatisfiedException.class, () -> orderService.createOrder(orderRequest, authorizationHeader));
    }

    @Test
    public void testCancelOrderWithin24Hours() {
        Order order = new Order();
        order.setCreatedAt(LocalDateTime.now(fixedClock));
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        orderService.cancelOrder(1L);

        verify(productService).restoreStock(order.getProducts());
        verify(orderRepository).delete(order);
    }

    @Test
    public void testCancelOrder_OrderDoesntExist_ThrowsException() {
        when(orderRepository.findById(1L)).thenReturn(Optional.ofNullable(null));
        assertThrows(RuntimeException.class, () -> orderService.cancelOrder(1L));
    }

    @Test
    public void testCancelOrderAfter24Hours() {
        Clock clockAfter24Hours = Clock.offset(fixedClock, Duration.ofHours(25));
        orderService = new OrderServiceImpl(
                productService, orderRepository,
                jwtService, userService, emailSenderService, ruleService, orderProductRepository,
                clockAfter24Hours
        );

        Order order = new Order();
        order.setCreatedAt(LocalDateTime.now(fixedClock));
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(RuntimeException.class, () -> orderService.cancelOrder(1L),
                "Time to cancel the order expired");
    }

    @Test
    public void testConfirmOrder_ConfirmValidOrder() {
        String username = "testUser";
        User user = new User();
        user.setUsername(username);
        user.setEmail("user@user.com");

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setProducts(List.of(new ProductRequest(1L, "Product1", 2)));

        Product product = new Product();
        product.setId(1L);
        product.setAttributes(Map.of("color", "red"));


        List<OrderProduct> products = List.of(new OrderProduct(null, product.getId(),
                product.getProduct_name(), 2, product.getAttributes()));
        Order order = new Order(null, user, products,
                OrderStatus.CREATED, LocalDateTime.now());

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(emailSenderService.buildOrderConfirmationEmail(order)).thenReturn(anyString());

        orderService.confirmOrder(1L);

        verify(productService).updateStock(products);
        verify(orderRepository).save(order);
        verify(emailSenderService).sendConfirmationEmail(anyString(), anyString(), anyString());
    }

    @Test
    public void testConfirmOrder_ConfirmNonExistentOrder_ThrowsException() {
        when(orderRepository.findById(1L)).thenReturn(Optional.ofNullable(null));
        assertThrows(RuntimeException.class, () -> orderService.confirmOrder(1L));
    }

    @Test
    public void testGetOrders_GetAllWithNoOrdersCreated_ThrowsException() {
            when(orderRepository.findAll()).thenReturn(List.of());
            assertThrows(Exception.class, () -> orderService.getAllOrders());
    }

    @Test
    public void testGetAllOrder_ReturnsAllOrders() throws Exception {
        User user = new User();
        user.setUsername("user");

        Order order = new Order();
        order.setOwner(user);

        when(orderRepository.findAll()).thenReturn(List.of(order));

        List<OrderResponse> responses = orderService.getAllOrders();
        assertEquals(responses.size(), 1);
    }

    @Test
    public void testChangeOrderStatus_ChangeStatusToValidOrder() {
            Order order = new Order();
            order.setId(1L);
            order.setStatus(OrderStatus.CREATED);
            when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

            orderService.changeOrderStatus(order.getId(), OrderStatus.CONFIRMED);
            verify(orderRepository).save(order);
    }

    @Test
    public void testChangeOrderStatus_ChangeStatusToNonExistentOrder_ThrowsException() {
        assertThrows(RuntimeException.class, () -> orderService.changeOrderStatus(any(), any()));
    }

    @Test
    public void testGetUserOrder_WithNoOrders_ThrowsException() {
            assertThrows(Exception.class, () -> orderService.getUserOrders(anyString()));
    }

    @Test
    public void testGetUserOrder_canGetOwnOrders() throws Exception {
            String authorizationHeader = "Bearer token";
            String username = "testUser";
            User user = new User();
            user.setUsername(username);

            Order order = new Order();
            order.setOwner(user);

            when(jwtService.validateAuthorization(authorizationHeader)).thenReturn(username);
            when(userService.findByUsername(username)).thenReturn(user);
            when(orderRepository.findByOwner(user)).thenReturn(List.of(order));

            List<OrderResponse> responses = orderService.getUserOrders(authorizationHeader);
            assertEquals(responses.size(), 1);
    }
}
