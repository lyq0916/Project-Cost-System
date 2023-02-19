package com.lyq.eca.dao;

import com.lyq.eca.pojo.ConstLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ConstLogDao extends JpaRepository<ConstLog,Integer> {
    List<ConstLog> findAll();
    List<ConstLog> findByPid(int pid);
    List<ConstLog> findByDate(Date date);
    List<ConstLog> findByPidAndDate(int pid,Date date);
}
