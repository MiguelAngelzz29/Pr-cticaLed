/*
 * Copyright (c) 2013. by Gerrit Grunwald
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package es.cifpcarlos3.fxled;

import java.io.IOException;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class App extends Application {
    private static int noOfNodes = 0;
  //  private boolean encendido;
    private Scene scene;

    @Override public void start(Stage stage) throws IOException {
        
        
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(
                "led.fxml"));
        scene = new Scene(fxmlLoader.load());
        
        
        
        
  /*      var control = new ShapeLed();
        control.setPrefWidth(200);
        control.setPrefHeight(200);

        StackPane pane = new StackPane();
        pane.getChildren().setAll(control);

        Scene scene = new Scene(pane);

        
       
        //Ejercicio 1
        
        
        control.setOnMouseClicked(new EventHandler<MouseEvent>() {
    @Override
    public void handle(MouseEvent event) {
        
        if(encendido){
        control.setBlinking(false);
        encendido = false;
        }else{
        control.setBlinking(true);
        encendido = true;
        }
        
    }
});
       
 // Ejercicio 2
 
   control.setOnMouseEntered(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent t) {
                control.setLedColor(Color.GREEN);
            }
   });
   
   control.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                control.setLedColor(Color.RED);
            }
        });    
   
   // Ejercicio 3
   
   control.setBorderColor(Color.YELLOW);
   
   control.borderColorProperty();
        
        stage.setTitle("JavaFX Led Canvas");*/
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}