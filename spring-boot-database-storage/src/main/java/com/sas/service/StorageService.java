package com.sas.service;

import com.sas.entity.ImageData;
import com.sas.exception.ObjectNotFoundException;
import com.sas.respository.StorageRepository;
import com.sas.util.ImageUtils;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Service
public class StorageService {

	@Autowired
	private StorageRepository storageRepository;

	public String uploadImageToDB(MultipartFile file) throws IOException {
		storageRepository.save(ImageData.builder().name(file.getOriginalFilename())
				.type(file.getContentType()).imageData(ImageUtils.compressImage(file.getBytes())).build());
		return "File uploaded successfully : " + file.getOriginalFilename();
	}

	public byte[] downloadImageFromDB(String fileName) throws Exception {
		Optional<ImageData> dbImageData = storageRepository.findByName(fileName);
		if(dbImageData.isEmpty())
			throw new ObjectNotFoundException("File not found", HttpStatus.NOT_FOUND);
		return ImageUtils.decompressImage(dbImageData.get().getImageData());
	}

	public void downloadImage(String fileName, HttpServletResponse response) throws Exception {
		Optional<ImageData> dbImageData = storageRepository.findByName(fileName);
		if (dbImageData.isEmpty())
			throw new ObjectNotFoundException("File not found", HttpStatus.NOT_FOUND);
		ImageData imageData = dbImageData.get();
		byte[] bytes = ImageUtils.decompressImage(imageData.getImageData());
		try (InputStream inputStream = new ByteArrayInputStream(bytes)) {
			response.setContentType(imageData.getType());
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			IOUtils.copy(inputStream, response.getOutputStream());
		}
	}
}
