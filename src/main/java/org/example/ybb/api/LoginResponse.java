package org.example.ybb.api;

public class LoginResponse {
    private String message;
    private String token;

    // 构造方法
    public LoginResponse(String message, String token) {
        this.message = message;
        this.token = token;
    }

    // Getter 和 Setter 方法
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    // 重写toString()方法，方便调试时查看内容
    @Override
    public String toString() {
        return "LoginResponse{" +
                "message='" + message + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
