package com.example.springoot.models;
import jakarta.persistence.*;
import lombok.*;
import com.example.springoot.models.loginCredentials;
import com.example.springoot.models.Company;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="possessions")
public class Posessions {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "amount")
    private long amount;

    @Column(name = "cost")
    private float cost;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private loginCredentials user;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
}
