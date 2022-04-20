package recipesearch;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import recipesearch.*;
import se.chalmers.ait.dat215.lab2.Recipe;

import java.io.IOException;

public class RecipeListItem extends AnchorPane {
    private RecipeSearchController parentController;
    private Recipe recipe;
    @FXML
    private Label nameItem;
    @FXML
    private ImageView imageItem;
    @FXML
    private ImageView mainIngImage;
    @FXML
    private ImageView difficultyImage;
    @FXML
    private Label timeLabel;
    @FXML
    private Label costLabel;
    @FXML
    private Label discLabel;
    @FXML private ImageView cusineImage;

    ///////////////////////////////////////




    public RecipeListItem(Recipe recipe, RecipeSearchController recipeSearchController) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("recipe_listitem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);


        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.recipe = recipe;
        this.parentController = recipeSearchController;
        this.imageItem.setImage(this.recipe.getFXImage());
        this.nameItem.setText(this.recipe.getName());
        this.cusineImage.setImage((this.parentController.getCuisineImage(recipe.getCuisine())));
        this.mainIngImage.setImage(this.parentController.getMainIngImage(recipe.getMainIngredient()));
        this.difficultyImage.setImage(this.parentController.getDiffImage(recipe.getDifficulty()));
        this.timeLabel.setText(this.parentController.getTimeImage(recipe.getTime()));
        this.costLabel.setText(recipe.getPrice() + "kr");
        this.discLabel.setText(recipe.getDescription());
        /////////////////

    }

    @FXML
    protected void onClick(Event event) {
        parentController.openRecipeView(recipe);
    }


}

