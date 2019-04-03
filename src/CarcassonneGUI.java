/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.File;
import java.io.FileNotFoundException;
import javafx.scene.image.Image ;
import static java.lang.Double.SIZE;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;
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
    
    static final String PROVES_SRC = "src/proves/";

    private Stack<Peça> _peces;
    private ArrayList<Jugador> _jugadors;
    private Tauler _tauler;
    private CarcassonneGUI _gui;


    public void init(String arxiu){
        File f = new File(PROVES_SRC+arxiu+".txt");
        HashMap<String,Integer> peces = new HashMap<>();
        boolean error = false;
        try {
            Scanner lector = new Scanner(f);

            //Llegim els jugadors
            if(lector.hasNext() && lector.next().equals("nombre_jugadors")){
                int njugs = lector.nextInt();
                for(int i=0;i<njugs;i++) _jugadors.add(new Jugador(i));
                if(lector.hasNext() && lector.next().equals("jugadors_cpu")){
                    while(lector.hasNextInt()){
                        int j_cpu = lector.nextInt();
                        _jugadors.get(j_cpu-1).setCpu();
                    }
                } else error = true;
            } else error = true;


            //Llegim les peces
            if(lector.hasNext() && lector.next().equals("rajoles")){
                String codi;
                while(!(codi = lector.next()).equals("#")) {
                    Integer nombre = lector.nextInt();
                    peces.put(codi, nombre);
                }
            } else error = true;

            //Llegim la rajola inicial
            if(lector.hasNext() && lector.next().equals("rajola_inicial")){
                String inicial = lector.next();
                peces.put(inicial,peces.get(inicial)-1);
                _tauler.afegirPeça(new Peça(inicial),0,0);
            } else error = true;

        } catch (FileNotFoundException e){
            System.err.println(e);
        }

        //Afegim les peces al stack
        if(!error){
            Iterator it = peces.entrySet().iterator();
            int nReg = 0;
            while(it.hasNext()){
                Map.Entry pair = (Map.Entry)it.next();
                for(int i=0;i<(Integer)pair.getValue();i++){
                    Peça p = new Peça((String)pair.getKey());
                    nReg += p.afegirRegions(nReg);
                    _peces.push(p);
                }
            }
            Collections.shuffle(_peces);
        } else System.out.println("Error");
    }
    
    public Peça obtenirTop(){
        return _peces.peek();
    }
    
    @Override
    public void start(Stage primaryStage) {
        _peces = new Stack<>();
        _jugadors = new ArrayList<>();
        _tauler = new Tauler(false);
        init("1");
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
        launch(args);
    }
    
}
