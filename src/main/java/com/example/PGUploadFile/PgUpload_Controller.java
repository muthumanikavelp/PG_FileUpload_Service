package com.example.PGUploadFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.NoSuchElementException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.StreamingHttpOutputMessage.Body;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import lombok.val;

@RestController
@CrossOrigin()
@ControllerAdvice
public class PgUpload_Controller {
	Path fileStoragePath;
	
	private String uploadDir;

	public String getUploadDir() {
		return uploadDir;
	}
  public void setUploadDir(String uploadDir) {
		this.uploadDir = uploadDir;
	}
	Logger logger = LoggerFactory.getLogger(PgUpload_Controller.class);
	
	Decrypt decrypt;
//OnboardUpload
	private ResponseEntity<String> Uploadfile(String directoryName,HttpServletRequest request) throws ServletException, IOException{
		
	 	String projectLocation = System.getProperty("user.dir");
	 	
	 logger.info("projectLocation:"+projectLocation);
			String filePath=directoryName;
			System.out.println("filePath:"+filePath);
			File directory = new File(String.valueOf(filePath));
			System.out.println("directoryName:" + directoryName);
			if (!directory.exists()) {
				directory.mkdir();
}

			String fileLocation = filePath+"/" ;
			System.out.println("fileLocation:"+fileLocation);
			
			fileStoragePath = Paths.get(fileLocation).toAbsolutePath().normalize();
			
	  final String path = fileLocation.toString(); 
	  System.out.println("path:"+path);
	  
	  final javax.servlet.http.Part filePart = request.getPart("file");
	  final String fileName = request.getParameter("filename");
	  

	  OutputStream out = null;
	  InputStream fileContent = null;	 

	  try {  
		  out = new FileOutputStream(new File(fileStoragePath.toString()+'/'+fileName));	   
	    fileContent = filePart.getInputStream();
	    int read = 0;
	    final byte[] bytes = new byte[1024];

	    while ((read = fileContent.read(bytes)) != -1) {
	      out.write(bytes, 0, read);
	    }	   

	  } catch (FileNotFoundException fne) {
	   
	  } finally {
	    if (out != null) {
	      out.close();
	    }
	    if (fileContent != null) {
	      fileContent.close();
	    }
	   
	    return ResponseEntity.ok(path);
	  }
	}
	@RequestMapping("/onboardupload")
	private ResponseEntity<String> onboardupload(HttpServletRequest request)
	        throws ServletException, IOException {
		
		String directoryName = applicationproperties.getUploadConfigPath();
		logger.info("directoryName:"+directoryName);
	  return Uploadfile(directoryName, request);
	}
	//FundUpload
	@RequestMapping("/fundupload")
	private ResponseEntity<String> fundupload(HttpServletRequest request)
	        throws ServletException, IOException {

		
		String directoryName = applicationproperties.getUploadFundReqPath();
			
		 return Uploadfile(directoryName, request);
	  }
	
	//businessupload
	
	@RequestMapping("/businessupload")
	private ResponseEntity<String> businessupload(HttpServletRequest request)
	        throws ServletException, IOException {
		String directoryName = applicationproperties.getUploadBusPlanPath();
		return Uploadfile(directoryName, request);
		
	}
	
	//Mobileupload
	
	@PostMapping("/Mobuploadfile")

	public ResponseEntity<String> Mobuploadfile(@RequestBody String ObjJson ,HttpServletRequest request) throws NoSuchElementException, java.sql.SQLException, IOException, ServletException{
		int i=0;
		
		/*JSONArray jsonarray = new JSONArray(ObjJson);
			JSONObject jsonObject = jsonarray.getJSONObject(1);*/
			JSONObject jsonObject = new JSONObject(ObjJson);
			String tempValue = jsonObject.toString();
		JSONObject attachment_obj = jsonObject.getJSONObject("sync_attachment_mobile_to_web");
	//	JSONObject attachment_obj = ObjJson;
		String attachmentInfo = attachment_obj.getString("AttachmentInfo");
		//String projectLocation = System.getProperty("user.dir");
			String filePathUrl = applicationproperties.getUploadPathMem();
			String filePathmem = filePathUrl;
					
			String directoryMemPro =filePathUrl;
					
			String directoryNamePG = filePathUrl+"/"+ attachment_obj.getString("PG_ID").toString();
					
			String directoryNameMem =filePathUrl+"/"+ attachment_obj.getString("PG_ID").toString() + "/"
					+ attachment_obj.getString("MemberID").toString();
			String filePath =  directoryMemPro;
			File	directory = new File(String.valueOf(filePath));
			if (!directory.exists()) {

			directory.mkdir();

			}
			filePath =  directoryNamePG;
			 directory = new File(String.valueOf(filePath));
			if (!directory.exists()) {
			directory.mkdir();
			}
			filePath = directoryNameMem;
			directory = new File(String.valueOf(filePath));
			if (!directory.exists()) {
			directory.mkdir();

			}
			String crntImage = attachment_obj.getString("AttachmentInfo").toString();
			byte[] data = Base64.getMimeDecoder().decode(crntImage);

			try (OutputStream stream = new FileOutputStream(
			filePath + "/"+ attachment_obj.getString("FileName").toString())) {
			stream.write(data);
			
		
			//String fileLocation = filePath ;
			String fileLocation = filePath + "/" + attachment_obj.getString("FileName").toString();
					
			System.out.println("fileLocation:"+fileLocation);

		    return ResponseEntity.ok(fileLocation);
	  }catch(Exception ex) {
		  throw ex;
	  }
	
		
		
		}
	//sqlattach
	
		@RequestMapping("/SQLliteupload")
		private ResponseEntity<String>SQLliteupload(HttpServletRequest request)
		        throws ServletException, IOException {
			logger.info("Hit SQLliteupload");
			String projectLocation = System.getProperty("user.dir");
			logger.info("projectLocation",projectLocation);
			String directoryName = applicationproperties.getUploadPathSQLlite();
			logger.info("directoryName",directoryName);
				String filePath=directoryName;
				System.out.println("filePath:"+filePath);
				File directory = new File(String.valueOf(filePath));
				System.out.println("directoryName:" + directoryName);
				if (!directory.exists()) {
					directory.mkdir();

				}
	          String fileLocation = filePath+"/" ;
				logger.info(fileLocation);

				fileStoragePath = Paths.get(fileLocation).toAbsolutePath().normalize();
				logger.info("fileStoragePath",fileStoragePath);
				
		  final String path = fileLocation.toString(); 
		  logger.info("path",path);
		  System.out.println("path:"+path);
		  final javax.servlet.http.Part filePart = request.getPart("file");
		  logger.info("filePart",filePart);
		  final String fileName = request.getParameter("filename");
		  logger.info("filename"+fileName);
		  final String fileId = request.getParameter("fileid");
		  logger.info("fileId",fileId);
		  System.out.println("fileName:"+fileName);

		  OutputStream out = null;
		  InputStream fileContent = null;
		 

		  try {
		  
			
			  out = new FileOutputStream(new File(fileStoragePath.toString()+'/'+fileId+"_"+fileName));
			  logger.info("out:" +out);
			  System.out.println("out:"+out);
		    fileContent = filePart.getInputStream();

		    logger.info("fileContent:" +fileContent);
		    int read = 0;
		    final byte[] bytes = new byte[1024];
		    logger.info("bytes:" +fileContent);
		    while ((read = fileContent.read(bytes)) != -1) {
		      out.write(bytes, 0, read);
		    }
		   

		  } catch (FileNotFoundException fne) {
		   

		  } finally {
		    if (out != null) {
		      out.close();
		    }
		    if (fileContent != null) {
		      fileContent.close();
		    }
		    return ResponseEntity.ok(path);
		  }
		}

	//onboardingdownload
	@PostMapping("/Download")
	private ResponseEntity<Blob> onBoardDownload(@RequestBody String  filePath)
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, Exception {
		try {
			String  _filePath=filePath;
			File file = new File(_filePath);
			System.out.println("file:"+file);
			byte[] bytes = new byte[(int) file.length()];

			try (FileInputStream fis = new FileInputStream(file)) {
				fis.read(bytes);
			}

			Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);
			return ResponseEntity.ok(blob);


		} catch (SQLException ex) {
			logger.error(ex.getMessage());
			throw ex;
		} catch (IOException ex) {
			throw ex;
		}
	}

	//fundDownload
		/*@PostMapping("/Funddownloads")
		private ResponseEntity<Blob> Funddownloads(@RequestBody String filePath)
				throws IOException, SerialException, SQLException, ClassNotFoundException {
			try {
				File file = new File(filePath);
				System.out.println("file:"+file);
				byte[] bytes = new byte[(int) file.length()];

				try (FileInputStream fis = new FileInputStream(file)) {
					fis.read(bytes);
				}

				Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);
				
				return ResponseEntity.ok(blob);

			} catch (SQLException ex) {
				logger.error(ex.getMessage());
				throw ex;
			} catch (IOException ex) {
				throw ex;
			}
		}
		//BusinessDownload
		@PostMapping("/Businessdownloads")
		private ResponseEntity<Blob> Businessdownloads(@RequestBody String filePath)
				throws IOException, SerialException, SQLException, ClassNotFoundException {
			try {
				File file = new File(filePath);
				System.out.println("file:"+file);
				byte[] bytes = new byte[(int) file.length()];

				try (FileInputStream fis = new FileInputStream(file)) {
					fis.read(bytes);
				}

				Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);
				
				return ResponseEntity.ok(blob);
			} catch (IOException ex) {
				throw ex;
			}
		}*/
		
		//MobileDownload
		
		/*@RequestMapping("/Mobiledownloads")
		private ResponseEntity<Blob> readFileToBytes(@RequestBody String filePath)
				throws IOException, SerialException, SQLException, ClassNotFoundException {
			try {
				File file = new File(filePath);
				System.out.println("file:"+file);
				byte[] bytes = new byte[(int) file.length()];

				
				
				try (FileInputStream fis = new FileInputStream(file)) {
					fis.read(bytes);
				}

				Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);
				return ResponseEntity.ok(blob);

			} catch (SQLException ex) {
				logger.error(ex.getMessage());
				throw ex;
			} catch (IOException ex) {
				throw ex;
			}
		}*/
	
	
}
