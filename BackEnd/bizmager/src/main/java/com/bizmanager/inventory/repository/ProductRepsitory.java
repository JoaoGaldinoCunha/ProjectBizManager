package com.bizmanager.inventory.repository;

import com.bizmanager.inventory.model.TbProduct;
import com.bizmanager.inventory.model.dto.LowQuantityProductDTO;
import com.bizmanager.inventory.model.dto.ProductOrderDTO;
import com.bizmanager.inventory.model.dto.ProductStockDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductRepsitory extends JpaRepository<TbProduct,Long> {

    List<TbProduct> findByName(String name);

    @Query(nativeQuery = true, value = "SELECT * FROM products WHERE stock_id=:id")
    List<TbProduct> searchingProductsByStockId(@Param("id") Long id);

    @Query(value = "SELECT * FROM products WHERE name LIKE :name% AND stock_id = :stock_id", nativeQuery = true)
    List<TbProduct> findProductsByNameAndStockId(@Param("name") String name, @Param("stock_id") Long id);


    @Query(value = "SELECT p.id AS product_id, p.name AS product_name, p.description, p.category, p.expiration_date, p.purchase_price, p.quantity, s.id AS stock_id, s.name AS stock_name, c.id AS company_id, c.name AS company_name FROM products p JOIN stock s ON p.stock_id = s.id JOIN company c ON s.company_id = c.id WHERE c.id = :company_id;",nativeQuery = true)
    List<ProductStockDTO> searchingaAllProductsByCompanyId(@Param("company_id") Long id);

    @Override
    boolean existsById(Long Long);


    @Query(nativeQuery = true, value = "SELECT CASE WHEN (stock.max_capacity - COALESCE(SUM(products.quantity), 0) - :quantity) >= 0 THEN 1 ELSE 0 END AS 'TemEspaco' FROM stock LEFT JOIN products ON stock.id = products.stock_id WHERE stock.id = :id")
    Long checkingSpaceInStock(@Param("quantity") Integer quantity, @Param("id") Long id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "UPDATE products SET quantity = quantity - :quantity WHERE id = :id")
    void updatingQuantityOfProductsAfterOrder(@Param("id") Long id, @Param("quantity") Integer quantity);


    @Query(nativeQuery = true, value = "SELECT CASE WHEN (products.quantity - :quantity) >= 0 THEN 1 ELSE 0 END AS 'ProdutoQtd' FROM products WHERE id = :id")
    Long checkingIfProductsInStock(@Param("id") Long id, @Param("quantity") Integer quantity);


    @Query(nativeQuery = true, value = "SELECT p.id AS product_id, p.category AS product_category, p.name AS product_name, p.quantity, p.stock_id, s.company_id, CASE WHEN p.quantity < 10 THEN 'Baixo' WHEN p.quantity BETWEEN 10 AND 50 THEN 'Médio' ELSE 'Alto' END AS stock_volume FROM products p JOIN stock s ON p.stock_id = s.id WHERE s.company_id = :id ORDER BY p.quantity ASC LIMIT 7;")
    List<LowQuantityProductDTO> searchingProductsWithLowQuantity(@Param("id") Long id);
    
    @Query(nativeQuery = true, value = "SELECT p.id AS productId, p.name AS productName, SUM(oi.quantity) AS totalOrdered, p.quantity AS quantityInStock, CASE WHEN p.quantity > 0 THEN 'Disponível' ELSE 'Indisponível' END AS status FROM order_items oi JOIN products p ON oi.product_id = p.id JOIN stock s ON p.stock_id = s.id WHERE s.company_id = :id GROUP BY p.id, p.name, p.quantity ORDER BY totalOrdered DESC LIMIT 10")
    List<ProductOrderDTO> searchingProductsMostOrdered(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "UPDATE products SET quantity = quantity + :quantity WHERE id = :id")
    void incrementProductQuantity(@Param("id") Long id, @Param("quantity") Integer quantity);

}