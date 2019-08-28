package Engines;

import SceneCore.AllertBox;
import SceneCore.ScenesController.HomeController;
import SceneCore.ScenesController.MindEditorController;
import SceneCore.ScenesController.PropertiesController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ViewEngine {

    Engine engine;

    //JavaFX stuff
    private Stage primaryStage;
    private HomeController homeController;
    private PropertiesController propertiesController;
    private MindEditorController mindEditorController;

    boolean viewLoaded = false;

    public ViewEngine(Engine engine, Stage primaryStage) {
        this.engine = engine;
        this.primaryStage = primaryStage;
    }

    public void boot(){
        setupView();
    }

    //Implements everything what we need to see a window and start the window!
    private void setupView(){
        loadScenes();
        openHome();
        viewLoaded = true;
    }

    private void loadScenes(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/home.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (Exception ex) {
            ex.printStackTrace();
            engine.closeProgram(engine.getProperties().getPlannedProgrammShutdownOnError(), false);
        }
        homeController = loader.getController();
        Scene scene = new Scene(root);

        homeController.initController(engine, primaryStage, scene);

        loader = new FXMLLoader(getClass().getResource("/Scenes/properties.fxml"));
        try {
            root = loader.load();
        } catch (Exception ex) {
            ex.printStackTrace();
            engine.closeProgram(engine.getProperties().getPlannedProgrammShutdownOnError(), false);
        }
        propertiesController = loader.getController();
        scene = new Scene(root);
        propertiesController.initController(engine,new Stage(),scene);

        loader = new FXMLLoader(getClass().getResource("/Scenes/mindEditor.fxml"));
        try {
            root = loader.load();
        } catch (Exception ex) {
            ex.printStackTrace();
            engine.closeProgram(engine.getProperties().getPlannedProgrammShutdownOnError(), false);
        }
        mindEditorController = loader.getController();
        scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        mindEditorController.initController(engine,new Stage(),scene);
    }

    public void printConsollogOnGui(String text){
        homeController.addCommandLine(text, false);
    }

    public void clearConsollogOnGui(){
        homeController.clearCommandLine();
    }

    public void displayMessageAllertBox(String title, String message){
        if(viewLoaded){
            new AllertBox(null, Modality.APPLICATION_MODAL, engine).displayMessage(title, message, "ok", "buttonBlue", false);
        }
    }

    public void showProperties(){
        closeHome();
        propertiesController.updatePropertiesWindow();
        propertiesController.getPrimaryStage().show();
    }

    public void showMindEditor(){
        //primaryStage.hide();
        mindEditorController.updateContent(engine.getBotEngine().getAiEngine().getAiCommands());
        mindEditorController.getPrimaryStage().show();
    }

    public void closeMindEditor(){
        mindEditorController.getPrimaryStage().close();
        openHome();
    }

    public void closeProperties(){
        propertiesController.getPrimaryStage().hide();
        openHome();
    }

    public void closeHome(){
        homeController.getPrimaryStage().hide();
    }

    public void openHome(){
        homeController.getPrimaryStage().show();
        homeController.getPrimaryStage().toFront();
    }

    public boolean isViewLoaded() {
        return viewLoaded;
    }

}
