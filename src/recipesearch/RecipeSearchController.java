
package recipesearch;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import se.chalmers.ait.dat215.lab2.Recipe;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;


public class RecipeSearchController implements Initializable{


    @FXML public AnchorPane recipeDetailPane;
    @FXML public SplitPane searchPane;

    @FXML private ComboBox cuisineCBox;
    @FXML private ComboBox mainIngCBox;
    RecipeDatabase db = RecipeDatabase.getSharedInstance();
    @FXML  private RadioButton everythingRButton;
    @FXML  private RadioButton easyRButton;
    @FXML  private RadioButton lagomRButton;
    @FXML  private RadioButton hardRButton;
    @FXML private Spinner maxPriceSpinner;
    @FXML private Slider maxTimeSlider;
    @FXML private Label timeSelectedLabel;
    @FXML private FlowPane items;
    @FXML private ImageView itemImageIV;
    @FXML private Label itemTitleLabel;
    RecipeBackendController backendC = new RecipeBackendController();

    private Map<String, RecipeListItem> recipeListItemMap = new HashMap<String, RecipeListItem>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        for (Recipe recipe : backendC.getRecipes()) {
            RecipeListItem recipeListItem = new RecipeListItem(recipe, this);
            recipeListItemMap.put(recipe.getName(), recipeListItem);
        }


        mainIngCBox.getItems().addAll("Visa alla", "Kött", "Fisk", "Kyckling", "Vegetarisk");
        mainIngCBox.getSelectionModel().select("Visa alla");
        mainIngCBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                backendC.setMainIngredient(newValue);
                updateRecipeList();
            }
        });

        cuisineCBox.getItems().addAll("Visa alla", "Sverige", "Grekland", "Indien", "Asien","Afrika","Frankrike");
        cuisineCBox.getSelectionModel().select("Visa alla");
        cuisineCBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                backendC.setCuisine(newValue);
                updateRecipeList();
            }
        });

        ToggleGroup difficultyToggleGroup = new ToggleGroup();
        everythingRButton.setToggleGroup(difficultyToggleGroup);
        easyRButton.setToggleGroup(difficultyToggleGroup);
        lagomRButton.setToggleGroup(difficultyToggleGroup);
        hardRButton.setToggleGroup(difficultyToggleGroup);
        everythingRButton.setSelected(true);
        difficultyToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {

                if (difficultyToggleGroup.getSelectedToggle() != null) {
                    RadioButton selected = (RadioButton) difficultyToggleGroup.getSelectedToggle();
                    backendC.setDifficulty(selected.getText());
                    updateRecipeList();
                }
            }
        });

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(10,150,80,10);
        maxPriceSpinner.setValueFactory(valueFactory);
        maxPriceSpinner.valueProperty().addListener(new ChangeListener() {
                                                        @Override
                                                        public void changed(ObservableValue observableValue, Object o, Object t1) {
                                                            Integer value = Integer.valueOf(maxPriceSpinner.getEditor().getText());
                                                            backendC.setMaxPrice(value);
                                                            updateRecipeList();
                                                        }
                                                    });
        maxPriceSpinner.focusedProperty().addListener(new ChangeListener<Boolean>() {

                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                        if (newValue) {
                            //focusgained - do nothing
                        } else {
                            Integer value = Integer.valueOf(maxPriceSpinner.getEditor().getText());
                            backendC.setMaxPrice(value);
                            updateRecipeList();
                        }

                    }
                });

        maxTimeSlider.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                                                      public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                                                          timeSelectedLabel.setText( (int) Math.round(maxTimeSlider.getValue()/10)*10+ " Minuter");
                                                          if(newValue != null && !newValue.equals(oldValue) && !maxTimeSlider.isValueChanging()) {

                                                              updateRecipeList();
                                                              backendC.setMaxTime((int) maxTimeSlider.getValue());
                                                          }


                                                      }
                                                  });

                updateRecipeList();
    }
    private void updateRecipeList() {
        items.getChildren().clear();

        List<Recipe> recipeList = backendC.getRecipes();

        for (Recipe recipeItem : recipeList ){

            items.getChildren().add(recipeListItemMap.get(recipeItem.getName()));
        }


    }
    private void populateRecipeDetailView(Recipe recipe){

        itemImageIV.setImage(recipe.getFXImage());
        itemTitleLabel.setText(recipe.getName());

    }
    @FXML
    public void closeRecipeView(){
        searchPane.toFront();
    }
    public void openRecipeView(Recipe recipe){
        populateRecipeDetailView(recipe);
        recipeDetailPane.toFront();
    }

}