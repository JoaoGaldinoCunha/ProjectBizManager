package com.bizmanager.inventory.service;

import com.bizmanager.inventory.model.dto.EmployeeDetailsDTO;
import com.bizmanager.inventory.model.dto.UpdateEmployeeByAdminOrCompanyDTO;
import com.bizmanager.inventory.model.response.ApiResponse;
import com.bizmanager.inventory.model.TbCompany;
import com.bizmanager.inventory.model.TbEmployees;
import com.bizmanager.inventory.model.TbRoles;
import com.bizmanager.inventory.model.dto.EmployeeDTO;
import com.bizmanager.inventory.model.dto.UpdateEmployeeDTO;
import com.bizmanager.inventory.model.request.VerifyPassword;
import com.bizmanager.inventory.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeesService {

    @Autowired
    private EmployeesRepository employeesRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtDecoder jwtDecoder;


    public ResponseEntity<?> createEmployee(TbEmployees employee) {
        if (employee.getEmail() == null) {
            return new ResponseEntity<>(new ApiResponse("O campo e-mail não pode ser nulo ou vazio."), HttpStatus.BAD_REQUEST);
        }
        if (employeesRepository.findTbEmployeesByEmail(employee.getEmail()) != null) {
            return new ResponseEntity<>(new ApiResponse("Já existe um funcionário cadastrado com esse e-mail."), HttpStatus.CONFLICT);
        }
        if (employee.getCpf().isEmpty() || employee.getCpf() == null) {
            return new ResponseEntity<>(new ApiResponse("O campo CPF não pode ser nulo ou vazio."), HttpStatus.BAD_REQUEST);
        }
        if (employeesRepository.existsByCpf(employee.getCpf())) {
            return new ResponseEntity<>(new ApiResponse("O campo CPF já existe"), HttpStatus.BAD_REQUEST);
        }
        if (employee.getName() == null || employee.getName().isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("O campo nome não pode ser nulo ou vazio."), HttpStatus.BAD_REQUEST);
        }
        if (employee.getPassword() == null || employee.getPassword().isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("O campo senha não pode ser nulo ou vazio."), HttpStatus.BAD_REQUEST);
        }
        if (employee.getBirthDate() == null || employee.getBirthDate().getTime() == 0) {
            return new ResponseEntity<>(new ApiResponse("O campo Data de aniversário não pode ser nulo ou vazio."), HttpStatus.BAD_REQUEST);
        }
        if (employee.getRole() == null || employee.getRole().getId() == null) {
            return new ResponseEntity<>(new ApiResponse("O campo role não pode ser nulo."), HttpStatus.BAD_REQUEST);
        }
        if (employee.getCompany() == null || employee.getCompany().getId() == null) {
            return new ResponseEntity<>(new ApiResponse("O campo company não pode ser nulo."), HttpStatus.BAD_REQUEST);
        }

        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        TbEmployees savedEmployee = employeesRepository.save(employee);
        return new ResponseEntity<>(new ApiResponse("Funcionário criado com sucesso."), HttpStatus.CREATED);
    }


    public ResponseEntity<ApiResponse> deleteEmployee(Long id) {
        var company = employeesRepository.findById(id);
        if (company.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("Funcionário não encontrado."), HttpStatus.NOT_FOUND);
        }
        if (!orderRepository.lookingforOrderByResponsibleId(id).isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("Não é possível apagar o funcionário, pois ele é responsável por um pedido."), HttpStatus.BAD_REQUEST);
        }
        if (!stockRepository.lookingforSotckByResponsibleId(id).isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("Não é possível apagar o funcionário, pois ele é responsável por um estoque."), HttpStatus.BAD_REQUEST);
        }

        employeesRepository.deleteById(id);
        return new ResponseEntity<>(new ApiResponse("Funcionário deletado com sucesso!"), HttpStatus.OK);
    }


    public ResponseEntity<ApiResponse> updateEmployee(Long id, UpdateEmployeeDTO updateEmployeeDTO) {
        Optional<TbEmployees> employee = employeesRepository.findById(id);
        if (employee.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("Funcionário não foi encontrado"), HttpStatus.NOT_FOUND);
        }

        TbEmployees existingEmployees = employee.get();


        if (updateEmployeeDTO.getOldPassword() == null || updateEmployeeDTO.getOldPassword().trim().isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("Preencha a senha antiga."), HttpStatus.BAD_REQUEST);
        }

        boolean isPasswordCorrect = existingEmployees.isPasswordCorrect(new VerifyPassword(updateEmployeeDTO.getOldPassword()), passwordEncoder);
        if (!isPasswordCorrect) {
            return new ResponseEntity<>(new ApiResponse("Senha incorreta"), HttpStatus.UNAUTHORIZED);
        }

        if (updateEmployeeDTO.getName() != null && !updateEmployeeDTO.getName().isEmpty()) {
            existingEmployees.setName(updateEmployeeDTO.getName());
        }
        if (updateEmployeeDTO.getEmail() != null && !updateEmployeeDTO.getEmail().isEmpty()) {
            existingEmployees.setEmail(updateEmployeeDTO.getEmail());
        }

        if (updateEmployeeDTO.getCpf().isEmpty() || updateEmployeeDTO.getCpf() == null) {
            return new ResponseEntity<>(new ApiResponse("O campo CPF não pode ser nulo ou vazio."), HttpStatus.BAD_REQUEST);
        }
        TbEmployees employeeWithSameCpf = employeesRepository.findByCpf(updateEmployeeDTO.getCpf());
        if (employeeWithSameCpf != null && !employeeWithSameCpf.getId().equals(existingEmployees.getId())) {
            return new ResponseEntity<>(new ApiResponse("O campo CPF já existe"), HttpStatus.BAD_REQUEST);
        }
        existingEmployees.setCpf(updateEmployeeDTO.getCpf());

        if (updateEmployeeDTO.getBirthDate() != null && updateEmployeeDTO.getBirthDate().getTime() != 0) {
            existingEmployees.setBirthDate(updateEmployeeDTO.getBirthDate());
        }

        if (updateEmployeeDTO.getCompany() != null) {
            TbCompany company = companyRepository.findById(updateEmployeeDTO.getCompany().getId()).orElse(null);
            if (company != null) {
                existingEmployees.setCompany(company);
            } else {
                return new ResponseEntity<>(new ApiResponse("Empresa não encontrada."), HttpStatus.NOT_FOUND);
            }
        }

        if (updateEmployeeDTO.getRole() != null) {
            TbRoles role = roleRepository.findById(updateEmployeeDTO.getRole().getId()).orElse(null);
            if (role != null) {
                existingEmployees.setRole(role);
            } else {
                return new ResponseEntity<>(new ApiResponse("Erro ao trocar nível de acesso."), HttpStatus.NOT_FOUND);
            }
        }

        if (updateEmployeeDTO.getPassword() != null && !updateEmployeeDTO.getPassword().trim().isEmpty()) {
            existingEmployees.setPassword(passwordEncoder.encode(updateEmployeeDTO.getPassword()));
        }

        employeesRepository.save(existingEmployees);
        return new ResponseEntity<>(new ApiResponse("Dados do funcionário atualizados"), HttpStatus.OK);
    }


    public ResponseEntity<ApiResponse> updateAdminOrCompany(Long id, UpdateEmployeeByAdminOrCompanyDTO updateEmployeeByAdminOrCompanyDTO) {
        Optional<TbEmployees> employee = employeesRepository.findById(id);
        if (employee.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("Funcionário não foi encontrado"), HttpStatus.NOT_FOUND);
        }

        TbEmployees existingEmployees = employee.get();

        if (updateEmployeeByAdminOrCompanyDTO.getName() != null && !updateEmployeeByAdminOrCompanyDTO.getName().isEmpty()) {
            existingEmployees.setName(updateEmployeeByAdminOrCompanyDTO.getName());
        }
        if (updateEmployeeByAdminOrCompanyDTO.getEmail() != null && !updateEmployeeByAdminOrCompanyDTO.getEmail().isEmpty()) {
            existingEmployees.setEmail(updateEmployeeByAdminOrCompanyDTO.getEmail());
        }

        if (updateEmployeeByAdminOrCompanyDTO.getCpf() != null && !updateEmployeeByAdminOrCompanyDTO.getCpf().isEmpty()) {
            TbEmployees employeeWithSameCpf = employeesRepository.findByCpf(updateEmployeeByAdminOrCompanyDTO.getCpf());
            if (employeeWithSameCpf != null && !employeeWithSameCpf.getId().equals(existingEmployees.getId())) {
                return new ResponseEntity<>(new ApiResponse("O campo CPF já existe"), HttpStatus.BAD_REQUEST);
            }
            existingEmployees.setCpf(updateEmployeeByAdminOrCompanyDTO.getCpf());
        }

        if (updateEmployeeByAdminOrCompanyDTO.getBirthDate() != null) {
            existingEmployees.setBirthDate(updateEmployeeByAdminOrCompanyDTO.getBirthDate());
        }

        if (updateEmployeeByAdminOrCompanyDTO.getCompany() != null) {
            Optional<TbCompany> company = companyRepository.findById(updateEmployeeByAdminOrCompanyDTO.getCompany().getId());
            if (company.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse("Empresa não encontrada"), HttpStatus.NOT_FOUND);
            }
            existingEmployees.setCompany(company.get());
        }

        if (updateEmployeeByAdminOrCompanyDTO.getRole() != null) {
            Optional<TbRoles> role = roleRepository.findById(updateEmployeeByAdminOrCompanyDTO.getRole().getId());
            if (role.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse("Erro ao trocar nível de acesso"), HttpStatus.NOT_FOUND);
            }
            existingEmployees.setRole(role.get());
        }

        if (updateEmployeeByAdminOrCompanyDTO.getPassword() != null && !updateEmployeeByAdminOrCompanyDTO.getPassword().trim().isEmpty()) {
            existingEmployees.setPassword(passwordEncoder.encode(updateEmployeeByAdminOrCompanyDTO.getPassword()));
        }

        employeesRepository.save(existingEmployees);
        return new ResponseEntity<>(new ApiResponse("Dados do Funcionário atualizados"), HttpStatus.OK);
    }


    public ResponseEntity<?> searchEmployeesByName(String name,Long companyId) {
        if (name.matches(".*\\d.*")) {
            return new ResponseEntity<>(new ApiResponse("Não é permitido usar números"), HttpStatus.BAD_REQUEST);
        }
        List<TbEmployees> employeesData = employeesRepository.searchingEmployeeNames(name,companyId);

        List<EmployeeDetailsDTO> employeeDTOs = employeesData.stream().map(employee -> {
            EmployeeDetailsDTO dto = new EmployeeDetailsDTO();
            dto.setId(employee.getId());
            dto.setName(employee.getName());
            dto.setEmail(employee.getEmail());
            dto.setCpf(employee.getCpf());
            dto.setCompanyId(employee.getCompany().getId());
            dto.setRoleId(employee.getRole().getId());
            dto.setRoleName(employee.getRole().getName());
            dto.setBirthDate(employee.getBirthDate());
            return dto;
        }).collect(Collectors.toList());

        return new ResponseEntity<>(employeeDTOs, HttpStatus.OK);
    }


    public ResponseEntity<?> searchAllEmployeesByCompanyId(Long id) {
        List<TbEmployees> employees = employeesRepository.searchingAllEmployeesByCompanyId(id);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }


    public ResponseEntity<?> searchEmployeeById(Long id) {
        Optional<TbEmployees> employee = employeesRepository.findById(id);
        if (employee.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("Nenhum funcionário encontrado"), HttpStatus.NOT_FOUND);
        }

        TbEmployees existingEmployee = employee.get();
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(existingEmployee.getId());
        employeeDTO.setName(existingEmployee.getName());
        employeeDTO.setBirthDate(existingEmployee.getBirthDate());
        employeeDTO.setEmail(existingEmployee.getEmail());
        employeeDTO.setCpf(existingEmployee.getCpf());
        employeeDTO.setCompanyId(existingEmployee.getCompany().getId());
        employeeDTO.setRole(existingEmployee.getRole());

        return new ResponseEntity<>(employeeDTO, HttpStatus.OK);
    }





}