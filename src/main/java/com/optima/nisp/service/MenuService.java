package com.optima.nisp.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.optima.nisp.dao.GroupDao;
import com.optima.nisp.dao.GroupMenuDao;
import com.optima.nisp.dao.MenuDao;
import com.optima.nisp.dao.MenuRelationDao;
import com.optima.nisp.model.Group;
import com.optima.nisp.model.GroupMenu;
import com.optima.nisp.model.Menu;
import com.optima.nisp.model.MenuRelation;
import com.optima.nisp.response.MenuResponse;

@Component
@Transactional
public class MenuService {
	
	@Autowired
	private MenuDao menuDao;
	
	@Autowired 
	private MenuRelationDao menuRelationDao;
	
	@Autowired
	private GroupMenuDao groupMenuDao;

	@Autowired
	GroupDao groupDao;
	
	public Menu getMenu(long menuId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		return menuDao.getMenu(menuId, 1);
	}
	
	public List<Menu> getAllMenus() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		return menuDao.getAllMenus();
	}

	public List<MenuResponse> getTreeViewMenus() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		List<Menu> rootMenus = menuDao.getRootMenus();
		List<MenuResponse> treeViewMenus = new ArrayList<MenuResponse>();
		
		for(Menu menu : rootMenus){
			MenuResponse menuResponse = new MenuResponse();
			menuResponse.setFlag(menu.getFlag());
			menuResponse.setLink(menu.getLink());
			menuResponse.setMenuId(menu.getMenuId());
			menuResponse.setSubtitle(menu.getSubtitle());
			menuResponse.setTitle(menu.getTitle());
			menuResponse = getFullParents(menuResponse);
			treeViewMenus.add(menuResponse);
		}
		
		return treeViewMenus;
	}
	
	private MenuResponse getFullParents(MenuResponse menuResponse) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		menuResponse.setSubMenus(new ArrayList<MenuResponse>());
		List<MenuRelation> menuRelations = menuRelationDao.getMenuRelations(menuResponse.getMenuId());
		
		if(menuRelations!=null){
			for(MenuRelation mr : menuRelations){
				Menu subMenu = menuDao.getMenu(mr.getChildMenuId(), 1);
				
				if(subMenu!=null){
					MenuResponse subMenuResponse = new MenuResponse();
					subMenuResponse.setFlag(subMenu.getFlag());
					subMenuResponse.setLink(subMenu.getLink());
					subMenuResponse.setMenuId(subMenu.getMenuId());
					subMenuResponse.setSubtitle(subMenu.getSubtitle());
					subMenuResponse.setTitle(subMenu.getTitle());
					
					if(subMenuResponse.getFlag()==0) continue;
					subMenuResponse = getFullParents(subMenuResponse);
					menuResponse.getSubMenus().add(subMenuResponse);
				}
			}
		}
		
		return menuResponse;
	}

	public void insertMenu(Menu menu, long parentId) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Long menuId = menuDao.create(menu);
				
		if(parentId != 0){	//Memiliki Parent
			MenuRelation mr = new MenuRelation();
			mr.setParentMenuId(parentId);
			mr.setChildMenuId(menuId.longValue());
			menuRelationDao.create(mr);
		}				
	}

	public void deleteMenu(Long menuId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		Menu menu = menuDao.getMenu(menuId);		
		
		if(menu.getFlag()==1){	//Jika Parent, maka anak anaknya juga di hapus
			List<MenuRelation> menuRelations = menuRelationDao.getMenuRelations(menuId);
			
			for(MenuRelation menuRelation : menuRelations){
				Menu childMenu = new Menu();
				childMenu.setMenuId(menuRelation.getChildMenuId());
				childMenu.setActiveStatus(0);
				menuDao.update(childMenu);
				deleteMenu(childMenu.getMenuId());
			}
			
			menuRelationDao.deleteByParent(menuId);
		}
		else {	//jika anak
			menuRelationDao.deleteByChild(menuId);
		}
		
		Menu deletedMenu = new Menu();
		deletedMenu.setMenuId(menuId);
		deletedMenu.setActiveStatus(0); //Nonaktifkan
		menuDao.update(deletedMenu);
		
		//Delete di tabel group menu
		groupMenuDao.deleteByMenuId(menuId);
	}

	public List<MenuResponse> getTreeViewCheckboxMenus() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		List<Menu> rootMenus = menuDao.getAllRootMenus();
		List<MenuResponse> treeViewCheckboxMenus = new ArrayList<MenuResponse>();
		
		for(Menu menu : rootMenus){
			MenuResponse menuResponse = new MenuResponse();
			menuResponse.setFlag(menu.getFlag());
			menuResponse.setLink(menu.getLink());
			menuResponse.setMenuId(menu.getMenuId());
			menuResponse.setSubtitle(menu.getSubtitle());
			menuResponse.setTitle(menu.getTitle());
			menuResponse = getFullMenus(menuResponse);
			treeViewCheckboxMenus.add(menuResponse);
		}
		return treeViewCheckboxMenus;
	}
	
	private MenuResponse getFullMenus(MenuResponse menu) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		menu.setSubMenus(new ArrayList<MenuResponse>());
		List<MenuRelation> menuRelations = menuRelationDao.getMenuRelations(menu.getMenuId());
		
		if(menuRelations!=null){
			for(MenuRelation mr : menuRelations){
				Menu subMenu = menuDao.getMenu(mr.getChildMenuId(),1);
				
				if(subMenu!=null){
					MenuResponse subMenuResponse = new MenuResponse();
					subMenuResponse.setFlag(subMenu.getFlag());
					subMenuResponse.setLink(subMenu.getLink());
					subMenuResponse.setMenuId(subMenu.getMenuId());
					subMenuResponse.setSubtitle(subMenu.getSubtitle());
					subMenuResponse.setTitle(subMenu.getTitle());
					
					subMenuResponse = getFullMenus(subMenuResponse);
					menu.getSubMenus().add(subMenuResponse);
				}
			}
		}
		
		return menu;
	}

	public List<MenuResponse> getTreeViewCheckboxEditMenus(Long groupId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		List<Menu> rootMenus = menuDao.getAllRootMenus();
		List<GroupMenu> groupMenus = groupMenuDao.getByGroupId(groupId);
		List<MenuResponse> treeViewCheckboxEditMenus = new ArrayList<MenuResponse>();
		
		for(Menu menu : rootMenus){
			MenuResponse menuResponse = new MenuResponse();
			menuResponse.setFlag(menu.getFlag());
			menuResponse.setLink(menu.getLink());
			menuResponse.setMenuId(menu.getMenuId());
			menuResponse.setSubtitle(menu.getSubtitle());
			menuResponse.setTitle(menu.getTitle());
			menuResponse = getCheckedFullMenus(menuResponse, groupMenus);
			treeViewCheckboxEditMenus.add(menuResponse);
		}		
		
		return treeViewCheckboxEditMenus;
	}

	private MenuResponse getCheckedFullMenus(MenuResponse menu, List<GroupMenu> checkedGroupMenu) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		menu.setSubMenus(new ArrayList<MenuResponse>());
		List<MenuRelation> menuRelations = menuRelationDao.getMenuRelations(menu.getMenuId());
		
		if(menuRelations!=null){
			for(MenuRelation mr : menuRelations){
				Menu subMenu = menuDao.getMenu(mr.getChildMenuId(),1);
				
				if(subMenu!=null){
					MenuResponse subMenuResponse = new MenuResponse();
					subMenuResponse.setFlag(subMenu.getFlag());
					subMenuResponse.setLink(subMenu.getLink());
					subMenuResponse.setMenuId(subMenu.getMenuId());
					subMenuResponse.setSubtitle(subMenu.getSubtitle());
					subMenuResponse.setTitle(subMenu.getTitle());
					
					subMenuResponse = getCheckedFullMenus(subMenuResponse, checkedGroupMenu);
					menu.getSubMenus().add(subMenuResponse);
				}
			}
		}
		
		for(GroupMenu gm : checkedGroupMenu){
			if(menu.getMenuId().equals(gm.getMenuId())){
				menu.setCheckedStatus(1);
				break;
			}				
		}
		
		return menu;
	}
	
	public List<Group> getGroupsByFGroup(String fGroupName) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		return groupDao.getGroupsByFGroup(fGroupName);
	}

	public void bulkUpdate(List<Menu> menus) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		menuDao.updateBatch(menus.toArray());
	}

	public List<Menu> getAllChildMenus() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		return menuDao.getAllChildMenus();
	}
	
	public List<MenuResponse> getSidebarMenu(Long[] groupIds) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		
		/*Get Semua Menu dari berbagai grup (Tanpa Hirarki Parent-Child)*/
		List<GroupMenu> allMemberMenus = new ArrayList<GroupMenu>();
		for(Long groupId : groupIds){
			List<GroupMenu> memberGroupMenus = groupMenuDao.getByGroupId(groupId);
			for(GroupMenu gm : memberGroupMenus){
				if(!isMenuMemberOf(gm, allMemberMenus)){
					allMemberMenus.add(gm);
				}
			}
		}
		
		/*Get Semua Root dari berbagai grup*/
		List<GroupMenu> allRootMenus = new ArrayList<GroupMenu>();
		for(Long groupId : groupIds){
			List<GroupMenu> rootMenus = groupMenuDao.getRootGroupMenus(groupId);
			for(GroupMenu gm : rootMenus){
				if(!isMenuMemberOf(gm, allRootMenus)){
					allRootMenus.add(gm);
				}
			}
		}
		
		List<MenuResponse> menus = new ArrayList<MenuResponse>();
		
		for(GroupMenu rootMenu : allRootMenus){
			Menu rm = menuDao.getMenu(rootMenu.getMenuId());
			if(rm!=null){
				MenuResponse menuResponse = new MenuResponse();
				menuResponse.setFlag(rm.getFlag());
				menuResponse.setLink(rm.getLink());
				menuResponse.setMenuId(rm.getMenuId());
				menuResponse.setSubtitle(rm.getSubtitle());
				menuResponse.setTitle(rm.getTitle());
				menuResponse = getFullMenuSidebar(menuResponse, allMemberMenus);
				menus.add(menuResponse);
			}
				
		}
		
		return menus;
	}
	
	private MenuResponse getFullMenuSidebar(MenuResponse menu, List<GroupMenu> member) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		menu.setSubMenus(new ArrayList<MenuResponse>());
		
		List<MenuRelation> childMenus = menuRelationDao.getMenuRelations(menu.getMenuId());
		
		if(childMenus!=null){
			for(MenuRelation mr : childMenus){
				Menu subMenu = menuDao.getMenu(mr.getChildMenuId());
				if(subMenu!=null){
					if(isMenuMemberOf(subMenu, member)){
						MenuResponse subMenuResponse = new MenuResponse();
						subMenuResponse.setFlag(subMenu.getFlag());
						subMenuResponse.setLink(subMenu.getLink());
						subMenuResponse.setMenuId(subMenu.getMenuId());
						subMenuResponse.setSubtitle(subMenu.getSubtitle());
						subMenuResponse.setTitle(subMenu.getTitle());
						subMenuResponse = getFullMenuSidebar(subMenuResponse, member);
						menu.getSubMenus().add(subMenuResponse);
					}					
				}
			}
		}
		
		return menu;
	}
	
	private boolean isMenuMemberOf(Menu menu, List<GroupMenu> member){
		for(GroupMenu groupMenu : member){
			if(menu.getMenuId().equals(groupMenu.getMenuId()))
				return true;
		}
		
		return false;
	}

	private boolean isMenuMemberOf(GroupMenu gm, List<GroupMenu> gmList){
		for(GroupMenu groupMenu : gmList){
			if(gm.getMenuId().equals(groupMenu.getMenuId()))
				return true;
		}
		
		return false;
	}
	
	public Menu getByLink(String link) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		return menuDao.getByLink(link);
	}
}
