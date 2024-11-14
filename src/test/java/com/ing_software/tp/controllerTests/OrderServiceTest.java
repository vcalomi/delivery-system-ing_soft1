package com.ing_software.tp.controllerTests;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.time.*;
import java.util.Optional;

import com.ing_software.tp.model.Order;
import com.ing_software.tp.repository.OrderProductRepository;
import com.ing_software.tp.repository.OrderRepository;
import com.ing_software.tp.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductService productService;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserService userService;

    @Mock
    private EmailSenderService emailSenderService;

    @Mock
    private RuleService ruleService;

    @Mock
    private OrderProductRepository orderProductRepository;

    private Clock fixedClock;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        fixedClock = Clock.fixed(Instant.parse("2024-11-12T20:56:13.124653Z"), ZoneId.of("UTC"));
        orderService = new OrderServiceImpl(
                productService, orderRepository,
                jwtService, userService, emailSenderService, ruleService, orderProductRepository,
                fixedClock
        );
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
}
