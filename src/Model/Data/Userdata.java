package Model.Data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 管理用户数据的辅助类
 */
public class Userdata {
    private  Map<String,String> usermap;//保存用户数据
    public Userdata(String Username, String Password, String account, String label, String sex, String address, String phone, String head, int age,String background) {
        usermap = new HashMap<>();
        usermap.put("account",account);
        usermap.put("password",Password);
        usermap.put("label",label);
        usermap.put("sex",sex);
        usermap.put("head",head);
        usermap.put("age", String.valueOf(age));
        usermap.put("address",address);
        usermap.put("phone",phone);
        usermap.put("name",Username);
        usermap.put("background",background);

    }
    public Userdata(ResultSet resultSet) throws SQLException {
        usermap = new HashMap<>();
        usermap.put("account",resultSet.getString("account"));
        usermap.put("password",resultSet.getString("password"));
        usermap.put("label",resultSet.getString("label"));
        usermap.put("sex",resultSet.getString("sex"));
        usermap.put("head",resultSet.getString("head"));
        usermap.put("age", resultSet.getString("age"));
        usermap.put("address",resultSet.getString("address"));
        usermap.put("phone",resultSet.getString("phone"));
        usermap.put("name",resultSet.getString("name"));
        usermap.put("background",resultSet.getString("background"));
    }
    public void setUserdata(ResultSet resultSet) throws SQLException {
        usermap.put("account",resultSet.getString("account"));
        usermap.put("password",resultSet.getString("password"));
        usermap.put("label",resultSet.getString("label"));
        usermap.put("sex",resultSet.getString("sex"));
        usermap.put("head",resultSet.getString("head"));
        usermap.put("age", resultSet.getString("age"));
        usermap.put("address",resultSet.getString("address"));
        usermap.put("phone",resultSet.getString("phone"));
        usermap.put("name",resultSet.getString("name"));
        usermap.put("background",resultSet.getString("background"));
    }
    public Map<String,String> getUserdata() {
        return usermap;
    }
    public Userdata() {
        usermap = new HashMap<>();
    }

    public void setPassword(String password){
        usermap.put("password",password);
    }

    public void setName(String name) {
        usermap.put("name",name);

    }

    public String getAge() {
        return usermap.get("age");
    }

    public String getAccount() {
        return usermap.get("account");
    }

    public String getAddress() {
        return usermap.get("address");
    }

    public String getHead() {
        if(usermap.get("head") == null)
            usermap.put("head","head1");
        return usermap.get("head");
    }
    public String getBackground(){

        return usermap.get("background");
    }

    public String getLabel() {
        return usermap.get("label");
    }

    public String getPhone() {
        return usermap.get("phone");
    }

    public String getSex() {
        return usermap.get("sex");
    }

    public void setAccount(String account) {
        usermap.put("account",account);
    }

    public void setAddress(String address) {
        usermap.put("address",address);
    }

    public void setAge(int age) {
        usermap.put("age",String.valueOf(age));
    }

    public void setHead(String head) {
        usermap.put("head",head);
    }

    public void setLabel(String label) {
        usermap.put("label",label);
    }

    public void setPhone(String phone) {
        usermap.put("phone",phone);
    }

    public void setSex(String sex) {
        usermap.put("sex",sex);
    }

    public String getPassword() {
        return usermap.get("password");
    }

    public String getName() {
        return usermap.get("name");
    }
    public void setData(ResultSet resultSet) throws SQLException {
        setName(resultSet.getString("name"));
        setPassword(resultSet.getString("password"));
        setAccount(resultSet.getString("account"));
        setAddress(resultSet.getString("address"));
        setAge(Integer.parseInt(resultSet.getString("age")));
        setHead(resultSet.getString("head"));
        setSex(resultSet.getString("sex"));
        setLabel(resultSet.getString("label"));
        setPhone(resultSet.getString("phone"));

    }
}
