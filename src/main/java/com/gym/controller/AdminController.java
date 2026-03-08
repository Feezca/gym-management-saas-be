package com.gym.controller;

import com.gym.dto.request.*;
import com.gym.dto.response.ExerciseResponse;
import com.gym.dto.response.MemberResponse;
import com.gym.dto.response.ProductResponse;
import com.gym.dto.response.TrainerResponse;
import com.gym.dto.response.UserResponse;
import com.gym.service.tenant.ExerciseService;
import com.gym.service.tenant.MemberService;
import com.gym.service.tenant.ProductService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
@Slf4j
public class AdminController {

    private final MemberService memberService;
    private final ProductService productService;
    private final ExerciseService exerciseService;

    public AdminController(MemberService memberService, ProductService productService, ExerciseService exerciseService) {
        this.memberService = memberService;
        this.productService = productService;
        this.exerciseService = exerciseService;
    }

    //staff management can be added here in future
    @PostMapping("/staff")
    public UserResponse createStaff(@Valid @RequestBody CreateTrainerRequest request) {
        return memberService.createTrainer(request);
    }

    @GetMapping("/staff/search")
    public Page<TrainerResponse> searchStaff( @ModelAttribute BaseFilterDto filter) {
        return memberService.searchStaff(filter);
    }

    @DeleteMapping("/staff/{id}")
    public ResponseEntity<Void> deleteTrainer(@PathVariable Long id) {
        memberService.deleteTrainer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/staff")
    public List<TrainerResponse> getAllStaff() {
        return memberService.getAllTrainers();
    }
    // Member Management
    @PostMapping("/members")
    public MemberResponse createMember(@Valid @RequestBody CreateMemberRequest request) {
        return memberService.createMember(request);
    }

    @GetMapping("/members/{id}")
    public ResponseEntity<MemberResponse> getMemberById(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getMemberById(id));
    }

    @PutMapping("/members/{id}")
    public ResponseEntity<MemberResponse> updateMember(@PathVariable Long id,
                                                        @Valid @RequestBody CreateMemberRequest request) {
        return ResponseEntity.ok(memberService.updateMember(request));
    }


    @DeleteMapping("/members/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }


    // Product Management
    @PostMapping("/products")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody CreateProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(request));
    }

    @GetMapping("/products")
    public ResponseEntity<Page<ProductResponse>> getAllProducts(Pageable pageable) {
        return ResponseEntity.ok(productService.getAllProducts(pageable));
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id,
                                                          @Valid @RequestBody CreateProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // Exercise Management
    @PostMapping("/exercises")
    public ResponseEntity<ExerciseResponse> createExercise(@Valid @RequestBody CreateExerciseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(exerciseService.createExercise(request));
    }

    @GetMapping("/exercises")
    public ResponseEntity<Page<ExerciseResponse>> getAllExercises(Pageable pageable) {
        return ResponseEntity.ok(exerciseService.getAllExercises(pageable));
    }

    @GetMapping("/exercises/{id}")
    public ResponseEntity<ExerciseResponse> getExerciseById(@PathVariable Long id) {
        return ResponseEntity.ok(exerciseService.getExerciseById(id));
    }

    @PutMapping("/exercises/{id}")
    public ResponseEntity<ExerciseResponse> updateExercise(@PathVariable Long id,
                                                          @Valid @RequestBody CreateExerciseRequest request) {
        return ResponseEntity.ok(exerciseService.updateExercise(id, request));
    }

    @DeleteMapping("/exercises/{id}")
    public ResponseEntity<Void> deleteExercise(@PathVariable Long id) {
        exerciseService.deleteExercise(id);
        return ResponseEntity.noContent().build();
    }
}
