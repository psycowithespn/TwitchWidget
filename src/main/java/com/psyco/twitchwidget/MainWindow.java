package com.psyco.twitchwidget;

import com.psyco.twitchwidget.authentication.AuthState;
import com.psyco.twitchwidget.authentication.Authentication;
import com.psyco.twitchwidget.twitchapi.TwitchAPIAccessor;
import javafx.animation.ScaleTransition;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainWindow extends Application {

    private static final int MIN_TILE_SIZE = 20;
    private static final int MAX_TILE_SIZE = 50;
    private static final int TILE_SIZE_DIFF = MAX_TILE_SIZE - MIN_TILE_SIZE;
    private final Image image = new Image("https://static-cdn.jtvnw.net/jtv_user_pictures/tenshotstv-profile_image-e921186e15b2225a-300x300.png");

    private Authentication authStage;
    private TwitchAPIAccessor accessor;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        //primaryStage.initStyle(StageStyle.TRANSPARENT);
        accessor = new TwitchAPIAccessor();
        GridPane root = new GridPane();
        root.setHgap(20);
        root.setVgap(20);
        Button exit = new Button("Exit");
        exit.setOnAction(e -> primaryStage.close());
        root.add(exit, 0, 0);

        authStage = new Authentication();
        Button auth = new Button("Authenticate");
        auth.setOnAction(e -> authStage.authenticate(false, this::authSuccess, this::authFailure));
        root.add(auth, 0, 1);

        Button forceAuth = new Button("Force Authenticate");
        forceAuth.setOnAction(e -> authStage.authenticate(true, this::authSuccess, this::authFailure));
        root.add(forceAuth, 0, 2);

        Button isValid = new Button("Is Valid");
        isValid.setOnAction(e -> accessor.isOAuthValid(this::oauthValid, this::oauthInvalid));
        root.add(isValid, 0, 3);

        ImageView imageView = new ImageView(image);
        imageView.setEffect(new ColorAdjust(0, -1, 0, 0));

        BorderPane imagePane = new BorderPane(imageView);
        imagePane.setStyle("-fx-border-color: green;\n" +
                "-fx-border-insets: 5;\n" +
                "-fx-border-width:3;\n" +
                "-fx-border-style: dashed;\n");

        root.add(imagePane, 0, 4);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        accessor.dispose();
    }

    private void oauthValid() {
        System.out.println("OAuth is valid");
    }

    private void oauthInvalid() {
        System.out.println("OAuth is not valid");
    }

    private void authFailure(AuthState state) {
        System.out.println("Failed to auth: " + state);
        accessor.setOauthToken(null);
    }

    private void authSuccess(AuthState state) {
        System.out.println("Auth Success: " + state + ": " + authStage.getAuthToken());
        accessor.setOauthToken(authStage.getAuthToken());
    }
}
