package com.lyq.eca.service;

import com.lyq.eca.dao.ConstLogDao;
import com.lyq.eca.dao.Project.ProjectDao;
import com.lyq.eca.pojo.ConstLog;
import com.lyq.eca.pojo.ConstLogBean;
import com.lyq.eca.pojo.Project.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ConstLogService {
    @Autowired
    ConstLogDao constLogDao;
    @Autowired
    ProjectDao projectDao;

    public void add(ConstLog constLog){
        constLogDao.save(constLog);
    }

    public List<ConstLogBean> findAll() {
        List<ConstLog> constLogs = constLogDao.findAll();
        List<ConstLogBean> constLogBeans = new ArrayList<>();
        constLogBeans.addAll(transform(constLogs));
        return constLogBeans;
    }

    public List<ConstLogBean> findByPname(String pname){
        List<ConstLogBean> constLogBeans=new ArrayList<>();
        List<Project> projects=projectDao.search(pname);
        for(int i=0;i<projects.size();i++){
            List<ConstLog> constLogs=constLogDao.findByPid(projects.get(i).getPid());
            constLogBeans.addAll(transform(constLogs));
        }
        return constLogBeans;
    }

    public List<ConstLogBean> findByDate(Date date){
        List<ConstLog> constLogs=constLogDao.findByDate(date);
        return transform(constLogs);
    }

    public List<ConstLogBean> findByPnameAndDate(String pname,Date date){
        List<ConstLogBean> constLogBeans=new ArrayList<>();
        List<Project> projects=projectDao.search(pname);
        for(int i=0;i<projects.size();i++){
            List<ConstLog> constLogs=constLogDao.findByPidAndDate(projects.get(i).getPid(),date);
            constLogBeans.addAll(transform(constLogs));
        }
        return constLogBeans;
    }

    public List<ConstLogBean> transform(List<ConstLog> constLogs){
        List<ConstLogBean> constLogBeans=new ArrayList<>();
        for(int i=0;i<constLogs.size();i++){
            ConstLogBean constLogBean = new ConstLogBean();
            constLogBean.setId(constLogs.get(i).getId());
            constLogBean.setPid(constLogs.get(i).getPid());
            String pname = projectDao.findByPid(constLogs.get(i).getPid()).getPname();
            constLogBean.setPname(pname);
            constLogBean.setNote(constLogs.get(i).getNote());
            constLogBean.setDate(constLogs.get(i).getDate());
            constLogBean.setPicture(constLogs.get(i).getPicture());
            constLogBeans.add(constLogBean);
        }
        return constLogBeans;
    }
}
