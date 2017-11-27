package top.zeroyiq.master_help_me.models;

/**
 *  app 更新信息
 * Created by ZeroyiQ on 2017/6/4.
 */

public class UpdateInfo extends BaseRecord {
    private  int id;
    private String name;
    private String path;
    private String version;
    private int  versionCode;
    private String updateTime;
    private String versioninfo;
    private String updateSize;

    public UpdateInfo(int id, String name, String path, String version, int versionCode, String updateTime, String versioninfo, String updateSize) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.version = version;
        this.versionCode = versionCode;
        this.updateTime = updateTime;
        this.versioninfo = versioninfo;
        this.updateSize = updateSize;
    }

    public String getVersioninfo() {
        return versioninfo;
    }

    public void setVersioninfo(String versioninfo) {
        this.versioninfo = versioninfo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateSize() {
        return updateSize;
    }

    public void setUpdateSize(String updateSize) {
        this.updateSize = updateSize;
    }


    @Override
    public String toString() {
        return "发现新版本："+getVersion()+
                "\n更新内容："+getVersioninfo()+
                "\n更新时间："+getUpdateTime()+
                "\n更新包大小:"+getUpdateSize()+"M";
    }
}

