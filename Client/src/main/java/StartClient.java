
import controllers.ControllerPrincipal;
import controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import services.IServices;

import java.io.IOException;
import java.util.Properties;
public class StartClient extends Application {

    private static int defaultChatPort = 55555;
    private static String defaultServer = "localhost";
    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("In start");

        try {

            ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:spring-client.xml");
            IServices server=(IServices)factory.getBean("service");
            System.out.println("Obtained a reference to remote chat server");

  //LOGIN
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginView.fxml"));
            Parent root=loader.load();
            //
            LoginController ctrl =loader.getController();
            ctrl.setContext(server);


//MAIN
            FXMLLoader cloader = new FXMLLoader(
                    getClass().getResource("/principalView.fxml"));
            Parent croot=cloader.load();

            ControllerPrincipal concursCtrl = cloader.getController();
            concursCtrl.setContext(server);

            ctrl.setControllerPrincipal(concursCtrl);
            ctrl.setParent(croot);

            primaryStage.setTitle("Concurs");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();

        } catch (Exception e) {
            System.err.println("Chat Initialization  exception:"+e);
            e.printStackTrace();
        }


    }

}