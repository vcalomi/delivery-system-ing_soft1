package com.ing_software.tp.service;

import com.ing_software.tp.dto.*;
import com.ing_software.tp.exceptions.RulesNotSatisfiedException;
import com.ing_software.tp.model.*;
import com.ing_software.tp.repository.OrderProductRepository;
import com.ing_software.tp.repository.OrderRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService{

    private final ProductService productService;
    private final OrderRepository orderRepository;
    private final JwtService jwtService;
    private final UserService userService;
    private final EmailSenderService emailSenderService;
    private final RuleService ruleService;
    private final OrderProductRepository orderProductRepository;
    private final Clock clock;

    public OrderServiceImpl(ProductService productService, OrderRepository orderRepository, JwtService jwtService, UserService userService, EmailSenderService emailSenderService, RuleService ruleService, OrderProductRepository orderProductRepository, Clock clock) {
        this.productService = productService;
        this.orderRepository = orderRepository;
        this.jwtService = jwtService;
        this.userService = userService;
        this.emailSenderService = emailSenderService;
        this.ruleService = ruleService;
        this.orderProductRepository = orderProductRepository;
        this.clock = clock;
    }



    public OrderCreateResponse createOrder(@Valid OrderRequest orderRequest, String authorizationHeader) {

        String username = jwtService.validateAuthorization(authorizationHeader);

        User user = userService.findByUsername(username);

        Map<ProductRequest, Boolean> nonValidProductsRequested = validateOrderRequestStock(orderRequest);
        if(!nonValidProductsRequested.isEmpty()) {
            throw new RuntimeException("Invalid order");
        }

        Order order = new Order();
        List<OrderProduct> products = new ArrayList<>();

        for (ProductRequest productRequest: orderRequest.getProducts()){

            Optional<Product> optionalProduct = productService.findProductById(productRequest.getId());
            Map<String, String> attributes = null;
            if (optionalProduct.isPresent()) {
                attributes = new HashMap<>(Map.copyOf(optionalProduct.get().getAttributes()));
            }

            OrderProduct product = new OrderProduct(null, optionalProduct.get().getId(), productRequest.getProduct_name(),
                    productRequest.getQuantity(), attributes);
            OrderProduct entityProduct = orderProductRepository.save(product);
            products.add(entityProduct);
        }
        order.setProducts(products);
        order.setOwner(user);
        order.setStatus(OrderStatus.CREATED);

        if (!validateRules(order)) {
            DividerOrdersResponse response = parseDividedOrder(order);
            throw new RulesNotSatisfiedException(response);
        }

        order.setCreatedAt(LocalDateTime.now());
        orderRepository.save(order);
        return new OrderCreateResponse(order.getId(), user.getUsername(), order.getProducts());
    }

    public void cancelOrder(Long orderId) {
        Optional<Order> orderToCancel = orderRepository.findById(orderId);
        if (orderToCancel.isPresent()) {
            LocalDateTime initialTime = orderToCancel.get().getCreatedAt();
            LocalDateTime finalTime = LocalDateTime.now(clock);
            long hoursBetween = ChronoUnit.HOURS.between(initialTime,finalTime);
            if (hoursBetween <= 24) {
                productService.restoreStock(orderToCancel.get().getProducts());
                orderRepository.delete(orderToCancel.get());
                return;
            }
            throw new RuntimeException("Time to cancel the order expired");
        }
        throw new RuntimeException("Order not found");
    }

    public Map<ProductRequest, Boolean> validateOrderRequestStock(OrderRequest orderRequest){
        Map<ProductRequest, Boolean> result = new HashMap<>();
        for (ProductRequest productRequest: orderRequest.getProducts()){
            if (!productService.validateStock(productRequest.getId(), productRequest.getQuantity())){
                result.put(productRequest, false);
            }
        }
        return result;
    }

    public void confirmOrder(Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isEmpty()){
            throw new RuntimeException("No order with provided id");
        }
        User user = order.get().getOwner();
        order.get().setStatus(OrderStatus.CONFIRMED);
        productService.updateStock(order.get().getProducts());
        orderRepository.save(order.get());
        emailSenderService.sendConfirmationEmail(user.getEmail(),"Confirmation Email", emailSenderService.buildOrderConfirmationEmail(order.get()));
    }

    public List<OrderResponse> getConfirmedOrders(String sortBy) throws Exception {
        List<Order> orders = (List<Order>) orderRepository.findAll();
        if (orders.isEmpty()) {
            throw new Exception("No orders found");
        }
        if (Objects.equals(sortBy, "confirmed")) {
            List<OrderResponse> confirmedOrders = new ArrayList<>();
            for (Order order: orders){
                if(order.getStatus().equals(OrderStatus.CONFIRMED)){
                    OrderResponse confirmedOrder = new OrderResponse(order.getId(),
                            order.getOwner().getUsername(), order.getOwner().getEmail(), order.getProducts(), order.getStatus());
                    confirmedOrders.add(confirmedOrder);
                }
            }
            if (confirmedOrders.isEmpty()){
                throw new Exception("No confirmed orders found");
            }
            return confirmedOrders;
        }
        List<OrderResponse> ordersResponse = new ArrayList<>();
        for (Order order: orders){
            OrderResponse confirmedOrder = new OrderResponse(order.getId(),
                        order.getOwner().getUsername(), order.getOwner().getEmail(), order.getProducts(), order.getStatus());
            ordersResponse.add(confirmedOrder);
        }
        return ordersResponse;
    }

    public List<OrderResponse> getOrders(String authorizationHeader) throws Exception {
        String username = jwtService.validateAuthorization(authorizationHeader);
        User owner = userService.findByUsername(username);
        List<Order> orders = (List<Order>) orderRepository.findByOwner(owner);
        List<OrderResponse> ordersResponse = new ArrayList<>();
        for (Order order: orders) {
            OrderResponse confirmedOrder = new OrderResponse(order.getId(),
                    order.getOwner().getUsername(), order.getOwner().getEmail(), order.getProducts(), order.getStatus());
            ordersResponse.add(confirmedOrder);
        }
        return ordersResponse;
    }

    public void changeOrderStatus(Long order_id, OrderStatus status) {
        Optional<Order> optionalOrder = orderRepository.findById(order_id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(status);
            orderRepository.save(order);
            return;
        }
        throw new RuntimeException("Order not found");
    }

    private boolean validateRules(Order order) {
        List<OrderRule> rules = ruleService.getAllRules();
        for (OrderRule rule: rules) {
            if (!rule.isSatisfiedBy(order))
                return false;
        }
        return true;
    }

    private void addProduct(Order order, OrderProduct orderProduct) {
        if (order.getProducts() == null) {
            List<OrderProduct> products = new ArrayList<>(List.of(orderProduct));
            order.setProducts(products);
            return;
        }
        List<OrderProduct> products = order.getProducts();
        products.add(orderProduct);
        order.setProducts(products);
    }

    private void removeProduct(Order order, OrderProduct orderProduct) {
        if (order.getProducts() == null) {
            return;
        }
        List<OrderProduct> products = order.getProducts();
        products.removeLast();
        order.setProducts(products);
    }

    private List<OrderProduct> divideProductForQuantity(List<OrderProduct> products) {
        List<OrderProduct> finalList = new ArrayList<>();
        for (OrderProduct product: products) {
            for (int i = 0; i < product.getQuantity(); i++) {
                OrderProduct newOrderProduct = new OrderProduct(product.getId(), product.getProduct_id(),
                        product.getProduct_name(), 1, product.getAttributes());
                finalList.add(newOrderProduct);
            }
        }
        return finalList;
    }

    private List<Order> divideOrder(Order order) {
        List<Order> validOrders = new ArrayList<>();
        List<OrderProduct> products = divideProductForQuantity(order.getProducts());


        for (OrderProduct product: products) {
            boolean added = false;

            for (Order order1: validOrders) {
                addProduct(order1, product);
                if (validateRules(order1)) {
                    added = true;
                    break;
                } else {
                    removeProduct(order1, product);
                }
            }

            if (!added) {
                Order newOrder = new Order();
                addProduct(newOrder, product);
                validOrders.add(newOrder);
            }
        }
        return validOrders;
    }

    private DividerOrdersResponse parseDividedOrder(Order order) {
        List<Order> dividedOrder = divideOrder(order);
        DividerOrdersResponse response = new DividerOrdersResponse();
        List<List<OrderProduct>> newOrderProducts = new ArrayList<>();
        for (Order order1: dividedOrder) {
            newOrderProducts.add(order1.getProducts());
        }
        response.setDividedProducts(newOrderProducts);
        return response;
    }
}
