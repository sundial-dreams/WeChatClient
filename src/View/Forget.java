package View;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;

import java.io.IOException;

/**邓鹏飞
 *
 * 忘记密码
 */
public class Forget extends window {

    public Forget() throws IOException {
        root = FXMLLoader.load(getClass().getResource("Fxml/Forget.fxml"));
        Scene scene = new Scene(root, 700, 450);
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
    public void setErrorTip(String id,String Text){
        ((Label) $(id)).setText(Text);
    }
    public void resetErrorTip(){
         ((Label) $("accountError")).setText("");
         ((Label) $("nameError")).setText("");
         ((Label) $("phoneError")).setText("");
         ((Label) $("passwordError")).setText("");
         ((Label) $("rePasswordError")).setText("");
    }
    public void clear(){
        ((TextField) $("account")).clear();
        ((TextField) $("name")).clear();
        ((TextField) $("phone")).clear();
        ((PasswordField) $("password")).clear();
        ((PasswordField) $("rePassword")).clear();
    }
}
