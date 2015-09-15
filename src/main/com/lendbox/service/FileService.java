package com.lendbox.service;


import com.lendbox.model.File;

import java.util.List;

public interface FileService {
    public void addFile(File file) throws Exception;
    public List<File> getAllFiles() throws Exception;
}
