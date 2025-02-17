package com.bizmanager.inventory.service;

import com.bizmanager.inventory.model.TbCompany;
import com.bizmanager.inventory.model.TbRoles;
import com.bizmanager.inventory.model.dto.CompanyGetDetails;
import com.bizmanager.inventory.model.dto.UpdateCompanyDTO;
import com.bizmanager.inventory.model.response.ApiResponse;
import com.bizmanager.inventory.repository.CompanyRepository;
import com.bizmanager.inventory.repository.EmployeesRepository;
import com.bizmanager.inventory.repository.RoleRepository;
import com.bizmanager.inventory.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private EmployeesRepository employeesRepository;

    @Autowired
    private BCryptPasswordEncoder passowrdEncoder;

    @Autowired
    RoleRepository roleRepository;

    private static final Pattern CNPJ_PATTERN = Pattern.compile("\\d{14}");
    @Autowired
    private StockRepository stockRepository;

    private boolean isValidCnpj(String cnpj) {
        return CNPJ_PATTERN.matcher(cnpj).matches();
    }

    public ResponseEntity<?> createCompany(TbCompany company) {

        if (!isValidCnpj(company.getCnpj())) {
            return new ResponseEntity<>(new ApiResponse("CNPJ inválido."), HttpStatus.BAD_REQUEST);
        }

        String cepUrl = "https://viacep.com.br/ws/" + company.getCep() + "/json/";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> responseCep = restTemplate.getForEntity(cepUrl, Map.class);

        if (responseCep.getStatusCode() != HttpStatus.OK || responseCep.getBody() == null) {
            return new ResponseEntity<>(new ApiResponse("CEP inválido."), HttpStatus.BAD_REQUEST);
        }
        Map<String, String> cepData = responseCep.getBody();
        company.setStreet(cepData.get("logradouro"));
        company.setNeighborhood(cepData.get("bairro"));
        company.setCity(cepData.get("localidade"));
        company.setState(cepData.get("uf"));

        String cnpjUrl = "https://open.cnpja.com/office/" + company.getCnpj();
        ResponseEntity<Map> responseCnpj = restTemplate.getForEntity(cnpjUrl, Map.class);

        if (responseCnpj.getStatusCode() != HttpStatus.OK || responseCnpj.getBody() == null) {
            return new ResponseEntity<>(new ApiResponse("CNPJ inválido ou inativo."), HttpStatus.BAD_REQUEST);
        }

        Optional<TbRoles> role = roleRepository.findById(TbRoles.Values.company.getId());
        if (role.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("Role 'Company' não foi encontrado."), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        company.setRole(role.get());

        if (company.getName() == null || company.getName().isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("O nome da companhia não pode ser nulo ou vazio."), HttpStatus.BAD_REQUEST);
        }
        if (company.getName().length() < 3) {
            return new ResponseEntity<>(new ApiResponse("O nome da companhia deve ter no mínimo 3 caracteres."), HttpStatus.BAD_REQUEST);
        }
        if (company.getEmail() == null || company.getEmail().isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("O e-mail da companhia não pode ser nulo ou vazio."), HttpStatus.BAD_REQUEST);
        }
        if (companyRepository.findTbCompaniesByEmail(company.getEmail()) != null) {
            return new ResponseEntity<>(new ApiResponse("Já existe uma companhia cadastrada com o e-mail."), HttpStatus.CONFLICT);
        }
        if (company.getPhone() == null || company.getPhone().isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("O telefone da companhia não pode ser nulo ou vazio."), HttpStatus.BAD_REQUEST);
        }
        if (company.getPhone().length() > 20) {
            return new ResponseEntity<>(new ApiResponse("O telefone da companhia deve ter no máximo 20 caracteres."), HttpStatus.BAD_REQUEST);
        }
        if (company.getPassword() == null || company.getPassword().isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("A senha da companhia não pode ser nula ou vazia."), HttpStatus.BAD_REQUEST);
        }
        if (company.getPassword().length() > 20) {
            return new ResponseEntity<>(new ApiResponse("A senha da companhia deve ter no máximo 20 caracteres."), HttpStatus.BAD_REQUEST);
        }
        if (company.getCnpj() == null || company.getCnpj().isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("O CNPJ da companhia não pode ser nulo ou vazio."), HttpStatus.BAD_REQUEST);
        }
        if (companyRepository.findByTbComponiesByCnpj(company.getCnpj()) != null) {
            return new ResponseEntity<>(new ApiResponse("O CNPJ da companhia já está cadastrado."), HttpStatus.CONFLICT);
        }
        if (company.getCnpj().length() != 14) {
            return new ResponseEntity<>(new ApiResponse("O CNPJ da companhia deve ter 14 caracteres."), HttpStatus.BAD_REQUEST);
        }
        if (company.getCep() == null || company.getCep().isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("O CEP da companhia não pode ser nulo ou vazio."), HttpStatus.BAD_REQUEST);
        }
        if (company.getCep().length() != 8) {
            return new ResponseEntity<>(new ApiResponse("O CEP da companhia deve ter 8 caracteres."), HttpStatus.BAD_REQUEST);
        }
        if (company.getStreet() == null || company.getStreet().isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("A rua da companhia não pode ser nula ou vazia."), HttpStatus.BAD_REQUEST);
        }
        if (company.getStreet().length() > 60) {
            return new ResponseEntity<>(new ApiResponse("A rua da companhia deve ter no máximo 60 caracteres."), HttpStatus.BAD_REQUEST);
        }
        if (company.getNeighborhood() == null || company.getNeighborhood().isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("O bairro da companhia não pode ser nulo ou vazio."), HttpStatus.BAD_REQUEST);
        }
        if (company.getNeighborhood().length() > 60) {
            return new ResponseEntity<>(new ApiResponse("O bairro da companhia deve ter no máximo 60 caracteres."), HttpStatus.BAD_REQUEST);
        }
        if (company.getCity() == null || company.getCity().isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("A cidade da companhia não pode ser nula ou vazia."), HttpStatus.BAD_REQUEST);
        }
        if (company.getCity().length() > 60) {
            return new ResponseEntity<>(new ApiResponse("A cidade da companhia deve ter no máximo 60 caracteres."), HttpStatus.BAD_REQUEST);
        }
        if (company.getState() == null || company.getState().isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("O estado da companhia não pode ser nulo ou vazio."), HttpStatus.BAD_REQUEST);
        }
        if (company.getState().length() > 120) {
            return new ResponseEntity<>(new ApiResponse("O estado da companhia deve ter no máximo 120 caracteres."), HttpStatus.BAD_REQUEST);
        }
        if (company.getComplement() != null && company.getComplement().length() > 15) {
            return new ResponseEntity<>(new ApiResponse("O complemento da companhia deve ter no máximo 15 caracteres."), HttpStatus.BAD_REQUEST);
        }
        if (company.getNumber() == null || company.getNumber().isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("O número da companhia não pode ser nulo ou vazio."), HttpStatus.BAD_REQUEST);
        }
        if (company.getNumber().length() > 15) {
            return new ResponseEntity<>(new ApiResponse("O número da companhia deve ter no máximo 20 caracteres."), HttpStatus.BAD_REQUEST);
        }

        company.setPassword(passowrdEncoder.encode(company.getPassword()));
        TbCompany savedCompany = companyRepository.save(company);
        return new ResponseEntity<>(new ApiResponse("Companhia salva com sucesso."), HttpStatus.CREATED);
    }


    public ResponseEntity<ApiResponse> deleteCompany(Long id) {
        var company = companyRepository.findById(id);

        if (company.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("Companhia não encontrada."), HttpStatus.NOT_FOUND);
        }
        if(!employeesRepository.searchingAllEmployeesByCompanyId(id).isEmpty()){
            return new ResponseEntity<>(new ApiResponse("Companhia não pode ser apagada, pois há funcionários registrados."), HttpStatus.BAD_REQUEST);
        }
        if (!stockRepository.searchingStocksByCompanyId(id).isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("Companhia não pode ser apagada, pois há estoques registrados."), HttpStatus.BAD_REQUEST);
        }
        companyRepository.deleteById(id);
        return new ResponseEntity<>(new ApiResponse("Companhia deletada com sucesso!"), HttpStatus.OK);
    }


    public ResponseEntity<ApiResponse> updateCompany(Long id, UpdateCompanyDTO updateCompanyDTO) {
        if (!companyRepository.existsById(id)) {
            return new ResponseEntity<>(new ApiResponse("Companhia não foi encontrada"), HttpStatus.NOT_FOUND);
        }

        TbCompany existingCompany = companyRepository.findById(id).get();

        if (updateCompanyDTO.getOldPassword() == null || updateCompanyDTO.getOldPassword().trim().isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("Preencha a senha antiga."), HttpStatus.BAD_REQUEST);
        }

        boolean isPasswordCorrect = passowrdEncoder.matches(updateCompanyDTO.getOldPassword(), existingCompany.getPassword());
        if (!isPasswordCorrect) {
            return new ResponseEntity<>(new ApiResponse("Senha incorreta"), HttpStatus.UNAUTHORIZED);
        }

        if (updateCompanyDTO.getCep() != null && !updateCompanyDTO.getCep().isEmpty()) {
            String cepUrl = "https://viacep.com.br/ws/" + updateCompanyDTO.getCep() + "/json/";
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> responseCep = restTemplate.getForEntity(cepUrl, Map.class);

            if (responseCep.getStatusCode() != HttpStatus.OK || responseCep.getBody() == null) {
                return new ResponseEntity<>(new ApiResponse("CEP inválido."), HttpStatus.BAD_REQUEST);
            }
            Map<String, String> cepData = responseCep.getBody();
            existingCompany.setStreet(cepData.get("logradouro"));
            existingCompany.setNeighborhood(cepData.get("bairro"));
            existingCompany.setCity(cepData.get("localidade"));
            existingCompany.setState(cepData.get("uf"));
            existingCompany.setCep(updateCompanyDTO.getCep());
        }

        if (updateCompanyDTO.getCnpj() != null && !updateCompanyDTO.getCnpj().isEmpty()) {
            if (!isValidCnpj(updateCompanyDTO.getCnpj())) {
                return new ResponseEntity<>(new ApiResponse("CNPJ inválido."), HttpStatus.BAD_REQUEST);
            }
            String cnpjUrl = "https://open.cnpja.com/office/" + updateCompanyDTO.getCnpj();
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> responseCnpj = restTemplate.getForEntity(cnpjUrl, Map.class);

            if (responseCnpj.getStatusCode() != HttpStatus.OK || responseCnpj.getBody() == null) {
                return new ResponseEntity<>(new ApiResponse("CNPJ inválido ou inativo."), HttpStatus.BAD_REQUEST);
            }
            existingCompany.setCnpj(updateCompanyDTO.getCnpj());
        }

        if (updateCompanyDTO.getName() != null && !updateCompanyDTO.getName().isEmpty()) {
            existingCompany.setName(updateCompanyDTO.getName());
        }
        if (updateCompanyDTO.getEmail() != null && !updateCompanyDTO.getEmail().isEmpty()) {
            existingCompany.setEmail(updateCompanyDTO.getEmail());
        }
        if (updateCompanyDTO.getPhone() != null && !updateCompanyDTO.getPhone().isEmpty()) {
            existingCompany.setPhone(updateCompanyDTO.getPhone());
        }
        if (updateCompanyDTO.getPassword() != null && !updateCompanyDTO.getPassword().trim().isEmpty()) {
            existingCompany.setPassword(passowrdEncoder.encode(updateCompanyDTO.getPassword()));
        }
        if (updateCompanyDTO.getStreet() != null && !updateCompanyDTO.getStreet().isEmpty()) {
            existingCompany.setStreet(updateCompanyDTO.getStreet());
        }
        if (updateCompanyDTO.getNeighborhood() != null && !updateCompanyDTO.getNeighborhood().isEmpty()) {
            existingCompany.setNeighborhood(updateCompanyDTO.getNeighborhood());
        }
        if (updateCompanyDTO.getCity() != null && !updateCompanyDTO.getCity().isEmpty()) {
            existingCompany.setCity(updateCompanyDTO.getCity());
        }
        if (updateCompanyDTO.getState() != null && !updateCompanyDTO.getState().isEmpty()) {
            existingCompany.setState(updateCompanyDTO.getState());
        }
        if (updateCompanyDTO.getComplement() != null && !updateCompanyDTO.getComplement().isEmpty()) {
            existingCompany.setComplement(updateCompanyDTO.getComplement());
        }
        if (updateCompanyDTO.getNumber() != null && !updateCompanyDTO.getNumber().isEmpty()) {
            existingCompany.setNumber(updateCompanyDTO.getNumber());
        }

        companyRepository.save(existingCompany);
        return new ResponseEntity<>(new ApiResponse("Dados da companhia atualizados."), HttpStatus.OK);
    }
    public ResponseEntity<?> searchCompanyById(Long id) {
        Optional<CompanyGetDetails> company = companyRepository.findCompanyById(id);
        if (company.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("Nenhum companhia encontrada."), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(company, HttpStatus.OK);
    }

}