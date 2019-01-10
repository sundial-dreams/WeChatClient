package View;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * 邓鹏飞
 * 登入界面
 */
public class Dialog extends window {
    public Dialog() throws IOException {
        root = FXMLLoader.load(getClass().getResource("Fxml/Dialog.fxml"));
        Scene scene = new Scene(root, 450, 480);
        scene.setFill(Color.TRANSPARENT);
        setScene(scene);
        initStyle(StageStyle.TRANSPARENT);
        setResizable(false);
        setTitle("We Chat");
        move();
        quit();
        setIcon();
        minimiser();
    }
    /**
     * 退出
     */
    @Override
    public void quit() {
        ((Button) $("quit1")).setTooltip(new Tooltip("退出"));
        ((Button) $("quit1")).setOnAction(event -> {
            close();
            System.exit(0);
        });
    }
    @Override
    public void minimiser() {
        ((Button) $("minimiser1")).setTooltip(new Tooltip("最小化"));
        ((Button) $("minimiser1")).setOnAction(event -> {
            setIconified(true);
        });
    }

    /**
     * 设置错误提示
     * @param id
     * @param Text
     */
    public void setErrorTip(String id,String Text){
        ((Label) $(id)).setText(Text);
    }

    /**
     * 重置错误提醒
     */
    public void resetErrorTip(){
       setErrorTip("accountError", "");
       setErrorTip("passwordError", "");
    }
    /*
    清除各种输入框
     */
    public void clear(){

        ((TextField) $("UserName")).clear();
        ((PasswordField) $("Password")).clear();
    }
    public void clear(String id){

        ((TextField) $(id)).clear();

    }

}
