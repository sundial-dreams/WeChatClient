package View;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Map;

/**
 * 邓鹏飞
 *
 * 修改个人信息窗口
 */
public class AlterPerson extends window {
    private ToggleGroup group;
    public AlterPerson() throws IOException {
       root = FXMLLoader.load(getClass().getResource("Fxml/AlterPerson.fxml"));
       Scene scene = new Scene(root, 450, 800);
       scene.setFill(Color.TRANSPARENT);
       setScene(scene);
       initStyle(StageStyle.TRANSPARENT);
       setResizable(false);
       setTitle("We Chat");
        group = new ToggleGroup();
        RadioButton radioButton = ((RadioButton) $("man"));
        radioButton.setToggleGroup(group);
        ((RadioButton) $("woman")).setToggleGroup(group);
        radioButton.setSelected(true);
        radioButton.requestFocus();
       move();
       quit();
        setIcon();
       cancel();
       minimiser();
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
    public void cancel(){
        ((Button) $("cancel")).setOnAction(event -> {
            close();
        });
    }
    public void setErrorTip(String id,String text){
        ((Label) $(id)).setText(text);
    }
    public void resetErrorTip(){
        ((Label) $("nameError")).setText("");
        ((Label) $("ageError")).setText("");
        ((Label) $("phoneError")).setText("");
    }
    public void setUserData(String id,String text){
        if(id.equals("account")){
            ((Label) $(id)).setText(text);
        }
        else if(id.equals("label")){
            ((TextArea) $(id)).setText(text);
        }
       else
        {
            ((TextField) $(id)).setText(text);
        }

    }
    public void setUserData(Map<String,String> usermap){
        ((Label) $("account")).setText(usermap.get("account"));
        ((TextField) $("name")).setText(usermap.get("name"));
        ((TextField) $("address")).setText(usermap.get("address"));
        ((TextField) $("age")).setText(usermap.get("age"));
        ((TextField) $("phone")).setText(usermap.get("phone"));
        ((TextArea) $("label")).setText(usermap.get("label"));
        if(usermap.get("sex").equals("man")){
            ((RadioButton) $("man")).setSelected(true);
            ((RadioButton) $("man")).requestFocus();
        }
        else
        {
            ((RadioButton) $("woman")).setSelected(true);
            ((RadioButton) $("woman")).requestFocus();
        }
         setHeadPortrait(((Button) $("head")),usermap.get("head"));
        setHeadPortrait(((Button) $("background")),usermap.get("head"),"head1");
    }
    public static void setHeadPortrait(Button button,String head){

        button.setStyle(String.format("-fx-background-image: url('/View/Fxml/CSS/Image/head/%s.jpg')",head));

    }
    public static void setHeadPortrait(Button button,String head,String file){

        button.setStyle(String.format("-fx-background-image: url('/View/Fxml/CSS/Image/%s/%s.jpg')",file,head));

    }
}
