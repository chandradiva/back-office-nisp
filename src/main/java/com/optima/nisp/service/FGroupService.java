package com.optima.nisp.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.optima.nisp.constanta.CommonCons;
import com.optima.nisp.dao.FGroupDao;
import com.optima.nisp.model.FGroup;

@Component
@Transactional
public class FGroupService {

	@Autowired
	private FGroupDao fgroupDao;
	
	public void insert(FGroup fgroup) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		fgroupDao.create(fgroup);
	}

	public FGroup getFGroup(Long fgroupId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		return fgroupDao.getFGroup(fgroupId);
	}

	public List<FGroup> getAllFGroups() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		return fgroupDao.getAllFGroups();
	}
	
	public List<FGroup> getAllFGroupsNoSecurity() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		return fgroupDao.getAllFGroupsNoSecurity();
	}

	public List<FGroup> getByGroupId(Long groupId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		return fgroupDao.getByGroupId(groupId);
	}
	
	public void delete(Long fgroupId) {
		fgroupDao.delete(fgroupId);
	}

	public void update(FGroup fgroup) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		fgroupDao.update(fgroup);
	}
	
	public void bulkUpdate(List<FGroup> fgroups) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		List<FGroup> filteredFGroups = new ArrayList<FGroup>();
		
		/*Handle jika ada yang mencoba edit FGROUP WEBSTMTSECADMIN*/
		FGroup secAdmFGroup = fgroupDao.getByName(CommonCons.SECURITY_FGROUP);
		
		if(secAdmFGroup!=null){
			for(FGroup fgroup : fgroups){
				if(fgroup.getFgroupId()!=null && !fgroup.getFgroupId().equals(secAdmFGroup.getFgroupId())){
					filteredFGroups.add(fgroup);
				}
			}
		}
		
		fgroupDao.updateBatch(filteredFGroups.toArray());
	}
}
