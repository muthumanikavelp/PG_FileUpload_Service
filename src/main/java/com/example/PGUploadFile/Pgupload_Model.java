package com.example.PGUploadFile;

import org.springframework.core.io.ByteArrayResource;

public class Pgupload_Model {
	
	public ByteArrayResource File;
	public String filename;
	public String fileGID;
	public ByteArrayResource getFile() {
		return File;
	}
	public void setFile(ByteArrayResource file) {
		File = file;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getFileGID() {
		return fileGID;
	}
	public void setFileGID(String fileGID) {
		this.fileGID = fileGID;
	}
	
	

}
