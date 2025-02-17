package com.bizmanager.inventory.service;

import com.bizmanager.inventory.model.response.ApiResponse;
import com.bizmanager.inventory.model.TbOrder;
import com.bizmanager.inventory.model.TbOrderItems;
import com.bizmanager.inventory.model.TbProduct;
import com.bizmanager.inventory.model.dto.ProductQuantityDTO;
import com.bizmanager.inventory.repository.CompanyRepository;
import com.bizmanager.inventory.repository.OrdemItemRepository;
import com.bizmanager.inventory.repository.OrderRepository;
import com.bizmanager.inventory.repository.ProductRepsitory;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrdemItemRepository ordemItemRepository;

    @Autowired
    ProductRepsitory productRepsitory;

    @Autowired
    private CompanyRepository companyRepository;


    public ResponseEntity<?> createOrder(TbOrder order, List<ProductQuantityDTO> products) {
        if (order.getCreatedAt() == null) {
            order.setCreatedAt(LocalDateTime.now());
        }

        if (order.getClientCnpj() == null || order.getClientCnpj().isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("O campo CNPJ não pode ser nulo ou vazio."), HttpStatus.CONFLICT);
        }

        if (order.getDestination() == null || order.getDestination().isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("O campo destino de estoque não pode ser nulo ou vazio."), HttpStatus.BAD_REQUEST);
        }

        if (order.getResponsible() == null) {
            return new ResponseEntity<>(new ApiResponse("O campo responsável não pode ser nulo ou menor que zero."), HttpStatus.BAD_REQUEST);
        }

        TbOrder saveOrder = orderRepository.save(order);

        List<TbOrderItems> orderItemsList = new ArrayList<>();
        for (ProductQuantityDTO productQuantityDTO : products) {
            TbProduct product = productRepsitory.findById(productQuantityDTO.getProductId()).orElse(null);

            if (product == null) {
                return new ResponseEntity<>(new ApiResponse("Produto não encontrado"), HttpStatus.BAD_REQUEST);
            }

            if (productRepsitory.checkingIfProductsInStock(product.getId(), productQuantityDTO.getQuantity()) == 0) {
                return new ResponseEntity<>(new ApiResponse("Não há produtos suficientes no estoque"), HttpStatus.BAD_REQUEST);
            }

            TbOrderItems orderItems = new TbOrderItems();
            orderItems.setProduct(product);
            orderItems.setOrder(saveOrder);
            orderItems.setQuantity(productQuantityDTO.getQuantity());
            orderItemsList.add(orderItems);

            productRepsitory.updatingQuantityOfProductsAfterOrder(product.getId(), productQuantityDTO.getQuantity());
        }

        ordemItemRepository.saveAll(orderItemsList);
        return new ResponseEntity<>(new ApiResponse("Pedido salvo."), HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<?> deleteOrder(Long id) {
        Optional<TbOrder> orderId = orderRepository.findById(id);

        if (orderId.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("Pedido não encontrado."), HttpStatus.BAD_REQUEST);
        }


        TbOrder tbOrder = orderId.get();
        List<TbOrderItems> orderItemsList = ordemItemRepository.findByOrderId(tbOrder.getId());
        for (TbOrderItems orderItems : orderItemsList) {
            Long productId = orderItems.getProduct().getId();
            Integer quantity = orderItems.getQuantity();

            productRepsitory.incrementProductQuantity(productId, quantity);
        }

        ordemItemRepository.deleteAll(orderItemsList);
        orderRepository.delete(tbOrder);
        return new ResponseEntity<>(new ApiResponse("Pedido excluído com sucesso."), HttpStatus.OK);
    }


    public ResponseEntity<?> updateOrderById(Long id, String status) {
        Optional<TbOrder> orderId = orderRepository.findById(id);
        if (orderId.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("Pedido não encotrado."), HttpStatus.BAD_REQUEST);
        }
        if (status == null) {
            return new ResponseEntity<>(new ApiResponse("Status não pode ser nulo."), HttpStatus.BAD_REQUEST);
        }

        orderRepository.updatingStatusByOrderId(status, id);
        return new ResponseEntity<>(new ApiResponse("Status do pedido atualizado com sucesso."), HttpStatus.OK);
    }


    public ResponseEntity<?> searchAllOrders(Long id) {
        if (!companyRepository.findById(id).isPresent()) {
            return new ResponseEntity<>(new ApiResponse("Companhia não encontrada."), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(orderRepository.searchingAllOrders(id), HttpStatus.OK);
    }


    public ResponseEntity<?>searchOdersById(Long id){
        if(!orderRepository.findById(id).isPresent()){
            return new ResponseEntity<>(new ApiResponse("Pedido não encontrada."), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(orderRepository.seachingOderById(id), HttpStatus.OK);
    }

    public ResponseEntity<?> seachOrderByIdCompanyAndOrder(Long companyId, Long orderId) {
        if (!orderRepository.findById(orderId).isPresent()) {
            return new ResponseEntity<>(new ApiResponse("Pedido não encontrado."), HttpStatus.BAD_REQUEST);
        }
        if(!companyRepository.findById(companyId).isPresent()){
            return new ResponseEntity<>(new ApiResponse("Companhia não encontrado."), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(orderRepository.searchingOrdersByIdComapnyAndOrderId(companyId, orderId), HttpStatus.OK);
    }



}