package recipesearch;

import se.chalmers.ait.dat215.lab2.*;

import java.util.List;

public class RecipeBackendController {
    String cuisine;
    String mainIngredient;
    String difficulty;
    int maxPrice = 0;
    int maxTime = 0;



    public List<Recipe> getRecipes(){

        RecipeDatabase db = RecipeDatabase.getSharedInstance();
         List<Recipe> recipes = db.search(new SearchFilter(this.difficulty,this.maxTime,this.cuisine,this.maxPrice,this.mainIngredient));
        return recipes;
    }
    public void setCuisine( String cuisine){
            this.cuisine = cuisine;
    }
    public void setMainIngredient(String mainIngredient){
            this.mainIngredient = mainIngredient;
    }
    public void setDifficulty(String difficulty){
            this.difficulty = difficulty;
    }
    public void setMaxPrice(int maxPrice){
        if (maxPrice > 0){
            this.maxPrice = maxPrice;
        }else{ this.maxPrice = 0;}

    }
    public void setMaxTime(int maxTime){
            this.maxTime = maxTime;
    }



}
