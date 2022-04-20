
package recipesearch;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.util.Callback;
import se.chalmers.ait.dat215.lab2.Recipe;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;


public class RecipeSearchController implements Initializable {


    @FXML
    public AnchorPane recipeDetailPane;
    @FXML
    public SplitPane searchPane;

    @FXML
    private ComboBox cuisineCBox;
    @FXML
    private ComboBox mainIngCBox;
    RecipeDatabase db = RecipeDatabase.getSharedInstance();
    @FXML
    private RadioButton everythingRButton;
    @FXML
    private RadioButton easyRButton;
    @FXML
    private RadioButton lagomRButton;
    @FXML
    private RadioButton hardRButton;
    @FXML
    private Spinner maxPriceSpinner;
    @FXML
    private Slider maxTimeSlider;
    @FXML
    private Label timeSelectedLabel;
    @FXML
    private FlowPane items;

    @FXML
    private ImageView itemImageIV;
    @FXML
    private Label detNameItem;

    @FXML
    private ImageView detcuisineImageview;
    @FXML
    private ImageView detMainIngImageView;

    @FXML
    private ImageView detdifficultyImageview;
    @FXML
    private Label detTimeLabel;
    @FXML
    private Label detPriceLabel;
    @FXML
    private TextArea detDiscTextArea;
    @FXML
    private TextArea detInsTextArea;
    @FXML
    private TextArea detIngTextArea;
    @FXML
    private Label detportLabel;

    @FXML
    private ImageView closeImageView;


    RecipeBackendController backendC = new RecipeBackendController();

    private Map<String, RecipeListItem> recipeListItemMap = new HashMap<String, RecipeListItem>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        for (Recipe recipe : backendC.getRecipes()) {

            RecipeListItem recipeListItem = new RecipeListItem(recipe, this);

            recipeListItemMap.put(recipe.getName(), recipeListItem);

        }
        populateCuisineComboBox();
        populateMainIngredientComboBox();
        Platform.runLater(() -> mainIngCBox.requestFocus());

        mainIngCBox.getItems().addAll("Visa alla", "Kött", "Fisk", "Kyckling", "Vegetarisk");
        mainIngCBox.getSelectionModel().select("Visa alla");
        mainIngCBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                backendC.setMainIngredient(newValue);
                updateRecipeList();
            }
        });

        cuisineCBox.getItems().addAll("Visa alla", "Sverige", "Grekland", "Indien", "Asien", "Afrika", "Frankrike");
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

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 150, 80, 10);
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
                timeSelectedLabel.setText((int) Math.round(maxTimeSlider.getValue() / 10) * 10 + " Minuter");
                if (newValue != null && !newValue.equals(oldValue) && !maxTimeSlider.isValueChanging()) {

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

        for (Recipe recipeItem : recipeList) {

            items.getChildren().add(recipeListItemMap.get(recipeItem.getName()));
        }


    }



    private void populateRecipeDetailView(Recipe recipe) {

        itemImageIV.setImage(recipe.getFXImage());
        detNameItem.setText(recipe.getName());

        detcuisineImageview.setImage(getCuisineImage(recipe.getCuisine()));
        detMainIngImageView.setImage((getMainIngImage(recipe.getMainIngredient())));
        detdifficultyImageview.setImage(getDiffImage(recipe.getDifficulty()));
        detTimeLabel.setText(getTimeImage(recipe.getTime()));
        detPriceLabel.setText(recipe.getPrice() + " Kr");
        detDiscTextArea.setText(recipe.getDescription());
        detInsTextArea.setText(recipe.getInstruction());
        detIngTextArea.setText(recipe.getIngredients().toString());
        detportLabel.setText(recipe.getServings() + " Portioner");
    }

    @FXML
    public void closeRecipeView() {
        searchPane.toFront();
    }

    public void openRecipeView(Recipe recipe) {
        populateRecipeDetailView(recipe);
        recipeDetailPane.toFront();
    }

    private void populateMainIngredientComboBox() {
        Callback<ListView<String>, ListCell<String>> cellFactory = new Callback<ListView<String>, ListCell<String>>() {

            @Override
            public ListCell<String> call(ListView<String> p) {

                return new ListCell<String>() {

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        setText(item);

                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            Image icon = null;
                            String iconPath;
                            try {
                                switch (item) {

                                    case "Kött":
                                        iconPath = "RecipeSearch/resources/icon_main_meat.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Fisk":
                                        iconPath = "RecipeSearch/resources/icon_main_fish.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Kyckling":
                                        iconPath = "RecipeSearch/resources/icon_main_chicken.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Vegetarisk":
                                        iconPath = "RecipeSearch/resources/icon_main_veg.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                }
                            } catch (NullPointerException ex) {
                                //This should never happen in this lab but could load a default image in case of a NullPointer
                            }
                            ImageView iconImageView = new ImageView(icon);
                            iconImageView.setFitHeight(12);
                            iconImageView.setPreserveRatio(true);
                            setGraphic(iconImageView);
                        }
                    }
                };
            }
        };
        mainIngCBox.setButtonCell(cellFactory.call(null));
        mainIngCBox.setCellFactory(cellFactory);

    }

    private void populateCuisineComboBox() {
        Callback<ListView<String>, ListCell<String>> cellFactory = new Callback<ListView<String>, ListCell<String>>() {

            @Override
            public ListCell<String> call(ListView<String> p) {

                return new ListCell<String>() {

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        setText(item);

                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            Image icon = null;
                            String iconPath;
                            try {
                                switch (item) {

                                    case "Sverige":
                                        iconPath = "RecipeSearch/resources/icon_flag_sweden.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Grekland":
                                        iconPath = "RecipeSearch/resources/icon_flag_greece.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Asien":
                                        iconPath = "RecipeSearch/resources/icon_flag_asia.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Indien":
                                        iconPath = "RecipeSearch/resources/icon_flag_india.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Afrika":
                                        iconPath = "RecipeSearch/resources/icon_flag_africa.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Frankrike":
                                        iconPath = "RecipeSearch/resources/icon_flag_france.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                }
                            } catch (NullPointerException ex) {
                                //This should never happen in this lab but could load a default image in case of a NullPointer
                            }
                            ImageView iconImageView = new ImageView(icon);
                            iconImageView.setFitHeight(12);
                            iconImageView.setPreserveRatio(true);
                            setGraphic(iconImageView);
                        }
                    }
                };
            }
        };
        cuisineCBox.setButtonCell(cellFactory.call(null));
        cuisineCBox.setCellFactory(cellFactory);

    }

    public Image getCuisineImage(String cuisine) {
        String iconPath;
        switch (cuisine) {
            case "Sverige":
                iconPath = "RecipeSearch/resources/icon_flag_sweden.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Grekland":
                iconPath = "RecipeSearch/resources/icon_flag_greece.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Frankrike":
                iconPath = "RecipeSearch/resources/icon_flag_france.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Asien":
                iconPath = "RecipeSearch/resources/icon_flag_asia.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Afrika":
                iconPath = "RecipeSearch/resources/icon_flag_africa.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Indien":
                iconPath = "RecipeSearch/resources/icon_flag_india.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
        }
        return new Image(getClass().getClassLoader().getResourceAsStream( "RecipeSearch/resources/icon_flag_france.png"));
    }
    public Image getMainIngImage(String cuisine) {
        String iconPath;
        switch (cuisine) {
            case "Kött":
                iconPath = "RecipeSearch/resources/icon_main_meat.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Fisk":
                iconPath = "RecipeSearch/resources/icon_main_fish.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Kyckling":
                iconPath = "RecipeSearch/resources/icon_main_chicken.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Vegetarisk":
                iconPath = "RecipeSearch/resources/icon_main_veg.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
        }
        return new Image(getClass().getClassLoader().getResourceAsStream( "RecipeSearch/resources/icon_main_meat.png"));
    }
    public Image getDiffImage(String cuisine) {
        String iconPath;
        timeSelectedLabel.setText((int) Math.round(maxTimeSlider.getValue() / 10) * 10 + " Minuter");
        switch (cuisine) {
            case "Lätt":
                iconPath = "RecipeSearch/resources/icon_difficulty_easy.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Mellan":
                iconPath = "RecipeSearch/resources/icon_difficulty_medium.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Svår":
                iconPath = "RecipeSearch/resources/icon_difficulty_hard.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
        }
        return new Image(getClass().getClassLoader().getResourceAsStream( "RecipeSearch/resources/icon_main_meat.png"));
    }
    public String getTimeImage(int cuisine) {


        return  cuisine +  " Minuter";
    }

    @FXML
    public void closeButtonMouseEntered(){
        closeImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "RecipeSearch/resources/icon_close_hover.png")));
    }
    @FXML
    public void closeButtonMousePressed(){
        closeImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "RecipeSearch/resources/icon_close_pressed.png")));
        recipeDetailPane.toBack();
    }
    @FXML
    public void closeButtonMouseExited(){
        closeImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "RecipeSearch/resources/icon_close.png")));
    }
}
