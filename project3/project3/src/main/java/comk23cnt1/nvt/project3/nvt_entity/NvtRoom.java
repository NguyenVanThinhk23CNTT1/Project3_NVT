package comk23cnt1.nvt.project3.nvt_entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "rooms")
public class NvtRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="room_code", nullable = false, unique = true, length = 20)
    private String roomCode;

    @Column(name="room_name", length = 50)
    private String roomName;

    private Integer floor;

    private BigDecimal area;

    @Column(name="max_people")
    private Integer maxPeople;

    @Column(name="rent_price", nullable = false)
    private BigDecimal rentPrice;

    @Column(name="deposit_default")
    private BigDecimal depositDefault;

    @Enumerated(EnumType.STRING)
    private RoomStatus status;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    public enum RoomStatus { EMPTY, RENTING, REPAIR }


    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (this.createdAt == null) this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hostel_id")
    private NvtHostel hostel;


    @Column(name = "image_url", length = 255)
    private String imageUrl;

}
