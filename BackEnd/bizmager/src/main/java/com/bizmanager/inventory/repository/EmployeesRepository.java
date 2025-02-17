package com.bizmanager.inventory.repository;

import com.bizmanager.inventory.model.TbEmployees;
import com.bizmanager.inventory.model.TbProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeesRepository extends JpaRepository<TbEmployees,Long> {

    @Query(value = "SELECT * FROM employees WHERE company_id = :id", nativeQuery = true)
    List<TbEmployees> searchingAllEmployeesByCompanyId(@Param("id") Long id);

    Optional<TbEmployees> findById(Long id);

    TbEmployees findTbEmployeesByEmail(String email);

    @Query(value = "SELECT e.id, e.name, e.birth_date, e.email, e.password, e.cpf, e.company_id, e.role_id, r.name AS role_name FROM employees e JOIN roles r ON e.role_id = r.id WHERE e.name LIKE :name%", nativeQuery = true)
    List<TbEmployees> searchingEmployeeNames(@Param("name") String name);


    Optional<TbEmployees> findByEmail(String email);

    boolean existsByCpf(String cpf);

    TbEmployees findByCpf(String cpf);

    @Query(nativeQuery = true,value = "SELECT p.id AS product_id, p.name AS product_name, p.description, p.category, p.quantity, p.expiration_date, p.purchase_price, s.name AS stock_name FROM products p JOIN stock s ON p.stock_id = s.id WHERE p.name = :name")
    List<TbProduct> searchingProductsNames(@Param("name") String name);

}
