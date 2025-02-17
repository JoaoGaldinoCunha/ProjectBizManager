package com.bizmanager.inventory.service;

import com.bizmanager.inventory.model.TbStock;
import com.bizmanager.inventory.model.dto.StockByIdDTO;
import com.bizmanager.inventory.model.response.ApiResponse;
import com.bizmanager.inventory.repository.ProductRepsitory;
import com.bizmanager.inventory.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ProductRepsitory productRepsitory;

    public ResponseEntity<ApiResponse> createStock(TbStock stock) {
        if (stock.getName() == null || stock.getName().isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("O campo nome não pode ser nulo ou vazio."), HttpStatus.BAD_REQUEST);
        }

        List<TbStock> stockName = stockRepository.findByName(stock.getName());
        if (!stockName.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("Já existe um Stock com esse nome."), HttpStatus.CONFLICT);
        }

        if (stock.getMaxCapacity() == null || stock.getMaxCapacity() < 0) {
            return new ResponseEntity<>(new ApiResponse("O campo capacidade máxima não pode ser nulo ou menor que zero."), HttpStatus.BAD_REQUEST);
        }

        if (stock.getStockType() == null || stock.getStockType().isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("O campo tipo de estoque não pode ser nulo ou vazio."), HttpStatus.BAD_REQUEST);
        }

        if (stock.getCreatedAt() == null) {
            return new ResponseEntity<>(new ApiResponse("O campo data de criação não pode ser nulo ou vazio."), HttpStatus.BAD_REQUEST);
        }

        if (stock.getCompany() == null) {
            return new ResponseEntity<>(new ApiResponse("O campo tipo de companhia não pode ser nulo ou vazio."), HttpStatus.BAD_REQUEST);
        }

        if (stock.getResponsible() == null) {
            return new ResponseEntity<>(new ApiResponse("O campo responsável não pode ser nulo ou vazio."), HttpStatus.BAD_REQUEST);
        }

        TbStock savedStock = stockRepository.save(stock);
        return new ResponseEntity<>(new ApiResponse("Estoque criado com sucesso."), HttpStatus.CREATED);
    }

    public ResponseEntity<ApiResponse> updateStock(Long id, TbStock tbStock) {
        if (!stockRepository.existsById(id)) {
            return new ResponseEntity<>(new ApiResponse("Estoque não foi encontrado"), HttpStatus.NOT_FOUND);
        }

        TbStock existingStock = stockRepository.findById(id).orElse(null);
        if (existingStock == null) {
            return new ResponseEntity<>(new ApiResponse("Estoque não foi encontrado"), HttpStatus.NOT_FOUND);
        }

        if (tbStock.getName() != null && !tbStock.getName().isEmpty()) {
            existingStock.setName(tbStock.getName());
        }

        Integer quantityOld = existingStock.getMaxCapacity();
        Integer newQuantity = tbStock.getMaxCapacity();

        if (newQuantity < quantityOld) {
            Long capacityCheck = stockRepository.checkingStockCapacity(id, newQuantity);
            if (capacityCheck == 0) {
                return new ResponseEntity<>(new ApiResponse("Não é possível diminuir o tamanho do estoque, pois há uma quantidade de produtos maior do que a capacidade desejada."), HttpStatus.BAD_REQUEST);
            }
        }

        if (tbStock.getMaxCapacity() != null) {
            existingStock.setMaxCapacity(tbStock.getMaxCapacity());
        }

        if (tbStock.getStockType() != null && !tbStock.getStockType().isEmpty()) {
            existingStock.setStockType(tbStock.getStockType());
        }

        if (tbStock.getCompany() != null) {
            existingStock.setCompany(tbStock.getCompany());
        }

        if (tbStock.getResponsible() != null) {
            existingStock.setResponsible(tbStock.getResponsible());
        }

        stockRepository.save(existingStock);
        return new ResponseEntity<>(new ApiResponse("Dados do estoque atualizados"), HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse> deleteStock(Long id) {
        if (!stockRepository.existsById(id)) {
            return new ResponseEntity<>(new ApiResponse("Estoque não encontrado."), HttpStatus.BAD_REQUEST);
        }

        if (!productRepsitory.searchingProductsByStockId(id).isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("Estoque não pode ser apagado, pois possui produtos cadastrados nele."), HttpStatus.BAD_REQUEST);
        }

        stockRepository.deleteById(id);
        return new ResponseEntity<>(new ApiResponse("Estoque apagado com sucesso"), HttpStatus.OK);
    }

    public ResponseEntity<?> searchAllStocksByCompanyId(Long id) {
        List<TbStock> stocks = stockRepository.searchingStocksByCompanyId(id);
        if (stocks.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("Estoques não foram encontrados."), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(stocks, HttpStatus.OK);
    }


    public ResponseEntity<?> searchStockById(Long id) {
        StockByIdDTO stocks = stockRepository.findStockById(id);
        if (stocks== null) {
            return new ResponseEntity<>(new ApiResponse("Estoques não foram encontrados."), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(stocks, HttpStatus.OK);
    }


}