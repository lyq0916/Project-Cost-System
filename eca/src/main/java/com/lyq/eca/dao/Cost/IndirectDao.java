package com.lyq.eca.dao.Cost;

import com.lyq.eca.pojo.Cost.Indirect;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IndirectDao extends JpaRepository<Indirect,Integer> {
    List<Indirect> findAll();
    List<Indirect> findByMid(int mid);
}
