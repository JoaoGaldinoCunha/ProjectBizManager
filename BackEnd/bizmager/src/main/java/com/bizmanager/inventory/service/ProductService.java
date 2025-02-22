package com.bizmanager.inventory.service;

import com.bizmanager.inventory.model.TbOrder;
import com.bizmanager.inventory.model.TbOrderItems;
import com.bizmanager.inventory.model.TbProduct;
import com.bizmanager.inventory.model.TbStock;
import com.bizmanager.inventory.model.dto.*;
import com.bizmanager.inventory.model.response.ApiResponse;
import com.bizmanager.inventory.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepsitory productRepository;
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private OrdemItemRepository ordemItemRepository;
    @Autowired
    private OrderRepository orderRepository;


    public ResponseEntity<ApiResponse> createProduct(TbProduct product,Long companyId) {
        if (product.getStock() == null) {
            return new ResponseEntity<>(new ApiResponse("O campo identificador de estoque não pode ser nulo."), HttpStatus.BAD_REQUEST);
        }

        List<Long> productName = productRepository.findByName(product.getName(),companyId,product.getStock().getId());
        Optional<TbStock> stock = stockRepository.findById(product.getStock().getId());
        if (!stock.isPresent()) {
            return new ResponseEntity<>(new ApiResponse("Id do estoque: " + product.getStock().getId() + " não existe"), HttpStatus.BAD_REQUEST);
        }

        if (product.getName() == null || product.getName().isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("O campo nome não pode ser nulo ou vazio."), HttpStatus.BAD_REQUEST);
        }

        if (!productName.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("Já existe um produto com esse nome."), HttpStatus.CONFLICT);
        }

        if (product.getDescription() == null || product.getDescription().isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("O campo descrição de estoque não pode ser nulo ou vazio."), HttpStatus.BAD_REQUEST);
        }

        if (product.getQuantity() == null || product.getQuantity() < 0) {
            return new ResponseEntity<>(new ApiResponse("O campo capacidade máxima não pode ser nulo ou menor que zero."), HttpStatus.BAD_REQUEST);
        }

        if (product.getCategory() == null || product.getCategory().isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("O campo categoria de estoque não pode ser nulo ou vazio."), HttpStatus.BAD_REQUEST);
        }

        if (product.getPurchasePrice() == null || product.getPurchasePrice().compareTo(BigDecimal.ZERO) < 0) {
            return new ResponseEntity<>(new ApiResponse("O campo preço de compra não pode ser nulo ou menor que zero."), HttpStatus.BAD_REQUEST);
        }

        if (product.getExpirationDate() != null && product.getExpirationDate().before(new Date())) {
            return new ResponseEntity<>(new ApiResponse("A data de expiração não pode ser uma data passada."), HttpStatus.BAD_REQUEST);
        }
        if (productRepository.checkingSpaceInStock(product.getQuantity(), product.getStock().getId()) == 0) {
            return new ResponseEntity<>(new ApiResponse("Não há espaço no estoque para adicionar essa quantidade de produtos."), HttpStatus.BAD_REQUEST);
        }

        TbProduct saveProduct = productRepository.save(product);
        return new ResponseEntity<>(new ApiResponse("Produto salvo com sucesso."), HttpStatus.CREATED);
    }


    public ResponseEntity<ApiResponse> updateProduct(Long id, TbProduct tbProduct) {
        if (!productRepository.existsById(id)) {
            return new ResponseEntity<>(new ApiResponse("Produto não foi encontrado"), HttpStatus.NOT_FOUND);
        }

        TbProduct existingProduct = productRepository.findById(id).get();

        if (tbProduct.getName() != null && !tbProduct.getName().isEmpty()) {
            existingProduct.setName(tbProduct.getName());
        }

        if (tbProduct.getDescription() != null) {
            existingProduct.setDescription(tbProduct.getDescription());
        }

        if (tbProduct.getStock() != null) {
            existingProduct.setStock(tbProduct.getStock());
        }

        if (tbProduct.getPurchasePrice() != null) {
            existingProduct.setPurchasePrice(tbProduct.getPurchasePrice());
        }

        if (tbProduct.getExpirationDate() != null) {
            existingProduct.setExpirationDate(tbProduct.getExpirationDate());
        }

        productRepository.save(existingProduct);
        return new ResponseEntity<>(new ApiResponse("Dados do produto atualizados"), HttpStatus.OK);
    }


    public ResponseEntity<ApiResponse> updateProductQuantity(ProductQuantityDTO productQuantityDTO) {
        if (!productRepository.existsById(productQuantityDTO.getProductId())) {
            return new ResponseEntity<>(new ApiResponse("Produto não foi encontrado"), HttpStatus.NOT_FOUND);
        }

        TbProduct existingProduct = productRepository.findById(productQuantityDTO.getProductId()).get();
        Integer quantityOld = existingProduct.getQuantity();
        Integer newQuantity = productQuantityDTO.getQuantity();

        if (newQuantity != null) {
            if (newQuantity > quantityOld) {
                int additionalQuantity = newQuantity - quantityOld;
                if (productRepository.checkingSpaceInStock(additionalQuantity, existingProduct.getStock().getId()) == 0) {
                    return new ResponseEntity<>(new ApiResponse("Não há espaço no estoque para adicionar essa quantidade de produtos."), HttpStatus.BAD_REQUEST);
                }
            }
            existingProduct.setQuantity(newQuantity);
        }

        productRepository.save(existingProduct);
        return new ResponseEntity<>(new ApiResponse("Quantidade do produto atualizada"), HttpStatus.OK);
    }


    public ResponseEntity<ApiResponse> deleteProduct(Long id) {
        Optional<TbProduct> productOptional = productRepository.findById(id);
        if (productOptional.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("Produto não encontrado."), HttpStatus.BAD_REQUEST);
        }

        List<TbOrderItems> orders = ordemItemRepository.lookingforOrderByProductId(id);
        if (!orders.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("Produto não pode ser apagado. Ele está relacionado a um pedido."), HttpStatus.BAD_REQUEST);
        }

        productRepository.deleteById(id);
        return new ResponseEntity<>(new ApiResponse("Produto apagado com sucesso."), HttpStatus.OK);
    }


    public ResponseEntity<?> searchAllProductsByStockId(Long id) {
        if (productRepository.searchingProductsByStockId(id) == null) {
            return new ResponseEntity<>(new ApiResponse("Estoques não foram encontrados."), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(productRepository.searchingProductsByStockId(id), HttpStatus.OK);
    }


    public ResponseEntity<?> searchProductsByName(String name,Long stockId) {
        if (name.matches(".*\\d.*")) {
            return new ResponseEntity<>(new ApiResponse("Não é permitido usar números."), HttpStatus.BAD_REQUEST);
        }

        List<TbProduct> products = productRepository.findProductsByNameAndStockId(name,stockId);

        if (products.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("Nenhum produto encontrado com esse nome."), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }


    public ResponseEntity<?> searchProductsMostOrdered(Long id) {
        List<ProductOrderDTO> productOrderDTOList = productRepository.searchingProductsMostOrdered(id);
        if (companyRepository.findById(id).isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("Nenhuma empresa encontrada."), HttpStatus.NOT_FOUND);
        }
        if (productOrderDTOList.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("Nenhum produto encontrado."), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(productOrderDTOList, HttpStatus.OK);
    }


    public ResponseEntity<?> searchProductsWithLowQuantity(Long id) {
        List<LowQuantityProductDTO> lowQuantityProductDTOS = productRepository.searchingProductsWithLowQuantity(id);
        if (companyRepository.findById(id).isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("Nenhuma empresa encontrada."), HttpStatus.NOT_FOUND);
        }
        if (lowQuantityProductDTOS.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("Nenhum produto encontrado."), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(lowQuantityProductDTOS, HttpStatus.OK);
    }

    public ResponseEntity<?> searchProductById(Long id) {
        Optional<TbProduct> productOptn = productRepository.findById(id);
        if (productOptn.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("Nenhuma produto encontrada."), HttpStatus.NOT_FOUND);
        }

        TbProduct product = productOptn.get();
        ProductByIdDTO responseDTO = new ProductByIdDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getCategory(),
                product.getExpirationDate(),
                product.getPurchasePrice(),
                product.getQuantity(),
                product.getStock().getId(),
                product.getStock().getName()
        );

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);

    }

    public ResponseEntity<?> searchAllProductsByComapnyId(Long id) {
        List<ProductStockDTO> products = productRepository.searchingaAllProductsByCompanyId(id);
        if (companyRepository.findById(id).isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("Nenhuma empresa encontrada."), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}