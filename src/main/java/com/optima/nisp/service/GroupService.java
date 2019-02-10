package com.optima.nisp.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.optima.nisp.constanta.CommonCons;
import com.optima.nisp.dao.GroupAdDao;
import com.optima.nisp.dao.GroupButtonDao;
import com.optima.nisp.dao.GroupDao;
import com.optima.nisp.dao.GroupFGroupDao;
import com.optima.nisp.dao.GroupMenuDao;
import com.optima.nisp.dao.MenuDao;
import com.optima.nisp.dao.MenuRelationDao;
import com.optima.nisp.model.Group;
import com.optima.nisp.model.GroupButton;
import com.optima.nisp.model.GroupFGroup;
import com.optima.nisp.model.GroupMenu;
import com.optima.nisp.model.Menu;
import com.optima.nisp.model.MenuRelation;
import com.optima.nisp.model.api.GroupApi;
import com.optima.nisp.response.GroupResponse;

@Component
@Transactional
public class GroupService {

	@Autowired
	private GroupDao groupDao;
	
	@Autowired
	private GroupMenuDao groupMenuDao;
	
	@Autowired
	private MenuDao menuDao;
	
	@Autowired
	private GroupAdDao groupAdDao;
	
	@Autowired
	private GroupFGroupDao groupFGroupDao;
	
	@Autowired
	private GroupButtonDao groupButtonDao;
	
	@Autowired
	private MenuRelationDao menuRelationDao;

	public List<Group> getAllGroups() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		return groupDao.getAllGroups();
	}

	public void deleteById(Long groupId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		Group group = groupDao.getById(groupId);
		
		if(group!=null && group.getGroupName()!=null && !group.getGroupName().equalsIgnoreCase(CommonCons.SECURITY_GROUP)){
			groupMenuDao.deleteByGroupId(groupId);
			groupButtonDao.deleteByGroupId(groupId);
			groupFGroupDao.deleteByGroupId(groupId);
			groupAdDao.deleteByGroupId(groupId);
			groupDao.deleteById(groupId);
		}
	}

	public void update(Group group) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		groupDao.update(group);
	}
	
	public List<Group> getByButtonId(Long buttonId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		return groupDao.getByButtonId(buttonId);
	}

	private boolean isMemberOfManageGroup(Menu menu){
		String[] members = new String[4];
		members[0] = CommonCons.ADD_GROUP;
		members[1] = CommonCons.EDIT_GROUP;
		members[2] = CommonCons.MANAGE_GROUP;
		members[3] = CommonCons.MANAGE_FGROUP;
		
		if(menu.getTitle()!=null){
			for(int i=0; i<members.length; i++){
				if(menu.getTitle().equalsIgnoreCase(members[i])){
					return true;
				}
			}
		}
		
		return false;
	}
	
	public void insertGroupWithMenus(GroupApi groupApi) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		Group group = new Group();
		group.setGroupName(groupApi.getGroup().getGroupName());
		Long groupId = groupDao.create(group);
		
		List<GroupMenu> groupMenus = new ArrayList<GroupMenu>();
		for(Long menuId : groupApi.getMenuIds()){
			Menu menu = menuDao.getMenu(menuId);
			
			if(menu!=null && !isMemberOfManageGroup(menu)){
				GroupMenu gm = new GroupMenu();
				gm.setGroupId(groupId);
				gm.setMenuId(menuId);
				groupMenus.add(gm);
			}
		}
		groupMenuDao.createBatch(groupMenus.toArray());
	}
	
	public void updateGroupWithMenus(Long groupId, GroupApi groupApi) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		Group group = groupDao.getById(groupId);
		
		if(group!=null && group.getGroupName()!=null && !group.getGroupName().equalsIgnoreCase(CommonCons.SECURITY_GROUP)){
			groupMenuDao.deleteByGroupId(groupId);
			List<GroupMenu> groupMenus = new ArrayList<GroupMenu>();
			for(Long menuId : groupApi.getMenuIds()){
				Menu menu = menuDao.getMenu(menuId);
				
				if(menu!=null && !isMemberOfManageGroup(menu)){
					GroupMenu gm = new GroupMenu();
					gm.setGroupId(groupId);
					gm.setMenuId(menuId);
					groupMenus.add(gm);
				}			
			}	
			groupMenuDao.createBatch(groupMenus.toArray());
			
			groupFGroupDao.deleteByGroupId(groupId);
			List<GroupFGroup> groupFGroups = new ArrayList<GroupFGroup>();
			for(Long fgroupId : groupApi.getFgroupIds()){
				GroupFGroup groupFGroup = new GroupFGroup();
				groupFGroup.setFgroupId(fgroupId);
				groupFGroup.setGroupId(groupId);
				groupFGroups.add(groupFGroup);
			}
			groupFGroupDao.createBatch(groupFGroups.toArray());
			
			groupDao.update(groupApi.getGroup());
		}
	}

	public List<GroupResponse> getGroupsWithChecked(Long menuId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		List<Group> allGroup = groupDao.getAllGroups();
		List<Group> relationGroups = groupDao.getByMenuId(menuId);
		
		List<GroupResponse> response = new ArrayList<GroupResponse>();
		
		for(Group g : allGroup){
			GroupResponse gr = new GroupResponse();
			gr.setGroupId(g.getGroupId());
			gr.setGroupName(g.getGroupName());
			if(isGroupInRelation(g, relationGroups))
				gr.setCheckedStatus(1);
			
			response.add(gr);
		}
		
		return response;
	}
	
	private boolean isGroupInRelation(Group group, List<Group> gmr){
		for(Group g : gmr){
			if(g.getGroupId() == group.getGroupId())
				return true;
		}
		
		return false;
	}

	public List<GroupResponse> getGroups(Long buttonId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		List<Group> allGroup = groupDao.getAllGroups();
		List<Group> relationGroups = groupDao.getByButtonId(buttonId);
		
		List<GroupResponse> response = new ArrayList<GroupResponse>();
		
		for(Group g : allGroup){
			GroupResponse gr = new GroupResponse();
			gr.setGroupId(g.getGroupId());
			gr.setGroupName(g.getGroupName());
			if(isGroupInRelation(g, relationGroups))
				gr.setCheckedStatus(1);
			
			response.add(gr);
		}
		
		return response;
	}

	public void mapButton(Long buttonId, Long[] groupIds) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		groupButtonDao.deleteByButtonId(buttonId);
		
		if(groupIds.length>0){
			List<Long> filteredGroupIds = new ArrayList<Long>();
			
			//Filter Security Admin
			for(Long gId : groupIds){
				Group group = groupDao.getById(gId);
				
				if(group!=null && group.getGroupName()!=null && !group.getGroupName().equalsIgnoreCase(CommonCons.SECURITY_GROUP)){
					filteredGroupIds.add(gId);
				}
			}
			
			for(Long groupId : filteredGroupIds){
				GroupButton groupButton = new GroupButton();
				groupButton.setGroupId(groupId);
				groupButton.setButtonId(buttonId);
				groupButtonDao.create(groupButton);
			}
		}
	}

	public void mapMenu(Long menuId, Long[] groupIds) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		Menu menu = menuDao.getMenu(menuId);
		
		if(menu!=null && !isMemberOfManageGroup(menu)){
			groupMenuDao.deleteByMenuId(menuId);
			
			//Selanjutnya Delete ancestor juga jika semua anaknya sudah tidak ada
			List<Group> groups = groupDao.getAllGroups();
			List<Group> filteredGroups = new ArrayList<Group>();
			for(Group group : groups){
				if(group.getGroupName()!=null && !group.getGroupName().equalsIgnoreCase(CommonCons.SECURITY_GROUP)){
					filteredGroups.add(group);
				}
			}	
			
			for(Group group : filteredGroups){
				deleteAncestor(menuId, group.getGroupId());
			}
			
			//Get Semua ID Ancestor
			List<Long> ancestorsId;
			ancestorsId = getMenuAncestorsId(menuId, new ArrayList<Long>());
			
			List<Long> filteredGroupIds = new ArrayList<Long>();
			for(Long gId : groupIds){
				Group group = groupDao.getById(gId);
				
				if(group!=null && group.getGroupName()!=null && !group.getGroupName().equalsIgnoreCase(CommonCons.SECURITY_GROUP)){
					filteredGroupIds.add(gId);
				}
			}
			
			for(Long gId : filteredGroupIds){
				GroupMenu gm = new GroupMenu();
				gm.setGroupId(gId);
				gm.setMenuId(menuId);
				groupMenuDao.create(gm);
				
				for(Long ancId : ancestorsId){
					if(!isAlreadyInGroupMenu(ancId, gId)){
						GroupMenu gm2 = new GroupMenu();
						gm2.setGroupId(gId);
						gm2.setMenuId(ancId);
						groupMenuDao.create(gm2);
					}					
				}
			}
		}
	}
	
	private boolean isAlreadyInGroupMenu(Long menuId, Long groupId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		GroupMenu gm = groupMenuDao.getGroupMenu(groupId, menuId);
		if(gm!=null)
			return true;
		
		return false;
	}
	
	private List<Long> getMenuAncestorsId(Long menuId, List<Long> ancestorsId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		MenuRelation mr = menuRelationDao.getByChild(menuId);
		if(mr==null){
			return ancestorsId;
		}
				
		ancestorsId.add(mr.getParentMenuId());
		getMenuAncestorsId(mr.getParentMenuId(), ancestorsId);
		return ancestorsId;
	}
	
	private boolean isNoSibling(Long menuId, Long groupId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		MenuRelation mr = menuRelationDao.getByChild(menuId);
		if(mr!=null){
			List<MenuRelation> mrs = menuRelationDao.getMenuRelations(mr.getParentMenuId());
			
			if(mrs!=null && mrs.size()>1){
				for(MenuRelation mr2 : mrs){
					if(mr2.getChildMenuId()==menuId)
						continue;
					
					GroupMenu gm = groupMenuDao.getGroupMenu(groupId, mr2.getChildMenuId());
					if(gm!=null)
						return false;
					else 
						return true;
				}
			}				
			else 
				return true;
		}
			
		return false;
	}
	
	private void deleteAncestor(Long menuId, Long groupId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		if(isNoSibling(menuId, groupId)){
			MenuRelation mr = menuRelationDao.getByChild(menuId);
			if(mr!=null){
				groupMenuDao.delete(groupId, mr.getParentMenuId());
				deleteAncestor(mr.getParentMenuId(), groupId);
			}				
		}
	}
}
