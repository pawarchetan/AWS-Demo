package com.lendbox.controller;

import com.lendbox.Exception.FileSizeExceedException;
import com.lendbox.Exception.InvalidContentTypeException;
import com.lendbox.model.File;
import com.lendbox.processor.FileUploadProcessor;
import com.lendbox.service.FileService;
import org.apache.commons.fileupload.FileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.util.*;


@Controller
public class FileController implements HandlerExceptionResolver {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
    private static int FILE_SIZE_LIMIT = 2097152;
    private static final List<String> SUPPORTED_CONTENT_TYPES = Arrays.asList("image/jpeg", "application/pdf", "application/msword", "image/png");
    private FileService fileService;
	
	@Autowired(required=true)
	@Qualifier(value="fileService")
	public void setFileService(FileService ps){
		this.fileService = ps;
	}
	
	@RequestMapping(value = "/files", method = RequestMethod.GET)
	public String listFiles(Model model) throws Exception {
		model.addAttribute("file", new File());
		model.addAttribute("listFiles", this.fileService.getAllFiles());
		return "file";
	}

    @RequestMapping(value = "/file/upload", method = RequestMethod.POST)
    public String uploadFile(@RequestPart("file") MultipartFile fileObject, @Context HttpServletRequest req) throws IOException, FileSizeExceedException, InvalidContentTypeException {
        String bucketName = "lendbox-test-chetan";
        FileItem fileItem = ((CommonsMultipartFile) fileObject).getFileItem();
        String contentType = fileItem.getContentType();
        String fileName = fileItem.getName();
        long fileSize = fileObject.getSize();


        if (fileSize > FILE_SIZE_LIMIT) {
            logger.error("You have exceeded the Upload File Size");
            throw new FileSizeExceedException("File Size exceeds the limit (Limit is 2MB");
        }
        if (!SUPPORTED_CONTENT_TYPES.contains(contentType)) {
            logger.error("File type is not supported.You tried to upload " + contentType + " file");
            throw new InvalidContentTypeException("File Type is not supported!!!!!!");
        }

        FileUploadProcessor fileUploadProcessor = new FileUploadProcessor();
        java.io.File convertedFile = fileUploadProcessor.multipartToFile(fileObject);
        try {
            String output = fileUploadProcessor.addFileToS3Bucket(convertedFile, fileName, bucketName);
            if (output.equalsIgnoreCase("success")) {
                File file = new File();
                file.setName(fileName);
                file.setSize(fileSize);
                file.setUrl(fileUploadProcessor.getUrlForDocument(bucketName, fileName));
                file.setCreatedDate(new Date());
                this.fileService.addFile(file);
                return "redirect:/files";
            } else {
                System.out.println("sorry...File is Invalid");
                return "redirect:/files";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/files";
        }
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest arg0,
                                         HttpServletResponse arg1, Object arg2, Exception exception) {
        Map<Object, Object> model = new HashMap<>();
        if (exception instanceof MaxUploadSizeExceededException){
            model.put("errors", "File size should be less then "+
                    ((MaxUploadSizeExceededException)exception).getMaxUploadSize()+" byte.");
        } else{
            model.put("errors", "Unexpected error: " + exception.getMessage());
        }
        return new ModelAndView("/file", (Map) model);
    }
	

	
}
