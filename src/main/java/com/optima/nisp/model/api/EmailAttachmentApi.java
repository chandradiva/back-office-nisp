package com.optima.nisp.model.api;

public class EmailAttachmentApi {

	private String newAttachment;
	private String oldAttachment;
	private boolean highlight = false;
	
	public String getNewAttachment() {
		return newAttachment;
	}
	public void setNewAttachment(String newAttachment) {
		this.newAttachment = newAttachment;
	}
	public String getOldAttachment() {
		return oldAttachment;
	}
	public void setOldAttachment(String oldAttachment) {
		this.oldAttachment = oldAttachment;
	}
	public boolean getHighlight() {
		return highlight;
	}
	public void setHighlight(boolean highlight) {
		this.highlight = highlight;
	}
}
