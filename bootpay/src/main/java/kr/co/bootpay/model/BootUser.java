package kr.co.bootpay.model;

import com.google.gson.Gson;

import java.util.Locale;

public class BootUser {
    private String id; //개발사가 발급한 고유 아이디
    private String username;  // 구매자 명
    private String birth; //생년월일 "1986-10-14"
    private String email; //구매자의 이메일 정보
    private int gender = -1; //1:남자 0:여자
    private String area; // [서울,인천,대구,광주,부산,울산,경기,강원,충청북도,충북,충청남도,충남,전라북도,전북,전라남도,전남,경상북도,경북,경상남도,경남,제주,세종,대전] 중 택 1
    private String phone; //구매자의 전화번호 (페이앱 필수)

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
