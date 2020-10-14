package io.drew.record.service.bean.response;

import java.io.Serializable;

/**
 * @Author: KKK
 * @CreateDate: 2020/9/1 11:16 AM
 */
public class OssRecord implements Serializable {

    /**
     * SecurityToken : CAIS3wJ1q6Ft5B2yfSjIr5b5GPL6o6xS46SgZXH2o00XSfp12afSuzz2IHlPe3FhAOoev/k/mW9U7foclrMqEcAfGRydPJAts8gJqVj/JpLFst2J6r8JjsVGuPJFp1ipsvXJasDVEfkuE5XEMiI5/00e6L/+cirYXD7BGJaViJlhQ80KVw2jF1RvD8tXIQ0Qk619K3zdZ9mgLibui3vxCkRv2HBijm8txqmj/MyQ5x31i1v0y+B3wYHtOcqca8B9MY1WTsu1vohzarGT6CpZ+jlM+qAU6qlY4mXrs9qHEkFNwBiXSZ22lOdiNwhkfKM3NrdZpfzn751Ct/fUip78xmQmX4gXcVyGGtDxkZOZQrzzbY5hK+igARmXjIDTbKuSmhg/fHcWODlNf9ccMXJqAXQuMGqDcfD/qQmQOlb+G/XajPpqj4AJ5lHp7MeMGV+DeLyQyh0EIaU7a044qL6obt8XmsQagAE7QAp0Rc5SXYaIWM/VAvuC9udy0gs+IpcGmrn5SfFzgBYir1TsKk8cXWNCAv53svs6h/8BFSfuLKdT1RaCH7E00kQWmksy+n+STYOx9nJ/dzur4cwX5KtL6XP7havrOsvsdf5qId1sL0zeqnbLnQUmtoUtazAZlqhSzKS5aCO3yA==
     * AuthInfo : {"CI":"5AyJHIwC/S6uxmHifHwRL2I6s/QDGyPUbqkk2jzAHlpjTH69nGREwnl9Gmi7bVDuKOxgqcLkoCV3\r\nG8P5xxUxhBzehWEyWdrVRqpdHyF+W3I=\r\n","Caller":"z0gYOEwn8DPFHldjXeUVzkPBGaUr8PMi5L3xluUl7Ag=\r\n","ExpireTime":"2020-09-01T03:15:05Z","MediaId":"35b31c11d18e47ffad5d0f38b9cc6c6b","PlayDomain":"vod.hualeme.com","Signature":"yWJ+hEUnnXZ3ossiltjYTYLlfj0="}
     * VideoMeta : {"Status":"Normal","VideoId":"35b31c11d18e47ffad5d0f38b9cc6c6b","Title":"test","CoverURL":"http://vod.hualeme.com/35b31c11d18e47ffad5d0f38b9cc6c6b/snapshots/b1df5b2175d74a6d998b1dc5bf012d5e-00003.jpg?auth_key=1598930005-ed11106303f44189b5b11ae804b4ff57-0-9644ec624f4252e3b68b9b8cc0efe152","Duration":0}
     * AccessKeyId : STS.NULSHNNssTfKgWGGMBEuY6byY
     * PlayDomain : vod.hualeme.com
     * AccessKeySecret : 8jRaXpXqx1NyKXkSHKJn8gVprE8qprtjuCsreYycFAQ4
     * Region : cn-shanghai
     * CustomerId : 1272481935546036
     * playAuth : eyJTZWN1cml0eVRva2VuIjoiQ0FJUzN3SjFxNkZ0NUIyeWZTaklyNWI1R1BMNm82eFM0NlNnWlhIMm8wMFhTZnAxMmFmU3V6ejJJSGxQZTNGaEFPb2V2L2svbVc5VTdmb2Nsck1xRWNBZkdSeWRQSkF0czhnSnFWai9KcExGc3QySjZyOEpqc1ZHdVBKRnAxaXBzdlhKYXNEVkVma3VFNVhFTWlJNS8wMGU2TC8rY2lyWVhEN0JHSmFWaUpsaFE4MEtWdzJqRjFSdkQ4dFhJUTBRazYxOUszemRaOW1nTGlidWkzdnhDa1J2MkhCaWptOHR4cW1qL015UTV4MzFpMXYweStCM3dZSHRPY3FjYThCOU1ZMVdUc3Uxdm9oemFyR1Q2Q3BaK2psTStxQVU2cWxZNG1YcnM5cUhFa0ZOd0JpWFNaMjJsT2RpTndoa2ZLTTNOcmRacGZ6bjc1MUN0L2ZVaXA3OHhtUW1YNGdYY1Z5R0d0RHhrWk9aUXJ6emJZNWhLK2lnQVJtWGpJRFRiS3VTbWhnL2ZIY1dPRGxOZjljY01YSnFBWFF1TUdxRGNmRC9xUW1RT2xiK0cvWGFqUHBxajRBSjVsSHA3TWVNR1YrRGVMeVF5aDBFSWFVN2EwNDRxTDZvYnQ4WG1zUWFnQUU3UUFwMFJjNVNYWWFJV00vVkF2dUM5dWR5MGdzK0lwY0dtcm41U2ZGemdCWWlyMVRzS2s4Y1hXTkNBdjUzc3ZzNmgvOEJGU2Z1TEtkVDFSYUNIN0UwMGtRV21rc3krbitTVFlPeDluSi9kenVyNGN3WDVLdEw2WFA3aGF2ck9zdnNkZjVxSWQxc0wwemVxbmJMblFVbXRvVXRhekFabHFoU3pLUzVhQ08zeUE9PSIsIkF1dGhJbmZvIjoie1wiQ0lcIjpcIjVBeUpISXdDL1M2dXhtSGlmSHdSTDJJNnMvUURHeVBVYnFrazJqekFIbHBqVEg2OW5HUkV3bmw5R21pN2JWRHVLT3hncWNMa29DVjNcXHJcXG5HOFA1eHhVeGhCemVoV0V5V2RyVlJxcGRIeUYrVzNJPVxcclxcblwiLFwiQ2FsbGVyXCI6XCJ6MGdZT0V3bjhEUEZIbGRqWGVVVnprUEJHYVVyOFBNaTVMM3hsdVVsN0FnPVxcclxcblwiLFwiRXhwaXJlVGltZVwiOlwiMjAyMC0wOS0wMVQwMzoxNTowNVpcIixcIk1lZGlhSWRcIjpcIjM1YjMxYzExZDE4ZTQ3ZmZhZDVkMGYzOGI5Y2M2YzZiXCIsXCJQbGF5RG9tYWluXCI6XCJ2b2QuaHVhbGVtZS5jb21cIixcIlNpZ25hdHVyZVwiOlwieVdKK2hFVW5uWFozb3NzaWx0allUWUxsZmowPVwifSIsIlZpZGVvTWV0YSI6eyJTdGF0dXMiOiJOb3JtYWwiLCJWaWRlb0lkIjoiMzViMzFjMTFkMThlNDdmZmFkNWQwZjM4YjljYzZjNmIiLCJUaXRsZSI6InRlc3QiLCJDb3ZlclVSTCI6Imh0dHA6Ly92b2QuaHVhbGVtZS5jb20vMzViMzFjMTFkMThlNDdmZmFkNWQwZjM4YjljYzZjNmIvc25hcHNob3RzL2IxZGY1YjIxNzVkNzRhNmQ5OThiMWRjNWJmMDEyZDVlLTAwMDAzLmpwZz9hdXRoX2tleT0xNTk4OTMwMDA1LWVkMTExMDYzMDNmNDQxODliNWIxMWFlODA0YjRmZjU3LTAtOTY0NGVjNjI0ZjQyNTJlM2I2OGI5YjhjYzBlZmUxNTIiLCJEdXJhdGlvbiI6MC4wfSwiQWNjZXNzS2V5SWQiOiJTVFMuTlVMU0hOTnNzVGZLZ1dHR01CRXVZNmJ5WSIsIlBsYXlEb21haW4iOiJ2b2QuaHVhbGVtZS5jb20iLCJBY2Nlc3NLZXlTZWNyZXQiOiI4alJhWHBYcXgxTnlLWGtTSEtKbjhnVnByRThxcHJ0anVDc3JlWXljRkFRNCIsIlJlZ2lvbiI6ImNuLXNoYW5naGFpIiwiQ3VzdG9tZXJJZCI6MTI3MjQ4MTkzNTU0NjAzNn0=
     * videoId : 35b31c11d18e47ffad5d0f38b9cc6c6b
     */

    private String SecurityToken;
    private String AuthInfo;
    private VideoMetaBean VideoMeta;
    private String AccessKeyId;
    private String PlayDomain;
    private String AccessKeySecret;
    private String Region;
    private long CustomerId;
    private String playAuth;
    private String videoId;

    public String getSecurityToken() {
        return SecurityToken;
    }

    public void setSecurityToken(String SecurityToken) {
        this.SecurityToken = SecurityToken;
    }

    public String getAuthInfo() {
        return AuthInfo;
    }

    public void setAuthInfo(String AuthInfo) {
        this.AuthInfo = AuthInfo;
    }

    public VideoMetaBean getVideoMeta() {
        return VideoMeta;
    }

    public void setVideoMeta(VideoMetaBean VideoMeta) {
        this.VideoMeta = VideoMeta;
    }

    public String getAccessKeyId() {
        return AccessKeyId;
    }

    public void setAccessKeyId(String AccessKeyId) {
        this.AccessKeyId = AccessKeyId;
    }

    public String getPlayDomain() {
        return PlayDomain;
    }

    public void setPlayDomain(String PlayDomain) {
        this.PlayDomain = PlayDomain;
    }

    public String getAccessKeySecret() {
        return AccessKeySecret;
    }

    public void setAccessKeySecret(String AccessKeySecret) {
        this.AccessKeySecret = AccessKeySecret;
    }

    public String getRegion() {
        return Region;
    }

    public void setRegion(String Region) {
        this.Region = Region;
    }

    public long getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(long CustomerId) {
        this.CustomerId = CustomerId;
    }

    public String getPlayAuth() {
        return playAuth;
    }

    public void setPlayAuth(String playAuth) {
        this.playAuth = playAuth;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public static class VideoMetaBean implements Serializable{
        /**
         * Status : Normal
         * VideoId : 35b31c11d18e47ffad5d0f38b9cc6c6b
         * Title : test
         * CoverURL : http://vod.hualeme.com/35b31c11d18e47ffad5d0f38b9cc6c6b/snapshots/b1df5b2175d74a6d998b1dc5bf012d5e-00003.jpg?auth_key=1598930005-ed11106303f44189b5b11ae804b4ff57-0-9644ec624f4252e3b68b9b8cc0efe152
         * Duration : 0
         */

        private String Status;
        private String VideoId;
        private String Title;
        private String CoverURL;
        private Double Duration;

        public String getStatus() {
            return Status;
        }

        public void setStatus(String Status) {
            this.Status = Status;
        }

        public String getVideoId() {
            return VideoId;
        }

        public void setVideoId(String VideoId) {
            this.VideoId = VideoId;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public String getCoverURL() {
            return CoverURL;
        }

        public void setCoverURL(String CoverURL) {
            this.CoverURL = CoverURL;
        }

        public Double getDuration() {
            return Duration;
        }

        public void setDuration(Double Duration) {
            this.Duration = Duration;
        }
    }
}
