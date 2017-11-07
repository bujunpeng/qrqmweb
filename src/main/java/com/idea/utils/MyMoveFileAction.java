package com.idea.utils;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.storm.hdfs.common.rotation.RotationAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by poul on 2017/11/7.
 */
public class MyMoveFileAction implements RotationAction {
    private static final Logger LOG = LoggerFactory.getLogger(MyMoveFileAction.class);

    private String destination;

    public MyMoveFileAction toDestination(String destDir){
        destination = destDir;
        return this;
    }

    @Override
    public void execute(FileSystem fileSystem, Path filePath) throws IOException {
        Path destPath = new Path(destination, filePath.getName());
        LOG.info("Moving file {} to {}", filePath, destPath);
        InputStream ins = fileSystem.open(filePath);
        OutputStream os = fileSystem.create(destPath);
        try{
            IOUtils.copy(ins, os);
            fileSystem.delete(filePath,true);
        }catch(Exception e){
            throw new IOException(e);
        }finally{
            ins.close();
            os.close();

        }

        return;
    }
}
