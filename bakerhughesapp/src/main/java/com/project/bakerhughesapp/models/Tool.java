package com.project.bakerhughesapp.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name= "tool")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tool {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   // khi insert vao thi se autoincrement by 1
    private Long id;

    @Column(name = "sensor_offset")
    private Float sensorOffset;

    @Column(name = "default_bur")
    private Float defaultBUR;

}
