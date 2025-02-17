package com.bizmanager.inventory.repository;
import com.bizmanager.inventory.model.TbStock;
import com.bizmanager.inventory.model.dto.StockByIdDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository  extends JpaRepository<TbStock,Long> {

    List<TbStock> findByName(String name);

    @Query(nativeQuery = true, value = "SELECT s.id AS stock_id, s.name AS stock_name, s.company_id, s.responsible_id, e.name AS responsible_name, s.max_capacity, s.stock_type, s.created_at FROM stock s JOIN employees e ON s.responsible_id = e.id WHERE s.id = :id;")
    StockByIdDTO findStockById(@Param("id") Long id);

    @Query(nativeQuery = true,value = "SELECT * FROM stock WHERE company_id=:id ;")
    List<TbStock> searchingStocksByCompanyId(@Param("id") Long id);

    @Query(nativeQuery = true,value = "SELECT * FROM stock WHERE responsible_id=:id;")
    List<TbStock> lookingforSotckByResponsibleId(@Param("id") Long id);

    @Query(nativeQuery = true, value = "SELECT CASE WHEN (:quantity - COALESCE(SUM(products.quantity), 0)) < 0 THEN 0 ELSE 1 END AS 'Espaço' FROM stock LEFT JOIN products ON stock.id = products.stock_id WHERE stock.id = :id")
    Long checkingStockCapacity(@Param("id") Long id,@Param("quantity") Integer newQuantity);
}
