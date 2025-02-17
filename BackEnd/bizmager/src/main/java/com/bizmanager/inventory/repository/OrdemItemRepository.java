package com.bizmanager.inventory.repository;

import com.bizmanager.inventory.model.TbOrder;
import com.bizmanager.inventory.model.TbOrderItems;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdemItemRepository extends JpaRepository<TbOrderItems,Long> {

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "DELETE FROM order_items WHERE order_id = :orderId")
    void deleteOrderItemByOrderId(@Param("orderId") Long orderId);

    @Query(nativeQuery = true,value = "SELECT * FROM order_items WHERE product_id=:id;")
    List<TbOrderItems> lookingforOrderByProductId(@Param("id") Long id);

    List<TbOrderItems> findByOrderId(Long orderId);
}
