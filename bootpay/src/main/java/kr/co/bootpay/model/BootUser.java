package kr.co.bootpay.model;

import com.google.gson.Gson;

import java.util.Locale;

public class BootUser {
    private String id; //개발사가 발급한 고유 아이디
    private String username;
    private String birth;
    private String email;
    private int gender = -1;
    private String area;
    private String phone;

    public BootUser setID(String value) {
        this.id = value;
        return this;
    }

    public BootUser setUsername(String value) {
        this.username = value;
        return this;
    }

    public BootUser setBirth(String value) {
        this.birth = value;
        return this;
    }

    public BootUser setEmail(String value) {
        this.email = value;
        return this;
    }

    public BootUser setGender(int value) {
        this.gender = value;
        return this;
    }

    public BootUser setArea(String value) {
        this.area = value;
        return this;
    }

    public BootUser setPhone(String value) {
        this.phone = value;
        return this;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getBirth() {
        return birth;
    }

    public String getEmail() {
        return email;
    }

    public int getGender() {
        return gender;
    }

    public String getArea() {
        return area;
    }

    public String getPhone() {
        return phone;
    }

    private String user(String... etcs) {
        StringBuilder sb = new StringBuilder();
        for(String str : etcs) {
            if(str.length() == 0) continue;
            if(sb.toString().length() > 0) sb.append(",");
            sb.append(str);
        }
        return String.format(Locale.KOREA,"{%s}", sb.toString());
    }

    private String id() {
        if(this.id == null) return "";
        return String.format(Locale.KOREA, "id: '%s'", this.id);
    }

    private String username() {
        if(this.username == null) return "";
        return String.format(Locale.KOREA, "username: '%s'", this.username);
    }

    private String birth() {
        if(this.birth == null) return "";
        return String.format(Locale.KOREA, "birth: '%s'", this.birth);
    }

    private String phone() {
        if(this.phone == null) return "";
        return String.format(Locale.KOREA, "phone: '%s'", this.phone);
    }

    private String email() {
        if(this.email == null) return "";
        return String.format(Locale.KOREA, "email: '%s'", this.email);
    }

    private String gender() {
        return String.format(Locale.KOREA, "gender: %d", this.gender);
    }

    private String area() {
        if(this.area == null) return "";
        return String.format(Locale.KOREA, "area: '%s'", this.area);
    }

    public String toJson() {
        return user(
                id(),
                username(),
                birth(),
                phone(),
                email(),
                gender(),
                area()
        );
    }

    public final String toGson() {
        if(this == null) return "";
        return new Gson().toJson(this);
    }
}
