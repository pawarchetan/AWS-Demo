package com.lendbox.service;

import com.lendbox.dao.FileDao;
import com.lendbox.model.File;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FileServiceImpl implements FileService {

    private FileDao fileDao;

    public void setFileDao(FileDao fileDao){
        this.fileDao = fileDao;
    }

    @Override
    public void addFile(File file) throws Exception {
        this.fileDao.addFile(file);
    }

    @Override
    public List<File> getAllFiles() throws Exception {
        return this.fileDao.getAllFiles();
    }
}
