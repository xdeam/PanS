package login.model;


import javax.persistence.Column;

public class Dept {
    private int deptid;
    @Column(name = "dept_name")
    private String deptName;

    public int getDeptid() {
        return deptid;
    }

    public void setDeptid(int deptid) {
        this.deptid = deptid;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    @Override
    public String toString() {
        return "Dept{" +
                "deptid=" + deptid +
                ", deptName='" + deptName + '\'' +
                '}';
    }
}
