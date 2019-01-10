package View;


import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FriendPage extends window {


   public   FriendPage() throws IOException {
       root = FXMLLoader.load(getClass().getResource("Fxml/FriendPage.fxml"));
       Scene scene = new Scene(root, 385, 648);
       scene.setFill(Color.TRANSPARENT);
       setScene(scene);
       initStyle(StageStyle.TRANSPARENT);
       setResizable(false);
       setTitle("We Chat");
       move();
       quit();
       minimiser();
       setIcon();
    }
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
    public void setFriendData(ResultSet resultSet,String remark) throws SQLException {
        ((Label) $("account")).setText(resultSet.getString("account"));
        ((TextArea) $("label")).setText(resultSet.getString("label"));
        ((TextField) $("name")).setText(resultSet.getString("name"));
        ((TextField) $("address")).setText(resultSet.getString("address"));
        ((TextField) $("sex")).setText(resultSet.getString("sex"));
        ((TextField) $("age")).setText(resultSet.getString("age"));
        ((TextField) $("phone")).setText(resultSet.getString("phone"));
        ((TextField) $("remark")).setText(remark);
        setHeadPortrait(((Button) $("head")),resultSet.getString("head"),"head1");
    }
    public static void setHeadPortrait(Button button,String head,String file){

        button.setStyle(String.format("-fx-background-image: url('/View/Fxml/CSS/Image/%s/%s.jpg')",file,head));

    }
}
