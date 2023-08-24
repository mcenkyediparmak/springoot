package com.example.springoot.models;

import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PosessionsHelper
{
    private long amount;
    private String company;
}
