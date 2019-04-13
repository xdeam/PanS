package login.model;

import org.springframework.stereotype.Component;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Component
@Table(name = "user")
@NameStyle(Style.normal)
public class User {
    @Id
    @KeySql(useGeneratedKeys = true)
    @Column(name = "uid")
    int uid;
    @Column(name = "uname")
    String uname;
    @Column(name = "passwd")
    String passwd;

    Dept dept;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", uname='" + uname + '\'' +
                ", passwd='" + passwd + '\'' +
                ", dept=" + dept +
                '}';
    }
}
