package com.lyq.eca.dao.Project;

import com.lyq.eca.pojo.Project.BidProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface BidProjectDao extends JpaRepository<BidProject,Integer> {
    List<BidProject> findAll();
    List<BidProject> findByState(String state);

    @Transactional
    @Modifying
    @Query(value = "update bid_project set state=?1 where pid=?2",nativeQuery = true)
    void updateStateByPid(String state,int pid);
}
