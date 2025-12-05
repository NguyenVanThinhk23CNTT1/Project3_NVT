package comk23cnt1.nvt.project3.nvt_entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "users")
public class NvtUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String username;

    // ✅ DB đang là password_hash
    @Column(name = "password_hash", nullable = false, length = 255)
    private String password;

    @Column(name = "full_name", length = 100)
    private String fullName;

    @Column(length = 20, unique = true)
    private String phone;

    @Column(length = 100, unique = true)
    private String email;

    // ✅ DB role là ENUM('ADMIN','USER') => map STRING
    @Column(nullable = false)
    private String role; // "ADMIN" hoặc "USER"

    @Column(nullable = false)
    private Boolean enabled;

    // ✅ DB status là ENUM('ACTIVE','LOCKED') => map STRING
    @Column(nullable = false)
    private String status; // "ACTIVE" hoặc "LOCKED"

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;


    @PrePersist
    public void prePersist() {
        if (enabled == null) enabled = true;
        if (role == null || role.isBlank()) role = "ADMIN";
        if (status == null || status.isBlank()) status = "ACTIVE";
    }
}
