package com.dat.ecommerce.repository;

import com.dat.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Trong Spring Data JPA, chỉ cần định nghĩa interface, Spring sẽ tự động tạo ra một lớp cài đặt (implementation) ngầm bằng bytecode generation dựa trên các phương thức bạn khai báo.
// Kế thừa JpaRepository để sở hữu các phương thức thao tác với database (save, findById, findAll, deleteById, count,...)
// Cú pháp User: Tên entity mà reponsitory quản lý, Long: kiểu dữ liệu khóa chính (ID)
// Chỉ cần viết chữ kỹ hàm (method signature) là đủ, code sẽ tự động sinh ra từ đó thông qua tính năng Query Methods của Spring Data JPA
@Repository
public interface UserRepository extends JpaRepository<User, Long>,
        JpaSpecificationExecutor<User> {


    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

}