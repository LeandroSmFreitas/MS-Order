package br.com.leandrofood.order.web.rest;

import br.com.leandrofood.order.dto.OrderDto;
import br.com.leandrofood.order.dto.StatusDto;
import br.com.leandrofood.order.service.impl.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderResource {

        @Autowired
        private OrderServiceImpl service;

        @GetMapping()
        public List<OrderDto> allOrders() {
            return service.AllOrders();
        }

        @GetMapping("/orders/{id}")
        public ResponseEntity<OrderDto> orderPerId(@PathVariable @NotNull Long id) {
            OrderDto dto = service.OrderPerId(id);

            return  ResponseEntity.ok(dto);
        }

        @PostMapping("/orders")
        public ResponseEntity<OrderDto> orderRealized(@RequestBody @Valid OrderDto dto, UriComponentsBuilder uriBuilder) {
            OrderDto orderRealized = service.orderCreate(dto);

            URI address = uriBuilder.path("/orders/{id}").buildAndExpand(orderRealized.getId()).toUri();

            return ResponseEntity.created(address).body(orderRealized);

        }

        @PutMapping("/orders/{id}/status")
        public ResponseEntity<OrderDto> updateStatus(@PathVariable Long id, @RequestBody StatusDto status){
           OrderDto dto = service.updateStatus(id, status);

            return ResponseEntity.ok(dto);
        }


        @PutMapping("/orders/{id}/pago")
        public ResponseEntity<Void> approvePagamento(@PathVariable @NotNull Long id) {
            service.approvePaymentOrder(id);

            return ResponseEntity.ok().build();

        }
}
