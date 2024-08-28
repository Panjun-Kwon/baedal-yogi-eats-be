package com.fstuckint.baedalyogieats.core.api.order.domain;

import com.fstuckint.baedalyogieats.storage.db.core.order.*;
import lombok.*;
import org.springframework.stereotype.*;

import java.util.*;

@Component
@RequiredArgsConstructor
public class OrderStore {

    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    private final BuyerRepository buyerRepository;

    public OrderEntity storeOrder(OrderEntity orderEntity, List<OrderItemEntity> orderItemEntities,
            BuyerEntity buyerEntity) {
        OrderEntity savedOrder = orderRepository.save(orderEntity);
        orderItemRepository.saveAll(orderItemEntities);
        buyerRepository.save(buyerEntity);
        return savedOrder;
    }

}