package kcl.paramount.group.dao;

public interface FileDao {
    Boolean checkPreDic(String username, String url);
    Boolean checkPreFile(String username, String url);
    Boolean delectInDir(String username, String url);
    Boolean delectInFile(String username, String url);
    Boolean checkFile(String username, String url);
    Boolean addFile(String username, String url, String time, String pre, long size);
    Boolean addDir(String username, String url, String prel);
    Boolean lock(String username, String url);
    Boolean unlock(String username, String url);
    Boolean isLock(String username, String url);
}