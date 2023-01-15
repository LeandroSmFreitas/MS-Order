package br.com.leandrofood.order.service;

import br.com.leandrofood.order.dto.OrderDto;
import br.com.leandrofood.order.dto.StatusDto;

import java.util.List;

public interface OrderService {


    List<OrderDto> AllOrders();

    OrderDto OrderPerId(Long id);

    OrderDto orderCreate(OrderDto dto);

    OrderDto updateStatus(Long id, StatusDto dto);

    void approvePaymentOrder(Long id);
}
