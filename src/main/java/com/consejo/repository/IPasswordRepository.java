package com.consejo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.consejo.pojos.Password;

@Repository
public interface IPasswordRepository extends JpaRepository <Password,Integer> {

}
