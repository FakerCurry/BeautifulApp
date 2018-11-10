package com.sjw.beautifulapp.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class LoveFileBean {

    @Id(autoincrement = true)
    private Long love_id;
    private String fileTitle;
    private String filePath;
    private String isType;
    //判断是否处于编辑状态
    private boolean  isEdit;
    //是否处于选中装套
    private boolean isSelect;

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isCut() {
        return isCut;
    }

    public void setCut(boolean cut) {
        isCut = cut;
    }

    public boolean isCompressed() {
        return compressed;
    }

    public Long getLove_id() {
        return this.love_id;
    }

    public void setLove_id(Long love_id) {
        this.love_id = love_id;
    }

    public String getFileTitle() {
        return this.fileTitle;
    }

    public void setFileTitle(String fileTitle) {
        this.fileTitle = fileTitle;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getIsType() {
        return this.isType;
    }

    public void setIsType(String isType) {
        this.isType = isType;
    }

    public boolean getIsEdit() {
        return this.isEdit;
    }

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    public boolean getIsSelect() {
        return this.isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCompressPath() {
        return this.compressPath;
    }

    public void setCompressPath(String compressPath) {
        this.compressPath = compressPath;
    }

    public String getCutPath() {
        return this.cutPath;
    }

    public void setCutPath(String cutPath) {
        this.cutPath = cutPath;
    }

    public long getDuration() {
        return this.duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public boolean getIsChecked() {
        return this.isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public boolean getIsCut() {
        return this.isCut;
    }

    public void setIsCut(boolean isCut) {
        this.isCut = isCut;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getNum() {
        return this.num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getMimeType() {
        return this.mimeType;
    }

    public void setMimeType(int mimeType) {
        this.mimeType = mimeType;
    }

    public String getPictureType() {
        return this.pictureType;
    }

    public void setPictureType(String pictureType) {
        this.pictureType = pictureType;
    }

    public boolean getCompressed() {
        return this.compressed;
    }

    public void setCompressed(boolean compressed) {
        this.compressed = compressed;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    private String path;
    private String compressPath;
    private String cutPath;
    private long duration;
    private boolean isChecked;
    private boolean isCut;
    public int position;
    private int num;
    private int mimeType;
    private String pictureType;
    private boolean compressed;
    private int width;
    private int height;

    @Generated(hash = 918448980)
    public LoveFileBean(Long love_id, String fileTitle, String filePath,
            String isType, boolean isEdit, boolean isSelect, String path,
            String compressPath, String cutPath, long duration, boolean isChecked,
            boolean isCut, int position, int num, int mimeType, String pictureType,
            boolean compressed, int width, int height) {
        this.love_id = love_id;
        this.fileTitle = fileTitle;
        this.filePath = filePath;
        this.isType = isType;
        this.isEdit = isEdit;
        this.isSelect = isSelect;
        this.path = path;
        this.compressPath = compressPath;
        this.cutPath = cutPath;
        this.duration = duration;
        this.isChecked = isChecked;
        this.isCut = isCut;
        this.position = position;
        this.num = num;
        this.mimeType = mimeType;
        this.pictureType = pictureType;
        this.compressed = compressed;
        this.width = width;
        this.height = height;
    }

    @Generated(hash = 194160180)
    public LoveFileBean() {
    }
  





}
