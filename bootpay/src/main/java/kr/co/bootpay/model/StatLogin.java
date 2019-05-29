package kr.co.bootpay.model;

public class StatLogin {
    public String ver;
    public String application_id;
    public String id;
    public String email;
    public String username;
    public int gender;
    public String birth;
    public String phone;
    public String area;

    public StatLogin(String ver,
                     String application_id,
                     String id,
                     String email,
                     String username,
                     int gender,
                     String birth,
                     String phone,
                     String area) {
        this.ver = ver;
        this.application_id = application_id;
        this.id = id;
        this.email = email;
        this.username = username;
        this.gender = gender;
        this.birth = birth;
        this.phone = phone;
        this.area = area;
    }
}
