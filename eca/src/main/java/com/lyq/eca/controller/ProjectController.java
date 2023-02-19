package com.lyq.eca.controller;

import com.lyq.eca.pojo.*;
import com.lyq.eca.pojo.Project.BidBean;
import com.lyq.eca.pojo.Project.ConstBean;
import com.lyq.eca.pojo.Project.ConstProject;
import com.lyq.eca.pojo.Project.Project;
import com.lyq.eca.result.Result;
import com.lyq.eca.service.ConstLogService;
import com.lyq.eca.service.CostMaintainService;
import com.lyq.eca.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class ProjectController {
    /*
     * 每个service类都要加上@Autowired ！！！
     * */
    @Autowired
    ProjectService projectService;
    @Autowired
    CostMaintainService costMaintainService;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    ConstLogService constLogService;

    //项目报备
    @CrossOrigin
    @PostMapping(value = "api/projectadd")
    @ResponseBody
    public Result projectAdd(@RequestBody Project project) {
        try {
            projectService.add(project);
            return new Result(200);
        } catch (Exception e) {
            return new Result(300);
        }
    }

    //request错误分析
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public String messageNotReadable(HttpMessageNotReadableException exception, HttpServletResponse response) {
        System.out.println(exception);
        return "";
    }

    @CrossOrigin
    @GetMapping(value = "api/getprojectbyid")
    @ResponseBody
    public Project getProjectById(@RequestParam("pid") String pid) {
        //System.out.println(pid);
        return projectService.findByPid(Integer.parseInt(pid));
    }

    @CrossOrigin
    @GetMapping(value = "api/getprojectbyuid")
    @ResponseBody
    public List<Project> getProjectByUid(@RequestParam("uid") String uid){
        return projectService.findByHeader(Integer.parseInt(uid));
    }

    //项目详情展示
    @CrossOrigin
    @GetMapping(value = "api/projectshow")
    @ResponseBody
    public List<Project> projectshow() {
        try {
            List<Project> p = new ArrayList<>();
            p = projectService.findall();
            //System.out.println(p.get(0).toString());
            return p;
        } catch (Exception e) {
            return null;
        }
    }

    //模糊查询项目
    @CrossOrigin
    @GetMapping(value = "api/searchproject")
    @ResponseBody
    public List<Project> searchproject(@RequestParam("pname") String pname) {
        if ("".equals(pname)) {
            return projectService.findall();
        } else {
            //return projectService.findByPnameLikeOrAddressLike(pname);
            return projectService.search(pname);
        }
    }

    //成本支出

    //增加成本支出记录
    @CrossOrigin
    @PostMapping(value = "api/costadd")
    @ResponseBody
    public Result costadd(@RequestBody CostMaintain costMaintain) {
        try {
            //System.out.println(costMaintain.toString());
            costMaintainService.add(costMaintain);
            return new Result(200);
        } catch (Exception e) {
            System.out.println(e);
            return new Result(400);
        }
    }

    //展示成本支出记录
    @CrossOrigin
    @GetMapping(value = "api/costshow")
    @ResponseBody
    public List<CostMaintain> costshow() {
        try {
            List<CostMaintain> c = costMaintainService.findAll();
            return c;
        } catch (Exception e) {
            return null;
        }
    }

    @CrossOrigin
    @GetMapping(value = "api/findByPidAndType")
    @ResponseBody
    public List<CostMaintain> findByPidAndType(@RequestParam("pid")String pid,@RequestParam("type")String type){
        return costMaintainService.findByPidAndType(Integer.parseInt(pid),type);
    }

    //根据项目名和日期查询支出记录 ---项目名和日期可能为空--麻烦，要判断下下
    @CrossOrigin
    @GetMapping(value = "api/searchcost")
    @ResponseBody
    public List<CostMaintain> searchcost(@RequestParam("pname") String pname, @RequestParam("date") String date) throws ParseException {
        System.out.println(pname);
        List<CostMaintain> c = new ArrayList<>();
        if ("".equals(pname) && (date.equals("null") || "".equals(date))) {
            return costMaintainService.findAll();
        } else if (!("".equals(pname)) && (date.equals("null") || "".equals(date))) {
            List<Project> p = projectService.search(pname);
            int pid;
            for (int i = 0; i < p.size(); i++) {
                pid = p.get(i).getPid();
                c.addAll(costMaintainService.findByPid(pid));
            }
            return c;
        } else if ("".equals(pname) && !(date.equals("null") || "".equals(date))) {
            SimpleDateFormat trans = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = trans.parse(date);
            return costMaintainService.findByUpdatedate(date1);
        } else {
            /*日期格式转换*/
            SimpleDateFormat trans = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = trans.parse(date);
            List<Project> p = projectService.search(pname);
            int pid;
            for (int i = 0; i < p.size(); i++) {
                pid = p.get(i).getPid();
                c.addAll(costMaintainService.findByPidAndUpdatedate(pid, date1));
            }
            //System.out.println(c);
            return c;
        }
    }

    //施工日志

    //添加施工日志
    @CrossOrigin
    @PostMapping(value = "api/logadd")
    @ResponseBody
    public Result logadd(@RequestBody ConstLog constLog){
        try{
            constLogService.add(constLog);
            return new Result(200);
        }catch (Exception e){
            return new Result(400);
        }
    }

    //展示施工日志
    @CrossOrigin
    @GetMapping(value = "api/logshow")
    @ResponseBody
    public List<ConstLogBean> logshow(){
        return constLogService.findAll();
    }

    //搜索展示施工日志
    @CrossOrigin
    @GetMapping(value = "api/searchlog")
    @ResponseBody
    public List<ConstLogBean> searchlog(@RequestParam("pname") String pname, @RequestParam("date") String date) throws ParseException {
        List<ConstLogBean> c = new ArrayList<>();
        if ("".equals(pname) && (date.equals("null") || "".equals(date))) {
            //输入的项目名和日期都为空
            return constLogService.findAll();
        } else if (!("".equals(pname)) && (date.equals("null") || "".equals(date))) {
            //根据项目名搜索
           return constLogService.findByPname(pname);
        } else if ("".equals(pname) && !(date.equals("null") || "".equals(date))) {
            //根据日期搜索
            SimpleDateFormat trans = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = trans.parse(date);
            return constLogService.findByDate(date1);
        } else {
            /*日期格式转换*/
            SimpleDateFormat trans = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = trans.parse(date);
            return constLogService.findByPnameAndDate(pname,date1);
        }
    }


    //展示图表相关

    //不同项目类型
    @CrossOrigin
    @GetMapping(value = "api/groupbytype")
    @ResponseBody
    public List<Object[]> groupbytype() {
        return projectService.groupByType();
    }

    //今日支出 --按支出类型分
    @CrossOrigin
    @GetMapping(value = "api/costtype")
    @ResponseBody
    public List<Object[]> costtype() {
        return costMaintainService.findByCurdate();
    }

    //地图
    @CrossOrigin
    @GetMapping(value = "api/getaddress")
    @ResponseBody
    public Map<String, Integer> getaddress() {
        return projectService.getAddress();
    }

    //每个项目支出多少钱
    @CrossOrigin
    @GetMapping(value = "api/costgroupByPid")
    @ResponseBody
    public List<Object[]> costgroupByPid() {
        return costMaintainService.costgroupByPid();
    }

    //每个状态的项目有多少个
    @CrossOrigin
    @GetMapping(value = "api/groupbystate")
    @ResponseBody
    public List<Object[]> groupByState() {
        return projectService.groupByState();
    }

    //每个项目 --不同类型支出
    @CrossOrigin
    @GetMapping(value = "api/costtypebypid")
    @ResponseBody
    public List<Object[]> costtypeByPid(@RequestParam("pid")String pid){
        if("".equals(pid)){
            return null;
        }else {
            return costMaintainService.groupByTypeFroPid(Integer.parseInt(pid));
        }
    }

    //折线图实现 ---成本累加 ---分类型
    @CrossOrigin
    @GetMapping(value = "api/getSumByDateAndType")
    @ResponseBody
    public Map<String,List<Map<String,Object>>> get(@RequestParam("pid") String pid){
        HashMap<String,List<Map<String, Object>>> map=new HashMap<>();
        int id=Integer.parseInt(pid);
        List<Map<String,Object>> m= simpleQuery(id,"材料");
        map.put("材料",m);
        List<Map<String,Object>> e= simpleQuery(id,"机械设备");
        map.put("机械设备",e);
        List<Map<String,Object>> l=simpleQuery(id,"人工");
        map.put("人工",l);
        List<Map<String,Object>> o=simpleQuery(id,"其他");
        map.put("其他",o);
        List<Map<String,Object>> i=simpleQuery(id,"间接支出");
        map.put("间接支出",i);
        return map;
    }

    public List<Map<String,Object>> simpleQuery(int pid,String type){
        return jdbcTemplate.queryForList("select update_date as date,(@s:=@s+sum) as sum from (select update_date,sum(money) as sum from cost_maintain where pid=? and type=? group by update_date) as a,(select @s:=0) as b",pid,type);
    }

    //折线图  ---按照时间累加  --总成本总预算
    @CrossOrigin
    @GetMapping(value = "api/getSumByDate")
    @ResponseBody
    public List<Map<String,Object>> getSumByDate(@RequestParam("pid") String pid){
        int id=Integer.parseInt(pid);
        List<Map<String,Object>> m=sQuery1(id);
        System.out.println(m);
        return m;
    }

    public List<Map<String,Object>> sQuery1(int pid){
        return jdbcTemplate.queryForList(" select update_date as date,convert((@s:=@s+sum),DECIMAL(10,2)) as sum from (select update_date,CONVERT(sum(money),DECIMAL(10,2)) as sum from cost_maintain where pid=? group by update_date order by update_date) as a,(select @s:=0) as b",pid);
    }

    //双柱状图实现 ---实际支出、预算
    @CrossOrigin
    @GetMapping(value = "api/getcbByType")
    @ResponseBody
    public Map<String,Map<String,Object>> getcbByType(@RequestParam("pid") String pid){
        int id=Integer.parseInt(pid);
        Map<String,Map<String,Object>> m=new HashMap<>();
        Project p=projectService.findByPid(id);
        Map<String,Object> cost=sQuery2(id);
        //材料 --预算、实际
        Map<String,Object> mmap=new HashMap<>();
        mmap.put("预算",p.getMbudget());
        mmap.put("实际",cost.get("材料"));
        m.put("材料",mmap);

        //机械设备
        Map<String,Object> emap=new HashMap<>();
        emap.put("预算",p.getEbudget());
        emap.put("实际",cost.get("机械设备"));
        m.put("机械设备",emap);

        //人工
        Map<String,Object> lmap=new HashMap<>();
        lmap.put("预算",p.getLbudget());
        lmap.put("实际",cost.get("人工"));
        m.put("人工",lmap);

        //其他
        Map<String,Object> omap=new HashMap<>();
        omap.put("预算",p.getObudget());
        omap.put("实际",cost.get("其他"));
        m.put("其他",omap);

        //间接支出
        Map<String,Object> imap=new HashMap<>();
        imap.put("预算",p.getIbudget());
        imap.put("实际",cost.get("间接支出"));
        m.put("间接支出",imap);

        return m;
    }

    public Map<String, Object> sQuery2(int pid){
        Map<String, Object> map=new HashMap<>();
        List<Map<String,Object>> l= jdbcTemplate.queryForList("select type,sum(money) from cost_maintain where pid=? group by type",pid);
        //List<Map<String,Object>>形式转换为Map
        for(int i=0;i<l.size();i++){
            map.put(l.get(i).get("type").toString(),l.get(i).get("sum(money)"));
        }
        return map;
    }

    //表格相关

    //投标项目管理
    @CrossOrigin
    @GetMapping(value = "api/getbidbeans")
    @ResponseBody
    public List<BidBean> getbidbeans() {
        return projectService.getBidBeans();
    }

    @CrossOrigin
    @GetMapping(value = "api/getbidbeans1")
    @ResponseBody
    public List<BidBean> getbidbeans1() {
        return projectService.getBidBeans1();
    }


    //建设中项目管理
    @CrossOrigin
    @GetMapping(value = "api/getconstbeans")
    @ResponseBody
    public List<ConstBean> getconstbeans() {
        return projectService.getConstBeans();
    }

    @CrossOrigin
    @GetMapping(value = "api/findConstByPid")
    @ResponseBody
    public ConstProject findConstByPid(@RequestParam("pid")String pid){
        return projectService.findConstByPid(Integer.parseInt(pid));
    }

    @CrossOrigin
    @GetMapping(value = "api/getconstbeans1")
    @ResponseBody
    public List<ConstBean>  getconstbeans1() {
        return projectService.getConstBeans1();
    }

    @CrossOrigin
    @GetMapping(value = "api/getcompletebeans")
    @ResponseBody
    public List<ConstBean> getcompletebeans(){
        return projectService.getCompleteBeans();
    }

    //更改投标项目状态
    @CrossOrigin
    @GetMapping(value = "api/updatebp")
    @ResponseBody
    public Result updatebp(@RequestParam("pid") String pid, @RequestParam("state") String state) {
        try {
            System.out.println(state);
            projectService.updateBidProject(state, Integer.parseInt(pid));
            return new Result(200);
        } catch (Exception e) {
            System.out.println(e);
            return new Result(400);
        }
    }

    //开工大吉！！
    //设置预计开工完工日期
    @CrossOrigin
    @GetMapping(value = "api/addconst")
    @ResponseBody
    public Result addconst(@RequestParam("pid") String pid, @RequestParam("start") String start, @RequestParam("end") String end) {
        try {
            SimpleDateFormat trans = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = trans.parse(start);
            Date date2 = trans.parse(end);
            projectService.addConstProject(Integer.parseInt(pid), date1, date2);
            return new Result(200);
        } catch (Exception e) {
            System.out.println(e);
            return new Result(400);
        }
    }

    //正式开工！！
    @CrossOrigin
    @GetMapping(value = "api/start")
    @ResponseBody
    public Result start(@RequestParam("pid") String pid, @RequestParam("start") String start) throws ParseException {
        try {
            //System.out.println(start);
            SimpleDateFormat trans = new SimpleDateFormat("yyyy-MM-dd");
            Date date = trans.parse(start);
            //System.out.println(date);
            projectService.startConstProject(date, Integer.parseInt(pid));
            return new Result(200);
        } catch (Exception e) {
            System.out.println(e);
            return new Result(400);
        }
    }

    //完工！！！
    @CrossOrigin
    @GetMapping(value = "api/end")
    @ResponseBody
    public Result end(@RequestParam("pid") String pid, @RequestParam("end") String end) throws ParseException {
        try {
            SimpleDateFormat trans = new SimpleDateFormat("yyyy-MM-dd");
            Date date = trans.parse(end);
            projectService.endConstProject(date, Integer.parseInt(pid));
            return new Result(200);
        } catch (Exception e) {
            return new Result(400);
        }
    }
}