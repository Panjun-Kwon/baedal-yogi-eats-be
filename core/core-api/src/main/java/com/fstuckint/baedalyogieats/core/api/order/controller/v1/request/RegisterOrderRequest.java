package com.fstuckint.baedalyogieats.core.api.order.controller.v1.request;

import com.fstuckint.baedalyogieats.core.api.order.domain.dto.*;
import com.fstuckint.baedalyogieats.core.api.order.domain.dto.RegisterOrderCommand.*;
import com.fstuckint.baedalyogieats.core.enums.order.*;
import lombok.*;

import java.util.*;

@Getter
@NoArgsConstructor
public class RegisterOrderRequest {

    private Type type;

    private RegisterBuyerRequest buyer;

    private List<RegisterOrderItemRequest> products = new ArrayList<>();

    public RegisterOrderCommand toCommand() {

        RegisterBuyerCommand buyer = this.buyer.toCommand();

        List<RegisterOrderItemCommand> orderItems = this.products.stream()
            .map(registerOrderItemRequest -> RegisterOrderItemCommand.builder()
                .name(registerOrderItemRequest.name)
                .unitPrice(registerOrderItemRequest.unitPrice)
                .productUuid(registerOrderItemRequest.productUuid)
                .build())
            .toList();

        return RegisterOrderCommand.builder().type(this.type).buyer(buyer).products(orderItems).build();
    }

    @Getter
    @NoArgsConstructor
    public static class RegisterBuyerRequest {

        private String nickname;

        private UUID userUuid;

        public RegisterBuyerCommand toCommand() {
            return RegisterBuyerCommand.builder().nickname(this.nickname).userUuid(this.userUuid).build();
        }

    }

    @Getter
    @NoArgsConstructor
    public static class RegisterOrderItemRequest {

        private String name;

        private Integer unitPrice;

        private UUID productUuid;

        public RegisterOrderItemCommand toCommand() {
            return RegisterOrderItemCommand.builder()
                .name(this.name)
                .unitPrice(this.unitPrice)
                .productUuid(this.productUuid)
                .build();
        }

    }

}