package com.lendbox.dao;


import com.lendbox.model.File;

import java.util.List;

public interface FileDao {
    public void addFile(File p);
    public List<File> getAllFiles();
}
