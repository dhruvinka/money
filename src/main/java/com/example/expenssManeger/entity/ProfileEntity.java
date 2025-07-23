package com.example.expenssManeger.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor@NoArgsConstructor
@Builder
@Table(name = "tbl_profile")
public class ProfileEntity
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    private  String  email;
    private  String password;
    private String profileImageUrl;
    private String activationToken;
    private String  fullName;
    private Boolean isActive;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updatedAt;



    @PrePersist
public void prePersist()
{
    if (this.isActive == null)
    {
        isActive=false;
    }
}

}
