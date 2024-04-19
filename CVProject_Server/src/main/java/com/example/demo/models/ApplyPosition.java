package com.example.demo.models;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "apply_position")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplyPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
}
