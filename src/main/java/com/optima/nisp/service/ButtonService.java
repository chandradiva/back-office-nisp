package com.optima.nisp.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.optima.nisp.dao.ButtonDao;
import com.optima.nisp.dao.GroupButtonDao;
import com.optima.nisp.model.Button;

@Component
@Transactional
public class ButtonService {

	@Autowired
	private ButtonDao buttonDao;
	
	@Autowired
	private GroupButtonDao groupButtonDao;
	
	public void insert(Button button) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		buttonDao.create(button);
	}

	public Button getButton(Long buttonId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		return buttonDao.getButton(buttonId);
	}

	public List<Button> getAllButtons() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		return buttonDao.getAllButtons();
	}

	public void delete(Long buttonId) {
		groupButtonDao.deleteByButtonId(buttonId);
		buttonDao.delete(buttonId);
	}

	public void update(Button button) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		buttonDao.update(button);
	}

	public void bulkUpdate(List<Button> buttons) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		buttonDao.updateBatch(buttons.toArray());
	}
	
	public Button getByButtonUrl(String url) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		return buttonDao.getByButtonUrl(url);
	}
}
