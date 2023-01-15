package br.com.leandrofood.order.service.impl;

import br.com.leandrofood.order.domain.Order;
import br.com.leandrofood.order.domain.Status;
import br.com.leandrofood.order.dto.OrderDto;
import br.com.leandrofood.order.dto.StatusDto;
import br.com.leandrofood.order.repository.OrderRepository;
import br.com.leandrofood.order.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<OrderDto> AllOrders() {
        return repository.findAll().stream()
                .map(p -> modelMapper.map(p, OrderDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto OrderPerId(Long id) {
        Order order = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return modelMapper.map(order, OrderDto.class);
    }

    @Override
    public OrderDto orderCreate(OrderDto dto) {
        Order order = modelMapper.map(dto, Order.class);

        order.setDate(LocalDateTime.now());
        order.setStatus(Status.REALIZED);
        order.getItens().forEach(item -> item.setOrder(order));
        repository.save(order);

        return modelMapper.map(order, OrderDto.class);
    }

    @Override
    public OrderDto updateStatus(Long id, StatusDto dto) {

        Order order = repository.toIdWithOrder(id);

        if (order == null) {
            throw new EntityNotFoundException();
        }

        order.setStatus(dto.getStatus());
        repository.updateStatus(dto.getStatus(), order);
        return modelMapper.map(order, OrderDto.class);
    }

    @Override
    public void approvePaymentOrder(Long id) {

        Order order = repository.toIdWithOrder(id);

        if (order == null) {
            throw new EntityNotFoundException();
        }

        order.setStatus(Status.PAID);
        repository.updateStatus(Status.PAID, order);
    }
}
