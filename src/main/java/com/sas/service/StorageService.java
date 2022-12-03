package com.sas.service;

import com.sas.entity.FileData;
import com.sas.entity.ImageData;
import com.sas.exception.ObjectNotFoundException;
import com.sas.respository.FileDataRepository;
import com.sas.respository.StorageRepository;
import com.sas.util.ImageUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

@Service
public class StorageService {

	@Autowired
	private StorageRepository repository;

	@Autowired
	private FileDataRepository fileDataRepository;

	private final String FOLDER_PATH = "D:\\home\\";

	public String uploadImage(MultipartFile file) throws IOException {
		ImageData imageData = repository.save(ImageData.builder().name(file.getOriginalFilename())
				.type(file.getContentType()).imageData(ImageUtils.compressImage(file.getBytes())).build());
		if (imageData != null) {
			return "File uploaded successfully : " + file.getOriginalFilename();
		}
		return null;
	}

	public byte[] downloadImage(String fileName) throws ObjectNotFoundException {
		Optional<ImageData> dbImageData = repository.findByName(fileName);
		if(dbImageData.isEmpty())
			throw new ObjectNotFoundException("File not found", HttpStatus.NOT_FOUND);
		return ImageUtils.decompressImage(dbImageData.get().getImageData());
	}

	public String uploadImageToFileSystem(MultipartFile file) throws IOException {
		String filePath = FOLDER_PATH + file.getOriginalFilename();
		FileData fileData = fileDataRepository.save(FileData.builder().name(file.getOriginalFilename())
				.type(file.getContentType()).filePath(filePath).build());
		file.transferTo(new File(filePath));
		if (fileData != null) {
			return "File uploaded successfully : " + filePath;
		}
		return null;
	}

	public byte[] downloadImageFromFileSystem(String fileName) throws IOException, ObjectNotFoundException {
		Optional<FileData> fileData = fileDataRepository.findByName(fileName);
		if(fileData.isEmpty())
			throw new ObjectNotFoundException("File not found", HttpStatus.NOT_FOUND);
		String filePath = fileData.get().getFilePath();
		return Files.readAllBytes(new File(filePath).toPath());
	}

}
