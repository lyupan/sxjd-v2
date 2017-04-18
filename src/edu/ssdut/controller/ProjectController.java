package edu.ssdut.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import edu.ssdut.bean.ResultBean;
import edu.ssdut.model.Project;
import edu.ssdut.model.ProjectPhase;
import edu.ssdut.model.Student;
import edu.ssdut.model.Teacher;
import edu.ssdut.service.ProjectService;

@Controller
public class ProjectController {
	
	private String folder;
	
	@Autowired
	private ProjectService projectService;
	
	ProjectController() {
        Properties pps = new Properties();
        
        InputStream stream=Thread.currentThread().getContextClassLoader().getResourceAsStream("sxjd.properties");
        try {
			pps.load(stream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        folder = pps.getProperty("filefolder");
        if (folder == null) {
        	System.out.println("配置文件未指定上传文件存储位置. 默认位置");
        	folder = "/home/lyu/uploadFiles/";
        }
        System.out.println(folder);
	}
	
	//获取所有项目列表
	@RequestMapping(value="/projects", method=RequestMethod.GET)
	@ResponseBody
	public ResultBean<List<Project>> allProjects(HttpServletRequest request) {
		//验证用户身份
		//验证是否登录
		Object o = request.getAttribute("username");
		if (o == null)
			return ResultBean.failure("1004");
		if ( !(o instanceof String) )
			return ResultBean.failure("1004");
		
		List<Project> list = projectService.allProjects();
		return ResultBean.success(list);
	}
	
	//新增项目
	@RequestMapping(value="/projects", method=RequestMethod.POST)
	@ResponseBody
	public ResultBean<Project> addProject(String dateString, Project project, HttpServletRequest request) {
		//验证是否登录
		Object o = request.getAttribute("username");
		if (o == null)
			return ResultBean.failure("1004");
		if ( !(o instanceof String) )
			return ResultBean.failure("1004");
		String username = (String) o;
		
		if (project == null || project.getName() == null)
			return ResultBean.failure("2003");
		Date date;
		try {  
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		    date = sdf.parse(dateString);
		} catch (ParseException e) { 
			System.out.println(e.getMessage());
			date = new Date();
			//return ResultBean.failure("2003");
		}
		
		project.setBeginDate(date);
		Teacher t = new Teacher();
		t.setUsername(username);
		project.setTeacher(t);
		Project p = projectService.add(project);
		if (p == null)
			return ResultBean.failure("3002");
		return ResultBean.success(p);
	}
	
	@RequestMapping(value="/projects", method=RequestMethod.DELETE)
	@ResponseBody
	public ResultBean<Project> delProject(int id, HttpServletRequest request) {
		//验证是否登录
		Object o = request.getAttribute("username");
		if (o == null)
			return ResultBean.failure("1004");
		if ( !(o instanceof String) )
			return ResultBean.failure("1004");
		//String username = (String) o;
		
		Project p = projectService.searchById(id);
		if (p == null)
			return ResultBean.failure("2004");
		projectService.delete(id);
		return ResultBean.success(p);
	}
	
	@RequestMapping(value="/projects/{id:\\d+}", method=RequestMethod.GET)
	@ResponseBody
	public ResultBean<Project> getProject(@PathVariable int id, HttpServletRequest request) {
		Project p =  projectService.searchById(id);
		if (p == null)
			return ResultBean.failure("2004");
		return ResultBean.success(p);
	}

	//修改项目信息
	@RequestMapping(value="/projects/{id:\\d+}", method=RequestMethod.PUT)
	@ResponseBody
	public ResultBean<Project> updateProject(@PathVariable int id, String dateString, Project project, HttpServletRequest request) {
		Project ori = projectService.searchById(id);
		if (ori == null)
			return ResultBean.failure("2004");
		project.setId(id);
		project.setTeacher(ori.getTeacher());
		if (project.getName() == null)
			project.setName(ori.getName());
		
		if (project == null || project.getName() == null)
		return ResultBean.failure("2003");
		Date date;		
		try {  
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		    date = sdf.parse(dateString);
		} catch (ParseException e) { 
			System.out.println(e.getMessage());
			date = new Date();
		}
		project.setBeginDate(date);
		if (projectService.update(project))
			return ResultBean.success();
		return ResultBean.failure("1004");
	}
	
	@RequestMapping(value="fuzzysearch", method=RequestMethod.POST)
	@ResponseBody
	public ResultBean<List<Project>> fuzzySearch(String name, HttpServletRequest request) {
		if (name == null || name.equals(""))
			return ResultBean.failure("2003");
		name = "%" + name + "%";
		List<Project> list = projectService.fuzzySearch(name);
		return ResultBean.success(list);
	}
	
	@RequestMapping(value="/projects/{projectId:\\d+}/phases", method=RequestMethod.GET)
	@ResponseBody
	public ResultBean<List<ProjectPhase>> allPhases(@PathVariable int projectId, HttpServletRequest request) {
		Project p = projectService.searchById(projectId);
		if (p == null)
			return ResultBean.failure("2004");
		List<ProjectPhase> phases = projectService.allPhases(projectId);
		return ResultBean.success(phases);
	}
	
	@RequestMapping(value="/projects/{projectId:\\d+}/phases", method=RequestMethod.POST)
	@ResponseBody
	public ResultBean<ProjectPhase> addPhase(@PathVariable int projectId, ProjectPhase pp, HttpServletRequest request) {
		if ( pp==null || pp.getPhaseId() == 0 || pp.getName() == null)
			return ResultBean.failure("2003");
		if(pp.getProjectId() != projectId)
			pp.setProjectId(projectId);
		
		Project p = projectService.searchById(projectId);
		if (p == null)
			return ResultBean.failure("2004");
		if (pp.getPhaseId() == 0)
			return ResultBean.failure("2003");
//		
//		List<ProjectPhase> phases = projectService.allPhases(projectId);
//		if (phases.contains(pp))
//			return ResultBean.failure("2003");
		
		if (!projectService.addPhase(pp))
			return ResultBean.failure("2003");
		return ResultBean.success(pp);
	}
	
	@RequestMapping(value="/projects/{projectId:\\d+}/phases", method=RequestMethod.PUT)
	@ResponseBody
	public ResultBean<ProjectPhase> updatePhase(@PathVariable int projectId, ProjectPhase pp, HttpServletRequest request) {
		if ( pp==null || pp.getPhaseId() == 0 || pp.getName() == null)
			return ResultBean.failure("2003");
		if(pp.getProjectId() != projectId)
			pp.setProjectId(projectId);
		
		Project p = projectService.searchById(projectId);
		if (p == null)
			return ResultBean.failure("2004");
//		List<ProjectPhase> phases = projectService.allPhases(projectId);
//		if (!phases.contains(pp))
//			return ResultBean.failure("2003");
		
		if (!projectService.updatePhase(pp))
			return ResultBean.failure("2003");
		return ResultBean.success(pp);
	}
	
	@RequestMapping(value="/projects/{projectId:\\d+}/phases", method=RequestMethod.DELETE)
	@ResponseBody
	public ResultBean<ProjectPhase> delPhase(@PathVariable int projectId, int phaseId, HttpServletRequest request) {
		ProjectPhase pp = projectService.getPhase(projectId, phaseId);
		if (pp == null)
			return ResultBean.failure("2004");

		if (!projectService.deletePhase(projectId, phaseId))
			return ResultBean.failure("2003");
		
		//还应该删除服务器中的文件
		String path = folder + projectId + "/" + phaseId + "/";
		Util.delDir(path);
		return ResultBean.success(pp);
	}
	
	@RequestMapping(value="/projects/{projectId:\\d+}/members", method=RequestMethod.GET)
	@ResponseBody
	public ResultBean<List<Student>> allMembers(@PathVariable int projectId, HttpServletRequest request) {
		if (projectService.searchById(projectId) == null)
			return ResultBean.failure("2004");
		List<Student> list = projectService.allMembers(projectId);
		return ResultBean.success(list);
	}
	
	@RequestMapping(value="/projects/{projectId:\\d+}/members", method=RequestMethod.POST)
	@ResponseBody
	public ResultBean<Student> addMember(@PathVariable int projectId, String username, HttpServletRequest request) {
		if (projectService.searchById(projectId) == null)
			return ResultBean.failure("2004");
		if (username == null)
			return ResultBean.failure("2003");
		
		if (projectService.addMember(projectId, username)) {
			Student s = projectService.getMember(projectId, username);
			return ResultBean.success(s);
		}
		return ResultBean.failure("3001");
	}
	
	@RequestMapping(value="/projects/{projectId:\\d+}/members", method=RequestMethod.DELETE)
	@ResponseBody
	public ResultBean<Student> delMember(@PathVariable int projectId, String username, HttpServletRequest request) {
		if (projectService.searchById(projectId) == null)
			return ResultBean.failure("2004");
		if (username == null)
			return ResultBean.failure("2003");
		
		if (projectService.delMember(projectId, username)) {
			Student s = projectService.getMember(projectId, username);
			return ResultBean.success(s);
		}
		return ResultBean.failure("2001");
	}
	
	@RequestMapping(value = "/projects/{projectId}/{phaseId}/upload", method=RequestMethod.POST)
	@ResponseBody
    public ResultBean<?> upload(HttpServletRequest request, @PathVariable int projectId, @PathVariable int phaseId, @RequestParam(value = "file", required = false) MultipartFile file) {  
		ProjectPhase pp = projectService.getPhase(projectId, phaseId);
		if (pp == null)
			return ResultBean.failure("2003");
		
		System.out.println("开始上传");
        if (file == null) {
        	System.out.println("file is null");
        	return ResultBean.failure("2003");
        }
        
        String path = folder + projectId + "/" + phaseId + "/";
        String fileName = file.getOriginalFilename();
        
        System.out.println(path);
        File targetFile = new File(path, fileName);
        if(!targetFile.exists()){
            targetFile.mkdirs(); 
        }
        
        //保存
        try {  
            file.transferTo(targetFile);  
        } catch (Exception e) {  
            e.printStackTrace();
            return ResultBean.failure("3004");
        }
        pp.setFileName(fileName);
        projectService.updatePhase(pp);
        return ResultBean.success();
    }
	
	@RequestMapping(value = "/projects/{projectId}/{phaseId}/download", method=RequestMethod.GET)
	@ResponseBody
	public ResultBean<?> download(@PathVariable int projectId, @PathVariable int phaseId, HttpServletResponse response) {
        System.out.println("开始下载");
        
        ProjectPhase pp = projectService.getPhase(projectId, phaseId);
        if (pp == null)
        	return ResultBean.failure("2003");
        if (pp.getFileName() == null)
        	return ResultBean.failure("3003");
        
        String path = folder + projectId + "/" + phaseId + "/";
        String fileName = pp.getFileName();
        System.out.println("fileName : " + fileName);
        
        File file = new File(path + fileName);
        if (!file.exists())
        	return ResultBean.failure("3003");
        
        try {
			InputStream input = FileUtils.openInputStream(file);
			byte[] data = IOUtils.toByteArray(input);
			//response.reset();  
	        response.setHeader("content-disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));  
	        response.addHeader("Content-Length", "" + data.length);  
	        //response.setContentType("application/pdf; charset=UTF-8");	  
	        IOUtils.write(data, response.getOutputStream());			
		} catch (IOException e) {
			e.printStackTrace();
			return ResultBean.failure("3004");
		}        
        return ResultBean.success();
	}
}
