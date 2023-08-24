package com.example.springoot.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @Column(name = "free_share")
    private long free_share;
    @Column(name = "worth_per_share")
    private float worth_per_share;
    @Column(name = "old_worth_per_share")
    private float old_worth_per_share;


}
