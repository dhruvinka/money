package com.example.expenssManeger.Repository;

import com.example.expenssManeger.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepo extends JpaRepository<ProfileEntity,Long>

{


Optional<ProfileEntity> findByEmail(String email);

Optional<ProfileEntity> findByActivationToken(String activationToken);



}
