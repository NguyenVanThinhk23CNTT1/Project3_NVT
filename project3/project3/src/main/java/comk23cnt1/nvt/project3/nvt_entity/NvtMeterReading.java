package comk23cnt1.nvt.project3.nvt_entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(
        name = "meter_readings",
        uniqueConstraints = @UniqueConstraint(name = "uq_meter_room_month_year", columnNames = {"room_id", "bill_month", "bill_year"})
)
public class NvtMeterReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="room_id", nullable = false)
    private Long roomId;

    @Column(name="bill_month", nullable = false)
    private Integer billMonth;

    @Column(name="bill_year", nullable = false)
    private Integer billYear;

    @Column(name="electric_old", nullable = false)
    private Integer electricOld;

    @Column(name="electric_new", nullable = false)
    private Integer electricNew;

    @Column(name="water_old", nullable = false)
    private Integer waterOld;

    @Column(name="water_new", nullable = false)
    private Integer waterNew;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (electricOld == null) electricOld = 0;
        if (electricNew == null) electricNew = 0;
        if (waterOld == null) waterOld = 0;
        if (waterNew == null) waterNew = 0;
    }
}
