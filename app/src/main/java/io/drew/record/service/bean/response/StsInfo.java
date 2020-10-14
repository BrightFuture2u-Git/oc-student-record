package io.drew.record.service.bean.response;

/**
 * @Author: KKK
 * @CreateDate: 2020/5/15 4:43 PM
 */
public class StsInfo {

    /**
     * RequestID : D8AF4381-979F-42B2-9928-E1B986C9B568
     * OssEndpoint : oss-cn-shanghai.aliyuncs.com
     * Expiration : 2020-05-15T09:42:11Z
     * KS : FMB8KH47zZqmTPpm6FqnT6xzjH1VEnG1Uyr4LKtFLDvS
     * AK : STS.NURSyTEt4afFFnnRHF8JjEZwK
     * Token : CAIStwN1q6Ft5B2yfSjIr5bnGMPgqKsV1qStREjftkgTNMVGqp/cqTz2IHxJeXJrBOwes/kwmGxZ7vcflrh+W4NIX0rNaY5t9ZlN9wqkbtIyMX1gJPpW5qe+EE2/VjRmta27Opd+JbGwU/OpbE++oU0X6LDmdDKkckW4OJmS8/BOZcgWWQ/KQlgjA8xNdCRvtOgQN3baKZTINQXx0FLNEG1iuAd3lQEa7r+kkOmd8QbmhUfm0Y1loJ/qcLGuaNNwGYp7T9at1fZqS7LF1ydckfQjlsFMgbdD5C3Ms/yEHlBV+G/mCefP9cB1JwILZspFEqVf/v/njq866K6BnYqyyhNEMuxOFGa9O4mr25nDA/j7dIR7J7z1fnndyteVKt630XwtamlJMxhRKZhzaC16B1ktTyndMLPgujK5awy4Ga+ey/N0g9gnxVut49uGKl+UBq2E22FaGOdlNRxya05PjDe8L/NYK1cSST49WebJF7cURQtFtKblsTfVUiBd1XxNt5X8HaiK5/1EMtijBs8bjtZBNcwX7nFZRlD2Wq+ojVwPaGtmTLBZ3a/gI5aj76Wfx+GecTID41bjeJwwGoABUIXoewQV/szYNpYiuDpa6NNC7404GLIjIBGLxctrra6r43dPENaVEmt03u0jHnglmHig4OBcd9z3Ah1UwBg4q/NhNhQY2RDd2Tzg7V+Ap2Hnw9EKjHzbBWMDzgbXFGfruNzWB+vBE4Ytk6d9/Fjd7keWX3TuqPbUlMGeu0NNLu8=
     */

    private String RequestID;
    private String OssEndpoint;
    private String Expiration;
    private String KS;
    private String AK;
    private String Token;

    public String getRequestID() {
        return RequestID;
    }

    public void setRequestID(String RequestID) {
        this.RequestID = RequestID;
    }

    public String getOssEndpoint() {
        return OssEndpoint;
    }

    public void setOssEndpoint(String OssEndpoint) {
        this.OssEndpoint = OssEndpoint;
    }

    public String getExpiration() {
        return Expiration;
    }

    public void setExpiration(String Expiration) {
        this.Expiration = Expiration;
    }

    public String getKS() {
        return KS;
    }

    public void setKS(String KS) {
        this.KS = KS;
    }

    public String getAK() {
        return AK;
    }

    public void setAK(String AK) {
        this.AK = AK;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String Token) {
        this.Token = Token;
    }
}
