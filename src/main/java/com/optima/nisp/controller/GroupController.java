package com.optima.nisp.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.optima.nisp.constanta.CommonCons;
import com.optima.nisp.model.Group;
import com.optima.nisp.model.api.GroupApi;
import com.optima.nisp.response.GroupResponse;
import com.optima.nisp.response.Response;
import com.optima.nisp.service.GroupService;
import com.optima.nisp.utility.CommonUtils;

@Controller
@RequestMapping(value="group")
@SessionAttributes("userSession")
public class GroupController {
	private static final Logger logger = Logger.getLogger(GroupController.class);
	
	@Autowired
	private GroupService groupService;
	
	@ResponseBody
	@RequestMapping(value="get", method=RequestMethod.GET)
	public Response getGroup(Model model){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){
				List<Group> groups = groupService.getAllGroups();
				resp.setResultCode(1000);
				resp.setMessage("Sukses get data");
				resp.setData(groups);
			} else {
				resp.setResultCode(5001);
				resp.setMessage("No Session");
			}	
			
			
			return resp;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value="get-with-checked", method=RequestMethod.GET)
	public Response getGroupWithChecked(Model model, @RequestParam("menuId") Long menuId){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){
				List<GroupResponse> groups = groupService.getGroupsWithChecked(menuId);
				resp.setResultCode(1000);
				resp.setMessage("Sukses get data");
				resp.setData(groups);
			} else {
				resp.setResultCode(5001);
				resp.setMessage("No Session");
			}	
			
			return resp;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value="delete", method=RequestMethod.DELETE)
	public Response deleteGroup(Model model, HttpServletRequest request, @RequestParam("groupId") Long groupId){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){			
				groupService.deleteById(groupId);
				resp.setResultCode(1000);
				resp.setMessage("Sukses hapus data");
			} else {
				resp.setResultCode(5001);
				resp.setMessage("No Session");
			}	
			
			return resp;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value="add", method=RequestMethod.POST)
	public Response deleteGroup(Model model, HttpServletRequest request, @RequestBody GroupApi groupApi){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){			
				groupService.insertGroupWithMenus(groupApi);
				resp.setResultCode(1000);
				resp.setMessage("Sukses tambah data");
			} else {
				resp.setResultCode(5001);
				resp.setMessage("No Session");
			}		
			
			return resp;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	@RequestMapping(value="add-page", method=RequestMethod.GET)
	public ModelAndView addGroupPage(Model model, HttpServletRequest request){
		ModelAndView mv = new ModelAndView();
		try{
			if(model.containsAttribute("userSession")){			
				mv.addObject("manageGroup", CommonCons.MANAGE_GROUP);
				mv.addObject("manageFGroup", CommonCons.MANAGE_FGROUP);
				mv.addObject("addGroup", CommonCons.ADD_GROUP);
				mv.addObject("editGroup", CommonCons.EDIT_GROUP);
				mv.setViewName("add-group");
			} else {
				mv.setViewName("redirect-page");
				mv.addObject("url","login");
			}
			
		}catch( Exception e){
			logger.error("Error", e);
			mv.setViewName("redirect-page");
			mv.addObject("url","login");
		}
		
		return mv;
	}
	
	@RequestMapping(value="edit-page", method=RequestMethod.GET)
	public ModelAndView editGroupPage(Model model, HttpServletRequest request){
		ModelAndView mv = new ModelAndView();
		try{
			if(model.containsAttribute("userSession")){			
				List<Group> groups = groupService.getAllGroups();
				boolean showInfoNoGroup = false;
				
				if(groups.size()==0){
					showInfoNoGroup = true;
				}
				
				mv.addObject("showInfoNoGroup",showInfoNoGroup);
				mv.addObject("manageGroup", CommonCons.MANAGE_GROUP);
				mv.addObject("manageFGroup", CommonCons.MANAGE_FGROUP);
				mv.addObject("addGroup", CommonCons.ADD_GROUP);
				mv.addObject("editGroup", CommonCons.EDIT_GROUP);
				mv.addObject("securityGroup", CommonCons.SECURITY_GROUP);
				mv.setViewName("edit-group");
			} else {
				mv.setViewName("redirect-page");
				mv.addObject("url","login");
			}		
			
		}catch( Exception e){
			logger.error("Error", e);
			mv.setViewName("redirect-page");
			mv.addObject("url","login");
		}
		
		return mv;
	}
	
	@ResponseBody
	@RequestMapping(value="update", method=RequestMethod.PUT)
	public Response updateGroup(Model model, HttpServletRequest request, @RequestBody Group group){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){			
				groupService.update(group);
				resp.setResultCode(1000);
				resp.setMessage("Sukses Update Data");
			} else {
				resp.setResultCode(5001);
				resp.setMessage("No Session");
			}
			
			return resp;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value="edit", method=RequestMethod.PUT)
	public Response updateGroup(Model model, HttpServletRequest request, @RequestParam("groupId") Long groupId, @RequestBody GroupApi groupApi){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){			
				groupService.updateGroupWithMenus(groupId, groupApi);
				resp.setResultCode(1000);
				resp.setMessage("Sukses Update Data");
			} else {
				resp.setResultCode(5001);
				resp.setMessage("No Session");
			}
			
			return resp;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value="map-menu", method=RequestMethod.PUT)
	public Response mappingMenu(Model model, HttpServletRequest request, @RequestParam("menuId") Long menuId, @RequestBody Long[] groupIds){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){			
				groupService.mapMenu(menuId, groupIds);
				resp.setResultCode(1000);
				resp.setMessage("Sukses Mapping Data");
			} else {
				resp.setResultCode(5001);
				resp.setMessage("No Session");
			}		
			
			return resp;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value="get-group-button", method=RequestMethod.GET)
	public Response getGroupButtonWithChecked(Model model, HttpServletRequest request, @RequestParam("buttonId") Long buttonId){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){			
				List<GroupResponse> groups = groupService.getGroups(buttonId);
				resp.setResultCode(1000);
				resp.setMessage("Sukses get data");
				resp.setData(groups);
			} else {
				resp.setResultCode(5001);
				resp.setMessage("No Session");
			}		
			
			return resp;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value="map-button", method=RequestMethod.PUT)
	public Response mappingButton(Model model, HttpServletRequest request, @RequestParam("buttonId") Long buttonId, @RequestBody Long[] groupIds){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){			
				groupService.mapButton(buttonId, groupIds);
				resp.setResultCode(1000);
				resp.setMessage("Sukses Mapping Data");
			} else {
				resp.setResultCode(5001);
				resp.setMessage("No Session");
			}		
			
			return resp;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
}
