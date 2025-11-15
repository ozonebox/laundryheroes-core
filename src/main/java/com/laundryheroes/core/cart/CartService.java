package com.laundryheroes.core.cart;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.laundryheroes.core.common.ApiResponse;
import com.laundryheroes.core.common.ResponseCode;
import com.laundryheroes.core.common.ResponseFactory;
import com.laundryheroes.core.servicecatalog.LaundryService;
import com.laundryheroes.core.servicecatalog.LaundryServiceRepository;
import com.laundryheroes.core.user.User;

@Service
public class CartService {

    private final CartItemRepository cartRepo;
    private final LaundryServiceRepository serviceRepo;
    private final ResponseFactory responseFactory;

    public CartService(CartItemRepository cartRepo,
                       LaundryServiceRepository serviceRepo,
                       ResponseFactory responseFactory) {
        this.cartRepo = cartRepo;
        this.serviceRepo = serviceRepo;
        this.responseFactory = responseFactory;
    }

    @Transactional
    public ApiResponse<?> addToCart(User user, AddToCartRequest request) {

        LaundryService service = serviceRepo.findById(request.getServiceId()).orElse(null);
        if (service == null || !service.isActive()) {
            return responseFactory.error(ResponseCode.SERVICE_NOT_FOUND);
        }

        CartItem item = cartRepo.findByUserAndService(user, service)
                .orElse(new CartItem());

        item.setUser(user);
        item.setService(service);
        item.setQuantity(request.getQuantity());
        item.setSubtotal(service.getPrice() * request.getQuantity());

        cartRepo.save(item);

        return responseFactory.success(ResponseCode.SUCCESS, null);
    }

    @Transactional
    public ApiResponse<?> updateCartItem(User user, Long cartItemId, UpdateCartRequest request) {

        CartItem item = cartRepo.findById(cartItemId).orElse(null);
        if (item == null || !item.getUser().getId().equals(user.getId())) {
            return responseFactory.error(ResponseCode.CART_ITEM_NOT_FOUND);
        }

        item.setQuantity(request.getQuantity());
        item.setSubtotal(item.getService().getPrice() * request.getQuantity());

        cartRepo.save(item);

        return responseFactory.success(ResponseCode.SUCCESS, null);
    }

    @Transactional
    public ApiResponse<?> removeItem(User user, Long cartItemId) {

        CartItem item = cartRepo.findById(cartItemId).orElse(null);
        if (item == null || !item.getUser().getId().equals(user.getId())) {
            return responseFactory.error(ResponseCode.CART_ITEM_NOT_FOUND);
        }

        cartRepo.delete(item);
        return responseFactory.success(ResponseCode.SUCCESS, null);
    }

    @Transactional
    public ApiResponse<?> clearCart(User user) {

        cartRepo.deleteByUser(user);
        return responseFactory.success(ResponseCode.SUCCESS, null);
    }

    public ApiResponse<List<CartItemResponse>> getCart(User user) {

        List<CartItemResponse> list = cartRepo.findByUser(user)
                .stream()
                .map(c -> new CartItemResponse(
                        c.getId(),
                        c.getService().getId(),
                        c.getService().getServiceType(),
                        c.getService().getItemType(),
                        c.getService().getCategory(),
                        c.getService().getPrice(),
                        c.getQuantity(),
                        c.getSubtotal()
                ))
                .collect(Collectors.toList());

        return responseFactory.success(ResponseCode.SUCCESS, list);
    }
}
