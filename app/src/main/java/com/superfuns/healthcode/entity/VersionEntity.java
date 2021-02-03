package com.superfuns.healthcode.entity;

import android.text.TextUtils;

/**
 * Created by DELL on 2018/8/2.
 */

public class VersionEntity {
        /**
         * id :
         * name : sdfsdf
         * applicationName : img
         * versionNumber :
         * url : \app\图片.jpg
         * remark :
         * createTime : 2018-07-31 19:55:03
         * versionName :
         */

        private String id;

        public float getName() {
            return name;
        }

        public void setName(float name) {
            this.name = name;
        }

        //版本号
        private float name;
        private String applicationName;


        private String url;
        private String remark;
        private String createTime;
        private String versionName;
        private int versionNumber;
        private String housing_id;


    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }


        public String getApplicationName() {
            return applicationName;
        }

        public void setApplicationName(String applicationName) {
            this.applicationName = applicationName;
        }

        public String getHousing_id() {
            return housing_id;
        }

        public void setHousing_id(String housing_id) {
            this.housing_id = housing_id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

//    public boolean isSuccess() {
//        return status == 2 && data != null && (!TextUtils.isEmpty(data.getUrl())) && data.getName() > 0;
//    }

}

