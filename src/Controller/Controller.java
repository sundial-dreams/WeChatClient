package Controller;

import Model.ChatManager;
import Model.Data.MsgData;
import Model.Data.Userdata;
import Model.DatabaseModel;
import View.Alert;
import View.*;
import View.Dialog;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Pattern;

/**
 * 页面控制类 所有页面的控制 包含的方法  exec() 和 $()
 * 包含的属性为View包下的页面类
 */
public class Controller {
    private Dialog dialog;
    private Register register;
    private Forget forget;
    private MainWindow mainWindow;
    public static Userdata userdata;
    private Homepage homepage;
    public static DatabaseModel database;
    private AlterPerson alterPerson;
    public static FriendPage friendPage;
    public static SearchFriend searchFriend;
    private HeadProtrait headProtrait;
    private String friendName;
    private String friendHead;
    public static Alert alert;

    public Controller() throws IOException {
        dialog = new Dialog();
        register = new Register();
        userdata = new Userdata();
        database = new DatabaseModel();
        forget = new Forget();
        mainWindow = new MainWindow();
        homepage = new Homepage();
        alterPerson = new AlterPerson();
        alert = new Alert();
        friendPage = new FriendPage();
        searchFriend = new SearchFriend();
        headProtrait = new HeadProtrait();
        MsgData.msg = new Vector<>();
        MsgData.MsgMap = new HashMap<>();
        MsgData.accountList = new Vector<>();
        database.connect();
        dialog.show();
    }

    /**
     * 该方法实现各个页面的各种交互 例如点击当前页面的按钮 跳转至另一个页面 所有功能集合
     *
     * @throws ClassNotFoundException
     */
    public void exec() throws ClassNotFoundException {
        headProtrait.setModailty(register);
        headProtrait.setModailty(alterPerson);
        alert.setModailty(mainWindow);
        alert.setModailty(searchFriend);
        ChatManager.getInstance().setMainWindow(mainWindow);
        initEvent();
        dialogExec();
        forgetExec();
        alterPersonExec();
        registerExec();
        sendMsgExec();
        OptionHead();
        SearchFriends();
        find();
        FriendInfo();
        saveRemark();
        dialog.show();
    }

    /**
     * 初始化事件
     */
    public void initEvent() {
        ((Button) $(dialog, "register")).setOnAction(event -> {
            dialog.hide();
            dialog.clear();
            register.show();
        });
        ((Button) $(register, "back")).setOnAction(event -> {
            register.hide();
            register.clear();
            dialog.show();
        });
        ((Button) $(dialog, "getBack")).setOnAction(event -> {
            dialog.hide();
            dialog.clear("Password");
            forget.show();

        });
        ((Button) $(forget, "cancel")).setOnAction(event -> {
            forget.hide();
            forget.clear();
            dialog.show();
        });
        ((Button) $(mainWindow, "more")).setOnAction(event -> {
            homepage.show();
        });
        ((Button) $(homepage, "alter")).setOnAction(event -> {
            alterPerson.setUserData(userdata.getUserdata());
            alterPerson.show();
        });
        ((Button) $(register, "ChooseHead")).setOnAction(event -> {
            headProtrait.show();
        });
        ((Button) $(mainWindow, "maximization")).setOnAction(event -> {
            searchFriend.clear();
            searchFriend.show();
        });
    }

    /**
     * 该方法通过页面对象 以及给定的id 选择页面的元素  用法:TextField t = (TextField)$(dialog,"UserName");
     * 这样选出登入框对象的id为UserName的输入框 之后就可以为 t 绑定事件了
     *
     * @param window
     * @param id
     * @return
     */
    private Object $(window window, String id) {
        return (Object) window.getRoot().lookup("#" + id);
    }

    /**
     * 邓鹏飞
     * 登入功能
     */
    private void dialogExec() {
        ((Button) $(dialog, "enter")).setOnAction(event -> {
            dialog.resetErrorTip();
            String UserName = ((TextField) $(dialog, "UserName")).getText();
            String Password = ((PasswordField) $(dialog, "Password")).getText();
            if (UserName.equals("") || Password.equals("")) {
                if (UserName.equals("")) {
                    dialog.setErrorTip("accountError", "！未输入账号");
                }
                if (Password.equals("")) {
                    dialog.setErrorTip("passwordError", "！未输入密码");
                }
            } else {
                ResultSet resultSet = null;
                try {
                    resultSet = database.execResult("SELECT * FROM user WHERE account=?", UserName);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    if (resultSet.next()) {
                        if (resultSet.getString(3).equals(Password)) {
                            ResultSet set = database.execResult("SELECT * FROM dialog WHERE account = ?", UserName);
                            if (set.next()) {
                                dialog.setErrorTip("accountError", "该账号已经登入，不能重复登入!");
                            } else {
                                database.exec("INSERT INTO dialog VALUES(?)", UserName);//登入记录
                                //设置用户数据
                                userdata.setUserdata(resultSet);
                                userdata.setData(resultSet);
                                //个人主页数据
                                homepage.setUserData(userdata.getUserdata());
                                dialog.close();
                                //主窗口
                                mainWindow.setHead(userdata.getHead());
                                mainWindow.setPersonalInfo(userdata.getAccount(),userdata.getName(),userdata.getAddress(),userdata.getPhone());

                                ResultSet resultSet1 = database.execResult("SELECT head,account,remark FROM user,companion WHERE account = Y_account AND I_account=?", UserName);

                                //聊天助手
                                mainWindow.addFriend("system", "WeChat聊天助手","聊天助手");
                                ((Label) $(mainWindow, "Y_account")).setText("WeChat聊天助手");
                                MsgData.msg.add(new Vector<>());
                                MsgData.accountList.add("WeChat聊天助手");
                                MsgData.msgTip.put("WeChat聊天助手", 0);
                                //所有好友
                                while (resultSet1.next()) {
                                    MsgData.msg.add(new Vector<>());
                                    String temp = resultSet1.getString("account");
                                    MsgData.accountList.add(temp);
                                    MsgData.msgTip.put(temp, 0);
                                    mainWindow.addFriend(resultSet1.getString("head"), resultSet1.getString("account"), resultSet1.getString("remark"),database, friendPage);
                                }

                                mainWindow.addLeft("system", "欢迎使用WeChat,赶快找好友聊天吧!");
                                MsgData.msg.get(0).add("WeChat聊天助手 欢迎使用WeChat,赶快找好友聊天吧!");
                                //输入框禁用
                                ((TextField) $(mainWindow, "input")).setDisable(true);
                                ((Button) $(mainWindow, "send")).setDisable(true);
                                //开始选择聊天助手
                                mainWindow.getFriendList().getSelectionModel().select(0);
                                //获取已登入的好友
                                ResultSet resultSet2 = database.execResult("SELECT Y_account FROM companion WHERE I_account=? AND Y_account in (SELECT account FROM dialog)", UserName);
                                while (resultSet2.next()) {
                                  int i = MsgData.accountList.indexOf(resultSet2.getString("Y_account"));
                                        if (i!=-1) {
                                            mainWindow.getFriendVector().get(i).setOnline();//已登入就设置为登入状态
                                        }
                                    }
                                    mainWindow.getFriendVector().get(0).setOnline();//否则未登入状态
                                ChatManager.getInstance().connect("123.206.49.113", UserName);//链接服务器
                                //设置背景
                                //setHeadPortrait(((Button)$(mainWindow,"background")),"background",resultSet.getString("background"));
                                mainWindow.show();
                            }
                        } else {
                            dialog.setErrorTip("passwordError", "！您输入的密码有误");
                        }
                    } else {
                        dialog.setErrorTip("accountError", "！账号未注册");
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 发消息功能
     * 邓鹏飞
     */
    private void sendMsgExec() {
        ((Button) $(mainWindow, "send")).setOnAction(event -> {

            String youAccount = MsgData.accountList.get(mainWindow.getFriendList().getSelectionModel().getSelectedIndex());

            try {
                //选择登入的好友
                ResultSet resultSet = database.execResult("SELECT * FROM dialog WHERE account=?", youAccount);
                if (resultSet.next()) {
                    String input = ((TextField) $(mainWindow, "input")).getText();
                    if (!input.equals("")) {
                        String line = userdata.getAccount() + " " + youAccount + " " + input;
                        mainWindow.addRight(userdata.getHead(), input);//添加自己的消息
                        try {
                            ChatManager.getInstance().send(line);//向服务器发消息
                            int i = MsgData.accountList.indexOf(youAccount);//添加到消息集
                            if (i != -1) {
                                MsgData.msg.get(i).add(userdata.getAccount() + " " + input);
                            }
                            ((TextField) $(mainWindow, "input")).clear();//清输入框
                        } catch (IOException e) {
                            alert.setInformation("你断开了链接!");
                            alert.exec();
                            e.printStackTrace();
                        }

                    } else {
                        return;
                    }
                }
                else
                {
                    alert.setInformation("对方暂时不在线，你发的消息对方无法接收!");
                    alert.exec();
                }
                } catch(SQLException e){
                    e.printStackTrace();
                }


        });
        ((TextField)$(mainWindow,"input")).setOnKeyPressed(event ->
        {
            if(event.getCode() == KeyCode.ENTER){
                String youAccount = MsgData.accountList.get(mainWindow.getFriendList().getSelectionModel().getSelectedIndex());

                try {
                    //选择登入的好友
                    ResultSet resultSet = database.execResult("SELECT * FROM dialog WHERE account=?", youAccount);
                    if (resultSet.next()) {
                        String input = ((TextField) $(mainWindow, "input")).getText();
                        if (!input.equals("")) {
                            String line = userdata.getAccount() + " " + youAccount + " " + input;
                            mainWindow.addRight(userdata.getHead(), input);//添加自己的消息
                            try {
                                ChatManager.getInstance().send(line);//向服务器发消息
                                int i = MsgData.accountList.indexOf(youAccount);//添加到消息集
                                if (i != -1) {
                                    MsgData.msg.get(i).add(userdata.getAccount() + " " + input);
                                }
                                ((TextField) $(mainWindow, "input")).clear();//清输入框
                            } catch (IOException e) {
                                alert.setInformation("你断开了链接!");
                                alert.exec();
                                e.printStackTrace();
                            }

                        } else {
                            return;
                        }
                    }
                    else
                    {
                        alert.setInformation("对方暂时不在线，你发的消息对方无法接收!");
                        alert.exec();
                    }
                } catch(SQLException e){
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     *
     * 忘记密码功能
     */
    private void forgetExec() {
        ///点击重置按钮
        ((Button) $(forget, "reset")).setOnAction(event -> {
            forget.resetErrorTip();
            String account = ((TextField) $(forget, "account")).getText();
            String name = ((TextField) $(forget, "name")).getText();
            String phone = ((TextField) $(forget, "phone")).getText();
            String password = ((PasswordField) $(forget, "password")).getText();
            String rePassword = ((PasswordField) $(forget, "rePassword")).getText();
            String accountRegExp = "^[0-9,a-z,A-Z,\\u4e00-\\u9fa5]{1,15}$";
            String nameRegExp = "^[a-z,A-Z,\\u4e00-\\u9fa5]{1,100}$";
            String phoneRegExp = "^(((13[0-9])|(15[0-3][5-9])|(18[0,5-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$";
            String passwordReExp = "^[a-z,A-Z,0-9]{6,20}$";
            String rePasswordRegExp = "^[a-z,A-Z,0-9]{6,20}$";

            if (account.equals("") || name.equals("") || phone.equals("") || password.equals("") || rePassword.equals("")) {
                if (account.equals("")) {
                    forget.setErrorTip("accountError", "！未输入账号");
                }
                if (name.equals("")) {
                    forget.setErrorTip("nameError", "！未输入姓名");
                }
                if (phone.equals("")) {
                    forget.setErrorTip("phoneError", "！未输入电话号");
                }
                if (password.equals("")) {
                    forget.setErrorTip("passwordError", "！未输入密码");
                }
                if (rePassword.equals("")) {
                    forget.setErrorTip("rePasswordError", "！未输入密码");

                }

            } else if (!Pattern.matches(accountRegExp, account) || !Pattern.matches(nameRegExp, name) || !Pattern.matches(phoneRegExp, phone) || !Pattern.matches(passwordReExp, password) || !Pattern.matches(rePasswordRegExp, rePassword)) {
                if (!Pattern.matches(accountRegExp, account)) {
                    forget.setErrorTip("accountError", "！错误,账号是长度不超过15位的中文和英文和数字");
                }
                if (!Pattern.matches(nameRegExp, name)) {
                    forget.setErrorTip("nameError", "！姓名格式错误");
                }
                if (!Pattern.matches(phoneRegExp, phone)) {
                    forget.setErrorTip("phoneError", "！电话号格式错误");
                }
                if (!Pattern.matches(passwordReExp, password)) {
                    forget.setErrorTip("passwordError", "！错误,密码是长度在6-20位的英文字母和数字");
                }
                if (!Pattern.matches(rePasswordRegExp, rePassword)) {
                    forget.setErrorTip("rePasswordError", "！错误,密码是长度在6-20位的英文字母和数字");
                }
            } else {
                try {
                    ResultSet resultSet = database.execResult("SELECT * FROM user WHERE account = ?", account);
                    if (resultSet.next()) {
                        if (name.equals(resultSet.getString(2))) {
                            if (phone.equals(resultSet.getString(9))) {
                                if (password.equals(resultSet.getString(3))) {
                                    forget.setErrorTip("passwordError", "！新密码不能和旧密码一样");
                                } else {
                                    if (password.equals(rePassword)) {
                                        database.exec("UPDATE user SET password = ? WHERE account=?", password, account);
                                        forget.close();
                                        forget.clear();
                                        dialog.show();
                                    } else {
                                        forget.setErrorTip("rePasswordError", "！两次密码输入不一致");
                                    }
                                }
                            } else {
                                forget.setErrorTip("phoneError", "！电话号输入错误");
                            }
                        } else {
                            forget.setErrorTip("nameError", "！姓名输入错误");
                        }
                    } else {
                        forget.setErrorTip("accountError", "！该账号不存在");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    /**
     *
     *
     * 注册功能
     */
    public void registerExec() {
        ((Button) $(register, "register")).setOnAction(event -> {
            register.resetErrorTip();
            String account = ((TextField) $(register, "account")).getText();
            String name = ((TextField) $(register, "name")).getText();
            String password = ((PasswordField) $(register, "password")).getText();
            String rePassword = ((PasswordField) $(register, "rePassword")).getText();
            String age = ((TextField) $(register, "age")).getText();
            String sex;
            String phone = ((TextField) $(register, "phone")).getText();
            RadioButton man = ((RadioButton) $(register, "man"));
            String accountRegExp = "^[0-9,a-z,A-Z,\\u4e00-\\u9fa5]{1,15}$";
            String nameRegExp = "^[a-z,A-Z,\\u4e00-\\u9fa5]{1,100}$";
            String phoneRegExp = "^(((13[0-9])|(15[0-3][5-9])|(18[0,5-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$";
            String ageRegExp = "^\\d{1,3}$";
            String passwordReExp = "^[a-z,A-Z,0-9]{6,20}$";
            String rePasswordRegExp = "^[a-z,A-Z,0-9]{6,20}$";
            if (account.equals("") || name.equals("") || password.equals("") || rePassword.equals("") || age.equals("") || phone.equals("")) {
                if (account.equals("")) {

                    register.setErrorTip("accountError", "！未输入账号");

                }
                if (name.equals("")) {
                    register.setErrorTip("nameError", "！未输入姓名");
                }
                if (password.equals("")) {
                    register.setErrorTip("passwordError", "！未输入密码");
                }
                if (rePassword.equals("")) {
                    register.setErrorTip("rePasswordError", "！未输入密码");
                }
                if (age.equals("")) {
                    register.setErrorTip("ageError", "！未输入年龄");
                }
                if (phone.equals("")) {

                    register.setErrorTip("phoneError", "！未输入电话号");

                }
            } else if (!Pattern.matches(accountRegExp, account) || !Pattern.matches(nameRegExp, name) || !Pattern.matches(passwordReExp, password) || !Pattern.matches(rePasswordRegExp, rePassword) || !Pattern.matches(ageRegExp, age) || !Pattern.matches(phoneRegExp, phone)) {

                if (!Pattern.matches(accountRegExp, account)) {
                    register.setErrorTip("accountError", "！错误,账号是长度不超过15位的中文和英文和数字");
                }
                if (!Pattern.matches(nameRegExp, name)) {
                    register.setErrorTip("nameError", "！姓名格式错误");
                }
                if (!Pattern.matches(passwordReExp, password)) {
                    register.setErrorTip("passwordError", "！错误,密码是长度在6-20位的英文字母和数字");
                }
                if (!Pattern.matches(rePasswordRegExp, rePassword)) {
                    register.setErrorTip("rePasswordError", "！错误,密码是长度在6-20位的英文字母和数字");
                }
                if (!Pattern.matches(ageRegExp, age)) {
                    register.setErrorTip("ageError", "！错误年龄只能是数字");
                }
                if (!Pattern.matches(phoneRegExp, phone)) {
                    register.setErrorTip("phoneError", "！电话号格式错误");
                }

            } else {
                try {
                    ResultSet resultSet = database.execResult("SELECT * FROM user WHERE account=?",account);
                if (!resultSet.next()) {
                    if (password.equals(rePassword)) {
                        if (man.isSelected()) {
                            sex = "man";
                        } else {
                            sex = "woman";
                        }
                        try {
                            database.exec("INSERT INTO user VALUES(?,?,?,?,?,?,?,?,?,?)", account, name, password, age, sex, userdata.getHead(), "", "", phone,"background1");
                            register.close();
                            register.clear();
                            dialog.show();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else {
                        register.setErrorTip("rePasswordError", "！两次密码不一致");

                    }

                }
                else
                {
                    register.setErrorTip("accountError", "！错误,该账号已存在");
                }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    /**
     * 邓鹏飞
     * 修改个人信息
     *
     * */
    public void alterPersonExec() {
        ((Button) $(alterPerson, "submit")).setOnAction(event -> {
            alterPerson.resetErrorTip();
            String sex;
            String account = ((Label) $(alterPerson, "account")).getText();
            String label = ((TextArea) $(alterPerson, "label")).getText();
            String name = ((TextField) $(alterPerson, "name")).getText();
            String age = ((TextField) $(alterPerson, "age")).getText();
            String address = ((TextField) $(alterPerson, "address")).getText();
            String phone = ((TextField) $(alterPerson, "phone")).getText();
            String nameRegExp = "^[a-z,A-Z,\\u4e00-\\u9fa5]{1,100}$";
            String phoneRegExp = "^(((13[0-9])|(15[0-3][5-9])|(18[0,5-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$";
            String ageRegExp = "^\\d{1,3}$";
            RadioButton man = ((RadioButton) $(alterPerson, "man"));
            RadioButton woman = ((RadioButton) $(alterPerson, "woman"));
            if (name.equals("") || phone.equals("") || age.equals("")) {
                if (name.equals("")) {
                    alterPerson.setErrorTip("nameError", "！未输入姓名");
                }
                if (phone.equals("")) {
                    alterPerson.setErrorTip("phoneError", "！未输入电话号");
                }
                if (age.equals("")) {
                    alterPerson.setErrorTip("ageError", "！未输入年龄");
                }
            } else if (!Pattern.matches(nameRegExp, name) || !Pattern.matches(phoneRegExp, phone) || !Pattern.matches(ageRegExp, age)) {
                if (!Pattern.matches(nameRegExp, name)) {
                    alterPerson.setErrorTip("nameError", "！姓名格式错误");
                }
                if (!Pattern.matches(phoneRegExp, phone)) {
                    alterPerson.setErrorTip("phoneError", "！电话号格式错误");
                }
                if (!Pattern.matches(ageRegExp, age)) {
                    alterPerson.setErrorTip("ageError", "！年龄输入有误,年龄只能是数字");
                }
            } else {
                if (man.isSelected()) {
                    sex = "man";
                } else {
                    sex = "woman";
                }
                try {
                    userdata.setPhone(phone);
                    userdata.setLabel(label);
                    userdata.setSex(sex);
                    userdata.setAddress(address);
                    userdata.setName(name);
                    userdata.setAge(Integer.parseInt(age));
                    database.exec("UPDATE user SET label=?, name=?, age=?, address=?, phone=?, sex=?,head=? WHERE account=?", label, name, age, address, phone, sex, userdata.getHead(), account);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                homepage.setUserData("label", label);
                homepage.setUserData("name", name);
                homepage.setUserData("age", age);
                homepage.setUserData("address", address);
                homepage.setUserData("phone", phone);
                homepage.setUserData("sex", sex);
                setHeadPortrait(((Button) $(homepage, "head")), userdata.getHead());
                setHeadPortrait(((Button) $(homepage, "background")), "head1",userdata.getHead());
                setHeadPortrait(((Button) $(mainWindow, "individual")), userdata.getHead());
                mainWindow.setPersonalInfo(account,name,address,phone);
                alterPerson.close();
            }

        });
        alterHead();

    }

    /**
     * 选择头像
     * 邓鹏飞
     */
    public void OptionHead() {
        ((Button) $(headProtrait, "submit")).setOnAction((ActionEvent event) -> {
            RadioButton one = ((RadioButton) $(headProtrait, "one"));
            RadioButton two = ((RadioButton) $(headProtrait, "two"));
            RadioButton three = ((RadioButton) $(headProtrait, "three"));
            RadioButton four = ((RadioButton) $(headProtrait, "four"));
            RadioButton five = ((RadioButton) $(headProtrait, "five"));
            RadioButton six = ((RadioButton) $(headProtrait, "six"));
            RadioButton seven = ((RadioButton) $(headProtrait, "seven"));
            RadioButton eight = ((RadioButton) $(headProtrait, "eight"));
            RadioButton nine = ((RadioButton) $(headProtrait, "nine"));
            RadioButton ten = ((RadioButton) $(headProtrait, "ten"));
            if (one.isSelected()) {
                userdata.setHead("head1");
            } else if (two.isSelected()) {
                userdata.setHead("head2");
            } else if (three.isSelected()){
                userdata.setHead("head3");
            } else if (four.isSelected()) {
                userdata.setHead("head4");
            } else if (five.isSelected()){
                userdata.setHead("head5");
            } else if (six.isSelected()) {
                userdata.setHead("head6");
            }
            else if(seven.isSelected()){
                userdata.setHead("head7");
            }
            else if(eight.isSelected()){
                userdata.setHead("head8");
            }
            else if(nine.isSelected()){
                userdata.setHead("head9");
            }
            else if(ten.isSelected()){
                userdata.setHead("head10");
            }
            setHeadPortrait(((Button) $(register, "HeadPortrait")), userdata.getHead());
            setHeadPortrait(((Button) $(alterPerson, "head")), userdata.getHead());
            setHeadPortrait(((Button) $(alterPerson, "background")),"head1", userdata.getHead());
            headProtrait.close();
        });
    }

    /**
     *
     *
     * 添加查找好友
     */
    public void SearchFriends() {
        ((TextField) $(searchFriend, "textInput")).setOnKeyPressed(event ->
        {
            if(event.getCode() == KeyCode.ENTER) {
                searchFriend.clear();
                String UserName = ((TextField) $(searchFriend, "textInput")).getText();
                ((TextField) $(searchFriend,"textInput")).clear();
                if (UserName.equals("")) {
                    alert.setInformation("未输入账号!");
                    alert.exec();
                } else if (UserName.equals(userdata.getAccount())) {
                    alert.setInformation("不能输入自己的账号!");
                    alert.exec();
                } else {
                    ResultSet resultSet = null;
                    try {
                        resultSet = database.execResult("SELECT head,account FROM user WHERE account!=? AND account not in (SELECT Y_account FROM companion WHERE I_account = ?) ", userdata.getAccount(), userdata.getAccount());
                        boolean flag = false;
                        while (resultSet.next()) {
                            if (resultSet.getString("account").indexOf(UserName) != -1) {
                                searchFriend.add(resultSet.getString("head"), resultSet.getString("account"), mainWindow);
                                flag = true;
                            }
                        }
                        if (!flag) {
                            alert.setInformation("没有相关结果!");
                            alert.exec();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            else
            {
                return;
            }
        });
    }

    /**
     * 邓鹏飞
     * 修改头像
     */
    public void alterHead() {
        ((Button) $(alterPerson, "replace")).setOnAction(event -> {
            headProtrait.show();
        });
    }
    public static void setHeadPortrait(Button button, String head) {
        button.setStyle(String.format("-fx-background-image: url('/View/Fxml/CSS/Image/head/%s.jpg')", head));
    }
    public static void setHeadPortrait(Button button, String file,String bg) {
        button.setStyle(String.format("-fx-background-image: url('/View/Fxml/CSS/Image/%s/%s.jpg')",file, bg));
    }
    /**
     * 查找自己的好友
     */
    public void find() {
        ((TextField) $(mainWindow, "search")).setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String text = ((TextField) $(mainWindow, "search")).getText();
                int i = MsgData.accountList.indexOf(text);
                    if (i!=-1) {
                        ((ListView) $(mainWindow, "FirendList")).getSelectionModel().select(i);
                    }
                    else
                    {
                        alert.setInformation("!未查找到该好友");
                        alert.exec();
                        return;
                    }
                }

        });
    }

    public static void flush(MainWindow mainWindow) throws SQLException {
        return ;
    }
    public void FriendInfo(){

        ((Button) $(mainWindow,"moref")).setOnAction(event -> {
            int index = mainWindow.getFriendList().getSelectionModel().getSelectedIndex();
            String account = MsgData.accountList.get(index);
            if(account.equals("WeChat聊天助手"))
            {
                return;
            }
            else {


                if (friendPage.isShowing()) {
                    friendPage.close();
                }
                try {
                    ResultSet resultSet = database.execResult("SELECT * FROM user WHERE account=?", account);
                    resultSet.next();
                    friendPage.setFriendData(resultSet,((Label)$(mainWindow,"Y_account")).getText());
                    friendPage.show();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        });


    }
    public void saveRemark(){
        ((Button) $(friendPage,"submit")).setOnAction(event -> {
            String remark = ((TextField) $(friendPage,"remark")).getText();
            if(remark.equals("")){
                return ;
            }
            int index = MsgData.accountList.indexOf(((Label) $(friendPage,"account")).getText());
            if(index==-1)
            {
                return ;
            }
            int index1 = mainWindow.getFriendList().getSelectionModel().getSelectedIndex();
            if(index == index1) {
                mainWindow.getFriendVector().get(index).setText(remark);
                ((Label) $(mainWindow,"Y_account")).setText(remark);
            }
            else
            {
                mainWindow.getFriendVector().get(index).setText(remark);
            }
            try {
                database.exec("UPDATE companion SET remark=? WHERE I_account=? AND Y_account =?",remark,userdata.getAccount(),((Label) $(friendPage,"account")).getText());
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });


    }

}

