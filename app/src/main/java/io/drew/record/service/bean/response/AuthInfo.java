package io.drew.record.service.bean.response;

import java.io.Serializable;
import java.util.List;

public class AuthInfo implements Serializable {


    /**
     * token : bearer eyJ0eXAiOiJKc29uV2ViVG9rZW4iLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJpc3N1c2VyIiwiYXVkIjoiYXVkaWVuY2UiLCJyb2xlX25hbWUiOiIiLCJ1c2VyX1R5cGUiOiIwIiwidXNlcl9pZCI6InVzcl8wMDAwNyIsInRva2VuX3R5cGUiOiJhY2Nlc3NfdG9rZW4iLCJleHAiOjE1OTIzNjk3NDMsIm5iZiI6MTU5MTc2NDk0M30.XCtrIQu-wUEYCMmWhse17QAk3ZFUskbSFDF-Y2A2WQc
     * refreshToken : eyJ0eXAiOiJKc29uV2ViVG9rZW4iLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJpc3N1c2VyIiwiYXVkIjoiYXVkaWVuY2UiLCJ1c2VyX2lkIjoidXNyXzAwMDA3IiwidG9rZW5fdHlwZSI6InJlZnJlc2hfdG9rZW4iLCJleHAiOjE1OTI5NzQ1NDMsIm5iZiI6MTU5MTc2NDk0M30.1pjNEQKRgDr1YKPVorXN_zssmar6ECc7_F3pGgM834Y
     * user : {"createTime":null,"updateTime":"2020-06-10 10:13:20","status":1,"id":"usr_00007","phone":"18900000005","nickname":"啦啦啦啦","headImage":"https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1591007126821_IMG_20200515_113911.jpg","realName":null,"gender":0,"identity":null,"bySelf":1,"weiXin":null,"email":null,"consigneeName":null,"consigneePhone":null,"consigneeAddress":null,"studentList":[{"createTime":"2020-04-28 15:00:10","updateTime":"2020-06-10 10:07:48","status":1,"id":"stu_80b4c3f714d94d02baa9c2f23c907dee","userId":"usr_00007","realName":"小小康","nickname":null,"birthday":"2020-04-28","gender":1,"age":null,"freeClassHour":9,"freeRecordCourseNum":1,"isDefault":1,"bySelf":0,"no":100308,"grade":null,"vipList":[]},{"createTime":"2020-04-28 15:00:29","updateTime":"2020-04-28 15:00:29","status":1,"id":"stu_da27396bec634e7d9c0af1995e4d75b3","userId":"usr_00007","realName":"小小小康","nickname":null,"birthday":"2020-04-07","gender":1,"age":null,"freeClassHour":9,"freeRecordCourseNum":2,"isDefault":0,"bySelf":0,"no":100309,"grade":null,"vipList":[]},{"createTime":null,"updateTime":"2020-06-10 10:13:20","status":1,"id":"stu_00009","userId":"usr_00007","realName":"小康","nickname":null,"birthday":"2020-04-23","gender":1,"age":null,"freeClassHour":1,"freeRecordCourseNum":1,"isDefault":0,"bySelf":1,"no":100232,"grade":null,"vipList":[]}]}
     */

    private String token;
    private String refreshToken;
    private UserBean user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public static class UserBean implements Serializable{
        /**
         * createTime : null
         * updateTime : 2020-06-10 10:13:20
         * status : 1
         * id : usr_00007
         * phone : 18900000005
         * nickname : 啦啦啦啦
         * headImage : https://bf-images.oss-cn-shanghai.aliyuncs.com/fourm_18900000005_1591007126821_IMG_20200515_113911.jpg
         * realName : null
         * gender : 0
         * identity : null
         * bySelf : 1
         * weiXin : null
         * email : null
         * consigneeName : null
         * consigneePhone : null
         * consigneeAddress : null
         * studentList : [{"createTime":"2020-04-28 15:00:10","updateTime":"2020-06-10 10:07:48","status":1,"id":"stu_80b4c3f714d94d02baa9c2f23c907dee","userId":"usr_00007","realName":"小小康","nickname":null,"birthday":"2020-04-28","gender":1,"age":null,"freeClassHour":9,"freeRecordCourseNum":1,"isDefault":1,"bySelf":0,"no":100308,"grade":null,"vipList":[]},{"createTime":"2020-04-28 15:00:29","updateTime":"2020-04-28 15:00:29","status":1,"id":"stu_da27396bec634e7d9c0af1995e4d75b3","userId":"usr_00007","realName":"小小小康","nickname":null,"birthday":"2020-04-07","gender":1,"age":null,"freeClassHour":9,"freeRecordCourseNum":2,"isDefault":0,"bySelf":0,"no":100309,"grade":null,"vipList":[]},{"createTime":null,"updateTime":"2020-06-10 10:13:20","status":1,"id":"stu_00009","userId":"usr_00007","realName":"小康","nickname":null,"birthday":"2020-04-23","gender":1,"age":null,"freeClassHour":1,"freeRecordCourseNum":1,"isDefault":0,"bySelf":1,"no":100232,"grade":null,"vipList":[]}]
         */

        private Object createTime;
        private String updateTime;
        private int status;
        private String id;
        private String phone;
        private String nickname;
        private String headImage;
        private Object realName;
        private int gender;
        private Object identity;
        private int bySelf;
        private int hadHuaLeMeVip;
        private Object weiXin;
        private Object email;
        private Object consigneeName;
        private Object consigneePhone;
        private Object consigneeAddress;
        private List<StudentListBean> studentList;

        public int getHadHuaLeMeVip() {
            return hadHuaLeMeVip;
        }

        public void setHadHuaLeMeVip(int hadHuaLeMeVip) {
            this.hadHuaLeMeVip = hadHuaLeMeVip;
        }

        public Object getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Object createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getHeadImage() {
            return headImage;
        }

        public void setHeadImage(String headImage) {
            this.headImage = headImage;
        }

        public Object getRealName() {
            return realName;
        }

        public void setRealName(Object realName) {
            this.realName = realName;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public Object getIdentity() {
            return identity;
        }

        public void setIdentity(Object identity) {
            this.identity = identity;
        }

        public int getBySelf() {
            return bySelf;
        }

        public void setBySelf(int bySelf) {
            this.bySelf = bySelf;
        }

        public Object getWeiXin() {
            return weiXin;
        }

        public void setWeiXin(Object weiXin) {
            this.weiXin = weiXin;
        }

        public Object getEmail() {
            return email;
        }

        public void setEmail(Object email) {
            this.email = email;
        }

        public Object getConsigneeName() {
            return consigneeName;
        }

        public void setConsigneeName(Object consigneeName) {
            this.consigneeName = consigneeName;
        }

        public Object getConsigneePhone() {
            return consigneePhone;
        }

        public void setConsigneePhone(Object consigneePhone) {
            this.consigneePhone = consigneePhone;
        }

        public Object getConsigneeAddress() {
            return consigneeAddress;
        }

        public void setConsigneeAddress(Object consigneeAddress) {
            this.consigneeAddress = consigneeAddress;
        }

        public List<StudentListBean> getStudentList() {
            return studentList;
        }

        public void setStudentList(List<StudentListBean> studentList) {
            this.studentList = studentList;
        }

        public static class StudentListBean implements Serializable{
            /**
             * createTime : 2020-04-28 15:00:10
             * updateTime : 2020-06-10 10:07:48
             * status : 1
             * id : stu_80b4c3f714d94d02baa9c2f23c907dee
             * userId : usr_00007
             * realName : 小小康
             * nickname : null
             * birthday : 2020-04-28
             * gender : 1
             * age : null
             * freeClassHour : 9
             * freeRecordCourseNum : 1
             * isDefault : 1
             * bySelf : 0
             * no : 100308
             * grade : null
             * vipList : []
             */

            private String createTime;
            private String updateTime;
            private int status;
            private String id;
            private String userId;
            private String realName;
            private Object nickname;
            private String birthday;
            private String avatar;
            private int gender;
            private Object age;
            private int freeClassHour;
            private int freeRecordCourseNum;
            private int isDefault;
            private int bySelf;
            private int no;
            private Object grade;
            private List<?> vipList;

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(String updateTime) {
                this.updateTime = updateTime;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getRealName() {
                return realName;
            }

            public void setRealName(String realName) {
                this.realName = realName;
            }

            public Object getNickname() {
                return nickname;
            }

            public void setNickname(Object nickname) {
                this.nickname = nickname;
            }

            public String getBirthday() {
                return birthday;
            }

            public void setBirthday(String birthday) {
                this.birthday = birthday;
            }

            public int getGender() {
                return gender;
            }

            public void setGender(int gender) {
                this.gender = gender;
            }

            public Object getAge() {
                return age;
            }

            public void setAge(Object age) {
                this.age = age;
            }

            public int getFreeClassHour() {
                return freeClassHour;
            }

            public void setFreeClassHour(int freeClassHour) {
                this.freeClassHour = freeClassHour;
            }

            public int getFreeRecordCourseNum() {
                return freeRecordCourseNum;
            }

            public void setFreeRecordCourseNum(int freeRecordCourseNum) {
                this.freeRecordCourseNum = freeRecordCourseNum;
            }

            public int getIsDefault() {
                return isDefault;
            }

            public void setIsDefault(int isDefault) {
                this.isDefault = isDefault;
            }

            public int getBySelf() {
                return bySelf;
            }

            public void setBySelf(int bySelf) {
                this.bySelf = bySelf;
            }

            public int getNo() {
                return no;
            }

            public void setNo(int no) {
                this.no = no;
            }

            public Object getGrade() {
                return grade;
            }

            public void setGrade(Object grade) {
                this.grade = grade;
            }

            public List<?> getVipList() {
                return vipList;
            }

            public void setVipList(List<?> vipList) {
                this.vipList = vipList;
            }
        }
    }
}
