package com.orderflow.repository;

import com.orderflow.model.Contem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContemRepository extends JpaRepository<Contem, Integer> {

    List<Contem> findByIdPedido(Integer idPedido);

}