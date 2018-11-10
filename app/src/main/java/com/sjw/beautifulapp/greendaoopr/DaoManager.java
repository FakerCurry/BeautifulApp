package com.sjw.beautifulapp.greendaoopr;


import com.sjw.beautifulapp.application.AppApplication;
import com.sjw.beautifulapp.bean.LoveFileBean;
import com.sjw.beautifulapp.greendao.DaoSession;
import com.sjw.beautifulapp.greendao.LoveFileBeanDao;

import java.util.List;

/**
 * Created by pc on 2018/7/21.
 */

public class DaoManager {
    //用户
    DaoSession daoSession = AppApplication.getInstances().getDaoSession();


    private static DaoManager instance = null;

    public static synchronized DaoManager getInstance() {
        // 这个方法比上面有所改进，不用每次都进行生成对象，只是第一次
        // 使用时生成实例，提高了效率！
        if (instance == null)
            instance = new DaoManager();

        return instance;

    }


    //==================================用户表=======================================

    /**
     * 新增文件名
     */
    public void insertFile(LoveFileBean file) {
        daoSession.getLoveFileBeanDao().insert(file);
    }

    /**
     * 删除文件名（主键）
     */
    public void deleteByKeyFile(Long key) {

        daoSession.getLoveFileBeanDao().deleteByKey(key);

    }

    /**
     * 删除文件名（文件）
     */
    public void deleteByKeyFile(LoveFileBean file) {

        daoSession.getLoveFileBeanDao().delete(file);

    }

    /**
     * 删除所有文件名
     */
    public void deleteAllFile() {

        daoSession.getLoveFileBeanDao().deleteAll();

    }

    /**
     * 修改文件类
     */
    public void updateFile(LoveFileBean file) {

        daoSession.getLoveFileBeanDao().update(file);

    }


    /**
     * 查询所有文件
     */
    public List<LoveFileBean> searchAllFile() {
        return daoSession.getLoveFileBeanDao().loadAll();
//        return userDao.queryBuilder().list();
    }

    /**
     * 根据主键查询用户
     */
    public LoveFileBean searchByKeyFile(Long key) {
        return daoSession.getLoveFileBeanDao().load(key);
    }

    /**
     * 根据条件查询(这里根据文件路径)
     */
    public List<LoveFileBean> searchByConditionFile(String filePath) {
        // .eq(userName)
        //queryRaw(String where,String selectionArg)：返回：List
        return daoSession.getLoveFileBeanDao().queryBuilder().where(LoveFileBeanDao.Properties.FilePath.like(filePath)).list();
    }

    /**
     * 根据条件查询(这里根据文件类型)
     */
    public List<LoveFileBean> searchByType(String type) {
        // .eq(userName)
        //queryRaw(String where,String selectionArg)：返回：List
        return daoSession.getLoveFileBeanDao().queryBuilder().where(LoveFileBeanDao.Properties.IsType.like(type)).list();
    }
}
