package com.optima.nisp.controller;

import java.io.Serializable;
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
import com.optima.nisp.constanta.UserSession;
import com.optima.nisp.model.Menu;
import com.optima.nisp.response.MenuResponse;
import com.optima.nisp.response.Response;
import com.optima.nisp.service.MenuService;
import com.optima.nisp.utility.CommonUtils;

@Controller
@RequestMapping(value="menu")
@SessionAttributes("userSession")
public class MenuController {
	private static final Logger logger = Logger.getLogger(MenuController.class);
	
	@Autowired
	private MenuService menuService;
	
	@ResponseBody
	@RequestMapping(value="get-sidebar", method = RequestMethod.GET)
	public Serializable getSidebarMenus(Model model){
		try{
			Response response = new Response();
			
			if(model.containsAttribute("userSession")){
				UserSession userSession = (UserSession) model.asMap().get("userSession");
							
				List<MenuResponse> menus = menuService.getSidebarMenu(userSession.getGroupIds());
				
				response.setData(menus);
				response.setMessage("message");
				response.setResultCode(1000);
			} else {
				response.setMessage("no session");
				response.setResultCode(5001);
			}		
			
			return response;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	@RequestMapping(value="manage", method = RequestMethod.GET)
	public ModelAndView getManageMenuPage(Model model, HttpServletRequest request){
		ModelAndView modelAndView = new ModelAndView();
		try{
			if(model.containsAttribute("userSession")){			
				modelAndView.setViewName("manage-menu");
				modelAndView.addObject("menus", menuService.getAllMenus());
			} else {
				modelAndView.setViewName("redirect-page");
				modelAndView.addObject("url","login");
			}
			
		}catch( Exception e){
			logger.error("Error", e);
			modelAndView.setViewName("redirect-page");
			modelAndView.addObject("url","login");
		}
		
		return modelAndView;
	}
	
	@ResponseBody
	@RequestMapping(value="add", method = RequestMethod.POST)
	public Response insertMenu(Model model, HttpServletRequest request, @RequestBody Menu menu, @RequestParam("parentId") Long parentId){
		try{
			Response response = new Response();
			
			if(model.containsAttribute("userSession")){			
				menuService.insertMenu(menu, parentId);
				response.setResultCode(1000);
				response.setMessage("Menu Added Successfully");
			} else {
				response.setMessage("no session");
				response.setResultCode(5001);
			}			
			
			return response;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value = "get-parents", method = RequestMethod.GET)
	public Response getRootMenus(Model model){
		try{
			Response response = new Response();
			
			if(model.containsAttribute("userSession")){
				List<MenuResponse> rootMenus = menuService.getTreeViewMenus();
				response.setData(rootMenus);
				response.setResultCode(1000);
			} else {
				response.setMessage("no session");
				response.setResultCode(5001);
			}		
			
			return response;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value = "get-full-menus", method = RequestMethod.GET)
	public Response getFullAllMenus(Model model){
		try{
			Response response = new Response();
			
			if(model.containsAttribute("userSession")){
				List<MenuResponse> allMenus = menuService.getTreeViewCheckboxMenus();
				response.setData(allMenus);
				response.setResultCode(1000);
			} else {
				response.setMessage("no session");
				response.setResultCode(5001);
			}			
			
			return response;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value = "get", method = RequestMethod.GET)
	public Response getAllMenus(Model model){
		try{
			Response response = new Response();
			
			if(model.containsAttribute("userSession")){
				try {
					List<Menu> menus = menuService.getAllMenus();
					response.setData(menus);
					response.setResultCode(1000);
				} catch(Exception e){
					response.setResultCode(0);
					response.setMessage("Gagal get menus");
				}
			} else {
				response.setMessage("no session");
				response.setResultCode(5001);
			}		
			
			return response;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value = "get-all-children", method = RequestMethod.GET)
	public Response getAllChildMenus(Model model){
		try{
			Response response = new Response();
			
			if(model.containsAttribute("userSession")){
				try {
					List<Menu> menus = menuService.getAllChildMenus();
					response.setData(menus);
					response.setResultCode(1000);
				} catch(Exception e){
					response.setResultCode(0);
					response.setMessage("Gagal get menus");
				}
			} else {
				response.setMessage("no session");
				response.setResultCode(5001);
			}		
			
			return response;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value = "get-full-checked", method = RequestMethod.GET)
	public Response getFullAllMenusWithChecked(Model model, @RequestParam("groupId") Long groupId){
		try{
			Response response = new Response();
			
			if(model.containsAttribute("userSession")){
				List<MenuResponse> allMenus = menuService.getTreeViewCheckboxEditMenus(groupId);
				response.setData(allMenus);
				response.setResultCode(1000);
			} else {
				response.setMessage("no session");
				response.setResultCode(5001);
			}		
			
			return response;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value="delete", method = RequestMethod.DELETE)
	public Response deleteMenu(Model model, HttpServletRequest request, @RequestParam("menuId") long menuId){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){			
				menuService.deleteMenu(menuId);
				resp.setResultCode(1000);
				resp.setMessage("Delete Succeeded");
			} else {
				resp.setMessage("no session");
				resp.setResultCode(5001);
			}			
			
			return resp;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value="bulk-update", method = RequestMethod.PUT)
	public Response bulkUpdate(Model model, HttpServletRequest request, @RequestBody List<Menu> menus){
		try{
			Response response = new Response();
			
			if(model.containsAttribute("userSession")){			
				menuService.bulkUpdate(menus);
				response.setResultCode(1000);
				response.setMessage("Menu Updated Successfully");
			} else {
				response.setMessage("no session");
				response.setResultCode(5001);
			}		
			
			return response;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	@RequestMapping(value="mapping-to-group", method = RequestMethod.GET)
	public ModelAndView getMappingPage(Model model, HttpServletRequest request){
		ModelAndView mv = new ModelAndView();
		try{
			if(model.containsAttribute("userSession")){			
				mv.setViewName("mapping-menu-to-group");
				mv.addObject("manageGroup", CommonCons.MANAGE_GROUP);
				mv.addObject("manageFGroup", CommonCons.MANAGE_FGROUP);
				mv.addObject("addGroup", CommonCons.ADD_GROUP);
				mv.addObject("editGroup", CommonCons.EDIT_GROUP);
				mv.addObject("securityGroup", CommonCons.SECURITY_GROUP);
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
}
