package com.sas.service;

import com.sas.constant.Constant;
import com.sas.entity.FileData;
import com.sas.exception.ObjectNotFoundException;
import com.sas.respository.FileDataRepository;

import lombok.AllArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StorageService implements Constant {

	private final FileDataRepository fileDataRepository;

	public String uploadImageToFileSystem(MultipartFile file) throws IOException {
		String filePath = FILE_PATH + file.getOriginalFilename();
		fileDataRepository.save(FileData.builder().name(file.getOriginalFilename())
				.type(file.getContentType()).filePath(filePath).build());
		file.transferTo(new File(filePath));
		return "File uploaded successfully : " + filePath;
	}

	public byte[] downloadImageFromFileSystem(String fileName) throws IOException, ObjectNotFoundException {
		Optional<FileData> fileData = fileDataRepository.findByName(fileName);
		if(fileData.isEmpty())
			throw new ObjectNotFoundException("File not found", HttpStatus.NOT_FOUND);
		String filePath = fileData.get().getFilePath();
		return Files.readAllBytes(new File(filePath).toPath());
	}

	public void downloadImage(String fileName, HttpServletResponse response) throws ObjectNotFoundException, IOException {
		Optional<FileData> optional = fileDataRepository.findByName(fileName);
		if(optional.isEmpty())
			throw new ObjectNotFoundException("File not found", HttpStatus.NOT_FOUND);
		FileData fileData = optional.get();
		String filePath = fileData.getFilePath();
		try (InputStream inputStream = new FileInputStream(filePath)) {
			response.setContentType(fileData.getType());
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			IOUtils.copy(inputStream, response.getOutputStream());
		}
	}
}
