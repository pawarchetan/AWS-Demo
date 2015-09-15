package com.lendbox.dao;

import com.lendbox.model.File;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class FileDaoImpl implements FileDao {

    private static final Logger logger = LoggerFactory.getLogger(FileDaoImpl.class);

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sf) {
        this.sessionFactory = sf;
    }

    @Override
    public void addFile(File file) {
        Session session = this.sessionFactory.getCurrentSession();
        session.save(file);
        logger.info("File Details Saved Successfully" + file);
    }

    @SuppressWarnings("JpaQlInspection")
    @Override
    public List<File> getAllFiles() {
        Session session = this.sessionFactory.getCurrentSession();
        List<File> files = session.createQuery("from File").list();
        for (File file : files) {
            logger.info("File List::" + file);
        }
        return files;
    }
}
