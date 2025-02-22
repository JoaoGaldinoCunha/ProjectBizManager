package com.bizmanager.inventory.repository;

import com.bizmanager.inventory.model.TbOrder;
import com.bizmanager.inventory.model.dto.OrdersDTO;
import com.bizmanager.inventory.model.dto.OrdersDetailsDTO;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<TbOrder,Long> {

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "UPDATE orders SET status = :status WHERE id = :id")
    void updatingStatusByOrderId(@Param("status") String status , @Param("id") Long id);


    @Query(nativeQuery = true,value = "SELECT * FROM orders WHERE responsible_id=:id")
    List<TbOrder> lookingforOrderByResponsibleId(@Param("id") Long id);


    @Query(nativeQuery = true, value = "SELECT orders.id AS orderId, orders.created_at AS createdAt, orders.client_cnpj AS clientCnpj, orders.destination AS destination, COUNT(order_items.id) AS itemCount, orders.status AS status FROM orders JOIN order_items ON orders.id = order_items.order_id JOIN employees ON orders.responsible_id = employees.id WHERE employees.company_id = :company_id GROUP BY orders.id, orders.created_at, orders.client_cnpj, orders.destination, orders.status;")
    List<OrdersDTO> searchingAllOrders(@Param("company_id") Long id);


    @Query(nativeQuery = true, value = "SELECT orders.id AS orderId, orders.created_at AS createdAt, orders.client_cnpj AS clientCnpj, orders.destination AS destination, COUNT(order_items.id) AS itemCount, orders.status AS status FROM orders JOIN order_items ON orders.id = order_items.order_id JOIN employees ON orders.responsible_id = employees.id WHERE employees.company_id = :company_id AND orders.id = :order_id GROUP BY orders.id, orders.created_at, orders.client_cnpj, orders.destination, orders.status")
    List<OrdersDTO> searchingOrdersByIdComapnyAndOrderId(@Param("company_id") Long companyId, @Param("order_id") Long orderId);


    @Query(nativeQuery = true,value = "SELECT o.id AS order_id, o.status, o.client_cnpj, p.name AS product_name, oi.quantity AS product_quantity, p.category AS product_category FROM orders o JOIN order_items oi ON o.id = oi.order_id JOIN products p ON oi.product_id = p.id JOIN employees e ON o.responsible_id = e.id JOIN company c ON e.company_id = c.id WHERE o.id =:order_id  AND c.id = :company_id ORDER BY o.id;")
    List<OrdersDetailsDTO> seachingOderById(@Param("order_id")Long idOrder,@Param("company_id")Long idComapny);


}
