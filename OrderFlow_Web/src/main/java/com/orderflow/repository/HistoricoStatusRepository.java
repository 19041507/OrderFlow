package com.orderflow.repository;

import com.orderflow.model.HistoricoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricoStatusRepository extends JpaRepository<HistoricoStatus, Integer> {

    List<HistoricoStatus> findByIdPedido(Integer idPedido);

}
