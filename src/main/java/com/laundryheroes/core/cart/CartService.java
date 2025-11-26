package com.laundryheroes.core.cart;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.laundryheroes.core.common.ApiResponse;
import com.laundryheroes.core.common.ResponseCode;
import com.laundryheroes.core.common.ResponseFactory;
import com.laundryheroes.core.order.OrderItem;
import com.laundryheroes.core.order.OrderItemResponse;
import com.laundryheroes.core.order.OrderResponse;
import com.laundryheroes.core.servicecatalog.LaundryService;
import com.laundryheroes.core.servicecatalog.LaundryServiceRepository;
import com.laundryheroes.core.user.User;

@Service
public class CartService {

    private final CartRepository cartRepo;
    private final CartItemRepository cartItemRepo;
    private final LaundryServiceRepository serviceRepo;
    private final ResponseFactory responseFactory;

    public CartService(CartRepository cartRepo,
                       LaundryServiceRepository serviceRepo,
                       ResponseFactory responseFactory,CartItemRepository cartItemRepo) {
        this.cartRepo = cartRepo;
        this.serviceRepo = serviceRepo;
        this.responseFactory = responseFactory;
        this.cartItemRepo = cartItemRepo;
    }

    @Transactional
    public ApiResponse<?> addToCart(User user, CreateCartRequest request) {
        Cart cart =  new Cart();
        cart.setUser(user);
        cartRepo.save(cart);
       List<CartItem> cartItems = new ArrayList<>();
       double total = 0;
       for(CreateCartItemRequest item: request.getItems()){
             LaundryService service = serviceRepo.findById(item.getServiceId()).orElse(null);
            if (service == null || !service.isActive()) {
                return responseFactory.error(ResponseCode.SERVICE_NOT_FOUND);
            }

            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setLaundryService(service);
            newItem.setUnitPrice(service.getPrice());
            newItem.setQuantity(item.getQuantity());
            double subtotal = service.getPrice() * item.getQuantity();
            newItem.setSubtotal(subtotal);
            cartItemRepo.save(newItem);
            total += subtotal;
            cartItems.add(newItem);
       }
       cart.setTotalAmount(total);
       cart.setItems(cartItems);
       cartRepo.save(cart);

       

        return responseFactory.success(ResponseCode.SUCCESS, toResponse(cart));
    }


    @Transactional
    public ApiResponse<?> clearCart(User user) {

        cartRepo.deleteByUser(user);
        return responseFactory.success(ResponseCode.SUCCESS, null);
    }

    public ApiResponse<List<CartResponse>> getCart(User user) {

        List<CartResponse> list = cartRepo.findByUser(user)
        .stream()
        .map(this::toResponse)
        .toList();

        return responseFactory.success(ResponseCode.SUCCESS, list);
    }

    private CartResponse toResponse(Cart cart) {

        List<CartItemResponse> items = cart.getItems()
                .stream()
                .map(i -> new CartItemResponse(
                        i.getLaundryService().getId(),
                        i.getLaundryService().getServiceType(),
                        i.getLaundryService().getItemType(),
                        i.getLaundryService().getCategory(),
                        i.getUnitPrice(),
                        i.getQuantity(),
                        i.getSubtotal()
                ))
                .toList();

        return new CartResponse(
                cart.getId(),
                cart.getCreatedAt(),
                cart.getTotalAmount(),
                items
        );
    }
}
