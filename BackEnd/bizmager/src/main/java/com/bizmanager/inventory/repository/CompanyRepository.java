package com.bizmanager.inventory.repository;

import com.bizmanager.inventory.model.TbCompany;
import com.bizmanager.inventory.model.dto.CompanyDetailsDTO;
import com.bizmanager.inventory.model.dto.CompanyGetDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CompanyRepository extends JpaRepository<TbCompany,Long> {

    @Query(nativeQuery = true, value = "SELECT " +
            "c.id, c.email, c.name, c.phone, c.cnpj, c.cep, c.street, c.neighborhood, c.city, c.state, c.complement, c.number, " +
            "r.id AS role_id " +
            "FROM company c " +
            "JOIN roles r ON c.role_id = r.id " +
            "WHERE c.id = :id")
    Optional<CompanyGetDetails> findCompanyById(@Param("id") Long id);

    TbCompany findTbCompaniesByName(String name);

    @Query(value = "SELECT id , name,cnpj FROM company WHERE cnpj=:cnpj;",nativeQuery = true)
    CompanyDetailsDTO findByTbComponiesByCnpj(@Param("cnpj") String cnpj);

    TbCompany findTbCompaniesByEmail(String email);

    Optional<TbCompany> findByEmail(String email);
}
