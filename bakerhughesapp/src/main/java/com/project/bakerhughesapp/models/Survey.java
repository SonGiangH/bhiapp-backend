package com.project.bakerhughesapp.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "surveys")
@Data // To String
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Survey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   // khi insert vao thi se autoincrement by 1
    private Long id;

    @Column(name = "dp_length")
    private Float dpLength;

    @Column(name = "bit_depth")
    private Float bitDepth;

    @Column(name = "survey_depth", nullable = false)
    private Float surveyDepth;

    @Column(name = "inc", nullable = false)
    private Float inc;

    @Column(name = "azi", nullable = false)
    private Float azi;

    @Column(name = "totalseen")
    private Float totalSeen;

    @Column(name = "dlswa", nullable = false)
    private Float dlsWa;

    @Column(name = "dls30m")
    private Float dls30m;

    @Column(name = "motoryield")
    private Float motorYield;

    @Column(name = "toolface")
    private String toolFace;

    @Column(name = "st")
    private Float st;

    @Column(name = "ed")
    private Float ed;

    @Column(name = "totalslid")
    private Float totalSlid;

    @Column(name = "slidseen")
    private Float slidSeen;

    @Column(name = "slidunseen")
    private Float slidUnseen;

    @Column(name = "meterahead")
    private Float meterAhead;
}
