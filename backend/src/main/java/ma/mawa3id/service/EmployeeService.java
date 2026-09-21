package ma.mawa3id.service;

import lombok.RequiredArgsConstructor;
import ma.mawa3id.domain.entity.Employee;
import ma.mawa3id.domain.entity.User;
import ma.mawa3id.domain.enums.Role;
import ma.mawa3id.dto.employee.EmployeeRequest;
import ma.mawa3id.dto.employee.EmployeeResponse;
import ma.mawa3id.exception.DuplicateResourceException;
import ma.mawa3id.exception.ResourceNotFoundException;
import ma.mawa3id.repository.EmployeeRepository;
import ma.mawa3id.repository.UserRepository;
import ma.mawa3id.tenant.TenantContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<EmployeeResponse> getEmployees() {
        Long businessId = TenantContext.getCurrentBusinessId();
        List<Employee> employees = employeeRepository.findByBusinessId(businessId);
        return employees.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        Long businessId = TenantContext.getCurrentBusinessId();

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Un utilisateur existe déjà avec cet email");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .passwordHash(passwordEncoder.encode(UUID.randomUUID().toString().substring(0, 8)))
                .role(Role.EMPLOYEE)
                .businessId(businessId)
                .active(request.isActive())
                .build();
        user = userRepository.save(user);

        Employee employee = Employee.builder()
                .userId(user.getId())
                .businessId(businessId)
                .title(request.getTitle())
                .color(request.getColor() != null ? request.getColor() : "#1B5E3C")
                .active(request.isActive())
                .build();

        return toResponse(employeeRepository.save(employee), user);
    }

    @Transactional
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest request) {
        Long businessId = TenantContext.getCurrentBusinessId();
        Employee employee = employeeRepository.findById(id)
                .filter(e -> e.getBusinessId().equals(businessId))
                .orElseThrow(() -> new ResourceNotFoundException("Collaborateur introuvable"));

        User user = userRepository.findById(employee.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setActive(request.isActive());
        userRepository.save(user);

        employee.setTitle(request.getTitle());
        if (request.getColor() != null) employee.setColor(request.getColor());
        employee.setActive(request.isActive());

        return toResponse(employeeRepository.save(employee), user);
    }

    @Transactional
    public void deleteEmployee(Long id) {
        Long businessId = TenantContext.getCurrentBusinessId();
        Employee employee = employeeRepository.findById(id)
                .filter(e -> e.getBusinessId().equals(businessId))
                .orElseThrow(() -> new ResourceNotFoundException("Collaborateur introuvable"));

        employee.setActive(false);
        employeeRepository.save(employee);
    }

    private EmployeeResponse toResponse(Employee employee) {
        User user = userRepository.findById(employee.getUserId()).orElse(null);
        return toResponse(employee, user);
    }

    private EmployeeResponse toResponse(Employee employee, User user) {
        return EmployeeResponse.builder()
                .id(employee.getId())
                .publicId(employee.getPublicId())
                .userId(employee.getUserId())
                .firstName(user != null ? user.getFirstName() : "")
                .lastName(user != null ? user.getLastName() : "")
                .fullName(user != null ? user.getFullName() : "")
                .email(user != null ? user.getEmail() : "")
                .phone(user != null ? user.getPhone() : "")
                .title(employee.getTitle())
                .color(employee.getColor())
                .active(employee.isActive())
                .build();
    }
}
