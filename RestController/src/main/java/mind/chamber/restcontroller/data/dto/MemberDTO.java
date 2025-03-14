package mind.chamber.restcontroller.data.dto;

/*
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

// 위의 내용 대신 ALT+INSERT 를 통해서도 아래와 같이 생성 시킬 수 있다.

*/

public class MemberDTO {
    private String name;
    private String email;
    private String org;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    @Override
    public String toString() {
        return "MemberDTO{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", org='" + org + '\'' +
                '}';
    }
}
