/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import javafx.scene.image.Image ;
import static java.lang.Double.SIZE;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;


/**
 *
 * @author usuario
 */
public class CarcassonneGUI extends Application {
    
    private BorderPane root;
    private Joc j;
    
    @Override
    public void start(Stage primaryStage) {
        root = new BorderPane();
        GridPane grid = getLeftGridPane();
        root.setLeft(grid);
        root.setAlignment(grid,Pos.CENTER);
        BorderPane.setMargin(grid, new Insets(30, 10, 10, 50));
        root.setRight(getRightLabel());
        root.setCenter(getCenterGridPane());
        
        
       
        
        Scene scene = new Scene(root, 1000, 600);
        
        
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public GridPane getLeftGridPane(){
        GridPane grid = new GridPane();
        grid.setHgap(75);
        grid.setVgap(20);
        
        int nJug = 4;
        
        //EFECTES DE TEXT
        DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0f);
        ds.setColor(Color.color(0.4f, 0.4f, 0.4f));
        ///
        
        
        int numAux = 200;
        Text category = new Text("JUGADORS");
        category.setEffect(ds);
        category.setCache(true);
        category.setFill(Color.BLUE);
        category.setFont(Font.font(null, FontWeight.BOLD, 20));
        grid.add(category, 0, 0);
        category = new Text("PUNTUACIO");
        category.setEffect(ds);
        category.setCache(true);
        category.setFill(Color.BLUE);
        category.setFont(Font.font(null, FontWeight.BOLD, 20));
        grid.add(category, 1, 0);
        for(int i = 0; i < 2; i++){
            for(int j = 1; j <= nJug; j++){
                if(i == 0){
                    category = new Text("Jugador"+ j);
                    category.setEffect(ds);
                    category.setCache(true);
                    category.setFill(Color.BLUE);
                    category.setFont(Font.font(null, FontWeight.BOLD, 18));
                }
                else{
                    category = new Text("0");
                    category.setEffect(ds);
                    category.setCache(true);
                    category.setFill(Color.BLUE);
                    category.setFont(Font.font(null, FontWeight.BOLD, 18));
                }
                grid.add(category, i, j); 
            }
        }
        grid.setAlignment(Pos.TOP_RIGHT);
        return grid;
    }
    
    public Label getRightLabel(){
        Label res = new Label("Right Label");
        res.setPrefWidth(SIZE*5);
        res.prefHeightProperty().bind(root.heightProperty());
        res.setStyle("-fx-border-style: dotted; -fx-border-width: 0 0 0 1;-fx-font-weight:bold;");
        res.setAlignment(Pos.BASELINE_CENTER);
        return res;
    }
    
    public GridPane getCenterGridPane(){
        GridPane grid = new GridPane();
        grid.setHgap(0);
        grid.setVgap(0);
        grid.setPadding(new Insets(0, 0, 0, 0));
        
        /*for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                Text category = new Text("("+String.valueOf(i)+","+String.valueOf(j)+")");
                category.setFont(Font.font("Arial", FontWeight.BOLD, 90));
                grid.add(category, i, j);
            }
        }*/
        int row = 8;
        int col = 9;
        int numAux = 690;
        for(int i = 0; i < col; i++){
            for(int j = 0; j < row; j++){
                ImageView imageChart;
                if(i!=0 && i!=col-1 && j!=0 && j!=row-1){
                    imageChart = new ImageView(new Image(CarcassonneGUI.class.getResourceAsStream("tiles/CCFCF.png")));
                }
                else{
                    imageChart = new ImageView(new Image(CarcassonneGUI.class.getResourceAsStream("tiles/RES.png")));
                }
                if(row >= col){
                        imageChart.setFitHeight(numAux/row);
                        imageChart.setFitWidth(numAux/row);
                    }
                    else{
                        imageChart.setFitHeight(numAux/col);
                        imageChart.setFitWidth(numAux/col);
                }
                grid.add(imageChart, i, j); 
            }
        }

        
        

        
        grid.setAlignment(Pos.CENTER);

        return grid;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Joc _joc = new Joc("1");
        launch(args);
    }
    
}
