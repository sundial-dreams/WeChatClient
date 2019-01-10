package View;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

/**
 * 邓鹏飞
 *
 * 个人主页
 */
public class Homepage extends window{
    public Homepage() throws IOException {
        root = FXMLLoader.load(getClass().getResource("Fxml/Homepage.fxml"));
        Scene scene = new Scene(root, 600, 700);
        scene.setFill(Color.TRANSPARENT);
        setScene(scene);
        initStyle(StageStyle.TRANSPARENT);
        setResizable(false);
        setTitle("We Chat");
        ((Button) $("alter")).setTooltip(new Tooltip("修改资料"));
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
        ((Button) $("quit1")).setTooltip(new Tooltip("关闭"));
        ((Button) $("quit1")).setOnAction(event -> {
            close();
        });
    }
    @Override
    public void minimiser() {
        ((Button) $("minimiser1")).setTooltip(new Tooltip("最小化"));
        ((Button) $("minimiser1")).setOnAction(event -> {
            setIconified(true);
        });
    }
    public void setUserData(String id,String text){
        if(id.equals("account")){
            ((Label) $(id)).setText(text);
        }
        else if(id.equals("label")) {
            ((TextArea) $(id)).setText(text);
        } else
        {
            ((TextField) $(id)).setText(text);
        }
    }
    public void setUserData(Map<String,String> userdata) throws SQLException {
        ((Label) $("account")).setText(userdata.get("account"));
        ((TextField) $("name")).setText(userdata.get("name"));
        ((TextField) $("address")).setText(userdata.get("address"));
        ((TextField) $("sex")).setText(userdata.get("sex"));
        ((TextField) $("age")).setText(userdata.get("age"));
        ((TextField) $("phone")).setText(userdata.get("phone"));
        ((TextArea) $("label")).setText(userdata.get("label"));
        setHeadPortrait(((Button) $("head")),userdata.get("head"));
        setHeadPortrait(((Button) $("background")),userdata.get("head"),"head1");
    }
    public static void setHeadPortrait(Button button,String head){

        button.setStyle(String.format("-fx-background-image: url('/View/Fxml/CSS/Image/head/%s.jpg')",head));

    }
    public static void setHeadPortrait(Button button,String head,String file){

        button.setStyle(String.format("-fx-background-image: url('/View/Fxml/CSS/Image/%s/%s.jpg')",file,head));

    }
}
